package cn.yunchuang.im.location;


import android.util.Log;

import cn.yunchuang.im.MeService;
import cn.yunchuang.im.constants.TimeConstants;
import cn.yunchuang.im.event.RefreshLocationEvent;
import cn.yunchuang.im.sp.BasePref;

import static cn.yunchuang.im.sp.BasePref.KEY_LOCATE_SUCCESS;

/**
 * Created by liumingkong on 2017/6/12.
 */

public class LocApiReqDispatch implements Runnable {

    private boolean isForceUpdate;

    public LocApiReqDispatch(boolean isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

    @Override
    public void run() {
        requestLocation(isForceUpdate);
    }

    // 开始获取地理位置,isForceUpdate强制获取地理位置
    private void requestLocation(boolean isForceUpdate) {
        if (isForceUpdate || isOverRefreshLocationTime()) { //强制更新或者距离上一次成功定位已经超过2分钟了，则进行定位
            LocateManager.INSTANCE.meetsLocateFinder.requestLocate();//进行高德定位
            Log.d("xxxxxx", "进行高德定位");
        } else {//说明前面一次定位成功在2分钟以内，比如启动页就定位成功了，到主页时直接使用保存的位置即可
            Log.d("xxxxxx", "2分钟已经定位成功过，直接使用sp数据");
            RefreshLocationEvent.postEvent(MeService.getMyLocation(), true,
                    LocateReqManager.currentLocateRequest.getLoacateSender());
        }
    }

    private boolean isOverRefreshLocationTime() { // 超过2分钟后才能重新定位，即使定位成功，也需要与旧距离超过500米才会进行位置保存和上传
        boolean ringFlag = false;
        long historyTimestamp = BasePref.getLong(KEY_LOCATE_SUCCESS, 0L);
        long nowTimestamp = System.currentTimeMillis();
        long sliceTimestamp = nowTimestamp - historyTimestamp;
        if (sliceTimestamp > TimeConstants.MIN_2) { //2分钟
            ringFlag = true;
        }
        return ringFlag;
    }
}
