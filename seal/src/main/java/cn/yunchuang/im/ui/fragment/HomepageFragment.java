package cn.yunchuang.im.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yunchuang.im.R;
import cn.yunchuang.im.SealUserInfoManager;
import cn.yunchuang.im.server.response.HomepageModel;
import cn.yunchuang.im.server.response.HomepageResponse;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.ui.adapter.HomepageAdapter_New;
import cn.yunchuang.im.zmico.ResourceUtils;
import cn.yunchuang.im.zmico.statusbar.StatusBarCompat;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * tab 2 通讯录的 Fragment
 * Created by Bob on 2015/1/25.
 */
public class HomepageFragment extends BaseFragment implements View.OnClickListener, OnRefreshLoadMoreListener {

    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private static boolean isFirstEnter = true;

    private HomepageAdapter_New mHomepageAdapter;

    private int startIndex = 0;
    private static final int PAGE_SIZE = 3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_homepage_new, container, false);
        initView(view);

        StatusBarCompat.setStatusBarColor(getActivity(), ResourceUtils.getColor(R.color.color_FC6880));

        return view;
    }

    @Override
    public void fragmentShow() {
        super.fragmentShow();
        mRefreshLayout.autoRefresh();
    }

    private void initView(View view) {

        mRefreshLayout = (RefreshLayout) view.findViewById(R.id.activity_homepage_refreshLayout);
        mRefreshLayout.setEnableHeaderTranslationContent(true);
        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setEnableFooterFollowWhenNoMoreData(true);//开启自动加载功能（非必须）
        mRefreshLayout.setPrimaryColors(getResources().getColor(R.color.color_FC6880));//设置主题颜色


        mRecyclerView = (RecyclerView) view.findViewById(R.id.activity_homepage_recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mHomepageAdapter = new HomepageAdapter_New(getActivity());

        mRecyclerView.setAdapter(mHomepageAdapter);

        mRefreshLayout.setOnRefreshLoadMoreListener(this);

        if (isFirstEnter) {
            isFirstEnter = false;
            mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void getData() {
        SealUserInfoManager.getInstance().getRecommendUsers(startIndex, PAGE_SIZE, new SealUserInfoManager.ResultCallback<HomepageResponse>() {
            @Override
            public void onSuccess(HomepageResponse homepageResponse) {
                Log.e("xxxxxx", "getData onSuccess");
                if (homepageResponse != null) {
                    List<HomepageModel> list = homepageResponse.getResult().getData();
                    if (startIndex == 0) {
                        updateLiveList(list, true);
                    } else {
                        updateLiveList(list, false);
                    }
                    startIndex = homepageResponse.getResult().getNextIndex();
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
//                if (pullToRefreshMRecyclerView != null) {
//                    pullToRefreshMRecyclerView.onRefreshComplete();
//                }

                mRefreshLayout.finishRefresh();
                mRefreshLayout.resetNoMoreData();//setNoMoreData(false);

            }
        });
    }

    private void updateLiveList(List<HomepageModel> list, boolean isRefresh) {
        if (list == null) {
            if (isRefresh) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.resetNoMoreData();//setNoMoreData(false);//恢复上拉状态
            } else {
                mRefreshLayout.finishLoadMoreWithNoMoreData();//设置之后，将不会再触发加载事件
            }
            return;
        }

        if (isRefresh) {
            mHomepageAdapter.replaceData(list);
            mRefreshLayout.finishRefresh();
            mRefreshLayout.resetNoMoreData();//setNoMoreData(false);//恢复上拉状态
            return;
        }

        // 通过本地已有直播间列表构建 map{key: roomId, value: roomInfo}
        List<HomepageModel> localList = new ArrayList<>(mHomepageAdapter.getData());
        Map<String, HomepageModel> liveMap = new HashMap<>(); // upgrade to SparseLongArray when using API >= 18
        for (HomepageModel model : localList) {
            if (model.getId() != null) {
                liveMap.put(model.getId(), model);
            }
        }

        List<HomepageModel> newList = new ArrayList<>();
        boolean hasDuplicateRoom = false;
        // 请求返回的直播间列表, 通过上面本地直播间 map 进行合并
        for (HomepageModel model : list) {
            if (model.getId() == null) {
                continue;
            }

            if (!liveMap.containsKey(model.getId())) {
                newList.add(model);
            } else {
                hasDuplicateRoom = true;
                //替换旧的，先不做了

            }
        }

        if (!newList.isEmpty()) {
            mHomepageAdapter.addData(newList);
            mRefreshLayout.finishLoadMore();
        } else {
            mRefreshLayout.finishLoadMoreWithNoMoreData();//设置之后，将不会再触发加载事件
        }


    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        startIndex = 0;
        getData();

//        mRefreshLayout.getLayout().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mRefreshLayout.finishRefresh();
//                mRefreshLayout.resetNoMoreData();//setNoMoreData(false);
//            }
//        }, 1000);

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        getData();
//        refreshLayout.finishLoadMoreWithNoMoreData();
    }

}
