package cn.yunchuang.im.server.response;

public class GetWdyhOrderLbModel extends GetWdyhBaseOrderLbModel {
    private String wdyhOrderId;
    private String payUserIdStr;
    private String receiveUserIdStr;
    private int status;
    private String yyxm;
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

    private GetUserDetailModelOne PayUser;
    private GetUserDetailModelOne receiveUser;

    public String getWdyhOrderId() {
        return wdyhOrderId;
    }

    public void setWdyhOrderId(String wdyhOrderId) {
        this.wdyhOrderId = wdyhOrderId;
    }

    public String getPayUserIdStr() {
        return payUserIdStr;
    }

    public void setPayUserIdStr(String payUserIdStr) {
        this.payUserIdStr = payUserIdStr;
    }

    public String getReceiveUserIdStr() {
        return receiveUserIdStr;
    }

    public void setReceiveUserIdStr(String receiveUserIdStr) {
        this.receiveUserIdStr = receiveUserIdStr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getYyxm() {
        return yyxm;
    }

    public void setYyxm(String yyxm) {
        this.yyxm = yyxm;
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

    public GetUserDetailModelOne getPayUser() {
        return PayUser;
    }

    public void setPayUser(GetUserDetailModelOne payUser) {
        PayUser = payUser;
    }

    public GetUserDetailModelOne getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(GetUserDetailModelOne receiveUser) {
        this.receiveUser = receiveUser;
    }
}
