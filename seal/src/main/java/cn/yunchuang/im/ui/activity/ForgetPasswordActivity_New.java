package cn.yunchuang.im.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.yunchuang.im.R;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.server.response.LoginResponse;
import cn.yunchuang.im.server.response.SendCodeResponse;
import cn.yunchuang.im.server.response.VerifyCodeResponse;
import cn.yunchuang.im.server.utils.AMUtils;
import cn.yunchuang.im.server.utils.CommonUtils;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.server.utils.downtime.DownTimer;
import cn.yunchuang.im.server.utils.downtime.DownTimerListener;
import cn.yunchuang.im.server.widget.ClearWriteEditText;
import cn.yunchuang.im.server.widget.LoadDialog;

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
    private TextView mTitleBack, mGetCode, mConfirm;
    private String phoneString, codeString, mCodeToken;
    private boolean isBright = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_new);
        setHeadVisibility(View.GONE);
        initView();

    }

    private void initView() {
        mTitleBack = (TextView) findViewById(R.id.forget_title_back);
        mPhoneEdit = (ClearWriteEditText) findViewById(R.id.activity_forget_phone);
        mCodeEdit = (ClearWriteEditText) findViewById(R.id.activity_forget_code);
        mGetCode = (TextView) findViewById(R.id.activity_forget_getcode);
        mConfirm = (TextView) findViewById(R.id.activty_forget_next);
        mTitleBack.setOnClickListener(this);
        mGetCode.setOnClickListener(this);
        mGetCode.setEnabled(false);
        mConfirm.setOnClickListener(this);
        mPhoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11 && isBright) {
                    if (AMUtils.isMobile(s.toString().trim())) {
                        phoneString = s.toString().trim();
//                        AMUtils.onInactive(mContext, mPhoneEdit);//收起键盘，模仿请吃饭，不用收起键盘，点击发送验证码后，直接输入验证码
                    } else {
                        Toast.makeText(mContext, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                    mGetCode.setEnabled(true);
                } else {
                    mGetCode.setEnabled(false);
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
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case SEND_CODE:
                return action.sendCode("86", phoneString);
            case VERIFY_CODE:
                return action.verifyCode("86", phoneString, mCodeEdit.getText().toString());
            case CODE_LOGIN: //请求登录接口，目的是为了判断账户是否已经注册
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
                case CODE_LOGIN: //请求登录接口，目的是为了判断账户是否已经注册
                    LoginResponse loginResponse = (LoginResponse) result;
                    if (loginResponse.getCode() == 200) {
                        //已经注册过，可以进入修改密码界面
                        LoadDialog.dismiss(mContext);
                        Intent intent = new Intent(this, ForgetPasswordActivity_Reset.class);
                        intent.putExtra("phone", phoneString);
                        intent.putExtra("verification_token", mCodeToken);
                        intent.putExtra("resetType", "has_register");
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
                NToast.shortToast(mContext, "获取验证码失败，请稍后重试");
                break;
            case VERIFY_CODE:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, "验证码错误或已过期");
                break;
            case CODE_LOGIN: //请求登录接口，目的是为了判断账户是否已经注册
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, "未知错误，请稍后重试");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_title_back:
                finish();
                break;
            case R.id.activity_forget_getcode:
                if (!AMUtils.isMobile(mPhoneEdit.getText().toString().trim())) {
                    Toast.makeText(mContext, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
                    mPhoneEdit.setShakeAnimation();
                    return;
                }
                if (!CommonUtils.isNetworkConnected(mContext)) {
                    NToast.shortToast(mContext, getString(R.string.network_not_available));
                    return;
                }
                DownTimer downTimer = new DownTimer();
                downTimer.setListener(this);
                downTimer.startDown(60 * 1000);
                request(SEND_CODE);
                break;
            case R.id.activty_forget_next:
                phoneString = mPhoneEdit.getText().toString().trim();
                codeString = mCodeEdit.getText().toString().trim();
                if (TextUtils.isEmpty(phoneString) || !AMUtils.isMobile(phoneString)) {
                    NToast.shortToast(mContext, "请填写正确的手机号码");
                    mPhoneEdit.setShakeAnimation();
                    return;
                }
                if (TextUtils.isEmpty(codeString) || codeString.length() < 4 || codeString.length() > 6) {
                    NToast.shortToast(mContext, "请填写4-6位数字验证码");
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
        mGetCode.setText(String.valueOf(millisUntilFinished / 1000) + "秒后获取");
        mGetCode.setEnabled(false);
        isBright = false;
    }

    @Override
    public void onFinish() {
        mGetCode.setText(R.string.get_code);
        mGetCode.setEnabled(true);
        isBright = true;
    }
}
