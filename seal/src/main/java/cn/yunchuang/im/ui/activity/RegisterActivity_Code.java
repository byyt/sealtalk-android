package cn.yunchuang.im.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

import cn.yunchuang.im.R;
import cn.yunchuang.im.SealConst;
import cn.yunchuang.im.SealUserInfoManager;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.server.response.GetTokenResponse;
import cn.yunchuang.im.server.response.GetUserInfoByIdResponse;
import cn.yunchuang.im.server.response.LoginResponse;
import cn.yunchuang.im.server.response.RegisterResponse;
import cn.yunchuang.im.server.utils.NLog;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.server.utils.RongGenerate;
import cn.yunchuang.im.server.widget.LoadDialog;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by AMing on 16/1/14.
 * Company RongCloud
 */
@SuppressWarnings("deprecation")
public class RegisterActivity_Code extends BaseActivity implements View.OnClickListener {

    private final static String TAG = "RegisterActivity_Code";
    private static final int REGISTER = 4;
    private static final int CODE_LOGIN = 5;
    private static final int GET_TOKEN = 6;
    private static final int SYNC_USER_INFO = 9;
    private EditText mNickEdit;
    private TextView mConfirm;
    private String mPhone, mNickName, mPassword, mCodeToken, loginToken, mLoginType;
    private String connectResultId;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_code);
        setHeadVisibility(View.GONE);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        initView();
    }

    private void initView() {
        mPhone = getIntent().getStringExtra("phone");
        mCodeToken = getIntent().getStringExtra("verification_token");
        mLoginType = getIntent().getStringExtra("loginType");
        mNickEdit = (EditText) findViewById(R.id.register_code_username);
        mConfirm = (TextView) findViewById(R.id.register_code_sure);
        mConfirm.setOnClickListener(this);

        if ("code_login".equals(mLoginType)) {
            mPassword = getRandomString(16); //如果是验证码登录，随机生成一串16位密码（确保不被盗密码）
            mConfirm.setText("确认");
        } else if ("forget_password".equals(mLoginType)) {
            mPassword = "";
            mConfirm.setText("下一步");
        }
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case REGISTER:
                return action.codeRegister(mNickName, mPassword, mCodeToken);
            case CODE_LOGIN:
                return action.codeLogin("86", mPhone, mCodeToken);
            case GET_TOKEN:
                return action.getToken();
            case SYNC_USER_INFO:
                return action.getUserInfoById(connectResultId);
        }
        return super.doInBackground(requestCode, id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case REGISTER:
                    RegisterResponse rres = (RegisterResponse) result;
                    switch (rres.getCode()) {
                        case 200:
                            request(CODE_LOGIN);
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
                                }
                            });
                        }
                    } else if (loginResponse.getCode() == 3000) {
                        //用户未注册，进入到完善个人资料页面，刚注册完登录，按理说不会出现这种情况
                    } else {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, "登录失败，请重新检查网络哦");
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

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case REGISTER:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, "注册失败，请重新检查网络哦");
                break;
            case CODE_LOGIN:
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
    public android.support.v4.app.FragmentManager getSupportFragmentManager() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_code_sure:
                mNickName = mNickEdit.getText().toString().trim();

                if (TextUtils.isEmpty(mNickName)) {
                    NToast.shortToast(mContext, getString(R.string.name_is_null));
//                    mNickEdit.setShakeAnimation();
                    return;
                }
                if (mNickName.contains(" ")) {
                    NToast.shortToast(mContext, getString(R.string.name_contain_spaces));
//                    mNickEdit.setShakeAnimation();
                    return;
                }
                if ("code_login".equals(mLoginType)) {
                    //验证码登录，完善资料后直接登录
                    LoadDialog.show(mContext);
                    request(REGISTER, true);
                } else if ("forget_password".equals(mLoginType)) {
                    //忘记密码，完善资料后进入设置密码界面
                    Intent intent = new Intent(this, ForgetPasswordActivity_Reset.class);
                    intent.putExtra("phone", mPhone);
                    intent.putExtra("verification_token", mCodeToken);
                    intent.putExtra("resetType","not_register");
                    intent.putExtra("nickName",mNickName);
                    startActivity(intent);
                }
                break;
        }
    }

    private void reGetToken() {
        request(GET_TOKEN);
    }

    private void goToMain() {
        editor.putString("loginToken", loginToken);
        editor.putString(SealConst.SEALTALK_LOGING_PHONE, mPhone);
//        editor.putString(SealConst.SEALTALK_LOGING_PASSWORD, codeString);
        editor.commit();
        LoadDialog.dismiss(mContext);
        NToast.shortToast(mContext, R.string.login_success);
        startActivity(new Intent(RegisterActivity_Code.this, MainActivity.class));
        finish();
    }

    //随机字符串
    private String getRandomString(int length) {
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //由Random生成随机数
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        //长度为几就循环几次
        for (int i = 0; i < length; ++i) {
            //产生0-61的数字
            int number = random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }
}
