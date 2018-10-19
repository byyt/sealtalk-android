package cn.yunchuang.im.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.yunchuang.im.R;
import cn.yunchuang.im.pulltorefresh.recyclerview.base.BaseHolder;
import cn.yunchuang.im.pulltorefresh.recyclerview.base.MRecyclerViewAdapter;
import cn.yunchuang.im.server.response.HomepageModel;

/**
 * 放松入口
 * Created by mulinrui on 2017/10/12.
 */
public class HomepageAdapter extends MRecyclerViewAdapter<HomepageModel, HomepageAdapter.HomepageHolder> {


    public HomepageAdapter(Context context, List items) {
        super(context, items);
    }

    @Override
    protected int getItemType(int position) {
        return VIEW_TYPE_ITEM;
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    protected HomepageHolder createItemHolder(ViewGroup parent, int viewType) {
        View view = getInflater().inflate(R.layout.item_homepage_adapter, parent, false);
        return new HomepageHolder(view, getContext());
    }

    @Override
    protected void bindItemHolder(HomepageHolder holder, int position, int viewType) {
        HomepageModel homepageModel = getItem(position);
        if (homepageModel != null) {
            holder.mNickName.setText(homepageModel.getNickname());
        }
    }


    public class HomepageHolder extends BaseHolder<HomepageHolder> {

        private ImageView mPortraitImg;
        private TextView mNickName;


        public HomepageHolder(View itemView, Context context) {
            super(itemView, context);
            mPortraitImg = (ImageView) itemView.findViewById(R.id.homepage_adapter_item_portrait);
            mNickName = (TextView) itemView.findViewById(R.id.homepage_adabpter_item_nickname);

        }
    }

}
