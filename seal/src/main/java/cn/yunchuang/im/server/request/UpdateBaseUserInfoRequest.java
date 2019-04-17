package cn.yunchuang.im.server.request;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class UpdateBaseUserInfoRequest {

    private String nickname;
    private String portraitUri;
    private int sex;
    private int height;
    private long birthday;
    private int age;
    private String suoZaiDi;
    private String qianMing;
    private String xqah;
    private String freeImgList; //免费图片列表，是一个json数组的字符串

    public UpdateBaseUserInfoRequest(String nickname, String portraitUri, int sex, int height,
                                     long birthday, int age, String suoZaiDi, String qianMing, String xqah,
                                     String freeImgList) {
        this.nickname = nickname;
        this.portraitUri = portraitUri;
        this.sex = sex;
        this.height = height;
        this.birthday = birthday;
        this.age = age;
        this.suoZaiDi = suoZaiDi;
        this.qianMing = qianMing;
        this.xqah = xqah;
        this.freeImgList = freeImgList;
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

    public String getSuoZaiDi() {
        return suoZaiDi;
    }

    public void setSuoZaiDi(String suoZaiDi) {
        this.suoZaiDi = suoZaiDi;
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
}
