package cn.yunchuang.im.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import cn.yunchuang.im.HttpManager;
import cn.yunchuang.im.MeService;
import cn.yunchuang.im.R;
import cn.yunchuang.im.SealAppContext;
import cn.yunchuang.im.SealUserInfoManager;
import cn.yunchuang.im.db.GroupMember;
import cn.yunchuang.im.event.RefreshBalanceCoinsEvent;
import cn.yunchuang.im.server.response.BalanceCoinsModel;
import cn.yunchuang.im.server.response.BalanceCoinsResponse;
import cn.yunchuang.im.server.response.GetUserDetailModelOne;
import cn.yunchuang.im.server.response.GetUserDetailOneResponse;
import cn.yunchuang.im.server.utils.NLog;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.ui.fragment.ConversationFragmentEx;
import cn.yunchuang.im.ui.fragment.ConversationFragmentSystem;
import cn.yunchuang.im.ui.widget.LoadingDialog;
import cn.yunchuang.im.utils.AndroidBug5497Workaround;
import cn.yunchuang.im.utils.DialogUtils;
import cn.yunchuang.im.zmico.utils.BaseBaseUtils;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.rong.callkit.RongCallKit;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongKitIntent;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.fragment.UriFragment;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.UserInfo;
import io.rong.imlib.typingmessage.TypingStatus;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

import static cn.yunchuang.im.SealConst.ADD_BALANCE_ONE_CHAT_MESSAGE;
import static cn.yunchuang.im.SealConst.BALANCE_COINS_NOT_ENOUGH_BALANCE;
import static cn.yunchuang.im.SealConst.BALANCE_COINS_TYPE_CHAT_MESSAGE;
import static cn.yunchuang.im.SealConst.IDENTITY_TYPE_DAREN;
import static cn.yunchuang.im.SealConst.MINUS_BALANCE_ONE_CHAT_MESSAGE;

//CallKit start 1
//CallKit end 1

/**
 * 会话页面
 * 1，设置 ActionBar title
 * 2，加载会话页面
 * 3，push 和 通知 判断
 */
public class ConversationActivity extends BaseActivity implements View.OnClickListener, RongIM.OnSendMessageListener {

    private String TAG = ConversationActivity.class.getSimpleName();
    /**
     * 对方id
     */
    private String mTargetId;
    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    /**
     * title
     */
    private String title;
    /**
     * 是否在讨论组内，如果不在讨论组内，则进入不到讨论组设置页面
     */
    private boolean isFromPush = false;

    private LoadingDialog mDialog;

    private SharedPreferences sp;

    private String TextTypingTitle;
    private String VoiceTypingTitle;

    private Handler mHandler;

    public static final int SET_TEXT_TYPING_TITLE = 1;
    public static final int SET_VOICE_TYPING_TITLE = 2;
    public static final int SET_TARGET_ID_TITLE = 0;

    private Button mRightButton;
    private RelativeLayout layout_announce;
    private TextView tv_announce;
    private ImageView iv_arrow;

    private FrameLayout titleLayout;
    private ImageView backImg;
    private TextView titleTv;
    private TextView meIsDarenTv;
    private TextView heIsDarenTv;
    private TextView errorTv;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    @TargetApi(23)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseBaseUtils.setTranslucentStatus(this);//状态栏透明，
        setContentView(R.layout.conversation);
        //设置状态栏透明BaseBaseUtils.setTranslucentStatus(this)会和windowSoftInputMode="stateHidden|adjustResize"冲突，导致键盘弹起时无法把聊天区域往上推
        //这个类是用来解决这个问题的，解决网址：https://blog.csdn.net/plq690816/article/details/51374883
        AndroidBug5497Workaround.assistActivity(this);

        setHeadVisibility(View.GONE);

        //额外加的沉浸式标题栏
        titleLayout = (FrameLayout) findViewById(R.id.activity_conversation_title_layout);
        backImg = (ImageView) findViewById(R.id.activity_conversation_back);
        backImg.setOnClickListener(this);
        titleTv = (TextView) findViewById(R.id.activity_conversation_title_tv);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DeviceUtils.dpToPx(48) + DeviceUtils.getStatusBarHeightPixels(this));
        titleLayout.setLayoutParams(layoutParams);

        TextTypingTitle = getString(R.string.the_other_side_is_typing);
        VoiceTypingTitle = getString(R.string.the_other_side_is__speaking);

        sp = getSharedPreferences("config", MODE_PRIVATE);
        mDialog = new LoadingDialog(this);
        layout_announce = (RelativeLayout) findViewById(R.id.ll_annouce);
        iv_arrow = (ImageView) findViewById(R.id.iv_announce_arrow);
        layout_announce.setVisibility(View.GONE);
        tv_announce = (TextView) findViewById(R.id.tv_announce_msg);

        mRightButton = getHeadRightButton();

        Intent intent = getIntent();

        if (intent == null || intent.getData() == null)
            return;

        mTargetId = intent.getData().getQueryParameter("targetId");
        //10000 为 Demo Server 加好友的 id，若 targetId 为 10000，则为加好友消息，默认跳转到 NewFriendListActivity
        // Demo 逻辑
        if (mTargetId != null && mTargetId.equals("10000")) {
            startActivity(new Intent(ConversationActivity.this, NewFriendListActivity.class));
            return;
        }
        mConversationType = Conversation.ConversationType.valueOf(intent.getData()
                .getLastPathSegment().toUpperCase(Locale.US));

        title = intent.getData().getQueryParameter("title");

        setActionBarTitle(mConversationType, mTargetId);


        if (mConversationType.equals(Conversation.ConversationType.GROUP)) {
            mRightButton.setBackground(getResources().getDrawable(R.drawable.icon2_menu));
        } else if (mConversationType.equals(Conversation.ConversationType.PRIVATE)
                || mConversationType.equals(Conversation.ConversationType.PUBLIC_SERVICE)
                || mConversationType.equals(Conversation.ConversationType.APP_PUBLIC_SERVICE)
                || mConversationType.equals(Conversation.ConversationType.DISCUSSION)) {
            mRightButton.setBackground(getResources().getDrawable(R.drawable.icon1_menu));
        } else {
            mRightButton.setVisibility(View.GONE);
            mRightButton.setClickable(false);
        }
        mRightButton.setOnClickListener(this);

        isPushMessage(intent);
        if (mConversationType.equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
            setAnnounceListener();
        }

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case SET_TEXT_TYPING_TITLE:
                        setTitle(TextTypingTitle);
                        break;
                    case SET_VOICE_TYPING_TITLE:
                        setTitle(VoiceTypingTitle);
                        break;
                    case SET_TARGET_ID_TITLE:
                        setActionBarTitle(mConversationType, mTargetId);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {
            @Override
            public void onTypingStatusChanged(Conversation.ConversationType type, String targetId, Collection<TypingStatus> typingStatusSet) {
                //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
                if (type.equals(mConversationType) && targetId.equals(mTargetId)) {
                    int count = typingStatusSet.size();
                    //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                    if (count > 0) {
                        Iterator iterator = typingStatusSet.iterator();
                        TypingStatus status = (TypingStatus) iterator.next();
                        String objectName = status.getTypingContentType();

                        MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                        MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                        //匹配对方正在输入的是文本消息还是语音消息
                        if (objectName.equals(textTag.value())) {
                            mHandler.sendEmptyMessage(SET_TEXT_TYPING_TITLE);
                        } else if (objectName.equals(voiceTag.value())) {
                            mHandler.sendEmptyMessage(SET_VOICE_TYPING_TITLE);
                        }
                    } else {//当前会话没有用户正在输入，标题栏仍显示原来标题
                        mHandler.sendEmptyMessage(SET_TARGET_ID_TITLE);
                    }
                }
            }
        });

        SealAppContext.getInstance().pushActivity(this);

        //CallKit start 2
        RongCallKit.setGroupMemberProvider(new RongCallKit.GroupMembersProvider() {
            @Override
            public ArrayList<String> getMemberList(String groupId, final RongCallKit.OnGroupMembersResult result) {
                getGroupMembersForCall();
                mCallMemberResult = result;
                return null;
            }
        });


        //CallKit end 2


        //发送消息的监听，主要用到发送消息前，判断余额是否充足，充足则允许发送，不充足则弹窗诱导充值
        //发送消息成功后，再调用扣费的请求，扣费有可能出现失败的情况，发送消息进行本地打日志，同时通过融云后台的消息记录与自己后台的数据库记录进行对比，解决纠纷
        RongIM.getInstance().setSendMessageListener(this);

        meIsDarenTv = (TextView) findViewById(R.id.activity_conversation_identity_you_are_daren);
        heIsDarenTv = (TextView) findViewById(R.id.activity_conversation_identity_he_is_daren);
        errorTv = (TextView) findViewById(R.id.activity_conversation_identity_error);

        //只在私聊界面中展示
        if (mConversationType == Conversation.ConversationType.PRIVATE) {
            //请求自己信息，判断自己是否为达人，从而确定收到消息是否可以获得收益
            Disposable disposable1 = HttpManager.getInstance().getUserDetailOne(MeService.getUid(), new HttpManager.ResultCallback<GetUserDetailOneResponse>() {
                @Override
                public void onSuccess(GetUserDetailOneResponse response) {
                    if (ConversationActivity.this.isDestroyed() || ConversationActivity.this.isFinishing()) {
                        return;
                    }
                    if (response != null) {
                        GetUserDetailModelOne model = response.getResult();
                        if (model != null) {
                            if (model.getIdentity() == IDENTITY_TYPE_DAREN) {//身份是达人
                                meIsDarenTv.setVisibility(View.VISIBLE);
                                errorTv.setVisibility(View.GONE);
                            } else {
                                meIsDarenTv.setVisibility(View.GONE);
                                errorTv.setVisibility(View.GONE);
                            }
                        }
                    } else {
//                    NToast.shortToast(ConversationActivity.this, "获取用户信息失败");
                        errorTv.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onError(String errString) {
                    if (ConversationActivity.this.isDestroyed() || ConversationActivity.this.isFinishing()) {
                        return;
                    }
                    if (!TextUtils.isEmpty(errString)) {
//                    NToast.shortToast(ConversationActivity.this, errString);
                    } else {
//                    NToast.shortToast(ConversationActivity.this, "获取用户信息失败");
                    }
                    errorTv.setVisibility(View.VISIBLE);
                }
            });
            //请求对方信息，判断对方是否为达人，从而确定发送消息是否需要赠送私信钥匙
            Disposable disposable2 = HttpManager.getInstance().getUserDetailOne(mTargetId, new HttpManager.ResultCallback<GetUserDetailOneResponse>() {
                @Override
                public void onSuccess(GetUserDetailOneResponse response) {
                    if (ConversationActivity.this.isDestroyed() || ConversationActivity.this.isFinishing()) {
                        return;
                    }
                    if (response != null) {
                        GetUserDetailModelOne model = response.getResult();
                        if (model != null) {
                            if (model.getIdentity() == IDENTITY_TYPE_DAREN) {//身份是达人
                                heIsDarenTv.setVisibility(View.VISIBLE);
                                errorTv.setVisibility(View.GONE);
                            } else {
                                heIsDarenTv.setVisibility(View.GONE);
                                errorTv.setVisibility(View.GONE);
                            }
                        }
                    } else {
//                    NToast.shortToast(ConversationActivity.this, "获取用户信息失败");
                        errorTv.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onError(String errString) {
                    if (ConversationActivity.this.isDestroyed() || ConversationActivity.this.isFinishing()) {
                        return;
                    }
                    if (!TextUtils.isEmpty(errString)) {
                        NToast.shortToast(ConversationActivity.this, errString);
                    } else {
//                    NToast.shortToast(ConversationActivity.this, "获取用户信息失败");
                    }
                    errorTv.setVisibility(View.VISIBLE);
                }
            });
            compositeDisposable.add(disposable1);
            compositeDisposable.add(disposable2);
        }
    }

    /**
     * 设置通告栏的监听
     */
    private void setAnnounceListener() {
        if (fragment != null && fragment instanceof ConversationFragmentEx) {
            ((ConversationFragmentEx) fragment).setOnShowAnnounceBarListener(new ConversationFragmentEx.OnShowAnnounceListener() {
                @Override
                public void onShowAnnounceView(String announceMsg, final String announceUrl) {
                    layout_announce.setVisibility(View.VISIBLE);
                    tv_announce.setText(announceMsg);
                    layout_announce.setClickable(false);
                    if (!TextUtils.isEmpty(announceUrl)) {
                        iv_arrow.setVisibility(View.VISIBLE);
                        layout_announce.setClickable(true);
                        layout_announce.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String str = announceUrl.toLowerCase();
                                if (!TextUtils.isEmpty(str)) {
                                    if (!str.startsWith("http") && !str.startsWith("https")) {
                                        str = "http://" + str;
                                    }
                                    Intent intent = new Intent(RongKitIntent.RONG_INTENT_ACTION_WEBVIEW);
                                    intent.setPackage(v.getContext().getPackageName());
                                    intent.putExtra("url", str);
                                    v.getContext().startActivity(intent);
                                }
                            }
                        });
                    } else {
                        iv_arrow.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    /**
     * 判断是否是 Push 消息，判断是否需要做 connect 操作
     */
    private void isPushMessage(Intent intent) {

        if (intent == null || intent.getData() == null)
            return;
        //push
        if (intent.getData().getScheme().equals("rong") && intent.getData().getQueryParameter("isFromPush") != null) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("isFromPush").equals("true")) {
                //只有收到系统消息和不落地 push 消息的时候，pushId 不为 null。而且这两种消息只能通过 server 来发送，客户端发送不了。
                //RongIM.getInstance().getRongIMClient().recordNotificationEvent(id);
                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                }
                isFromPush = true;
                enterActivity();
            } else if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                }
                if (intent.getData().getPath().contains("conversation/system")) {
                    Intent intent1 = new Intent(mContext, MainActivity.class);
                    intent1.putExtra("systemconversation", true);
                    startActivity(intent1);
                    SealAppContext.getInstance().popAllActivity();
                    return;
                }
                enterActivity();
            } else {
                if (intent.getData().getPath().contains("conversation/system")) {
                    Intent intent1 = new Intent(mContext, MainActivity.class);
                    intent1.putExtra("systemconversation", true);
                    startActivity(intent1);
                    SealAppContext.getInstance().popAllActivity();
                    return;
                }
                enterFragment(mConversationType, mTargetId);
            }

        } else {
            if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enterActivity();
                    }
                }, 300);
            } else {
                enterFragment(mConversationType, mTargetId);
            }
        }
    }


    /**
     * 收到 push 消息后，选择进入哪个 Activity
     * 如果程序缓存未被清理，进入 MainActivity
     * 程序缓存被清理，进入 LoginActivity，重新获取token
     * <p>
     * 作用：由于在 manifest 中 intent-filter 是配置在 ConversationActivity 下面，所以收到消息后点击notifacition 会跳转到 DemoActivity。
     * 以跳到 MainActivity 为例：
     * 在 ConversationActivity 收到消息后，选择进入 MainActivity，这样就把 MainActivity 激活了，当你读完收到的消息点击 返回键 时，程序会退到
     * MainActivity 页面，而不是直接退回到 桌面。
     */
    private void enterActivity() {

        String token = sp.getString("loginToken", "");

        if (token.equals("default")) {
            NLog.e("ConversationActivity push", "push2");
            startActivity(new Intent(ConversationActivity.this, LoginActivity.class));
            SealAppContext.getInstance().popAllActivity();
        } else {
            NLog.e("ConversationActivity push", "push3");
            reconnect(token);
        }
    }

    private void reconnect(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

                Log.e(TAG, "---onTokenIncorrect--");
            }

            @Override
            public void onSuccess(String s) {
                Log.i(TAG, "---onSuccess--" + s);
                NLog.e("ConversationActivity push", "push4");

                if (mDialog != null)
                    mDialog.dismiss();

                enterFragment(mConversationType, mTargetId);

            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
                Log.e(TAG, "---onError--" + e);
                if (mDialog != null)
                    mDialog.dismiss();

                enterFragment(mConversationType, mTargetId);
            }
        });

    }

    private ConversationFragment fragment;

    /**
     * 加载会话页面 ConversationFragmentEx 继承自 ConversationFragment
     *
     * @param mConversationType 会话类型
     * @param mTargetId         会话 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        if (mConversationType.equals(Conversation.ConversationType.SYSTEM)) {

            fragment = new ConversationFragmentSystem();

            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                    .appendQueryParameter("targetId", mTargetId).build();

            fragment.setUri(uri);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //xxx 为你要加载的 id
            transaction.add(R.id.rong_content, fragment);
            transaction.commitAllowingStateLoss();

        } else {

            fragment = new ConversationFragmentEx();

            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                    .appendQueryParameter("targetId", mTargetId).build();

            fragment.setUri(uri);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //xxx 为你要加载的 id
            transaction.add(R.id.rong_content, fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void setTitle(String titleId) {
        super.setTitle(titleId);
        titleTv.setText(titleId);
    }

    /**
     * 设置会话页面 Title
     *
     * @param conversationType 会话类型
     * @param targetId         目标 Id
     */
    private void setActionBarTitle(Conversation.ConversationType conversationType, String targetId) {

        if (conversationType == null)
            return;

        if (conversationType.equals(Conversation.ConversationType.PRIVATE)) {
            setPrivateActionBar(targetId);
        } else if (conversationType.equals(Conversation.ConversationType.GROUP)) {
            setGroupActionBar(targetId);
        } else if (conversationType.equals(Conversation.ConversationType.DISCUSSION)) {
            setDiscussionActionBar(targetId);
        } else if (conversationType.equals(Conversation.ConversationType.CHATROOM)) {
            setTitle(title);
        } else if (conversationType.equals(Conversation.ConversationType.SYSTEM)) {
            setTitle("约会秘书");//自己做的修改
        } else if (conversationType.equals(Conversation.ConversationType.APP_PUBLIC_SERVICE)) {
            setAppPublicServiceActionBar(targetId);
        } else if (conversationType.equals(Conversation.ConversationType.PUBLIC_SERVICE)) {
            setPublicServiceActionBar(targetId);
        } else if (conversationType.equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
            setTitle(R.string.main_customer);
        } else {
            setTitle(R.string.de_actionbar_sub_defult);
        }

    }

    /**
     * 设置群聊界面 ActionBar
     *
     * @param targetId 会话 Id
     */
    private void setGroupActionBar(String targetId) {
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        } else {
            setTitle(targetId);
        }
    }

    /**
     * 设置应用公众服务界面 ActionBar
     */
    private void setAppPublicServiceActionBar(String targetId) {
        if (targetId == null)
            return;

        RongIM.getInstance().getPublicServiceProfile(Conversation.PublicServiceType.APP_PUBLIC_SERVICE
                , targetId, new RongIMClient.ResultCallback<PublicServiceProfile>() {
                    @Override
                    public void onSuccess(PublicServiceProfile publicServiceProfile) {
                        setTitle(publicServiceProfile.getName());
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
    }

    /**
     * 设置公共服务号 ActionBar
     */
    private void setPublicServiceActionBar(String targetId) {

        if (targetId == null)
            return;


        RongIM.getInstance().getPublicServiceProfile(Conversation.PublicServiceType.PUBLIC_SERVICE
                , targetId, new RongIMClient.ResultCallback<PublicServiceProfile>() {
                    @Override
                    public void onSuccess(PublicServiceProfile publicServiceProfile) {
                        setTitle(publicServiceProfile.getName());
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
    }

    /**
     * 设置讨论组界面 ActionBar
     */
    private void setDiscussionActionBar(String targetId) {

        if (targetId != null) {

            RongIM.getInstance().getDiscussion(targetId
                    , new RongIMClient.ResultCallback<Discussion>() {
                        @Override
                        public void onSuccess(Discussion discussion) {
                            setTitle(discussion.getName());
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode e) {
                            if (e.equals(RongIMClient.ErrorCode.NOT_IN_DISCUSSION)) {
                                setTitle(getString(R.string.not_in_discussion_group));
                                supportInvalidateOptionsMenu();
                            }
                        }
                    });
        } else {
            setTitle(getString(R.string.discussion_group));
        }
    }


    /**
     * 设置私聊界面 ActionBar
     */
    private void setPrivateActionBar(String targetId) {
        if (!TextUtils.isEmpty(title)) {
            if (title.equals("null")) {
                if (!TextUtils.isEmpty(targetId)) {
                    UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
                    if (userInfo != null) {
                        setTitle(userInfo.getName());
                    }
                }
            } else {
                setTitle(title);
            }

        } else {
            setTitle(targetId);
        }
    }

    /**
     * 根据 targetid 和 ConversationType 进入到设置页面
     */
    private void enterSettingActivity() {

        if (mConversationType == Conversation.ConversationType.PUBLIC_SERVICE
                || mConversationType == Conversation.ConversationType.APP_PUBLIC_SERVICE) {

            RongIM.getInstance().startPublicServiceProfile(this, mConversationType, mTargetId);
        } else {
            UriFragment fragment = (UriFragment) getSupportFragmentManager().getFragments().get(0);
            //得到讨论组的 targetId
            mTargetId = fragment.getUri().getQueryParameter("targetId");

            if (TextUtils.isEmpty(mTargetId)) {
                NToast.shortToast(mContext, mContext.getString(R.string.discussion_group_not_created));
            }


            Intent intent = null;
            if (mConversationType == Conversation.ConversationType.GROUP) {
                intent = new Intent(this, GroupDetailActivity.class);
                intent.putExtra("conversationType", Conversation.ConversationType.GROUP);
            } else if (mConversationType == Conversation.ConversationType.PRIVATE) {
                intent = new Intent(this, PrivateChatDetailActivity.class);
                intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE);
            } else if (mConversationType == Conversation.ConversationType.DISCUSSION) {
                intent = new Intent(this, DiscussionDetailActivity.class);
                intent.putExtra("TargetId", mTargetId);
                startActivityForResult(intent, 166);
                return;
            }
            intent.putExtra("TargetId", mTargetId);
            if (intent != null) {
                startActivityForResult(intent, 500);
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 501) {
            SealAppContext.getInstance().popAllActivity();
        }
    }

    @Override
    protected void onDestroy() {
        //CallKit start 3
        RongCallKit.setGroupMemberProvider(null);
        //CallKit end 3

        RongIMClient.setTypingStatusListener(null);
        SealAppContext.getInstance().popActivity(this);
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
            if (fragment != null && !fragment.onBackPressed()) {
                if (isFromPush) {
                    isFromPush = false;
                    startActivity(new Intent(this, MainActivity.class));
                    SealAppContext.getInstance().popAllActivity();
                } else {
                    if (fragment.isLocationSharing()) {
                        fragment.showQuitLocationSharingDialog(this);
                        return true;
                    }
                    if (mConversationType.equals(Conversation.ConversationType.CHATROOM)
                            || mConversationType.equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
                        SealAppContext.getInstance().popActivity(this);
                    } else {
                        SealAppContext.getInstance().popActivity(this);
                    }
                }
            }
        }
        return false;
    }

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    //CallKit start 4
    private RongCallKit.OnGroupMembersResult mCallMemberResult;

    private void getGroupMembersForCall() {
        SealUserInfoManager.getInstance().getGroupMembers(mTargetId, new SealUserInfoManager.ResultCallback<List<GroupMember>>() {
            @Override
            public void onSuccess(List<GroupMember> groupMembers) {
                ArrayList<String> userIds = new ArrayList<>();
                if (groupMembers != null) {
                    for (GroupMember groupMember : groupMembers) {
                        if (groupMember != null) {
                            userIds.add(groupMember.getUserId());
                        }
                    }
                }
                mCallMemberResult.onGotMemberList(userIds);
            }

            @Override
            public void onError(String errString) {
                mCallMemberResult.onGotMemberList(null);
            }
        });
    }
    //CallKit end 4

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_conversation_back) {
            if (fragment != null && !fragment.onBackPressed()) {
                if (fragment.isLocationSharing()) {
                    fragment.showQuitLocationSharingDialog(this);
                    return;
                }
                hintKbTwo();
                if (isFromPush) {
                    isFromPush = false;
                    startActivity(new Intent(this, MainActivity.class));
                }
                if (mConversationType.equals(Conversation.ConversationType.CHATROOM)
                        || mConversationType.equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
                    SealAppContext.getInstance().popActivity(this);
                } else {
                    SealAppContext.getInstance().popAllActivity();
                }
            }
        } else {
            enterSettingActivity();
        }
    }

    @Override
    public void onHeadLeftButtonClick(View v) {
        if (fragment != null && !fragment.onBackPressed()) {
            if (fragment.isLocationSharing()) {
                fragment.showQuitLocationSharingDialog(this);
                return;
            }
            hintKbTwo();
            if (isFromPush) {
                isFromPush = false;
                startActivity(new Intent(this, MainActivity.class));
            }
            if (mConversationType.equals(Conversation.ConversationType.CHATROOM)
                    || mConversationType.equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
                SealAppContext.getInstance().popActivity(this);
            } else {
                SealAppContext.getInstance().popAllActivity();
            }
        }
    }

    //自己加的监听，消息发送前的处理，
    //先判断当前余额是否充足，不充足则发送不成功，弹一个窗引导充值，（直接用sp保存的值来判断，而不是通过网络来判断，这样比较快）
    //如果充足，则允许发送，然后再异步调用减钱的操作（减钱过程可能失败，这时候发送要写日志下来，或者到融云里查询这条消息发送记录，与后台数据库进行对比，解除纠纷）
    @Override
    public io.rong.imlib.model.Message onSend(io.rong.imlib.model.Message message) {

        if (MeService.getMyBalance() >= MINUS_BALANCE_ONE_CHAT_MESSAGE) {
            return message;
        } else {
            DialogUtils.showBalanceNotEnoughDialog(this);
        }

        return null;
    }

    @Override
    public boolean onSent(io.rong.imlib.model.Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
        if (message.getSentStatus() == io.rong.imlib.model.Message.SentStatus.FAILED) {
            if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {
                //不在聊天室
            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {
                //不在讨论组
            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {
                //不在群组
            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {
                //你在他的黑名单中
                NToast.shortToast(ConversationActivity.this, "发送失败，您已被对方拉黑");
            }
        }

        //发送成功后，进行减余额处理
        if (message.getSentStatus() == io.rong.imlib.model.Message.SentStatus.SENT) {
            //这个请求，不用再退出activity时注销，减余额操作可以在后台进行
            HttpManager.getInstance().postBalanceCoinsOperationMulti(BALANCE_COINS_TYPE_CHAT_MESSAGE,
                    MINUS_BALANCE_ONE_CHAT_MESSAGE, 0,
                    message.getTargetId(), ADD_BALANCE_ONE_CHAT_MESSAGE, 0,
                    new HttpManager.ResultCallback<BalanceCoinsResponse>() {
                        @Override
                        public void onSuccess(BalanceCoinsResponse balanceCoinsResponse) {
                            if (balanceCoinsResponse == null) {
                                return;
                            }
                            //不管余额是否充足，都更新服务端返回的余额和金币的最新值，并更新本地值
                            BalanceCoinsModel model = balanceCoinsResponse.getResult();
                            if (model == null) {
                                return;
                            }
                            MeService.setMyBalance(model.getBalance());
                            MeService.setMyCoin(model.getCoins());
                            RefreshBalanceCoinsEvent.postEvent(model.getBalance(), model.getCoins());
                            if (balanceCoinsResponse.getCode() == 200) {

                            } else if (balanceCoinsResponse.getCode() == BALANCE_COINS_NOT_ENOUGH_BALANCE) {
                                DialogUtils.showBalanceNotEnoughDialog(ConversationActivity.this);
                            } else if (balanceCoinsResponse.getCode() == 500) {
                                //这种情况，是服务端开启事务插数据库失败的情况，按理说这时候不允许再进行下一步操作
                                //淡入如果是发消息，发成功之后才调用次接口，所以看下情况先
                                //可以让他发成功一次，下次再发则先调用此接口，如果此接口还是失败，则不允许他发
                            }
                        }

                        @Override
                        public void onError(String errString) {
//                        Ln.e("发消息，减余额失败，"+errString);
                        }
                    });
        }

        return false;
    }
}

