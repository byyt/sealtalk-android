package cn.yunchuang.im.notify;

import android.app.NotificationManager;
import android.content.Context;

import cn.yunchuang.im.App;

/**
 * Created by liumingkong on 2018/5/17.
 */

public class NotifyIdsBase {

    // 20以内
    public static final int notifyIdChat = 1;
    public static final int notifyIdLiveBroadcasting = 2;
    public static final int notifyIdPushLink = 3;

    public static final String defaultTag = "defaultTag";

    public static void clearNotify(int notifyId, String notifyTag) {
        NotificationManager notificationManager = (NotificationManager) App.getAppContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notifyTag, notifyId);
    }

    public static void clearAllNotify() {
        NotificationManager notificationManager = (NotificationManager) App.getAppContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
