package cn.yunchuang.im.zmico.utils;

import android.os.Looper;

import java.util.Collection;

/**
 * Created by liumingkong on 14-3-19.
 */
public class Utils {

    public static boolean isEmptyArray(Object[] array) {
        if (isNull(array) || isZero(array.length)) {
            return true;
        } else {
            return false;
        }
    }

    public static int getCollectionSize(Collection collection) {
        return collection == null ? 0 : collection.size();
    }

    public static boolean isEmptyCollection(Collection collection) {
        if (isNull(collection) || isZero(collection.size())) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotEmptyCollection(Collection collection) {
        if (isNull(collection) || isZero(collection.size())) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isNull(Object obj) {
        if (obj == null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNullObjects(Object... objects) {
        for (Object o : objects) {
            if (isNull(o)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isEmptyString(String str) {
        if (isNull(str)) {
            return true;
        } else {
            if (isZero(str.trim().length())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean ensureNotNull(Object... objects) {
        for (Object o : objects) {
            if (isNull(o)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmptyString(String str) {
        return !isEmptyString(str);
    }

    public static boolean isZero(int num) {
        if (num == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isZeroLong(long num) {
        if (num == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isZeroDouble(double num) {
        if (num == 0.0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmptyByte(byte[] content) {
        if (isNull(content) || isZero(content.length)) {
            return true;
        } else {
            return false;
        }
    }

    // 判断当前线程是否为主线程
    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    private static long lastClickTime;
    private static long lastClickLongTime;
    private static final int MIN_CLICK_DELAY_TIME = 400;
    private static final int MIN_CLICK_DELAY_TIME_LONG = 1000;

    // 是否是过快点击(在同一点击事件里最好只调用1次！！！)
    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (lastClickTime < time && time - lastClickTime < MIN_CLICK_DELAY_TIME) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }

    // 操作类适用，发动态，save等
    public static boolean isLongFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickLongTime < MIN_CLICK_DELAY_TIME_LONG) {
            return true;
        } else {
            lastClickLongTime = time;
            return false;
        }
    }

    /**
     * 不允许为null,否者返回false
     */
    public static boolean isEquals(Object o1, Object o2) {
        return o1 != null && o2 != null && (o1.equals(o2));
    }

    /**
     * 允许为null
     */
    public static boolean isEqualsAllowNull(Object o1, Object o2) {
        boolean isO1Valid = o1 != null;
        boolean isO2Valid = o2 != null;
        boolean result;
        if (isO1Valid && isO2Valid) {
            result = o1.equals(o2);
        } else {
            result = !isO1Valid && !isO2Valid;
        }
        return result;
    }

    // 保证string的返回结果至少为空字符串
    public static String getStringNotNull(String str) {
        if (isNull(str)) {
            return "";
        } else {
            return str;
        }
    }
}
