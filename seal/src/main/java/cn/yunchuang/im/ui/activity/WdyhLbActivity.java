package cn.yunchuang.im.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.yunchuang.im.R;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.server.response.GetUserDetailModelOne;
import cn.yunchuang.im.server.response.GetUserDetailOneResponse;
import cn.yunchuang.im.server.response.SkillModel;
import cn.yunchuang.im.server.utils.CommonUtils;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.server.widget.LoadDialog;
import cn.yunchuang.im.ui.adapter.WdyhLbPagerAdapter;
import cn.yunchuang.im.zmico.MicoTabLayout;
import cn.yunchuang.im.zmico.utils.BaseBaseUtils;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import cn.yunchuang.im.zmico.utils.Utils;


public class WdyhLbActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout titleLayout;
    private ImageView backImg;
    private TextView nextTv;

    private ViewPager viewPager;
    private WdyhLbPagerAdapter pagerAdapter;
    private MicoTabLayout tabLayout;

    private static final int GET_USER_DETAIL_ONE = 1602;

    private String userId = "";

    private SkillModel seletSkillModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseBaseUtils.setTranslucentStatus(this);//状态栏透明
        setContentView(R.layout.activity_wdyh_lb);
        setHeadVisibility(View.GONE);
        initView();
    }

    private void initView() {
        userId = getIntent().getStringExtra("userId");

        titleLayout = (FrameLayout) findViewById(R.id.activity_wdyh_lb_title_layout);
        backImg = (ImageView) findViewById(R.id.activity_wdyh_lb_back);
        backImg.setOnClickListener(this);
        nextTv = (TextView) findViewById(R.id.activity_wdyh_lb_next);
        nextTv.setOnClickListener(this);


        tabLayout = (MicoTabLayout) findViewById(R.id.activity_wdyh_lb_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.activity_wdyh_lb_view_pager);
        viewPager.setOffscreenPageLimit(3);

        pagerAdapter = new WdyhLbPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(MicoTabLayout.MODE_FIXED);

        initTitleLayout();
        getData();
    }

    private void initTitleLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DeviceUtils.dpToPx(48) + DeviceUtils.getStatusBarHeightPixels(this));
        titleLayout.setLayoutParams(layoutParams);
    }


    private void getData() {
        request(GET_USER_DETAIL_ONE);
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
            case R.id.activity_wdyh_lb_back:
                finish();
                break;
            case R.id.activity_wdyh_lb_next:
                Intent intent = new Intent(mContext, YueTaMsytActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putSerializable("skillModel", seletSkillModel);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    private void saveShaixuan() {

    }

    private void updateDataOne(GetUserDetailOneResponse getUserDetailOneResponse) {
        if (getUserDetailOneResponse == null) {
            return;
        }
        GetUserDetailModelOne modelOne = getUserDetailOneResponse.getResult();
        if (modelOne == null) {
            return;
        }


    }


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case GET_USER_DETAIL_ONE:
                return action.getUserDetailOne(userId);
        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case GET_USER_DETAIL_ONE:
                    GetUserDetailOneResponse getUserDetailOneResponse = (GetUserDetailOneResponse) result;
                    if (getUserDetailOneResponse.getCode() == 200) {
                        updateDataOne(getUserDetailOneResponse);
                    } else {
                        NToast.shortToast(mContext, "获取个人信息失败");
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        if (!CommonUtils.isNetworkConnected(mContext)) {
            LoadDialog.dismiss(mContext);
            NToast.shortToast(mContext, getString(R.string.network_not_available));
            return;
        }
        switch (requestCode) {
            case GET_USER_DETAIL_ONE:
                NToast.shortToast(mContext, "获取个人信息失败");
                break;
        }
    }
}
