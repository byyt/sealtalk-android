package cn.yunchuang.im.ui.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.previewlibrary.GPreviewBuilder;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import cn.yunchuang.im.R;
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
import cn.yunchuang.im.widget.GlideImageLoader;
import cn.yunchuang.im.widget.dialog.ConstantDialogUtils;
import cn.yunchuang.im.widget.dialog.VZyPayImgDialog;
import cn.yunchuang.im.widget.dialog.VZyPayWeChatDialog;
import cn.yunchuang.im.zmico.utils.BaseBaseUtils;

//CallKit start 1
//CallKit end 1

/**
 * Created by tiankui on 16/11/2.
 */

public class UserDetailActivity_New extends BaseActivity implements View.OnClickListener {

    private ScrollView scrollView;


    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRecyclerView;
    private UserDetailPicListAdapter mPicdapter;//小图列表的adapter
    private VZyPayImgDialog mVZyPayImgDialog;//弹出图片付费的对话框
    private VZyPayWeChatDialog mVZyPayWeChatDialog;//弹出微信号付费的对话框
    private TextView mSeeWeChat;

    private Banner banner;

    private GetUserDetailModelOne mUserDetailModelOne;
    private GetUserDetailModelTwo mUserDetailModelTwo;
    private ArrayList<String> mFreeImageList = new ArrayList<>(); //免费图片地址集合
    private ArrayList<String> mPayImageList = new ArrayList<>(); //付费图片地址集合
    private ArrayList<String> allImageList = new ArrayList<>(); //所有图片地址集合
    private List<UserViewInfo> userViewInfoList = new ArrayList<>(); //大图集合
    private List<ImageModel> resultImageList = new ArrayList<>(); //最原始总数据集合，免费图片加上付费图片,为了得到付费图片的id
    private String userId = "";
    private String imgId; //当前点击图片的id，主要针对付费图片


    private static final int GET_USER_DETAIL_ONE = 1601;
    private static final int GET_USER_DETAIL_TWO = 1602;
    private static final int PAY_IMG = 1603;
    private static final int PAY_WECHAT = 1604;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseBaseUtils.setFullScreen(this);
        setContentView(R.layout.activity_user_detail_new);
        setHeadVisibility(View.GONE);
        initView();
        getUserDetailData();
    }

    private void initView() {
        userId = getIntent().getStringExtra("userId");

        scrollView = (ScrollView) findViewById(R.id.user_detail_new_root_scrollview);
        scrollView.scrollTo(0, 0);

        mSeeWeChat = (TextView) findViewById(R.id.user_detail_new_wei_xin_cha_kan);
        mSeeWeChat.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.user_detail_new_top_recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mPicdapter = new UserDetailPicListAdapter(this);
        mPicdapter.replaceData(userViewInfoList);
        mRecyclerView.setAdapter(mPicdapter);
        mPicdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                imgId = resultImageList.get(position).getId();
                if (StaticDataUtils.isInBlurImgUrlList(userViewInfoList.get(position).getUrl())) {
                    //如果点击的是需要付费的照片，则弹出框让用户付费
                    showPayImgDialog();
                } else {
                    //否则进入大图浏览模式
                    computeBoundsBackward(mLinearLayoutManager.findFirstVisibleItemPosition());
                    GPreviewBuilder.from(UserDetailActivity_New.this)
                            .setData(userViewInfoList)
                            .setCurrentIndex(position)
                            .setSingleFling(true)
                            .setType(GPreviewBuilder.IndicatorType.Number)
                            .start();
                }
            }
        });

        findViewById(R.id.user_detail_new_back).setOnClickListener(this);

        initBanner();

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
    }

    private void setBannerData(List<String> images) {
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
//        banner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
//        banner.isAutoPlay(true);
        //设置轮播时间
//        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
//        banner.setIndicatorGravity(BannerConfig.RIGHT);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    private void getUserDetailData() {
        request(GET_USER_DETAIL_ONE);
        request(GET_USER_DETAIL_TWO);
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case GET_USER_DETAIL_ONE:
                return action.getUserDetailOne(userId);
            case GET_USER_DETAIL_TWO:
                return action.getUserDetailTwo(userId);
            case PAY_IMG:
                return action.payImg(imgId);
            case PAY_WECHAT:
                return action.payWeChat(mUserDetailModelTwo.getWeChat(), mUserDetailModelTwo.getWeChatPrice());
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
        switch (v.getId()) {
            case R.id.user_detail_new_wei_xin_cha_kan:
                showPayWeChatDialog();
                break;
            case R.id.user_detail_new_back:
                finish();
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
        GetUserDetailModelOne detailModel = getUserDetailOneResponse.getResult();
        if (detailModel == null) {
            return;
        }
        mUserDetailModelOne = detailModel;
        if (TextUtils.isEmpty(detailModel.getFreeImgList())) {
            return;
        }
        //免费图片，和付费图片的处理
        try {
            //将字符串的json数组转为list
            List<ImageModel> freeImageList = JSONArray.parseArray(detailModel.getFreeImgList(), ImageModel.class); //得到免费图片列表
            List<ImageModel> payImageList = detailModel.getPayImgList(); //得到仍需付费图片列表
            resultImageList = new ArrayList<>();
            //两个列表拼起来
            if (freeImageList != null) {
                resultImageList.addAll(freeImageList);
            }
            if (payImageList != null) {
                resultImageList.addAll(payImageList);
                ArrayList<String> blurImgUrlList = new ArrayList<>();
                for (ImageModel imageModel : payImageList) {
                    blurImgUrlList.add(imageModel.getImgUrl());
                }
                StaticDataUtils.setBlurImgUrlList(blurImgUrlList); //设置需要模糊显示的图片
            } else {
                StaticDataUtils.setBlurImgUrlList(new ArrayList<String>()); //默认没有图片模糊显示
            }
            if (resultImageList != null && resultImageList.size() > 0) {
                allImageList.clear();
                for (ImageModel imageModel : resultImageList) {
                    allImageList.add(imageModel.getImgUrl());
                }
                userViewInfoList.clear();
                for (int i = 0; i < allImageList.size(); i++) {
                    userViewInfoList.add(new UserViewInfo(allImageList.get(i)));
                }
                mPicdapter.replaceData(userViewInfoList);
                setBannerData(allImageList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新第二部分数据
     *
     * @param getUserDetailTwoResponse
     */
    private void updateDataTwo(GetUserDetailTwoResponse getUserDetailTwoResponse) {
        mSeeWeChat.setText("");
        mSeeWeChat.setClickable(false);
        if (getUserDetailTwoResponse == null) {
            return;
        }
        GetUserDetailModelTwo detailModel = getUserDetailTwoResponse.getResult();
        if (detailModel == null) {
            return;
        }
        mUserDetailModelTwo = detailModel;
        if (mUserDetailModelTwo.isHasPayedWeChat()) {
            mSeeWeChat.setText(mUserDetailModelTwo.getWeChat());
            mSeeWeChat.setClickable(false);
        } else {
            mSeeWeChat.setText("查看");
            mSeeWeChat.setClickable(true);
        }

    }

    /**
     * 弹出图片需要付费对话框
     */
    private void showPayImgDialog() {
        mVZyPayImgDialog = new VZyPayImgDialog(this, new VZyPayImgDialog.CallBack() {
            @Override
            public void callback() {
                Log.d("xxxxxx", "showPayImgDialog");
                request(PAY_IMG);
            }
        });
        mVZyPayImgDialog.showDialog(ConstantDialogUtils.LEVEL_3, ConstantDialogUtils.OPERATEWINDOW);
    }

    /**
     * 弹出微信号需要付费对话框
     */
    private void showPayWeChatDialog() {
        if (mUserDetailModelTwo == null) {
            return;
        }
        if (TextUtils.isEmpty(mUserDetailModelTwo.getWeChat()) || mUserDetailModelTwo.getWeChatPrice() <= 0) {
            return;
        }
        //理论上不会出现上面两种情况，如果出现上面两种情况不会出现查看微信这个按钮
        mVZyPayWeChatDialog = new VZyPayWeChatDialog(this, new VZyPayWeChatDialog.CallBack() {
            @Override
            public void callback() {
                Log.d("xxxxxx", "showPayImgDialog");
                request(PAY_WECHAT);
            }
        });
        mVZyPayWeChatDialog.setContent("查看TA的微信需要" + mUserDetailModelTwo.getWeChatPrice() + "元，是否支付？");

        mVZyPayWeChatDialog.showDialog(ConstantDialogUtils.LEVEL_3, ConstantDialogUtils.OPERATEWINDOW);
    }


    /**
     * 大图浏览，计算每个图片的位置
     * 从第一个完整可见item逆序遍历，如果初始位置为0，则不执行方法内循环
     */
    private void computeBoundsBackward(int firstCompletelyVisiblePos) {
        for (int i = firstCompletelyVisiblePos; i < userViewInfoList.size(); i++) {
            View itemView = mLinearLayoutManager.findViewByPosition(i);
            Rect bounds = new Rect();
            if (itemView != null) {
                ImageView thumbView = (ImageView) itemView.findViewById(R.id.user_detail_new_pic_item_img);
                thumbView.getGlobalVisibleRect(bounds);
            }
            userViewInfoList.get(i).setBounds(bounds);
        }
    }

    private int getDimen(int resourceId) {
        return getApplicationContext().getResources().getDimensionPixelOffset(resourceId);
    }

}
