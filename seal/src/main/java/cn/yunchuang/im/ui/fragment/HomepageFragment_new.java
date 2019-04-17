package cn.yunchuang.im.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.yunchuang.im.MeService;
import cn.yunchuang.im.R;
import cn.yunchuang.im.event.RefreshLocationEvent;
import cn.yunchuang.im.event.SaveShaixuanEvent;
import cn.yunchuang.im.location.LocateReqManager;
import cn.yunchuang.im.model.ShaixuanModel;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.ui.activity.ShaixuanActivity;
import cn.yunchuang.im.ui.adapter.HompagePagerAdapter;
import cn.yunchuang.im.zmico.utils.BaseBaseUtils;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import cn.yunchuang.im.zmico.utils.ResourceUtils;
import cn.yunchuang.im.zmico.utils.Utils;

/**
 * 首页
 */
public class HomepageFragment_new extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private TextView dingweiTv;
    private ImageView shaixuanIv;
    private TextView jlzjTv;
    private TextView cnxhTv;
    private TextView hpyxTv;

    private HompagePagerAdapter hompagePagerAdapter;
    private ViewPager viewPager;

    private ShaixuanModel shaixuanModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_homepage_new, container, false);
        initView(view);
        return view;
    }

    @Override
    public void fragmentShow() {
        super.fragmentShow();

    }

    private void initView(View view) {
        //启动页会进行第一次定位，如果定位成功，会保存定位成功的时间，这里就会直接用sp保存的数据
        //点击附近的人会再次请求定位，如果发现2分钟内已经定位成功过，则直接使用sp保存的定位结果，不再进行定位
        if (getActivity() != null && isAdded()) {
            LocateReqManager.sendRequestLocation(getActivity(), HomepageFragment_new.class.getSimpleName(), false);
        }

        //设置标题栏高度，还有状态栏透明
        if (getActivity() != null) {
            int topLayoutHeight = DeviceUtils.getStatusBarHeightPixels(getActivity()) + DeviceUtils.dpToPx(81);
            FrameLayout titleLayout = (FrameLayout) view.findViewById(R.id.homepage_new_title_root_layout);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    topLayoutHeight);
            titleLayout.setLayoutParams(layoutParams);
            BaseBaseUtils.setTranslucentStatus(getActivity());//状态栏透明
        }

        dingweiTv = view.findViewById(R.id.homepage_new_title_location_tv);
        dingweiTv.setOnClickListener(this);
        shaixuanIv = view.findViewById(R.id.homepage_new_title_shaixuan_iv);
        shaixuanIv.setOnClickListener(this);
        jlzjTv = view.findViewById(R.id.homepage_new_title_jlzj_tab);
        jlzjTv.setOnClickListener(this);
        cnxhTv = view.findViewById(R.id.homepage_new_title_cnxh_tab);
        cnxhTv.setOnClickListener(this);
        hpyxTv = view.findViewById(R.id.homepage_new_title_hpyx_tab);
        hpyxTv.setOnClickListener(this);

        viewPager = view.findViewById(R.id.homepage_new_view_pager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(this);
        hompagePagerAdapter = new HompagePagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(hompagePagerAdapter);
        viewPager.setCurrentItem(1);

        //定位位置，默认采用sp保存的，会进行定位请求来更新这个值
        dingweiTv.setText(MeService.getMyLocation().getCity());


    }

    @Override
    public void onClick(View v) {
        if (Utils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.homepage_new_title_location_tv:
                break;
            case R.id.homepage_new_title_shaixuan_iv:
                startShaixuanActivity();
                break;
            case R.id.homepage_new_title_jlzj_tab:
                viewPager.setCurrentItem(0);
                break;
            case R.id.homepage_new_title_cnxh_tab:
                viewPager.setCurrentItem(1);
                break;
            case R.id.homepage_new_title_hpyx_tab:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        setTabStyle(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void setTabStyle(int position) {
        if (position == 0) {
            jlzjTv.setTextColor(ResourceUtils.getColor(R.color.white));
            jlzjTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            cnxhTv.setTextColor(ResourceUtils.getColor(R.color.color_D9D6DE));
            cnxhTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            hpyxTv.setTextColor(ResourceUtils.getColor(R.color.color_D9D6DE));
            hpyxTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        } else if (position == 1) {
            jlzjTv.setTextColor(ResourceUtils.getColor(R.color.color_D9D6DE));
            jlzjTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);//注意新的TextView.setTextSize默认单位就是sp
            cnxhTv.setTextColor(ResourceUtils.getColor(R.color.white));
            cnxhTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            hpyxTv.setTextColor(ResourceUtils.getColor(R.color.color_D9D6DE));
            hpyxTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        } else {
            jlzjTv.setTextColor(ResourceUtils.getColor(R.color.color_D9D6DE));
            jlzjTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            cnxhTv.setTextColor(ResourceUtils.getColor(R.color.color_D9D6DE));
            cnxhTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            hpyxTv.setTextColor(ResourceUtils.getColor(R.color.white));
            hpyxTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        }
    }

    private void startShaixuanActivity() {
        Intent intent = new Intent(getActivity(), ShaixuanActivity.class);
        String currentFragmentName = getCurrentFragmentName();
        intent.putExtra("fromFragmentName", currentFragmentName);
        if (shaixuanModel == null) {
            shaixuanModel = new ShaixuanModel();
            shaixuanModel.setXbSelected(2);
            shaixuanModel.setFromAge(18);
            shaixuanModel.setToAge(50);
            shaixuanModel.setFromHeight(140);
            shaixuanModel.setToHeight(200);
        }
        intent.putExtra("shaixuanModel", shaixuanModel);
        startActivity(intent);
    }

    private String getCurrentFragmentName() {
        String fragmentName = HomepageLikeFragment.TAG;
        switch (viewPager.getCurrentItem()) {
            case 0:
                fragmentName = HomepageNearByFragment.TAG;
                break;
            case 1:
                fragmentName = HomepageLikeFragment.TAG;
                break;
            case 2:
                fragmentName = HomepageRateFragment.TAG;
                break;
        }
        return fragmentName;
    }

    public ShaixuanModel getShaixuanModel() {
        return shaixuanModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshLocationEvent(RefreshLocationEvent event) {
        //只处理本类发起的定位请求
        if (Utils.isNotNull(event) && event.getLocateSender().equals(HomepageFragment_new.class.getSimpleName())) {
            Log.d("xxxxxx", "onRefreshLocationEvent homepage_new");
            if (Utils.isNotNull(event) && event.getLocationVO() != null) {
                if (event.isLocationSuccess()) {
                    if (dingweiTv != null) {
                        dingweiTv.setText(event.getLocationVO().getCity());
                    }
                } else {
                    if (getActivity() != null && isAdded()) {
                        NToast.shortToast(getActivity(), "定位失败");
                    }
                }
            } else {
                if (getActivity() != null && isAdded()) {
                    NToast.shortToast(getActivity(), "定位失败");
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveShaixuanEvent(SaveShaixuanEvent event) {
        //只处理本类发起的定位请求
        if (Utils.isNotNull(event)) {
            shaixuanModel = event.getShaixuanModel();
        }
    }

}
