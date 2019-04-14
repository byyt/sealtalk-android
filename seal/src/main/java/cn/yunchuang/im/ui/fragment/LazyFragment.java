package cn.yunchuang.im.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jony on 2016/11/3.
 * <p>
 * lazyFragment是专门针对我们的业务加载场景设计的使用方式
 * 原理是根据fragment的可见性来进行的判断，只有当前的fragment真的可见了才会开始加载数据
 * <p>
 * 通过标志位保证懒加载只执行一次 onLazyLoad
 */

public abstract class LazyFragment extends Fragment {

    // 是否可见，是否已经初始化
    private boolean mIsVisibleToUser = false, isViewInit = false;
    private boolean isNeedRefresh = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        isViewInit = true;
        initView(view, inflater, savedInstanceState);
        loadOrNot(mIsVisibleToUser);
        return view;
    }

    protected abstract int getLayoutRes();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        loadOrNot(isVisibleToUser);

        // 当变为可见时候，需要判断是否进行数据刷新
        if (isNeedRefresh) {
            isNeedRefresh = false;
            onNeedRefresh();
        }
    }

    protected void loadOrNot(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            onViewShow(isViewInit);
            if (isViewInit) {
                // 已经初始化过，不需要严格同步
                isViewInit = false;
                isNeedRefresh = false;
                onLazyLoad();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (getUserVisibleHint()) { // 适用于前后台切换

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (getUserVisibleHint()) {

        }
    }

    // 当延迟加载开始
    protected abstract void onLazyLoad();

    // 当页面真正显示
    protected void onViewShow(boolean isViewInit) {

    }

    // 开始加载数据
    protected void onNeedRefresh() {
    }

    protected abstract void initView(View view, LayoutInflater inflater, Bundle savedInstanceState);

    // 设置开始加载数据
    protected void setNeedRefresh(boolean isNeedRefresh) {
        if (getUserVisibleHint()) { // 当前页面是否可见
            if (isNeedRefresh) {
                onNeedRefresh();
            }
        } else {
            this.isNeedRefresh = isNeedRefresh;
        }
    }
}
