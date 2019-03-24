package cn.yunchuang.im.zmico.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CompoundButton;
import android.widget.ImageView;

import static cn.yunchuang.im.zmico.utils.Utils.isNotNull;
import static cn.yunchuang.im.zmico.utils.Utils.isNull;


/**
 * Created by frank-mico on 2016/5/27.
 */
public class ViewUtil {

    public static void toggle(CompoundButton btn) {
        if (!isNull(btn)) {
            btn.toggle();
        }
    }

    public static void setChecked(CompoundButton btn, boolean checked) {
        if (!isNull(btn)) {
            btn.setChecked(checked);
        }
    }

    public static void setTag(View view, Object tag) {
        if (!isNull(view)) {
            view.setTag(tag);
        }
    }

    public static void setTag(View view, Object tag, int key) {
        if (!isNull(view)) {
            view.setTag(key, tag);
        }
    }

    public static <T> T getTag(View view, int key) {
        if (!isNull(view)) {
            try {
                return (T) view.getTag(key);
            } catch (Throwable throwable) {
//                Ln.e(throwable);
            }
        }
        return null;
    }

    public static void setImageDrawable(ImageView iv, Drawable drawable) {
        if (!isNull(iv)) {
            iv.setImageDrawable(drawable);
        }
    }

    public static void setEnabled(View view, boolean enable) {
        if (!isNull(view)) {
            view.setEnabled(enable);
        }
    }

    public static void setEnabled(boolean enable, View... views) {
        if (!isNull(views)) {
            for (View view : views) {
                if (isNotNull(view)) {
                    view.setEnabled(enable);
                }
            }
        }
    }

    public static void setClickable(View view, boolean enable) {
        if (!isNull(view)) {
            view.setClickable(enable);
        }
    }

    public static void cancelAnimator(Object animator, boolean removeListenerBefore) {
        if (!isNull(animator)) {
            if (removeListenerBefore) {
                removeListeners(animator);
            }
            if (animator instanceof ViewPropertyAnimatorCompat) {
                ((ViewPropertyAnimatorCompat) animator).cancel();
            } else if (animator instanceof Animator) {
                ((Animator) animator).cancel();
            }
            if (!removeListenerBefore) {
                removeListeners(animator);
            }
        }
    }

    //兼容appcompat
    public static Activity getActivityContext(@NonNull View view) {
        if (!isNull(view)) {
            Context context = view.getContext();
            if (context instanceof Activity) {
                return (Activity) context;
            } else if (context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
                if (context instanceof Activity) {
                    return (Activity) context;
                }
            }
        }
        return null;
    }

    public static boolean setTopMargin(@NonNull View view, int topMargin, boolean needLayout) {
        if (!isNull(view)) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp != null && lp instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) lp;
                layoutParams.topMargin = topMargin;
                if (needLayout) {
                    view.setLayoutParams(layoutParams);
                }
                return true;
            }
        }
        return false;
    }

    public static void setOnLongClickListener(View.OnLongClickListener listener, View... views) {
        if (!isNull(views)) {
            for (View v : views) {
                if (v != null) {
                    v.setOnLongClickListener(listener);
                }
            }
        }
    }

    public static void setOnClickListener(View.OnClickListener listener, View... views) {
        if (!isNull(views)) {
            for (View v : views) {
                if (v != null) {
                    v.setOnClickListener(listener);
                }
            }
        }
    }

    public static void setOnClickListener(View view, View.OnClickListener onClickListener) {
        try {
            if (isNotNull(view)) {
                view.setOnClickListener(onClickListener);
            }
        } catch (Throwable throwable) {
//            Ln.e(throwable);
        }
    }

    public static void setOnLongClickListener(View view, View.OnLongClickListener onLongClickListener) {
        try {
            if (isNotNull(view)) {
                view.setOnLongClickListener(onLongClickListener);
            }
        } catch (Throwable throwable) {
//            Ln.e(throwable);
        }
    }

    public static void setSelect(@NonNull View v, boolean selected) {
        if (!isNull(v)) {
            v.setSelected(selected);
        }
    }

    public static void removeChild(@NonNull View child) {
        if (!isNull(child)) {
            ViewGroup parent = (ViewGroup) child.getParent();
            if (parent != null) {
                parent.removeView(child);
            }
        }
    }

    //测量此View所占空间
//    public static void measureView(View v) {
//        ViewGroup.LayoutParams lp = v.getLayoutParams();
//        if (lp == null) {
//            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
//        int width = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
//        int height = 0;
//        int tempHeight = lp.height;
//        if (tempHeight > 0) {
//            height = View.MeasureSpec.makeMeasureSpec(tempHeight, View.MeasureSpec.EXACTLY);
//        } else {
//            height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        }
//        v.measure(width, height);
//    }


    public static boolean measureView(View view) {
        if (!isNull(view)) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp == null) {
                lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            int widthMeasureSpec = View.MeasureSpec.UNSPECIFIED, heightMeasureSpec = View.MeasureSpec.UNSPECIFIED;
            if (lp.width > 0) {
                widthMeasureSpec = View.MeasureSpec.EXACTLY;
            }
            if (lp.height > 0) {
                heightMeasureSpec = View.MeasureSpec.EXACTLY;
            }
            widthMeasureSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
            heightMeasureSpec = ViewGroup.getChildMeasureSpec(heightMeasureSpec, 0, lp.height);
            view.measure(widthMeasureSpec, heightMeasureSpec);
            return true;
        }
        return false;
    }

    public static int getMeasuredHeight(View view) {
        if (measureView(view)) {
            return view.getMeasuredHeight();
        }
        return 0;
    }

    public static int getMeasuredWidth(View view) {
        if (measureView(view)) {
            return view.getMeasuredWidth();
        }
        return 0;
    }


    public static void setViewWidth(View view, int width, boolean withLayout) {
        setViewSize(view, width, 0, withLayout, 1);
    }

    public static void setViewHeight(View view, int height, boolean withLayout) {
        setViewSize(view, 0, height, withLayout, 2);
    }

    public static void setViewSize(View view, int width, int height, boolean withLayout) {
        setViewSize(view, width, height, withLayout, 3);
    }

    //-------------------------
    private static void setViewSize(View view, int width, int height
            , boolean withLayout, int action) {
        if (!isNull(view)) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (!isNull(layoutParams)) {
                switch (action) {
                    case 1://setWidth
                        layoutParams.width = width;
                        break;
                    case 2://setHeight
                        layoutParams.height = height;
                        break;
                    case 3://both
                        layoutParams.width = width;
                        layoutParams.height = height;
                        break;
                    default:
                        return;
                }
                if (withLayout) {
                    view.setLayoutParams(layoutParams);
                }
            }
        }
    }

    private static void removeListeners(Object animator) {
        if (!isNull(animator)) {
            if (animator instanceof ViewPropertyAnimatorCompat) {
                ((ViewPropertyAnimatorCompat) animator).setUpdateListener(null);
                ((ViewPropertyAnimatorCompat) animator).setListener(null);
            } else if (animator instanceof ValueAnimator) {
                ((ValueAnimator) animator).removeAllListeners();
                ((ValueAnimator) animator).removeAllUpdateListeners();
            } else if (animator instanceof Animator) {
                ((Animator) animator).removeAllListeners();
            }
        }
    }

    public static void setScaleY(View view, float scaleY) {
        if (isNull(view)) return;
        view.setScaleY(scaleY);
    }

    public static void setAlpha(View view, float alpha) {
        if (isNull(view)) return;
        view.setAlpha(alpha);
    }

//    public static void animateScaleY(View view, int duration, float scaleY) {
//        if (isNull(view)) return;
//        ViewCompat.animate(view)
//                .scaleY(scaleY)
//                .setDuration(duration)
//                .setInterpolator(Interpolators.FOSI)
//                .start();
//    }
//
//    public static void animateRotation(View view, int duration, float rotation) {
//        if (isNull(view)) return;
//        ViewCompat.animate(view)
//                .rotation(rotation)
//                .setDuration(duration)
//                .setInterpolator(Interpolators.FOSI)
//                .start();
//    }

    public static void animateRotationAlpha(float alpha, View... views) {
        if (views == null || views.length == 0) {
            return;
        }
        for (View view : views) {
            if (isNull(view)) {
                continue;
            }
            ViewCompat.animate(view)
                    .alpha(alpha)
                    .start();
        }
    }

    public static  <T extends View> T viewStubInflate(ViewStub viewStub) {
        if (viewStub == null) {
            return null;
        }
        return (T) viewStub.inflate();
    }
}
