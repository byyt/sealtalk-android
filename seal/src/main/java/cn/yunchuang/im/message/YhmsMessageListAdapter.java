package cn.yunchuang.im.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.yunchuang.im.R;
import cn.yunchuang.im.utils.DateUtils;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imkit.widget.ProviderContainerView;
import io.rong.imkit.widget.adapter.MessageListAdapter;

/**
 * Created by zhou_yuntao on 2019/5/22.
 */

public class YhmsMessageListAdapter extends MessageListAdapter {

    private LayoutInflater inflater;
    private Context context;

    public YhmsMessageListAdapter(Context context) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    protected void bindView(View v, int position, UIMessage data) {
        super.bindView(v, position, data);
        MessageListAdapter.ViewHolder holderNew = (MessageListAdapter.ViewHolder) v.getTag();
        holderNew.nameView.setVisibility(View.GONE);
        holderNew.leftIconView.setVisibility(View.GONE);
        holderNew.rightIconView.setVisibility(View.GONE);

        //修改时间样式，并且每个推送上面都显示时间
        holderNew.time.setVisibility(View.VISIBLE);
        String time = DateUtils.getYMDHM(data.getSentTime() / 1000);
        holderNew.time.setText(time);

        //最后一条设置底部边距
        if (position == getCount() - 1) {
            holderNew.contentView.setPadding(0, 0, 0, DeviceUtils.dpToPx(15));
        } else {
            holderNew.contentView.setPadding(0, 0, 0, 0);
        }

    }

    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        View result = inflater.inflate(R.layout.rc_item_message_system, (ViewGroup) null);
        MessageListAdapter.ViewHolder holder = new MyViewHolder();
        holder.leftIconView = (AsyncImageView) this.findViewById(result, R.id.rc_left);
        holder.rightIconView = (AsyncImageView) this.findViewById(result, R.id.rc_right);
        holder.nameView = (TextView) this.findViewById(result, R.id.rc_title);
        holder.contentView = (ProviderContainerView) this.findViewById(result, R.id.rc_content);
        holder.layout = (ViewGroup) this.findViewById(result, R.id.rc_layout);
        holder.progressBar = (ProgressBar) this.findViewById(result, R.id.rc_progress);
        holder.warning = (ImageView) this.findViewById(result, R.id.rc_warning);
        holder.readReceipt = (TextView) this.findViewById(result, R.id.rc_read_receipt);
        holder.readReceiptRequest = (TextView) this.findViewById(result, R.id.rc_read_receipt_request);
        holder.readReceiptStatus = (TextView) this.findViewById(result, R.id.rc_read_receipt_status);
        holder.message_check = (CheckBox) this.findViewById(result, R.id.message_check);
        holder.checkboxLayout = (LinearLayout) this.findViewById(result, R.id.ll_message_check);
        holder.time = (TextView) this.findViewById(result, R.id.rc_time);
        holder.sentStatus = (TextView) this.findViewById(result, R.id.rc_sent_status);
        holder.layoutItem = (RelativeLayout) this.findViewById(result, R.id.rc_layout_item_message);
        holder.sendTimeView = (TextView) this.findViewById(result, R.id.countdown_time_send);
        holder.receiveTimeView = (TextView) this.findViewById(result, R.id.countdown_time_receive);
        if (holder.time.getVisibility() == View.GONE) {
            this.timeGone = true;
        } else {
            this.timeGone = false;
        }

        result.setTag(holder);
        return result;
    }


    public class MyViewHolder extends MessageListAdapter.ViewHolder {

    }
}
