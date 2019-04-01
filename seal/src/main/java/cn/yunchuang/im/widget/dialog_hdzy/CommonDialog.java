package cn.yunchuang.im.widget.dialog_hdzy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.yunchuang.im.R;


/**
 * 普通弹窗
 * <p>
 */
public class CommonDialog extends VZyBaseView implements View.OnClickListener {

    private Context mContext;
    private CallBack mCallBack;
    private TextView mTitleTv;
    private TextView mContentTv;
    private TextView mSureTv;
    private TextView mCancelTv;

    private String mTitleStr;
    private String mContentStr;
    private String mSureStr;
    private String mCancelStr;

    public CommonDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout, null);
        mTitleTv = (TextView) view.findViewById(R.id.common_dialog_title_tv);
        mContentTv = (TextView) view.findViewById(R.id.common_dialog_content_tv);
        mSureTv = (TextView) view.findViewById(R.id.common_dialog_sure_tv);
        mCancelTv = (TextView) view.findViewById(R.id.common_dialog_cancel_tv);
        mSureTv.setOnClickListener(this);
        mCancelTv.setOnClickListener(this);
        mTitleTv.setText(mTitleStr);
        mContentTv.setText(mContentStr);
        mSureTv.setText(mSureStr);
        mCancelTv.setText(mCancelStr);
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

    public static class Builder {
        private Activity context;
        private CallBack callBack;
        private String title;
        private String content;
        private String sureStr;
        private String cancelStr;


        public Builder() {
        }

        public CommonDialog build() {
            CommonDialog dialog = new CommonDialog(context);
            dialog.mContext = context;
            dialog.mCallBack = callBack == null ? new CallBack() {
                @Override
                public void callback() {

                }
            } : callBack;
            dialog.mTitleStr = title == null ? "" : title;
            dialog.mContentStr = content == null ? "" : content;
            dialog.mSureStr = sureStr == null ? "确定" : sureStr;
            dialog.mCancelStr = cancelStr == null ? "取消" : cancelStr;
            return dialog;

        }

        public CommonDialog.Builder context(Activity context) {
            this.context = context;
            return this;
        }

        public CommonDialog.Builder sureCallback(CallBack callBack) {
            this.callBack = callBack;
            return this;
        }

        public CommonDialog.Builder title(String title) {
            this.title = title;
            return this;
        }

        public CommonDialog.Builder content(String content) {
            this.content = content;
            return this;
        }

        public CommonDialog.Builder sureStr(String sureStr) {
            this.sureStr = sureStr;
            return this;
        }

        public CommonDialog.Builder cancelStr(String cancelStr) {
            this.cancelStr = cancelStr;
            return this;
        }
    }


}
