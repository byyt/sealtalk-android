package cn.yunchuang.im.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itheima.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import cn.yunchuang.im.R;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.server.response.GetMsztOrderModel;
import cn.yunchuang.im.server.response.GetMsztOrderResponse;
import cn.yunchuang.im.server.response.GetUserDetailModelOne;
import cn.yunchuang.im.server.response.GetUserDetailOneResponse;
import cn.yunchuang.im.server.response.SkillModel;
import cn.yunchuang.im.server.utils.CommonUtils;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.server.widget.LoadDialog;
import cn.yunchuang.im.ui.widget.AgeSexView;
import cn.yunchuang.im.utils.DialogUtils;
import cn.yunchuang.im.utils.GlideUtils;
import cn.yunchuang.im.zmico.utils.BaseBaseUtils;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import cn.yunchuang.im.zmico.utils.ResourceUtils;
import cn.yunchuang.im.zmico.utils.Utils;
import me.leefeng.promptlibrary.PromptDialog;


public class WodeXuqiuXqActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout titleLayout;
    private ImageView backImg;
    private TextView nextTv;

    private RoundedImageView portraitIv;
    private TextView nameTv;
    private AgeSexView ageSexView;

    private View dot1;
    private View dot2;
    private View dot3;
    private View dot4;
    private View dot5;
    private View dot6;
    private TextView jdTv1;
    private TextView jdTv2;
    private TextView jdTv3;
    private TextView jdTv4;
    private TextView jdTv5;
    private TextView jdTv6;
    private List<View> dots = new ArrayList<>(6);
    private List<TextView> jdTvs = new ArrayList<>(6);
    private View jinduLine1;
    private View jinduLine2;

    private static final int GET_USER_DETAIL_ONE = 1601;
    private static final int GET_MSZT_ORDER = 1602;

    private PromptDialog loadingDialog;
    private String userId = "";

    private SkillModel seletSkillModel;

    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseBaseUtils.setTranslucentStatus(this);//状态栏透明
        setContentView(R.layout.activity_wdxq_xq);
        setHeadVisibility(View.GONE);
        initView();
    }

    private void initView() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            userId = getIntent().getExtras().getString("userId");
            seletSkillModel = (SkillModel) getIntent().getExtras().getSerializable("skillModel");
        }

        titleLayout = (FrameLayout) findViewById(R.id.activity_xmxz_title_layout);
        backImg = (ImageView) findViewById(R.id.activity_xmxz_back);
        backImg.setOnClickListener(this);
        nextTv = (TextView) findViewById(R.id.activity_xmxz_next);
        nextTv.setOnClickListener(this);

        portraitIv = (RoundedImageView) findViewById(R.id.activity_wdxq_xq_portrait);
        nameTv = (TextView) findViewById(R.id.activity_wdxq_xq_nickname);
        ageSexView = (AgeSexView) findViewById(R.id.activity_wdxq_xq_age_sex_view);

        dot1 = (View) findViewById(R.id.activity_wdxq_xq_jd_1_dot);
        dot2 = (View) findViewById(R.id.activity_wdxq_xq_jd_2_dot);
        dot3 = (View) findViewById(R.id.activity_wdxq_xq_jd_3_dot);
        dot4 = (View) findViewById(R.id.activity_wdxq_xq_jd_4_dot);
        dot5 = (View) findViewById(R.id.activity_wdxq_xq_jd_5_dot);
        dot6 = (View) findViewById(R.id.activity_wdxq_xq_jd_6_dot);
        dots.add(dot1);
        dots.add(dot2);
        dots.add(dot3);
        dots.add(dot4);
        dots.add(dot5);
        dots.add(dot6);
        jdTv1 = (TextView) findViewById(R.id.activity_wdxq_xq_jd_1_tv);
        jdTv2 = (TextView) findViewById(R.id.activity_wdxq_xq_jd_2_tv);
        jdTv3 = (TextView) findViewById(R.id.activity_wdxq_xq_jd_3_tv);
        jdTv4 = (TextView) findViewById(R.id.activity_wdxq_xq_jd_4_tv);
        jdTv5 = (TextView) findViewById(R.id.activity_wdxq_xq_jd_5_tv);
        jdTv6 = (TextView) findViewById(R.id.activity_wdxq_xq_jd_6_tv);
        jdTvs.add(jdTv1);
        jdTvs.add(jdTv2);
        jdTvs.add(jdTv3);
        jdTvs.add(jdTv4);
        jdTvs.add(jdTv5);
        jdTvs.add(jdTv6);
        jinduLine1 = (View) findViewById(R.id.activity_wdxq_xq_red_line_1);
        jinduLine2 = (View) findViewById(R.id.activity_wdxq_xq_red_line_2);


        loadingDialog = DialogUtils.getLoadingDialog(this);

        initTitleLayout();
        resetJindu();

        screenWidth = DeviceUtils.getScreenWidthPixels(this);


        getData();
    }

    private void initTitleLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DeviceUtils.dpToPx(48) + DeviceUtils.getStatusBarHeightPixels(this));
        titleLayout.setLayoutParams(layoutParams);
    }


    private void getData() {
        DialogUtils.showLoading(loadingDialog);
        request(GET_USER_DETAIL_ONE);
        request(GET_MSZT_ORDER);
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
            case R.id.activity_xmxz_back:
                finish();
                break;
            case R.id.activity_xmxz_next:
                Intent intent = new Intent(mContext, YueTaMsytActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putSerializable("skillModel", seletSkillModel);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }


    private void updateDataOne(GetUserDetailOneResponse getUserDetailOneResponse) {
        if (getUserDetailOneResponse == null) {
            return;
        }
        GetUserDetailModelOne modelOne = getUserDetailOneResponse.getResult();
        if (modelOne == null) {
            return;
        }

        GlideUtils.load(this, modelOne.getPortraitUri(), portraitIv);
        nameTv.setVisibility(View.VISIBLE);
        nameTv.setText(modelOne.getNickname());
        ageSexView.setVisibility(View.VISIBLE);
        ageSexView.setAgeAndSex(modelOne.getAge(), modelOne.getSex());
        setJindu(5);

    }

    private void updateMsztOrderInfo(GetMsztOrderResponse getMsztOrderResponse) {
        if (getMsztOrderResponse == null) {
            return;
        }
        GetMsztOrderModel model = getMsztOrderResponse.getResult();
        if (model == null) {
            return;
        }


    }

    private void resetJindu() {
        for (int i = 0; i < 6; i++) {
            dots.get(i).setBackgroundResource(R.drawable.bg_wdxq_dot_gray);
            jdTvs.get(i).setTextColor(ResourceUtils.getColor(R.color.color_E4E4E4));
        }
        jinduLine1.setVisibility(View.GONE);
        jinduLine2.setVisibility(View.GONE);
    }

    private void setJindu(final int jindu) {
        resetJindu();
        if (jindu < 1) {
            return;
        }

        for (int i = 0; i < jindu; i++) {
            dots.get(i).setBackgroundResource(R.drawable.bg_wdxq_dot_red);
            jdTvs.get(i).setTextColor(ResourceUtils.getColor(R.color.color_FF6261));
        }
        if (jindu > 0 && jindu < 3) {
            jinduLine1.setVisibility(View.VISIBLE);
            setJinduRedLine(jinduLine1, dots.get(jindu - 1));
            jinduLine2.setVisibility(View.GONE);
        } else if (jindu == 3) {
            jinduLine1.setVisibility(View.VISIBLE);
            setJinduRedLineMatchParent(jinduLine1);
            jinduLine2.setVisibility(View.GONE);
        } else if (jindu > 3 && jindu < 6) {
            jinduLine1.setVisibility(View.VISIBLE);
            setJinduRedLineMatchParent(jinduLine1);
            jinduLine2.setVisibility(View.VISIBLE);
            setJinduRedLine(jinduLine2, dots.get(jindu - 1));
        } else {
            jinduLine1.setVisibility(View.VISIBLE);
            setJinduRedLineMatchParent(jinduLine1);
            jinduLine2.setVisibility(View.VISIBLE);
            setJinduRedLineMatchParent(jinduLine2);
        }
    }

    private void setJinduRedLine(View redLine, View dot) {
        if (Utils.isNull(redLine) || Utils.isNull(dot)) {
            return;
        }

        redLine.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        dot.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        int[] redLinePos = new int[2];
        int[] dotPos = new int[2];
        redLine.getLocationOnScreen(redLinePos);
        dot.getLocationOnScreen(dotPos);

        if (dotPos[0] == 0 || dotPos[1] == 0) {
            // not ready
            return;
        }

        int marginEnd = screenWidth - DeviceUtils.dpToPx(20) - dotPos[0];

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) redLine.getLayoutParams();
        lp.setMarginEnd(marginEnd);
        redLine.setLayoutParams(lp);

    }

    private void setJinduRedLineMatchParent(View redLine) {
        if (Utils.isNull(redLine)) {
            return;
        }

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) redLine.getLayoutParams();
        lp.setMarginEnd(0);
        redLine.setLayoutParams(lp);

    }


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case GET_USER_DETAIL_ONE:
                return action.getUserDetailOne(userId);
            case GET_MSZT_ORDER:
                return action.postMsztGetOrder();
        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        DialogUtils.dimiss(loadingDialog);
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
                case GET_MSZT_ORDER:
                    GetMsztOrderResponse getMsztOrderResponse = (GetMsztOrderResponse) result;
                    if (getMsztOrderResponse.getCode() == 200) {
                        updateMsztOrderInfo(getMsztOrderResponse);
                    } else {
                        NToast.shortToast(mContext, "获取订单信息失败");
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        DialogUtils.dimiss(loadingDialog);
        if (!CommonUtils.isNetworkConnected(mContext)) {
            LoadDialog.dismiss(mContext);
            NToast.shortToast(mContext, getString(R.string.network_not_available));
            return;
        }
        switch (requestCode) {
            case GET_USER_DETAIL_ONE:
                NToast.shortToast(mContext, "获取个人信息失败");
                break;
            case GET_MSZT_ORDER:
                NToast.shortToast(mContext, "获取订单信息失败");
                break;
        }
    }
}
