package cn.yunchuang.im.location;

/**
 * Created by liumingkong on 14-7-19.
 */
public class LocateApiResp {

    public boolean flag;
    public double latitude;
    public double longitude;
    public String errorInfo;

    public LocateApiResp(boolean flag,
                         double latitude, double longitude) {
        this.flag = flag;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocateApiResp(boolean flag,
                         String errorInfo) {
        this.flag = flag;
        this.errorInfo = errorInfo;
    }

    public static void sendLocateSucc(double latitude, double longitude) {
        LocateManager.INSTANCE.dispatchApiLocResp(new LocateApiResp(true, latitude, longitude));
    }

    public static void sendLocateFail(String errorInfo) {
        LocateManager.INSTANCE.dispatchApiLocResp(new LocateApiResp(false, errorInfo));
    }

}
