package cn.yunchuang.im.event;


import org.greenrobot.eventbus.EventBus;

public class SplashCheckPermissionsEvent {

    private boolean agreeLocationPermisssion;

    public boolean isAgreeLocationPermisssion() {
        return agreeLocationPermisssion;
    }

    public void setAgreeLocationPermisssion(boolean agreeLocationPermisssion) {
        this.agreeLocationPermisssion = agreeLocationPermisssion;
    }

    public SplashCheckPermissionsEvent(boolean agreeLocationPermisssion) {
        this.agreeLocationPermisssion = agreeLocationPermisssion;
    }

    public static void postEvent(boolean agreeLocationPermisssion) {
        SplashCheckPermissionsEvent event = new SplashCheckPermissionsEvent(agreeLocationPermisssion);
        EventBus.getDefault().post(event);
    }
}

