package cn.yunchuang.im.event;


import org.greenrobot.eventbus.EventBus;

public class RefreshBalanceCoinsEvent {


    private double balance;

    private long coins;

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

    public RefreshBalanceCoinsEvent(double balance, long coins) {
        this.balance = balance;
        this.coins = coins;
    }

    public static void postEvent(double balance, long coins) {
        RefreshBalanceCoinsEvent event = new RefreshBalanceCoinsEvent(balance, coins);
        EventBus.getDefault().post(event);
    }
}
