package cn.yunchuang.im.ui.activity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
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
import com.docwei.imageupload_lib.album.type.UsageTypeConstant;
import com.docwei.imageupload_lib.album.ui.ImageSelectProxyActivity;
import com.docwei.imageupload_lib.constant.ImageConstant;
import com.hjq.toast.ToastUtils;
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
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.ProgressResponseCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
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
import cn.yunchuang.im.MeService;
import cn.yunchuang.im.R;
import cn.yunchuang.im.event.RefreshMineInfoEvent;
import cn.yunchuang.im.http.HttpCallBack;
import cn.yunchuang.im.model.UserViewInfo;
import cn.yunchuang.im.server.BaseAction;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.server.response.BaseResponse;
import cn.yunchuang.im.server.response.GetUserDetailModelOne;
import cn.yunchuang.im.server.response.GetUserDetailOneResponse;
import cn.yunchuang.im.server.response.ImageModel;
import cn.yunchuang.im.utils.DateUtils;
import cn.yunchuang.im.utils.DialogUtils;
import cn.yunchuang.im.utils.FileUtils;
import cn.yunchuang.im.utils.GlideUtils;
import cn.yunchuang.im.utils.TextViewUtils;
import cn.yunchuang.im.utils.ViewVisibleUtils;
import cn.yunchuang.im.widget.AddressPickTask;
import cn.yunchuang.im.widget.dialog.ConfirmDialog;
import cn.yunchuang.im.widget.dialog.ShuruDialog;
import cn.yunchuang.im.widget.dialog.ShuruDuohangDialog;
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

    private FrameLayout touxiangLaout;
    private RoundedImageView touxiangImg;
    private FrameLayout txMengcengLayout;
    private TextView txMengcengTv;

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
    private LinearLayout qianMingLayout;
    private TextView qianMingTv;
    private LinearLayout xqahLayout;
    private TextView xqahTv;

    private PromptDialog loadingDialog;

    private String mType;//这个Type保留了原框架的用法，用来区别多张图片还是单张图片，我这里只用到单张图片
    private int clickZhaoPianPosition = 0;//当前点击的照片的位置，从加号之后的图片开始算起
    private List<UserViewInfo> touXiangInfoList = new ArrayList<>(); //头像大图，虽然是集合，其实就一张头像图，为了符合框架用法
    private List<UserViewInfo> zhaoPianInfoList = new ArrayList<>(); //照片大图集合
    private String shangchuanUrl;

    private int screenWidth;
    private int zhaopianWidth;

    private String setSex = "男";
    private String setYear = "1990";
    private String setMonth = "01";
    private String setDay = "01";
    private String setShengao = "180cm";
    private String setProvince = "广东省";
    private String setCity = "深圳市";

    private static final int GET_USER_BASE_INFO = 1;
    private static final int SAVE_USER_BASE_INFO = 2;

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
        txMengcengLayout = findViewById(R.id.activity_base_info_avatar_mengceng_layout);
        txMengcengLayout.setVisibility(View.GONE);
        txMengcengLayout.setOnLongClickListener(this);
        txMengcengTv = findViewById(R.id.activity_base_info_avatar_mengceng_tv);
        txMengcengTv.setText("0%");

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
        qianMingLayout = (LinearLayout) findViewById(R.id.activity_base_info_qianming_layout);
        qianMingLayout.setOnClickListener(this);
        qianMingTv = (TextView) findViewById(R.id.activity_base_info_qianming_tv);
        xqahLayout = (LinearLayout) findViewById(R.id.activity_base_info_xqah_layout);
        xqahLayout.setOnClickListener(this);
        xqahTv = (TextView) findViewById(R.id.activity_base_info_xqah_tv);

        initTitleLayout();

        loadingDialog = DialogUtils.getLoadingDialog(this);

        nestedScrollView.scrollTo(0, 0);

        zhaopianLayout.removeAllViews();//默认有添加按钮
        zhaopianLayout.addView(createAddItem());

        request(GET_USER_BASE_INFO);

    }


    private void initTouXiang(String imgUrl) {
        if (imgUrl == null) {
            imgUrl = "";
        }
        GlideUtils.load(this, BaseAction.DOMAIN_PIC + "/" + imgUrl, touxiangImg);
        UserViewInfo userViewInfo = new UserViewInfo(imgUrl);
        touXiangInfoList.add(userViewInfo);
    }

    private void initTitleLayout() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DeviceUtils.dpToPx(48) + DeviceUtils.getStatusBarHeightPixels(this));
        titleLayout.setLayoutParams(layoutParams);
    }

    public void initZhaoPianLayout(List<String> zhaopianList) {
        if (zhaopianList == null) {
            zhaopianList = new ArrayList<>();
        }
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

        GlideUtils.load(this, BaseAction.DOMAIN_PIC + "/" + imgUrl, itemIv);

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
            case GET_USER_BASE_INFO:
                return action.getUserDetailOne(MeService.getUid());
            case SAVE_USER_BASE_INFO:
                String freeImgList = checkTouxiangAndZhaoPianHefa();
                if (TextUtils.isEmpty(freeImgList)) {
                    return null;
                }
                String nickname = nichengTv.getText().toString();
                //上传到服务器只需后边upload/xxxx.jpg这一段，不用完整路径，把前面的域名截取掉
                String portraitUri = touXiangInfoList.get(0).getUrl().replace(BaseAction.DOMAIN_PIC + "/", "");
                int sex = xingbieTv.getText().toString().equals("男") ? 0 : 1;
                int height = Integer.valueOf(shengaoTv.getText().toString().replace("cm", ""));
                long birthday = DateUtils.date2TimeStamp(setYear + "-" + setMonth + "-" + setDay,
                        "yyyy-MM-dd");
                String suozaidi = setProvince + "/" + setCity;
                String qianMing = qianMingTv.getText().toString();
                String xqah = xqahTv.getText().toString();
                return action.upDateUserInfo(nickname, portraitUri, sex, height,
                        birthday, suozaidi, qianMing, xqah, freeImgList);
        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        DialogUtils.dimiss(loadingDialog);
        if (result != null) {
            switch (requestCode) {
                case GET_USER_BASE_INFO:
                    GetUserDetailOneResponse getUserDetailOneResponse = (GetUserDetailOneResponse) result;
                    if (getUserDetailOneResponse.getCode() == 200) {
                        updateBaseUserInfo(getUserDetailOneResponse);
                    } else {
                        ToastUtils.show("获取个人信息失败");
                    }
                    break;
                case SAVE_USER_BASE_INFO:
                    BaseResponse baseResponse = (BaseResponse) result;
                    if (baseResponse.getCode() == 200) {
                        ToastUtils.show("信息保存成功");
                        RefreshMineInfoEvent.postEvent();
                        finish();
                    } else {
                        ToastUtils.show("信息保存成功");
                    }

                    break;
            }
        } else {
            ToastUtils.show("信息保存失败");
        }
    }


    @Override
    public void onFailure(int requestCode, int state, Object result) {
        DialogUtils.dimiss(loadingDialog);
        switch (requestCode) {
            case GET_USER_BASE_INFO:
                ToastUtils.show("信息获取失败");
                break;
            case SAVE_USER_BASE_INFO:
                ToastUtils.show("信息保存失败");
                break;
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
            case R.id.activity_base_info_save:
//                shangchuan(shangchuanUrl);
                DialogUtils.showLoading(loadingDialog);
                request(SAVE_USER_BASE_INFO, true);
                break;
            case R.id.activity_base_info_avatar_iv:
                liuLanTouXiang();
                break;
            case R.id.activity_base_info_add_zhaopian_iv:
                showSelectPictureDialog(zhaoPianInfoList.size(), false);//添加图片按钮，传的位置是最后一个，zhaoPianInfoList.size()
                break;
            case R.id.activity_base_info_item_zhaopian_iv:
                if (ViewUtil.getTag(v, R.id.info_tag) == null) {
                    return;
                }
                clickZhaoPianPosition = (int) ViewUtil.getTag(v, R.id.info_tag);//从tag里取出当前点击的照片位置
                liuLanZhaoPian();
                break;
            case R.id.activity_base_info_nicheng_layout:
                onNichengXiugai();
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
            case R.id.activity_base_info_qianming_layout:
                onQianmingXiugai();
                break;
            case R.id.activity_base_info_xqah_layout:
                onXqahXiugai();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (Utils.isFastClick()) {
            return true;
        }
        switch (v.getId()) {
            case R.id.activity_base_info_avatar_iv://长按头像或者蒙层
            case R.id.activity_base_info_avatar_mengceng_layout:
                showSelectPictureDialog(-1, false);//头像位置比较特殊，设置为-1
                return true;
            case R.id.activity_base_info_item_zhaopian_iv://长按普通照片或者蒙层
            case R.id.activity_base_info_item_mengceng_layout:
                if (ViewUtil.getTag(v, R.id.info_tag) == null) {
                    return true;
                }
                clickZhaoPianPosition = (int) ViewUtil.getTag(v, R.id.info_tag);//从tag里取出当前点击的照片位置
                showSelectPictureDialog(clickZhaoPianPosition, true);//这期间，可能又回点了其他照片clickZhaoPianPosition会变化，将这个位置存进去，处理完之后再对该位置图片处理
                return true;
        }
        return true;
    }

    private void updateBaseUserInfo(GetUserDetailOneResponse getUserDetailOneResponse) {
        if (getUserDetailOneResponse == null) {
            return;
        }
        GetUserDetailModelOne model = getUserDetailOneResponse.getResult();
        if (model == null) {
            return;
        }
        nichengTv.setText(model.getNickname());
        setSex = model.getSex() == 0 ? "男" : "女";
        xingbieTv.setText(setSex);
        setYear = DateUtils.getYear(model.getBirthday());
        setMonth = DateUtils.getMonth(model.getBirthday());
        setDay = DateUtils.getDay(model.getBirthday());
        shengriTv.setText(setYear + "-" + setMonth + "-" + setDay);
        setShengao = String.valueOf(model.getHeight()) + "cm";
        shengaoTv.setText(setShengao);
        if (model.getSuoZaiDi() != null && model.getSuoZaiDi().split("/").length == 2) {
            String[] strings = model.getSuoZaiDi().split("/");
            setProvince = String.valueOf(strings[0]);
            setCity = strings[1];
        } else {
            setProvince = "";
            setCity = "未填写";
        }
        szdTv.setText(model.getSuoZaiDi());
        qianMingTv.setText(model.getQianMing());
        xqahTv.setText(model.getXqah());

        initTouXiang(model.getPortraitUri());
        if (TextUtils.isEmpty(model.getFreeImgList())) {
            return;
        }
        List<ImageModel> imgModelList = new ArrayList<>();
        try {
            //将字符串的json数组转为list
            imgModelList = com.alibaba.fastjson.JSONArray.
                    parseArray(model.getFreeImgList(), ImageModel.class); //得到免废费图片列表
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> imgUrlList = new ArrayList<>();
        if (imgModelList != null) {
            for (ImageModel imageModel : imgModelList) {
                imgUrlList.add(imageModel.getImgUrl());
            }

        }
        initZhaoPianLayout(imgUrlList);
    }

    //这个选照片的对话框不用，但下面的代码可以参考
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
                        params.textColor = ResourceUtils.getColor(R.color.color_0076FF);
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

    /**
     * @param position 这个参数很重要，postion是-1表示处理的是头像，>=0表示处理的是普通照片，后面都是通过这个来判断
     */
    private void showSelectPictureDialog(int position, boolean showDelete) {
        mType = UsageTypeConstant.HEAD_PORTRAIT;//为了保留原来的用法，才加入这个变量，其实这个变量基本就是这个值，在我这没啥用
        // 更换图片 ，每次只有一张，position为-1代表更换头像，position为0-zhaoPianInfoList.size()-1代表更换普通照片，position为zhaoPianInfoList.size()代表添加照片
        ImageSelectProxyActivity.selectImage(MyBaseInfoActivity_new.this,
                UsageTypeConstant.HEAD_PORTRAIT, 1, position, showDelete);

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

                        shangchuanUrl = list.get(0);

                        //把之前点击图片的位置取出来，-1是头像，0-其他是普通照片，如果是-2，则表示出错
                        int position = data.getIntExtra("position", -2);
                        if (position == -2) {
                            return;
                        }
                        //仿照waka，图片剪裁完就进行上传（后期服务端应该可以看到图片被哪些用户使用，哪些是没有用户使用的，可以后台统一删除）
                        if (position == -1) {
                            //更换头像
                            shangchuan(shangchuanUrl, touxiangImg, txMengcengLayout, txMengcengTv, position);
                        } else if (0 <= position && position <= zhaoPianInfoList.size() - 1) {
                            //更换普通照片
                            if (clickZhaoPianPosition > zhaopianLayout.getChildCount() - 2) {
                                return;
                            }
                            //取到当前点击的view
                            FrameLayout frameLayout
                                    = (FrameLayout) zhaopianLayout.getChildAt(clickZhaoPianPosition + 1);
                            if (frameLayout == null) {
                                return;
                            }
                            RoundedImageView roundedImageView = frameLayout.findViewById(R.id.activity_base_info_item_zhaopian_iv);
                            FrameLayout mengcengLayout = frameLayout.findViewById(R.id.activity_base_info_item_mengceng_layout);
                            mengcengLayout.setVisibility(View.GONE);
                            mengcengLayout.setOnLongClickListener(this);
                            ViewUtil.setTag(mengcengLayout, position, R.id.info_tag);//得把位置存进tag，方便点击的时候知道点击了第几个，浏览图片用到
                            TextView mengcengTv = frameLayout.findViewById(R.id.activity_base_info_item_mengceng_tv);
                            shangchuan(shangchuanUrl, roundedImageView, mengcengLayout, mengcengTv, position);
                        } else {//position == zhaoPianInfoList.size()
                            //添加照片
                            View view;
                            view = createImgItem("", position);//先新建一个空的view
                            zhaopianLayout.addView(view);
                            RoundedImageView roundedImageView = view.findViewById(R.id.activity_base_info_item_zhaopian_iv);
                            FrameLayout mengcengLayout = view.findViewById(R.id.activity_base_info_item_mengceng_layout);
                            mengcengLayout.setVisibility(View.GONE);
                            mengcengLayout.setOnLongClickListener(this);
                            ViewUtil.setTag(mengcengLayout, position, R.id.info_tag);//得把位置存进tag，方便点击的时候知道点击了第几个，浏览图片用到
                            TextView mengcengTv = view.findViewById(R.id.activity_base_info_item_mengceng_tv);
                            shangchuan(shangchuanUrl, roundedImageView, mengcengLayout, mengcengTv, position);
                        }
                    }
                }
            }
        } else if (-6 == resultCode) {//自己定义的，返回-6就表示删除照片
            //把之前点击图片的位置取出来，-1是头像，0-其他是普通照片，如果是-2，则表示出错
            final int position = data.getIntExtra("position", -2);
            if (position == -2) {
                return;
            }
            //仿照waka，图片剪裁完就进行上传（后期服务端应该可以看到图片被哪些用户使用，哪些是没有用户使用的，可以后台统一删除）
            if (position == -1) {
                //删除的是头像，正常来讲，点击头像不会出现删除照片选项
                return;
            } else {
                //删除的是普通照片
                DialogUtils.showConfirmDialog(this, "确定删除该照片？", "",
                        new ConfirmDialog.ConfirmListener() {
                            @Override
                            public void confirm() {
                                shanchuZhaoPian(position);
                            }
                        });
            }
        }
    }

    private void shanchuZhaoPian(int position) {
        if (position < 0 || position > zhaoPianInfoList.size() - 1) {
            ToastUtils.show("删除图片出错");
            return;
        }
        zhaoPianInfoList.remove(position);
        List<String> zhaopianList = new ArrayList<>();
        for (UserViewInfo userViewInfo : zhaoPianInfoList) {
            String imgUrl = userViewInfo.getUrl();
            zhaopianList.add(imgUrl);
        }
        initZhaoPianLayout(zhaopianList);
    }

    private void onNichengXiugai() {
        DialogUtils.showShuruDanhangDialog(this, "输入昵称", nichengTv.getText().toString(),
                new ShuruDialog.ConfirmListener() {
                    @Override
                    public void confirm(String content) {
                        nichengTv.setText(content);
                    }
                });
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
                setProvince = province.getAreaName();
                setCity = city.getAreaName();
                szdTv.setText(setProvince + " " + setCity);
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

    public class PictureTypeEntity {
        public int id;
        public String typeName;

        public PictureTypeEntity(int id, String typeName) {
            this.id = id;
            this.typeName = typeName;
        }

        @Override
        public String toString() {
            return typeName;
        }

    }

    private void onQianmingXiugai() {
        DialogUtils.showShuruDuohangDialog(this, "输入个性签名",
                qianMingTv.getText().toString(), "请输入您的个性签名（字数50以内）",
                new ShuruDuohangDialog.ConfirmListener() {
                    @Override
                    public void confirm(String content) {
                        qianMingTv.setText(content);
                    }
                });
    }

    private void onXqahXiugai() {
        DialogUtils.showShuruDuohangDialog(this, "输入兴趣爱好",
                xqahTv.getText().toString(), "请输入您的兴趣爱好（字数50以内）",
                new ShuruDuohangDialog.ConfirmListener() {
                    @Override
                    public void confirm(String content) {
                        xqahTv.setText(content);
                    }
                });
    }

    //上传图片
    private void shangchuan(String fileUrl, final RoundedImageView tupianImg,
                            final FrameLayout mengcengLayout, final TextView mengcengTv,
                            final int position) {
        ViewVisibleUtils.setVisibleGone(tupianImg, false);
        ViewVisibleUtils.setVisibleGone(mengcengLayout, true);
        TextViewUtils.setText(mengcengTv, "0%");

        File file = new File(fileUrl);
        if (!file.exists()) {
            return;
        }
        //重点，将图片fid修改为最终的fid
        final File finalFile = FileUtils.getFinalFileFid(file);
        if (finalFile == null) {
            Toast.makeText(mContext, "上传出错", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!finalFile.exists()) {
            Toast.makeText(mContext, "上传出错", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressResponseCallBack progressResponseCallBack = new ProgressResponseCallBack() {
            @Override
            public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {
                final int progress = (int) (bytesWritten * 100 / contentLength);
                Log.d("xxxxxx", "progress:" + progress);
                //需要切换到主线程
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ViewVisibleUtils.setVisibleGone(tupianImg, false);
                        ViewVisibleUtils.setVisibleGone(mengcengLayout, true);
                        TextViewUtils.setText(mengcengTv, progress + "%");
                    }
                });
            }
        };
        DialogUtils.showLoading(loadingDialog);
        //注意，上传成功之后是否需要删掉本地的图？后期再考虑
        EasyHttp.post("/file_upload")//这个路径表示上传，但服务器其实是把图片保存到/upload里，访问就是从/upload里取
                .baseUrl(BaseAction.DOMAIN_PIC)
                .params("img", finalFile, finalFile.getName(), progressResponseCallBack)//这个key，img，要跟nodejs后台的一个值对应起来
                .execute(new HttpCallBack<String>() {
                    @Override
                    public void onSuccess(String message) {
                        Log.d("xxxxxx", "onSuccess:" + message);
                        DialogUtils.dimiss(loadingDialog);
                        RequestOptions options = new RequestOptions();
                        options.placeholder(R.drawable.ic_image_zhanwei)
                                .error(R.drawable.ic_image_zhanwei);
                        if (position == -1) {//更换头像
                            Glide.with(MyBaseInfoActivity_new.this)
                                    .load(BaseAction.DOMAIN_PIC + "/" + BaseAction.DOMAIN_PIC_UPLOAD + finalFile.getName())
                                    .apply(options)
                                    .into(tupianImg);
                            touXiangInfoList.clear();
                            //存入时，不用把域名存入
                            touXiangInfoList.add(new UserViewInfo(BaseAction.DOMAIN_PIC_UPLOAD + finalFile.getName()));
                        } else if (0 <= position && position <= zhaoPianInfoList.size() - 1) {//更换普通照片
                            Glide.with(MyBaseInfoActivity_new.this)
                                    .load(BaseAction.DOMAIN_PIC + "/" + BaseAction.DOMAIN_PIC_UPLOAD + finalFile.getName())
                                    .apply(options)
                                    .into(tupianImg);
                            zhaoPianInfoList.set(position, new UserViewInfo(BaseAction.DOMAIN_PIC_UPLOAD + finalFile.getName()));
                        } else {//添加照片
                            Glide.with(MyBaseInfoActivity_new.this)
                                    .load(BaseAction.DOMAIN_PIC + "/" + BaseAction.DOMAIN_PIC_UPLOAD + finalFile.getName())
                                    .apply(options)
                                    .into(tupianImg);
                            zhaoPianInfoList.add(new UserViewInfo(BaseAction.DOMAIN_PIC_UPLOAD + finalFile.getName()));
                        }
                        ViewVisibleUtils.setVisibleGone(tupianImg, true);
                        ViewVisibleUtils.setVisibleGone(mengcengLayout, false);
                    }

                    @Override
                    public void onError(ApiException e) {
                        Log.d("xxxxxx", "onError");
                        DialogUtils.dimiss(loadingDialog);
                        if (position == -1) {
                            touXiangInfoList.clear();
                        } else if (0 <= position && position <= zhaoPianInfoList.size() - 1) {
                            zhaoPianInfoList.set(position, new UserViewInfo(""));
                        } else {
                            zhaoPianInfoList.add(new UserViewInfo(""));
                        }
                        ViewVisibleUtils.setVisibleGone(tupianImg, false);
                        ViewVisibleUtils.setVisibleGone(mengcengLayout, true);
                        TextViewUtils.setText(mengcengTv, "加载出错");
                    }
                });

    }

    //如果头像出现加载出错，或者某张照片加载出错，则不能保存个人信息
    private String checkTouxiangAndZhaoPianHefa() {
        String freeImgList = "";//若最终返回空，则说明中间发生了错误
        if (Utils.isEmptyCollection(touXiangInfoList)) {
            Toast.makeText(this, "头像加载出错，请重新选择", Toast.LENGTH_SHORT).show();
            return freeImgList;
        }

        if (Utils.isNotEmptyCollection(zhaoPianInfoList)) {
            try {
                JSONArray jsonArray = new JSONArray();
                for (UserViewInfo userViewInfo : zhaoPianInfoList) {
                    JSONObject jsonObject = new JSONObject();
                    if (userViewInfo == null) {
                        Toast.makeText(this, "有加载出错的照片，请重新选择", Toast.LENGTH_SHORT).show();
                        return freeImgList;
                    }
                    if (TextUtils.isEmpty(userViewInfo.getUrl())) {
                        Toast.makeText(this, "有加载出错的照片，请重新选择", Toast.LENGTH_SHORT).show();
                        return freeImgList;
                    }
                    //存入数据库只需后边一段，把前面域名一段去掉
                    jsonObject.put("imgUrl", userViewInfo.getUrl()
                            .replace(BaseAction.DOMAIN_PIC + "/", ""));
                    jsonArray.put(jsonObject);
                }
                freeImgList = jsonArray.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            freeImgList = "[]";//默认照片是一个空的json数组的字符串
        }

        return freeImgList;
    }

}
