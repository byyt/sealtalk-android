package cn.yunchuang.im.ui.activity;


import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import java.text.DecimalFormat;

import cn.yunchuang.im.R;
import cn.yunchuang.im.event.SaveShaixuanEvent;
import cn.yunchuang.im.model.ShaixuanModel;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.ui.fragment.HomepageLikeFragment;
import cn.yunchuang.im.zmico.utils.BaseBaseUtils;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import cn.yunchuang.im.zmico.utils.ResourceUtils;
import cn.yunchuang.im.zmico.utils.Utils;


public class ShaixuanActivity extends BaseActivity implements View.OnClickListener {

    private NestedScrollView nestedScrollView;

    private FrameLayout titleLayout;
    private ImageView backImg;
    private ImageView saveIv;

    private TextView xbBuxianTv;
    private TextView nanTv;
    private TextView nvTv;

    private TextView ageLeftTv;
    private TextView ageRightTv;
    private RangeSeekBar ageSeekBar;

    private TextView heightLeftTv;
    private TextView heightRightTv;
    private RangeSeekBar heightSeekBar;

    private int xbSelected = 2;
    private int fromAge = 18;
    private int toAge = 500;
    private int fromHeight = 140;
    private int toHeight = 200;

    //从哪个fragment 进来的，保存筛选条件的时候，发个event，只更新该fragment数据，其他fragment保留筛选条件
    private String fromFragmentName = HomepageLikeFragment.TAG;
    private ShaixuanModel shaixuanModel = new ShaixuanModel();

    DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseBaseUtils.setTranslucentStatus(this);//状态栏透明
        setContentView(R.layout.activity_shaixuan);
        setHeadVisibility(View.GONE);
        initView();
    }

    private void initView() {
        decimalFormat = new DecimalFormat("0");//将浮点数转为精度为整数，内部四舍五入？
        if (getIntent() != null) {
            fromFragmentName = getIntent().getStringExtra("fromFragmentName");
            shaixuanModel = (ShaixuanModel) getIntent().getSerializableExtra("shaixuanModel");
            if (shaixuanModel != null) {
                xbSelected = shaixuanModel.getXbSelected();
                fromAge = shaixuanModel.getFromAge();
                toAge = shaixuanModel.getToAge();
                fromHeight = shaixuanModel.getFromHeight();
                toHeight = shaixuanModel.getToHeight();
            }
        }

        nestedScrollView = (NestedScrollView) findViewById(R.id.activity_shaixuan_root_scrollview);

        titleLayout = (FrameLayout) findViewById(R.id.activity_shaixuan_title_layout);
        backImg = (ImageView) findViewById(R.id.activity_shaixuan_back);
        backImg.setOnClickListener(this);
        saveIv = (ImageView) findViewById(R.id.activity_shaixuan_save);
        saveIv.setOnClickListener(this);

        xbBuxianTv = (TextView) findViewById(R.id.activity_shaixuan_xingbie_buxian);
        xbBuxianTv.setOnClickListener(this);
        nanTv = (TextView) findViewById(R.id.activity_shaixuan_xingbie_nan);
        nanTv.setOnClickListener(this);
        nvTv = (TextView) findViewById(R.id.activity_shaixuan_xingbie_nv);
        nvTv.setOnClickListener(this);

        ageLeftTv = (TextView) findViewById(R.id.activity_shaixuan_age_left_tv);
        ageRightTv = (TextView) findViewById(R.id.activity_shaixuan_age_right_tv);
        ageSeekBar = (RangeSeekBar) findViewById(R.id.activity_shaixuan_age_seekbar);
        ageSeekBar.setIndicatorTextDecimalFormat("0");//精度是整数
        ageSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                ageLeftTv.setText(decimalFormat.format(leftValue));
                ageRightTv.setText(decimalFormat.format(rightValue));
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }
        });
        ageSeekBar.setValue(18, 50);

        heightLeftTv = (TextView) findViewById(R.id.activity_shaixuan_height_left_tv);
        heightRightTv = (TextView) findViewById(R.id.activity_shaixuan_height_right_tv);
        heightSeekBar = (RangeSeekBar) findViewById(R.id.activity_shaixuan_height_seekbar);
        heightSeekBar.setIndicatorTextDecimalFormat("0");
        heightSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                heightLeftTv.setText(decimalFormat.format(leftValue));
                heightRightTv.setText(decimalFormat.format(rightValue));
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }
        });
        heightSeekBar.setValue(140, 200);

        initTitleLayout();
        initData();
        nestedScrollView.scrollTo(0, 0);
    }

    private void initTitleLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DeviceUtils.dpToPx(48) + DeviceUtils.getStatusBarHeightPixels(this));
        titleLayout.setLayoutParams(layoutParams);
    }

    private void initData() {
        xbSelected(xbSelected);
        ageLeftTv.setText(String.valueOf(fromAge));
        ageRightTv.setText(String.valueOf(toAge));
        ageSeekBar.setValue(fromAge, toAge);
        heightLeftTv.setText(String.valueOf(fromHeight));
        heightRightTv.setText(String.valueOf(toHeight));
        heightSeekBar.setValue(fromHeight, toHeight);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (Utils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.activity_shaixuan_back:
                finish();
                break;
            case R.id.activity_shaixuan_save:
                saveShaixuan();
                finish();
                break;
            case R.id.activity_shaixuan_xingbie_buxian:
                xbSelected(2);
                break;
            case R.id.activity_shaixuan_xingbie_nv:
                xbSelected(1);
                break;
            case R.id.activity_shaixuan_xingbie_nan:
                xbSelected(0);
                break;
        }
    }

    private void xbSelected(int selected) {
        xbSelected = selected;
        switch (selected) {
            case 2:
                xbBuxianTv.setBackground(ResourceUtils.getDrawable(R.drawable.bg_common_new_left_radius));
                xbBuxianTv.setTextColor(ResourceUtils.getColor(R.color.white));
                nvTv.setBackground(null);
                nvTv.setTextColor(ResourceUtils.getColor(R.color.color_FC6880));
                nanTv.setBackground(null);
                nanTv.setTextColor(ResourceUtils.getColor(R.color.color_FC6880));
                break;
            case 1:
                xbBuxianTv.setBackground(null);
                xbBuxianTv.setTextColor(ResourceUtils.getColor(R.color.color_FC6880));
                nvTv.setBackground(ResourceUtils.getDrawable(R.drawable.bg_common_new));
                nvTv.setTextColor(ResourceUtils.getColor(R.color.white));
                nanTv.setBackground(null);
                nanTv.setTextColor(ResourceUtils.getColor(R.color.color_FC6880));
                break;
            case 0:
                xbBuxianTv.setBackground(null);
                xbBuxianTv.setTextColor(ResourceUtils.getColor(R.color.color_FC6880));
                nvTv.setBackground(null);
                nvTv.setTextColor(ResourceUtils.getColor(R.color.color_FC6880));
                nanTv.setBackground(ResourceUtils.getDrawable(R.drawable.bg_common_new_right_radius));
                nanTv.setTextColor(ResourceUtils.getColor(R.color.white));
                break;
        }
    }

    private void saveShaixuan() {
        fromAge = Integer.valueOf(ageLeftTv.getText().toString());
        toAge = Integer.valueOf(ageRightTv.getText().toString());
        fromHeight = Integer.valueOf(heightLeftTv.getText().toString());
        toHeight = Integer.valueOf(heightRightTv.getText().toString());
        ShaixuanModel shaixuanModel = new ShaixuanModel();
        shaixuanModel.setXbSelected(xbSelected);
        shaixuanModel.setFromAge(fromAge);
        shaixuanModel.setToAge(toAge);
        shaixuanModel.setFromHeight(fromHeight);
        shaixuanModel.setToHeight(toHeight);

        SaveShaixuanEvent.postEvent(shaixuanModel, fromFragmentName);
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {

        }
        return super.doInBackground(requestCode, id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {

            }
        }
    }


    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {

        }
    }

}
