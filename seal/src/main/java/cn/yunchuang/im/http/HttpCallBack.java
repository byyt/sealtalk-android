package cn.yunchuang.im.http;


import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.exception.ApiException;

public abstract class HttpCallBack<T> extends CallBack<T> {

    @Override
    public void onStart() {
    }

    @Override
    public void onCompleted() {

    }

    public abstract void onSuccess(T t);

    public abstract void onError(ApiException e);

}
