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
public class VZyPayImgDialog extends VZyBaseView implements View.OnClickListener {

    private Context mContext;
    private CallBack mCallBack;
    private TextView mSureTv;
    private TextView mCancelTv;

    public VZyPayImgDialog(Activity activity, CallBack callBack) {
        super(activity);
        this.mContext = activity;
        this.mCallBack = callBack;
    }

    @Override
    protected View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_pay_img, null);
        mSureTv = (TextView) view.findViewById(R.id.operate_window_sure_tv);
        mCancelTv = (TextView) view.findViewById(R.id.operate_window_cancel_tv);
        mSureTv.setOnClickListener(this);
        mCancelTv.setOnClickListener(this);
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
}
