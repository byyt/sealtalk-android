package cn.yunchuang.im.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import cn.yunchuang.im.R;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.server.response.RegisterResponse;
import cn.yunchuang.im.server.response.RestPasswordResponse;
import cn.yunchuang.im.server.utils.AMUtils;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.server.widget.ClearWriteEditText;
import cn.yunchuang.im.server.widget.LoadDialog;

/**
 * Created by AMing on 16/2/2.
 * Company RongCloud
 */
@SuppressWarnings("deprecation")
public class ForgetPasswordActivity_Reset extends BaseActivity implements View.OnClickListener {

    private static final int REGISTER = 4;
    private static final int CHANGE_PASSWORD = 33;
    private ClearWriteEditText mPassword1, mPassword2;
    private Button mConfirm;
    private String phoneString, mCodeToken, mResetType, mNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_reset);
        phoneString = getIntent().getStringExtra("phone");
        mCodeToken = getIntent().getStringExtra("verification_token");
        mResetType = getIntent().getStringExtra("resetType");
        //只有mResetType为not_register即还未注册过时（忘记密码进来的），下面的mNickName才有值
        mNickName = getIntent().getStringExtra("nickName");
        if ("has_register".equals(mResetType)) {
            setTitle("重置密码");
        } else {
            setTitle("设置密码");
        }
        initView();
    }

    private void initView() {
        mPassword1 = (ClearWriteEditText) findViewById(R.id.forget_password);
        mPassword2 = (ClearWriteEditText) findViewById(R.id.forget_password1);
        mConfirm = (Button) findViewById(R.id.forget_button);
        mConfirm.setOnClickListener(this);
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case CHANGE_PASSWORD:
                return action.restPassword(mPassword1.getText().toString(), mCodeToken);
            case REGISTER:
                return action.codeRegister(mNickName, mPassword1.getText().toString(), mCodeToken);
        }
        return super.doInBackground(requestCode, id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case CHANGE_PASSWORD:
                    RestPasswordResponse response1 = (RestPasswordResponse) result;
                    if (response1.getCode() == 200) {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, "密码更改成功");
                        Intent intent = new Intent(mContext, LoginActivity_Code_PassWord.class);
                        intent.putExtra("phone", phoneString);
                        intent.putExtra("password", mPassword1.getText().toString());
                        intent.putExtra("loginType", "forget_password");
                        startActivity(intent);
                        this.finish();
                    }
                    break;
                case REGISTER:
                    RegisterResponse rres = (RegisterResponse) result;
                    switch (rres.getCode()) {
                        case 200:
                            //注册成功后，跳回登录页
                            LoadDialog.dismiss(mContext);
                            NToast.shortToast(mContext, "密码设置成功");
                            Intent intent = new Intent(mContext, LoginActivity_Code_PassWord.class);
                            intent.putExtra("phone", phoneString);
                            intent.putExtra("password", mPassword1.getText().toString());
                            intent.putExtra("loginType", "forget_password");
                            startActivity(intent);
                            this.finish();
                            break;
                        case 400://后边的错误，最后要统一弹窗，把转圈去掉
                            // 错误的请求
                        case 404:
                            //token 不存在
                        case 500:
                            //应用服务端内部错误
                            LoadDialog.dismiss(mContext);
                            NToast.shortToast(mContext, "登录失败，请重新检查网络哦");
                            break;
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case CHANGE_PASSWORD:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, "修改密码失败，请重新检查网络哦");
                break;
            case REGISTER:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, "密码设置失败，请重新检查网络哦");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_button:
                if (TextUtils.isEmpty(mPassword1.getText().toString())) {
                    NToast.shortToast(mContext, getString(R.string.password_is_null));
                    mPassword1.setShakeAnimation();
                    return;
                }

                if (mPassword1.length() < 6 || mPassword1.length() > 16) {
                    NToast.shortToast(mContext, R.string.passwords_invalid);
                    return;
                }

                if (TextUtils.isEmpty(mPassword2.getText().toString())) {
                    NToast.shortToast(mContext, getString(R.string.confirm_password));
                    mPassword2.setShakeAnimation();
                    return;
                }

                if (!mPassword2.getText().toString().equals(mPassword1.getText().toString())) {
                    NToast.shortToast(mContext, getString(R.string.passwords_do_not_match));
                    return;
                }

                LoadDialog.show(mContext);
                if ("has_register".equals(mResetType)) {
                    request(CHANGE_PASSWORD);
                } else {
                    request(REGISTER);
                }

                break;
        }
    }

    @Override
    public void onHeadLeftButtonClick(View v) {
        super.onHeadLeftButtonClick(v);
        AMUtils.onInactive(mContext, mPassword1);
        AMUtils.onInactive(mContext, mPassword2);
    }
}
