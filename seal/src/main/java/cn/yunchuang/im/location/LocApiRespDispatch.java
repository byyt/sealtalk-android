package cn.yunchuang.im.location;

import android.util.Log;

import cn.yunchuang.im.event.RefreshLocationEvent;
import cn.yunchuang.im.zmico.utils.Utils;

/**
 * Created by liumingkong on 2017/6/12.
 */

public class LocApiRespDispatch implements Runnable {

    private LocateApiResp locateResponse;

    public LocApiRespDispatch(LocateApiResp locateResponse) {
        this.locateResponse = locateResponse;
    }

    @Override
    public void run() {
        if (Utils.isNull(locateResponse)) return;
        onLocateResponse();
    }

    private void onLocateResponse() {
        if (locateResponse.flag) {
            LocationVO locationVO = new LocationVO();
            locationVO.setLatitude(locateResponse.latitude);
            locationVO.setLongitude(locateResponse.longitude);
            locationVO.setCity(locateResponse.city);
            // 定位sdk的结果存到本地，并上传到服务器，前提是与旧位置距离超过500米
            LocateManager.INSTANCE.updateLocalLocationAndPost(locationVO);
        } else {
            Log.d("xxxxxx", "定位失败1:" + locateResponse.errorInfo);
            RefreshLocationEvent.postEvent(null, false,
                    LocateReqManager.currentLocateRequest.getLoacateSender());
        }
    }

}
