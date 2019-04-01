package cn.yunchuang.im.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.previewlibrary.loader.IZoomMediaLoader;
import com.previewlibrary.loader.MySimpleTarget;

import java.util.ArrayList;
import java.util.List;

import cn.yunchuang.im.R;
import cn.yunchuang.im.server.utils.StaticDataUtils;
import cn.yunchuang.im.utils.DialogUtils;
import cn.yunchuang.im.utils.FileUtils;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by yangc on 2017/9/4.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public class ZoomImageLoader implements IZoomMediaLoader {


    @Override
    public void displayImage(@NonNull final Fragment context, @NonNull final String path, ImageView imageView, @NonNull final MySimpleTarget simpleTarget) {

        RequestOptions options;
        if (StaticDataUtils.isInBlurImgUrlList(path)) { //对图片进行模糊
            options = RequestOptions.bitmapTransform(new BlurTransformation(25, 2));//模糊效果，两个参数都是越大越模糊3
        } else {
            options = new RequestOptions();//正常加载
        }
        options.error(R.drawable.ic_default_image);
//                .placeholder(R.drawable.ic_default_image);//不要加这句，加上之后每次进入大图会显示一下占位图影响体验

        Glide.with(context).asBitmap()
                .load(path)
                .apply(options)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        simpleTarget.onLoadFailed(null);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        simpleTarget.onResourceReady();
                        return false;
                    }
                })
                .into(imageView);

        //添加长按保存图片功能，挺重要
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showSaveImgDialog(context, path);
                return true;
            }
        });
    }

    @Override
    public void displayGifImage(@NonNull Fragment context, @NonNull String path, ImageView
            imageView, @NonNull final MySimpleTarget simpleTarget) {
        RequestOptions options = new RequestOptions();
        options.error(R.drawable.ic_default_image)
//                .placeholder(R.drawable.banner_moren2)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//可以解决gif比较几种时 ，加载过慢  //DiskCacheStrategy.NONE
                .dontAnimate();//去掉显示动画

        Glide.with(context).asGif()
                .load(path)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        simpleTarget.onLoadFailed(null);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        simpleTarget.onResourceReady();
                        return false;
                    }
                })
                .into(imageView);


//                .listener(new RequestListener<String, GifDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
//                        simpleTarget.onResourceReady();
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        simpleTarget.onLoadFailed(null);
//                        return false;
//                    }
//                })
//                .into(imageView);
    }

    @Override
    public void onStop(@NonNull Fragment context) {
        Glide.with(context).onStop();

    }

    @Override
    public void clearMemory(@NonNull Context c) {
        Glide.get(c).clearMemory();
    }

    private void showSaveImgDialog(final Fragment context, final String imgUrl) {
        if (context != null && context.getActivity() != null) {
            //弹出框震动一下
            Vibrator vibrator = (Vibrator) context.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(30);
            List<DialogUtils.PictureTypeEntity> entityList = new ArrayList<>();
            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        FileUtils.savePicToAlbum(context.getActivity(), imgUrl);
                    }
                }
            };
            entityList.add(new DialogUtils.PictureTypeEntity(0, "保存图片"));
            DialogUtils.showBottomList(context.getActivity(),
                    entityList, itemClickListener);

        }
    }

}
