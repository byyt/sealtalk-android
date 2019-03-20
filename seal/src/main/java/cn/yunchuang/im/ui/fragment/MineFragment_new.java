package cn.yunchuang.im.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jrmf360.rylib.JrmfClient;

import cn.yunchuang.im.R;
import cn.yunchuang.im.zmico.utils.BaseBaseUtils;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import cn.yunchuang.im.zmico.utils.Utils;

/**
 * Created by AMing on 16/6/21.
 * Company RongCloud
 */
public class MineFragment_new extends BaseFragment implements View.OnClickListener
        , NestedScrollView.OnScrollChangeListener {

    //滑动控件
    private NestedScrollView nestedScrollView;

    //顶部区域
    private FrameLayout topLayout;

    //标题栏
    private FrameLayout titleLayoutTranslate;
    private FrameLayout titleLayoutWhite;
    private ImageView bjImgTranslate;
    private ImageView bjImgBlack;

    private int statusBarHeight = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //设置标题栏高度，还有状态栏透明
        if (getActivity() != null) {
            BaseBaseUtils.setTranslucentStatus(getActivity());//状态栏透明
        }
        View view = inflater.inflate(R.layout.fragment_mine_new, container, false);
        initViews(view);
        return view;
    }


    private void initViews(View view) {

        nestedScrollView = (NestedScrollView) view.findViewById(R.id.fragment_mine_new_root_scrollview);
        nestedScrollView.setOnScrollChangeListener(this);

        topLayout = (FrameLayout) view.findViewById(R.id.fragment_mine_top_layout);

        titleLayoutTranslate = (FrameLayout) view.findViewById(R.id.fragment_mine_new_title_layout_translate);
        titleLayoutWhite = (FrameLayout) view.findViewById(R.id.fragment_mine_new_title_layout_white);
        bjImgTranslate = (ImageView) view.findViewById(R.id.fragment_mine_new_title_bianji);
        bjImgBlack = (ImageView) view.findViewById(R.id.fragment_mine_new_title_bianji_black);

        statusBarHeight = DeviceUtils.getStatusBarHeightPixels(getActivity());

        initTitleLayout();

        nestedScrollView.scrollTo(0, 0);
    }

    private void initTitleLayout() {
        //设置标题栏距离顶部的距离，这个距离就是状态栏的高度
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DeviceUtils.dpToPx(48));
        layoutParams.setMargins(0, statusBarHeight, 0, 0);
        titleLayoutTranslate.setLayoutParams(layoutParams);
        titleLayoutTranslate.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DeviceUtils.dpToPx(48) + statusBarHeight);
        titleLayoutWhite.setLayoutParams(layoutParams1);
        titleLayoutWhite.setVisibility(View.GONE);
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (Utils.isNotNull(topLayout) && Utils.isNotNull(titleLayoutTranslate)
                && Utils.ensureNotNull(titleLayoutWhite)) {
            int height = topLayout.getMeasuredHeight() - titleLayoutWhite.getMeasuredHeight();
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
        if (getActivity() == null) {
            return;
        }
//        StatusBarCompat.setStatusBarColor(this, Color.WHITE);
        BaseBaseUtils.setStatusBarColor(getActivity(), Color.WHITE);
        titleLayoutWhite.setVisibility(View.VISIBLE);
        titleLayoutTranslate.setVisibility(View.GONE);
    }

    private void buildTitleBgTransparent() {
        if (getActivity() == null) {
            return;
        }
//        StatusBarCompat.setStatusBarColor(this, Color.TRANSPARENT);
        BaseBaseUtils.setTranslucentStatus(getActivity());
        titleLayoutWhite.setVisibility(View.GONE);
        titleLayoutTranslate.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_wallet:
                JrmfClient.intentWallet(getActivity());
                break;
        }
    }


}
