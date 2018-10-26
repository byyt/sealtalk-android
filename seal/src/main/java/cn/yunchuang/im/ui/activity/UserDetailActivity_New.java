package cn.yunchuang.im.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;

import java.util.concurrent.CopyOnWriteArrayList;

import cn.yunchuang.im.R;
import cn.yunchuang.im.utils.CommonUtils;
import cn.yunchuang.im.widget.banner.Banner;
import cn.yunchuang.im.widget.banner.BannerConfig;
import cn.yunchuang.im.widget.banner.BannerImageLoader;

//CallKit start 1
//CallKit end 1

/**
 * Created by tiankui on 16/11/2.
 */

public class UserDetailActivity_New extends BaseActivity implements View.OnClickListener {

    private Banner mBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_new);
        setHeadVisibility(View.GONE);
        initView();

    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mBanner.startAutoPlay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },200);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (CommonUtils.isOnMainThread()) {
//            Glide.with(mContext).pauseRequests();
//        }
    }

    private void initView() {
        mBanner = (Banner) findViewById(R.id.auto_slide_view);
        initBanner();
    }

    /**
     * 初始化banner
     */
    private void initBanner() {
        mBanner.setActivity(this);
        mBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.setImages(new CopyOnWriteArrayList<Object>()).setImageLoader(new BannerImageLoader());
        Drawable placeholder = getApplicationContext().getResources().getDrawable(R.drawable.aaa);
        CopyOnWriteArrayList<Object> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        copyOnWriteArrayList.add(placeholder);
        copyOnWriteArrayList.add(placeholder);
        copyOnWriteArrayList.add(placeholder);
        copyOnWriteArrayList.add(placeholder);
        mBanner.update(copyOnWriteArrayList);
    }

    @Override
    public void onClick(View v) {

    }
}
