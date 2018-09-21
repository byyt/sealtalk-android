package cn.yunchuang.im.server.request;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class CodeLoginRequest {

    private String region;
    private String phone;
    private String verification_token;

    public CodeLoginRequest(String region, String phone, String verification_token) {
        this.region = region;
        this.phone = phone;
        this.verification_token = verification_token;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerification_token() {
        return verification_token;
    }

    public void setVerification_token(String verification_token) {
        this.verification_token = verification_token;
    }
}
