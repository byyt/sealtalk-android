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

    //马上租Ta，订单当前所处的状态
    public static final int MSZT_ORDER_STATUS_WFYFK = 0; // 未付预付款
    public static final int MSZT_ORDER_STATUS_YFYFK = 1; // 已付预付款待被租方接受
    public static final int MSZT_ORDER_STATUS_DFQK = 2; // 被租方已接受，待租方付全款
    public static final int MSZT_ORDER_STATUS_DJQR = 3; // 租方见到被租方，点击确认
    public static final int MSZT_ORDER_STATUS_ZZ = 4; // 双方无纠纷后48小时后将钱转给被租方
    public static final int MSZT_ORDER_STATUS_WJSTK = 5; // 已退钱回给租方（已付预付款，被租方没有接受，全额退款）
    public static final int MSZT_ORDER_STATUS_WFQKTK = 6; // 已退钱回给租方（被租方接受了，租方未付全款，扣除一定费用后退回给租方）
    public static final int MSZT_ORDER_STATUS_JFTK = 7; // 已退钱回给租方（点击确认后，租方和被租方后期发生纠纷，根据情况退钱回给被租方或被租方）

}
