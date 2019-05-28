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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.yunchuang.im.MeService;
import cn.yunchuang.im.R;
import cn.yunchuang.im.SealConst;
import cn.yunchuang.im.event.RefreshWdyhDetailEvent;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.server.request.WdyhUpdateOrderStatusRequest;
import cn.yunchuang.im.server.response.BaseResponse;
import cn.yunchuang.im.server.response.GetUserDetailModelOne;
import cn.yunchuang.im.server.response.GetUserDetailOneResponse;
import cn.yunchuang.im.server.response.GetWdyhOrderDetailModel;
import cn.yunchuang.im.server.response.GetWdyhOrderDetailResponse;
import cn.yunchuang.im.server.response.SkillModel;
import cn.yunchuang.im.server.utils.CommonUtils;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.server.utils.json.JsonMananger;
import cn.yunchuang.im.server.widget.LoadDialog;
import cn.yunchuang.im.ui.widget.AgeSexView;
import cn.yunchuang.im.utils.DateUtils;
import cn.yunchuang.im.utils.DialogUtils;
import cn.yunchuang.im.utils.GlideUtils;
import cn.yunchuang.im.zmico.utils.BaseBaseUtils;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import cn.yunchuang.im.zmico.utils.ResourceUtils;
import cn.yunchuang.im.zmico.utils.Utils;
import me.leefeng.promptlibrary.PromptDialog;


public class WdyhDetailActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout titleLayout;
    private ImageView backImg;
    private TextView kefuTv;

    private TextView zhuangtaiTv;
    private TextView caozuoTv;

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

    private RoundedImageView portraitIv;
    private TextView nameTv;
    private AgeSexView ageSexView;
    private TextView ztTv;
    private TextView zjTv;
    private TextView yfkTv;
    private TextView yylxTv;
    private TextView yysjTv;
    private TextView yyscTv;
    private TextView yyddTv;


    private static final int GET_MSZT_ORDER_DETAIL = 1602;
    private static final int UPDATE_MSZT_ORDER_DETAIL = 1603;

    private PromptDialog loadingDialog;

    private String wdyhOrderId;

    private GetWdyhOrderDetailModel msztOrderModel = new GetWdyhOrderDetailModel();

    private WdyhUpdateOrderStatusRequest wdyhUpdateOrderStatusRequest = new WdyhUpdateOrderStatusRequest();

    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        BaseBaseUtils.setTranslucentStatus(this);//状态栏透明
        setContentView(R.layout.activity_wdxq_xq);
        setHeadVisibility(View.GONE);
        initView(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initView(intent);
    }

    private void initView(Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            wdyhOrderId = intent.getExtras().getString("wdyhOrderId");
        }

        titleLayout = (FrameLayout) findViewById(R.id.activity_wdxq_xq_title_layout);
        backImg = (ImageView) findViewById(R.id.activity_wdxq_xq_back);
        backImg.setOnClickListener(this);
        kefuTv = (TextView) findViewById(R.id.activity_wdxq_xq_kefu);
        kefuTv.setOnClickListener(this);

        zhuangtaiTv = (TextView) findViewById(R.id.activity_wdxq_xq_zhuangtai_tv);
        caozuoTv = (TextView) findViewById(R.id.activity_wdxq_xq_caozuo_tv);

        portraitIv = (RoundedImageView) findViewById(R.id.activity_wdxq_xq_portrait);
        nameTv = (TextView) findViewById(R.id.activity_wdxq_xq_nickname);
        ageSexView = (AgeSexView) findViewById(R.id.activity_wdxq_xq_age_sex_view);
        ztTv = (TextView) findViewById(R.id.activity_wdxq_xq_zt_tv);
        zjTv = (TextView) findViewById(R.id.activity_wdxq_xq_zj_tv);
        yfkTv = (TextView) findViewById(R.id.activity_wdxq_xq_yfk_tv);
        yylxTv = (TextView) findViewById(R.id.activity_wdxq_xq_yylx_tv);
        yysjTv = (TextView) findViewById(R.id.activity_wdxq_xq_yysj_tv);
        yyscTv = (TextView) findViewById(R.id.activity_wdxq_xq_yysc_tv);
        yyddTv = (TextView) findViewById(R.id.activity_wdxq_xq_yydd_tv);

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
//        request(GET_USER_DETAIL_ONE);//个人信息从查询订单详情表时，通过连表查询返回来了，这个请求用不到来
        request(GET_MSZT_ORDER_DETAIL);
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
            case R.id.activity_wdxq_xq_back:
                finish();
                break;
            case R.id.activity_wdxq_xq_kefu:

                break;
        }
    }


    //个人信息直接从查询订单表时，通过外键连表查询，返回来了，这个函数用不到来
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

    }

    private void updateMsztOrderDetail(GetWdyhOrderDetailResponse getWdyhOrderDetailResponse) {
        if (getWdyhOrderDetailResponse == null) {
            return;
        }
        GetWdyhOrderDetailModel model = getWdyhOrderDetailResponse.getResult();
        if (model == null) {
            return;
        }

        final boolean isPayUser = MeService.getUid().equals(model.getPayUserIdStr());
        boolean isReceiveUser = MeService.getUid().equals(model.getReceiveUserIdStr());
        //正常情况下，后端会做判断，不能同时是付款方和收款方，如果同时都是真的，当成只是付款方来处理
        //如果两者都不是，说明这个订单请求出错，弹个窗出来，不能再进行任何操作，必须退出
        //也就是，同时都是两者是有问题的，同时都不是两者也是有问题的，应该都弹个窗出来，不能进行任何操作，但用户此时已经付款，只能让他找客服了
        if (isPayUser && isReceiveUser) {
            //弹窗报错，退出，让用户找客服
            NToast.shortToast(this, "付款方和收款方相同，出错");
            return;
        } else if (!isPayUser && !isReceiveUser) {
            //弹窗报错，退出，让用户找客服
            NToast.shortToast(this, "既不是付款方也不是收款方，出错");
            return;
        }

        setUserDetail(model, isPayUser, isReceiveUser);

        msztOrderModel = model;
        SkillModel skillModel = null;
        try {
            skillModel = JsonMananger.jsonToBean(model.getYyxm(), SkillModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (skillModel != null) {
            ztTv.setText("主题：" + skillModel.getName());
        }
        zjTv.setText("总价¥" + model.getTotalPayment() + "元");
        yfkTv.setText("预付款¥" + model.getAdvancePayment() + "元");
        yylxTv.setText("预约类型：" + getOrderType(model.getOrderType(), isPayUser, isReceiveUser));
        yysjTv.setText("预约时间：" + DateUtils.getYMDHM(model.getYysj()));
        yyscTv.setText("预约时长：" + model.getYysc() + "小时");
        yyddTv.setText("预约地点：" + model.getYydd());

        statusToJindu(model.getStatus(), isPayUser, isReceiveUser);


    }

    //订单显示对方的姓名、性别、年龄、头像
    //根据自己是付款方，还是收款方来显示
    private void setUserDetail(GetWdyhOrderDetailModel model, boolean isPayUser, boolean isReceiveUser) {
        if (isPayUser) {
            GlideUtils.load(this, model.getReceiveUser().getPortraitUri(), portraitIv);
            nameTv.setText(model.getReceiveUser().getNickname());
            ageSexView.setVisibility(View.VISIBLE);
            ageSexView.setAgeAndSex(model.getReceiveUser().getAge(), model.getReceiveUser().getSex());
        }
        if (isReceiveUser) {
            GlideUtils.load(this, model.getPayUser().getPortraitUri(), portraitIv);
            nameTv.setText(model.getPayUser().getNickname());
            ageSexView.setVisibility(View.VISIBLE);
            ageSexView.setAgeAndSex(model.getPayUser().getAge(), model.getPayUser().getSex());
        }
    }

    //服务器返回的状态，转成相对应的进度
    //这里非常重要，后期一定要仔细测这一部分，考虑全面
    private void statusToJindu(int status, boolean isPayUser, boolean isReceiveUser) {
        if (status == 0) {
            setJindu(1);
            jdTvs.get(0).setText(SealConst.MSZT_ORDER_STRING_DFYFK);
            zhuangtaiTv.setText(SealConst.MSZT_ORDER_STRING_DFYFK);
            //付款方可以看到操作按钮：
            if (isPayUser) {
                caozuoTv.setVisibility(View.VISIBLE);
                caozuoTv.setText(SealConst.MSZT_ORDER_STRING_QFYFK);
                caozuoTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //去付预付款
                    }
                });
            }
            //收款方不可以看到按钮
            if (isReceiveUser) {
                caozuoTv.setVisibility(View.GONE);
            }
        } else if (status == 1) {
            setJindu(2);
            jdTvs.get(0).setText(SealConst.MSZT_ORDER_STRING_YFYFK);
            jdTvs.get(1).setText(SealConst.MSZT_ORDER_STRING_DJS);
            zhuangtaiTv.setText(SealConst.MSZT_ORDER_STRING_DJS);
            //付款方不可以看到操作按钮：
            if (isPayUser) {
                caozuoTv.setVisibility(View.GONE);
            }
            //收款方可以看到按钮
            if (isReceiveUser) {
                caozuoTv.setVisibility(View.VISIBLE);
                caozuoTv.setText(SealConst.MSZT_ORDER_STRING_JS);
                caozuoTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //接受
                        wdyhUpdateOrderStatusRequest = new WdyhUpdateOrderStatusRequest();
                        wdyhUpdateOrderStatusRequest.setWdyhOrderNum(msztOrderModel.getWdyhOrderId());
                        wdyhUpdateOrderStatusRequest.setStatus(SealConst.MSZT_ORDER_STATUS_DFQK);
                        wdyhUpdateOrderStatusRequest.setJsTs(System.currentTimeMillis() / 1000);
                        request(UPDATE_MSZT_ORDER_DETAIL);
                    }
                });
            }
        } else if (status == 2) {
            setJindu(3);
            jdTvs.get(0).setText(SealConst.MSZT_ORDER_STRING_YFYFK);
            jdTvs.get(1).setText(SealConst.MSZT_ORDER_STRING_YJS);
            jdTvs.get(2).setText(SealConst.MSZT_ORDER_STRING_DFQK);
            zhuangtaiTv.setText(SealConst.MSZT_ORDER_STRING_DFQK);
            //付款方可以看到操作按钮：
            if (isPayUser) {
                caozuoTv.setVisibility(View.VISIBLE);
                caozuoTv.setText(SealConst.MSZT_ORDER_STRING_QFQK);
                caozuoTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //去付全款
                        wdyhUpdateOrderStatusRequest = new WdyhUpdateOrderStatusRequest();
                        wdyhUpdateOrderStatusRequest.setWdyhOrderNum(msztOrderModel.getWdyhOrderId());
                        wdyhUpdateOrderStatusRequest.setStatus(SealConst.MSZT_ORDER_STATUS_DQR);
                        wdyhUpdateOrderStatusRequest.setJsTs(System.currentTimeMillis() / 1000);
                        request(UPDATE_MSZT_ORDER_DETAIL);
                    }
                });
            }
            //收款方不可以看到按钮
            if (isReceiveUser) {
                caozuoTv.setVisibility(View.GONE);
            }
        } else if (status == 3) {
            setJindu(4);
            jdTvs.get(0).setText(SealConst.MSZT_ORDER_STRING_YFYFK);
            jdTvs.get(1).setText(SealConst.MSZT_ORDER_STRING_YJS);
            jdTvs.get(2).setText(SealConst.MSZT_ORDER_STRING_YFQK);
            jdTvs.get(3).setText(SealConst.MSZT_ORDER_STRING_DQR);
            zhuangtaiTv.setText(SealConst.MSZT_ORDER_STRING_DQR);
            //付款方可以看到操作按钮：
            if (isPayUser) {
                caozuoTv.setVisibility(View.VISIBLE);
                caozuoTv.setText(SealConst.MSZT_ORDER_STRING_QR);
                caozuoTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //确认
                        wdyhUpdateOrderStatusRequest = new WdyhUpdateOrderStatusRequest();
                        wdyhUpdateOrderStatusRequest.setWdyhOrderNum(msztOrderModel.getWdyhOrderId());
                        wdyhUpdateOrderStatusRequest.setStatus(SealConst.MSZT_ORDER_STATUS_DPJ);
                        wdyhUpdateOrderStatusRequest.setJsTs(System.currentTimeMillis() / 1000);
                        request(UPDATE_MSZT_ORDER_DETAIL);
                    }
                });
            }
            //收款方不可以看到按钮
            if (isReceiveUser) {
                caozuoTv.setVisibility(View.GONE);
            }
        } else if (status == 4) {
            setJindu(5);
            jdTvs.get(0).setText(SealConst.MSZT_ORDER_STRING_YFYFK);
            jdTvs.get(1).setText(SealConst.MSZT_ORDER_STRING_YJS);
            jdTvs.get(2).setText(SealConst.MSZT_ORDER_STRING_YFQK);
            jdTvs.get(3).setText(SealConst.MSZT_ORDER_STRING_YQR);
            jdTvs.get(4).setText(SealConst.MSZT_ORDER_STRING_DPJ);
            zhuangtaiTv.setText(SealConst.MSZT_ORDER_STRING_DPJ);
            //付款方可以看到操作按钮：
            if (isPayUser) {
                caozuoTv.setVisibility(View.VISIBLE);
                caozuoTv.setText(SealConst.MSZT_ORDER_STRING_QPJ);
                caozuoTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //去评价
                        wdyhUpdateOrderStatusRequest = new WdyhUpdateOrderStatusRequest();
                        wdyhUpdateOrderStatusRequest.setWdyhOrderNum(msztOrderModel.getWdyhOrderId());
                        wdyhUpdateOrderStatusRequest.setStatus(SealConst.MSZT_ORDER_STATUS_WC);
                        wdyhUpdateOrderStatusRequest.setJsTs(System.currentTimeMillis() / 1000);
                        request(UPDATE_MSZT_ORDER_DETAIL);
                    }
                });
            }
            //收款方不可以看到按钮
            if (isReceiveUser) {
                caozuoTv.setVisibility(View.GONE);
            }
        } else if (status == 5) {
            setJindu(6);
            jdTvs.get(0).setText(SealConst.MSZT_ORDER_STRING_YFYFK);
            jdTvs.get(1).setText(SealConst.MSZT_ORDER_STRING_YJS);
            jdTvs.get(2).setText(SealConst.MSZT_ORDER_STRING_YFQK);
            jdTvs.get(3).setText(SealConst.MSZT_ORDER_STRING_YQR);
            jdTvs.get(4).setText(SealConst.MSZT_ORDER_STRING_YPJ);
            jdTvs.get(5).setText(SealConst.MSZT_ORDER_STRING_WC);
            zhuangtaiTv.setText(SealConst.MSZT_ORDER_STRING_WC);
            caozuoTv.setVisibility(View.GONE);
        }
    }

    private void resetJindu() {
        for (int i = 0; i < 6; i++) {
            dots.get(i).setBackgroundResource(R.drawable.bg_wdxq_dot_gray);
            jdTvs.get(i).setTextColor(ResourceUtils.getColor(R.color.color_E4E4E4));
        }
        jdTvs.get(0).setText(SealConst.MSZT_ORDER_STRING_DFYFK);
        jdTvs.get(1).setText(SealConst.MSZT_ORDER_STRING_DJS);
        jdTvs.get(2).setText(SealConst.MSZT_ORDER_STRING_DFQK);
        jdTvs.get(3).setText(SealConst.MSZT_ORDER_STRING_DQR);
        jdTvs.get(4).setText(SealConst.MSZT_ORDER_STRING_DPJ);
        jdTvs.get(5).setText(SealConst.MSZT_ORDER_STRING_WC);
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

    //订单类型
    private String getOrderType(int orderType, boolean isPayUser, boolean isReceiveUser) {
        if (orderType == SealConst.WDYH_ORDER_TYPE_MSZT) {//0是马上租Ta类型
            if (isPayUser) {
                return "我约Ta";
            } else {
                return "Ta约我";
            }
        } else {//1是发布报名类型
            if (isPayUser) {
                return "我的发布";
            } else {
                return "我的报名";
            }
        }
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case GET_MSZT_ORDER_DETAIL:
                return action.postWdyhGetOrderDetail(wdyhOrderId);
            case UPDATE_MSZT_ORDER_DETAIL:
                return action.postWdyhUpdateOrderStatus(wdyhUpdateOrderStatusRequest);
        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        DialogUtils.dimiss(loadingDialog);
        if (result != null) {
            switch (requestCode) {
                case GET_MSZT_ORDER_DETAIL:
                    GetWdyhOrderDetailResponse getWdyhOrderDetailResponse1 = (GetWdyhOrderDetailResponse) result;
                    if (getWdyhOrderDetailResponse1.getCode() == 200) {
                        updateMsztOrderDetail(getWdyhOrderDetailResponse1);
                    } else {
                        NToast.shortToast(mContext, "获取订单信息失败");
                    }
                    break;
                case UPDATE_MSZT_ORDER_DETAIL:
                    BaseResponse baseResponse = (BaseResponse) result;
                    if (baseResponse.getCode() == 200) {
                        request(GET_MSZT_ORDER_DETAIL);
                    } else {
                        NToast.shortToast(mContext, "更新订单信息失败");
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
            case GET_MSZT_ORDER_DETAIL:
                NToast.shortToast(mContext, "获取订单信息失败");
                break;
            case UPDATE_MSZT_ORDER_DETAIL:
                NToast.shortToast(mContext, "更新订单信息失败");
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshWdyhDetailEvent(RefreshWdyhDetailEvent event) {
        if (Utils.isNotNull(event)) {
            this.wdyhOrderId = event.getWdyhOrderId();
            getData();
        }
    }
}
