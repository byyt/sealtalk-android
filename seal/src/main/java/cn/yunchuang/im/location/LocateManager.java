package cn.yunchuang.im.location;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.yunchuang.im.App;
import cn.yunchuang.im.MeService;
import cn.yunchuang.im.sp.ConfigPref;
import cn.yunchuang.im.zmico.utils.Utils;


/**
 * Created by liumingkong on 2017/6/12.
 */

public enum LocateManager {

    INSTANCE;

    public MeetsLocateFinder meetsLocateFinder;
    public LocationVO cacheLocationVO;
    public HashSet<LocationRequest> locationRequests = new HashSet<>();

    private ExecutorService locReqPools = Executors.newSingleThreadExecutor();
    private ExecutorService locApiPools = Executors.newSingleThreadExecutor();

    public void initLocate() {
        meetsLocateFinder = new MeetsLocateFinder();
    }

    public void stopLocate() {
        try {
            if (!Utils.isNull(meetsLocateFinder)) {
                meetsLocateFinder.closeLocate();
            }
            locationRequests.clear();
        } catch (Throwable throwable) {
//            Ln.e(throwable);
        }
    }

    // 更新local地理位置并上传地理位置
    public void updateLocalLocationAndPost(LocationVO locationVO) {
        MeService.setMyLocation(locationVO);
        if (ConfigPref.isLogined()) {
            //发送一个上传请求，将地址请求到服务器
        }
    }


    // ********************************************
    //单线程的线程池处理定位请求
    public void dispatchApiLocReq(boolean isForceUpdate) {
        locReqPools.execute(new LocApiReqDispatch(isForceUpdate));
    }

    //单线程的线程池处理定位结果
    public void dispatchApiLocResp(LocateApiResp locateResponse) {
        locApiPools.execute(new LocApiRespDispatch(locateResponse));
    }
}
