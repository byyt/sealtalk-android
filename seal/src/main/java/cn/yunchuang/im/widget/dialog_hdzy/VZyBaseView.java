package cn.yunchuang.im.widget.dialog_hdzy;

import android.app.Activity;
import android.view.View;

/**
 * Created by ZhuMing on 2018/8/30.
 * dialog base
 */

public abstract class VZyBaseView implements VZyBaseDialogBackListener {
    public VZyCustomBaseDialog baseDialog;
    private Activity activity;

    /**
     * 构造函数
     *
     * @param activity 必须是activity 不能是context
     */
    public VZyBaseView(Activity activity) {
        this.activity = activity;
        baseDialog = VZyCustomBaseDialog.getInstance(activity);
    }

    protected abstract View initView();

    /**
     * 显示dialog 中的view
     *
     * @param level  层级
     * @param unique 标识唯一值
     */
    public void showDialog(int level, String unique, boolean isFullScreen) {
        if (baseDialog != null) {
            View view = initView();
            VZyBaseDialogManager.getInstance().addListener(view, this);
            baseDialog.setViewData(view, level, unique);
            baseDialog.setFullScreen(isFullScreen);
            if (!baseDialog.isShowing()) {
                ConstantDialogUtils.isShowDialog = true;
                baseDialog.show();
            }
        }
    }


    /**
     * 子类可以自己处理返回逻辑
     *
     * @return 默认 false
     */
    @Override
    public boolean onDialogKeyDown() {

        return false;
    }
}
