package cn.yunchuang.im.ui.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itheima.roundedimageview.RoundedImageView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.WheelPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.widget.WheelView;
import cn.yunchuang.im.R;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.server.response.GetUserDetailModelOne;
import cn.yunchuang.im.server.response.GetUserDetailOneResponse;
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


    private static final int GET_USER_DETAIL_ONE = 1601;

    private PromptDialog loadingDialog;
    private String userId = "";

    private SkillModel seletSkillModel = new SkillModel();

    private long yysjTs;
    private int yyscHours;
    private String yydd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseBaseUtils.setTranslucentStatus(this);//状态栏透明
        setContentView(R.layout.activity_yue_ta_msyt);
        setHeadVisibility(View.GONE);
        initView();
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

        loadingDialog = DialogUtils.getLoadingDialog(this);

        initTitleLayout();
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
        GlideUtils.load(this, modelOne.getPortraitUri(), portraitIv);
        nameTv.setVisibility(View.VISIBLE);
        nameTv.setText(modelOne.getNickname());
        ageSexView.setVisibility(View.VISIBLE);
        ageSexView.setAgeAndSex(modelOne.getAge(), modelOne.getSex());
        distanceTv.setVisibility(View.VISIBLE);
        distanceTv.setText(MessageFormat.format("{0}{1}", String.format(Locale.getDefault(),
                "%.2f", modelOne.getDistance()), "km"));

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
        String hourLater = DateUtils.getHoursLater(currentTs, 3);
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
                Log.d("xxxxxx", selectedFirstIndex + "");
                Log.d("xxxxxx", selectedSecondIndex + "");
                Log.d("xxxxxx", selectedThirdIndex + "");
                if (picker.getSelectedFirstItem().equals("尽快")) {
                    yyShijianTv.setText("尽快（" + picker.getSelectedSecondItem() + ":" + picker.getSelectedThirdItem() + "之前）");
                }
            }
        });
        picker.setOnWheelListener(new TriplePicker.OnWheelListener() {
            @Override
            public void onFirstWheeled(int index, String item) {
                if (index == 0) {
                    //选的尽快，则默认选择3小时之后，
                    long currentTs = Calendar.getInstance().getTimeInMillis() / 1000;
                    String hourLater = DateUtils.getHoursLater(currentTs, 3);
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
        String[] shiChangArray = new String[]{"1小时", "2小时", "3小时", "4小时", "5小时", "6小时",
                "7小时", "8小时", "9小时", "10小时", "11小时", "12小时"};
        OptionPicker picker = new OptionPicker(this, shiChangArray);
        setPickerStyle(picker);
        picker.setTitleText("预约时长");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {

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
        }
    }
}
