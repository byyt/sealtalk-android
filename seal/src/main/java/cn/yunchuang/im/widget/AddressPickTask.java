package cn.yunchuang.im.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.WheelPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.widget.WheelView;
import cn.yunchuang.im.App;
import cn.yunchuang.im.R;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import cn.yunchuang.im.zmico.utils.ResourceUtils;

/**
 * 获取地址数据并显示地址选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/12/15
 */
public class AddressPickTask extends AsyncTask<String, Void, ArrayList<Province>> {
    private WeakReference<Activity> activityReference;// 2018/6/1 StaticFieldLeak
    private ProgressDialog dialog;
    private Callback callback;
    private String selectedProvince = "", selectedCity = "", selectedCounty = "";
    private boolean hideProvince = false;
    private boolean hideCounty = false;

    public AddressPickTask(Activity activity) {
        this.activityReference = new WeakReference<>(activity);
    }

    public void setHideProvince(boolean hideProvince) {
        this.hideProvince = hideProvince;
    }

    public void setHideCounty(boolean hideCounty) {
        this.hideCounty = hideCounty;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        Activity activity = activityReference.get();
        if (activity == null) {
            return;
        }
//        dialog = ProgressDialog.show(activity, null, "正在初始化数据...", true, true);
    }

    @Override
    protected ArrayList<Province> doInBackground(String... params) {
        if (params != null) {
            switch (params.length) {
                case 1:
                    selectedProvince = params[0];
                    break;
                case 2:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    break;
                case 3:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    selectedCounty = params[2];
                    break;
                default:
                    break;
            }
        }
        ArrayList<Province> data = new ArrayList<>();
        try {
            Activity activity = activityReference.get();
            if (activity != null) {
                String json = ConvertUtils.toString(activity.getAssets().open("city.json"));
                data.addAll(JSON.parseArray(json, Province.class));
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<Province> result) {
        if (dialog != null) {
            dialog.dismiss();
        }
        if (result.size() > 0) {
            Activity activity = activityReference.get();
            if (activity == null) {
                return;
            }
            AddressPicker picker = new AddressPicker(activity, result);
            setPickerStyle(picker);
            picker.setTitleText("所在地");
            picker.setHideProvince(hideProvince);
            picker.setHideCounty(hideCounty);
            if (hideCounty) {
                picker.setColumnWeight(1 / 3.0f, 2 / 3.0f);//将屏幕分为3份，省级和地级的比例为1:2
            } else {
                picker.setColumnWeight(2 / 8.0f, 3 / 8.0f, 3 / 8.0f);//省级、地级和县级的比例为2:3:3
            }
            picker.setSelectedItem(selectedProvince, selectedCity, selectedCounty);
            picker.setOnAddressPickListener(callback);
            picker.show();
        } else {
            callback.onAddressInitFailed();
        }
    }

    public interface Callback extends AddressPicker.OnAddressPickListener {

        void onAddressInitFailed();

    }

    private void setPickerStyle(WheelPicker picker) {
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(App.getAppContext(), 10));
        picker.setDividerColor(ResourceUtils.getColor(R.color.color_E4E4E4));
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setTextColor(ResourceUtils.getColor(R.color.black));
        picker.setTextSize(20);
        picker.setLabelTextColor(ResourceUtils.getColor(R.color.black));
        picker.setCancelTextColor(ResourceUtils.getColor(R.color.black));
        picker.setCancelTextSize(16);
        picker.setSubmitTextColor(ResourceUtils.getColor(R.color.black));
        picker.setSubmitTextSize(ResourceUtils.getColor(R.color.black));
        picker.setPressedTextColor(ResourceUtils.getColor(R.color.color_888888));
        picker.setTitleTextColor(ResourceUtils.getColor(R.color.black));
        picker.setTitleTextSize(18);
        picker.setTopLineColor(ResourceUtils.getColor(R.color.color_E4E4E4));
        picker.setTopHeight(45);
        picker.setHeight(DeviceUtils.dpToPx(260));
        picker.setAnimationStyle(R.style.BottomDialogStyle_AnimationStyle150);
    }

}
