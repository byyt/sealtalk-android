package cn.yunchuang.im.location;

import android.app.Activity;

/**
 * Created by zhou_yuntao on 2019/4/14.
 */

public class LocateRequest {

    private String LoacateSender;  //发起请求的类名

    private boolean isForce;       //是否强制请求

    public LocateRequest(String LoacateSender, boolean isForce) {
        this.LoacateSender = LoacateSender;
        this.isForce = isForce;
    }

    public String getLoacateSender() {
        return LoacateSender;
    }

    public void setLoacateSender(String loacateSender) {
        LoacateSender = loacateSender;
    }

    public boolean isForce() {
        return isForce;
    }

    public void setForce(boolean force) {
        isForce = force;
    }
}
