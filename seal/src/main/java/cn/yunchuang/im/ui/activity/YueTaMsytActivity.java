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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;

import cn.qqtheme.framework.picker.WheelPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.DateUtils;
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

    private ImageView portraitIv;
    private TextView nameTv;
    private AgeSexView ageSexView;
    private TextView distanceTv;

    private TextView skillNameTv;
    private FrameLayout skillLayout;
    private TextView skillPriceTv;
    private TextView skillDescTv;
    private LinearLayout yyShijianLayout;
    private TextView yyShijian;
    private LinearLayout yyShichangLayout;
    private TextView yyShichang;
    private LinearLayout yyDidianLayout;
    private TextView yyDidian;


    private static final int GET_USER_DETAIL_ONE = 1601;

    private PromptDialog loadingDialog;
    private String userId = "";

    private SkillModel seletSkillModel = new SkillModel();

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

        portraitIv = (ImageView) findViewById(R.id.activity_msyt_portrait);
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
        yyShijian = (TextView) findViewById(R.id.activity_msyt_yuyue_shijian_tv);
        yyShichangLayout = (LinearLayout) findViewById(R.id.activity_msyt_yuyue_shichang_layout);
        yyShichangLayout.setOnClickListener(this);
        yyShichang = (TextView) findViewById(R.id.activity_msyt_yuyue_shichang_tv);
        yyDidianLayout = (LinearLayout) findViewById(R.id.activity_msyt_yuyue_didian_layout);
        yyDidianLayout.setOnClickListener(this);
        yyDidian = (TextView) findViewById(R.id.activity_msyt_yuyue_didian_tv);

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
        dates.add("尽快");
        dates.add("今天");
        dates.add("明天");
        dates.add("后天");
        final ArrayList<String> hours = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
            hours.add(DateUtils.fillZero(i));
        }
        final ArrayList<String> minutes = new ArrayList<>();
        minutes.add("00");
        minutes.add("15");
        minutes.add("30");
        minutes.add("45");
        TriplePicker picker = new TriplePicker(this, dates, hours, minutes);
        setPickerStyle(picker);
        picker.setFirstLabel("", "");
        picker.setSecondLabel("", "");
        picker.setThirdLabel("", "");
        picker.setOnPickListener(new TriplePicker.OnPickListener() {
            @Override
            public void onPicked(int selectedFirstIndex, int selectedSecondIndex, int selectedThirdIndex) {
                Log.d("xxxxxx",selectedFirstIndex+"");
                Log.d("xxxxxx",selectedSecondIndex+"");
                Log.d("xxxxxx",selectedThirdIndex+"");
            }
        });
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
