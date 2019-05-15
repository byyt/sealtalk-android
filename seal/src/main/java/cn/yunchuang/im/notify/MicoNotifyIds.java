package cn.yunchuang.im.notify;


/**
 * Created by liumingkong on 2018/5/17.
 */

public class MicoNotifyIds extends NotifyIdsBase {

    // 20以外可以用
    public static final int notifyIdComment = 21;
    public static final int notifyIdLike = 22;
    public static final int notifyIdVisit = 23;
    public static final int notifyIdNewFans = 24;
    public static final int notifyIdMoment = 25;
    public static final int notifyIdRecommend = 26;
    public static final int notifyIdSso = 27;
    public static final int notifyIdReg = 28;
    public static final int notifyIdEmoji = 29;
    public static final int notifyIdProfileLikeOther = 30;
    public static final int notifyIdMatch = 31;
    public static final int notifyIdFeedPostSucc = 32;
    public static final int notifyIdFeedPostFailed = 33;

    public static final int notifyIdGamePlayJoin = 34;
    public static final int notifyIdGameApply = 35;
    public static final int notifyIdGameAgree = 36;
    public static final int notifyIdGameInvite = 37;
    public static final int notifyIdAudioStartLive = 38;
    public static final int notifyIdAudioRoomBack = 39;



    public static final int notifyIdTest = 100;



    public static void clearNotifyWhenSign() {
        clearNotify(notifyIdSso);
        clearNotify(notifyIdReg);
    }

    public static void clearNotify(int notifyId) {
        // 批量清除
        if (notifyIdComment == notifyId) {
            for (String tag : NotifyCountCache.commentIdDatas) {
                clearNotify(notifyId, tag);
            }
        } else {
            if (notifyIdVisit == notifyId) {
                NotifyCountCache.resetNotifyCountCache(NotifyCountCache.NotifyCountCacheType.VISITOR);
            } else if (notifyIdNewFans == notifyId) {
                NotifyCountCache.resetNotifyCountCache(NotifyCountCache.NotifyCountCacheType.FOLLOW);
            } else if (notifyIdLike == notifyId) {
                NotifyCountCache.resetNotifyCountCache(NotifyCountCache.NotifyCountCacheType.LIKE);
            } else if (notifyIdProfileLikeOther == notifyId) {
                NotifyCountCache.resetNotifyCountCache(NotifyCountCache.NotifyCountCacheType.BE_LIKE);
            } else if (notifyIdMatch == notifyId) {
                NotifyCountCache.resetNotifyCountCache(NotifyCountCache.NotifyCountCacheType.MATCH_LIKE);
            }
            clearNotify(notifyId, defaultTag);
        }
    }
}
