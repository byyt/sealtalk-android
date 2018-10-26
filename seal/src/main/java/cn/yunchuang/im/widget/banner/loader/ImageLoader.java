package cn.yunchuang.im.widget.banner.loader;

import android.support.v4.app.FragmentActivity;
import android.view.View;


public abstract class ImageLoader implements ImageLoaderInterface<View> {

    @Override
    public View createImageView(FragmentActivity context) {
        View imageView = new View(context);
        return imageView;
    }

}
