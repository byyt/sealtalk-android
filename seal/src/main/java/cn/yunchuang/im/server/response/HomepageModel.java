package cn.yunchuang.im.server.response;

public class HomepageModel {
    private String id;
    private String nickname;
    private String portraitUri;
    private int sex;
    private int age;
    private String location;
    private int feedback_rate;
    private String qianMing;
    private String freeImgList; //免费图片列表，是一个json数组的字符串


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getFeedback_rate() {
        return feedback_rate;
    }

    public void setFeedback_rate(int feedback_rate) {
        this.feedback_rate = feedback_rate;
    }

    public String getQianMing() {
        return qianMing;
    }

    public void setQianMing(String qianMing) {
        this.qianMing = qianMing;
    }

    public String getFreeImgList() {
        return freeImgList;
    }

    public void setFreeImgList(String freeImgList) {
        this.freeImgList = freeImgList;
    }
}
