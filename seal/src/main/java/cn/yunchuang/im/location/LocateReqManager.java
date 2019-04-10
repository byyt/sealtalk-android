package cn.yunchuang.im.location;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

import cn.yunchuang.im.App;
import cn.yunchuang.im.SealConst;

/**
 * Created by liumingkong on 16/9/26.
 * 管理locate发出的请求，
 * 这个是外界调用定位的入口类
 */

public class LocateReqManager {

    public static final String DEFAULT_SENDER = "DEFAULT_SENDER";// 默认的requester

    public static void sendRequestLocation(Activity activity) {
        if (activity == null) return;

        if (AndPermission.hasPermissions(activity, Permission.ACCESS_COARSE_LOCATION)) {
            sendRequestLocation(DEFAULT_SENDER, true);
        } else {
            requestPermission(activity, Permission.ACCESS_COARSE_LOCATION);
        }
    }

    /**
     * 检查权限
     */
    private static void requestPermission(final Activity activity, String... permissions) {
        AndPermission.with(App.getAppContext())
                .runtime()
                .permission(permissions)
//                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Log.d("xxxxxx", "权限申请失败");
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        Log.d("xxxxxx", "权限申请失败");
                        if (AndPermission.hasAlwaysDeniedPermission(activity, permissions)) {

                        }
                    }
                })
                .start();
    }

    // 发起定位请求，是否强制更新
    public static void sendRequestLocation(Object requester, boolean isForce) {
        Intent intent = new Intent(App.getAppContext(), LocationService.class);
        intent.putExtra(SealConst.LASTUPDATE, isForce);
        App.getAppContext().startService(intent);
    }
}
