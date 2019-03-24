package com.docwei.imageupload_lib.album.statusbar;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;


/**
 * 兼容LOLLIPOP版本
 *
 * @author msdx (msdx.android@qq.com)
 * @version 0.5
 * @since 0.3
 * Window window = activity.getWindow();
 * window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
 * window.setStatusBarColor(color);
 */

class StatusBarLollipopImpl implements IStatusBar {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarColor(Window window, int color) {
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (Color.WHITE == color) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //设置状态栏颜色
                window.setStatusBarColor(color);
            } else {
                window.setStatusBarColor(Color.parseColor("#E6E8EB"));
            }
        } else {
            window.setStatusBarColor(color);
        }
    }

}
