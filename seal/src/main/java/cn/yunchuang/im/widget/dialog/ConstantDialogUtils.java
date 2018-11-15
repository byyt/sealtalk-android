package cn.yunchuang.im.widget.dialog;

/**
 * Created by ZhuMing on 2018/8/28.
 * dialog 层级常量
 */

public class ConstantDialogUtils {
    /**
     * 判断自定义dialog是否显示
     */
    public static boolean isShowDialog = false;

    /**
     * 数值越小 层级越靠上面
     * 数值相同 后添加的层级在同级的下面
     */
    //-------------------------------view层级------------------------------
    public final static int LEVEL_1 = 1;

    public final static int LEVEL_2 = 2;

    //默认
    public final static int LEVEL_3 = 3;


    //-------------------------------唯一标识------------------------------
    //版本更新
    public final static String VERSIONUPDATE = "1000";

    //账号过期
    public final static String ACCOUNTEXPIREDVIEW = "1001";

    //分享书单
    public final static String SHAREBOOKLIST = "1002";

    //下载进度
    public final static String DOWNLOADPROGRESS = "1003";

    //运营弹窗
    public final static String OPERATEWINDOW = "1004";

}
