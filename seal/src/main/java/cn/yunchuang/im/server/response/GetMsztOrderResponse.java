package cn.yunchuang.im.server.response;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class GetMsztOrderResponse {

    private int code;

    private GetMsztOrderModel result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public GetMsztOrderModel getResult() {
        return result;
    }

    public void setResult(GetMsztOrderModel result) {
        this.result = result;
    }
}
