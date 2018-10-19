/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package cn.yunchuang.im.pulltorefresh.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cn.yunchuang.im.R;

import java.util.Calendar;

public class PullToRefreshNestedScrollView extends PullToRefreshBase<NestedScrollView> {
    private int lastY = 0;
    private int touchEventId = -9983761;
    private int firstVisibleItem = 0;
    private int lastVisibleItem = 0;
    private boolean isAd = false;//判断是否有广告
    private int scrollY = 0;
    int lineCount = 0;
    int lastLineCount = 0;

    public void setAd(boolean ad) {
        isAd = ad;
        if (isAd) {
            lastVisibleItem = 8;
        } else {
            lastVisibleItem = 12;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            View scroller = (View) msg.obj;

            if (msg.what == touchEventId) {
                if (lastY == scroller.getScrollY()) {
                    try {
                        if (isAd) {//判斷是否有廣告 有
                            Log.i("scrool", "-lastY-" + lastY + "--scrollY--" + scrollY);
                            if (lastY >= 200) {//滑动出广告的距离
                                calculateVisibleValue(200);
                            } else {
                                firstVisibleItem = 0;
                                lastVisibleItem = 8;
                            }
                            scrollY = lastY;
                            lastLineCount = lineCount;
                        } else {//沒有廣告
                            calculateVisibleValue(0);
                            scrollY = lastY;
                            lastLineCount = lineCount;
                        }

                        Log.e("scrool", lastY + "---firstVisibleItem---" + firstVisibleItem + "--lastVisibleItem---:" + lastVisibleItem + "--lineCount---:" + lineCount);
                        scrollViewListener.onScroll(lineCount, firstVisibleItem, lastVisibleItem);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    handler.sendMessageDelayed(handler.obtainMessage(touchEventId, scroller), 1);
                    lastY = scroller.getScrollY();
                }
            }
        }
    };

    /**
     * 计算visible值
     *
     * @param adHeight
     */
    private void calculateVisibleValue(int adHeight) {
        if (lastY > scrollY) {//向上滑  增加距离
            lineCount = (lastY - adHeight) / currentHeight;
            firstVisibleItem = 0 + lineCount * columnCount;
            lastVisibleItem = 12 + lineCount * columnCount;
        } else {//距离减小
            lineCount = (lastY - adHeight) / currentHeight;
            firstVisibleItem = firstVisibleItem - (lastLineCount - lineCount) * columnCount;
            lastVisibleItem = lastVisibleItem - (lastLineCount - lineCount) * columnCount;
        }
    }


    private ScrollViewListener scrollViewListener = null;

    public PullToRefreshNestedScrollView(Context context) {
        super(context);
    }

    public PullToRefreshNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshNestedScrollView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshNestedScrollView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected NestedScrollView createRefreshableView(Context context, AttributeSet attrs) {
        NestedScrollView scrollView;
        if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
            scrollView = new InternalScrollViewSDK9(context, attrs);
        } else {
            scrollView = new NestedScrollView(context, attrs);
        }

        scrollView.setId(R.id.scrollview);
        return scrollView;
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected boolean isReadyForPullStart() {
        return mRefreshableView.getScrollY() == 0;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        View scrollViewChild = mRefreshableView.getChildAt(0);
        if (null != scrollViewChild) {
            return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
        }
        return false;
    }

    private int currentHeight;

    public void setItemHeight(int currentHeight) {
        this.currentHeight = currentHeight;
    }

    //返回列数
    int columnCount;

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    @TargetApi(9)
    final class InternalScrollViewSDK9 extends NestedScrollView {
        public static final int MIN_CLICK_DELAY_TIME = 500;
        private long lastClickTime = 0;

        public InternalScrollViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);
            if (scrollViewListener != null)
                scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                handler.sendMessageDelayed(handler.obtainMessage(touchEventId, this), 5);
            }
            return super.onTouchEvent(ev);
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                       int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

            final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                    scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
            OverscrollHelper.overScrollBy(PullToRefreshNestedScrollView.this, deltaX, scrollX, deltaY, scrollY,
                    getScrollRange(), isTouchEvent);
            if (scrollViewListener != null) {
//                scrollViewListener.onScrollChanged(this, deltaX, deltaY, scrollX, scrollY);
                if (deltaY > 0) {
                    if (scrollY + 70 >= getScrollRange()) {
                        long currentTime = Calendar.getInstance().getTimeInMillis();
                        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                            lastClickTime = currentTime;
                            scrollViewListener.onScrollBottom();
                        }
                    }
                }
            }
//            Log.e("tatatata",deltaX+"==="+scrollX+"==="+deltaY+"==="+ scrollY);
            return returnValue;
        }

        /**
         * Taken from the AOSP ScrollView source
         */
        private int getScrollRange() {
            int scrollRange = 0;
            if (getChildCount() > 0) {
                View child = getChildAt(0);
                scrollRange = Math.max(0, child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
            }
            return scrollRange;
        }
    }

    // 接口
    public interface ScrollViewListener {
        void onScrollChanged(View scrollView, int x, int y, int oldx, int oldy);

        void onScrollBottom();

        void onScroll(int coll, int firstVisibleItem, int visibleItemCount);

    }
}
