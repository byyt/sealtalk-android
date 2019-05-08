package cn.yunchuang.im.server.request;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class MsztGetOrderDetailRequest {

    private String msztOrderId;

    public MsztGetOrderDetailRequest(String msztOrderId) {
        this.msztOrderId = msztOrderId;
    }

    public String getMsztOrderId() {
        return msztOrderId;
    }

    public void setMsztOrderId(String msztOrderId) {
        this.msztOrderId = msztOrderId;
    }
}
