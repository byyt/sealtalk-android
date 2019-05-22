package cn.yunchuang.im;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.LocaleList;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.facebook.stetho.inspector.database.DefaultDatabaseConnectionProvider;
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsDomain;
import com.hjq.toast.ToastUtils;
import com.mylhyl.circledialog.scale.ScaleLayoutConfig;
import com.previewlibrary.ZoomMediaLoader;
import com.zhouyou.http.EasyHttp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.yunchuang.im.db.Friend;
import cn.yunchuang.im.message.YhmsMessage;
import cn.yunchuang.im.message.provider.ContactNotificationMessageProvider;
import cn.yunchuang.im.message.provider.YhmsMessageProvider;
import cn.yunchuang.im.server.pinyin.CharacterParser;
import cn.yunchuang.im.server.utils.NLog;
import cn.yunchuang.im.server.utils.RongGenerate;
import cn.yunchuang.im.stetho.RongDatabaseDriver;
import cn.yunchuang.im.stetho.RongDatabaseFilesProvider;
import cn.yunchuang.im.stetho.RongDbFilesDumperPlugin;
import cn.yunchuang.im.ui.activity.UserDetailActivity;
import cn.yunchuang.im.utils.SharedPreferencesContext;
import cn.yunchuang.im.widget.ZoomImageLoader;
import io.rong.contactcard.ContactCardExtensionModule;
import io.rong.contactcard.IContactCardClickListener;
import io.rong.contactcard.IContactCardInfoProvider;
import io.rong.contactcard.message.ContactMessage;
import io.rong.imageloader.core.DisplayImageOptions;
import io.rong.imageloader.core.display.FadeInBitmapDisplayer;
import io.rong.imkit.RongConfigurationManager;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.RealTimeLocationMessageProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.ipc.RongExceptionHandler;
import io.rong.imlib.model.UserInfo;
import io.rong.push.RongPushClient;
import io.rong.push.pushconfig.PushConfig;
import io.rong.recognizer.RecognizeExtensionModule;
import io.rong.sight.SightExtensionModule;


public class App extends MultiDexApplication {

    private static DisplayImageOptions options;

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return App.getInstance().getApplicationContext();
    }

    @Override
    public void onCreate() {

        super.onCreate();
        Stetho.initialize(new Stetho.Initializer(this) {
            @Override
            protected Iterable<DumperPlugin> getDumperPlugins() {
                return new Stetho.DefaultDumperPluginsBuilder(App.this)
                        .provide(new RongDbFilesDumperPlugin(App.this, new RongDatabaseFilesProvider(App.this)))
                        .finish();
            }

            @Override
            protected Iterable<ChromeDevtoolsDomain> getInspectorModules() {
                Stetho.DefaultInspectorModulesBuilder defaultInspectorModulesBuilder = new Stetho.DefaultInspectorModulesBuilder(App.this);
                defaultInspectorModulesBuilder.provideDatabaseDriver(new RongDatabaseDriver(App.this, new RongDatabaseFilesProvider(App.this), new DefaultDatabaseConnectionProvider()));
                return defaultInspectorModulesBuilder.finish();
            }
        });

        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {

//            LeakCanary.install(this);//内存泄露检测
            PushConfig config = new PushConfig
                    .Builder()
                    .enableHWPush(true)
                    .enableMiPush("2882303761517473625", "5451747338625")
                    .enableMeiZuPush("112988", "2fa951a802ac4bd5843d694517307896")
                    .enableVivoPush(true)
                    .enableFCM(true)
                    .build();
            RongPushClient.setPushConfig(config);
            /**
             * 注意：
             *
             * IMKit SDK调用第一步 初始化
             *
             * context上下文
             *
             * 只有两个进程需要初始化，主进程和 push 进程
             */
            RongIM.setServerInfo("nav.cn.ronghub.com", "up.qbox.me");
            RongIM.init(this);
            NLog.setDebug(true);//Seal Module Log 开关
            SealAppContext.init(this);
            SharedPreferencesContext.init(this);
            Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));

            try {
                RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
                RongIM.registerMessageTemplate(new RealTimeLocationMessageProvider());
//                RongIM.registerMessageType(TestMessage.class);
//                RongIM.registerMessageTemplate(new TestMessageProvider());
                RongIM.registerMessageType(YhmsMessage.class);
                RongIM.registerMessageTemplate(new YhmsMessageProvider());


            } catch (Exception e) {
                e.printStackTrace();
            }
            openSealDBIfHasCachedToken();
            RongIM.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
                @Override
                public void onChanged(ConnectionStatus status) {
                    if (status == ConnectionStatus.TOKEN_INCORRECT) {
                        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                        final String cacheToken = sp.getString("loginToken", "");
                        if (!TextUtils.isEmpty(cacheToken)) {
                            RongIM.connect(cacheToken, SealAppContext.getInstance().getConnectCallback());
                        } else {
                            Log.e("seal", "token is empty, can not reconnect");
                        }
                    }
                }
            });

            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(cn.yunchuang.im.R.drawable.de_default_portrait)
                    .showImageOnFail(cn.yunchuang.im.R.drawable.de_default_portrait)
                    .showImageOnLoading(cn.yunchuang.im.R.drawable.de_default_portrait)
                    .displayer(new FadeInBitmapDisplayer(300))
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();

//            RongExtensionManager.getInstance().registerExtensionModule(new PTTExtensionModule(this, true, 1000 * 60));
            RongExtensionManager.getInstance().registerExtensionModule(new ContactCardExtensionModule(new IContactCardInfoProvider() {
                @Override
                public void getContactAllInfoProvider(final IContactCardInfoCallback contactInfoCallback) {
                    SealUserInfoManager.getInstance().getFriends(new SealUserInfoManager.ResultCallback<List<Friend>>() {
                        @Override
                        public void onSuccess(List<Friend> friendList) {
                            contactInfoCallback.getContactCardInfoCallback(friendList);
                        }

                        @Override
                        public void onError(String errString) {
                            contactInfoCallback.getContactCardInfoCallback(null);
                        }
                    });
                }

                @Override
                public void getContactAppointedInfoProvider(String userId, String name, String portrait, final IContactCardInfoCallback contactInfoCallback) {
                    SealUserInfoManager.getInstance().getFriendByID(userId, new SealUserInfoManager.ResultCallback<Friend>() {
                        @Override
                        public void onSuccess(Friend friend) {
                            List<UserInfo> list = new ArrayList<>();
                            list.add(friend);
                            contactInfoCallback.getContactCardInfoCallback(list);
                        }

                        @Override
                        public void onError(String errString) {
                            contactInfoCallback.getContactCardInfoCallback(null);
                        }
                    });
                }

            }, new IContactCardClickListener() {
                @Override
                public void onContactCardClick(View view, ContactMessage content) {
                    Intent intent = new Intent(view.getContext(), UserDetailActivity.class);
                    Friend friend = SealUserInfoManager.getInstance().getFriendByID(content.getId());
                    if (friend == null) {
                        UserInfo userInfo = new UserInfo(content.getId(), content.getName(),
                                Uri.parse(TextUtils.isEmpty(content.getImgUrl()) ? RongGenerate.generateDefaultAvatar(content.getName(), content.getId()) : content.getImgUrl()));
                        friend = CharacterParser.getInstance().generateFriendFromUserInfo(userInfo);
                    }
                    intent.putExtra("friend", friend);
                    view.getContext().startActivity(intent);
                }
            }));
            RongExtensionManager.getInstance().registerExtensionModule(new RecognizeExtensionModule());
            //小视频
            RongExtensionManager.getInstance().registerExtensionModule(new SightExtensionModule());

            //网络框架，主要用来上传下载
            EasyHttp.init(this);//默认初始化

            //自定义的网络类，用在fragment中做请求
            HttpManager.init(this);

            //图片浏览框架
            ZoomMediaLoader.getInstance().init(new ZoomImageLoader());

            //对话框，先初始化这个类，方便将尺寸换回原始的
            ScaleLayoutConfig.init(this);

            //吐司Toast
            ToastUtils.init(this);
        }

        instance = this;

    }

    public static DisplayImageOptions getOptions() {
        return options;
    }

    private void openSealDBIfHasCachedToken() {
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        String cachedToken = sp.getString("loginToken", "");
        if (!TextUtils.isEmpty(cachedToken)) {
            String current = getCurProcessName(this);
            String mainProcessName = getPackageName();
            if (mainProcessName.equals(current)) {
                SealUserInfoManager.getInstance().openDB();
            }
        }
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        Context context = RongConfigurationManager.getInstance().getConfigurationContext(base);
        super.attachBaseContext(context);
    }

    public static Resources getAppResources() {
        return instance.getResources();
    }

    public static void updateApplicationLanguage() {
        if (instance == null) return;

        Resources resources = instance.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        Locale locale = RongConfigurationManager.getInstance().getAppLocale(instance).toLocale();
        config.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
        }
        resources.updateConfiguration(config, dm);
    }

}
