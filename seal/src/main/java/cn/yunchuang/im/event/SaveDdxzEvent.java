package cn.yunchuang.im.event;


import com.amap.api.services.core.LatLonPoint;

import org.greenrobot.eventbus.EventBus;

public class SaveDdxzEvent {

    private LatLonPoint selectedPoint;//最终选择地点的经纬度
    private String selectedName;//选择地点的名称

    public LatLonPoint getSelectedPoint() {
        return selectedPoint;
    }

    public String getSelectedName() {
        return selectedName;
    }

    public SaveDdxzEvent(LatLonPoint selectedPoint, String selectedName) {
        this.selectedPoint = selectedPoint;
        this.selectedName = selectedName;
    }

    public static void postEvent(LatLonPoint selectedPoint, String selectedName) {
        SaveDdxzEvent event = new SaveDdxzEvent(selectedPoint, selectedName);
        EventBus.getDefault().post(event);
    }


}
