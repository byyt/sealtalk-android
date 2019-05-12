package cn.yunchuang.im.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yunchuang.im.HttpManager;
import cn.yunchuang.im.R;
import cn.yunchuang.im.event.SaveShaixuanEvent;
import cn.yunchuang.im.model.ShaixuanOrderModel;
import cn.yunchuang.im.server.response.GetWdyhOrderLbModel;
import cn.yunchuang.im.server.response.GetWdyhOrderLbResponse;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.ui.activity.MainActivity;
import cn.yunchuang.im.ui.adapter.WdyhLbAdapter;
import cn.yunchuang.im.ui.widget.MyFooter;
import cn.yunchuang.im.zmico.utils.Utils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * 我的约会--列表基类fragment
 */
public class WdyhLbBaseFragment extends LazyFragment implements View.OnClickListener, OnRefreshLoadMoreListener {

    public static final String TAG = "WdyhLbBaseFragment";

    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private static boolean isFirstEnter = true;

    private WdyhLbAdapter wdyhLbAdapter;

    private int startIndex = 0;
    private static final int PAGE_SIZE = 6;

    private ShaixuanOrderModel shaixuanModel;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_wdyh_lb_base;
    }

    @Override
    protected void onLazyLoad() {
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initView(View view, LayoutInflater inflater, Bundle savedInstanceState) {

        if (getActivity() != null && getActivity() instanceof MainActivity) {
//            shaixuanModel = ((MainActivity) getActivity()).getShaixuanModel();
        }

        refreshLayout = (RefreshLayout) view.findViewById(R.id.activity_wdyh_lb_base_refreshLayout);
        refreshLayout.setEnableHeaderTranslationContent(true);
        refreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        refreshLayout.setEnableFooterFollowWhenNoMoreData(true);//开启自动加载功能（非必须）
        refreshLayout.setPrimaryColors(getResources().getColor(R.color.color_FC6880));//设置主题颜色
//        refreshLayout.(getResources().getColor(R.color.color_FC6880));//设置主题颜色
        refreshLayout.setRefreshFooter(new MyFooter(getContext()));


        recyclerView = (RecyclerView) view.findViewById(R.id.activity_wdyh_lb_base_recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        wdyhLbAdapter = new WdyhLbAdapter(getActivity());

        recyclerView.setAdapter(wdyhLbAdapter);

        refreshLayout.setOnRefreshLoadMoreListener(this);

        if (isFirstEnter) {
            isFirstEnter = false;
            refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private void getData() {
        Disposable disposable = HttpManager.getInstance().getWdyhLb(startIndex, PAGE_SIZE, shaixuanModel,
                new HttpManager.ResultCallback<GetWdyhOrderLbResponse>() {
                    @Override
                    public void onSuccess(GetWdyhOrderLbResponse getMsztOrderLbResponse) {
                        if (getMsztOrderLbResponse != null) {
                            List<GetWdyhOrderLbModel> list = getMsztOrderLbResponse.getResult().getData();
                            if (startIndex == 0) {
                                updateLiveList(list, true);
                            } else {
                                updateLiveList(list, false);
                            }
                            startIndex = getMsztOrderLbResponse.getResult().getNextIndex();
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

                        refreshLayout.finishRefresh();
                        refreshLayout.resetNoMoreData();//setNoMoreData(false);
                        refreshLayout.finishLoadMoreWithNoMoreData();//设置之后，将不会再触发加载事件
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void updateLiveList(List<GetWdyhOrderLbModel> list, boolean isRefresh) {
        if (list == null) {
            if (isRefresh) {
                refreshLayout.finishRefresh();
                refreshLayout.resetNoMoreData();//setNoMoreData(false);//恢复上拉状态
            } else {
                refreshLayout.finishLoadMoreWithNoMoreData();//设置之后，将不会再触发加载事件
            }
            return;
        }

        if (isRefresh) {
            wdyhLbAdapter.replaceData(list);
            refreshLayout.finishRefresh();
            refreshLayout.resetNoMoreData();//setNoMoreData(false);//恢复上拉状态
            return;
        }

        // 通过本地已有直播间列表构建 map{key: roomId, value: roomInfo}
        List<GetWdyhOrderLbModel> localList = new ArrayList<>(wdyhLbAdapter.getData());
        Map<String, GetWdyhOrderLbModel> liveMap = new HashMap<>(); // upgrade to SparseLongArray when using API >= 18
        for (GetWdyhOrderLbModel model : localList) {
            if (model.getWdyhOrderId() != null) {
                liveMap.put(model.getWdyhOrderId(), model);
            }
        }

        List<GetWdyhOrderLbModel> newList = new ArrayList<>();
        boolean hasDuplicateRoom = false;
        // 请求返回的直播间列表, 通过上面本地直播间 map 进行合并
        for (GetWdyhOrderLbModel model : list) {
            if (model.getWdyhOrderId() == null) {
                continue;
            }

            if (!liveMap.containsKey(model.getWdyhOrderId())) {
                newList.add(model);
            } else {
                hasDuplicateRoom = true;
                //替换旧的，先不做了

            }
        }

        if (!newList.isEmpty()) {
            wdyhLbAdapter.addData(newList);
            refreshLayout.finishLoadMore();
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData();//设置之后，将不会再触发加载事件
        }


    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        startIndex = 0;
        getData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        getData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        compositeDisposable.clear();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveShaixuanEvent(SaveShaixuanEvent event) {
        //只处理本类发起的筛选请求
        if (Utils.isNotNull(event)) {
//            shaixuanModel = event.getShaixuanModel();
            if (event.getFromFragmentName().equals(TAG)) {
                if (wdyhLbAdapter != null && Utils.isNotEmptyCollection(wdyhLbAdapter.getData())) {
                    recyclerView.scrollToPosition(0);
                }
                if (refreshLayout != null) {
                    refreshLayout.autoRefresh();
                }
            }
        }
    }

}
