package cn.yunchuang.im.notify;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;



/**
 * Created by liumingkong on 15/10/29.
 */
public class NotifyRingUtils {

    private volatile static boolean isRing = true;

    // 响铃的限制
    private static boolean isShowNotifyRing() {
        boolean ringFlag = isRing;
        if (isRing) {
            isRing = false;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
//                        Ln.e(e);
                    }
                    isRing = true;
                }
            }).start();
        }
        return ringFlag;
    }

    // 是否响铃和振动，会判断优先勿扰时间
    public static boolean isRingChat(Context context) {
        boolean isRing = true;
//        if (!SwitchPref.isNoAlert()) { // 是否是勿扰时段
//            isRing = SwitchPref.getNotification(SwitchPref.TAG_NOTIFICATION_SOUND);
//            if (SwitchPref.getNotification(SwitchPref.TAG_NOTIFICATION_VIBRATE)) {
//                startVibrator(context, 25);
//            }
//        } else {
//            Ln.d("isRingChat:属于勿扰时段");
//        }
//        if (isRing) {
//            isRing = isShowNotifyRing();
//        }
        return isRing;
    }

    /**
     * 增加振动效果
     */
    public static void startVibrator(Context context, long vibratorTime) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(vibratorTime);
    }
}
