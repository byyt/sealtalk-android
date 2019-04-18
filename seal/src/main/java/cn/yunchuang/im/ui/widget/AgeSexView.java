package cn.yunchuang.im.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.yunchuang.im.App;
import cn.yunchuang.im.R;
import cn.yunchuang.im.zmico.utils.ResourceUtils;

/**
 * Created by zhou_yuntao on 2019/4/19.
 */

public class AgeSexView extends FrameLayout {

    private Context context;

    private TextView textView;

    public AgeSexView(Context context) {
        this(context, null);
    }

    public AgeSexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AgeSexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_age_sex_view, this);
        textView = view.findViewById(R.id.xb_nl_tv);

    }

    public void setAgeAndSex(int age, int sex) {
        setSexStyle(textView, age, sex);
    }

    private void setSexStyle(TextView xbnl, int age, int sex) {
        xbnl.setText(String.valueOf(age));
        if (sex == 1) {
            Drawable drawable = App.getAppContext().getResources().getDrawable(R.drawable.icon_woman);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            xbnl.setCompoundDrawables(drawable, null, null, null);
            xbnl.setTextColor(ResourceUtils.getColor(R.color.color_FC6880));
            xbnl.setBackground(ResourceUtils.getDrawable(R.drawable.ic_sex_woman_bg));
        } else {
            Drawable drawable = App.getAppContext().getResources().getDrawable(R.drawable.icon_man);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            xbnl.setCompoundDrawables(drawable, null, null, null);
            xbnl.setTextColor(ResourceUtils.getColor(R.color.color_65ACDE));
            xbnl.setBackground(ResourceUtils.getDrawable(R.drawable.ic_sex_man_bg));
        }
    }

}
