package cn.yunchuang.im;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by zhou_yuntao on 2019/3/30.
 */

public class MeService {

    public static String getUid() {
        return App.getAppContext().getSharedPreferences("config", MODE_PRIVATE)
                .getString(SealConst.SEALTALK_LOGIN_ID, "");
    }

}
