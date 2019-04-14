package cn.yunchuang.im.zmico.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


/**
 * Created by liumingkong on 2017/6/19.
 */

public class DeviceUtils {

    private static final String TAG = "DeviceUtils";

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeightPixels(Context context) {
        int statusBarHeight = 0;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * statusBar高度
     **/
    public static int getStatusBarHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.top;

    }

    /**
     * 可见屏幕高度
     **/
    public static int getVisibleFrameHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.height();
    }

    public static int dip2px(Activity activity, float dip) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getDisplayMetrics(activity)));
    }

    // 设备的屏幕分辨率
    public static String getDeviceScreen(Context context) {
        try {
            Resources resources = context.getResources();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            return "height=" + displayMetrics.heightPixels + ",width:" + displayMetrics.widthPixels + ",density:" + displayMetrics.density;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isScreenLock(Context context) {
        boolean isScreenLock = false;
        try {
            KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(context.KEYGUARD_SERVICE);
            isScreenLock = mKeyguardManager.inKeyguardRestrictedInputMode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isScreenLock;
    }

    public static boolean isSpecifyProcess(Context context, String packageName) {
        try {
            int myPid1 = android.os.Process.myPid();//获取当前运行进程pid
            String processName = "";
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if (appProcess.pid == myPid1) {
                    processName = appProcess.processName;
                    break;
                }
            }
            Log.d(TAG, "packageName:" + packageName + ",processName:" + processName);
            return packageName.equals(processName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int dp2px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static float sp2px(Context context, float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    public static float pxToDp(float px) {
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

    public static int dpToPx(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static int dpToPx(float dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static float spToPx(int sp) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }

    public static float pxToSp(int sp) {
        final float scale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return sp / scale;
    }

    public static double getDensity(Activity activity) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return (double) mDisplayMetrics.density;
    }

    public static float getScreenDensity(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.density;
    }

    public static int getScreenHeightPixels(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getScreenWidthPixels(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static int px2dp(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    // 屏幕的真实高度,获取的像素宽高包含虚拟键所占空间
    public static int getScreenHeightPixelsReal(Context context) {
        int screenHeight = 0;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                DisplayMetrics dm = new DisplayMetrics();
                WindowManager windowMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                windowMgr.getDefaultDisplay().getRealMetrics(dm);
                screenHeight = dm.heightPixels;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenHeight;
    }

    // 获取的像素宽高不包含虚拟键所占空间
    // getScreenHeightPixelsReal - getScreenHeightPixelsVirtual = 虚拟按键的高度
    public static int getScreenHeightPixelsVirtual(Context context) {
        int screenHeight = 0;
        try {
            screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenHeight;
    }

    /**
     * 仅6.0系统以上隐藏状态栏
     *
     * @param activity
     */
    public static void setTranslucentStatus(Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 仅6.0系统以上隐藏状态栏
     *
     * @param activity
     * @param isDark   状态栏图标是否变为深色 true：深色  false：浅色
     */
    public static void setTranslucentStatus(Activity activity, boolean isDark) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            if (isDark) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
