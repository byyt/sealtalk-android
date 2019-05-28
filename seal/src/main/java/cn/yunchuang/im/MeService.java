package cn.yunchuang.im;

import cn.yunchuang.im.location.LocationVO;
import cn.yunchuang.im.sp.BasePref;
import cn.yunchuang.im.zmico.utils.Utils;

import static android.content.Context.MODE_PRIVATE;
import static cn.yunchuang.im.sp.BasePref.KEY_CITY;
import static cn.yunchuang.im.sp.BasePref.KEY_LATITUDE;
import static cn.yunchuang.im.sp.BasePref.KEY_LONGITUDE;
import static cn.yunchuang.im.sp.BasePref.KEY_MY_BALANCE;
import static cn.yunchuang.im.sp.BasePref.KEY_MY_COINS;

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
            BasePref.saveString(KEY_CITY, String.valueOf(myLocation.getCity()));
        }
    }

    public static LocationVO getMyLocation() {
        LocationVO myLocation = new LocationVO();
        myLocation.setLongitude(Double.valueOf(BasePref.getString(KEY_LONGITUDE, "0")));
        myLocation.setLatitude(Double.valueOf(BasePref.getString(KEY_LATITUDE, "0")));
        myLocation.setCity(BasePref.getString(KEY_CITY, ""));
        return myLocation;
    }

    public static void setMyBalance(double balance) {
//        Ln.d("setMyBalance:" + balance);
        BasePref.saveString(KEY_MY_BALANCE, String.valueOf(balance >= 0 ? balance : 0));
    }

    public static double getMyBalance() {
        double myBalance = Double.valueOf(BasePref.getString(KEY_MY_BALANCE, "0"));
//        Ln.d("getMyBalance:" + myBalance);
        return myBalance;
    }

    public static void setMyCoin(long coins) {
//        Ln.d("setMyCoin:" + coin);
        BasePref.saveString(KEY_MY_COINS, String.valueOf(coins >= 0 ? coins : 0));
    }

    public static long getMyCoin() {
        long myCoin = Long.valueOf(BasePref.getString(KEY_MY_COINS, "0"));
//        Ln.d("getMyCoin:" + myCoin);
        return myCoin;
    }

}
