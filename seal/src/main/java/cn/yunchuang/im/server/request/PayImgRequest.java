package cn.yunchuang.im.server.request;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class PayImgRequest {

    private String image;
    private int imgPrice;

    public PayImgRequest(String image, int imgPrice) {
        this.image = image;
        this.imgPrice = imgPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getImgPrice() {
        return imgPrice;
    }

    public void setImgPrice(int imgPrice) {
        this.imgPrice = imgPrice;
    }
}
