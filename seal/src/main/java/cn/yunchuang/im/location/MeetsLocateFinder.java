package cn.yunchuang.im.location;


import cn.yunchuang.im.zmico.utils.Utils;

/**
 * Created by liumingkong on 14-7-20.
 */
public class MeetsLocateFinder {

    private GaodeLocateFinder gaodeLocateFinder;

    public MeetsLocateFinder() {
        this.gaodeLocateFinder = new GaodeLocateFinder();
    }

    public synchronized boolean requestLocate() {
        boolean isStart = false;
        if (!isRunning()) {
            synchronized (this) {
                if (!isRunning()) {
                    this.gaodeLocateFinder.startLocate();
                    isStart = true;
                }
            }
        }
        return isStart;
    }

    public boolean isRunning() {
        return gaodeLocateFinder.isRunning();
    }

    public void closeLocate() {
        if (!Utils.isNull(gaodeLocateFinder)) {
            gaodeLocateFinder.stopLocate();
        }
    }
}
