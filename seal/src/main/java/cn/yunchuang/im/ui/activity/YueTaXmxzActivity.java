package cn.yunchuang.im.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

import cn.yunchuang.im.R;
import cn.yunchuang.im.server.network.http.HttpException;
import cn.yunchuang.im.server.response.GetUserDetailModelOne;
import cn.yunchuang.im.server.response.GetUserDetailOneResponse;
import cn.yunchuang.im.server.response.SkillModel;
import cn.yunchuang.im.server.utils.CommonUtils;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.server.widget.LoadDialog;
import cn.yunchuang.im.utils.DialogUtils;
import cn.yunchuang.im.utils.ViewVisibleUtils;
import cn.yunchuang.im.zmico.utils.BaseBaseUtils;
import cn.yunchuang.im.zmico.utils.DeviceUtils;
import cn.yunchuang.im.zmico.utils.Utils;
import cn.yunchuang.im.zmico.utils.ViewUtil;
import me.leefeng.promptlibrary.PromptDialog;

import static cn.yunchuang.im.SealConst.SKILL_CHI_FAN;
import static cn.yunchuang.im.SealConst.SKILL_JIAN_SHEN;
import static cn.yunchuang.im.SealConst.SKILL_KAN_DIAN_YING;
import static cn.yunchuang.im.SealConst.SKILL_PAO_BU;


public class YueTaXmxzActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout titleLayout;
    private ImageView backImg;
    private TextView nextTv;

    private LinearLayout skillsLayout;

    private static final int GET_USER_DETAIL_ONE = 1601;

    private PromptDialog loadingDialog;
    private String userId = "";

    private SkillModel seletSkillModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseBaseUtils.setTranslucentStatus(this);//状态栏透明
        setContentView(R.layout.activity_yue_ta_xmxz);
        setHeadVisibility(View.GONE);
        initView();
    }

    private void initView() {
        userId = getIntent().getStringExtra("userId");

        titleLayout = (FrameLayout) findViewById(R.id.activity_xmxz_title_layout);
        backImg = (ImageView) findViewById(R.id.activity_xmxz_back);
        backImg.setOnClickListener(this);
        nextTv = (TextView) findViewById(R.id.activity_xmxz_next);
        nextTv.setOnClickListener(this);

        skillsLayout = (LinearLayout) findViewById(R.id.activity_xmxz_skills_layout);

        loadingDialog = DialogUtils.getLoadingDialog(this);

        initTitleLayout();
        getData();
    }

    private void initTitleLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DeviceUtils.dpToPx(48) + DeviceUtils.getStatusBarHeightPixels(this));
        titleLayout.setLayoutParams(layoutParams);
    }


    private void getData() {
        DialogUtils.showLoading(loadingDialog);
        request(GET_USER_DETAIL_ONE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (Utils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.activity_xmxz_back:
                finish();
                break;
            case R.id.activity_xmxz_next:
                Intent intent = new Intent(mContext, YueTaMsytActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putSerializable("skillModel", seletSkillModel);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    private void saveShaixuan() {

    }

    private void updateDataOne(GetUserDetailOneResponse getUserDetailOneResponse) {
        if (getUserDetailOneResponse == null) {
            return;
        }
        GetUserDetailModelOne modelOne = getUserDetailOneResponse.getResult();
        if (modelOne == null) {
            return;
        }

        updateSkillLayout(modelOne);
    }

    //更新技能列表
    private void updateSkillLayout(GetUserDetailModelOne modelOne) {
        String skillJsonStr = modelOne.getSkills();
        if (skillJsonStr == null || skillJsonStr.equals("")) {
            View skillEmptyView = LayoutInflater.from(this).inflate(R.layout.layout_yue_ta_xmxz_kong, null);
            skillsLayout.addView(skillEmptyView);
            ViewVisibleUtils.setVisibleGone(nextTv, false);
            return;
        }
        try {
            List<SkillModel> skillList = JSONArray.parseArray(skillJsonStr, SkillModel.class); //得到技能列表
            if (Utils.isEmptyCollection(skillList)) {
                View skillEmptyView = LayoutInflater.from(this).inflate(R.layout.layout_yue_ta_xmxz_kong, null);
                skillsLayout.addView(skillEmptyView);
                ViewVisibleUtils.setVisibleGone(nextTv, false);
                return;
            }
            //如果数据不为空，添加Ta的技能这一行
            View skillTitleView = LayoutInflater.from(this).inflate(R.layout.layout_yue_ta_xmxz_top_title, null);
            skillsLayout.addView(skillTitleView);
            //一次添加技能列表，遍历json的key和value
            for (int i = 0; i < skillList.size(); i++) {
                SkillModel skillModel = skillList.get(i);
                int type = skillModel.getType();
                String name = skillModel.getName();
                int price = skillModel.getPrice();

                View view = LayoutInflater.from(this).inflate(R.layout.layout_yue_ta_xmxz_item, null);
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.activity_xmxz_item_root_layout);
                ImageView imageView = (ImageView) view.findViewById(R.id.activity_xmxz_skill_item_iv);
                TextView nameTv = (TextView) view.findViewById(R.id.activity_xmxz_skill_item_name_tv);
                TextView priceTv = (TextView) view.findViewById(R.id.activity_xmxz_skill_item_price_tv);
                ImageView selectIv = (ImageView) view.findViewById(R.id.activity_xmxz_skill_item_select_iv);

                if (i == 0) {//默认选中第一个
                    ViewVisibleUtils.setVisibleGone(selectIv, true);
                    seletSkillModel = skillModel;
                } else {
                    ViewVisibleUtils.setVisibleGone(selectIv, false);
                }

                switch (type) {
                    case SKILL_PAO_BU:
                        imageView.setImageResource(R.drawable.user_detail_paobu);
                        break;
                    case SKILL_JIAN_SHEN:
                        imageView.setImageResource(R.drawable.user_detail_jianshen);
                        break;
                    case SKILL_CHI_FAN:
                        imageView.setImageResource(R.drawable.user_detail_chifan);
                        break;
                    case SKILL_KAN_DIAN_YING:
                        imageView.setImageResource(R.drawable.user_detail_kandianying);
                        break;
                    default:
                        //记得添加一个其他图标
                        imageView.setImageResource(R.drawable.user_detail_paobu);
                        break;
                }
                nameTv.setText(name);
                priceTv.setText(price + "元／小时");

                ViewUtil.setTag(view, skillModel, R.id.info_tag);

                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ViewUtil.getTag(view, R.id.info_tag) == null) {
                            return;
                        }
                        seletSkillModel = (SkillModel) ViewUtil.getTag(view, R.id.info_tag);//从tag里取出SkillModel
                        if (seletSkillModel != null) {
                            updateSelectItem();
                        }
                    }
                });

                skillsLayout.addView(view);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //被选中的item打上勾，其他的取消勾
    private void updateSelectItem() {
        if (skillsLayout.getChildCount() == 0) {
            return;
        }
        //第一个child是Ta的技能
        for (int i = 1; i < skillsLayout.getChildCount(); i++) {
            View view = skillsLayout.getChildAt(i);
            if (ViewUtil.getTag(view, R.id.info_tag) == null) {
                return;
            }
            SkillModel skillModel = (SkillModel) ViewUtil.getTag(view, R.id.info_tag);//从tag里取出SkillModel
            ImageView imageView = view.findViewById(R.id.activity_xmxz_skill_item_select_iv);
            if (skillModel != null && seletSkillModel.getType() == skillModel.getType()) {
                ViewVisibleUtils.setVisibleGone(imageView, true);
            } else {
                ViewVisibleUtils.setVisibleGone(imageView, false);
            }
        }
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case GET_USER_DETAIL_ONE:
                return action.getUserDetailOne(userId);
        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        DialogUtils.dimiss(loadingDialog);
        if (result != null) {
            switch (requestCode) {
                case GET_USER_DETAIL_ONE:
                    GetUserDetailOneResponse getUserDetailOneResponse = (GetUserDetailOneResponse) result;
                    if (getUserDetailOneResponse.getCode() == 200) {
                        updateDataOne(getUserDetailOneResponse);
                    } else {
                        NToast.shortToast(mContext, "获取个人信息失败");
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        DialogUtils.dimiss(loadingDialog);
        if (!CommonUtils.isNetworkConnected(mContext)) {
            LoadDialog.dismiss(mContext);
            NToast.shortToast(mContext, getString(R.string.network_not_available));
            return;
        }
        switch (requestCode) {
            case GET_USER_DETAIL_ONE:
                NToast.shortToast(mContext, "获取个人信息失败");
                break;
        }
    }
}
