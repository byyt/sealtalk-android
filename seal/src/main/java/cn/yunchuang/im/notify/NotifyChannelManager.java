package cn.yunchuang.im.notify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import java.util.HashSet;

import cn.yunchuang.im.zmico.utils.Utils;


/**
 * Created by liumingkong on 2018/10/17.
 * https://mp.weixin.qq.com/s/Ezfm-Xaz3fzsaSm0TU5LMw
 * Android 8.0 需要设置channel
 * https://www.jianshu.com/p/b529e61d220a
 * https://developer.android.google.cn/training/notify-user/channels.html
 * https://github.com/googlesamples/android-NotificationChannels/#readme
 */
public class NotifyChannelManager {

    private static HashSet<NotifyChannelType> notifyChannels = new HashSet<>();

    public static String getChannelId(Context context, NotifyChannelType notifyChannelType) {
        String channelID = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Utils.isNotNull(notifyChannelType)) {
                if (!notifyChannels.contains(notifyChannelType)) {
                    channelID = String.valueOf(notifyChannelType.value());
                    String channelName = notifyChannelType.name();
                    NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.createNotificationChannel(channel);
                    notifyChannels.add(notifyChannelType);
//                    NotifyLog.d("getChannelId create:" + channelID);
                } else {
                    channelID = String.valueOf(notifyChannelType.value());
                }
            }
        }
        return channelID;
    }

    // 通知的类型
    // https://blog.csdn.net/ein3614/article/details/81633107
    public enum NotifyChannelType {

        MSG(1),
        GROUP(2),
        LIVE_BOARDCAST(3),
        SOCIAL(4),
        CUSTOM(0);

        private final int code;

        NotifyChannelType(int code) {
            this.code = code;
        }

        public int value() {
            return code;
        }


    }
}
