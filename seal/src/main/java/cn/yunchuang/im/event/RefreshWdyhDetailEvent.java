package cn.yunchuang.im.event;


import org.greenrobot.eventbus.EventBus;

public class RefreshWdyhDetailEvent {

    private String wdyhOrderId;

    public String getWdyhOrderId() {
        return wdyhOrderId;
    }

    public void setWdyhOrderId(String wdyhOrderId) {
        this.wdyhOrderId = wdyhOrderId;
    }

    public RefreshWdyhDetailEvent(String wdyhOrderId) {
        this.wdyhOrderId = wdyhOrderId;
    }

    public static void postEvent(String wdyhOrderId) {
        RefreshWdyhDetailEvent event = new RefreshWdyhDetailEvent(wdyhOrderId);
        EventBus.getDefault().post(event);
    }
}
