package cn.yunchuang.im.utils;

import android.app.Activity;

import cn.yunchuang.im.R;
import cn.yunchuang.im.zmico.utils.ResourceUtils;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by zhou_yuntao on 2019/3/23.
 */

public class DialogUtils {

    public static PromptDialog getLoadingDialog(Activity activity) {
        PromptDialog loadingDialog = new PromptDialog(activity);
        loadingDialog.getDefaultBuilder().touchAble(false).round(3)
                .loadingDuration(3000)
                .cancleAble(true)
                .roundColor(ResourceUtils.getColor(R.color.color_111111))
                .roundAlpha(240);
        return loadingDialog;
    }

    public static PromptDialog getPicDialog(Activity activity) {
        PromptDialog promptDialog = new PromptDialog(activity);
        promptDialog.getDefaultBuilder().touchAble(false).round(3)
                .stayDuration(200)
                .cancleAble(true);
        //设置显示的文字大小及颜色
//                promptDialog.getAlertDefaultBuilder().textSize(12).textColor(Color.GRAY);
        //默认两个按钮为Alert对话框，大于三个按钮的为底部SHeet形式展现
        return promptDialog;
    }

}
