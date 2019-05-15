package cn.yunchuang.im.notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;



import java.util.List;

import cn.yunchuang.im.App;
import cn.yunchuang.im.R;
import cn.yunchuang.im.zmico.utils.Utils;


/**
 * Created by liumingkong on 2018/5/17.
 */

public class NotifyManager {


    protected static void showNotifyCommon(NotifyInfo notifyInfo, Intent notificationIntent) {
        showNotifyCommon(notifyInfo, notificationIntent, null, null);
    }

    protected static void showNotifyCommon(NotifyInfo notifyInfo, Intent notificationIntent, Bitmap largeIcon, Bitmap bigPicture) {
        try {
            Context context = App.getAppContext();
            PendingIntent contentIntent = PendingIntent
                    .getActivity(context, notifyInfo.getRequestCode(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification;
            notification = getNotification(context, notifyInfo.getNotifyTitle(),
                    notifyInfo.getNotifyContent(), largeIcon, notifyInfo.getNotifyTicker(), contentIntent, bigPicture, notifyInfo.isOngoing(), notifyInfo.getPriority(), null, notifyInfo.getNotifyChannelType());
            if (!Utils.isNull(notification)) {
                notification.flags |= Notification.FLAG_SHOW_LIGHTS; // 添加LED灯提醒
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                if (NotifyRingUtils.isRingChat(context)) {
                    notification.defaults |= Notification.DEFAULT_SOUND; // 声音
                }
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notifyInfo.getNotifyTag(), notifyInfo.getNotifyId(), notification);
            }
        } catch (Throwable throwable) {
//            Ln.e(throwable);
            throwable.printStackTrace();
        }
    }

    // 生成通知的模板
    protected static Notification getNotification(Context context,
                                                  CharSequence notifyTitle,
                                                  CharSequence notifyContent,
                                                  Bitmap largeIcon,
                                                  CharSequence notifyTicker,
                                                  PendingIntent contentIntent,
                                                  Bitmap bigPicure,
                                                  boolean ongoing,
                                                  int priority,
                                                  List<NotificationCompat.Action> actions,
                                                  NotifyChannelManager.NotifyChannelType notifyChannelType) {
        Notification notification = null;
        try {
            if (Utils.isNull(largeIcon)) {
//                largeIcon = NotifyImageUtils.decodeResource(context.getResources(), RouterUtils.getAppNotifyLargeImage());
                largeIcon = NotifyImageUtils.decodeResource(context.getResources(), R.drawable.seal_app_logo);
            }
            NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context)
                    .setAutoCancel(true)  // 点击后自动清除Notification
//                    .setSmallIcon(RouterUtils.getAppNotifySmallImage()) // 小图标
                    .setSmallIcon(R.drawable.seal_app_logo) // 小图标
                    .setContentTitle(notifyTitle) // 通知栏标题
                    .setContentText(notifyContent)  // 通知栏内容
                    .setTicker(notifyTicker)      //状态栏显示的通知文本提示
                    .setContentIntent(contentIntent)
                    .setWhen(System.currentTimeMillis()) //通知产生的时间，会在通知信息里显示
                    .setOngoing(ongoing); //通知栏是否常驻

            // 设置8.0兼容的通道
            String channelID = NotifyChannelManager.getChannelId(context, notifyChannelType);
            if (!Utils.isEmptyString(channelID)) {
                notifyBuilder.setChannelId(channelID);
            }

            if (!Utils.isNull(largeIcon)) {
                notifyBuilder.setLargeIcon(largeIcon);
            }
            if (!Utils.isEmptyCollection(actions)) {
                for (NotificationCompat.Action action : actions) {
                    notifyBuilder.addAction(action);
                }
            }
            if (!Utils.isNull(bigPicure)) {
                NotificationCompat.BigPictureStyle pictureStyle = new NotificationCompat.BigPictureStyle();

                pictureStyle.setBigContentTitle(notifyTitle)
                        .setSummaryText(notifyContent)
                        .bigLargeIcon(largeIcon)
                        .bigPicture(bigPicure);
                notifyBuilder.setStyle(pictureStyle);
            }
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    notifyBuilder.setPriority(priority);
                }
            } catch (Throwable e) {
//                Ln.e(e);
            }

            notification = notifyBuilder.build();
            notification.ledARGB = Color.BLUE;
            notification.ledOnMS = 5000;

        } catch (OutOfMemoryError error) {
//            Ln.e(error);
            error.printStackTrace();
        } catch (Throwable e) {
//            Ln.e(e);
            e.printStackTrace();
        }
        return notification;
    }

}
