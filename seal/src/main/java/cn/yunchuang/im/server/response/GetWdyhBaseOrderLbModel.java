package cn.yunchuang.im.server.response;

public class GetWdyhBaseOrderLbModel {

    private int orderType;//订单类的区分，1：马上租Ta类型订单，2：马上发布类型订单

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
