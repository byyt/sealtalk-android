package cn.yunchuang.im;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;

import cn.yunchuang.im.server.SealAction;
import cn.yunchuang.im.server.response.GetUserDetailOneResponse;
import cn.yunchuang.im.server.response.HomepageResponse;
import cn.yunchuang.im.server.utils.NLog;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 自己弄的网络请求，在Fragment里面没法用request，就用这个吧
 * 其实还有一个SealUserInfoManager，那个先不用了
 */
public class HttpManager {

    private final static String TAG = "HttpManager";


    private static HttpManager instance;
    private final Context context;
    private final SealAction action;
    private static Handler handler;

    public static HttpManager getInstance() {
        return instance;
    }

    public HttpManager(Context context) {
        this.context = context;
        action = new SealAction(context);
        handler = new Handler(Looper.getMainLooper());
    }

    public static void init(Context context) {
        instance = new HttpManager(context);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 泛型类，主要用于 API 中功能的回调处理。
     *
     * @param <T> 声明一个泛型 T。
     */
    public static abstract class ResultCallback<T> {

        public static class Result<T> {
            public T t;
        }

        public ResultCallback() {

        }

        /**
         * 成功时回调。
         *
         * @param t 已声明的类型。
         */
        public abstract void onSuccess(T t);

        /**
         * 错误时回调。
         *
         * @param errString 错误提示
         */
        public abstract void onError(String errString);

        //下面两个是为了切换会主线程回调成功和失败
        public void onFail(final String errString) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onError(errString);
                }
            });
            //也可以像下面这样
//            Observable.just(0)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<Integer>() {
//                        @Override
//                        public void accept(Integer integer) throws Exception {
//                            onError(errString);
//                        }
//                    });
        }

        public void onCallback(final T t) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onSuccess(t);
                }
            });
        }

    }

    private void onCallBackFail(ResultCallback<?> callback) {
        if (callback != null) {
            callback.onFail(null);
        }
    }

    private void onCallBackFail(ResultCallback<?> callback, String errString) {
        if (callback != null) {
            callback.onFail(errString);
        }
    }

    /**
     * 异步接口,获取首页推荐用户，分页加载
     *
     * @param startIndex 起始页
     * @param pageSize   页面大小
     * @param callback   获取首页推荐用户的回调
     */
    public void getRecommendUsers(final int startIndex, final int pageSize, final ResultCallback<HomepageResponse> callback) {
        Observable.just(0)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {
                        HomepageResponse homepageResponse = null;
                        if (!isNetworkConnected()) {
                            onCallBackFail(callback, "网络未连接");
                            return;
                        }
                        try {
                            homepageResponse = action.getRecommendUsers(startIndex, pageSize);
                        } catch (Exception e) {
                            onCallBackFail(callback);
                            NLog.e(TAG, "getRecommendUsers occurs Exception e=" + e.toString());
                            return;
                        }
                        if (callback != null) {
                            callback.onCallback(homepageResponse);
                        }
                    }
                });
    }

    /**
     * 我的页拉取个人信息
     *
     * @param userId
     * @param callback
     */
    public void getUserDetailOne(final String userId, final ResultCallback<GetUserDetailOneResponse> callback) {
        Observable.just(0)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {
                        GetUserDetailOneResponse getUserDetailOneResponse = null;
                        if (!isNetworkConnected()) {
                            onCallBackFail(callback, "网络未连接");
                            return;
                        }
                        try {
                            getUserDetailOneResponse = action.getUserDetailOne(userId);
                        } catch (Exception e) {
                            onCallBackFail(callback);
                            NLog.e(TAG, "getRecommendUsers occurs Exception e=" + e.toString());
                            return;
                        }
                        if (callback != null) {
                            callback.onCallback(getUserDetailOneResponse);
                        }
                    }
                });
    }

}
