package cn.yunchuang.im.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.yunchuang.im.R;
import cn.yunchuang.im.server.BaseAction;
import cn.yunchuang.im.server.response.HomepageModel;
import cn.yunchuang.im.ui.activity.UserDetailActivity_New;

/**
 * 放松入口
 * Created by mulinrui on 2017/10/12.
 */
public class HomepageAdapter_New extends BaseQuickAdapter<HomepageModel, BaseViewHolder> {

    public static final String TAG = "UserDetailPicListAdapter";
    private Context mContext;

    public HomepageAdapter_New(Context context) {
        super(R.layout.item_homepage_adapter);
        this.mContext = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, final HomepageModel item) {
        LinearLayout rootLayout = helper.getView(R.id.homepage_adabpter_item_root_layout);
        ImageView portraitImg = helper.getView(R.id.homepage_adapter_item_portrait);
        TextView nickName = helper.getView(R.id.homepage_adabpter_item_nickname);
        ImageView imageView1 = helper.getView(R.id.homepage_adapter_item_tu_pian_one);
        ImageView imageView2 = helper.getView(R.id.homepage_adapter_item_tu_pian_two);
        ImageView imageView3 = helper.getView(R.id.homepage_adapter_item_tu_pian_three);

        //Glide 加载图片简单用法
        RequestOptions optionsPortrait = RequestOptions.bitmapTransform(new CircleCrop());
        optionsPortrait.placeholder(R.drawable.banner_moren2).error(R.drawable.banner_moren2).centerCrop();

        Glide.with(mContext)
                .load(BaseAction.DOMAIN_PIC+"/renwu1.png")
                .apply(optionsPortrait)
                .into(portraitImg);

        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.banner_moren2).error(R.drawable.banner_moren2).centerCrop();

        Glide.with(mContext)
                .load(BaseAction.DOMAIN_PIC+"/renwu1.png")
                .apply(options)
                .into(imageView1);
        Glide.with(mContext)
                .load(BaseAction.DOMAIN_PIC+"/renwu1.png")
                .apply(options)
                .into(imageView2);
        Glide.with(mContext)
                .load(BaseAction.DOMAIN_PIC+"/renwu1.png")
                .apply(options)
                .into(imageView3);

        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserDetailActivity_New.class);
                intent.putExtra("userId", item.getId());
                mContext.startActivity(intent);
            }
        });
        nickName.setText(item.getNickname());
    }


}
