package cn.yunchuang.im.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.yunchuang.im.R;
import cn.yunchuang.im.pulltorefresh.recyclerview.base.BaseHolder;
import cn.yunchuang.im.pulltorefresh.recyclerview.base.MRecyclerViewAdapter;
import cn.yunchuang.im.server.response.HomepageModel;
import cn.yunchuang.im.ui.activity.LoginActivity_Code_PassWord;
import cn.yunchuang.im.ui.activity.MainActivity;
import cn.yunchuang.im.ui.activity.UserDetailActivity_New;
import io.rong.imkit.RongIM;

/**
 * 放松入口
 * Created by mulinrui on 2017/10/12.
 */
public class HomepageAdapter extends MRecyclerViewAdapter<HomepageModel, HomepageAdapter.HomepageHolder> {

    private Context mContext;

    public HomepageAdapter(Context context, List items) {
        super(context, items);
        mContext = context;
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
    protected void bindItemHolder(final HomepageHolder holder, int position, int viewType) {
        final HomepageModel homepageModel = getItem(position);
        if (homepageModel != null) {
            holder.mNickName.setText(homepageModel.getNickname());
            holder.mRootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    String displayName = holder.mNickName.getText().toString();
//                    if (!TextUtils.isEmpty(displayName)) {
//                        RongIM.getInstance().startPrivateChat(mContext, homepageModel.getId(), displayName);
//                    } else {
//                        RongIM.getInstance().startPrivateChat(mContext, homepageModel.getId(), homepageModel.getNickname());
//                    }
                    Intent intent = new Intent(mContext, UserDetailActivity_New.class);
                    intent.putExtra("userId",homepageModel.getId());
                    mContext.startActivity(intent);

                }
            });
        }
    }


    public class HomepageHolder extends BaseHolder<HomepageHolder> {

        private LinearLayout mRootLayout;
        private ImageView mPortraitImg;
        private TextView mNickName;


        public HomepageHolder(View view, Context context) {
            super(view, context);
            mRootLayout = (LinearLayout) view.findViewById(R.id.homepage_adabpter_item_root_layout);
            mPortraitImg = (ImageView) view.findViewById(R.id.homepage_adapter_item_portrait);
            mNickName = (TextView) view.findViewById(R.id.homepage_adabpter_item_nickname);

        }
    }

}
