package cn.yunchuang.im.server;

import android.content.Context;

import java.util.List;

import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.server.network.http.SyncHttpClient;
import cn.yunchuang.im.server.utils.json.JsonMananger;

/**
 * Created by AMing on 16/1/14.
 * Company RongCloud
 */
public class BaseAction {

    //    private static final String DOMAIN = "http://api.sealtalk.im";
    public static final String DOMAIN = "http://192.168.0.110:8585";
    public static final String DOMAIN_PIC = "http://192.168.0.110:8081";
    //保存个人资料时上传的图片会保存到upload路径下，服务器端保存的路径就是"upload/abcd.jpg"
    //然后加载的时候就是BaseAction.DOMAIN_PIC+"/"+服务端的路径
    //如果是本地路径，则需要DOMAIN_PIC + DOMAIN_PIC_UPLOAD + 本地路径
    public static final String DOMAIN_PIC_UPLOAD = "upload/";
    protected Context mContext;
    protected SyncHttpClient httpManager;


    /**
     * 构造方法
     *
     * @param context 上下文
     */
    public BaseAction(Context context) {
        this.mContext = context;
        this.httpManager = SyncHttpClient.getInstance(context);
    }

    /**
     * JSON转JAVA对象方法
     *
     * @param json json
     * @param cls  class
     * @throws HttpException
     */
    public <T> T jsonToBean(String json, Class<T> cls) throws HttpException {
        return JsonMananger.jsonToBean(json, cls);
    }

    /**
     * JSON转JAVA数组方法
     *
     * @param json json
     * @param cls  class
     * @throws HttpException
     */
    public <T> List<T> jsonToList(String json, Class<T> cls) throws HttpException {
        return JsonMananger.jsonToList(json, cls);
    }

    /**
     * JAVA对象转JSON方法
     *
     * @param obj object
     * @throws HttpException
     */
    public String BeanTojson(Object obj) throws HttpException {
        return JsonMananger.beanToJson(obj);
    }


    /**
     * 获取完整URL方法
     *
     * @param url url
     */
    protected String getURL(String url) {
        return getURL(url, new String[]{});
    }

    /**
     * 获取完整URL方法
     *
     * @param url    url
     * @param params params
     */
    protected String getURL(String url, String... params) {
        StringBuilder urlBuilder = new StringBuilder(DOMAIN).append("/").append(url);
        if (params != null) {
            for (String param : params) {
                if (!urlBuilder.toString().endsWith("/")) {
                    urlBuilder.append("/");
                }
                urlBuilder.append(param);
            }
        }
        return urlBuilder.toString();
    }
}
