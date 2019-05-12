package cn.yunchuang.im.location;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.yunchuang.im.MeService;
import cn.yunchuang.im.R;
import cn.yunchuang.im.event.SaveDdxzEvent;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.utils.AMapUtil;
import cn.yunchuang.im.zmico.utils.BaseBaseUtils;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import cn.yunchuang.im.zmico.utils.Utils;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


/**
 * AMapV1地图中简单介绍poisearch搜索
 */
public class PoiKeywordSearchActivity extends FragmentActivity implements
        OnMarkerClickListener, InfoWindowAdapter, TextWatcher,
        OnClickListener, InputtipsListener {
    private AMap aMap;
    private EditText searchText;// 输入搜索关键字
    private String keyWord = "";// 要输入的poi搜索关键字

    private FrameLayout titleLayout;
    private ImageView backImg;
    private ImageView saveIv;

    private TextView dingweiTv;

    private RecyclerView recyclerView;

    private PoiAdapter poiAdapter;

    private LatLonPoint selectedPoint;//最终选择地点的经纬度
    private String selectedName;//选择地点的名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseBaseUtils.setTranslucentStatus(this);//状态栏透明
        setContentView(R.layout.activity_poikeywordsearch);
        init();
    }


    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.activity_ddxz_map)).getMap();
            setUpMap();
        }
    }

    /**
     * 设置页面监听
     */
    private void setUpMap() {
        searchText = (EditText) findViewById(R.id.activity_ddxz_et);
        searchText.addTextChangedListener(this);// 添加文本输入框监听事件
        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件

        titleLayout = (FrameLayout) findViewById(R.id.activity_ddxz_title_layout);
        backImg = (ImageView) findViewById(R.id.activity_ddxz_back);
        backImg.setOnClickListener(this);
        saveIv = (ImageView) findViewById(R.id.activity_ddxz_save);
        saveIv.setOnClickListener(this);

        dingweiTv = (TextView) findViewById(R.id.activity_ddxz_location_tv);
        dingweiTv.setText(MeService.getMyLocation().getCity());

        recyclerView = (RecyclerView) findViewById(R.id.activity_ddxz_inputlist);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        poiAdapter = new PoiAdapter(this);

        recyclerView.setAdapter(poiAdapter);

        initTitleLayout();

        //初始时，默认移动到定位所在的位置
        if (dingweiTv != null && dingweiTv.getText() != null
                && !(dingweiTv.getText().toString().trim().equals(""))
                && MeService.getMyLocation() != null) {
            keyWord = dingweiTv.getText().toString().trim();

            LatLng latLng = new LatLng(MeService.getMyLocation().getLatitude(),
                    MeService.getMyLocation().getLongitude());//定位到的位置
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            //清楚之前的标记
            clearAllMarker(aMap.getMapScreenMarkers());
            //添加标记
            aMap.addMarker(new MarkerOptions().position(latLng));
        }


    }

    private void initTitleLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DeviceUtils.dpToPx(48) + DeviceUtils.getStatusBarHeightPixels(this));
        titleLayout.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View v) {
        if (Utils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.activity_ddxz_back:
                finish();
                break;
            case R.id.activity_ddxz_save:
                saveDdxz();
                break;
        }
    }

    private void saveDdxz() {
        if (selectedPoint != null && !TextUtils.isEmpty(selectedName)) {
            SaveDdxzEvent.postEvent(selectedPoint, selectedName);
            finish();
        } else {
            NToast.shortToast(this, "还未选择地址，请重新选择");
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());

        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        return view;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        if (!AMapUtil.IsEmptyOrNullString(newText)) {
            recyclerView.setVisibility(View.VISIBLE);
            InputtipsQuery inputquery = new InputtipsQuery(newText, dingweiTv.getText().toString());
            inputquery.setCityLimit(true);
            Inputtips inputTips = new Inputtips(PoiKeywordSearchActivity.this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            poiAdapter.replaceData(tipList);

        } else {
            NToast.shortToast(this, String.valueOf(rCode));
        }

    }

    private void clearAllMarker(List<Marker> markerList) {
        if (Utils.isEmptyCollection(markerList)) {
            return;
        }
        for (Marker marker : markerList) {
            marker.remove();
        }
        markerList.clear();
    }

    public class PoiAdapter extends BaseQuickAdapter<Tip, BaseViewHolder> {

        private Context mContext;

        public PoiAdapter(Context context) {
            super(R.layout.item_layout);
            this.mContext = context;
        }


        @Override
        protected void convert(BaseViewHolder helper, final Tip item) {
            if (item == null) {
                return;
            }

            TextView field = helper.getView(R.id.poi_field_id);
            TextView value = helper.getView(R.id.poi_value_id);

            field.setText(item.getName());
            value.setText(item.getDistrict());

            LinearLayout rootLayout = helper.getView(R.id.poi_root_layout);
            rootLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (item.getPoint() == null) {
                        NToast.shortToast(PoiKeywordSearchActivity.this, "请选择一个确切的地点");
                        return;
                    }

                    selectedPoint = item.getPoint();
                    selectedName = item.getName().trim();

                    searchText.setText(selectedName);
                    searchText.setSelection(selectedName.length());

                    recyclerView.setVisibility(View.GONE);

                    LatLng latLng = new LatLng(item.getPoint().getLatitude(), item.getPoint().getLongitude());
                    //移动到该点
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    //清楚之前的标记
                    clearAllMarker(aMap.getMapScreenMarkers());
                    //添加标记
                    aMap.addMarker(new MarkerOptions().position(latLng).title(item.getName()).snippet(item.getAddress()));

                    //隐藏键盘
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                }
            });

        }

    }

}
