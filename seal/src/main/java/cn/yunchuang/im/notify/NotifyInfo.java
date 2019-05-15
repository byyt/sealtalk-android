package cn.yunchuang.im.notify;

import android.app.Notification;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import cn.yunchuang.im.R;
import cn.yunchuang.im.zmico.utils.ResourceUtils;


/**
 * Created by liumingkong on 14-5-19.
 */
public class NotifyInfo {

    public CharSequence getNotifyTitle() {
        return notifyTitle;
    }

    public CharSequence getNotifyContent() {
        return notifyContent;
    }

    public CharSequence getNotifyTicker() {
        return notifyTicker;
    }

    public String getNotifyImage() {
        return notifyImage;
    }

    public String getNotifyTag() {
        return notifyTag;
    }

    public int getNotifyId() {
        return notifyId;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public int getPriority() {
        return priority;
    }

    public NotifyChannelManager.NotifyChannelType getNotifyChannelType() {
        return notifyChannelType;
    }

    private CharSequence notifyTicker = "您有一条新的消息";
    private CharSequence notifyTitle = "YunChuang";
    private CharSequence notifyContent = "您有一条新的消息";

    private String notifyImage;
    private String notifyTag;
    private int notifyId;
    private int requestCode;
    private boolean ongoing = false; //通知栏是否常驻，默认不常驻
    private int priority = Notification.PRIORITY_HIGH; //通知栏优先级，默认最高级
    // notifyId 划分类型，requestCode 区分跳转，notifyTag 是否平铺请求
    // 平铺类型的，notifyTag必须不同
    private NotifyChannelManager.NotifyChannelType notifyChannelType;


    // 所有的聊天消息类型
//    public void setNotifyInfoForSingle(String avatarFid, long convId, String username, int unreadCount, CharSequence content, ConvType convType) {
//        this.notifyChannelType = NotifyChannelManager.NotifyChannelType.MSG;
//        this.notifyTag = String.valueOf(convId);
//        setNotifyId(NotifyIdsBase.notifyIdChat, true);
//
//        this.notifyImage = avatarFid;
//        if (!Utils.isEmptyString(username)) {
//            notifyTitle = username;
//            notifyTicker = username + ":" + content;
//            if (unreadCount > 1) {
//                notifyContent = String.format(ResourceUtils.resourceString(R.string.chatting_syncbox_notify_content_send_tips), unreadCount);
//            } else {
//                notifyContent = content;
//            }
//        } else {
//            if (ConvType.STRANGER_SINGLE == convType) {
//                notifyTitle = ResourceUtils.resourceString(R.string.chatting_greeting_received);
//            } else {
//                notifyTitle = "Waka";
//            }
//
//            notifyTicker = ResourceUtils.resourceString(R.string.chatting_syncbox_notify_content_default);
//            if (unreadCount > 1) {
//                notifyContent = String.format(ResourceUtils.resourceString(R.string.chatting_notify_recv_msg_multi), unreadCount);
//            } else {
//                notifyContent = content;
//            }
//        }
//    }

    // 所有的聊天消息类型
//    public void setNotifyInfoForGroup(String avatarFid, long convId, String groupName, String userName, int unreadCount, CharSequence content, ConvType convType) {
//        this.notifyChannelType = NotifyChannelManager.NotifyChannelType.GROUP;
//        this.notifyTag = String.valueOf(convId);
//        setNotifyId(NotifyIdsBase.notifyIdChat, true);
//
//        this.notifyImage = avatarFid;
//        if (!Utils.isEmptyString(groupName)) {
//            notifyTitle = groupName;
//        } else {
//            notifyTitle = "Waka";
//        }
//
//        StringBuilder contentStringBuilder = new StringBuilder();
//        if (unreadCount > 1) {
//            contentStringBuilder.append("(");
//            contentStringBuilder.append(unreadCount);
//            contentStringBuilder.append(")");
//        }
//
//        if (!Utils.isEmptyString(userName)) {
//            contentStringBuilder.append(userName);
//            contentStringBuilder.append(":");
//        }
//
//        contentStringBuilder.append(content);
//        content = contentStringBuilder.toString();
//
//
//        String atTipsStr = null;
//
//        if (ConvAtTipPref.isNotifyAt(convId)) {
//            atTipsStr = "[" + ResourceUtils.resourceString(R.string.string_group_notify_at_me) + "] ";
//            content = atTipsStr + content;
//        }
//
//        if (Utils.isNotEmptyString(atTipsStr)) {
//            SpannableString spannableString = new SpannableString(content);
//            spannableString.setSpan(new ForegroundColorSpan(ResourceUtils.getColor(R.color.color00CC82)), 0, atTipsStr.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//
//            notifyTicker = spannableString;
//            notifyContent = spannableString;
//        } else {
//            notifyTicker = content;
//            notifyContent = content;
//        }
//    }

    // 推送消息（评论，赞，动态，访客，关注），sso，linkPage（新人推荐）
    public void setPushBodyNotify(int notifyId, String notifyTag, CharSequence notifyTicker, CharSequence notifyTitle,
                                  CharSequence notifyContent, boolean isSetSingleId, NotifyChannelManager.NotifyChannelType notifyChannelType) {
        this.notifyTicker = notifyTicker;
        this.notifyTitle = notifyTitle;
        this.notifyContent = notifyContent;
        setNotifyId(notifyId, isSetSingleId);
        this.notifyTag = notifyTag;
        this.notifyChannelType = notifyChannelType;
    }

    // requestCode是PendingIntent的标志，如果requestCode相同会被认为是一个。
    // FLAG_UPDATE_CURRENT是指后来的PendingIntent会更新前面的， 所以把requestCode设置为不重复就可以
    private void setNotifyId(int notifyId, boolean isSetSingleId) {
        this.notifyId = notifyId;
        if (isSetSingleId) {
            // 适用场景：
            // 聊天消息（平铺）
            // 最初的想法是每一个用户的会话是平铺的，可以采用会话ID convId作为notifyId，但是notifyId是整形的，没法直接这样使用
            // 采用 tag + notifyId，即tag不同notifyId相同的方法
            // 所有聊天消息采用同一个notifyId，PendingIntent采用FLAG_UPDATE_CURRENT
            // 存储对应的是 notifyId + tag --> notifyId + requestCode ---> intent
            // PendingIntent采用FLAG_UPDATE_CURRENT：如果PendingIntent已经存在，保留它并且只替换它的extra数据。

            // 如果不这样做：
            // notifyId + tag1 --> notifyId + requestCode --> intent1
            // 第二个人，产生新的intent2
            // 更新 notifyId + requestCode --> intent2，intent1跳转丢失

            // notifyId + tag1 --> notifyId + requestCode --> intent2
            // notifyId + tag2 --> notifyId + requestCode --> intent2
            // 产生了覆盖效果.....导致点击用户1的通知栏，跳转到2上了

            // 关注的动态，收到的评论都是相同场景
            this.requestCode = NotifyCountCache.getRequestCount();
        } else {
            // 存储对应的是 notifyId + requestCode ---> intent
            // intent始终是一样的，不需要更新intent
            this.requestCode = notifyId;
        }
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    @Override
    public String toString() {
        return "{" +
                "notifyTicker:" + notifyTicker
                + ",notifyTitle:" + notifyTitle
                + ",notifyContent:" + notifyContent
                + ",notifyImage:" + notifyImage
                + ",notifyTag:" + notifyTag
                + ",notifyId:" + notifyId
                + ",requestCode:" + requestCode
                + ",ongoing:" + ongoing
                + ",priority:" + priority
                + "}";
    }
}

/**
 * PendingIntent有一个getActivity方法，第一个参数是上下文，没啥好说的，第二个参数 requestCode，这个后面说，
 * 第三个参数是 Intent，用来存储信息，第四个参数是对参数的操作标识，常用的就是FLAG_CANCEL_CURRENT和FLAG_UPDATE_CURRENT。
 * 当使用FLAG_UPDATE_CURRENT时：
 * PendingIntent.getActivity(context, 0, notificationIntent,PendingIntent.FLAG_CANCEL_CURRENT时);
 * FLAG_UPDATE_CURRENT会更新之前PendingIntent的消息，比如，你推送了消息1，并在其中的Intent中putExtra了一个值“ABC”，
 * 在未点击该消息前，继续推送第二条消息，并在其中的Intent中putExtra了一个值“CBA”，好了，这时候，如果你单击消息1或者消息2，
 * 你会发现，他俩个的Intent中读取过来的信息都是“CBA”，就是说，第二个替换了第一个的内容
 * 当使用FLAG_CANCEL_CURRENT时：
 * 依然是上面的操作步骤，这时候会发现，点击消息1时，没反应，第二条可以点击。
 * 导致上面两个问题的原因就在于第二个参数requestCode，当requestCode值一样时，后面的就会对之前的消息起作用，
 * 所以为了避免影响之前的消息，requestCode每次要设置不同的内容。
 */