package cn.yunchuang.im.zmico.utils;

import android.app.Application;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.Locale;


import cn.yunchuang.im.App;

/**
 * Created by liumingkong on 14-4-4.
 * 加载资源文件中的文案
 */
public class ResourceUtils {

    private static float mDensity;
    private static int densityDpi;
    private static int mScreenWidth;
    private static int mScreenHeight;

    public static String[] getStringArray(int arrayId) {
        return getResources().getStringArray(arrayId);
    }

    public static Resources getResources() {
        return App.getAppContext().getResources();
    }

    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    public static float getDensity() {
        if (mDensity <= 0) {
            mDensity = getResources().getDisplayMetrics().density;
        }
        return mDensity;
    }

    public static int getDensityDpi() {
        if (densityDpi <= 0) {
            densityDpi = getResources().getDisplayMetrics().densityDpi;
        }
        return densityDpi;
    }

    public static float dp2PX(float dp) {
        return getDensity() * dp;
    }

    public static int getScreenWidth() {
        if (mScreenWidth <= 0) {
            mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        }
        return mScreenWidth;
    }

    //小心使用，某些机型上可能？
    public static int getScreenHeight() {
        if (mScreenHeight <= 0) {
            mScreenHeight = getResources().getDisplayMetrics().heightPixels;
        }
        return mScreenHeight;
    }

    public static int getColor(int colorId) {
        return getResources().getColor(colorId);
    }

    public static ColorStateList getColorStateList(int colorId) {
        return getResources().getColorStateList(colorId);
    }

    public static float getDimen(int resId) {
        return getResources().getDimension(resId);
    }

    public static int getDimensionPixelSize(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    //======================string format相关======================
    public static String resourceString(int resId, Object... formatArgs) {
        try {
            return getResources().getString(resId, formatArgs);
        } catch (Throwable e) {
//            Ln.e(e);
        }
        return "";
    }

    public static String resourceString(String raw, Object... formatArgs) {
        try {
            return String.format(raw, formatArgs);
        } catch (Throwable e) {
//            Ln.e(e);
        }
        return "";
    }

    public static String getQuantityString(int resId, int quantity) {
        try {
            return getResources().getQuantityString(resId, quantity);
        } catch (Throwable e) {
//            Ln.e(e);
        }
        return "";
    }

    public static String resourceString(int strId) {
        if (strId == -1) return "";
        return App.getAppContext().getString(strId);
    }

    public static String resourceString(Locale locale, int resId, Object... formatArgs) {
        try {
            String raw = resourceString(resId);
            return String.format(locale, raw, formatArgs);
        } catch (Throwable e) {
//            Ln.e(e);
        }
        return "";
    }
}
