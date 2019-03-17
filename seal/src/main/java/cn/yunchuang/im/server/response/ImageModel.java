package cn.yunchuang.im.server.response;

public class ImageModel {

    private int id; //付费图片有id，免费图片没有
    private String imgUrl;
    private int imgPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getImgPrice() {
        return imgPrice;
    }

    public void setImgPrice(int imgPrice) {
        this.imgPrice = imgPrice;
    }
}
