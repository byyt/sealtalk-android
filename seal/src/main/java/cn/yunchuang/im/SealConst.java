package cn.yunchuang.im;

/**
 * Created by AMing on 16/5/26.
 * Company RongCloud
 */
public class SealConst {
    public static final int DISCUSSION_REMOVE_MEMBER_REQUEST_CODE = 1;
    public static final int DISCUSSION_ADD_MEMBER_REQUEST_CODE = 2;
    public static final boolean ISOPENDISCUSSION = false;
    public static final int PERSONALPROFILEFROMCONVERSATION = 3;
    public static final int PERSONALPROFILEFROMGROUP = 4;
    public static final String GROUP_LIST_UPDATE = "GROUP_LIST_UPDATE";
    public static final String EXIT = "EXIT";
    public static final String CHANGEINFO = "CHANGEINFO";
    public static final String SEALTALK_LOGIN_ID = "loginid";
    public static final String SEALTALK_LOGIN_NAME = "loginnickname";
    public static final String SEALTALK_LOGING_PORTRAIT = "loginPortrait";
    public static final String SEALTALK_LOGING_PHONE = "loginphone";
    public static final String SEALTALK_LOGING_PASSWORD = "loginpassword";


    //后面自己加的
    public static final String LASTUPDATE = "lastUpdate";

    //性别排号
    public static final int SEX_MAN = 0;
    public static final int SEX_WOMAN = 1;
    public static final int SEX_ALL = 2;

    //技能排号
    public static final int SKILL_PAO_BU = 1;
    public static final int SKILL_JIAN_SHEN = 2;
    public static final int SKILL_CHI_FAN = 3;
    public static final int SKILL_KAN_DIAN_YING = 4;

    //支付方式
    public static final int ZFFS_QB = 0;  //钱包
    public static final int ZFFS_WX = 1;  //微信

    //我的约会，订单类型
    public static final int WDYH_ORDER_TYPE_MSZT = 0;//马上租Ta
    public static final int WDYH_ORDER_TYPE_FBBM = 0;//发布报名


    //马上租Ta，订单当前所处的状态
    public static final int MSZT_ORDER_STATUS_DFYFK = 0; // 待付预付款
    public static final int MSZT_ORDER_STATUS_DJS = 1; // 已付预付款待被租方接受
    public static final int MSZT_ORDER_STATUS_DFQK = 2; // 被租方已接受，待租方付全款
    public static final int MSZT_ORDER_STATUS_DQR = 3; // 已付全款，待确认租方见到被租方，点击确认
    public static final int MSZT_ORDER_STATUS_DPJ = 4; // 已确认，待评价
    public static final int MSZT_ORDER_STATUS_WC = 5; // 已确认，待评价
    public static final int MSZT_ORDER_STATUS_YZZ = 6; // 双方无纠纷后48小时后将钱转给被租方
    public static final int MSZT_ORDER_STATUS_WJSTK = 7; // 已退钱回给租方（已付预付款，被租方没有接受，全额退款）
    public static final int MSZT_ORDER_STATUS_WFQKTK = 8; // 已退钱回给租方（被租方接受了，租方未付全款，扣除一定费用后退回给租方）
    public static final int MSZT_ORDER_STATUS_JFTK = 9; // 已退钱回给租方（点击确认后，租方和被租方后期发生纠纷，根据情况退钱回给被租方或被租方）

    //马上租Ta，订单各种状态下的文字
    public static final String MSZT_ORDER_STRING_DFYFK = "待付预付款";
    public static final String MSZT_ORDER_STRING_YFYFK = "已付预付款";
    public static final String MSZT_ORDER_STRING_WFYFK = "未付预付款";
    public static final String MSZT_ORDER_STRING_DJS = "待接受";
    public static final String MSZT_ORDER_STRING_YJS = "已接受";
    public static final String MSZT_ORDER_STRING_WJS = "未接受";
    public static final String MSZT_ORDER_STRING_DFQK = "待付全款";
    public static final String MSZT_ORDER_STRING_YFQK = "已付全款";
    public static final String MSZT_ORDER_STRING_WFQK = "未付全款";
    public static final String MSZT_ORDER_STRING_DQR = "待确认";
    public static final String MSZT_ORDER_STRING_YQR = "已确认";
    public static final String MSZT_ORDER_STRING_WQR = "未确认";
    public static final String MSZT_ORDER_STRING_DPJ = "待评价";
    public static final String MSZT_ORDER_STRING_YPJ = "已评价";
    public static final String MSZT_ORDER_STRING_WPJ = "未评价";
    public static final String MSZT_ORDER_STRING_WC = "完成";
    public static final String MSZT_ORDER_STRING_YiJieShu = "已结束";
    //按钮文字
    public static final String MSZT_ORDER_STRING_QFYFK = "去付预付款";
    public static final String MSZT_ORDER_STRING_JS = "接受";
    public static final String MSZT_ORDER_STRING_QFQK = "去付全款";
    public static final String MSZT_ORDER_STRING_QR = "确认";
    public static final String MSZT_ORDER_STRING_QPJ = "去评价";

    //身份信息
    public static final int IDENTITY_TYPE_DAREN = 1;//身份信息：达人

    //余额、金币相关，
    //下面只是简单例子，应该再加个后缀，表示哪种操作100,200,300,400是基础值
    //加余额
    public static final int BALANCE_COINS_TYPE_ADD_BALANCE = 100;
    //加余额，充值
    public static final int BALANCE_COINS_TYPE_ADD_BALANCE_RECHARGE = 101;
    //减余额
    public static final int BALANCE_COINS_TYPE_MINUS_BALANCE = 200;
    //自己减余额，发消息；付款用户花费；收款方收入
    public static final int BALANCE_COINS_TYPE_CHAT_MESSAGE = 201;


    public static final int BALANCE_COINS_TYPE_ADD_COINS = 300;//加金币，充值或转换
    public static final int BALANCE_COINS_TYPE_MINUS_COINS = 400;//减金币，消费
    public static final int BALANCE_COINS_NOT_ENOUGH_BALANCE = 555;//服务器返回余额不足的错误码
    public static final int BALANCE_COINS_NOT_ENOUGH_COINS = 556;//服务器返回金币不足的错误码
    //各项消费金额，额度
    public static final double MINUS_BALANCE_ONE_CHAT_MESSAGE = 1.00;//每条消息付款用户花费1元
    public static final double ADD_BALANCE_ONE_CHAT_MESSAGE = 0.5;//每条消息收款用户收入0.5元


}
