package cn.yunchuang.im.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.yunchuang.im.HttpManager;
import cn.yunchuang.im.MeService;
import cn.yunchuang.im.R;
import cn.yunchuang.im.event.RefreshMineInfoEvent;
import cn.yunchuang.im.server.BaseAction;
import cn.yunchuang.im.server.response.GetUserDetailModelOne;
import cn.yunchuang.im.server.response.GetUserDetailOneResponse;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.ui.activity.MyBaseInfoActivity_new;
import cn.yunchuang.im.ui.activity.UserDetailActivity_New;
import cn.yunchuang.im.utils.GlideUtils;
import cn.yunchuang.im.zmico.utils.BaseBaseUtils;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import cn.yunchuang.im.zmico.utils.Utils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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
    private LinearLayout avatarNameLayout;
    private RoundedImageView avatarIv;
    private TextView nameTv;

    //标题栏
    private FrameLayout titleLayoutTranslate;
    private FrameLayout titleLayoutWhite;
    private ImageView bjImgTranslate;
    private ImageView bjImgBlack;

    //各个栏目
    private LinearLayout qianbaoLayout;
    private LinearLayout xuqiuLayout;
    private LinearLayout fuwuLayout;
    private LinearLayout weixinLayout;
    private LinearLayout zjzLayout;
    private LinearLayout zuopinLayout;
    private LinearLayout zjlfLayout;
    private LinearLayout guanzhuLayout;
    private LinearLayout fensiLayout;
    private LinearLayout kefuLayout;
    private LinearLayout shezhiLayout;

    private int statusBarHeight = 0;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        topLayout.setOnClickListener(this);
        avatarNameLayout = view.findViewById(R.id.fragment_mine_avatar_name_layout);
        avatarNameLayout.setOnClickListener(this);
        avatarIv = view.findViewById(R.id.fragment_mine_avatar_iv);
        nameTv = view.findViewById(R.id.fragment_mine_name_tv);

        titleLayoutTranslate = (FrameLayout) view.findViewById(R.id.fragment_mine_new_title_layout_translate);
        titleLayoutWhite = (FrameLayout) view.findViewById(R.id.fragment_mine_new_title_layout_white);
        bjImgTranslate = (ImageView) view.findViewById(R.id.fragment_mine_new_title_bianji);
        bjImgTranslate.setOnClickListener(this);
        bjImgBlack = (ImageView) view.findViewById(R.id.fragment_mine_new_title_bianji_black);
        bjImgBlack.setOnClickListener(this);

        qianbaoLayout = (LinearLayout) view.findViewById(R.id.fragment_mine_qianbao_layout);
        xuqiuLayout = (LinearLayout) view.findViewById(R.id.fragment_mine_xuqiu_layout);
        fuwuLayout = (LinearLayout) view.findViewById(R.id.fragment_mine_fuwu_layout);
        weixinLayout = (LinearLayout) view.findViewById(R.id.fragment_mine_weixin_layout);
        zjzLayout = (LinearLayout) view.findViewById(R.id.fragment_mine_zjz_layout);
        zuopinLayout = (LinearLayout) view.findViewById(R.id.fragment_mine_zuopin_layout);
        zjlfLayout = (LinearLayout) view.findViewById(R.id.fragment_mine_zjlf_layout);
        guanzhuLayout = (LinearLayout) view.findViewById(R.id.fragment_mine_guanzhu_layout);
        fensiLayout = (LinearLayout) view.findViewById(R.id.fragment_mine_fensi_layout);
        kefuLayout = (LinearLayout) view.findViewById(R.id.fragment_mine_kefu_layout);
        shezhiLayout = (LinearLayout) view.findViewById(R.id.fragment_mine_shezhi_layout);

        qianbaoLayout.setOnClickListener(this);
        xuqiuLayout.setOnClickListener(this);
        fuwuLayout.setOnClickListener(this);
        weixinLayout.setOnClickListener(this);
        zjzLayout.setOnClickListener(this);
        zuopinLayout.setOnClickListener(this);
        zjlfLayout.setOnClickListener(this);
        guanzhuLayout.setOnClickListener(this);
        fensiLayout.setOnClickListener(this);
        kefuLayout.setOnClickListener(this);
        shezhiLayout.setOnClickListener(this);

        statusBarHeight = DeviceUtils.getStatusBarHeightPixels(getActivity());
        initTitleLayout();
        nestedScrollView.scrollTo(0, 0);
        //请求个人信息
        getMeUserInfo();
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
//        ImageStatusBarCompat.setStatusBarColor(this, Color.WHITE);
        BaseBaseUtils.setStatusBarColor(getActivity(), Color.WHITE);
        titleLayoutWhite.setVisibility(View.VISIBLE);
        titleLayoutTranslate.setVisibility(View.GONE);
    }

    private void buildTitleBgTransparent() {
        if (getActivity() == null) {
            return;
        }
//        ImageStatusBarCompat.setStatusBarColor(this, Color.TRANSPARENT);
        BaseBaseUtils.setTranslucentStatus(getActivity());
        titleLayoutWhite.setVisibility(View.GONE);
        titleLayoutTranslate.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_mine_new_title_bianji:
            case R.id.fragment_mine_new_title_bianji_black:
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), MyBaseInfoActivity_new.class);
                    startActivity(intent);
                }
                break;
            case R.id.fragment_mine_avatar_name_layout:
                if (getActivity() != null) {
                    Intent intentUD = new Intent(getActivity(), UserDetailActivity_New.class);
                    intentUD.putExtra("userId", MeService.getUid());
                    getActivity().startActivity(intentUD);
                }
                break;
        }
    }

    private void getMeUserInfo() {
        Disposable disposable = HttpManager.getInstance().getUserDetailOne(MeService.getUid(), new HttpManager.ResultCallback<GetUserDetailOneResponse>() {
            @Override
            public void onSuccess(GetUserDetailOneResponse response) {
                if (response != null) {
                    GetUserDetailModelOne model = response.getResult();
                    if (model != null && getActivity() != null) {
                        GlideUtils.load(getActivity(), BaseAction.DOMAIN_PIC + "/" + model.getPortraitUri(), avatarIv);
                        nameTv.setText(model.getNickname());
                    }
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
            }
        });
        compositeDisposable.add(disposable);
    }

    //额，发现必须要放在onCreate和onDestroy里，不然收不到，
    //如果按照官网的在onStart和onStop里加，进入其他界面后就onStop注销了，所以收不到
    //而且EventBus.getDefault().register(this);了的类必须有@Subscribe标记的接收函数，否则会报错
    //只是发送事件是不需要EventBus.getDefault().register(this);的，直接EventBus.getDefault().post(event)即可
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshMineEvent(RefreshMineInfoEvent refreshMineInfoEvent) {
        if (Utils.isNotNull(refreshMineInfoEvent)) {
            getMeUserInfo();
        }
    }
}
