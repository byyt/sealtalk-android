package cn.yunchuang.im.server.response;

public class GetUserDetailModelOne {
    private String id;
    private String nickname;
    private String portraitUri;
    private int sex;
    private int height;
    private long birthday;//出生日期，时间戳
    private int age;//年龄
    private double distance; //目标用户与自己的距离，这个值服务端是通过目标用户的经纬度与自己的经纬度来计算距离，返回的值
    private String location; //定位信息，暂时用不到
    private String suoZaiDi; //所在地
    private int feedback_rate;
    private int followNum;
    private int fansNum;
    private String qianMing;
    private String xqah;
    private String freeImgList; //免费图片列表，是一个json数组的字符串
    private String skills;//技能，是一个json数组的字符串
    private int identity;//身份，0：普通用户，1：达人

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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSuoZaiDi() {
        return suoZaiDi;
    }

    public void setSuoZaiDi(String suoZaiDi) {
        this.suoZaiDi = suoZaiDi;
    }

    public int getFeedback_rate() {
        return feedback_rate;
    }

    public void setFeedback_rate(int feedback_rate) {
        this.feedback_rate = feedback_rate;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public String getQianMing() {
        return qianMing;
    }

    public void setQianMing(String qianMing) {
        this.qianMing = qianMing;
    }

    public String getXqah() {
        return xqah;
    }

    public void setXqah(String xqah) {
        this.xqah = xqah;
    }

    public String getFreeImgList() {
        return freeImgList;
    }

    public void setFreeImgList(String freeImgList) {
        this.freeImgList = freeImgList;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

}
