package cn.yunchuang.im.server.request;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class PayWeChatRequest {

    private String weChat;
    private int weChatPrice;

    public PayWeChatRequest(String weChat, int weChatPrice) {
        this.weChat = weChat;
        this.weChatPrice = weChatPrice;
    }

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
}
