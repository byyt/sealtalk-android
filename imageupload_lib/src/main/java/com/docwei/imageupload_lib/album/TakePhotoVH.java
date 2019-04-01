package com.docwei.imageupload_lib.album;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.docwei.imageupload_lib.R;
import com.docwei.imageupload_lib.dialog.DialogPlus;
import com.docwei.imageupload_lib.dialog.Holder;


/**
 * Created by wk on 2018/4/13.
 * 用户点击选择弹窗，是拍照、相册、取消
 * 跟我们的Activity是同生命周期的
 */

public class TakePhotoVH implements Holder {
    public ICameraAndPhotoListener mICameraAndPhotoListener;
    private Activity mContext;
    private View mContentContainer;

    private boolean showDelete = false;//加一个变量，如果传来的是头像，则不显示删除照片选项

    public TakePhotoVH(Activity context, boolean showDelete) {
        mContext = context;
        this.showDelete = showDelete;
    }

    @Override
    public View getInflatedView(LayoutInflater inflater, DialogPlus dialogPlus) {
        mContentContainer = inflater.inflate(R.layout.dialog_photo_camera_select, null);
        init(mContentContainer, dialogPlus);
        return mContentContainer;
    }

    private void init(View view, final DialogPlus dialogPlus) {
        final TextView tv_photo = view.findViewById(R.id.select_photo);
        TextView tv_camera = view.findViewById(R.id.select_camera);
        TextView tv_delete = view.findViewById(R.id.delte_photo);
        TextView tv_cancel = view.findViewById(R.id.select_cancel);
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mICameraAndPhotoListener != null) {
                    mICameraAndPhotoListener.takePhoto();
                }
                tv_photo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogPlus.dismiss();
                    }
                }, 300);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里修改过了，将mContext.finish();放到dismiss函数里
                dialogPlus.dismiss(mContext);
            }
        });
        tv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mICameraAndPhotoListener != null) {
                    mICameraAndPhotoListener.selectAlbum();
                }
                tv_photo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogPlus.dismiss();
                    }
                }, 300);
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mICameraAndPhotoListener != null) {
                    mICameraAndPhotoListener.deletePhoto();
                }
                tv_photo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogPlus.dismiss();
                    }
                }, 300);
            }
        });
        if (showDelete) {
            tv_delete.setVisibility(View.VISIBLE);//点击的是普通照片，显示删除按钮
        } else {
            tv_delete.setVisibility(View.GONE);//点击的是头像或者添加按钮，不显示删除按钮
        }
    }

    public void setCameraAndPhotoListener(ICameraAndPhotoListener iCameraAndPhotoListener) {
        mICameraAndPhotoListener = iCameraAndPhotoListener;
    }

    public interface ICameraAndPhotoListener {
        void takePhoto();

        void selectAlbum();

        void deletePhoto();
    }


}
