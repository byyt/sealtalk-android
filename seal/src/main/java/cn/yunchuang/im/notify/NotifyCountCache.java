package cn.yunchuang.im.notify;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liumingkong on 15/12/9.
 */
public class NotifyCountCache {

    public static HashSet<String> commentIdDatas = new HashSet<>();
    // 计数类型
    private static AtomicInteger requestCount = new AtomicInteger(0);
    private static AtomicInteger likeCount = new AtomicInteger(0);
    private static AtomicInteger matchCount = new AtomicInteger(0);
    private static AtomicInteger visitorCount = new AtomicInteger(0);
    private static AtomicInteger followCount = new AtomicInteger(0);
    private static AtomicInteger belikeCount = new AtomicInteger(0);

    public static void resetNotifyCountCache(NotifyCountCacheType notifyCountCacheType) {
        if (NotifyCountCacheType.LIKE == notifyCountCacheType) {
            likeCount.set(0);
        } else if (NotifyCountCacheType.VISITOR == notifyCountCacheType) {
            visitorCount.set(0);
        } else if (NotifyCountCacheType.FOLLOW == notifyCountCacheType) {
            followCount.set(0);
        } else if (NotifyCountCacheType.BE_LIKE == notifyCountCacheType) {
            belikeCount.set(0);
        } else if (NotifyCountCacheType.MATCH_LIKE == notifyCountCacheType) {
            matchCount.set(0);
        } else {
            likeCount.set(0);
            requestCount.set(0);
            commentIdDatas.clear();
            followCount.set(0);
            visitorCount.set(0);
            belikeCount.set(0);
            matchCount.set(0);
        }
    }

    public static int addCountAndGetNew(NotifyCountCacheType notifyCountCacheType) {
        if (NotifyCountCacheType.LIKE == notifyCountCacheType) {
            return likeCount.incrementAndGet();
        }
        if (NotifyCountCacheType.VISITOR == notifyCountCacheType) {
            return visitorCount.incrementAndGet();
        }
        if (NotifyCountCacheType.FOLLOW == notifyCountCacheType) {
            return followCount.incrementAndGet();
        }
        if (NotifyCountCacheType.BE_LIKE == notifyCountCacheType) {
            return belikeCount.incrementAndGet();
        }
        if (NotifyCountCacheType.MATCH_LIKE == notifyCountCacheType) {
            return matchCount.incrementAndGet();
        }
        return 0;
    }

    public static int getRequestCount() {
        return requestCount.incrementAndGet() + 100;
    }

    public enum NotifyCountCacheType {
        LIKE,
        VISITOR,
        FOLLOW,
        BE_LIKE,
        MATCH_LIKE,
        ALL
    }
}
