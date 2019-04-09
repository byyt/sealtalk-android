package cn.yunchuang.im.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.HashSet;
import java.util.Set;

import cn.yunchuang.im.App;
import cn.yunchuang.im.zmico.utils.Utils;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by liumingkong on 15/5/26.
 */
public class ConfigPref {

    public static boolean isLogined() {
        SharedPreferences sp = App.getAppContext().getSharedPreferences("config", MODE_PRIVATE);
        String cacheToken = sp.getString("loginToken", "");
        if (!TextUtils.isEmpty(cacheToken)) {
            return true;
        } else {
            return false;
        }
    }

}
