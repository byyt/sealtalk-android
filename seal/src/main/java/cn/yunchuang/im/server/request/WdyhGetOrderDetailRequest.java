package cn.yunchuang.im.server.request;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class WdyhGetOrderDetailRequest {

    private String wdyhOrderNum; //这里传的是订单号，如果命名为wdyhOrderId，带了Id字样后段会进行解密，我也不知道为什么，所以这个字段取这个名字

    public WdyhGetOrderDetailRequest(String wdyhOrderNum) {
        this.wdyhOrderNum = wdyhOrderNum;
    }

    public String getWdyhOrderNum() {
        return wdyhOrderNum;
    }

    public void setWdyhOrderNum(String wdyhOrderNum) {
        this.wdyhOrderNum = wdyhOrderNum;
    }

}
