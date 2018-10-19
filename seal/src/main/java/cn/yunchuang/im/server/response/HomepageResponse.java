package cn.yunchuang.im.server.response;


import java.util.List;

/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class HomepageResponse {

    private int code;

    private List<HomepageModel> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<HomepageModel> getResult() {
        return result;
    }

    public void setResult(List<HomepageModel> result) {
        this.result = result;
    }

}
