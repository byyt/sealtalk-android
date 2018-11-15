package cn.yunchuang.im.server.response;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class GetUserDetailOneResponse {

    private int code;

    private GetUserDetailModelOne result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public GetUserDetailModelOne getResult() {
        return result;
    }

    public void setResult(GetUserDetailModelOne result) {
        this.result = result;
    }
}
