package cn.yunchuang.im.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.loader.PhotoClickListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.yunchuang.im.MeService;
import cn.yunchuang.im.R;
import cn.yunchuang.im.event.RefreshMineInfoEvent;
import cn.yunchuang.im.model.UserViewInfo;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.server.response.BaseResponse;
import cn.yunchuang.im.server.response.GetUserDetailModelOne;
import cn.yunchuang.im.server.response.GetUserDetailModelTwo;
import cn.yunchuang.im.server.response.GetUserDetailOneResponse;
import cn.yunchuang.im.server.response.GetUserDetailTwoResponse;
import cn.yunchuang.im.server.response.ImageModel;
import cn.yunchuang.im.server.utils.CommonUtils;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.server.utils.StaticDataUtils;
import cn.yunchuang.im.server.widget.LoadDialog;
import cn.yunchuang.im.ui.adapter.UserDetailPicListAdapter;
import cn.yunchuang.im.utils.DateUtils;
import cn.yunchuang.im.widget.GlideImageLoader;
import cn.yunchuang.im.widget.dialog_hdzy.CommonDialog;
import cn.yunchuang.im.widget.dialog_hdzy.ConstantDialogUtils;
import cn.yunchuang.im.zmico.SquareLayout;
import cn.yunchuang.im.zmico.utils.BaseBaseUtils;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import cn.yunchuang.im.zmico.utils.ResourceUtils;
import cn.yunchuang.im.zmico.utils.Utils;

//CallKit start 1
//CallKit end 1

/**
 * Created by tiankui on 16/11/2.
 */

public class UserDetailActivity_New extends BaseActivity implements View.OnClickListener
        , NestedScrollView.OnScrollChangeListener {

    private NestedScrollView nestedScrollView;
    private SquareLayout squareLayout;
    //标题栏
    private FrameLayout titleRootLayout;
    private FrameLayout titleLayoutTranslate;
    private FrameLayout titleLayoutWhite;
    private View titleLine;
    private ImageView backImgTranslate;
    private ImageView backImgBlack;
    private ImageView moreOpImgTranslate;
    private ImageView moreOpImgBlack;
    private TextView titleTv;

    //轮播图
    private Banner banner;

    //个人信息
    private TextView feedBackRateTv;
    private TextView heightTv;
    private TextView locationTv;
    private TextView nameTv;
    private TextView sexAgeTv;
    private TextView followNumTv;
    private TextView fansNumTv;
    private TextView qianMingTv;

    //微信和证件照
    private LinearLayout weChatIdentifyImgLayout;
    private TextView seeWeChat;
    private TextView weChatPrice;

    //付费作品
    private LinearLayout productionsLayout;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private UserDetailPicListAdapter picdapter;//小图列表的adapter


    private GetUserDetailModelOne userDetailModelOne;
    private GetUserDetailModelTwo userDetailModelTwo;
    private ArrayList<String> freeImageList = new ArrayList<>(); //免费图片地址集合，在banner中展示
    private List<UserViewInfo> freeUserViewInfoList = new ArrayList<>(); //免费图片大图集合
    private List<UserViewInfo> payUserViewInfoList = new ArrayList<>(); //付费图片大图集合，即作品一栏的图片
    private List<ImageModel> payResultImageList = new ArrayList<>(); //最终的付费图片集合，确定哪些已经付费，哪些还未付费
    private String userId = "";
    private int imgId; //当前点击图片的id，主要针对付费图片
    private int imgPrice; //当前点击图片的id，主要针对付费图片

    private LinearLayout skillsLayout;

    private static final int GET_USER_DETAIL_ONE = 1601;
    private static final int GET_USER_DETAIL_TWO = 1602;
    private static final int PAY_IMG = 1603;
    private static final int PAY_WECHAT = 1604;

    private int statusBarHeight = 0;
    private boolean clickBannerPic = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseBaseUtils.setTranslucentStatus(this);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_user_detail_new);
        setHeadVisibility(View.GONE);
        initView();
        getUserDetailData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        userId = getIntent().getStringExtra("userId");

        nestedScrollView = (NestedScrollView) findViewById(R.id.user_detail_new_root_scrollview);
        nestedScrollView.setOnScrollChangeListener(this);
        squareLayout = (SquareLayout) findViewById(R.id.user_detail_new_top_square_layout);

        titleRootLayout = (FrameLayout) findViewById(R.id.user_detail_new_title_root_layout);
        titleLayoutTranslate = (FrameLayout) findViewById(R.id.user_detail_new_title_layout_translate);
        titleLayoutWhite = (FrameLayout) findViewById(R.id.user_detail_new_title_layout_white);
        titleLine = findViewById(R.id.user_detail_new_title_line);
        backImgTranslate = (ImageView) findViewById(R.id.user_detail_new_back_translate);
        backImgTranslate.setOnClickListener(this);
        backImgBlack = (ImageView) findViewById(R.id.user_detail_new_back_black);
        backImgBlack.setOnClickListener(this);
        moreOpImgTranslate = (ImageView) findViewById(R.id.user_detail_new_more_op_translate);
        moreOpImgTranslate.setOnClickListener(this);
        moreOpImgBlack = (ImageView) findViewById(R.id.user_detail_new_more_op_black);
        moreOpImgBlack.setOnClickListener(this);
        titleTv = (TextView) findViewById(R.id.user_detail_new_title_tv);
        initMoreOrEdit();
        initTitleLayout();

        feedBackRateTv = (TextView) findViewById(R.id.user_detail_new_feed_back_rate);
        heightTv = (TextView) findViewById(R.id.user_detail_new_height);
        locationTv = (TextView) findViewById(R.id.user_detail_new_location);
        nameTv = (TextView) findViewById(R.id.user_detail_new_name);
        sexAgeTv = (TextView) findViewById(R.id.user_detail_new_sex_age);
        followNumTv = (TextView) findViewById(R.id.user_detail_new_follow_num);
        fansNumTv = (TextView) findViewById(R.id.user_detail_new_fans_num);
        qianMingTv = (TextView) findViewById(R.id.user_detail_new_qian_ming);

        weChatIdentifyImgLayout = (LinearLayout) findViewById(R.id.user_detail_new_wechat_identify_img);
        seeWeChat = (TextView) findViewById(R.id.user_detail_new_wei_xin_cha_kan);
        seeWeChat.setOnClickListener(this);
        weChatPrice = (TextView) findViewById(R.id.user_detail_new_wei_xin_price);

        productionsLayout = (LinearLayout) findViewById(R.id.user_detail_new_productions_layout);
        recyclerView = (RecyclerView) findViewById(R.id.user_detail_new_top_recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        picdapter = new UserDetailPicListAdapter(this);
        picdapter.replaceData(payUserViewInfoList);
        recyclerView.setAdapter(picdapter);
        picdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                imgId = payResultImageList.get(position).getId();
                imgPrice = payResultImageList.get(position).getImgPrice();
                if (StaticDataUtils.isInBlurImgUrlList(payResultImageList.get(position).getImgUrl())) {
                    //如果点击的是需要付费的照片，则弹出框让用户付费
                    showPayImgDialog(position);

                } else {
                    //否则进入大图浏览模式
                    computeBoundsBackwardPay(linearLayoutManager.findFirstVisibleItemPosition());
                    GPreviewBuilder.from(UserDetailActivity_New.this)
                            .setData(payUserViewInfoList)
                            .setCurrentIndex(position)
                            .setSingleFling(true)
                            .setType(GPreviewBuilder.IndicatorType.Number)
                            .start();
                }
            }
        });

        skillsLayout = (LinearLayout) findViewById(R.id.user_detail_new_skills_layout);

        initBanner();

        statusBarHeight = DeviceUtils.getStatusBarHeightPixels(this);

        nestedScrollView.scrollTo(0, 0);
    }

    private void initMoreOrEdit() {//如果查看的是自己，右上角为编辑按钮
        if (userId.equals(MeService.getUid())) {
            moreOpImgTranslate.setImageResource(R.drawable.mine_fragment_bianji);
            moreOpImgBlack.setImageResource(R.drawable.mine_fragment_bianji_black);
        } else {
            moreOpImgTranslate.setImageResource(R.drawable.user_detail_more_op);
            moreOpImgBlack.setImageResource(R.drawable.user_detail_more_black);
        }

    }

    private void initTitleLayout() {
        //设置标题栏距离顶部的距离，这个距离就是状态栏的高度
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DeviceUtils.dpToPx(48));
        layoutParams.setMargins(0, DeviceUtils.getStatusBarHeightPixels(this), 0, 0);
        titleLayoutTranslate.setLayoutParams(layoutParams);
        titleLayoutWhite.setLayoutParams(layoutParams);
        titleLayoutTranslate.setVisibility(View.VISIBLE);
        titleLayoutWhite.setVisibility(View.GONE);
    }

    private void initBanner() {
        banner = (Banner) findViewById(R.id.user_detail_new_top_banner);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
//        banner.setImages(images);
        //设置banner动画效果
//        banner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
        banner.isAutoPlay(false);
        //设置轮播时间
//        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        //banner设置方法全部调用完毕时最后调用
//        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                clickBannerPic = true;//点击了banner的图进入了图片浏览模式
                computeBoundsBackwardFree(0);
                GPreviewBuilder.from(UserDetailActivity_New.this)
                        .setData(freeUserViewInfoList)
                        .setCurrentIndex(position)
                        .setSingleFling(true)
                        .setType(GPreviewBuilder.IndicatorType.Number)
                        .setOnPhotoClickListener(new PhotoClickListener() {//我给框架添加的一个监听
                            @Override
                            public void onPhotoClick(int index) {
                                if (banner != null) {
                                    //我给框架添加的一个方法，banner的setOnBannerListener下标从1开始
                                    banner.setCurrentItem(index + 1);
                                }
                            }
                        })
                        .start();
            }
        });
    }

    private void setBannerData(List<ImageModel> imageModels) {
        //设置图片集合
        freeImageList.clear();
        freeUserViewInfoList.clear();
        if (imageModels != null) {
            for (ImageModel imageModel : imageModels) {
                freeImageList.add(imageModel.getImgUrl());
                UserViewInfo userViewInfo = new UserViewInfo(imageModel.getImgUrl());
                freeUserViewInfoList.add(userViewInfo);
            }
        }
        banner.setImages(freeImageList);
        banner.start();
    }

    private void getUserDetailData() {
        request(GET_USER_DETAIL_ONE);
        request(GET_USER_DETAIL_TWO);
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (Utils.isNotNull(banner) && Utils.isNotNull(titleLayoutTranslate)
                && Utils.ensureNotNull(titleLayoutWhite)) {
            int height = banner.getMeasuredHeight() - titleLayoutTranslate.getMeasuredHeight() - statusBarHeight;
            if (scrollY >= height) {
                if (titleLayoutWhite.getVisibility() != View.VISIBLE) {
                    buildTitleBgWhile();
                }
            } else {
                if (titleLayoutTranslate.getVisibility() != View.VISIBLE) {
                    buildTitleBgTransparent();
                }
            }
        }
    }

    private void buildTitleBgWhile() {
//        ImageStatusBarCompat.setStatusBarColor(this, Color.WHITE);
        BaseBaseUtils.setStatusBarColor(this, Color.WHITE);
        titleRootLayout.setBackgroundColor(ResourceUtils.getColor(R.color.white));
        titleLayoutWhite.setVisibility(View.VISIBLE);
        titleLayoutTranslate.setVisibility(View.GONE);
    }

    private void buildTitleBgTransparent() {
//        ImageStatusBarCompat.setStatusBarColor(this, Color.TRANSPARENT);
        BaseBaseUtils.setTranslucentStatus(this);
        titleRootLayout.setBackground(ResourceUtils.getDrawable(R.drawable.md_profile_top_title_bg));
        titleLayoutWhite.setVisibility(View.GONE);
        titleLayoutTranslate.setVisibility(View.VISIBLE);
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case GET_USER_DETAIL_ONE:
                return action.getUserDetailOne(userId);
            case GET_USER_DETAIL_TWO:
                return action.getUserDetailTwo(userId);
            case PAY_IMG:
                return action.payImg(imgId, imgPrice);
            case PAY_WECHAT:
                return action.payWeChat(userDetailModelTwo.getWeChat(), userDetailModelTwo.getWeChatPrice());
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
                        NToast.shortToast(mContext, "获取个人信息成功1");
                        updateDataOne(getUserDetailOneResponse);
                    } else {
                        NToast.shortToast(mContext, "获取个人信息失败1");
                    }
                    break;
                case GET_USER_DETAIL_TWO:
                    GetUserDetailTwoResponse getUserDetailTwoResponse = (GetUserDetailTwoResponse) result;
                    if (getUserDetailTwoResponse.getCode() == 200) {
                        NToast.shortToast(mContext, "获取个人信息成功2");
                        updateDataTwo(getUserDetailTwoResponse);
                    } else {
                        NToast.shortToast(mContext, "获取个人信息失败2");
                    }
                    break;
                case PAY_IMG:
                    BaseResponse baseResponse = (BaseResponse) result;
                    if (baseResponse.getCode() == 200) {
                        NToast.shortToast(mContext, "图片付费成功");
                        request(GET_USER_DETAIL_ONE);
                    } else {
                        NToast.shortToast(mContext, "图片付费失败");
                    }
                    break;
                case PAY_WECHAT:
                    BaseResponse baseResponse2 = (BaseResponse) result;
                    if (baseResponse2.getCode() == 200) {
                        NToast.shortToast(mContext, "微信付费成功");
                        request(GET_USER_DETAIL_TWO);
                    } else {
                        NToast.shortToast(mContext, "微信付费失败");
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
                NToast.shortToast(mContext, "获取个人信息失败1");
                break;
            case GET_USER_DETAIL_TWO:
                NToast.shortToast(mContext, "获取个人信息失败2");
                break;
            case PAY_IMG:
                NToast.shortToast(mContext, "图片付费失败");
                break;
            case PAY_WECHAT:
                NToast.shortToast(mContext, "微信付费失败");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (Utils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.user_detail_new_wei_xin_cha_kan:
                showPayWeChatDialog();
                break;
            case R.id.user_detail_new_back_translate:
            case R.id.user_detail_new_back_black:
                finish();
                break;
            case R.id.user_detail_new_more_op_translate:
            case R.id.user_detail_new_more_op_black:
                if (userId.equals(MeService.getUid())) {
                    startActivity(new Intent(UserDetailActivity_New.this, MyBaseInfoActivity_new.class));
                } else {

                }
                break;
        }
    }

    /**
     * /更新第一部分数据
     *
     * @param getUserDetailOneResponse
     */
    private void updateDataOne(GetUserDetailOneResponse getUserDetailOneResponse) {
        if (getUserDetailOneResponse == null) {
            return;
        }
        GetUserDetailModelOne modelOne = getUserDetailOneResponse.getResult();
        if (modelOne == null) {
            return;
        }
        //个人信息
        feedBackRateTv.setText(MessageFormat.format("{0}{1}{2}", "好评率：", modelOne.getFeedback_rate(), "%"));
        heightTv.setText(MessageFormat.format("{0}{1}", String.valueOf(modelOne.getHeight()), "CM"));
        locationTv.setText(MessageFormat.format("{0}{1}", modelOne.getLocation(), "km"));
        nameTv.setText(modelOne.getNickname());
        sexAgeTv.setText(String.valueOf(DateUtils.getAge(modelOne.getBirthday())));
        followNumTv.setText(MessageFormat.format("{0}{1}", "关注 ", String.valueOf(modelOne.getFollowNum())));
        fansNumTv.setText(MessageFormat.format("{0}{1}", "粉丝 ", String.valueOf(modelOne.getFansNum())));
        qianMingTv.setText(modelOne.getQianMing());

        //技能信息
        updateSkillLayout(modelOne);

        //免费图片展示
        userDetailModelOne = modelOne;
        if (TextUtils.isEmpty(modelOne.getFreeImgList())) {
            return;
        }
        try {
            //将字符串的json数组转为list
            ImageModel imageModel = new ImageModel();
            imageModel.setImgUrl(modelOne.getPortraitUri());//记得把头像图片添加到轮播图里
            List<ImageModel> freeImgList = JSONArray.parseArray(modelOne.getFreeImgList(), ImageModel.class); //得到免废费图片列表
            freeImgList.add(0, imageModel);
            setBannerData(freeImgList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSkillLayout(GetUserDetailModelOne modelOne) {
        String skillJsonStr = modelOne.getSkills();
        if (skillJsonStr == null || skillJsonStr.equals("")) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(skillJsonStr);
            Iterator iterator = jsonObject.keys();
            //如果数据不为空，添加Ta的技能这一行
            if (iterator.hasNext()) {
                View skillTitleView = LayoutInflater.from(this).inflate(R.layout.layout_user_detail_skill_top_title, null);
                skillsLayout.addView(skillTitleView);
            }
            //一次添加技能列表，遍历json的key和value
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = jsonObject.getString(key);

                View view = LayoutInflater.from(this).inflate(R.layout.layout_user_detail_skill_item, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.user_detail_new_skill_item_iv);
                TextView nameTv = (TextView) view.findViewById(R.id.user_detail_new_skill_item_name_tv);
                TextView priceTv = (TextView) view.findViewById(R.id.user_detail_new_skill_item_price_tv);

                switch (key) {
                    case "跑步":
                        imageView.setImageResource(R.drawable.user_detail_paobu);
                        break;
                    case "健身":
                        imageView.setImageResource(R.drawable.user_detail_jianshen);
                        break;
                    case "吃饭":
                        imageView.setImageResource(R.drawable.user_detail_chifan);
                        break;
                    case "看电影":
                        imageView.setImageResource(R.drawable.user_detail_kandianying);
                        break;
                    default:
                        //记得添加一个其他图标
                        imageView.setImageResource(R.drawable.user_detail_paobu);
                        break;
                }
                nameTv.setText(key);
                priceTv.setText(value);

                skillsLayout.addView(view);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 更新第二部分数据，微信付费、付费图片、付费视频，付费情况
     *
     * @param getUserDetailTwoResponse
     */
    private void updateDataTwo(GetUserDetailTwoResponse getUserDetailTwoResponse) {
        if (getUserDetailTwoResponse == null) {
            weChatIdentifyImgLayout.setVisibility(View.GONE);
            productionsLayout.setVisibility(View.GONE);
            return;
        }
        GetUserDetailModelTwo modelTwo = getUserDetailTwoResponse.getResult();
        if (modelTwo == null) {
            weChatIdentifyImgLayout.setVisibility(View.GONE);
            productionsLayout.setVisibility(View.GONE);
            return;
        }

        //处理微信证件照信息情况
        handleWeChatIdentifyImgLayout(modelTwo);

        //付费图片付费情况
        handlePayImgLayout(modelTwo);

    }

    private void handleWeChatIdentifyImgLayout(GetUserDetailModelTwo modelTwo) {
        //没有填写微信号则不显示此行（后面还会加而且没有上传证件照）
        if (TextUtils.isEmpty(modelTwo.getWeChat())) {
            weChatIdentifyImgLayout.setVisibility(View.GONE);
            return;
        }

        weChatIdentifyImgLayout.setVisibility(View.VISIBLE);
        userDetailModelTwo = modelTwo;
        //微信付费情况
        if (userDetailModelTwo.isHasPayedWeChat()) {
            seeWeChat.setText(userDetailModelTwo.getWeChat());
            seeWeChat.setBackground(null);
            seeWeChat.setClickable(false);
            weChatPrice.setVisibility(View.GONE);
        } else {
            seeWeChat.setText("查看");
            seeWeChat.setBackground(ResourceUtils.getDrawable(R.drawable.user_detail_cha_kan_bg));
            seeWeChat.setClickable(true);
            weChatPrice.setVisibility(View.VISIBLE);
        }
    }

    private void handlePayImgLayout(GetUserDetailModelTwo modelTwo) {
        List<ImageModel> hasPayedImgList = modelTwo.getHasPayedImgList(); //得到已经付费图片列表
        List<ImageModel> notPayedImgList = modelTwo.getNotPayedImgList(); //得到未付费图片列表
        payResultImageList.clear();
        //两个列表拼起来
        if (hasPayedImgList != null) {
            payResultImageList.addAll(hasPayedImgList);
        }
        if (notPayedImgList != null) {
            payResultImageList.addAll(notPayedImgList);
            ArrayList<String> blurImgUrlList = new ArrayList<>();
            for (ImageModel imageModel : notPayedImgList) {
                blurImgUrlList.add(imageModel.getImgUrl());
            }
            StaticDataUtils.setBlurImgUrlList(blurImgUrlList); //设置需要模糊显示的图片
        } else {
            StaticDataUtils.setBlurImgUrlList(new ArrayList<String>()); //默认没有图片模糊显示
        }
        if (payResultImageList != null && payResultImageList.size() > 0) {
            //有数据，则展示作品一栏
            productionsLayout.setVisibility(View.VISIBLE);
            payUserViewInfoList.clear();
            UserViewInfo userViewInfo;
            for (int i = 0; i < payResultImageList.size(); i++) {
                userViewInfo = new UserViewInfo(payResultImageList.get(i).getImgUrl());
                userViewInfo.setImgPrice(payResultImageList.get(i).getImgPrice());
                payUserViewInfoList.add(userViewInfo);
            }
            picdapter.replaceData(payUserViewInfoList);
        } else {
            //没数据，则隐藏作品一栏
            productionsLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 弹出图片需要付费对话框
     */
    private void showPayImgDialog(final int position) {
        new CommonDialog.Builder()
                .context(UserDetailActivity_New.this)
                .title("温馨提示")
                .content("查看该图片需要" + payResultImageList.get(position).getImgPrice()
                        + "元，是否支付？")
                .sureCallback(new CommonDialog.CallBack() {
                    @Override
                    public void callback() {
                        request(PAY_IMG);
                    }
                })
                .build()
                .showDialog(ConstantDialogUtils.LEVEL_3, ConstantDialogUtils.OPERATEWINDOW, true);
    }

    /**
     * 弹出微信号需要付费对话框
     */
    private void showPayWeChatDialog() {
        if (userDetailModelTwo == null) {
            return;
        }
        if (TextUtils.isEmpty(userDetailModelTwo.getWeChat()) || userDetailModelTwo.getWeChatPrice() <= 0) {
            return;
        }
        //理论上不会出现上面两种情况，如果出现上面两种情况不会出现查看微信这个按钮
        new CommonDialog.Builder()
                .context(UserDetailActivity_New.this)
                .title("温馨提示")
                .content("查看TA的微信需要" + userDetailModelTwo.getWeChatPrice() + "元，是否支付？")
                .sureCallback(new CommonDialog.CallBack() {
                    @Override
                    public void callback() {
                        request(PAY_WECHAT);
                    }
                })
                .build()
                .showDialog(ConstantDialogUtils.LEVEL_3, ConstantDialogUtils.OPERATEWINDOW, false);
    }

    /**
     * 付费图片大图浏览，计算每个图片的位置
     * 从第一个完整可见item逆序遍历，如果初始位置为0，则不执行方法内循环
     */
    private void computeBoundsBackwardFree(int firstCompletelyVisiblePos) {
        for (int i = firstCompletelyVisiblePos; i < freeUserViewInfoList.size(); i++) {
            Rect bounds = new Rect();
            squareLayout.getGlobalVisibleRect(bounds);
            freeUserViewInfoList.get(i).setBounds(bounds);
        }
    }

    /**
     * 付费图片大图浏览，计算每个图片的位置
     * 从第一个完整可见item逆序遍历，如果初始位置为0，则不执行方法内循环
     */
    private void computeBoundsBackwardPay(int firstCompletelyVisiblePos) {
        for (int i = firstCompletelyVisiblePos; i < payUserViewInfoList.size(); i++) {
            View itemView = linearLayoutManager.findViewByPosition(i);
            Rect bounds = new Rect();
            if (itemView != null) {
                ImageView thumbView = (ImageView) itemView.findViewById(R.id.user_detail_new_pic_item_img);
                thumbView.getGlobalVisibleRect(bounds);
            }
            payUserViewInfoList.get(i).setBounds(bounds);
        }
    }

    private int getDimen(int resourceId) {
        return getApplicationContext().getResources().getDimensionPixelOffset(resourceId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshMineEvent(RefreshMineInfoEvent event) {
        if (Utils.isNotNull(event)) {
            getUserDetailData();
        }
    }


}
