package cn.yunchuang.im.location;

import com.amap.api.location.AMapLocationClient;

/**
 * Created by liumingkong on 2017/3/14.
 */

public class GaodeKeyUtils {

    // 初始化高德定位
    public static void initAMapLocationClient() {
        try {
            AMapLocationClient.setApiKey("d27a7092668126766d7f59009a34e784");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
