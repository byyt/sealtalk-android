package cn.yunchuang.im.server.response;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class GetUserDetailTwoResponse {

    private int code;

    private GetUserDetailModelTwo result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public GetUserDetailModelTwo getResult() {
        return result;
    }

    public void setResult(GetUserDetailModelTwo result) {
        this.result = result;
    }
}
