package cn.yunchuang.im.location;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import cn.yunchuang.im.SealConst;
import cn.yunchuang.im.zmico.utils.Utils;


/**
 * Created by liumingkong on 14-5-26.
 */
public class LocationService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        LocateManager.INSTANCE.initLocate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!Utils.isNull(intent)) {
            boolean isForceUpdate = intent.getBooleanExtra(SealConst.LASTUPDATE, false);
            LocateManager.INSTANCE.dispatchApiLocReq(isForceUpdate);
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        LocateManager.INSTANCE.stopLocate();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
