package cn.yunchuang.im.server.response;

import java.util.List;

public class GetUserDetailModel {
    private String id;
    private String phone;
    private String nickname;
    private String portraitUri;
    private String freeImgList; //免费图片列表，是一个json数组的字符串
    private List<ImageModel> payImgList; //付费图片列表，是一个json数组

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    public String getFreeImgList() {
        return freeImgList;
    }

    public void setFreeImgList(String freeImgList) {
        this.freeImgList = freeImgList;
    }

    public List<ImageModel> getPayImgList() {
        return payImgList;
    }

    public void setPayImgList(List<ImageModel> payImgList) {
        this.payImgList = payImgList;
    }
}
