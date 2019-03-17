package cn.yunchuang.im.server.response;

import java.util.List;

public class GetUserDetailModelTwo {

    private String weChat; //微信号
    private int weChatPrice; //微信号价格
    private boolean hasPayedWeChat; //是否已经付费，是则直接展示微信号，否则展示查看

    private List<ImageModel> hasPayedImgList; //付费图片列表，是一个json数组
    private List<ImageModel> notPayedImgList; //付费图片列表，是一个json数组

    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public int getWeChatPrice() {
        return weChatPrice;
    }

    public void setWeChatPrice(int weChatPrice) {
        this.weChatPrice = weChatPrice;
    }

    public boolean isHasPayedWeChat() {
        return hasPayedWeChat;
    }

    public void setHasPayedWeChat(boolean hasPayedWeChat) {
        this.hasPayedWeChat = hasPayedWeChat;
    }

    public List<ImageModel> getHasPayedImgList() {
        return hasPayedImgList;
    }

    public void setHasPayedImgList(List<ImageModel> hasPayedImgList) {
        this.hasPayedImgList = hasPayedImgList;
    }

    public List<ImageModel> getNotPayedImgList() {
        return notPayedImgList;
    }

    public void setNotPayedImgList(List<ImageModel> notPayedImgList) {
        this.notPayedImgList = notPayedImgList;
    }
}
