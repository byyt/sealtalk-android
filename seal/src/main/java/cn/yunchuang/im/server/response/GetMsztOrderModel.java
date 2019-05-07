package cn.yunchuang.im.server.response;

public class GetMsztOrderModel {
    private String MsztOrderId;
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

    public String getMsztOrderId() {
        return MsztOrderId;
    }

    public void setMsztOrderId(String msztOrderId) {
        MsztOrderId = msztOrderId;
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
}
