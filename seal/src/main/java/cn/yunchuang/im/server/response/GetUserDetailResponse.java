package cn.yunchuang.im.server.response;


import java.util.List;

/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class GetUserDetailResponse {

    private int code;

    private GetUserDetailModel result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public GetUserDetailModel getResult() {
        return result;
    }

    public void setResult(GetUserDetailModel result) {
        this.result = result;
    }
}
