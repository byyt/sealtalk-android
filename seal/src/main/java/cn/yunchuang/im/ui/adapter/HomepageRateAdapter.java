package cn.yunchuang.im.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.itheima.roundedimageview.RoundedImageView;

import java.text.MessageFormat;
import java.util.List;

import cn.yunchuang.im.R;
import cn.yunchuang.im.server.BaseAction;
import cn.yunchuang.im.server.response.HomepageModel;
import cn.yunchuang.im.server.response.ImageModel;
import cn.yunchuang.im.ui.activity.UserDetailActivity_New;
import cn.yunchuang.im.ui.activity.YueTaXmxzActivity;
import cn.yunchuang.im.ui.widget.AgeSexView;
import cn.yunchuang.im.zmico.utils.Utils;

/**
 * 放松入口
 * Created by mulinrui on 2017/10/12.
 */
public class HomepageRateAdapter extends BaseQuickAdapter<HomepageModel, BaseViewHolder> {

    public static final String TAG = "UserDetailPicListAdapter";
    private Context mContext;

    public HomepageRateAdapter(Context context) {
        super(R.layout.item_homepage_rate_adapter);
        this.mContext = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, final HomepageModel item) {
        if (item == null) {
            return;
        }
        LinearLayout rootLayout = helper.getView(R.id.homepage_adabpter_item_root_layout);
        AgeSexView ageSexView = helper.getView(R.id.homepage_adapter_item_age_sex_view);
        RoundedImageView portraitImg = helper.getView(R.id.homepage_adapter_item_portrait);
        TextView nickName = helper.getView(R.id.homepage_adabpter_item_nickname);
        TextView rate = helper.getView(R.id.homepage_adapter_item_rate_tv);
        ImageView imageView1 = helper.getView(R.id.homepage_adapter_item_tu_pian_one);
        ImageView imageView2 = helper.getView(R.id.homepage_adapter_item_tu_pian_two);
        ImageView imageView3 = helper.getView(R.id.homepage_adapter_item_tu_pian_three);
        ImageView yueta = helper.getView(R.id.homepage_adapter_item_yue_ta_iv);
        ImageView sixinta = helper.getView(R.id.homepage_adapter_item_si_xin_ta_iv);
        ImageView dashangta = helper.getView(R.id.homepage_adapter_item_da_shang_ta_iv);
        ImageView shipinta = helper.getView(R.id.homepage_adapter_item_shi_pin_ta_iv);

        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserDetailActivity_New.class);
                intent.putExtra("userId", item.getId());
                mContext.startActivity(intent);
            }
        });
        yueta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFastClick()) {
                    return;
                }
                Intent intent = new Intent(mContext, YueTaXmxzActivity.class);
                intent.putExtra("userId", item.getId());
                mContext.startActivity(intent);
            }
        });
        sixinta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFastClick()) {
                    return;
                }

            }
        });
        dashangta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFastClick()) {
                    return;
                }

            }
        });
        shipinta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFastClick()) {
                    return;
                }

            }
        });

        nickName.setText(item.getNickname());
        ageSexView.setAgeAndSex(item.getAge(), item.getSex());
        rate.setText(MessageFormat.format("{0}{1}{2}", "好评率：", item.getFeedback_rate(), "%"));

        //Glide 加载图片简单用法
//        RequestOptions optionsPortrait = RequestOptions.bitmapTransform(new CircleCrop());
        RequestOptions optionsPortrait = new RequestOptions();
        optionsPortrait.placeholder(R.drawable.ic_image_zhanwei).error(R.drawable.ic_image_zhanwei).centerCrop();

        Glide.with(mContext)
                .load(BaseAction.DOMAIN_PIC + "/" + item.getPortraitUri())
                .apply(optionsPortrait)
                .into(portraitImg);

        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.ic_image_zhanwei).error(R.drawable.ic_image_zhanwei).centerCrop();

        //暂时写死三张图，后面用一个linearLayout，根据图片多少来动态添加addview，imagevie
        //而且imageview宽高动态计算，
        List<ImageModel> freeImgList = JSONArray.parseArray(item.getFreeImgList(), ImageModel.class);
        if (freeImgList == null) {
            return;
        }
        if (freeImgList.size() < 3) {
            return;
        }

        Glide.with(mContext)
                .load(BaseAction.DOMAIN_PIC + "/" + freeImgList.get(0).getImgUrl())
                .apply(options)
                .into(imageView1);
        Glide.with(mContext)
                .load(BaseAction.DOMAIN_PIC + "/" + freeImgList.get(1).getImgUrl())
                .apply(options)
                .into(imageView2);
        Glide.with(mContext)
                .load(BaseAction.DOMAIN_PIC + "/" + freeImgList.get(2).getImgUrl())
                .apply(options)
                .into(imageView3);

    }

}
