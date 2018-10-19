package cn.yunchuang.im.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by jack-lu on 15/11/16.
 */
public class ViewUtil {
    private static DisplayMetrics sDisplay;
    private static int heightOfStatusBar = 0;

    public static int getScreenWidth(Context context) {
        if (sDisplay == null) {
            sDisplay = context.getResources().getDisplayMetrics();
        }
        return sDisplay.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        if (sDisplay == null) {
            sDisplay = context.getResources().getDisplayMetrics();
        }
        return sDisplay.heightPixels;
    }



    public static int getAvaiableScreenHeight(Context context) {
        int height = getScreenHeight(context);
        if (hasSmartBar()) {
            height -= 120;
        }
        return height;
    }
    public static int getHeightOfStatusBar(Context context) {
        if (heightOfStatusBar == 0) {
            Object obj = null;
            Field field = null;
            Class<?> c = null;
            int x = 0;
            try {
                c = Class.forName("com.android.internal.R$dimen");
                obj = c.newInstance();
                field = c.getField("status_bar_height");
                x = Integer.parseInt(field.get(obj).toString());
                heightOfStatusBar = context.getResources().getDimensionPixelSize(x);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }


        return heightOfStatusBar;
    }


    // 判断是否有SmartBar
    public static boolean hasSmartBar() {
        try {
            // 新型号可用反射调用Build.hasSmartBar()
            Method method = Class.forName("android.os.Build").getMethod(
                    "hasSmartBar");
            return ((Boolean) method.invoke(null)).booleanValue();
        } catch (Exception e) {
        }
        // 反射不到Build.hasSmartBar()，则用Build.DEVICE判断
        if (Build.DEVICE.equals("mx2")) {
            return true;
        } else if (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
            return false;
        }
        return false;
    }


    /**
     * 关闭输入法
     */
    public static void closeInput(Activity mAct) {
        if (mAct != null && mAct.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) mAct
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mAct.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    public static void closeInputQuick(View mAct) {
        if (mAct != null && mAct.getContext() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) mAct.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mAct
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }


    public static void openInput(Activity mAct) {
//        final InputMethodManager im = (InputMethodManager) mAct.getSystemService(Context.INPUT_METHOD_SERVICE);
//        ScheduledFuture scheduledFuture = ThreadManager.getScheduledThreadPool().schedule(new Runnable() {
//            @Override
//            public void run() {
//                if (im.isActive()) {
//                    im.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
//                }
//            }
//        }, 500, TimeUnit.MILLISECONDS);
    }

    public static void openInputQuick(View mAct) {
        InputMethodManager im = (InputMethodManager) mAct.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (im.isActive()) {
            im.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }


    /**
     * 判断输入法是否打开
     *
     * @param mAct
     * @return
     */
    public static boolean isOpenInput(Activity mAct) {
        InputMethodManager imm = (InputMethodManager) mAct
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        return isOpen;
    }

    /**
     * 判断状态栏是否可见
     *
     * @param mAct
     * @return
     */
    public static boolean isAvaliableStatusBar(Activity mAct) {
        WindowManager.LayoutParams attrs = mAct.getWindow().getAttributes();

        return (attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

    /**
     * 根据控件宽度等比例缩放图片
     *
     * @param bitmap
     * @param screenWidth
     * @return
     */
    public static Bitmap adaptiveW(Bitmap bitmap, float screenWidth) {
        float scale;
        Matrix matrix = new Matrix();
        int width = bitmap.getWidth();// 获取资源位图的宽
        int height = bitmap.getHeight();// 获取资源位图的高
//		if (width > screenWidth) {
        float w = screenWidth / bitmap.getWidth();
        matrix.postScale(w, w);// 获取缩放比例
        // 根据缩放比例获取新的位图
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newbmp;

//		}
//		return bitmap;
    }

    /**
     * 根据控件高度等比例缩放图片
     *
     * @param bitmap
     * @param screenHeight
     * @return
     */
    public static Bitmap adaptiveH(Bitmap bitmap, float screenHeight) {
        float scale;
        Matrix matrix = new Matrix();
        int width = bitmap.getWidth();// 获取资源位图的宽
        int height = bitmap.getHeight();// 获取资源位图的高
        if (height > screenHeight) {
            float w = screenHeight / bitmap.getHeight();
            matrix.postScale(w, w);// 获取缩放比例
            // 根据缩放比例获取新的位图
            Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                    matrix, true);
            return newbmp;

        }
        return bitmap;
    }

    /*
* 设置控件所在的位置X，并且不改变宽高，
* X为绝对位置，此时Y可能归0
*/
    public static void setLayoutX(View view, int x) {
        MarginLayoutParams margin = new MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x, margin.topMargin, x + margin.width, margin.bottomMargin);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }

    /*
    * 设置控件所在的位置Y，并且不改变宽高，
    * Y为绝对位置，此时X可能归0
    */
    public static void setLayoutY(View view, int y) {
        MarginLayoutParams margin = new MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(margin.leftMargin, y, margin.rightMargin, y + margin.height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }

    /*
    * 设置控件所在的位置YY，并且不改变宽高，
    * XY为绝对位置
    */
    public static void setLayout(View view, int x, int y) {
        MarginLayoutParams margin = new MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x, y, x + margin.width, y + margin.height);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }

    /**
     * try get host activity from view.
     * views hosted on floating window like dialog and toast will sure return null.
     *
     * @return host activity; or null if not available
     */
    public static Activity getActivityFromView(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

}
