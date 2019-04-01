package cn.yunchuang.im.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.yunchuang.im.R;


/**
 * 输入的弹窗，内容单行
 */
public class ShuruDialog extends AlertDialog {
    private TextView mTv_title;
    private EditText mTv_content;
    private Button mBtn_left;
    private Button mBtn_right;

    public ShuruDialog(Context context) {
        super(context);
    }

    public ShuruDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public ShuruDialog(Context context, int themeResId) {
        super(context, themeResId);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;//指定他的位置在底部中心的位置
        DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8f); // 宽度默认设置为屏幕的0.72
        Window window = getWindow();
        if (window != null) {
            window.setAttributes(lp);

            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setDimAmount(0.5f);
            //只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            //加上下面这一行弹出对话框时软键盘随之弹出
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        }
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_shuru_danhang, null);
        mTv_title = view.findViewById(R.id.dialog_shuru_title);
        mTv_content = view.findViewById(R.id.dialog_shuru_et);
        mBtn_left = view.findViewById(R.id.dialog_shuru_btn_left);
        mBtn_right = view.findViewById(R.id.dialog_shuru_btn_right);

        mBtn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mDialogListener != null) {
                    mDialogListener.leftButtonEvent();
                }
            }
        });
        mBtn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mDialogListener != null) {
                    mDialogListener.rightButtonEvent(mTv_content.getText().toString());
                }
            }
        });
        setContentView(view);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public DialogListener mDialogListener;

    public void setDialogListener(DialogListener listener) {
        mDialogListener = listener;
    }

    public interface DialogListener {
        void leftButtonEvent();

        void rightButtonEvent(String content);
    }

    public interface ConfirmListener {
        void confirm(String content);
    }

    public static abstract class SimpleDialog implements DialogListener {
        public void leftButtonEvent() {
        }
    }

    public ShuruDialog setTitle(String title) {
        if (mTv_title != null) {
            mTv_title.setText(title);
            if (TextUtils.isEmpty(title)) {
                mTv_title.setVisibility(View.GONE);
            }
        }
        return this;
    }

    public ShuruDialog setContent(String message) {
        if (mTv_content != null) {
            mTv_content.setText(message);
            if (!TextUtils.isEmpty(message)) {
                mTv_content.setSelection(message.length());
            }
        }
        return this;
    }

    public ShuruDialog setLeftText(String text) {
        if (mBtn_left != null) {
            mBtn_left.setText(text);

        }
        return this;
    }

    public ShuruDialog setRightText(String text) {
        if (mBtn_right != null) {
            mBtn_right.setText(text);
        }
        return this;
    }
}
