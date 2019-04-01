package cn.yunchuang.im.widget.dialog_hdzy;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import cn.yunchuang.im.R;


/**
 * Created by ZhuMing on 2018/8/10.
 * <p>
 */
public class VZyCustomBaseDialog extends Dialog implements View.OnClickListener {
    public static final String TAG = "VZyCustomBaseDialog";
    private Activity activity;
    private FrameLayout flContent;

    private View childView;
    private int childTag;
    private String markTag;

    private boolean isFullScreen = false; //是否全屏，隐藏状态栏

    private static VZyCustomBaseDialog baseDialog;
    /**
     * 标识层级
     */
    private static final int KEY_TIER = 0x7f040000;

    /**
     * 表示唯一性
     */
    private static final int KEY_UNIQUE = 0x7f040001;

    public static VZyCustomBaseDialog getInstance(Activity context) {
        if (baseDialog == null) {
            baseDialog = new VZyCustomBaseDialog(context);
        }
        return baseDialog;
    }

    public void setViewData(View view, int childTag, String markTag) {
        this.childView = view;
        this.childTag = childTag;
        this.markTag = markTag;
        if (isShowing()) {
            addView();
        }
    }

    public void setFullScreen(boolean isFullScreen) {
        this.isFullScreen = isFullScreen;
    }

    private VZyCustomBaseDialog(Activity context) {
        super(context, R.style.activityDialogTranslucent);
        this.activity = context;
    }

    @Override
    protected void onStart() {
        super.onStart();
        dialogStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "child----onCreateonCreateonCreateonCreateonCreate--1-" + childTag);
        initView();
        addView();
    }

    //子类重写，可以
    public void dialogStart() {
        if (isFullScreen) {
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

    private void initView() {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_custom_base, null);
        setContentView(view);
        flContent = (FrameLayout) view.findViewById(R.id.fl_content);
    }


    /**
     * tag 1表示第一个显示 2表示第二个显示 3表示第三个显示 4表示第四个显示
     */
    public void addView() {
        if (flContent != null && childView != null) {
            Log.i(TAG, "child--开始添加view----");
            if (!isExitView(markTag)) {
                childView.setTag(KEY_TIER, childTag);
                childView.setTag(KEY_UNIQUE, markTag);
                flContent.addView(childView, getIndex(childTag));
            }
        } else {
            Log.i(TAG, "child--flContent或者childView为null--flContent---->" + flContent + "--childView--->" + childView);
        }
    }

    private boolean isExitView(String markTag) {
        int childCount = flContent.getChildCount();
        boolean result = false;
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                String childMark = (String) flContent.getChildAt(i).getTag(KEY_UNIQUE);
                if (childMark.equals(markTag)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    private int getIndex(int tag) {
        int index = 0;
        int childCount = flContent.getChildCount();
        if (childCount > 0) {
            boolean b = false;
            for (int i = 0; i < childCount; i++) {
                int childTag = (int) flContent.getChildAt(i).getTag(KEY_TIER);
                Log.i(TAG, "child--1--------------:" + childTag);
                if (childTag == tag) {
                    index = i;
                    b = true;
                    break;
                }
            }

            if (!b) {
                for (int i = 0; i < childCount; i++) {
                    int childTag = (int) flContent.getChildAt(i).getTag(KEY_TIER);
                    Log.i(TAG, "child--2----:" + childTag);
                    if (tag < childTag) {
                        index = i + 1;
                        break;
                    }
                }
            }
        } else {
            index = 0;
        }
        Log.i(TAG, "child--index----:" + index);
        return index;
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "expired---onClick-->:");
    }

    /**
     * 监听物理返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i(TAG, "child----" + flContent.getChildCount());
            boolean b = VZyBaseDialogManager.getInstance().onDialogKeyDown(flContent.getChildAt(flContent.getChildCount() - 1));
            if (!b) {
                closeCurrentView();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 关闭当前的view
     */
    public void closeCurrentView() {
        if (flContent != null) {
            View childAt = flContent.getChildAt(flContent.getChildCount() - 1);
            VZyBaseDialogManager.getInstance().removeListener(childAt);
            ConstantDialogUtils.isShowDialog = false;
            if (flContent.getChildCount() > 1) {
                flContent.removeView(childAt);
            } else {
                flContent.removeView(childAt);
                dismiss();
            }
        }
    }

    /**
     * 创建
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG, "child---onAttachedToWindow()-->:");
    }

    /**
     * 销毁
     */
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (baseDialog != null) {
            baseDialog = null;
        }
        Log.i(TAG, "child---onDetachedFromWindow()-->:");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "child--onCloseExpireDialog---onStop---->");
    }
}