package cn.yunchuang.im.pulltorefresh.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import cn.yunchuang.im.pulltorefresh.recyclerview.base.MRecyclerViewAdapter;
import cn.yunchuang.im.R;


/**
 * MRecyclerView 基类
 * Created by mulinrui on 2017/10/11.
 */
public class MRecyclerView extends RecyclerView {

    // ==========================================================================
    // Constants
    // ==========================================================================
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private final int default_spanCount = 1;

    // ==========================================================================
    // Fields
    // ==========================================================================
    /**
     * 方向
     */
    private int mOrientation;
    /**
     * 布局管理
     */
    protected GridLayoutManager layoutManager;
    /**
     * 列数
     */
    private int mSpanCount;

    private View emptyView;

    // ==========================================================================
    // Constructors
    // ==========================================================================

    public MRecyclerView(Context context) {
        this(context, null);
    }

    public MRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置默认值
        mOrientation = VERTICAL;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MRecyclerView);
            mOrientation = a.getInteger(R.styleable.MRecyclerView_ORIENTATION, VERTICAL);
            a.recycle();
        }
        initRecyclerView();
    }

    // ==========================================================================
    // Getters
    // ==========================================================================
    public int getOrientation() {
        return mOrientation;
    }

    @Override
    public GridLayoutManager getLayoutManager() {
        return layoutManager;
    }


// ==========================================================================
    // Setters
    // ==========================================================================

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("invalid orientation:" + orientation);
        }
        if (mOrientation == orientation) {
            return;
        }

        mOrientation = orientation;
        layoutManager.setOrientation(mOrientation);
    }

    private void setSpanCount(int spanCount) {
        if (spanCount > default_spanCount) {
            mSpanCount = spanCount;
            layoutManager.setSpanCount(spanCount);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        //如果是垂直方向 且是MRecyclerViewAdapter则设置SpanSizeLookup监听
        if (mOrientation == VERTICAL && adapter instanceof MRecyclerViewAdapter) {
            setISpanSizeLookup((MRecyclerViewAdapter) adapter);
        }
        if (adapter instanceof ISpanSizeLookup) {
            setSpanCount(((ISpanSizeLookup) adapter).getSpanCount());
        }

        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }


    private void setISpanSizeLookup(final ISpanSizeLookup l) {
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (l != null) {
                    int spanSize = l.getSpanSize2(position);
                    if (spanSize > mSpanCount) {
                        spanSize = mSpanCount;
                    }
                    return spanSize;
                }
                return 1;
            }
        });
    }

    // ==========================================================================
    // Methods
    // ==========================================================================
    private void initRecyclerView() {
        // 设置RecyclerView的布局管理
        mSpanCount = default_spanCount;
        layoutManager = new TopSnapLayoutManager(getContext(), mSpanCount, mOrientation, false);
        setLayoutManager(layoutManager);

    }


    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    private void checkIfEmpty() {
        if (emptyView != null && getAdapter() != null) {
            final boolean emptyViewVisible =
                    getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
            setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
        }
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }


    // ==========================================================================
    // Inner/Nested Classes
    // ==========================================================================

    public class TopSnapLayoutManager extends GridLayoutManager {

        public TopSnapLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
        }

        public TopSnapLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
            super(context, spanCount, orientation, reverseLayout);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            TopSnapSmoothScroller testSmoothScroller =
                    new TopSnapSmoothScroller(recyclerView.getContext());
            testSmoothScroller.setTargetPosition(position);
            startSmoothScroll(testSmoothScroller);
        }

        public class TopSnapSmoothScroller extends LinearSmoothScroller {
            public TopSnapSmoothScroller(Context context) {
                super(context);
            }

            /**
             * When scrolling towards a child view, this method defines we should align
             * child view's left with the parent RecyclerView's left.
             *
             * @return SNAP_TO_START
             * @see #SNAP_TO_START
             * @see #SNAP_TO_END
             * @see #SNAP_TO_ANY
             */
            @Override
            protected int getHorizontalSnapPreference() {
                return SNAP_TO_START;
            }

            /**
             * When scrolling towards a child view, this method defines we should align
             * child view's top with the parent RecyclerView's top.
             *
             * @return SNAP_TO_START
             * @see #SNAP_TO_START
             * @see #SNAP_TO_END
             * @see #SNAP_TO_ANY
             */
            @Override
            protected int getVerticalSnapPreference() {
                return SNAP_TO_START;
            }
        }
    }

}
