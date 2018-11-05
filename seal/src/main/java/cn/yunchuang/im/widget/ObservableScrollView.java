package cn.yunchuang.im.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

/**
 * Created by jack-lu on 16/11/26.
 */
public class ObservableScrollView extends HorizontalScrollView {

    private ScrollViewListener scrollViewListener = null;
    int width;
    int allWidth;
    private int touchEventId = -9983761;
    private int firstVisibleItem = 0;
    private int lastVisibleItem = 0;
    int lastX;
    Context context;

    public ObservableScrollView(Context context) {
        this(context, null);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ObservableScrollView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setItemWidth(Context context, int width) {
        this.context = context;
        this.width = width;
        getAllWdith();
    }

    public void getAllWdith() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        allWidth = wm.getDefaultDisplay().getWidth();
    }


    public void setOnScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            lastX = x;
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == touchEventId) {
                if (context != null && width > 0) {
                    int fristItem = lastX / width;
                    int midle = allWidth / width;
                    int lastItem = fristItem + midle;
                    scrollViewListener.onScrollItemChanged(fristItem, lastItem);
                }

            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            handler.sendMessageDelayed(handler.obtainMessage(touchEventId, this), 5);
        }
        return super.onTouchEvent(ev);
    }


    int mLastX, mLastY;
    private ViewGroup mTouchInterceptionViewGroup;

    public void setTouchInterceptionViewGroup(ViewGroup viewGroup) {
        mTouchInterceptionViewGroup = viewGroup;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (mTouchInterceptionViewGroup != null) {
            //获得当前的位置坐标
            int x = (int) ev.getX();
            int y = (int) ev.getY();

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    //通知父容器不要拦截事件
                    mTouchInterceptionViewGroup.requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int deltaX = x - mLastX;
                    int deltaY = y - mLastY;
                    boolean isHorizontalScroll = (Math.abs(deltaX) >= Math.abs(deltaY));
                    if (!isHorizontalScroll) {
                        //通知父容器拦截此事件
                        mTouchInterceptionViewGroup.requestDisallowInterceptTouchEvent(false);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }

            //重置手指的初始位置
            mLastY = y;
            mLastX = x;
        }

        return super.dispatchTouchEvent(ev);
    }


    public interface ScrollViewListener {
        void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

        void onScrollItemChanged(int frist, int last);
    }
}