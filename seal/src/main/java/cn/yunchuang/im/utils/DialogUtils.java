package cn.yunchuang.im.utils;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.widget.AdapterView;

import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigItems;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.ItemsParams;
import com.mylhyl.circledialog.scale.ScaleLayoutConfig;

import java.util.List;

import cn.yunchuang.im.R;
import cn.yunchuang.im.widget.dialog.ConfirmDialog;
import cn.yunchuang.im.widget.dialog.ShuruDialog;
import cn.yunchuang.im.widget.dialog.ShuruDuohangDialog;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import cn.yunchuang.im.zmico.utils.ResourceUtils;
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
                .withAnim(false)
                .roundColor(ResourceUtils.getColor(R.color.color_111111))
                .roundAlpha(240);
        return loadingDialog;
    }

    public static void showLoading(PromptDialog loadingDialog) {
        if (loadingDialog != null) {
            loadingDialog.showLoading("加载中...");
        }
    }

    public static void dimiss(PromptDialog loadingDialog) {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public static PromptDialog getBottomListDialog(Activity activity) {
        PromptDialog promptDialog = new PromptDialog(activity);
        promptDialog.getDefaultBuilder().touchAble(false).round(3)
                .loadingDuration(3000)
                .cancleAble(true)
                .withAnim(false)
                .roundColor(ResourceUtils.getColor(R.color.color_111111))
                .roundAlpha(240);
        return promptDialog;
    }

    public static void showConfirmDialog(Activity activity, String title, String content,
                                         final ConfirmDialog.ConfirmListener confirmListener) {
        final ConfirmDialog dialog = new ConfirmDialog(activity);
        dialog.show();//必须先show，在设置下面的值，否则显示不出来？
        dialog.setTitle(title).setContent(content).setLeftText("取消").setRightText("确定");

        dialog.setDialogListener(new ConfirmDialog.SimpleDialog() {
            @Override
            public void rightButtonEvent() {
                if (confirmListener != null) {
                    confirmListener.confirm();
                }
                dialog.dismiss();
            }

            @Override
            public void leftButtonEvent() {
                dialog.dismiss();
            }
        });


    }

    /**
     * 输入框单行
     *
     * @param activity
     * @param title
     * @param content         初始输入内容
     * @param confirmListener
     */
    public static void showShuruDanhangDialog(Activity activity, String title, String content,
                                              final ShuruDialog.ConfirmListener confirmListener) {
        final ShuruDialog dialog = new ShuruDialog(activity);
        dialog.show();
        dialog.setTitle(title).setContent(content).setLeftText("取消").setRightText("确定");

        dialog.setDialogListener(new ShuruDialog.SimpleDialog() {
            @Override
            public void rightButtonEvent(String content) {
                if (confirmListener != null) {
                    confirmListener.confirm(content);
                }
                dialog.dismiss();
            }

            @Override
            public void leftButtonEvent() {
                dialog.dismiss();
            }
        });
    }

    /**
     * 输入框多行
     *
     * @param activity
     * @param title
     * @param content         初始输入内容
     * @param confirmListener
     */
    public static void showShuruDuohangDialog(Activity activity, String title, String content, String hint,
                                              final ShuruDuohangDialog.ConfirmListener confirmListener) {
        final ShuruDuohangDialog dialog = new ShuruDuohangDialog(activity);
        dialog.show();
        dialog.setTitle(title).setContent(content).setHint(hint).setLeftText("取消").setRightText("确定");

        dialog.setDialogListener(new ShuruDuohangDialog.SimpleDialog() {
            @Override
            public void rightButtonEvent(String content) {
                if (confirmListener != null) {
                    confirmListener.confirm(content);
                }
                dialog.dismiss();
            }

            @Override
            public void leftButtonEvent() {
                dialog.dismiss();
            }
        });
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

    /**
     * 其中一种方式展示底部列表对话框
     *
     * @param activity
     * @param items             传入的列表数据
     * @param itemClickListener 根据位置，点击相应的列表位置回调到该函数
     */
    public static void showBottomList(FragmentActivity activity,
                                      List<PictureTypeEntity> items,
                                      AdapterView.OnItemClickListener itemClickListener) {
        //如果需要弹出框震动的，可以把下面的代码拷贝过去，有些地方默认有震动的，所以这句话就不写死在这里里
//        Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
//        vibrator.vibrate(30);
        new CircleDialog.Builder()
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        //点击后颜色
                        params.backgroundColorPress = ResourceUtils.getColor(R.color.color_E4E4E4);
                        //增加弹出动画
                        params.animStyle = R.style.BottomDialogStyle_AnimationStyle200;
                        //宽度占屏幕比例
                        params.width = (float) 0.95;
                        params.radius = 20;
                    }
                })
                .configItems(new ConfigItems() {
                    @Override
                    public void onConfig(ItemsParams params) {
                        //这个框架单位内部又算了一遍，只能看着调了，蛋疼
                        params.textColor = ResourceUtils.getColor(R.color.black);
                        params.textSize = (int) (DeviceUtils.spToPx(18) / ScaleLayoutConfig.getInstance().getScale());
                        params.itemHeight = (int) (DeviceUtils.dpToPx(55) / ScaleLayoutConfig.getInstance().getScale());
                    }
                })
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.topMargin = DeviceUtils.dpToPx(5);
                        params.textColor = ResourceUtils.getColor(R.color.color_0076FF);
                        params.textSize = (int) (DeviceUtils.spToPx(18) / ScaleLayoutConfig.getInstance().getScale());
                        params.height = (int) (DeviceUtils.dpToPx(55) / ScaleLayoutConfig.getInstance().getScale());
                        params.text = "取消";
                    }
                })
                .setItems(items, itemClickListener)
                .show(activity.getSupportFragmentManager());

    }

    public static class PictureTypeEntity {
        public int id;
        public String typeName;

        public PictureTypeEntity(int id, String typeName) {
            this.id = id;
            this.typeName = typeName;
        }

        @Override
        public String toString() {
            return typeName;
        }

    }

}
