package cn.yunchuang.im.pulltorefresh.recyclerview;

/**
 * 加载更多监听
 * Created by mulinrui on 2016/12/1.
 */
public interface LoadMoreListener {

    /**
     * @param requestSize 请求数量
     */
    void onLoadMoreRequested(int requestSize);

}
