package cn.yunchuang.im.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.yunchuang.im.R;
import cn.yunchuang.im.SealConst;
import cn.yunchuang.im.SealUserInfoManager;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.server.response.LoginResponse;
import cn.yunchuang.im.server.response.SendCodeResponse;
import cn.yunchuang.im.server.response.VerifyCodeResponse;
import cn.yunchuang.im.server.utils.AMUtils;
import cn.yunchuang.im.server.utils.NLog;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.server.utils.downtime.DownTimer;
import cn.yunchuang.im.server.utils.downtime.DownTimerListener;
import cn.yunchuang.im.server.widget.ClearWriteEditText;
import cn.yunchuang.im.server.widget.LoadDialog;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by AMing on 16/2/2.
 * Company RongCloud
 */
@SuppressWarnings("deprecation")
public class ForgetPasswordActivity_New extends BaseActivity implements View.OnClickListener, DownTimerListener {

    private static final int SEND_CODE = 2;
    private static final int VERIFY_CODE = 3;
    private static final int CODE_LOGIN = 4;
    private ClearWriteEditText mPhoneEdit, mCodeEdit;
    private Button mGetCode, mConfirm;
    private String phoneString, mCodeToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_new);
        setTitle(R.string.forget_password);
        initView();

    }

    private void initView() {
        mPhoneEdit = (ClearWriteEditText) findViewById(R.id.forget_phone);
        mCodeEdit = (ClearWriteEditText) findViewById(R.id.forget_code);
        mGetCode = (Button) findViewById(R.id.forget_getcode);
        mConfirm = (Button) findViewById(R.id.forget_button);
        mGetCode.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mPhoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    if (AMUtils.isMobile(s.toString().trim())) {
                        phoneString = mPhoneEdit.getText().toString().trim();
                        AMUtils.onInactive(mContext, mPhoneEdit);
                        mGetCode.setClickable(true);
                        mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
                    } else {
                        Toast.makeText(mContext, R.string.Illegal_phone_number, Toast.LENGTH_SHORT).show();
                        mGetCode.setClickable(false);
                        mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
                    }
                } else {
                    mGetCode.setClickable(false);
                    mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String oldPhone = getIntent().getStringExtra("phone");
        if (!TextUtils.isEmpty(oldPhone)) {
            mPhoneEdit.setText(oldPhone);
        }
        mPhoneEdit.setSelection(mPhoneEdit.getText().length());

        mCodeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6 || s.length() == 4) { //验证码位数，这个后期需要确定
                    AMUtils.onInactive(mContext, mCodeEdit);
                    mConfirm.setClickable(true);
                    mConfirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
                } else {
                    mConfirm.setClickable(false);
                    mConfirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case SEND_CODE:
                return action.sendCode("86", phoneString);
            case VERIFY_CODE:
                return action.verifyCode("86", phoneString, mCodeEdit.getText().toString());
            case CODE_LOGIN:
                return action.codeLogin("86", phoneString, mCodeToken);
        }
        return super.doInBackground(requestCode, id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case SEND_CODE:
                    SendCodeResponse scrres = (SendCodeResponse) result;
                    if (scrres.getCode() == 200) {
                        NToast.shortToast(mContext, R.string.messge_send);
                    } else if (scrres.getCode() == 5000) {
                        NToast.shortToast(mContext, R.string.message_frequency);
                    }
                    break;
                case VERIFY_CODE:
                    VerifyCodeResponse vcres = (VerifyCodeResponse) result;
                    switch (vcres.getCode()) {
                        case 200:
                            mCodeToken = vcres.getResult().getVerification_token();
                            if (!TextUtils.isEmpty(mCodeToken)) {
                                request(CODE_LOGIN);
                            } else {
                                NToast.shortToast(mContext, "code token is null");
                                LoadDialog.dismiss(mContext);
                            }
                            break;
                        case 1000:
                            //验证码错误
                            NToast.shortToast(mContext, R.string.verification_code_error);
                            LoadDialog.dismiss(mContext);
                            break;
                        case 2000:
                            //验证码过期
                            NToast.shortToast(mContext, R.string.captcha_overdue);
                            LoadDialog.dismiss(mContext);
                            break;
                    }
                    break;
                case CODE_LOGIN:
                    LoginResponse loginResponse = (LoginResponse) result;
                    if (loginResponse.getCode() == 200) {
                        //已经注册过，可以进入修改密码界面
                        LoadDialog.dismiss(mContext);
                        Intent intent = new Intent(this, ForgetPasswordActivity_Reset.class);
                        intent.putExtra("phone", phoneString);
                        intent.putExtra("verification_token", mCodeToken);
                        intent.putExtra("resetType","has_register");
                        startActivityForResult(intent, 1);

                    } else if (loginResponse.getCode() == 3000) {
                        //用户未注册，进入到完善个人资料页面
                        LoadDialog.dismiss(mContext);
                        Intent intent = new Intent(this, RegisterActivity_Code.class);
                        intent.putExtra("phone", mPhoneEdit.getText().toString().trim());
                        intent.putExtra("verification_token", mCodeToken);
                        intent.putExtra("loginType", "forget_password");
                        startActivityForResult(intent, 1);
                    } else {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, R.string.code_error_or_overdue);
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case SEND_CODE:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, "获取验证码请求失败");
                break;
            case VERIFY_CODE:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, "验证码是否可用请求失败");
                break;
            case CODE_LOGIN:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, R.string.login_api_fail);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_getcode:
                if (TextUtils.isEmpty(mPhoneEdit.getText().toString().trim())) {
                    NToast.longToast(mContext, getString(R.string.phone_number_is_null));
                } else {
                    DownTimer downTimer = new DownTimer();
                    downTimer.setListener(this);
                    downTimer.startDown(60 * 1000);
                    request(SEND_CODE);
                }
                break;
            case R.id.forget_button:
                if (TextUtils.isEmpty(mPhoneEdit.getText().toString())) {
                    NToast.shortToast(mContext, getString(R.string.phone_number_is_null));
                    mPhoneEdit.setShakeAnimation();
                    return;
                }

                if (TextUtils.isEmpty(mCodeEdit.getText().toString())) {
                    NToast.shortToast(mContext, getString(R.string.code_is_null));
                    mCodeEdit.setShakeAnimation();
                    return;
                }

                LoadDialog.show(mContext);
                request(VERIFY_CODE);
                break;
        }
    }

    @Override
    public void onHeadLeftButtonClick(View v) {
        super.onHeadLeftButtonClick(v);
        AMUtils.onInactive(mContext, mPhoneEdit);
        AMUtils.onInactive(mContext, mCodeEdit);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mGetCode.setText("seconds:" + String.valueOf(millisUntilFinished / 1000));
        mGetCode.setClickable(false);
        mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
    }

    @Override
    public void onFinish() {
        mGetCode.setText(R.string.get_code);
        mGetCode.setClickable(true);
        mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
    }
}
