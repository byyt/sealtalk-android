package cn.yunchuang.im.server.response;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class GetWdyhOrderDetailResponse {

    private int code;

    private GetWdyhOrderDetailModel result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public GetWdyhOrderDetailModel getResult() {
        return result;
    }

    public void setResult(GetWdyhOrderDetailModel result) {
        this.result = result;
    }
}
