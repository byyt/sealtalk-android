package cn.yunchuang.im.server.response;


import java.util.List;

/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class GetWdyhOrderLbResponse {

    private int code;

    private ResultEntity result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ResultEntity getResult() {
        return result;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public static class ResultEntity {
        private List<GetWdyhOrderDetailModel> data;
        private int nextIndex;

        public List<GetWdyhOrderDetailModel> getData() {
            return data;
        }

        public void setData(List<GetWdyhOrderDetailModel> data) {
            this.data = data;
        }

        public int getNextIndex() {
            return nextIndex;
        }

        public void setNextIndex(int nextIndex) {
            this.nextIndex = nextIndex;
        }
    }

}
