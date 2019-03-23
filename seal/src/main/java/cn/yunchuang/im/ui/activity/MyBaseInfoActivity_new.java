package cn.yunchuang.im.ui.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.itheima.roundedimageview.RoundedImageView;
import com.jrmf360.rylib.common.util.ToastUtil;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigSubTitle;
import com.mylhyl.circledialog.callback.ConfigTitle;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.SubTitleParams;
import com.mylhyl.circledialog.params.TitleParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.WheelPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.widget.WheelView;
import cn.yunchuang.im.R;
import cn.yunchuang.im.server.BaseAction;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.widget.AddressPickTask;
import cn.yunchuang.im.zmico.statusbar.StatusBarCompat;
import cn.yunchuang.im.zmico.utils.BaseBaseUtils;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import cn.yunchuang.im.zmico.utils.ResourceUtils;


public class MyBaseInfoActivity_new extends BaseActivity implements View.OnClickListener,
        View.OnLongClickListener {

    private NestedScrollView nestedScrollView;

    private FrameLayout titleLayout;
    private ImageView backImg;
    private TextView saveTv;

    private LinearLayout zhaopianLayout;
    private LinearLayout nichengLayout;
    private TextView nichengTv;
    private LinearLayout xingbieLayout;
    private TextView xingbieTv;
    private LinearLayout shengriLayout;
    private TextView shengriTv;
    private LinearLayout shengaoLayout;
    private TextView shengaoTv;
    private LinearLayout szdLayout;
    private TextView szdTv;

    private int screenWidth;
    private int zhaopianWidth;

    private String setSex = "男";
    private String setYear = "1990";
    private String setMonth = "01";
    private String setDay = "01";
    private String setShengao = "180cm";
    private String setProvince = "广东省";
    private String setCity = "深圳市";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, Color.WHITE);
        BaseBaseUtils.setTranslucentStatus(this);//状态栏透明
        setContentView(R.layout.activity_my_base_info);
        setTitle(R.string.de_actionbar_myacc);
        setHeadVisibility(View.GONE);

        initView();
    }

    private void initView() {
        screenWidth = DeviceUtils.getScreenWidthPixels(this);
        zhaopianWidth = (screenWidth - DeviceUtils.dpToPx(35) - (DeviceUtils.dpToPx(5) * 3)) / 4;

        nestedScrollView = (NestedScrollView) findViewById(R.id.activity_base_info_root_scrollview);

        titleLayout = (FrameLayout) findViewById(R.id.activity_base_info_title_layout);
        backImg = (ImageView) findViewById(R.id.activity_base_info_back);
        backImg.setOnClickListener(this);
        saveTv = (TextView) findViewById(R.id.activity_base_info_save);
        saveTv.setOnClickListener(this);

        zhaopianLayout = (LinearLayout) findViewById(R.id.activity_base_info_zhaopian_layout);
        nichengLayout = (LinearLayout) findViewById(R.id.activity_base_info_nicheng_layout);
        nichengLayout.setOnClickListener(this);
        nichengTv = (TextView) findViewById(R.id.activity_base_info_nicheng_tv);
        xingbieLayout = (LinearLayout) findViewById(R.id.activity_base_info_xingbie_layout);
        xingbieLayout.setOnClickListener(this);
        xingbieTv = (TextView) findViewById(R.id.activity_base_info_xingbie_tv);
        shengriLayout = (LinearLayout) findViewById(R.id.activity_base_info_shengri_layout);
        shengriLayout.setOnClickListener(this);
        shengriTv = (TextView) findViewById(R.id.activity_base_info_shengri_tv);
        shengaoLayout = (LinearLayout) findViewById(R.id.activity_base_info_shengao_layout);
        shengaoLayout.setOnClickListener(this);
        shengaoTv = (TextView) findViewById(R.id.activity_base_info_shengao_tv);
        szdLayout = (LinearLayout) findViewById(R.id.activity_base_info_szd_layout);
        szdLayout.setOnClickListener(this);
        szdTv = (TextView) findViewById(R.id.activity_base_info_szd_tv);

        initTitleLayout();
        List<String> imgList = new ArrayList<>();
        for (int i = 16; i < 26; i++) {
            imgList.add(BaseAction.DOMAIN_PIC + "/" + i + ".jpg");
        }
        initZhaoPianLayout(imgList);

        nestedScrollView.scrollTo(0, 0);
    }

    private void initTitleLayout() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DeviceUtils.dpToPx(48) + DeviceUtils.getStatusBarHeightPixels(this));
        titleLayout.setLayoutParams(layoutParams);
    }

    public void initZhaoPianLayout(List<String> zhaopianList) {
        zhaopianLayout.removeAllViews();
        zhaopianLayout.addView(createAddItem());
        for (int i = 0; i < zhaopianList.size(); i++) {
            View view;
            String imgUrl = zhaopianList.get(i);
            view = createImgItem(imgUrl);
            view.setTag(imgUrl);
            zhaopianLayout.addView(view);
        }
    }

    private View createAddItem() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_my_base_info_zhaopian_add, zhaopianLayout, false);
        ImageView addIv = (ImageView) view.findViewById(R.id.activity_base_info_add_zhaopin_iv);
        //计算宽度，使正好能显示四个图片（包含添加）
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(zhaopianWidth, zhaopianWidth);
        params.setMargins(0, 0, DeviceUtils.dpToPx(5), 0);
        addIv.setLayoutParams(params);
        addIv.setOnClickListener(this);
        return view;
    }

    private View createImgItem(String imgUrl) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_my_base_info_zhaopian_item, zhaopianLayout, false);
        RoundedImageView itemIv = (RoundedImageView) view.findViewById(R.id.activity_base_info_item_zhaopin_iv);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(zhaopianWidth, zhaopianWidth);
        params.setMargins(0, 0, DeviceUtils.dpToPx(5), 0);
        itemIv.setLayoutParams(params);
        itemIv.setOnClickListener(this);
        itemIv.setOnLongClickListener(this);

        RequestOptions options = new RequestOptions();//正常加载

        options.placeholder(R.drawable.ic_image_zhanwei)
                .error(R.drawable.ic_image_zhanwei);

        Glide.with(this)
                .load(imgUrl)
                .apply(options)
                .into(itemIv);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_base_info_back:
                finish();
            case R.id.activity_base_info_add_zhaopin_iv:
                showSelectPictureDialog();
            case R.id.activity_base_info_item_zhaopin_iv:
                showSelectPictureDialog();
            case R.id.activity_base_info_xingbie_layout:
                onXingbiePicker();
                break;
            case R.id.activity_base_info_shengri_layout:
                onShengriPicker();
                break;
            case R.id.activity_base_info_shengao_layout:
                onShengaoPicker();
                break;
            case R.id.activity_base_info_szd_layout:
                onSzdPicker();
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.activity_base_info_item_zhaopin_iv:
                showSelectPictureDialog();
                break;
        }
        return true;
    }

    private void showSelectPictureDialog() {
        final List<PictureTypeEntity> items = new ArrayList<>();
        items.add(new PictureTypeEntity(1, "拍照"));
        items.add(new PictureTypeEntity(2, "从相册选择"));
        items.add(new PictureTypeEntity(3, "小视频"));
//                final String[] items = {"拍照", "从相册选择", "小视频"};
        new CircleDialog.Builder()
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.backgroundColorPress = Color.CYAN;
                        //增加弹出动画
                        params.animStyle = R.style.MDBottomDialogStyle_AnimationStyle;
//                            params.gravity = Gravity.TOP;
                    }
                })
                .setTitle("标题")
//                        .setTitleColor(Color.BLUE)
                .configTitle(new ConfigTitle() {
                    @Override
                    public void onConfig(TitleParams params) {
                        //                                params.backgroundColor = Color.RED;
                    }
                })
                .setSubTitle("副标题：请从以下中选择照片的方式进行提交")
                .configSubTitle(new ConfigSubTitle() {
                    @Override
                    public void onConfig(SubTitleParams params) {
                        //                                params.backgroundColor = Color.YELLOW;
                    }
                })
                .setItems(items, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(MyBaseInfoActivity_new.this, "点击了：" + items.get(i)
                                , Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegative("取消", null)
//                        .setNeutral("中间", null)
//                        .setPositive("确定", null)
//                        .configNegative(new ConfigButton() {
//                            @Override
//                            public void onConfig(ButtonParams params) {
//                                //取消按钮字体颜色
//                                params.textColor = Color.RED;
//                                params.backgroundColorPress = Color.BLUE;
//                            }
//                        })
                .show(getSupportFragmentManager());
    }

    private void onXingbiePicker() {
        OptionPicker picker = new OptionPicker(this, new String[]{"男", "女"});
        setPickerStyle(picker);
        picker.setTitleText("性别");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                setSex = item;
                xingbieTv.setText(item);
            }
        });
        picker.setSelectedItem(setSex);
        picker.show();
    }

    private void onShengriPicker() {
        final DatePicker picker = new DatePicker(this);
        setPickerStyle(picker);
        picker.setTitleText("出生日期");
        picker.setContentPadding(20, 0);
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                shengriTv.setText(year + "-" + month + "-" + day);
                setYear = year;
                setMonth = month;
                setDay = day;
            }
        });

        //范围从18岁到70岁，当前日期往前推18年和70年
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int endYear = currentYear - 18;
        int startYear = currentYear - 70;
        picker.setRangeEnd(endYear, currentMonth, currentDay);
        picker.setRangeStart(startYear, currentMonth, currentDay);
        picker.setSelectedItem(Integer.valueOf(setYear), Integer.valueOf(setMonth), Integer.valueOf(setDay));

        picker.show();
    }

    private void onShengaoPicker() {
        OptionPicker picker = new OptionPicker(this, getShengaoStringArray());
        setPickerStyle(picker);
        picker.setTitleText("身高");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                setShengao = item;
                shengaoTv.setText(item);
            }
        });
        picker.setSelectedItem(setShengao);
        picker.show();
    }

    private void onSzdPicker() {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideCounty(true);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                ToastUtil.showToast(MyBaseInfoActivity_new.this, "数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
//                showToast(province.getAreaName() + " " + city.getAreaName());
                setProvince = province.getAreaName();
                setCity = city.getAreaName();
                szdTv.setText(province.getAreaName() + " " + city.getAreaName());
            }
        });
        task.execute(setProvince, setCity);
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
        picker.setHeight(ResourceUtils.dpToPX(260));
        picker.setAnimationStyle(R.style.MDBottomDialogStyle_AnimationStyle);
    }

    private String[] getShengaoStringArray() {
        return new String[]{"145cm", "146cm", "147cm", "148cm", "149cm", "150cm",
                "151cm", "152cm", "153cm", "154cm", "155cm", "156cm", "157cm", "158cm", "159cm", "160cm",
                "161cm", "162cm", "163cm", "164cm", "165cm", "166cm", "167cm", "168cm", "169cm", "170cm",
                "171cm", "172cm", "173cm", "174cm", "175cm", "176cm", "177cm", "178cm", "179cm", "180cm",
                "181cm", "182cm", "183cm", "184cm", "185cm", "186cm", "187cm", "188cm", "189cm", "190cm",
                "191cm", "192cm", "193cm", "194cm", "195cm", "196cm", "197cm", "198cm", "199cm", "200cm"};
    }

    public class PictureTypeEntity
//        implements CircleItemLabel
    {
        public int id;
        public String typeName;

        public PictureTypeEntity() {
        }

        public PictureTypeEntity(int id, String typeName) {
            this.id = id;
            this.typeName = typeName;
        }

        @Override
        public String toString() {
            return typeName;
        }

//    @Override
//    public String getItemLabel() {
//        return typeName;
//    }
    }

}
