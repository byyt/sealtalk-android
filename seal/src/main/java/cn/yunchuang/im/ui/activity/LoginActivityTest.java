package cn.yunchuang.im.ui.activity;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.yunchuang.im.R;

public class LoginActivityTest extends Activity {
    private TextView res;
    private LinearLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test2);
        initialize();
        addLayoutListener(main, res);
    }

    private void initialize() {
        res = (TextView) findViewById(R.id.activity_login_sure);
        main = (LinearLayout) findViewById(R.id.activity_login_root_layout);
    }

    /**
     * 1、获取main在窗体的可视区域 * 2、获取main在窗体的不可视区域高度 * 3、判断不可视区域高度 * 1、大于100:键盘显示 获取Scroll的窗体坐标 * 算出main需要滚动的高度,使scroll显示。 * 2、小于100:键盘隐藏 * * @param main 根布局 * @param scroll 需要显示的最下方View
     */
    public void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                main.getWindowVisibleDisplayFrame(rect);
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                if (mainInvisibleHeight > 100) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    main.scrollTo(0, srollHeight);
                } else {
                    main.scrollTo(0, 0);
                }
            }
        });
    }
}
