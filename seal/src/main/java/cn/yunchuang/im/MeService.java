package cn.yunchuang.im;

import cn.yunchuang.im.location.LocationVO;
import cn.yunchuang.im.sp.BasePref;
import cn.yunchuang.im.zmico.utils.Utils;

import static android.content.Context.MODE_PRIVATE;
import static cn.yunchuang.im.sp.BasePref.KEY_LATITUDE;
import static cn.yunchuang.im.sp.BasePref.KEY_LONGITUDE;

/**
 * Created by zhou_yuntao on 2019/3/30.
 */

public class MeService {

    public static String getUid() {
        return App.getAppContext().getSharedPreferences("config", MODE_PRIVATE)
                .getString(SealConst.SEALTALK_LOGIN_ID, "");
    }

    public static void setMyLocation(LocationVO myLocation) {
        if (!Utils.isNull(myLocation)) {
            BasePref.saveString(KEY_LONGITUDE, String.valueOf(myLocation.getLongitude()));
            BasePref.saveString(KEY_LATITUDE, String.valueOf(myLocation.getLatitude()));
        }
    }

    public static LocationVO getMyLocation() {
        LocationVO myLocation = new LocationVO();
        myLocation.setLongitude(Double.valueOf(BasePref.getString(KEY_LONGITUDE, "0")));
        myLocation.setLatitude(Double.valueOf(BasePref.getString(KEY_LATITUDE, "0")));
        return myLocation;
    }

}
