package cn.yunchuang.im.event;


import org.greenrobot.eventbus.EventBus;

import cn.yunchuang.im.location.LocateReqManager;
import cn.yunchuang.im.location.LocationVO;

public class RefreshLocationEvent {

    private LocationVO locationVO;

    private boolean locationSuccess;

    private String locateSender;

    public LocationVO getLocationVO() {
        return locationVO;
    }

    public boolean isLocationSuccess() {
        return locationSuccess;
    }

    public String getLocateSender() {
        return locateSender;
    }

    public RefreshLocationEvent(LocationVO locationVO, boolean locationSuccess, String locateSender) {
        this.locationVO = locationVO;
        this.locationSuccess = locationSuccess;
        this.locateSender = locateSender;
    }

    public static void postEvent(LocationVO locationVO, boolean locationSuccess, String locateSender) {
        RefreshLocationEvent event = new RefreshLocationEvent(locationVO, locationSuccess, locateSender);
        EventBus.getDefault().post(event);
        //多加一个，处理完一次定位请求后，继续从请求队列中取出请求来处理，如果没请求了则不处理
        LocateReqManager.isWorking = false;
        LocateReqManager.startLocate();
    }
}
