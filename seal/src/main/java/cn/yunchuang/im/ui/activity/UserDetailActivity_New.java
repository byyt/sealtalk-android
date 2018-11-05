package cn.yunchuang.im.ui.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.previewlibrary.GPreviewBuilder;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import cn.yunchuang.im.R;
import cn.yunchuang.im.model.UserViewInfo;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.server.response.PayImgListResponse;
import cn.yunchuang.im.server.response.SendCodeResponse;
import cn.yunchuang.im.server.response.UserDetailModel;
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

public class UserDetailActivity_New extends BaseActivity implements View.OnClickListener, OnBannerListener {

    private LinearLayout mPictureListLayout;
    private ObservableScrollView mObservableScrollView;

    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRecyclerView;

    private ArrayList<String> mImageList = new ArrayList<>(); //小图图片地址集合
    private List<UserViewInfo> mUserViewInfoList = new ArrayList<>(); //大图集合
    private UserDetailModel mUserDetailModel;

    private static final int GET_PAYED_IMG_LIST = 1601;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_new);
        setHeadVisibility(View.GONE);
        initView();

        getPayData();
    }

    private void initView() {
        mPictureListLayout = (LinearLayout) findViewById(R.id.user_detail_new_top_list_layout);
        mObservableScrollView = (ObservableScrollView) findViewById(R.id.user_detail_new_top_scrollview);


        mImageList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            mImageList.add("http://192.168.1.236:8081/aaa.jpg");
            mImageList.add("http://192.168.1.236:8081/123.png");
            mImageList.add("http://192.168.1.236:8081/aac.jpg");
            mImageList.add("http://192.168.1.236:8081/bbb.jpg");
            mImageList.add("http://192.168.1.236:8081/banner_moren.png");
        }

        //设置模糊照片的url
        ArrayList<String> blurImgUrlList = new ArrayList<>();
        blurImgUrlList.add("http://192.168.1.236:8081/bbb.jpg");
        blurImgUrlList.add("http://192.168.1.236:8081/aac.jpg");
        StaticDataUtils.setBlurImgUrlList(blurImgUrlList);

        mUserDetailModel = new UserDetailModel();
        mUserDetailModel.setImgList(mImageList);

        mUserViewInfoList = new ArrayList<>();
        for (int i = 0; i < mImageList.size(); i++) {
            mUserViewInfoList.add(new UserViewInfo(mImageList.get(i)));
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.user_detail_new_top_recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        UserDetailPicListAdapter adapter = new UserDetailPicListAdapter(this);
        adapter.addData(mUserViewInfoList);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
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

//        updatePictureList();
    }

    private void getPayData(){
        request(GET_PAYED_IMG_LIST);
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case GET_PAYED_IMG_LIST:
                return action.getHashPayedImgList();
        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
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
            case GET_PAYED_IMG_LIST:
                NToast.shortToast(mContext, "获取验证码失败，请稍后重试～");
                break;
        }
    }

    private void updatePictureList() {
        if (mUserDetailModel != null && mUserDetailModel.getImgList() != null
                && mUserDetailModel.getImgList().size() > 0) {
            mObservableScrollView.setVisibility(View.VISIBLE);
            mPictureListLayout.removeAllViews();
            //加上一块左边的缝隙
            View leftGap = new View(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getDimen(R.dimen.px30), ViewGroup.LayoutParams.MATCH_PARENT);
            leftGap.setLayoutParams(layoutParams);
            mPictureListLayout.addView(leftGap);
            for (int i = 0; i < mUserDetailModel.getImgList().size(); i++) {
                try {
                    View view = createPicItem(mUserDetailModel.getImgList().get(i), i);
                    mPictureListLayout.addView(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            mObservableScrollView.setVisibility(View.GONE);
        }
    }

    /**
     * 创建顶部图片列表item
     *
     * @param imgUrl
     * @param position
     * @return
     */
    private View createPicItem(String imgUrl, final int position) {
        View view = LayoutInflater.from(UserDetailActivity_New.this)
                .inflate(R.layout.view_user_detail_new_pic_item, mPictureListLayout, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.user_detail_new_pic_item_img);

        UserViewInfo userViewInfo = new UserViewInfo(imgUrl);
//        Rect bounds = new Rect();
//        imageView.getGlobalVisibleRect(bounds);//getGlobalVisibleRect方法的作用是获取视图在屏幕坐标中的可视区域
//        userViewInfo.setBounds(bounds); //获取小图点击位置，出现一个动画效果，从该位置进入大图
        mUserViewInfoList.add(userViewInfo);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBigPicBrowse(position); //点击小图，进入大图浏览模式
            }
        });

        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.ic_default_image).error(R.drawable.ic_default_image);
        Glide.with(this)
                .load(imgUrl)
                .apply(options)
                .into(imageView);

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void OnBannerClick(int position) {
        NToast.shortToast(mContext, "点击了第" + (position + 1) + "页");
//        Intent intent = new Intent(UserDetailActivity_New.this, PictureBrowseActivity.class);
//        intent.putExtra("posision", position);
//        intent.putStringArrayListExtra("imageList", mImageList);
//        startActivity(intent);
        ArrayList<UserViewInfo> imageList = new ArrayList<>();
        UserViewInfo userViewInfo1 = new UserViewInfo("http://192.168.1.236:8081/123.png");
        UserViewInfo userViewInfo2 = new UserViewInfo("http://192.168.1.236:8081/aaa.jpg");
        UserViewInfo userViewInfo3 = new UserViewInfo("http://192.168.1.236:8081/aac.jpg");
        UserViewInfo userViewInfo4 = new UserViewInfo("http://192.168.1.236:8081/bbb.jpg");
        UserViewInfo userViewInfo5 = new UserViewInfo("http://192.168.1.236:8081/banner_moren.png");

        imageList.add(userViewInfo1);
        imageList.add(userViewInfo2);
        imageList.add(userViewInfo3);
        imageList.add(userViewInfo4);
        imageList.add(userViewInfo5);

        GPreviewBuilder.from(UserDetailActivity_New.this)
                .setData(imageList)
                .setCurrentIndex(position)
                .setSingleFling(true)
                .setType(GPreviewBuilder.IndicatorType.Number)
                .start();
    }

    /**
     * 点击小图，进入大图浏览模式
     *
     * @param position
     */
    private void startBigPicBrowse(int position) {
        computeBoundsBackwardOld();
        GPreviewBuilder.from(UserDetailActivity_New.this)
                .setData(mUserViewInfoList)
                .setCurrentIndex(position)
                .setSingleFling(true)
                .setType(GPreviewBuilder.IndicatorType.Number)
                .start();
    }

    /**
     * 查找信息
     * 从第一个完整可见item逆序遍历，如果初始位置为0，则不执行方法内循环
     */
    private void computeBoundsBackward(int firstCompletelyVisiblePos) {
        for (int i = firstCompletelyVisiblePos;i < mUserViewInfoList.size(); i++) {
            View itemView = mLinearLayoutManager.findViewByPosition(i);
            Rect bounds = new Rect();
            if (itemView != null) {
                ImageView thumbView = (ImageView) itemView.findViewById(R.id.user_detail_new_pic_item_img);
                thumbView.getGlobalVisibleRect(bounds);
            }
            mUserViewInfoList.get(i).setBounds(bounds);
        }
    }

    /**
     * 查找信息
     * 从第一个完整可见item逆序遍历，如果初始位置为0，则不执行方法内循环
     */
    private void computeBoundsBackwardOld() {
        //从1开始，第0个是左边的leftGap
        for (int i = 1; i < mPictureListLayout.getChildCount() - 1; i++) {
            View view = mPictureListLayout.getChildAt(i);
            Rect bounds = new Rect();
            if (view != null) {
                ImageView imageView = (ImageView) view.findViewById(R.id.user_detail_new_pic_item_img);
                imageView.getGlobalVisibleRect(bounds);
            }
            mUserViewInfoList.get(i - 1).setBounds(bounds);
        }
    }

    private int getDimen(int resourceId) {
        return getApplicationContext().getResources().getDimensionPixelOffset(resourceId);
    }
}
