package cn.yunchuang.im.pulltorefresh.recyclerview;

/**
 * 设置SpanSizeLookup的接口 该接口的设置至针对垂直滚动的列表
 * Created by mulinrui on 2015/12/2.
 */
public interface ISpanSizeLookup {

    /**
     * 获取一个单元格占原始单元格的倍数（不能大于一行的列数）
     * @param position
     * @return
     */
    int getSpanSize2(int position);

    /**
     * 获取列数
     *
     * @return
     */
    int getSpanCount();

}
