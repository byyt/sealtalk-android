package cn.yunchuang.im.event;


import org.greenrobot.eventbus.EventBus;

import cn.yunchuang.im.model.ShaixuanModel;

public class SaveShaixuanEvent {

    private ShaixuanModel shaixuanModel;
    private String fromFragmentName;

    public ShaixuanModel getShaixuanModel() {
        return shaixuanModel;
    }

    public String getFromFragmentName() {
        return fromFragmentName;
    }

    public SaveShaixuanEvent(ShaixuanModel shaixuanModel, String fromFragmentName) {
        this.shaixuanModel = shaixuanModel;
        this.fromFragmentName = fromFragmentName;
    }

    public static void postEvent(ShaixuanModel shaixuanModel, String fromFragmentName) {
        SaveShaixuanEvent event = new SaveShaixuanEvent(shaixuanModel, fromFragmentName);
        EventBus.getDefault().post(event);
    }


}
