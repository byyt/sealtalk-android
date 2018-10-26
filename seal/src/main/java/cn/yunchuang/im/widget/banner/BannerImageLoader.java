package cn.yunchuang.im.widget.banner;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.yunchuang.im.R;
import cn.yunchuang.im.utils.CommonUtils;
import cn.yunchuang.im.widget.banner.loader.ImageLoader;

/**
 * package_name: com.v.zy.mobile.view.loader
 * author：jack-lu on 17/2/4 10:50
 * class_name: BannerImageLoader
 */
public class BannerImageLoader extends ImageLoader {

    @Override
    public void displayImage(final FragmentActivity context, View view) {
        Object path = view.getTag();
        ImageView imageView = (ImageView) view.findViewById(R.id.iv);
        ImageView ad_logo = (ImageView) view.findViewById(R.id.ad_logo);
        String url = "";
//        if (path instanceof Banners) {
//            url = ((Banners) path).getBannerLargePixels();
//            MyBitmapUtils.displayMfsOSSImage(context, imageView, url, R.drawable.banner_mr, R.drawable.banner_mr,getBitmapLoadCallBack(context, view));
//            ad_logo.setVisibility(View.GONE);
//        }

        if (CommonUtils.isOnMainThread()) {

            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.banner_moren).error(R.drawable.banner_moren);

            Glide.with(context)
                    .load("http://192.168.1.236:8081/aaa.jpg")
                    .apply(options)
                    .into(imageView);
        }

    }

    @Override
    public void OnAdExposure(FragmentActivity context, View view) {
        Object tagObject = view.getTag();

    }
}
