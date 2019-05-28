package cn.yunchuang.im.server.request;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class WdqbBalanceCoinsMultiRequest {


    private int type;
    private double myBalance;
    private long myCoins;
    private String otherUserId;
    private double otherBalance;
    private long otherCoins;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getMyBalance() {
        return myBalance;
    }

    public void setMyBalance(double myBalance) {
        this.myBalance = myBalance;
    }

    public long getMyCoins() {
        return myCoins;
    }

    public void setMyCoins(long myCoins) {
        this.myCoins = myCoins;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public double getOtherBalance() {
        return otherBalance;
    }

    public void setOtherBalance(double otherBalance) {
        this.otherBalance = otherBalance;
    }

    public long getOtherCoins() {
        return otherCoins;
    }

    public void setOtherCoins(long otherCoins) {
        this.otherCoins = otherCoins;
    }
}
