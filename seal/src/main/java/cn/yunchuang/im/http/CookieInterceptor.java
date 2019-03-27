/*
 * Copyright (C) 2017 zhouyou(478319399@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.yunchuang.im.http;




import com.zhouyou.http.cookie.CookieManger;

import java.io.IOException;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.zhouyou.http.EasyHttp.getCookieJar;


/**
 * 这个拦截器是我加的，必须使用cookie，登陆成功后从服务端保存cookie，之后每次请求都带上cookit
 */
public class CookieInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        //取出cookie将cookie拼接成字符串
        CookieManger cookieManger = getCookieJar();
        List<Cookie> list = cookieManger.getCookieStore().getCookies();
        //将cookie拼接字符串
        StringBuilder s = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (Cookie cookie : list) {
                s.append(cookie.name()).append("=").append(cookie.value()).append(";");
            }
        }

        //拦截请求
        Request request = chain.request();
        //将cookie字符串添加到header头里
        request = request.newBuilder().header("Cookie", s.toString()).build();

        //获取请求的地址
        HttpUrl httpUrl = request.url();

        //拦截响应
        Response response = chain.proceed(request);
        //获取响应返回的cookie
        List<String> cookieList = response.headers("Set-Cookie");
        if (cookieList != null) {
            for (int i = 0; i < cookieList.size(); i++) {
                String cookieStr = cookieList.get(i);
                String[] cookievalues = cookieStr.split(";");
                for (int j = 0; j < cookievalues.length; j++) {
                    String[] keyPair = cookievalues[j].split("=");
                    String key = keyPair[0].trim();
                    String value = keyPair.length > 1 ? keyPair[1].trim() : "";
                    //构建Cookie，并且保存起来，下次发起请求时会带上cookie
                    Cookie.Builder builder = new Cookie.Builder();
                    Cookie cookie = builder.name(key).value(value).domain(httpUrl.host()).build();
                    cookieManger.saveFromResponse(httpUrl, cookie);
                }
            }
        }
        return response;
    }
}



