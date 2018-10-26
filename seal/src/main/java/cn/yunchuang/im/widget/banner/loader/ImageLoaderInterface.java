package cn.yunchuang.im.widget.banner.loader;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.io.Serializable;


public interface ImageLoaderInterface<T extends View> extends Serializable {

    void displayImage(FragmentActivity context, T view);

    void OnAdExposure(FragmentActivity context, View view);
    T createImageView(FragmentActivity context);
}
