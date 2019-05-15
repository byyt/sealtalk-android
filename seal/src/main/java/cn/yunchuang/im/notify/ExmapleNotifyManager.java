package cn.yunchuang.im.notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.yunchuang.im.ui.activity.MyBaseInfoActivity_new;
import cn.yunchuang.im.ui.activity.WdyhDetailActivity;

public class ExmapleNotifyManager extends NotifyManager {

    static final String TAG = "BackToRoomNotifyManager";

    private static volatile ExmapleNotifyManager instance = null;

    private ExmapleNotifyManager() {
    }

    public static ExmapleNotifyManager getInstance() {
        if (instance == null) {
            synchronized (ExmapleNotifyManager.class) {
                if (instance == null) {
                    instance = new ExmapleNotifyManager();
                }
            }
        }

        return instance;
    }


    public void notify(Context context,String content,String msztOrderId) {
        Intent intent = new Intent(context, WdyhDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);


        Bundle bundle = new Bundle();
        bundle.putString("msztOrderId", msztOrderId);
        intent.putExtras(bundle);

        String notifyTitle = "您有一条新的消息";

        String notifyContent = content;

        NotifyInfo notifyInfo = new NotifyInfo();
        notifyInfo.setPushBodyNotify(MicoNotifyIds.notifyIdTest, NotifyIdsBase.defaultTag,
                notifyContent, notifyTitle, notifyContent, false, NotifyChannelManager.NotifyChannelType.CUSTOM);
        notifyInfo.setOngoing(false);
        notifyInfo.setPriority(Notification.PRIORITY_HIGH);
        notifyInfo.setRequestCode(0);
        showNotifyCommon(notifyInfo, intent);

    }

    public void cancel(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NotifyIdsBase.defaultTag, MicoNotifyIds.notifyIdTest);
    }
}
