package cn.yunchuang.im;

import android.content.SharedPreferences;
import android.text.TextUtils;

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

    private static LocationVO myLocation;

    public static String getUid() {
        return App.getAppContext().getSharedPreferences("config", MODE_PRIVATE)
                .getString(SealConst.SEALTALK_LOGIN_ID, "");
    }

    public static void setMyLocation(LocationVO myLocation) {
        if (!Utils.isNull(myLocation)) {
            MeService.myLocation = myLocation;
            BasePref.saveString(KEY_LONGITUDE, String.valueOf(myLocation.getLongitude()));
            BasePref.saveString(KEY_LATITUDE, String.valueOf(myLocation.getLatitude()));
        }
    }

    public static LocationVO getMyLocation(LocationVO myLocation) {
        return myLocation;
    }


}
