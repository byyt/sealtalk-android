package cn.yunchuang.im.message.provider;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.yunchuang.im.R;
import cn.yunchuang.im.message.YhmsMessage;
import cn.yunchuang.im.utils.PushRecieveUtils;
import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;

/**
 * Created by Beyond on 2016/12/5.
 */

@ProviderTag(messageContent = YhmsMessage.class, showReadState = true)
public class YhmsMessageProvider extends IContainerItemProvider.MessageProvider<YhmsMessage> {
    private static final String TAG = "YhmsMessageProvider";

    private static class ViewHolder {
        TextView message;
        boolean longClick;
    }

    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_yhms_message, null);

        YhmsMessageProvider.ViewHolder holder = new YhmsMessageProvider.ViewHolder();
        holder.message = (TextView) view.findViewById(android.R.id.text1);
        view.setTag(holder);
        return view;
    }

    @Override
    public Spannable getContentSummary(YhmsMessage data) {
        return null;
    }

    @Override
    public Spannable getContentSummary(Context context, YhmsMessage data) {
        if (data == null)
            return null;

        String content = data.getContent();
        if (content != null) {
            if (content.length() > 100) {
                content = content.substring(0, 100);
            }
            return new SpannableString(AndroidEmoji.ensure(content));
        }
        return null;
    }

    @Override
    public void onItemClick(View view, int position, YhmsMessage content, UIMessage message) {

        String extra = content.getExtra();

//        Intent intent = new Intent("cn.yunchuang.im.ui.activity.WdyhDetailActivity");
//
//        Bundle bundle = new Bundle();
//        bundle.putString("wdyhOrderId", extra);
//        intent.putExtras(bundle);

        Intent intent = PushRecieveUtils.getIntent(content);

        view.getContext().startActivity(intent);
    }

    @Override
    public void onItemLongClick(final View view, int position, final YhmsMessage content, final UIMessage message) {


    }

    @Override
    public void bindView(final View v, int position, final YhmsMessage content, final UIMessage data) {
        YhmsMessageProvider.ViewHolder holder = (YhmsMessageProvider.ViewHolder) v.getTag();

        holder.message.setBackgroundResource(R.drawable.bg_common_white_8_radius);

        final TextView textView = holder.message;
        textView.setText(content.getContent());

    }
}
