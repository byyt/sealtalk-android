package cn.yunchuang.im.server.request;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class HomePageDataRequest {

    private int startIndex;
    private int pageSize;

    public HomePageDataRequest(int startIndex, int pageSize) {
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
