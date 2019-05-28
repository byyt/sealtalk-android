package cn.yunchuang.im.sp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import cn.yunchuang.im.App;
import cn.yunchuang.im.zmico.utils.Utils;

/**
 * Created by liumingkong on 15/5/26.
 */
public class BasePref {

    private static final String YUNCHUANG_ACCOUNT_PREFERENCE = "YUNCHUANG_ACCOUNT_PREFERENCE";

    //key值
    public static final String KEY_LOCATE_SUCCESS = "KEY_LOCATE_SUCCESS"; //最近一次定位成功时间
    public static final String KEY_LONGITUDE = "KEY_LONGITUDE"; //
    public static final String KEY_LATITUDE = "KEY_LATITUDE";
    public static final String KEY_CITY = "KEY_CITY";
    public static final String KEY_MY_BALANCE = "KEY_MY_BALANCE";
    public static final String KEY_MY_COINS = "KEY_MY_COINS";

    private static SharedPreferences getPreference() {
        return App.getAppContext().getSharedPreferences(YUNCHUANG_ACCOUNT_PREFERENCE, Context.MODE_PRIVATE);
    }
//   TODO 跟设备的信息永不删除
//    public static void clearAll(Context context) {
//        SharedPreferences.Editor editor = getPreference().edit();
//        editor.clear();
//        editor.apply();
//    }

    public static boolean contains(String key) {
        if (Utils.isEmptyString(key)) {
            return false;
        }
        return getPreference().contains(key);
    }

    public static void remove(String key) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.remove(key);
        editor.apply();
    }

    public static int getInt(String tag, int defaultValue) {
        return getPreference().getInt(tag, defaultValue);
    }

    public static void saveInt(String tag, int value) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putInt(tag, value);
        editor.apply();
    }

    public static long getLong(String tag, long defaultValue) {
        return getPreference().getLong(tag, defaultValue);
    }

    public static void saveLong(String tag, long value) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putLong(tag, value);
        editor.apply();
    }

    public static float getFloat(String tag, float defaultValue) {
        return getPreference().getFloat(tag, defaultValue);
    }

    public static void saveFloat(String tag, float value) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putFloat(tag, value);
        editor.apply();
    }

    public static boolean getBoolean(String tag, boolean defaultValue) {
        return getPreference().getBoolean(tag, defaultValue);
    }

    public static void saveBoolean(String tag, boolean value) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putBoolean(tag, value);
        editor.apply();
    }

    public static String getString(String tag, String defaultValue) {
        return getPreference().getString(tag, defaultValue);
    }

    public static void saveString(String tag, String value) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putString(tag, value);
        editor.apply();
    }

    public static Set<String> getStringSet(String tag) {
        return getPreference().getStringSet(tag, new HashSet<String>());
    }

    public static void saveStringSet(String tag, Set<String> value) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putStringSet(tag, value);
        editor.apply();
    }
}
