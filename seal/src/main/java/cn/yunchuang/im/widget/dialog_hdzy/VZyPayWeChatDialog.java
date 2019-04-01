package cn.yunchuang.im.widget.dialog_hdzy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.yunchuang.im.R;


/**
 * 首页运营弹窗
 * <p>
 * Created with IntelliJ IDEA.
 * User: jacob
 * Date: 14-2-26
 * Time: 下午1:35
 */
public class VZyPayWeChatDialog extends VZyBaseView implements View.OnClickListener {

    private Context mContext;
    private CallBack mCallBack;
    private TextView mContentTv;
    private TextView mSureTv;
    private TextView mCancelTv;

    private String mContentStr;

    public VZyPayWeChatDialog(Activity activity, CallBack callBack) {
        super(activity);
        this.mContext = activity;
        this.mCallBack = callBack;
    }

    @Override
    protected View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout, null);
        mContentTv = (TextView) view.findViewById(R.id.common_dialog_content_tv);
        mSureTv = (TextView) view.findViewById(R.id.common_dialog_sure_tv);
        mCancelTv = (TextView) view.findViewById(R.id.common_dialog_cancel_tv);
        mSureTv.setOnClickListener(this);
        mCancelTv.setOnClickListener(this);
        mContentTv.setText(mContentStr);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == mSureTv) {
            if (mCallBack != null) {
                mCallBack.callback();
            }
            baseDialog.closeCurrentView();
        } else if (view == mCancelTv) {
            baseDialog.closeCurrentView();
        }
    }

    public interface CallBack {
        void callback();
    }

    public void setContent(String content) {
        mContentStr = content;
    }

    public static class Builder {

    }
}
