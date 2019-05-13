package cn.yunchuang.im.server.request;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class WdyhUpdateOrderStatusRequest {

    private String wdyhOrderNum; //这里传的是订单号，如果命名为wdyhOrderId，带了Id字样后段会进行解密，我也不知道为什么，所以这个字段取这个名字
    private int status;
    private long yfkTs;
    private long jsTs;
    private long fqkTs;
    private long qrTs;
    private long pjTs;
    private long wcTs;
    private long zzTs;
    private long wjstkTs;
    private long wfqktkTs;
    private long jftkTs;

    public String getWdyhOrderNum() {
        return wdyhOrderNum;
    }

    public void setWdyhOrderNum(String wdyhOrderNum) {
        this.wdyhOrderNum = wdyhOrderNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getYfkTs() {
        return yfkTs;
    }

    public void setYfkTs(long yfkTs) {
        this.yfkTs = yfkTs;
    }

    public long getJsTs() {
        return jsTs;
    }

    public void setJsTs(long jsTs) {
        this.jsTs = jsTs;
    }

    public long getFqkTs() {
        return fqkTs;
    }

    public void setFqkTs(long fqkTs) {
        this.fqkTs = fqkTs;
    }

    public long getQrTs() {
        return qrTs;
    }

    public void setQrTs(long qrTs) {
        this.qrTs = qrTs;
    }

    public long getPjTs() {
        return pjTs;
    }

    public void setPjTs(long pjTs) {
        this.pjTs = pjTs;
    }

    public long getWcTs() {
        return wcTs;
    }

    public void setWcTs(long wcTs) {
        this.wcTs = wcTs;
    }

    public long getZzTs() {
        return zzTs;
    }

    public void setZzTs(long zzTs) {
        this.zzTs = zzTs;
    }

    public long getWjstkTs() {
        return wjstkTs;
    }

    public void setWjstkTs(long wjstkTs) {
        this.wjstkTs = wjstkTs;
    }

    public long getWfqktkTs() {
        return wfqktkTs;
    }

    public void setWfqktkTs(long wfqktkTs) {
        this.wfqktkTs = wfqktkTs;
    }

    public long getJftkTs() {
        return jftkTs;
    }

    public void setJftkTs(long jftkTs) {
        this.jftkTs = jftkTs;
    }
}
