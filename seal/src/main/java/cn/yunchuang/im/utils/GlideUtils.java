package cn.yunchuang.im.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.yunchuang.im.R;
import cn.yunchuang.im.server.BaseAction;

/**
 * Created by zhou_yuntao on 2019/3/31.
 */

public class GlideUtils {
    public static void load(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.ic_image_zhanwei)
                .error(R.drawable.ic_image_zhanwei).centerCrop();
        Glide.with(context)
                .load(BaseAction.DOMAIN_PIC + "/" + url)
                .apply(options)
                .into(imageView);
    }
}
