package cn.yunchuang.im.ui.activity;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.yunchuang.im.MeService;
import cn.yunchuang.im.R;
import cn.yunchuang.im.SealAppContext;
import cn.yunchuang.im.event.RefreshBalanceCoinsEvent;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.server.response.BalanceCoinsModel;
import cn.yunchuang.im.server.response.BalanceCoinsResponse;
import cn.yunchuang.im.server.response.GetUserDetailModelOne;
import cn.yunchuang.im.server.response.GetUserDetailOneResponse;
import cn.yunchuang.im.server.utils.CommonUtils;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.server.widget.LoadDialog;
import cn.yunchuang.im.utils.DialogUtils;
import cn.yunchuang.im.widget.dialog.ConfirmDialog;
import cn.yunchuang.im.zmico.utils.BaseBaseUtils;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import cn.yunchuang.im.zmico.utils.Utils;
import me.leefeng.promptlibrary.PromptDialog;

import static cn.yunchuang.im.SealConst.BALANCE_COINS_TYPE_ADD_BALANCE_RECHARGE;


public class WdqbActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout titleLayout;
    private ImageView backImg;
    private TextView nextTv;

    private LinearLayout balanceLayout;
    private TextView balanceTv;
    private TextView coinsTv;

    private static final int GET_USER_DETAIL_ONE = 1601;
    private static final int GET_BALANCE_COINS = 1602;
    private static final int ADD_BALANCE_RECHARGE = 1603;

    private PromptDialog loadingDialog;
    private String userId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseBaseUtils.setTranslucentStatus(this);//状态栏透明
        setContentView(R.layout.activity_wdqb);
        setHeadVisibility(View.GONE);
        initView();
        SealAppContext.getInstance().pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        SealAppContext.getInstance().popActivity(this);
        super.onDestroy();
    }

    private void initView() {
        userId = MeService.getUid();

        titleLayout = (FrameLayout) findViewById(R.id.activity_wdqb_title_layout);
        backImg = (ImageView) findViewById(R.id.activity_wdqb_back);
        backImg.setOnClickListener(this);
        nextTv = (TextView) findViewById(R.id.activity_wdqb_next);
        nextTv.setOnClickListener(this);

        balanceLayout = (LinearLayout) findViewById(R.id.activity_wdqb_balance_layout);
        balanceLayout.setOnClickListener(this);
        balanceTv = (TextView) findViewById(R.id.activity_wdqb_balance_tv);
        coinsTv = (TextView) findViewById(R.id.activity_wdqb_coins_tv);
        coinsTv.setOnClickListener(this);

        loadingDialog = DialogUtils.getLoadingDialog(this);

        initTitleLayout();
        getData();
    }

    private void initTitleLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DeviceUtils.dpToPx(48) + DeviceUtils.getStatusBarHeightPixels(this));
        titleLayout.setLayoutParams(layoutParams);
    }


    private void getData() {
        DialogUtils.showLoading(loadingDialog);
//        request(GET_USER_DETAIL_ONE);
        request(GET_BALANCE_COINS);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (Utils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.activity_wdqb_back:
                finish();
                break;
            case R.id.activity_wdqb_next:
                break;
            case R.id.activity_wdqb_balance_layout:
                DialogUtils.showConfirmDialog(WdqbActivity.this, "确定充值100元？", "",
                        new ConfirmDialog.ConfirmListener() {
                            @Override
                            public void confirm() {
                                request(ADD_BALANCE_RECHARGE);
                            }
                        });
                break;
        }
    }


    private void updateDataOne(GetUserDetailOneResponse getUserDetailOneResponse) {
        if (getUserDetailOneResponse == null) {
            return;
        }
        GetUserDetailModelOne modelOne = getUserDetailOneResponse.getResult();
        if (modelOne == null) {
            return;
        }
    }

    private void updateRechargeResult(BalanceCoinsResponse balanceCoinsResponse) {
        if (balanceCoinsResponse == null) {
            return;
        }
        BalanceCoinsModel model = balanceCoinsResponse.getResult();
        if (model == null) {
            return;
        }

        updateBalanceCoins(model.getBalance(), model.getCoins());
    }

    private void updateBalanceCoins(double balance, long coins) {
        balanceTv.setText(String.valueOf(balance) + "元");
        coinsTv.setText(String.valueOf(coins));
        MeService.setMyBalance(balance);
        MeService.setMyCoin(coins);
        RefreshBalanceCoinsEvent.postEvent(balance, coins);
    }


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case GET_USER_DETAIL_ONE:
                return action.getUserDetailOne(userId);
            case GET_BALANCE_COINS:
                return action.postMyBalanceCoinsGet();
            case ADD_BALANCE_RECHARGE:
                return action.postBalanceCoinsOperationSingle(BALANCE_COINS_TYPE_ADD_BALANCE_RECHARGE, 100, 0);
        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        DialogUtils.dimiss(loadingDialog);
        if (result != null) {
            switch (requestCode) {
                case GET_USER_DETAIL_ONE:
                    GetUserDetailOneResponse getUserDetailOneResponse = (GetUserDetailOneResponse) result;
                    if (getUserDetailOneResponse.getCode() == 200) {
                        updateDataOne(getUserDetailOneResponse);
                    } else {
                        NToast.shortToast(mContext, "获取余额和金币信息失败");
                    }
                    break;
                case GET_BALANCE_COINS:
                    BalanceCoinsResponse balanceCoinsResponse1 = (BalanceCoinsResponse) result;
                    if (balanceCoinsResponse1.getCode() == 200) {
                        updateRechargeResult(balanceCoinsResponse1);
                    } else {
                        NToast.shortToast(mContext, "获取余额和金币信息失败");
                    }
                    break;
                case ADD_BALANCE_RECHARGE:
                    BalanceCoinsResponse balanceCoinsResponse2 = (BalanceCoinsResponse) result;
                    if (balanceCoinsResponse2.getCode() == 200) {
                        updateRechargeResult(balanceCoinsResponse2);
                    } else {//加余额操作，没有余额不足的说法，不用判断
                        NToast.shortToast(mContext, "充值余额失败");
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        DialogUtils.dimiss(loadingDialog);
        if (!CommonUtils.isNetworkConnected(mContext)) {
            LoadDialog.dismiss(mContext);
            NToast.shortToast(mContext, getString(R.string.network_not_available));
            return;
        }
        switch (requestCode) {
            case GET_USER_DETAIL_ONE:
                NToast.shortToast(mContext, "获取余额和金币信息失败");
                break;
            case GET_BALANCE_COINS:
                NToast.shortToast(mContext, "获取余额和金币信息失败");
                break;
            case ADD_BALANCE_RECHARGE:
                NToast.shortToast(mContext, "充值余额失败");
                break;
        }
    }
}
