package cn.yunchuang.im.event;


import org.greenrobot.eventbus.EventBus;

public class RefreshMineInfoEvent {

    public static void postEvent() {
        RefreshMineInfoEvent event = new RefreshMineInfoEvent();
        EventBus.getDefault().post(event);
    }
}
