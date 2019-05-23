package cn.yunchuang.im.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.services.core.LatLonPoint;
import com.itheima.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.WheelPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.widget.WheelView;
import cn.yunchuang.im.R;
import cn.yunchuang.im.SealAppContext;
import cn.yunchuang.im.SealConst;
import cn.yunchuang.im.event.SaveDdxzEvent;
import cn.yunchuang.im.location.PoiKeywordSearchActivity;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.server.request.WdyhCreateOrderRequest;
import cn.yunchuang.im.server.response.BaseResponse;
import cn.yunchuang.im.server.response.GetUserDetailModelOne;
import cn.yunchuang.im.server.response.GetUserDetailOneResponse;
import cn.yunchuang.im.server.response.GetWdyhOrderDetailResponse;
import cn.yunchuang.im.server.response.SkillModel;
import cn.yunchuang.im.server.utils.CommonUtils;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.server.widget.LoadDialog;
import cn.yunchuang.im.server.widget.TriplePicker;
import cn.yunchuang.im.ui.widget.AgeSexView;
import cn.yunchuang.im.utils.DateUtils;
import cn.yunchuang.im.utils.DialogUtils;
import cn.yunchuang.im.utils.GlideUtils;
import cn.yunchuang.im.zmico.utils.BaseBaseUtils;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import cn.yunchuang.im.zmico.utils.ResourceUtils;
import cn.yunchuang.im.zmico.utils.Utils;
import me.leefeng.promptlibrary.PromptDialog;

import static cn.yunchuang.im.SealConst.ZFFS_QB;
import static cn.yunchuang.im.SealConst.ZFFS_WX;


public class YueTaMsytActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout titleLayout;
    private TextView titleTv;
    private ImageView backImg;

    private RoundedImageView portraitIv;
    private TextView nameTv;
    private AgeSexView ageSexView;
    private TextView distanceTv;

    private TextView skillNameTv;
    private FrameLayout skillLayout;
    private TextView skillPriceTv;
    private TextView skillDescTv;
    private LinearLayout yyShijianLayout;
    private TextView yyShijianTv;
    private LinearLayout yyShichangLayout;
    private TextView yyShichangTv;
    private LinearLayout yyDidianLayout;
    private TextView yyDidianTv;

    private TextView yfkTv;
    private TextView zjTv;
    private LinearLayout qbzfLayout;
    private TextView qbzfTv;
    private ImageView qbzfIv;
    private LinearLayout wxzfLayout;
    private ImageView wxzfIv;
    private TextView msytTv;

    private static final int GET_USER_DETAIL_ONE = 1601;
    private static final int MSZT_PAY = 1602;
    private static final int MSZT_CREATE_ORDER = 1603;

    private PromptDialog loadingDialog;
    private String userId = "";

    private SkillModel seletSkillModel = new SkillModel();

    private String yysjYear;
    private String yysjMonth;
    private String yysjDate;
    private String yysjHour;
    private String yysjMinute;
    private long yysjTs;
    private int yysc;
    private LatLonPoint yyddPoint; //最终选择地点的经纬度
    private String yydd; //选择地点的名称

    private boolean isYysjTsZq = false; //预约时间是否选择正确
    private boolean isYyscZq = false; //预约时长是否选择正确
    private boolean isYyddZq = false; //预约地点是否选择正确

    private int advancePayment = 0; //预付款金额
    private int totalPayment = 0;  //总金额

    private long lastClickTime;

    private int currentZffs = ZFFS_QB;//当前支付方式

    private WdyhCreateOrderRequest orderRequest = new WdyhCreateOrderRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        BaseBaseUtils.setTranslucentStatus(this);//状态栏透明
        setContentView(R.layout.activity_yue_ta_msyt);
        setHeadVisibility(View.GONE);
        initView();
        SealAppContext.getInstance().pushActivity(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        SealAppContext.getInstance().popActivity(this);
        super.onDestroy();
    }

    private void initView() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            userId = getIntent().getExtras().getString("userId");
            seletSkillModel = (SkillModel) getIntent().getExtras().getSerializable("skillModel");
        }

        titleLayout = (FrameLayout) findViewById(R.id.activity_msyt_title_layout);
        titleTv = (TextView) findViewById(R.id.activity_msyt_title_tv);
        titleTv.setText("约Ta" + seletSkillModel.getName());
        backImg = (ImageView) findViewById(R.id.activity_msyt_back);
        backImg.setOnClickListener(this);

        portraitIv = (RoundedImageView) findViewById(R.id.activity_msyt_portrait);
        nameTv = (TextView) findViewById(R.id.activity_msyt_nickname);
        ageSexView = (AgeSexView) findViewById(R.id.activity_msyt_age_sex_view);
        distanceTv = (TextView) findViewById(R.id.activity_msyt_distance_tv);

        skillLayout = (FrameLayout) findViewById(R.id.activity_msyt_skill_layout);
        skillLayout.setOnClickListener(this);
        skillNameTv = (TextView) findViewById(R.id.activity_msyt_skill_name_tv);
        skillNameTv.setText(seletSkillModel.getName());
        skillPriceTv = (TextView) findViewById(R.id.activity_msyt_skill_price_tv);
        skillPriceTv.setText(seletSkillModel.getPrice() + "元／小时");
        skillDescTv = (TextView) findViewById(R.id.activity_msyt_skill_decs_tv);
        skillDescTv.setText(seletSkillModel.getDesc());
        yyShijianLayout = (LinearLayout) findViewById(R.id.activity_msyt_yuyue_shijian_layout);
        yyShijianLayout.setOnClickListener(this);
        yyShijianTv = (TextView) findViewById(R.id.activity_msyt_yuyue_shijian_tv);
        yyShichangLayout = (LinearLayout) findViewById(R.id.activity_msyt_yuyue_shichang_layout);
        yyShichangLayout.setOnClickListener(this);
        yyShichangTv = (TextView) findViewById(R.id.activity_msyt_yuyue_shichang_tv);
        yyDidianLayout = (LinearLayout) findViewById(R.id.activity_msyt_yuyue_didian_layout);
        yyDidianLayout.setOnClickListener(this);
        yyDidianTv = (TextView) findViewById(R.id.activity_msyt_yuyue_didian_tv);

        yfkTv = (TextView) findViewById(R.id.activity_msyt_yfk_tv);
        zjTv = (TextView) findViewById(R.id.activity_msyt_zj_tv);
        qbzfLayout = (LinearLayout) findViewById(R.id.activity_msyt_qbzf_layout);
        qbzfLayout.setOnClickListener(this);
        qbzfTv = (TextView) findViewById(R.id.activity_msyt_qbzf_tv);
        qbzfIv = (ImageView) findViewById(R.id.activity_msyt_qbzf_iv);
        wxzfLayout = (LinearLayout) findViewById(R.id.activity_msyt_wxzf_layout);
        wxzfLayout.setOnClickListener(this);
        wxzfIv = (ImageView) findViewById(R.id.activity_msyt_wxzf_iv);

        msytTv = (TextView) findViewById(R.id.activity_msyt_btn);
        msytTv.setOnClickListener(this);

        loadingDialog = DialogUtils.getLoadingDialog(this);

        initTitleLayout();
        setZffs(currentZffs);
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
            case R.id.activity_msyt_back:
                finish();
                break;
            case R.id.activity_msyt_skill_layout:
                finish();
                break;
            case R.id.activity_msyt_yuyue_shijian_layout:
                onYysjPicker();
                break;
            case R.id.activity_msyt_yuyue_shichang_layout:
                onYyscPicker();
                break;
            case R.id.activity_msyt_yuyue_didian_layout:
                Intent intent2 = new Intent(YueTaMsytActivity.this, PoiKeywordSearchActivity.class);
                startActivity(intent2);
                break;
            case R.id.activity_msyt_qbzf_layout:
                setZffs(ZFFS_QB);
                break;
            case R.id.activity_msyt_wxzf_layout:
                setZffs(ZFFS_WX);
                break;
            case R.id.activity_msyt_btn:
                if (isFastClickOneMinute()) {
                    //一秒内点击了多次
                    NToast.shortToast(YueTaMsytActivity.this, "请不要重复支付");
                    return;
                }
                startPay();
                break;
        }
    }

    // 是否是过快点击，一秒内点击支付两次，
    private boolean isFastClickOneMinute() {
        long time = System.currentTimeMillis();
        if (lastClickTime < time && time - lastClickTime <= 1100) {
            return true;
        } else {
            lastClickTime = time;
            return false;
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
        distanceTv.setVisibility(View.VISIBLE);
        distanceTv.setText(MessageFormat.format("{0}{1}", String.format(Locale.getDefault(),
                "%.2f", modelOne.getDistance()), "km"));

    }

    private void setZffs(int zffs) {
        if (zffs == ZFFS_QB) {
            qbzfIv.setVisibility(View.VISIBLE);
            wxzfIv.setVisibility(View.GONE);
            currentZffs = ZFFS_QB;
        } else {
            qbzfIv.setVisibility(View.GONE);
            wxzfIv.setVisibility(View.VISIBLE);
            currentZffs = ZFFS_WX;
        }
    }

    private void startPay() {
        if (TextUtils.isEmpty(userId) || seletSkillModel == null) {
            NToast.shortToast(this, "用户信息出错，请重新选择");
            return;
        }
        if (!isYysjTsZq) {
            NToast.shortToast(this, "预约时间还未选择或选择错误，请重新选择");
            return;
        }
        if (!isYyscZq) {
            NToast.shortToast(this, "预约时长还未选择，请重新选择");
            return;
        }
        if (!isYyddZq) {
            NToast.shortToast(this, "预约地点还未选择或选择错误，请重新选择");
            return;
        }
        request(MSZT_PAY);
    }

    private void startCreateOrder() {
        orderRequest.setOrderType(SealConst.WDYH_ORDER_TYPE_MSZT);
        orderRequest.setReceiveUserId(userId);
        orderRequest.setStatus(SealConst.MSZT_ORDER_STATUS_DJS);
        orderRequest.setYyxm(JSONObject.toJSONString(seletSkillModel));
        orderRequest.setYysj(yysjTs);
        orderRequest.setYysc(yysc);
        orderRequest.setLongitude(yyddPoint.getLongitude());
        orderRequest.setLatitude(yyddPoint.getLatitude());
        orderRequest.setYydd(yydd);
        orderRequest.setAdvancePayment(advancePayment);
        orderRequest.setTotalPayment(totalPayment);
        orderRequest.setZffs(currentZffs);
        orderRequest.setYfkTs(System.currentTimeMillis() / 1000);
        request(MSZT_CREATE_ORDER);
    }

    public void onYysjPicker() {
        final ArrayList<String> dates = new ArrayList<>();
        //如果当前已经23点以后，则不能选择今天
        //默认是尽快，时间是当前时间三小时之后
        int hour = DateUtils.getCurrentHour();
        if (hour != 23) {
            dates.add("尽快");
            dates.add("今天");
            dates.add("明天");
            dates.add("后天");
        } else {
            dates.add("尽快");
            dates.add("明天");
            dates.add("后天");
        }
        long currentTs = Calendar.getInstance().getTimeInMillis() / 1000;
        String hourLater = DateUtils.getHourHoursLater(currentTs, 3);
        String minute = DateUtils.getMinute(currentTs);
        ArrayList<String> hours = new ArrayList<>();
        ArrayList<String> minutes = new ArrayList<>();
        hours.add(hourLater);
        minutes.add(minute);
        final TriplePicker picker = new TriplePicker(this, dates, hours, minutes);
        setPickerStyle(picker);
        picker.setTitleText("预约时间");
        picker.setFirstLabel("", "");
        picker.setSecondLabel("", ":");
        picker.setThirdLabel("", "");
        picker.setOnPickListener(new TriplePicker.OnPickListener() {
            @Override
            public void onPicked(int selectedFirstIndex, int selectedSecondIndex, int selectedThirdIndex) {
                if (picker.getSelectedFirstItem().equals("尽快")) {
                    long currentTs = Calendar.getInstance().getTimeInMillis() / 1000;
                    yysjYear = DateUtils.getYearHoursLater(currentTs, 3);
                    yysjMonth = DateUtils.getMonthHoursLater(currentTs, 3);
                    yysjDate = DateUtils.getDateHoursLater(currentTs, 3);
                    yysjHour = picker.getSelectedSecondItem();
                    yysjMinute = picker.getSelectedThirdItem();
                    yysjTs = DateUtils.date2TimeStamp(yysjYear + "-" + yysjMonth + "-" + yysjDate + " "
                                    + yysjHour + ":" + yysjMinute,
                            "yyyy-MM-dd HH:mm");
                    //确保日期不出错，最后确定的时间必须大于当前时间
                    if (yysjTs <= currentTs) {
                        NToast.shortToast(YueTaMsytActivity.this, "选择时间必须大于当前时间");
                        //租不可以点
                        isYysjTsZq = false;
                    } else {
                        yyShijianTv.setText("尽快 " + yysjYear + "-" + yysjMonth + "-" + yysjDate + " " +
                                picker.getSelectedSecondItem() + ":" + picker.getSelectedThirdItem() + "前");
                        //租可以点击
                        isYysjTsZq = true;
                    }
                } else if (picker.getSelectedFirstItem().equals("今天")) {
                    long currentTs = Calendar.getInstance().getTimeInMillis() / 1000;
                    yysjYear = DateUtils.getYearHoursLater(currentTs, 0);
                    yysjMonth = DateUtils.getMonthHoursLater(currentTs, 0);
                    yysjDate = DateUtils.getDateHoursLater(currentTs, 0);
                    yysjHour = picker.getSelectedSecondItem();
                    yysjMinute = picker.getSelectedThirdItem();
                    yysjTs = DateUtils.date2TimeStamp(yysjYear + "-" + yysjMonth + "-" + yysjDate + " "
                                    + yysjHour + ":" + yysjMinute,
                            "yyyy-MM-dd HH:mm");
                    //确保日期不出错，最后确定的时间必须大于当前时间
                    if (yysjTs <= currentTs) {
                        NToast.shortToast(YueTaMsytActivity.this, "选择时间必须大于当前时间");
                        //租不可以点
                        isYysjTsZq = false;
                    } else {
                        yyShijianTv.setText(yysjYear + "-" + yysjMonth + "-" + yysjDate + " " +
                                picker.getSelectedSecondItem() + ":" + picker.getSelectedThirdItem());
                        //租可以点击
                        isYysjTsZq = true;
                    }
                } else if (picker.getSelectedFirstItem().equals("明天")) {
                    long currentTs = Calendar.getInstance().getTimeInMillis() / 1000;
                    yysjYear = DateUtils.getYearHoursLater(currentTs, 24);
                    yysjMonth = DateUtils.getMonthHoursLater(currentTs, 24);
                    yysjDate = DateUtils.getDateHoursLater(currentTs, 24);
                    yysjHour = picker.getSelectedSecondItem();
                    yysjMinute = picker.getSelectedThirdItem();
                    yysjTs = DateUtils.date2TimeStamp(yysjYear + "-" + yysjMonth + "-" + yysjDate + " "
                                    + yysjHour + ":" + yysjMinute,
                            "yyyy-MM-dd HH:mm");
                    //确保日期不出错，最后确定的时间必须大于当前时间
                    if (yysjTs <= currentTs) {
                        NToast.shortToast(YueTaMsytActivity.this, "选择时间必须大于当前时间");
                        //租不可以点
                        isYysjTsZq = false;
                    } else {
                        yyShijianTv.setText(yysjYear + "-" + yysjMonth + "-" + yysjDate + " " +
                                picker.getSelectedSecondItem() + ":" + picker.getSelectedThirdItem());
                        //租可以点击
                        isYysjTsZq = true;
                    }
                } else {
                    long currentTs = Calendar.getInstance().getTimeInMillis() / 1000;
                    yysjYear = DateUtils.getYearHoursLater(currentTs, 48);
                    yysjMonth = DateUtils.getMonthHoursLater(currentTs, 48);
                    yysjDate = DateUtils.getDateHoursLater(currentTs, 48);
                    yysjHour = picker.getSelectedSecondItem();
                    yysjMinute = picker.getSelectedThirdItem();
                    yysjTs = DateUtils.date2TimeStamp(yysjYear + "-" + yysjMonth + "-" + yysjDate + " "
                                    + yysjHour + ":" + yysjMinute,
                            "yyyy-MM-dd HH:mm");
                    //确保日期不出错，最后确定的时间必须大于当前时间
                    if (yysjTs <= currentTs) {
                        NToast.shortToast(YueTaMsytActivity.this, "选择时间必须大于当前时间");
                        //租不可以点
                        isYysjTsZq = false;
                    } else {
                        yyShijianTv.setText(yysjYear + "-" + yysjMonth + "-" + yysjDate + " " +
                                picker.getSelectedSecondItem() + ":" + picker.getSelectedThirdItem());
                        //租可以点击
                        isYysjTsZq = true;
                    }
                }
            }
        });
        picker.setOnWheelListener(new TriplePicker.OnWheelListener() {
            @Override
            public void onFirstWheeled(int index, String item) {
                if (index == 0) {
                    //选的尽快，则默认选择3小时之后，
                    long currentTs = Calendar.getInstance().getTimeInMillis() / 1000;
                    String hourLater = DateUtils.getHourHoursLater(currentTs, 3);
                    String minute = DateUtils.getMinute(currentTs);
                    ArrayList<String> hours = new ArrayList<>();
                    ArrayList<String> minutes = new ArrayList<>();
                    hours.add(hourLater);
                    minutes.add(minute);
                    picker.setSecondData(hours, 0);
                    picker.setThirdData(minutes, 0);
                } else if (index == 1 && dates.get(1).equals("今天")) {
                    //选的今天，下一个小时开始，如果当前已经是23点，不会有今天选择，直接选择明天
                    int hour = DateUtils.getCurrentHour();
                    int minute = DateUtils.getCurrentMinute();
                    ArrayList<String> hours = new ArrayList<>();
                    for (int i = hour + 1; i < 24; i++) {
                        hours.add(DateUtils.fillZero(i));
                    }
                    ArrayList<String> minutes = new ArrayList<>();
                    for (int i = 0; i < 60; i++) {
                        minutes.add(DateUtils.fillZero(i));
                    }
                    picker.setSecondData(hours, 0);
                    picker.setThirdData(minutes, minute);
                } else if (index == 1 && dates.get(1).equals("明天")) {
                    //当前是23点的情况，没有今天，第二个就是明天，从00点开始
                    int hour = 0;
                    int minute = DateUtils.getCurrentMinute();
                    ArrayList<String> hours = new ArrayList<>();
                    for (int i = 0; i < 24; i++) {
                        hours.add(DateUtils.fillZero(i));
                    }
                    ArrayList<String> minutes = new ArrayList<>();
                    for (int i = 0; i < 60; i++) {
                        minutes.add(DateUtils.fillZero(i));
                    }
                    picker.setSecondData(hours, hour);
                    picker.setThirdData(minutes, minute);
                } else if (index == 2 && dates.get(1).equals("明天")) {
                    //当前不是23点的情况，既有今天，也有明天，默认选择14:00
                    int hour = 14;
                    int minute = 0;
                    ArrayList<String> hours = new ArrayList<>();
                    for (int i = 0; i < 24; i++) {
                        hours.add(DateUtils.fillZero(i));
                    }
                    ArrayList<String> minutes = new ArrayList<>();
                    for (int i = 0; i < 60; i++) {
                        minutes.add(DateUtils.fillZero(i));
                    }
                    picker.setSecondData(hours, hour);
                    picker.setThirdData(minutes, minute);
                } else {
                    //后天与上面的明天同理
                    int hour = 14;
                    int minute = 0;
                    ArrayList<String> hours = new ArrayList<>();
                    for (int i = 0; i < 24; i++) {
                        hours.add(DateUtils.fillZero(i));
                    }
                    ArrayList<String> minutes = new ArrayList<>();
                    for (int i = 0; i < 60; i++) {
                        minutes.add(DateUtils.fillZero(i));
                    }
                    picker.setSecondData(hours, hour);
                    picker.setThirdData(minutes, minute);
                }
            }

            @Override
            public void onSecondWheeled(int index, String item) {

            }

            @Override
            public void onThirdWheeled(int index, String item) {

            }
        });
        picker.show();
    }

    private void onYyscPicker() {
        final String[] shiChangArray = new String[]{"1小时", "2小时", "3小时", "4小时", "5小时", "6小时",
                "7小时", "8小时", "9小时", "10小时", "11小时", "12小时"};
        OptionPicker picker = new OptionPicker(this, shiChangArray);
        setPickerStyle(picker);
        picker.setTitleText("预约时长");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                if (seletSkillModel == null) {
                    NToast.shortToast(YueTaMsytActivity.this, "用户信息出错，请重新选择");
                    isYyscZq = false;
                    return;
                }
                yyShichangTv.setText(shiChangArray[index]);
                yysc = index + 1;
                advancePayment = yysc * seletSkillModel.getPrice() * 3 / 10;
                totalPayment = yysc * seletSkillModel.getPrice() * 11 / 10;
                yfkTv.setText("¥" + advancePayment + "元");
                zjTv.setText("¥" + totalPayment + "元");
                isYyscZq = true;
            }
        });
        picker.setSelectedItem("2小时");
        picker.show();
    }

    private void setPickerStyle(WheelPicker picker) {
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(this, 10));
        picker.setDividerColor(ResourceUtils.getColor(R.color.color_E4E4E4));
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setTextColor(ResourceUtils.getColor(R.color.black));
        picker.setTextSize(20);
        picker.setLabelTextColor(ResourceUtils.getColor(R.color.black));
        picker.setCancelTextColor(ResourceUtils.getColor(R.color.black));
        picker.setCancelTextSize(16);
        picker.setSubmitTextColor(ResourceUtils.getColor(R.color.black));
        picker.setSubmitTextSize(ResourceUtils.getColor(R.color.black));
        picker.setPressedTextColor(ResourceUtils.getColor(R.color.color_888888));
        picker.setTitleTextColor(ResourceUtils.getColor(R.color.black));
        picker.setTitleTextSize(18);
        picker.setTopLineColor(ResourceUtils.getColor(R.color.color_E4E4E4));
        picker.setTopHeight(45);
        picker.setHeight(DeviceUtils.dpToPx(260));
        picker.setAnimationStyle(R.style.BottomDialogStyle_AnimationStyle150);
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case GET_USER_DETAIL_ONE:
                return action.getUserDetailOne(userId);
            case MSZT_PAY:
                return action.postMsztPay(123);//订单id先随便填一个，后期再看是先支付再下单还是先下单得到订单号再支付
            case MSZT_CREATE_ORDER:
                return action.postWdyhCreateOrder(orderRequest);//下单
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
                case MSZT_PAY:
                    BaseResponse baseResponse = (BaseResponse) result;
                    if (baseResponse.getCode() == 200) {
                        startCreateOrder();
                    } else {
                        NToast.shortToast(mContext, "支付失败");
                    }
                    break;
                case MSZT_CREATE_ORDER:
                    GetWdyhOrderDetailResponse msztOrderResponse = (GetWdyhOrderDetailResponse) result;
                    if (msztOrderResponse.getCode() == 200 && msztOrderResponse.getResult() != null
                            && !TextUtils.isEmpty(msztOrderResponse.getResult().getWdyhOrderId())) {
                        NToast.shortToast(mContext, "下单成功");
                        Intent intent = new Intent(mContext, WdyhDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("wdyhOrderId", msztOrderResponse.getResult().getWdyhOrderId());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        SealAppContext.getInstance().popActivity("YueTaXmxzActivity");
                        SealAppContext.getInstance().popActivity("YueTaMsytActivity");
                    } else {
                        NToast.shortToast(mContext, "下单失败");
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
            case MSZT_PAY:
                NToast.shortToast(mContext, "支付失败");
                break;
            case MSZT_CREATE_ORDER:
                NToast.shortToast(mContext, "下单失败");
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveDdxzEvent(SaveDdxzEvent event) {
        //只处理本类发起的定位请求
        if (Utils.isNotNull(event)) {
            yyddPoint = event.getSelectedPoint();
            if (yyddPoint == null) {
                NToast.shortToast(YueTaMsytActivity.this, "地址选择出错，请重新选择");
                //租不能点
                isYyddZq = false;
                return;
            }
            yydd = event.getSelectedName();
            yyDidianTv.setText(yydd);
            isYyddZq = true;
        }
    }
}
