package cn.yunchuang.im.utils;

import android.view.View;

import cn.yunchuang.im.zmico.utils.Utils;

/**
 * Created by liumingkong on 16/4/27.
 */
public class ViewVisibleUtils {

    public static void setVisibleGone(View view, boolean visible) {
        if (Utils.ensureNotNull(view)) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public static void setVisibleGone(boolean visible, View... views) {
        if (!Utils.isNull(views)) {
            final int flag = visible ? View.VISIBLE : View.GONE;
            for (View view : views) {
                if (!Utils.isNull(view)) {
                    view.setVisibility(flag);
                }
            }
        }
    }

    public static void setVisibleInVisible(View view, boolean visible) {
        if (Utils.ensureNotNull(view)) {
            view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public static void setVisibleInVisible(boolean visible, View... views) {
        if (!Utils.isNull(views)) {
            final int flag = visible ? View.VISIBLE : View.INVISIBLE;
            for (View view : views) {
                if (!Utils.isNull(view)) {
                    view.setVisibility(flag);
                }
            }
        }
    }

    public static void setViewGone(View... views) {
        for (View view : views) {
            if (Utils.ensureNotNull(view)) {
                view.setVisibility(View.GONE);
            }
        }
    }

}
