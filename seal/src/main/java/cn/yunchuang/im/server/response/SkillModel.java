package cn.yunchuang.im.server.response;

public class SkillModel {

    private int type; //技能类型，客户端服务端统一规定好，就比如性别规定好男是0，女是1一样
    private String name;
    private int price;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
