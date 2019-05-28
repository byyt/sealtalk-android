package cn.yunchuang.im;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;

import cn.yunchuang.im.model.ShaixuanModel;
import cn.yunchuang.im.model.ShaixuanOrderModel;
import cn.yunchuang.im.server.SealAction;
import cn.yunchuang.im.server.response.BalanceCoinsResponse;
import cn.yunchuang.im.server.response.BaseResponse;
import cn.yunchuang.im.server.response.GetUserDetailOneResponse;
import cn.yunchuang.im.server.response.GetWdyhOrderLbResponse;
import cn.yunchuang.im.server.response.HomepageResponse;
import cn.yunchuang.im.server.utils.NLog;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
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
     * 上传地理位置即经纬度到服务器
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return
     */
    public Disposable postUserLocation(final double longitude, final double latitude, final ResultCallback<BaseResponse> callback) {
        return Observable.just(0)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {
                        BaseResponse baseResponse = null;
                        if (!isNetworkConnected()) {
                            onCallBackFail(callback, "网络未连接");
                            return;
                        }
                        try {
                            baseResponse = action.postUserLocation(longitude, latitude);
                        } catch (Exception e) {
                            onCallBackFail(callback);
                            NLog.e(TAG, "postUserLocation occurs Exception e=" + e.toString());
                            return;
                        }
                        if (callback != null) {
                            callback.onCallback(baseResponse);
                        }
                    }
                });
    }

    /**
     * 异步接口,首页--猜你喜欢，分页加载
     *
     * @param startIndex    起始页
     * @param pageSize      页面大小
     * @param shaixuanModel 筛选条件
     * @param callback      获取首页推荐用户的回调
     * @return
     */
    public Disposable getRecommendUsers(final int startIndex, final int pageSize, final ShaixuanModel shaixuanModel,
                                        final ResultCallback<HomepageResponse> callback) {
        return Observable.just(0)
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
                            homepageResponse = action.getRecommendUsers(startIndex, pageSize, shaixuanModel);
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
     * 首页--距离最近，分页加载
     *
     * @param startIndex    起始页
     * @param pageSize      页面大小
     * @param shaixuanModel 筛选条件
     * @param callback      获取首页推荐用户的回调
     * @return
     */
    public Disposable getNearByUsers(final int startIndex, final int pageSize, final ShaixuanModel shaixuanModel,
                                     final ResultCallback<HomepageResponse> callback) {
        return Observable.just(0)
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
                            homepageResponse = action.getNearByUsers(startIndex, pageSize, shaixuanModel);
                        } catch (Exception e) {
                            onCallBackFail(callback);
                            NLog.e(TAG, "getNearByUsers occurs Exception e=" + e.toString());
                            return;
                        }
                        if (callback != null) {
                            callback.onCallback(homepageResponse);
                        }
                    }
                });
    }

    /**
     * 首页--好评优先，分页加载
     *
     * @param startIndex    起始页
     * @param pageSize      页面大小
     * @param shaixuanModel 筛选条件
     * @param callback      获取首页推荐用户的回调
     * @return
     */
    public Disposable getRateUsers(final int startIndex, final int pageSize, final ShaixuanModel shaixuanModel,
                                   final ResultCallback<HomepageResponse> callback) {
        return Observable.just(0)
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
                            homepageResponse = action.getRateUsers(startIndex, pageSize, shaixuanModel);
                        } catch (Exception e) {
                            onCallBackFail(callback);
                            NLog.e(TAG, "getRateUsers occurs Exception e=" + e.toString());
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
     * @return
     */
    public Disposable getUserDetailOne(final String userId, final ResultCallback<GetUserDetailOneResponse> callback) {
        return Observable.just(0)
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
                            NLog.e(TAG, "getUserDetailOne occurs Exception e=" + e.toString());
                            return;
                        }
                        if (callback != null) {
                            callback.onCallback(getUserDetailOneResponse);
                        }
                    }
                });
    }

    /**
     * 我的约会列表，分页加载
     *
     * @param startIndex         起始页
     * @param pageSize           页面大小
     * @param shaixuanOrderModel 筛选条件
     * @param callback           获取首页推荐用户的回调
     * @return
     */
    public Disposable getWdyhLb(final int startIndex, final int pageSize, final ShaixuanOrderModel shaixuanOrderModel,
                                final ResultCallback<GetWdyhOrderLbResponse> callback) {
        return Observable.just(0)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {
                        GetWdyhOrderLbResponse getWdyhOrderLbResponse = null;
                        if (!isNetworkConnected()) {
                            onCallBackFail(callback, "网络未连接");
                            return;
                        }
                        try {
                            getWdyhOrderLbResponse = action.postWdyhLbList(startIndex, pageSize, shaixuanOrderModel);
                        } catch (Exception e) {
                            onCallBackFail(callback);
                            NLog.e(TAG, "postWdyhLbList occurs Exception e=" + e.toString());
                            return;
                        }
                        if (callback != null) {
                            callback.onCallback(getWdyhOrderLbResponse);
                        }
                    }
                });
    }


    /**
     * 余额和金币操作请求
     *
     * @param type     到SealConst去看具体的值
     * @param balance
     * @param coins
     * @param callback
     * @return
     */
    public Disposable postBalanceCoinsOperationSingle(final int type, final double balance, final long coins,
                                                      final ResultCallback<BalanceCoinsResponse> callback) {
        return Observable.just(0)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {
                        BalanceCoinsResponse response = null;
                        if (!isNetworkConnected()) {
                            onCallBackFail(callback, "网络未连接");
                            return;
                        }
                        try {
                            response = action.postBalanceCoinsOperationSingle(type, balance, coins);
                        } catch (Exception e) {
                            onCallBackFail(callback);
                            NLog.e(TAG, "postBalanceCoinsOperationSingle occurs Exception e=" + e.toString());
                            return;
                        }
                        if (callback != null) {
                            callback.onCallback(response);
                        }
                    }
                });
    }

    /**
     * 余额和金币操作，加减余额，加减金币，
     * 其他接口也可能操作余额和金币，不是说所有的余额金币操作都要调用这个接口
     * 这个接口与上个接口区别就是，这个接口既有付费方，也有收费方
     *
     * @param type         SealConstf中有详细说明
     * @param myBalance
     * @param myCoins
     * @param otherUserId  另外一方的userId
     * @param otherBalance
     * @param otherCoins
     * @return
     */
    public Disposable postBalanceCoinsOperationMulti(final int type, final double myBalance, final long myCoins,
                                                     final String otherUserId, final double otherBalance, final long otherCoins,
                                                     final ResultCallback<BalanceCoinsResponse> callback) {
        return Observable.just(0)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {
                        BalanceCoinsResponse response = null;
                        if (!isNetworkConnected()) {
                            onCallBackFail(callback, "网络未连接");
                            return;
                        }
                        try {
                            response = action.postBalanceCoinsOperationMulti(type, myBalance, myCoins, otherUserId,
                                    otherBalance, otherCoins);
                        } catch (Exception e) {
                            onCallBackFail(callback);
                            NLog.e(TAG, "postBalanceCoinsOperationMulti occurs Exception e=" + e.toString());
                            return;
                        }
                        if (callback != null) {
                            callback.onCallback(response);
                        }
                    }
                });
    }

}
