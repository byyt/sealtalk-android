package cn.yunchuang.im.ui.activity;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.yunchuang.im.R;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.zmico.utils.ResourceUtils;


public class MyBaseInfoActivity_new extends BaseActivity implements View.OnClickListener {

    private TextView birthDayTv;
    private int setYear = 1992;
    private int setMonth = 2;
    private int setDay = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_base_info);
        setTitle(R.string.de_actionbar_myacc);

        initView();
    }

    private void initView() {
        findViewById(R.id.my_base_info_shengri_layout).setOnClickListener(this);
        birthDayTv = (TextView) findViewById(R.id.my_base_info_shengri_tv);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {

        }
        return super.doInBackground(requestCode, id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {

            }
        }
    }


    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_base_info_shengri_layout:
                onYearMonthDayTimePicker();
                break;
        }
    }

    private void onYearMonthDayTimePicker() {
        final DatePicker picker = new DatePicker(this);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(this, 10));

        picker.setDividerColor(ResourceUtils.getColor(R.color.color_FC6880));
        picker.setTextColor(ResourceUtils.getColor(R.color.color_FC6880));
        picker.setLabelTextColor(ResourceUtils.getColor(R.color.color_FC6880));
        picker.setCancelTextColor(ResourceUtils.getColor(R.color.color_FC6880));
        picker.setSubmitTextColor(ResourceUtils.getColor(R.color.color_FC6880));
        picker.setTopLineColor(ResourceUtils.getColor(R.color.color_FC6880));

        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                birthDayTv.setText(year + "-" + month + "-" + day);
                setYear = Integer.valueOf(year);
                setMonth = Integer.valueOf(month);
                setDay = Integer.valueOf(day);
            }
        });

        //范围从18岁到70岁，当前日期往前推18年和70年
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int endYear = currentYear - 18;
        int startYear = currentYear - 70;
        picker.setRangeEnd(endYear, currentMonth, currentDay);
        picker.setRangeStart(startYear, currentMonth, currentDay);
        picker.setSelectedItem(setYear, setMonth, setDay);

        picker.show();
    }
}
