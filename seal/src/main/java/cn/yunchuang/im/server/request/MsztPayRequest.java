package cn.yunchuang.im.server.request;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class MsztPayRequest {

    private long msztOrderId;

    public MsztPayRequest(long msztOrderId) {
        this.msztOrderId = msztOrderId;
    }

    public long getMsztOrderId() {
        return msztOrderId;
    }

    public void setMsztOrderId(long msztOrderId) {
        this.msztOrderId = msztOrderId;
    }
}
