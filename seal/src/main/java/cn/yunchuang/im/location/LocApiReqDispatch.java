package cn.yunchuang.im.location;


import cn.yunchuang.im.constants.TimeConstants;
import cn.yunchuang.im.sp.BasePref;

import static cn.yunchuang.im.sp.BasePref.KEY_LOCATE_REFRESH;

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
        }
    }

    private boolean isOverRefreshLocationTime() { // 超过2分钟后才能重新定位，即使定位成功，也需要与旧距离超过500米才会进行位置保存和上传
        boolean ringFlag = false;
        long historyTimestamp = BasePref.getLong(KEY_LOCATE_REFRESH, 0L);
        long nowTimestamp = System.currentTimeMillis();
        long sliceTimestamp = nowTimestamp - historyTimestamp;
        if (sliceTimestamp > TimeConstants.MIN_2) { //2分钟
            ringFlag = true;
        }
        return ringFlag;
    }
}
