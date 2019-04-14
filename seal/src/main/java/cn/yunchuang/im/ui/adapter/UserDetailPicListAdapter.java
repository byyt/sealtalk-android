package cn.yunchuang.im.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.itheima.roundedimageview.RoundedImageView;

import cn.yunchuang.im.R;
import cn.yunchuang.im.model.UserViewInfo;
import cn.yunchuang.im.server.BaseAction;
import cn.yunchuang.im.server.utils.StaticDataUtils;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by yangc on 2017/8/29.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public class UserDetailPicListAdapter extends BaseQuickAdapter<UserViewInfo, BaseViewHolder> {
    public static final String TAG = "UserDetailPicListAdapter";
    private Context context;

    public UserDetailPicListAdapter(Context context) {
        super(R.layout.view_user_detail_new_pic_item);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserViewInfo item) {
        final RoundedImageView thumbView = helper.getView(R.id.user_detail_new_pic_item_img);
        final FrameLayout lockLayout = helper.getView(R.id.user_detail_new_pic_item_lock_layout);

        RequestOptions options;
        if (StaticDataUtils.isInBlurImgUrlList(item.getUrl())) { //对图片进行模糊
            options = RequestOptions.bitmapTransform(new BlurTransformation(10, 2));//模糊效果，两个参数都是越大越模糊3
            lockLayout.setVisibility(View.VISIBLE);
        } else {
            options = new RequestOptions();//正常加载
            lockLayout.setVisibility(View.GONE);
        }

        options.placeholder(R.drawable.ic_image_zhanwei)
                .error(R.drawable.ic_image_zhanwei);

        Glide.with(context)
                .load(BaseAction.DOMAIN_PIC + "/" + item.getUrl())
                .apply(options)
                .into(thumbView);
        helper.getView(R.id.user_detail_new_pic_item_img).setTag(R.id.user_detail_new_pic_item_img, item.getUrl());
    }
}
