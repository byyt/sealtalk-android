package cn.yunchuang.im.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.youth.banner.Banner;

import java.util.ArrayList;

import cn.yunchuang.im.R;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.widget.GlideImageLoader;

//CallKit start 1
//CallKit end 1

/**
 * Created by tiankui on 16/11/2.
 */

public class PictureBrowseActivity extends BaseActivity {

    private Banner mBanner;
    private TextView mIndicatorTv;
    private int mCurrentPosition;
    private ArrayList<String> mImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_browse);
        setHeadVisibility(View.GONE);
        mCurrentPosition = getIntent().getIntExtra("posision", 0);
        mImageList = getIntent().getStringArrayListExtra("imageList");
        initView();

    }

    private void initView() {
        initBanner();
    }

    /**
     * 初始化banner
     */
    private void initBanner() {

        if (mImageList == null || mImageList.size() == 0) {
            return;
        }

        mBanner = (Banner) findViewById(R.id.activity_picture_browse_banner);
        mIndicatorTv = (TextView) findViewById(R.id.activity_picture_browse_tv);

        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(mImageList);
        //设置滑动监听
        mBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                NToast.shortToast(mContext, "浏览了第" + (position + 1) + "页");
                mCurrentPosition = position;
                setIndicator();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //无需自动轮播
        mBanner.isAutoPlay(false);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
        //设置下方图片浏览的当前位置
        setIndicator();
    }

    private void setIndicator() {
        if (mImageList == null || mImageList.size() == 0) {
            return;
        }
        mIndicatorTv.setText((mCurrentPosition + 1) + "/" + mImageList.size());
    }

}
