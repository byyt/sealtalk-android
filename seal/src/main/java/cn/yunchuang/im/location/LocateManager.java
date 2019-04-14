package cn.yunchuang.im.location;


import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.yunchuang.im.HttpManager;
import cn.yunchuang.im.MeService;
import cn.yunchuang.im.event.RefreshLocationEvent;
import cn.yunchuang.im.server.response.BaseResponse;
import cn.yunchuang.im.server.utils.LocationUtils;
import cn.yunchuang.im.sp.BasePref;
import cn.yunchuang.im.sp.ConfigPref;
import cn.yunchuang.im.zmico.utils.Utils;

import static cn.yunchuang.im.sp.BasePref.KEY_LOCATE_SUCCESS;


/**
 * Created by liumingkong on 2017/6/12.
 */

public enum LocateManager {

    INSTANCE;

    public MeetsLocateFinder meetsLocateFinder;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 更新local地理位置并上传地理位置，最终定位成功之后会调用这个函数
    //如果新位置与旧位置距离超过500米，则更新本地位置，同时将位置上传到服务器
    public void updateLocalLocationAndPost(final LocationVO locationVO) {
        if (ConfigPref.isLogined()) {

//            if (!isOver500m(locationVO)) {//如果未超过500米，则不更新位置
//                RefreshLocationEvent.postEvent(locationVO, true);
//                //定位成功，保存定位成功的时间戳，下一次定位请求，要么强制更新要么需要大于一定时间间隔2分钟，才能进行定位
//                //即使定位成功，也需要与旧距离超过500米才会进行位置保存和上传
//                BasePref.saveLong(KEY_LOCATE_SUCCESS, System.currentTimeMillis());
//                return;
//            }

            //发送一个上传请求，将地址请求到服务器
            HttpManager.getInstance().postUserLocation(locationVO.getLongitude(), locationVO.getLatitude(),
                    new HttpManager.ResultCallback<BaseResponse>() {
                        @Override
                        public void onSuccess(BaseResponse baseResponse) {
                            Log.d("xxxxxx", "postUserLocation success");
                            MeService.setMyLocation(locationVO); //更新位置
                            BasePref.saveLong(KEY_LOCATE_SUCCESS, System.currentTimeMillis());
                            Log.d("xxxxxx", "高德定位成功");
                            RefreshLocationEvent.postEvent(locationVO, true,
                                    LocateReqManager.currentLocateRequest.getLoacateSender());
                        }

                        @Override
                        public void onError(String errString) {
                            Log.d("xxxxxx", "postUserLocation error");
                            Log.d("xxxxxx", "高德定位失败2");
                            RefreshLocationEvent.postEvent(locationVO, false,
                                    LocateReqManager.currentLocateRequest.getLoacateSender());
                        }
                    });
        } else {
            Log.d("xxxxxx", "定位成功，但还未登录，上传数据失败");
        }
    }

    //如果定位到的位置距离上一个保存的位置超过500米，则更新位置，并上传，否则不动
    private boolean isOver500m(LocationVO locationVO) {
        if (locationVO == null) {
            return false;
        }
        LocationVO oldLocationVO = MeService.getMyLocation();
        if (oldLocationVO == null) {
            return true;
        }
        //单位是千米，是否大于0.5km
        double distance = LocationUtils.getDistance(oldLocationVO.getLatitude(), oldLocationVO.getLongitude(),
                locationVO.getLatitude(), locationVO.getLongitude());
        if (distance >= 0.5) {
            return true;
        }

        return false;
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
