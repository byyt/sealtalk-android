package cn.yunchuang.im.ui.activity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.docwei.imageupload_lib.album.type.UsageTypeConstant;
import com.docwei.imageupload_lib.album.ui.ImageSelectProxyActivity;
import com.docwei.imageupload_lib.constant.ImageConstant;
import com.itheima.roundedimageview.RoundedImageView;
import com.jrmf360.rylib.common.util.ToastUtil;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigItems;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.ItemsParams;
import com.mylhyl.circledialog.scale.ScaleLayoutConfig;
import com.previewlibrary.GPreviewBuilder;

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
import cn.yunchuang.im.model.UserViewInfo;
import cn.yunchuang.im.server.BaseAction;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.utils.DialogUtils;
import cn.yunchuang.im.widget.AddressPickTask;
import cn.yunchuang.im.zmico.statusbar.StatusBarCompat;
import cn.yunchuang.im.zmico.utils.BaseBaseUtils;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import cn.yunchuang.im.zmico.utils.ResourceUtils;
import cn.yunchuang.im.zmico.utils.Utils;
import cn.yunchuang.im.zmico.utils.ViewUtil;
import me.leefeng.promptlibrary.PromptDialog;


public class MyBaseInfoActivity_new extends BaseActivity implements View.OnClickListener,
        View.OnLongClickListener {

    private static final String TAG = MyBaseInfoActivity_new.class.getName();

    private NestedScrollView nestedScrollView;

    private FrameLayout titleLayout;
    private ImageView backImg;
    private TextView saveTv;

    private RoundedImageView touxiangImg;

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

    private PromptDialog loadingDialog;

    private String mType;//这个Type保留了原框架的用法，用来区别多张图片还是单张图片，我这里只用到单张图片
    private int imgTyp = 0;//我这里再加一个类型，用来区别更换头像还是普通照片，0：更换头像，1：更换普通照片
    private int clickZhaoPianPosition = 0;//当前点击的照片的位置，从加号之后的图片开始算起
    private List<UserViewInfo> touXiangInfoList = new ArrayList<>(); //头像大图，虽然是集合，其实就一张头像图，为了符合框架用法
    private List<UserViewInfo> zhaoPianInfoList = new ArrayList<>(); //照片大图集合

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

        touxiangImg = (RoundedImageView) findViewById(R.id.activity_base_info_avatar_iv);
        touxiangImg.setOnClickListener(this);
        touxiangImg.setOnLongClickListener(this);

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
        String imgUrl = BaseAction.DOMAIN_PIC + "/" + 56 + ".jpg";
        initTouXiang(imgUrl);
        List<String> imgList = new ArrayList<>();
        for (int i = 16; i < 26; i++) {
            imgList.add(BaseAction.DOMAIN_PIC + "/" + i + ".jpg");
        }
        initZhaoPianLayout(imgList);

        loadingDialog = DialogUtils.getLoadingDialog(this);

        nestedScrollView.scrollTo(0, 0);
    }

    private void initTouXiang(String imgUrl) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.ic_image_zhanwei)
                .error(R.drawable.ic_image_zhanwei);
        Glide.with(this)
                .load(imgUrl)
                .apply(options)
                .into(touxiangImg);
        UserViewInfo userViewInfo = new UserViewInfo(imgUrl);
        touXiangInfoList.add(userViewInfo);
    }

    private void initTitleLayout() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DeviceUtils.dpToPx(48) + DeviceUtils.getStatusBarHeightPixels(this));
        titleLayout.setLayoutParams(layoutParams);
    }

    public void initZhaoPianLayout(List<String> zhaopianList) {
        zhaopianLayout.removeAllViews();
        zhaopianLayout.addView(createAddItem());
        zhaoPianInfoList.clear();
        UserViewInfo userViewInfo;
        for (int i = 0; i < zhaopianList.size(); i++) {
            View view;
            String imgUrl = zhaopianList.get(i);
            view = createImgItem(imgUrl, i);
            zhaopianLayout.addView(view);
            //添加大图浏览数据
            userViewInfo = new UserViewInfo(imgUrl);
            zhaoPianInfoList.add(userViewInfo);
        }
    }

    private View createAddItem() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_my_base_info_zhaopian_add, zhaopianLayout, false);
        ImageView addIv = (ImageView) view.findViewById(R.id.activity_base_info_add_zhaopian_iv);
        //计算宽度，使正好能显示四个图片（包含添加）
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(zhaopianWidth, zhaopianWidth);
        params.setMargins(0, 0, DeviceUtils.dpToPx(5), 0);
        addIv.setLayoutParams(params);
        addIv.setOnClickListener(this);
        return view;
    }

    private View createImgItem(String imgUrl, int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_my_base_info_zhaopian_item, zhaopianLayout, false);
        RoundedImageView itemIv = (RoundedImageView) view.findViewById(R.id.activity_base_info_item_zhaopian_iv);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(zhaopianWidth, zhaopianWidth);
        params.setMargins(0, 0, DeviceUtils.dpToPx(5), 0);
        itemIv.setLayoutParams(params);
        itemIv.setOnClickListener(this);
        itemIv.setOnLongClickListener(this);
        ViewUtil.setTag(itemIv, position, R.id.info_tag);//得把位置存进tag，方便点击的时候知道点击了第几个

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
        if (Utils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.activity_base_info_back:
                finish();
                break;
            case R.id.activity_base_info_avatar_iv:
                liuLanTouXiang();
                break;
            case R.id.activity_base_info_add_zhaopian_iv:
                imgTyp = 1;//更换普通照片
                showSelectPictureDialog();
                break;
            case R.id.activity_base_info_item_zhaopian_iv:
                if (ViewUtil.getTag(v, R.id.info_tag) == null) {
                    return;
                }
                clickZhaoPianPosition = (int) ViewUtil.getTag(v, R.id.info_tag);//从tag里取出当前点击的照片位置
                liuLanZhaoPian();
                break;
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
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.activity_base_info_avatar_iv:
                imgTyp = 0;//更换头像
                showSelectPictureDialog();
                return true;
            case R.id.activity_base_info_item_zhaopian_iv:
                imgTyp = 1;//更换普通照片
                if (ViewUtil.getTag(v, R.id.info_tag) == null) {
                    return true;
                }
                clickZhaoPianPosition = (int) ViewUtil.getTag(v, R.id.info_tag);//从tag里取出当前点击的照片位置
                showSelectPictureDialog();
                return true;
        }
        return true;
    }

    //这个选照片的对话框不用来，但下面的代码可以参考
    private void showSelectPictureDialog_old() {
        //这种对话框效果很好，但为了统一还是用下面的方式吧
//        PromptDialog promptDialog = DialogUtils.getLoadingDialog(this);
//        PromptButton cancle = new PromptButton("取消", null);
//        cancle.setTextColor(ResourceUtils.getColor(R.color.color_10AEFF));
//        promptDialog.showAlertSheet("", true, cancle,
//                new PromptButton("拍照", new PromptButtonListener() {
//                    @Override
//                    public void onClick(PromptButton promptButton) {
//
//                    }
//                }),
//                new PromptButton("从手机相册选择", new PromptButtonListener() {
//                    @Override
//                    public void onClick(PromptButton promptButton) {
//
//                    }
//                }));

        //此库自动将px转换百分比，由于 Dialog 布局一般只有微调，暂时只支持textSize，height，padding
        //默认字体大小;Title、message、button、padding 的px在设计稿为 1080 * 1920 的尺寸
        //所以在设置字体大小，高度、距离等时，记得 (int) (DeviceUtils.dpToPx(55) / ScaleLayoutConfig.getInstance().getScale());
        final List<PictureTypeEntity> items = new ArrayList<>();
        items.add(new PictureTypeEntity(1, "拍照"));
        items.add(new PictureTypeEntity(2, "从手机相册选择"));
        new CircleDialog.Builder()
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        //点击后颜色
                        params.backgroundColorPress = ResourceUtils.getColor(R.color.color_E4E4E4);
                        //增加弹出动画
                        params.animStyle = R.style.BottomDialogStyle_AnimationStyle200;
                        //宽度占屏幕比例
                        params.width = (float) 0.95;
                        params.radius = 20;
                    }
                })
                .configItems(new ConfigItems() {
                    @Override
                    public void onConfig(ItemsParams params) {
                        //这个框架单位内部又算了一遍，只能看着调了，蛋疼
                        params.textColor = ResourceUtils.getColor(R.color.black);
                        params.textSize = (int) (DeviceUtils.spToPx(18) / ScaleLayoutConfig.getInstance().getScale());
                        params.itemHeight = (int) (DeviceUtils.dpToPx(55) / ScaleLayoutConfig.getInstance().getScale());
                    }
                })
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.topMargin = DeviceUtils.dpToPx(5);
                        params.textColor = ResourceUtils.getColor(R.color.color_0076ff);
                        params.textSize = (int) (DeviceUtils.spToPx(18) / ScaleLayoutConfig.getInstance().getScale());
                        params.height = (int) (DeviceUtils.dpToPx(55) / ScaleLayoutConfig.getInstance().getScale());
                        params.text = "取消";
                    }
                })
                .setItems(items, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 0) {

                        } else {

                        }
                    }
                })
                .show(getSupportFragmentManager());

    }

    private void showSelectPictureDialog() {
        mType = UsageTypeConstant.HEAD_PORTRAIT;//为了保留原来的用法，才加入这个变量，其实这个变量基本就是这个值，在我这没啥用
        // 更换图片 ，每次只有一张
        ImageSelectProxyActivity.selectImage(MyBaseInfoActivity_new.this, UsageTypeConstant.HEAD_PORTRAIT, 1);

    }

    private void liuLanTouXiang() {
        if (touxiangImg == null || Utils.isEmptyCollection(touXiangInfoList)) {
            return;
        }
        Rect bounds = new Rect();
        touxiangImg.getGlobalVisibleRect(bounds);
        touXiangInfoList.get(0).setBounds(bounds);
        GPreviewBuilder.from(MyBaseInfoActivity_new.this)
                .setData(touXiangInfoList)
                .setCurrentIndex(0)
                .setSingleFling(true)
                .setType(GPreviewBuilder.IndicatorType.Number)
                .start();
    }

    private void liuLanZhaoPian() {
        if (zhaopianLayout == null || Utils.isEmptyCollection(zhaoPianInfoList)) {
            return;
        }
        //从第二个开始，第一个加号图片不算
        for (int i = 0; i < zhaopianLayout.getChildCount() - 1; i++) {
            Rect bounds = new Rect();
            zhaopianLayout.getChildAt(i + 1).getGlobalVisibleRect(bounds);
            zhaoPianInfoList.get(i).setBounds(bounds);
        }
        GPreviewBuilder.from(MyBaseInfoActivity_new.this)
                .setData(zhaoPianInfoList)
                .setCurrentIndex(clickZhaoPianPosition)
                .setSingleFling(true)
                .setType(GPreviewBuilder.IndicatorType.Number)
                .start();
    }

    /**
     * 大图浏览，计算头像的位置
     */
    private void computeBoundsBackward(int firstCompletelyVisiblePos) {
//        for (int i = firstCompletelyVisiblePos; i < zhaoPianInfoList.size(); i++) {
//            View itemView = linearLayoutManager.findViewByPosition(i);
//            Rect bounds = new Rect();
//            if (itemView != null) {
//                ImageView thumbView = (ImageView) itemView.findViewById(R.id.user_detail_new_pic_item_img);
//                thumbView.getGlobalVisibleRect(bounds);
//            }
//            zhaoPianInfoList.get(i).setBounds(bounds);
//        }
    }

    private void xuanzeZhaopian() {
//        photoUtils.selectPicture(MyBaseInfoActivity_new.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == ImageConstant.REQUEST_CODE_IAMGES) {
                ArrayList<String> list = (ArrayList<String>) data.getSerializableExtra(ImageConstant.SELECTED_IAMGES);
                //场景一：评论等上传 这里不裁剪
                if (mType.equals(UsageTypeConstant.OTHER)) {
//                    mImagesAdapter.updateDataFromAlbum(list);
                }


                if (mType.equals(UsageTypeConstant.HEAD_PORTRAIT)) {
                    //场景二：单张图片操作 有裁剪操作
                    if (list != null && list.size() > 0) {
                        RequestOptions options = new RequestOptions();//正常加载
                        options.placeholder(R.drawable.ic_image_zhanwei)
                                .error(R.drawable.ic_image_zhanwei);
                        if (imgTyp == 0) {
                            Glide.with(this)
                                    .load(list.get(0))
                                    .apply(options)
                                    .into(touxiangImg);
                            touXiangInfoList.clear();
                            touXiangInfoList.add(new UserViewInfo(list.get(0)));
                        } else {
                            if (clickZhaoPianPosition > zhaopianLayout.getChildCount() - 2) {
                                return;
                            }
                            FrameLayout frameLayout
                                    = (FrameLayout) zhaopianLayout.getChildAt(clickZhaoPianPosition + 1);
                            RoundedImageView roundedImageView = frameLayout.findViewById(R.id.activity_base_info_item_zhaopian_iv);
                            if (roundedImageView == null) {
                                return;
                            }
                            Glide.with(this)
                                    .load(list.get(0))
                                    .apply(options)
                                    .into(roundedImageView);
                            zhaoPianInfoList.set(clickZhaoPianPosition, new UserViewInfo(list.get(0)));
                        }
                    }
                }
            }
        }
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
        picker.setHeight(DeviceUtils.dpToPx(260));
        picker.setAnimationStyle(R.style.BottomDialogStyle_AnimationStyle150);
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
