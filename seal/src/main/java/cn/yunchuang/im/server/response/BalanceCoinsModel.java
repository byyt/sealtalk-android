package cn.yunchuang.im.server.response;

public class BalanceCoinsModel {

    private double balance;//余额
    private long coins;//金币

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }
}
