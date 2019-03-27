package cn.yunchuang.im.utils;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import cn.yunchuang.im.R;
import cn.yunchuang.im.zmico.utils.ResourceUtils;
import cn.yunchuang.im.zmico.utils.Utils;


/**
 * Created by liumingkong on 15/5/19.
 */
public class TextViewUtils {


    // 如果内容为空将会不可见
    public static void setTextAndVisible(TextView textView, CharSequence text) {
        if (!Utils.isNull(textView)) {
            if (!Utils.isNull(text) && !Utils.isEmptyString(text.toString())) {
                textView.setVisibility(View.VISIBLE);
                setTextContent(textView, text);
            } else {
                textView.setVisibility(View.GONE);
            }
        }
    }

    public static void setTextColor(TextView textView, @ColorInt int color) {
        if (!Utils.isNull(textView)) {
            textView.setTextColor(color);
        }
    }

    public static void setHintTextColor(TextView textView, @ColorInt int color) {
        if (!Utils.isNull(textView)) {
            textView.setHintTextColor(color);
        }
    }

    public static void setTextColorStateList(TextView textView, int color) {
        if (!Utils.isNull(textView)) {
            try {
                ColorStateList colorStateList = ResourceUtils.getColorStateList(color);
                textView.setTextColor(colorStateList);
            } catch (Throwable throwable) {
//                Ln.e(throwable);
            }
        }
    }

    // 安全的加载文本信息
    public static void setText(TextView textView, String text) {
        if (!Utils.isNull(textView)) {
            if (!Utils.isEmptyString(text)) {
                setTextContent(textView, text);
            } else {
                setTextContent(textView, "");
            }
        }
    }

    public static void setText(TextView textView, CharSequence text) {
        if (!Utils.isNull(textView)) {
            if (!Utils.isNull(text) && !Utils.isZero(text.length())) {
                setTextContent(textView, text);
            } else {
                setTextContent(textView, "");
            }
        }
    }

    // 安全的加载文本信息
    public static void setText(TextView textView, int resId) {
        if (!Utils.isNull(textView)) {
            String text = ResourceUtils.resourceString(resId);
            if (!Utils.isEmptyString(text)) {
                setTextContent(textView, text);
            } else {
                setTextContent(textView, "");
            }
        }
    }

    private static void setTextContent(TextView textView, String text) {
        try {
            textView.setText(text);
        } catch (Throwable throwable) {
        }
    }

    private static void setTextContent(TextView textView, CharSequence text) {
        try {
            textView.setText(text);
        } catch (Throwable throwable) {
        }
    }

    public static void setHint(TextView textView, String text) {
        if (!Utils.isNull(textView)) {
            if (!Utils.isEmptyString(text)) {
                setHintContent(textView, text);
            } else {
                setHintContent(textView, "");
            }
        }
    }

    public static void setHint(TextView tv, int resId) {
        if (!Utils.isNull(tv)) {
            tv.setHint(resId);
        }
    }

    private static void setHintContent(TextView textView, String text) {
        try {
            textView.setHint(text);
        } catch (Throwable throwable) {
//            Ln.e(throwable);
        }
    }

    // SpannableString 管理

    /**
     * 设置文本的大小
     *
     * @param textView 文本控件
     * @param textSize 文本的大小
     */
    public static void setTextSize(TextView textView, int textSize) {
        if (Utils.isNotNull(textView) && textSize > 0) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }

}
