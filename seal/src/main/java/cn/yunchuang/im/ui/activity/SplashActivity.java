package cn.yunchuang.im.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.yunchuang.im.R;
import cn.yunchuang.im.SealAppContext;
import cn.yunchuang.im.event.SplashCheckPermissionsEvent;
import cn.yunchuang.im.location.LocateReqManager;
import cn.yunchuang.im.zmico.utils.Utils;
import io.rong.imkit.RongIM;

/**
 * Created by AMing on 16/8/5.
 * Company RongCloud
 */
public class SplashActivity extends Activity {

    private Context context;
    private android.os.Handler handler = new android.os.Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        context = this;
        //进行第一次定位，如果定位成功，会保存定位成功的时间，
        //进入主页后会再次请求定位，如果发现2分钟内已经定位成功过，则直接使用sp保存的定位结果，不再进行定位
        //定位请求是按排队处理的，所以主页的定位请求会等启动页的定位请求处理再进行处理
        //里面会有检查权限，等权限处理完，再做跳转操作
        LocateReqManager.sendRequestLocation(this, SplashActivity.class.getSimpleName(), true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //定位请求里面会有检查权限，等权限处理完，再做跳转操作
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSplashCheckPermissionsEvent(SplashCheckPermissionsEvent event) {
        if (Utils.isNotNull(event)) {
            //不管是否同意定位权限，都做跳转操作，如果同意，如果不同意，进入主页后会再一次请求权限
            //判断是否登录，这个方法已经放到ConfigPref.isLogined，这里就保留别人原来写的吧
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            String cacheToken = sp.getString("loginToken", "");
            if (!TextUtils.isEmpty(cacheToken)) {
                RongIM.connect(cacheToken, SealAppContext.getInstance().getConnectCallback());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goToMain();
                    }
                }, 800);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goToLogin();
                    }
                }, 800);
            }
        }
    }

    private void goToMain() {
        startActivity(new Intent(context, MainActivity.class));
        finish();
    }

    private void goToLogin() {
        startActivity(new Intent(context, LoginActivity_Code_PassWord.class));
//        startActivity(new Intent(context, LoginActivity_Register_Test.class));
        finish();
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

}
