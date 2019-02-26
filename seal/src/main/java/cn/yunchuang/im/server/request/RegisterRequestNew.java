package cn.yunchuang.im.server.request;


/**
 * Created by AMing on 15/12/23.
 * Company RongCloud
 */
public class RegisterRequestNew {


    private String nickname;

    private String password;

    private String verification_token;

    private int sex;

    public RegisterRequestNew(String nickname, String password,
                              String verification_token,int sex) {
        this.nickname = nickname;
        this.password = password;
        this.verification_token = verification_token;
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerification_token() {
        return verification_token;
    }

    public void setVerification_token(String verification_token) {
        this.verification_token = verification_token;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
