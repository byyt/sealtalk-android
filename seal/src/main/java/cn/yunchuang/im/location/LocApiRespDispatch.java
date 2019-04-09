package cn.yunchuang.im.location;

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
            changeLocateApiType(locateResponse);// 更换地理位置
        }
    }

    private void changeLocateApiType(LocateApiResp locateResponse) {
        LocationVO locationVO = new LocationVO();
        locationVO.setLatitude(locateResponse.latitude);
        locationVO.setLongitude(locateResponse.longitude); // 缓存并上传地理位置
        LocateManager.INSTANCE.cacheLocationVO = locationVO;

        LocateManager.INSTANCE.updateLocalLocationAndPost(locationVO); // 定位sdk的结果上传地理位置
    }
}
