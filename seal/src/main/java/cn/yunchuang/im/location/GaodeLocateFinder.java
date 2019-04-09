package cn.yunchuang.im.location;


import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.Timer;
import java.util.TimerTask;

import cn.yunchuang.im.App;
import cn.yunchuang.im.constants.TimeConstants;
import cn.yunchuang.im.zmico.utils.Utils;


/**
 * Created by liumingkong on 2017/3/13.
 */

public class GaodeLocateFinder implements AMapLocationListener {

    private AMapLocationClient client;
    private Timer timer;

    public void startLocate() {
        //启动定位之后，30秒后自动停止定位
        this.timer = new Timer(true);
        timer.schedule(new TimerTask() {
            public void run() {
                stopLocate();
                LocateApiResp.sendLocateFail("Locate Gaode timeout");
            }
        }, TimeConstants.SEC_30);

        try {
            GaodeKeyUtils.initAMapLocationClient();
            this.client = new AMapLocationClient(App.getAppContext());
            client.setLocationOption(prepareOptions());
            client.setLocationListener(this);
            client.startLocation();
        } catch (Throwable e) {
            e.printStackTrace();
            stopLocate();
            LocateApiResp.sendLocateFail("Locate Gaode start fail res error" + e);
        }

    }

    public boolean isRunning() {
        return !Utils.isNull(client);
    }

    // 关闭定位服务
    public void stopLocate() {
        try {
            if (!Utils.isNull(client)) {
                client.onDestroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client = null;
        }
        try {
            if (!Utils.isNull(timer)) {
                timer.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            timer = null;
        }
    }

    private AMapLocationClientOption prepareOptions() {
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        option.setInterval(2000);
        option.setKillProcess(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        option.setHttpTimeOut(30000);
        option.setLocationCacheEnable(false);
        return option;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        this.stopLocate();//只定位成功一次，就关闭了定位服务
        Log.d("xxxxxx",amapLocation.getLatitude()+"");
        Log.d("xxxxxx",amapLocation.getLongitude()+"");
        Log.d("xxxxxx",amapLocation.getAddress()+"");
        if (!Utils.isNull(amapLocation)) {
            if (amapLocation.getErrorCode() == 0) {
                LocateApiResp.sendLocateSucc(amapLocation.getLatitude(), amapLocation.getLongitude());
            } else {
                LocateApiResp.sendLocateFail("Locate Gaode onRecv Fail locType:" + amapLocation.getErrorCode());
            }
        } else {
            LocateApiResp.sendLocateFail("Locate Gaode onRecv location is null");
        }
    }
}
