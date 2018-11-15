package cn.yunchuang.im.server.request;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class PayImgRequest {

    private String imgId;

    public PayImgRequest(String imgId) {
        this.imgId = imgId;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
}
