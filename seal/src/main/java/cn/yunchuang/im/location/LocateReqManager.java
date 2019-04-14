package cn.yunchuang.im.location;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.LinkedList;
import java.util.List;

import cn.yunchuang.im.App;
import cn.yunchuang.im.SealConst;
import cn.yunchuang.im.event.SplashCheckPermissionsEvent;

/**
 * Created by liumingkong on 16/9/26.
 * 管理locate发出的请求，
 * 这个是外界调用定位的入口类
 */

public class LocateReqManager {

    //定位请求的队列，请求一个个处理，处理完一个再进行下一个处理，
    //比如启动页请求定位，进入主页后再请求定位，两个定位可能会同时处理，会导致请求接口两次
    //如果加上排队，必须先处理完启动页的请求，到主页再处理时，发现启动页已经请求成功，直接取本地的sp数据即可就不会再发一次请求
    //容易内存泄漏，注意处理完及时删掉请求
    public static LinkedList<LocateRequest> locateRequestQueue = new LinkedList<>();
    //是否有请求在处理中
    public static boolean isWorking = false;
    //当前正在处理的请求，后面分发请求结果的时候需要用到，eventBus会告诉当前成功处理的是那个界面发起的定位请求
    public static LocateRequest currentLocateRequest;

    /**
     * 发起定位请求，外边的类都是调用这个入口函数
     *
     * @param activity
     * @param sender   //哪个界面发起的定位请求，传该界面的类名即可，作用是定位成功后，各界面在自己发起定位成功后才处理
     * @param isForce  //是否强制更新
     */
    public static void sendRequestLocation(Activity activity, String sender, boolean isForce) {
        if (activity == null) return;
        //记得检查一下权限
        if (AndPermission.hasPermissions(activity, Permission.ACCESS_COARSE_LOCATION)) {
            //把请求放入队列中
            LocateRequest locateRequest = new LocateRequest(sender, isForce);
            locateRequestQueue.offer(locateRequest);
            Log.d("xxxxxx", "存入队列：" + sender + "  " + isForce);
            if (!isWorking) {
                startLocate();
            }
            //已有定位权限，启动页可以直接跳转
            SplashCheckPermissionsEvent.postEvent(true);
        } else {
            requestPermission(activity, sender, isForce, Permission.ACCESS_COARSE_LOCATION);
        }
    }

    public static void startLocate() {
        currentLocateRequest = locateRequestQueue.poll(); //从队列取出，并删除，减去引用
        if (currentLocateRequest == null) {//队列中已无请求需要处理
            return;
        }
        isWorking = true;
        Intent intent = new Intent(App.getAppContext(), LocationService.class);
        intent.putExtra(SealConst.LASTUPDATE, currentLocateRequest.getLoacateSender());
        intent.putExtra(SealConst.SENDER, currentLocateRequest.isForce());
        Log.d("xxxxxx", "取出队列，开始请求：" + currentLocateRequest.getLoacateSender() + "  "
                + currentLocateRequest.isForce());
        App.getAppContext().startService(intent);
    }

    /**
     * 检查权限
     */
    private static void requestPermission(final Activity activity, final String sender, final boolean isForce,
                                          String permissions) {
        AndPermission.with(App.getAppContext())
                .runtime()
                .permission(permissions)
//                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Log.d("xxxxxx", "权限申请成功");
                        //把请求放入队列中
                        LocateRequest locateRequest = new LocateRequest(sender, isForce);
                        locateRequestQueue.offer(locateRequest);
                        Log.d("xxxxxx", "存入队列：" + sender + "  " + isForce);
                        if (!isWorking) {
                            startLocate();
                        }
                        //已有定位权限，启动页可以直接跳转
                        SplashCheckPermissionsEvent.postEvent(true);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        Log.d("xxxxxx", "权限申请失败");
                        SplashCheckPermissionsEvent.postEvent(false);
                        if (AndPermission.hasAlwaysDeniedPermission(activity, permissions)) {

                        }
                    }
                })
                .start();
    }

}
