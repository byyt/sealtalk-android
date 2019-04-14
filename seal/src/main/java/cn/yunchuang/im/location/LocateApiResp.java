package cn.yunchuang.im.location;

import android.util.Log;

/**
 * Created by liumingkong on 14-7-19.
 */
public class LocateApiResp {

    public boolean flag;
    public double latitude;
    public double longitude;
    public String city;
    public String errorInfo;

    public LocateApiResp(boolean flag,
                         double latitude, double longitude, String city) {
        this.flag = flag;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }

    public LocateApiResp(boolean flag,
                         String errorInfo) {
        this.flag = flag;
        this.errorInfo = errorInfo;
    }

    public static void sendLocateSucc(double latitude, double longitude, String city) {
        LocateManager.INSTANCE.dispatchApiLocResp(new LocateApiResp(true, latitude, longitude, city));
        Log.d("xxxxxx","sendLocateSucc");
    }

    public static void sendLocateFail(String errorInfo) {
        LocateManager.INSTANCE.dispatchApiLocResp(new LocateApiResp(false, errorInfo));
        Log.d("xxxxxx","sendLocateFail");
    }

}
