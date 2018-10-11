package cn.yunchuang.im.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.yunchuang.im.R;
import cn.yunchuang.im.SealConst;
import cn.yunchuang.im.SealUserInfoManager;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.server.response.GetTokenResponse;
import cn.yunchuang.im.server.response.GetUserInfoByIdResponse;
import cn.yunchuang.im.server.response.LoginResponse;
import cn.yunchuang.im.server.response.SendCodeResponse;
import cn.yunchuang.im.server.response.VerifyCodeResponse;
import cn.yunchuang.im.server.utils.AMUtils;
import cn.yunchuang.im.server.utils.CommonUtils;
import cn.yunchuang.im.server.utils.NLog;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.server.utils.RongGenerate;
import cn.yunchuang.im.server.utils.downtime.DownTimer;
import cn.yunchuang.im.server.utils.downtime.DownTimerListener;
import cn.yunchuang.im.server.widget.ClearWriteEditText;
import cn.yunchuang.im.server.widget.LoadDialog;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by AMing on 16/1/15.
 * Company RongCloud
 * 验证码登录界面
 */
@SuppressWarnings("deprecation")
public class LoginActivity_Code_PassWord extends BaseActivity implements View.OnClickListener, DownTimerListener {

    private final static String TAG = "LoginActivity";
    private static final int SEND_CODE = 2;
    private static final int VERIFY_CODE = 3;
    private static final int CODE_LOGIN = 4;
    private static final int LOGIN = 5;
    private static final int GET_TOKEN = 6;
    private static final int SYNC_USER_INFO = 9;

    private ClearWriteEditText mPhoneEdit, mCodeEdit;
    private TextView mGetCode, mConfirm, mSwitchLogin, mForgetPassword;
    private String phoneString;
    private String codeString;
    private String connectResultId;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String mCodeToken, loginToken;
    private boolean isBright = true;
    private boolean isRequestCode = false;
    private boolean isCodeLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_code_password);
        setHeadVisibility(View.GONE);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        initView();
    }

    //LoginActivity_Code_PassWord类设置为singleTask模式，在比如修改密码等界面中可以直接回到本登录界面，调用此函数
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            //只有在设置密码跳回登录页时才进行下面的操作
            if (!"forget_password".equals(intent.getStringExtra("loginType"))) {
                return;
            }
            isCodeLogin = false; //切为密码登录
            setCodeOrPasswordStatus();
            if (!TextUtils.isEmpty(intent.getStringExtra("phone"))) {
                phoneString = intent.getStringExtra("phone");
                mPhoneEdit.setText(phoneString);
            }
            if (!TextUtils.isEmpty(intent.getStringExtra("password"))) {
                codeString = intent.getStringExtra("password");
                mCodeEdit.setText(codeString);
            }
        }
        mPhoneEdit.setSelection(mPhoneEdit.getText().length());
        mCodeEdit.setSelection(mCodeEdit.getText().length());
    }

    private void initView() {
        mPhoneEdit = (ClearWriteEditText) findViewById(R.id.activity_login_phone_num);
        mCodeEdit = (ClearWriteEditText) findViewById(R.id.activity_login_verification_code);
        mGetCode = (TextView) findViewById(R.id.activity_login_get_verification_code);
        mConfirm = (TextView) findViewById(R.id.activity_login_sure);
        mSwitchLogin = (TextView) findViewById(R.id.activity_login_password_login);
        mForgetPassword = (TextView) findViewById(R.id.activity_forget_password);
        mGetCode.setOnClickListener(this);
        mGetCode.setClickable(false);
        mConfirm.setOnClickListener(this);
        mSwitchLogin.setOnClickListener(this);
        mForgetPassword.setOnClickListener(this);
        mPhoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11 && isBright) {
                    if (AMUtils.isMobile(s.toString().trim())) {
                        phoneString = s.toString().trim();
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

        String oldPhone = sp.getString(SealConst.SEALTALK_LOGING_PHONE, "");
        if (!TextUtils.isEmpty(oldPhone)) {
            mPhoneEdit.setText(oldPhone);
        }
        mPhoneEdit.setSelection(mPhoneEdit.getText().length());

        if (getIntent().getBooleanExtra("kickedByOtherClient", false)) {
            final AlertDialog dlg = new AlertDialog.Builder(LoginActivity_Code_PassWord.this).create();
            dlg.show();
            Window window = dlg.getWindow();
            window.setContentView(R.layout.other_devices);
            TextView text = (TextView) window.findViewById(R.id.ok);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dlg.cancel();
                }
            });
        }
        setCodeOrPasswordStatus();
        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.activity_login_root_layout);
        addLayoutListener(rootLayout, mForgetPassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_login_get_verification_code:
                if (TextUtils.isEmpty(mPhoneEdit.getText().toString().trim())) {
                    NToast.longToast(mContext, R.string.phone_number_is_null);
                } else {
                    isRequestCode = true;
                    DownTimer downTimer = new DownTimer();
                    downTimer.setListener(this);
                    downTimer.startDown(60 * 1000);
                    request(SEND_CODE);
                }
                break;
            case R.id.activity_login_sure:
                if (isCodeLogin) {
                    codeLogin();
                } else {
                    passwordLogin();
                }
                break;
            case R.id.activity_login_password_login:
                switchCodeOrPassword();
                break;
            case R.id.activity_forget_password:
                Intent intent = new Intent(this, ForgetPasswordActivity_New.class);
                intent.putExtra("phone", mPhoneEdit.getText().toString().trim());
                startActivityForResult(intent, 2);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && data != null) {
            String phone = data.getStringExtra("phone");
            String password = data.getStringExtra("password");
            mPhoneEdit.setText(phone);
            mCodeEdit.setText(password);
        } else if (data != null && requestCode == 1) {
            String phone = data.getStringExtra("phone");
            String password = data.getStringExtra("password");
            String id = data.getStringExtra("id");
            String nickname = data.getStringExtra("nickname");
            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(id) && !TextUtils.isEmpty(nickname)) {
                mPhoneEdit.setText(phone);
                mCodeEdit.setText(password);
                editor.putString(SealConst.SEALTALK_LOGING_PHONE, phone);
                editor.putString(SealConst.SEALTALK_LOGING_PASSWORD, password);
                editor.putString(SealConst.SEALTALK_LOGIN_ID, id);
                editor.putString(SealConst.SEALTALK_LOGIN_NAME, nickname);
                editor.commit();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case SEND_CODE:
                return action.sendCode("86", phoneString);
            case VERIFY_CODE:
                return action.verifyCode("86", phoneString, codeString);
            case CODE_LOGIN:
                return action.codeLogin("86", phoneString, mCodeToken);
            case LOGIN:
                return action.login("86", phoneString, codeString);
            case GET_TOKEN:
                return action.getToken();
            case SYNC_USER_INFO:
                return action.getUserInfoById(connectResultId);
        }
        return null;
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
                        loginToken = loginResponse.getResult().getToken();
                        if (!TextUtils.isEmpty(loginToken)) {
                            RongIM.connect(loginToken, new RongIMClient.ConnectCallback() {
                                @Override
                                public void onTokenIncorrect() {
                                    NLog.e("connect", "onTokenIncorrect");
                                    reGetToken();
                                }

                                @Override
                                public void onSuccess(String s) {
                                    connectResultId = s;
                                    NLog.e("connect", "onSuccess userid:" + s);
                                    editor.putString(SealConst.SEALTALK_LOGIN_ID, s);
                                    editor.commit();
                                    SealUserInfoManager.getInstance().openDB();
                                    request(SYNC_USER_INFO, true);
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    NLog.e("connect", "onError errorcode:" + errorCode.getValue());
                                    LoadDialog.dismiss(mContext);
                                    NToast.shortToast(mContext, "连接出错");
                                }
                            });
                        }
                    } else if (loginResponse.getCode() == 3000) {
                        //用户未注册，进入到完善个人资料页面
                        LoadDialog.dismiss(mContext);
                        Intent intent = new Intent(this, RegisterActivity_Code.class);
                        intent.putExtra("phone", mPhoneEdit.getText().toString().trim());
                        intent.putExtra("verification_token", mCodeToken);
                        intent.putExtra("loginType", "code_login");
                        startActivityForResult(intent, 1);
                    } else {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, R.string.code_error_or_overdue);
                    }
                    break;
                case LOGIN:
                    LoginResponse loginResponseP = (LoginResponse) result;
                    if (loginResponseP.getCode() == 200) {
                        loginToken = loginResponseP.getResult().getToken();
                        if (!TextUtils.isEmpty(loginToken)) {
                            RongIM.connect(loginToken, new RongIMClient.ConnectCallback() {
                                @Override
                                public void onTokenIncorrect() {
                                    NLog.e("connect", "onTokenIncorrect");
                                    reGetToken();
                                }

                                @Override
                                public void onSuccess(String s) {
                                    connectResultId = s;
                                    NLog.e("connect", "onSuccess userid:" + s);
                                    editor.putString(SealConst.SEALTALK_LOGIN_ID, s);
                                    editor.commit();
                                    SealUserInfoManager.getInstance().openDB();
                                    request(SYNC_USER_INFO, true);
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    NLog.e("connect", "onError errorcode:" + errorCode.getValue());
                                    LoadDialog.dismiss(mContext);
                                    NToast.shortToast(mContext, "连接出错");
                                }
                            });
                        }
                    } else if (loginResponseP.getCode() == 100) {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, R.string.phone_or_psw_error);
                    } else if (loginResponseP.getCode() == 1000) {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, R.string.phone_or_psw_error);
                    }
                    break;
                case SYNC_USER_INFO:
                    GetUserInfoByIdResponse userInfoByIdResponse = (GetUserInfoByIdResponse) result;
                    if (userInfoByIdResponse.getCode() == 200) {
                        if (TextUtils.isEmpty(userInfoByIdResponse.getResult().getPortraitUri())) {
                            userInfoByIdResponse.getResult().setPortraitUri(RongGenerate.generateDefaultAvatar(userInfoByIdResponse.getResult().getNickname(), userInfoByIdResponse.getResult().getId()));
                        }
                        String nickName = userInfoByIdResponse.getResult().getNickname();
                        String portraitUri = userInfoByIdResponse.getResult().getPortraitUri();
                        editor.putString(SealConst.SEALTALK_LOGIN_NAME, nickName);
                        editor.putString(SealConst.SEALTALK_LOGING_PORTRAIT, portraitUri);
                        editor.commit();
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(connectResultId, nickName, Uri.parse(portraitUri)));
                    }
                    //不继续在login界面同步好友,群组,群组成员信息
                    SealUserInfoManager.getInstance().getAllUserInfo();
                    goToMain();
                    break;
                case GET_TOKEN:
                    GetTokenResponse tokenResponse = (GetTokenResponse) result;
                    if (tokenResponse.getCode() == 200) {
                        String token = tokenResponse.getResult().getToken();
                        if (!TextUtils.isEmpty(token)) {
                            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                                @Override
                                public void onTokenIncorrect() {
                                    Log.e(TAG, "reToken Incorrect");
                                }

                                @Override
                                public void onSuccess(String s) {
                                    connectResultId = s;
                                    NLog.e("connect", "onSuccess userid:" + s);
                                    editor.putString(SealConst.SEALTALK_LOGIN_ID, s);
                                    editor.commit();
                                    SealUserInfoManager.getInstance().openDB();
                                    request(SYNC_USER_INFO, true);
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode e) {

                                }
                            });
                        }
                    }
                    break;
            }
        }

    }

    private void reGetToken() {
        request(GET_TOKEN);
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        if (!CommonUtils.isNetworkConnected(mContext)) {
            LoadDialog.dismiss(mContext);
            NToast.shortToast(mContext, getString(R.string.network_not_available));
            return;
        }
        switch (requestCode) {
            case SEND_CODE:
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
            case LOGIN:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, R.string.login_api_fail);
                break;
            case SYNC_USER_INFO:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, R.string.sync_userinfo_api_fail);
                break;
            case GET_TOKEN:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, R.string.get_token_api_fail);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void goToMain() {
        editor.putString("loginToken", loginToken);
        editor.putString(SealConst.SEALTALK_LOGING_PHONE, phoneString);
//        editor.putString(SealConst.SEALTALK_LOGING_PASSWORD, codeString); //保存密码，按理说，验证码登录不用保存密码
        editor.commit();
        LoadDialog.dismiss(mContext);
        NToast.shortToast(mContext, R.string.login_success);
        startActivity(new Intent(LoginActivity_Code_PassWord.this, MainActivity.class));
        finish();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mGetCode.setText(String.valueOf(millisUntilFinished / 1000) + "s");
        mGetCode.setClickable(false);
        mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
        isBright = false;
    }

    @Override
    public void onFinish() {
        mGetCode.setText(R.string.get_code);
        mGetCode.setClickable(true);
        mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
        isBright = true;
    }

    private void switchCodeOrPassword() {
        isCodeLogin = !isCodeLogin;
        setCodeOrPasswordStatus();
    }

    private void setCodeOrPasswordStatus() {
        if (isCodeLogin) {
            mSwitchLogin.setText("密码登录");
            mGetCode.setVisibility(View.VISIBLE);
            mCodeEdit.setText("");
            mCodeEdit.setHint("输入验证码");
            mCodeEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
            mCodeEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            mForgetPassword.setVisibility(View.GONE);
        } else {
            mSwitchLogin.setText("验证码登录");
            mGetCode.setVisibility(View.GONE);
            mCodeEdit.setText("");
            mCodeEdit.setHint("输入密码");
            mCodeEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mCodeEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
            mForgetPassword.setVisibility(View.VISIBLE);
        }
    }

    private void codeLogin() {
        phoneString = mPhoneEdit.getText().toString().trim();
        codeString = mCodeEdit.getText().toString().trim();

        if (TextUtils.isEmpty(phoneString)) {
            NToast.shortToast(mContext, R.string.phone_number_is_null);
//                    mPhoneEdit.setShakeAnimation();
            return;
        }

        if (!AMUtils.isMobile(phoneString)) {
            NToast.shortToast(mContext, R.string.Illegal_phone_number);
//                    mPhoneEdit.setShakeAnimation();
            return;
        }

        if (TextUtils.isEmpty(codeString)) {
            NToast.shortToast(mContext, R.string.code_is_null);
//                    mCodeEdit.setShakeAnimation();
            return;
        }
        if (!isRequestCode) {
            NToast.shortToast(mContext, getString(R.string.not_send_code));
            return;
        }
        LoadDialog.show(mContext);
        editor.putBoolean("exit", false);
        editor.commit();
        request(VERIFY_CODE, true);
    }

    private void passwordLogin() {
        phoneString = mPhoneEdit.getText().toString().trim();
        codeString = mCodeEdit.getText().toString().trim();

        if (TextUtils.isEmpty(phoneString)) {
            NToast.shortToast(mContext, R.string.phone_number_is_null);
//            mPhoneEdit.setShakeAnimation();
            return;
        }

        if (!AMUtils.isMobile(phoneString)) {
            NToast.shortToast(mContext, R.string.Illegal_phone_number);
//                    mPhoneEdit.setShakeAnimation();
            return;
        }

        if (TextUtils.isEmpty(codeString)) {
            NToast.shortToast(mContext, R.string.password_is_null);
//            mCodeEdit.setShakeAnimation();
            return;
        }
        if (codeString.contains(" ")) {
            NToast.shortToast(mContext, R.string.password_cannot_contain_spaces);
//            mCodeEdit.setShakeAnimation();
            return;
        }
        LoadDialog.show(mContext);
        editor.putBoolean("exit", false);
        editor.commit();
        String oldPhone = sp.getString(SealConst.SEALTALK_LOGING_PHONE, "");
        request(LOGIN, true);
    }

    /**
     * 实现键盘不遮挡登录按钮
     * 1、获取main在窗体的可视区域
     * 2、获取main在窗体的不可视区域高度
     * 3、判断不可视区域高度
     * 1、大于100:键盘显示 获取Scroll的窗体坐标 * 算出main需要滚动的高度,使scroll显示。
     * 2、小于100:键盘隐藏
     *
     * @param main   根布局
     * @param scroll 需要显示的最下方View
     */
    public void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                main.getWindowVisibleDisplayFrame(rect);
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                if (mainInvisibleHeight > 100) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    //scrollTo，x为正时向左移动，y为正时向上移动，这里不再移动到某个控件下方（因为在移动中控件位置容易不定），而是往上移动一定的距离200dp
//                    main.scrollTo(0, srollHeight + (int) CommonUtils.dpToPixel((float) 5, LoginActivity_Code_PassWord.this));
                    main.scrollTo(0, (int) CommonUtils.dpToPixel((float) 200, LoginActivity_Code_PassWord.this));
                } else {
                    main.scrollTo(0, 0);
                }
            }
        });
    }
}
