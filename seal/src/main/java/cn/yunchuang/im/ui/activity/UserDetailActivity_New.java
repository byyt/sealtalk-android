package cn.yunchuang.im.ui.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.previewlibrary.GPreviewBuilder;

import java.util.ArrayList;
import java.util.List;

import cn.yunchuang.im.R;
import cn.yunchuang.im.model.UserViewInfo;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.server.response.GetUserDetailModel;
import cn.yunchuang.im.server.response.GetUserDetailResponse;
import cn.yunchuang.im.server.response.ImageModel;
import cn.yunchuang.im.server.response.PayImgListResponse;
import cn.yunchuang.im.server.utils.CommonUtils;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.server.utils.StaticDataUtils;
import cn.yunchuang.im.server.widget.LoadDialog;
import cn.yunchuang.im.ui.adapter.UserDetailPicListAdapter;
import cn.yunchuang.im.widget.ObservableScrollView;

//CallKit start 1
//CallKit end 1

/**
 * Created by tiankui on 16/11/2.
 */

public class UserDetailActivity_New extends BaseActivity implements View.OnClickListener {

    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRecyclerView;
    private UserDetailPicListAdapter mPicdapter;//小图列表的adapter

    private ArrayList<String> mFreeImageList = new ArrayList<>(); //免费图片地址集合
    private ArrayList<String> mPayImageList = new ArrayList<>(); //付费图片地址集合
    private ArrayList<String> mAllImageList = new ArrayList<>(); //所有图片地址集合
    private List<UserViewInfo> mUserViewInfoList = new ArrayList<>(); //大图集合
    private String mUserId = "";

    private static final int GET_USER_DETAIL = 1601;
    private static final int GET_PAYED_IMG_LIST = 1602;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_new);
        setHeadVisibility(View.GONE);
        initView();
        getUserDetailData();
    }

    private void initView() {
        mUserId = getIntent().getStringExtra("userId");

        mRecyclerView = (RecyclerView) findViewById(R.id.user_detail_new_top_recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mPicdapter = new UserDetailPicListAdapter(this);
        mPicdapter.replaceData(mUserViewInfoList);
        mRecyclerView.setAdapter(mPicdapter);
        mPicdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                computeBoundsBackward(mLinearLayoutManager.findFirstVisibleItemPosition());
                GPreviewBuilder.from(UserDetailActivity_New.this)
                        .setData(mUserViewInfoList)
                        .setCurrentIndex(position)
                        .setSingleFling(true)
                        .setType(GPreviewBuilder.IndicatorType.Number)
                        .start();
            }
        });

    }

    private void getUserDetailData() {
        request(GET_USER_DETAIL);
    }

    private void getPayData() {
        request(GET_PAYED_IMG_LIST);
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case GET_USER_DETAIL:
                return action.getUserDetails(mUserId);
            case GET_PAYED_IMG_LIST:
                return action.getHashPayedImgList();
        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case GET_USER_DETAIL:
                    GetUserDetailResponse getUserDetailResponse = (GetUserDetailResponse) result;
                    if (getUserDetailResponse.getCode() == 200) {
                        NToast.shortToast(mContext, "获取个人信息成功");
                        updateData(getUserDetailResponse);
                    } else {
                        NToast.shortToast(mContext, "获取验证码失败，请稍后重试～");
                    }
                    break;
                case GET_PAYED_IMG_LIST:
                    PayImgListResponse payImgListResponse = (PayImgListResponse) result;
                    NToast.shortToast(mContext, "chengong");
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
            case GET_USER_DETAIL:
                NToast.shortToast(mContext, "获取个人信息失败");
                break;
            case GET_PAYED_IMG_LIST:
                NToast.shortToast(mContext, "获取验证码失败，请稍后重试～");
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }

    private void updateData(GetUserDetailResponse getUserDetailResponse) {
        if (getUserDetailResponse == null) {
            return;
        }
        GetUserDetailModel detailModel = getUserDetailResponse.getResult();
        if (detailModel == null) {
            return;
        }
        if (TextUtils.isEmpty(detailModel.getFreeImgList())) {
            return;
        }

        try {
            //将字符串的json数组转为list
            List<ImageModel> freeImageList = JSONArray.parseArray(detailModel.getFreeImgList(), ImageModel.class); //得到免费图片列表
            List<ImageModel> payImageList = detailModel.getPayImgList(); //得到付费图片列表
            List<ImageModel> resultImageList = new ArrayList<>();
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
                mAllImageList.clear();
                for (ImageModel imageModel : resultImageList) {
                    mAllImageList.add(imageModel.getImgUrl());
                }
                mUserViewInfoList.clear();
                for (int i = 0; i < mAllImageList.size(); i++) {
                    mUserViewInfoList.add(new UserViewInfo(mAllImageList.get(i)));
                }
                mPicdapter.replaceData(mUserViewInfoList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找信息
     * 从第一个完整可见item逆序遍历，如果初始位置为0，则不执行方法内循环
     */
    private void computeBoundsBackward(int firstCompletelyVisiblePos) {
        for (int i = firstCompletelyVisiblePos; i < mUserViewInfoList.size(); i++) {
            View itemView = mLinearLayoutManager.findViewByPosition(i);
            Rect bounds = new Rect();
            if (itemView != null) {
                ImageView thumbView = (ImageView) itemView.findViewById(R.id.user_detail_new_pic_item_img);
                thumbView.getGlobalVisibleRect(bounds);
            }
            mUserViewInfoList.get(i).setBounds(bounds);
        }
    }

    private int getDimen(int resourceId) {
        return getApplicationContext().getResources().getDimensionPixelOffset(resourceId);
    }

}
