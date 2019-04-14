package cn.yunchuang.im.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.yunchuang.im.MeService;
import cn.yunchuang.im.R;
import cn.yunchuang.im.event.RefreshLocationEvent;
import cn.yunchuang.im.location.LocateReqManager;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.zmico.utils.BaseBaseUtils;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import cn.yunchuang.im.zmico.utils.Utils;

/**
 * 首页
 */
public class HomepageFragment_new extends BaseFragment implements View.OnClickListener {

    private TextView dingweiTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_homepage_new, container, false);
        initView(view);
        return view;
    }

    @Override
    public void fragmentShow() {
        super.fragmentShow();

    }

    private void initView(View view) {
        //启动页会进行第一次定位，如果定位成功，会保存定位成功的时间，这里就会直接用sp保存的数据
        //点击附近的人会再次请求定位，如果发现2分钟内已经定位成功过，则直接使用sp保存的定位结果，不再进行定位
        if (getActivity() != null && isAdded()) {
            LocateReqManager.sendRequestLocation(getActivity(), HomepageFragment_new.class.getSimpleName(), false);
        }

        //设置标题栏高度，还有状态栏透明
        if (getActivity() != null) {
            int topLayoutHeight = DeviceUtils.getStatusBarHeightPixels(getActivity()) + DeviceUtils.dpToPx(81);
            FrameLayout titleLayout = (FrameLayout) view.findViewById(R.id.homepage_new_title_root_layout);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    topLayoutHeight);
            titleLayout.setLayoutParams(layoutParams);
            BaseBaseUtils.setTranslucentStatus(getActivity());//状态栏透明
        }

        dingweiTv = view.findViewById(R.id.homepage_new_title_location_tv);

        //定位位置，采用sp保存的
        dingweiTv.setText(MeService.getMyLocation().getCity());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
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
    }

    private void getData() {


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshLocationEvent(RefreshLocationEvent event) {
        //只处理本类发起的定位请求
        if (Utils.isNotNull(event) && event.getLocateSender().equals(HomepageFragment_new.class.getSimpleName())) {
            Log.d("xxxxxx", "onRefreshLocationEvent homepage_new");
            if (Utils.isNotNull(event) && event.getLocationVO() != null) {
                if (event.isLocationSuccess()) {
                    if (dingweiTv != null) {
                        dingweiTv.setText(event.getLocationVO().getCity());
                    }
                } else {
                    if (getActivity() != null && isAdded()) {
                        NToast.shortToast(getActivity(), "定位失败");
                    }
                }
            } else {
                if (getActivity() != null && isAdded()) {
                    NToast.shortToast(getActivity(), "定位失败");
                }
            }
        }
    }

}
