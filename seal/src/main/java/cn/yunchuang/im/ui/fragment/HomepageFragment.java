package cn.yunchuang.im.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.yunchuang.im.R;
import cn.yunchuang.im.SealAppContext;
import cn.yunchuang.im.SealConst;
import cn.yunchuang.im.SealUserInfoManager;
import cn.yunchuang.im.pulltorefresh.library.PullToRefreshBase;
import cn.yunchuang.im.pulltorefresh.recyclerview.ObservableRecyclerView;
import cn.yunchuang.im.pulltorefresh.recyclerview.PullToRefreshMRecyclerView;
import cn.yunchuang.im.pulltorefresh.recyclerview.RecycleViewDivider;
import cn.yunchuang.im.server.broadcast.BroadcastManager;
import cn.yunchuang.im.server.response.HomepageResponse;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.ui.adapter.HomepageAdapter;
import cn.yunchuang.im.utils.DpOrSp2PxUtil;

/**
 * tab 2 通讯录的 Fragment
 * Created by Bob on 2015/1/25.
 */
public class HomepageFragment extends BaseFragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener<ObservableRecyclerView> {

    private PullToRefreshMRecyclerView pullToRefreshMRecyclerView;
    private HomepageAdapter mHomepageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_homepage, container, false);
        initView(view);

        return view;
    }

    @Override
    public void fragmentShow() {
        super.fragmentShow();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                pullToRefreshMRecyclerView.setRefreshing(true);  //需要延迟调用，直接调用没有用
            }
        }, 600);
    }

    private void initView(View view) {
        pullToRefreshMRecyclerView = (PullToRefreshMRecyclerView) view.findViewById(R.id.homepage_pull_recyclerview);
        pullToRefreshMRecyclerView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshMRecyclerView.setOnRefreshListener(this);
        mHomepageAdapter = new HomepageAdapter(getActivity(), null);
        pullToRefreshMRecyclerView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshMRecyclerView.setOnRefreshListener(this);
        ObservableRecyclerView refreshableView = pullToRefreshMRecyclerView.getRefreshableView();
        refreshableView.setAdapter(mHomepageAdapter);
        refreshableView.addItemDecoration(new RecycleViewDivider(getContext(), (int) DpOrSp2PxUtil.dp2px(getActivity(), 8),
                getResources().getColor(R.color.white)));

        //关闭RecyclerView动画显示
        ((SimpleItemAnimator) refreshableView.getItemAnimator()).setSupportsChangeAnimations(false);


        TextView textPullData = (TextView) view.findViewById(R.id.homepage_pull_test);
        textPullData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onRefresh(PullToRefreshBase<ObservableRecyclerView> refreshView) {
        Log.e("xxxxxx", "onRefresh");
        getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            BroadcastManager.getInstance(getActivity()).destroy(SealAppContext.UPDATE_FRIEND);
            BroadcastManager.getInstance(getActivity()).destroy(SealAppContext.UPDATE_RED_DOT);
            BroadcastManager.getInstance(getActivity()).destroy(SealConst.CHANGEINFO);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void getData() {
        SealUserInfoManager.getInstance().getRecommendUsers(new SealUserInfoManager.ResultCallback<HomepageResponse>() {
            @Override
            public void onSuccess(HomepageResponse homepageResponse) {
                Log.e("xxxxxx", "getData onSuccess");
                if (homepageResponse != null) {
                    mHomepageAdapter.setData(homepageResponse.getResult());
                }
                if (pullToRefreshMRecyclerView != null) {
                    pullToRefreshMRecyclerView.onRefreshComplete();
                }
            }

            @Override
            public void onError(String errString) {
                if (getActivity() == null || !isAdded()) {
                    return;
                }
                if (!TextUtils.isEmpty(errString)) {
                    NToast.shortToast(getActivity(), errString);
                } else {
                    NToast.shortToast(getActivity(), "获取用户信息失败");
                }
                if (pullToRefreshMRecyclerView != null) {
                    pullToRefreshMRecyclerView.onRefreshComplete();
                }
            }
        });
    }

}
