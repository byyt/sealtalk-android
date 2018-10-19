package cn.yunchuang.im.pulltorefresh.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * recyclerView的分割线处理 GridLayoutManager或者LinearLayoutManager才能使用
 * Created by mulinrui on 2017/10/18.
 */
public class RecycleViewDivider extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private Drawable mDivider;
    private int mDividerHeight = 2;//分割线高度，默认为1px
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    /**
     * 默认分割线：高度为2px，颜色为灰色
     *
     * @param context
     */
    public RecycleViewDivider(Context context) {

        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param drawableId 分割线图片
     */
    public RecycleViewDivider(Context context, int drawableId) {
        this(context);
        mDivider = ContextCompat.getDrawable(context, drawableId);
        mDividerHeight = mDivider.getIntrinsicHeight();
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public RecycleViewDivider(Context context, int dividerHeight, int dividerColor) {
        this(context);
        mDividerHeight = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }


    //获取分割线尺寸
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            int orientation = layoutManager.getOrientation();

            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                //绘制网格分割线
                if (gridLayoutManager.getSpanCount() > 1) {
                    outRect.set(0, 0, mDividerHeight, mDividerHeight);
                } else {
                    //否则属于单列 水平 或者 垂直
                    if (orientation == LinearLayoutManager.VERTICAL) {
                        outRect.set(0, 0, 0, mDividerHeight);
                    } else if (orientation == LinearLayoutManager.HORIZONTAL) {
                        outRect.set(0, 0, mDividerHeight, 0);
                    }
                }
            }

        }
    }

    //绘制分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            int orientation = layoutManager.getOrientation();

            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                //绘制网格分割线
                if (gridLayoutManager.getSpanCount() > 1) {
                    drawHorizontalAndVertical(c, parent);
                }else {
                    //否则属于单列 水平 或者 垂直
                    if (orientation == LinearLayoutManager.VERTICAL) {
                        drawVertical(c, parent);
                    } else if (orientation == LinearLayoutManager.HORIZONTAL) {
                        drawHorizontal(c, parent);
                    }
                }
            }

        }
    }

    //绘制横向 item 分割线
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    //绘制纵向 item 分割线
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    //绘制横向 item 分割线 有问题 还没有实验效果
    private void drawHorizontalAndVertical(Canvas canvas, RecyclerView parent) {

        int childSize = parent.getChildCount();

        int top, bottom, left, right;

        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

            top = child.getBottom() + layoutParams.bottomMargin;
            bottom = top + mDividerHeight;

            left = child.getRight() + layoutParams.rightMargin;
            right = left + mDividerHeight;

            if (mDivider != null) {
                //Horizontal
                mDivider.setBounds(left, child.getTop() + layoutParams.topMargin, right, child.getBottom() + layoutParams.bottomMargin + mDividerHeight);
                mDivider.draw(canvas);
                if (mPaint != null) {
                    canvas.drawRect(left, child.getTop() + layoutParams.topMargin, right, child.getBottom() + layoutParams.bottomMargin + mDividerHeight, mPaint);
                }
                //Vertical
                mDivider.setBounds(child.getLeft() + layoutParams.leftMargin, top, child.getRight() + layoutParams.rightMargin + mDividerHeight, bottom);
                mDivider.draw(canvas);
                if (mPaint != null) {
                    canvas.drawRect(child.getLeft() + layoutParams.leftMargin, top, child.getRight() + layoutParams.rightMargin + mDividerHeight, bottom, mPaint);
                }
            }


        }
    }
}