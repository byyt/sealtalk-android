package cn.yunchuang.im.server.request;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class MsztCreateOrderRequest {

    private String receiveUserId;
    private int status;
    private long yysj;
    private int yysc;
    private double longitude;
    private double latitude;
    private String yydd;
    private double advancePayment;
    private double totalPayment;
    private int zffs;
    private long yfkTs;
    private long jsTs;
    private long qrTs;
    private long zzTs;
    private long wjstkTs;
    private long wfqktkTs;
    private long jftkTs;

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getYysj() {
        return yysj;
    }

    public void setYysj(long yysj) {
        this.yysj = yysj;
    }

    public int getYysc() {
        return yysc;
    }

    public void setYysc(int yysc) {
        this.yysc = yysc;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getYydd() {
        return yydd;
    }

    public void setYydd(String yydd) {
        this.yydd = yydd;
    }

    public double getAdvancePayment() {
        return advancePayment;
    }

    public void setAdvancePayment(double advancePayment) {
        this.advancePayment = advancePayment;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public int getZffs() {
        return zffs;
    }

    public void setZffs(int zffs) {
        this.zffs = zffs;
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

    public long getQrTs() {
        return qrTs;
    }

    public void setQrTs(long qrTs) {
        this.qrTs = qrTs;
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
