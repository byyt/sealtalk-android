package cn.yunchuang.im.utils;

import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.yunchuang.im.event.RefreshWdyhDetailEvent;
import cn.yunchuang.im.message.YhmsMessage;

/**
 * Created by zhou_yuntao on 2019/5/24.
 */

public class PushRecieveUtils {

    /**
     * 处理约会秘书发过来的推送，
     * 将其中的json字符串extra字段，取出来其中的type，和其他字段值，构造相应的需要跳转的Intent，返回去
     * 这里的跳转都采用隐式跳转
     *
     * @return
     */

    public static final String WDYH = "wdyh";

    public static Intent getIntent(YhmsMessage yhmsMessage) {
        Intent intent = null;

        try {
            String extra = yhmsMessage.getExtra();
            JSONObject jsonObject = JSON.parseObject(extra);
            String type = jsonObject.getString("type");

            if (type.equals(WDYH)) {
                //我的约会相关的推送，跳转到我的约会详情页
                String wdyhOrderId = jsonObject.getString("wdyhOrderId");

                intent = new Intent("cn.yunchuang.im.ui.activity.WdyhDetailActivity");
                Bundle bundle = new Bundle();
                bundle.putString("wdyhOrderId", wdyhOrderId);
                intent.putExtras(bundle);

                //发个event事件刷新WdyhDetailActivity，如果当前正在WdyhDetailActivity中，就进行刷新，否则这个事件作废
                RefreshWdyhDetailEvent.postEvent(wdyhOrderId);

            }

        } catch (Exception e) {
            e.printStackTrace();
//            Ln.e(e.getMessage());
            return null;
        }

        return intent;
    }


}
