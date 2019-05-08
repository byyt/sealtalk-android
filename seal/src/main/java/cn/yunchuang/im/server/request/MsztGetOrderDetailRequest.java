package cn.yunchuang.im.server.request;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class MsztGetOrderDetailRequest {

    private String msztOrderNum; //这里传的是订单号，如果命名为msztOrderId，带了Id字样后段会进行解密，我也不知道为什么，所以这个字段取这个名字

    public MsztGetOrderDetailRequest(String msztOrderNum) {
        this.msztOrderNum = msztOrderNum;
    }

    public String getMsztOrderNum() {
        return msztOrderNum;
    }

    public void setMsztOrderNum(String msztOrderNum) {
        this.msztOrderNum = msztOrderNum;
    }
}
