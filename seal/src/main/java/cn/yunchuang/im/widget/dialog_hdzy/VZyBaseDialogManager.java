package cn.yunchuang.im.widget.dialog_hdzy;

import android.view.View;

import java.util.HashMap;

/**
 * Created by ZhuMing on 2018/8/30.
 * <p>
 * dialog 监听管理类
 */

public class VZyBaseDialogManager {
    private HashMap<View, VZyBaseDialogBackListener> listeners;
    private static VZyBaseDialogManager dialogManager;

    public static VZyBaseDialogManager getInstance() {
        if (dialogManager == null) {
            dialogManager = new VZyBaseDialogManager();
        }
        return dialogManager;
    }

    private VZyBaseDialogManager() {
        listeners = new HashMap<>(5);
    }

    /**
     * 添加监听
     */
    public void addListener(View view, VZyBaseDialogBackListener listener) {
        if (listeners != null) {
            listeners.put(view, listener);
        }
    }

    /**
     * 移除监听
     */
    public void removeListener(View view) {
        if (listeners != null && listeners.containsKey(view)) {
            listeners.remove(view);
        }
    }

    /**
     * 返回键监听
     *
     * @param view 当前view
     */
    public boolean onDialogKeyDown(View view) {
        if (view != null) {
            return listeners.get(view).onDialogKeyDown();
        }
        return false;
    }
}
