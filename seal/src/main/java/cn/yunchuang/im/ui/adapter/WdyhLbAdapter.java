package cn.yunchuang.im.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import cn.yunchuang.im.MeService;
import cn.yunchuang.im.R;
import cn.yunchuang.im.SealConst;
import cn.yunchuang.im.server.response.GetWdyhOrderDetailModel;
import cn.yunchuang.im.server.response.SkillModel;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.server.utils.json.JsonMananger;
import cn.yunchuang.im.ui.activity.WdyhDetailActivity;
import cn.yunchuang.im.ui.widget.AgeSexView;
import cn.yunchuang.im.utils.GlideUtils;

/**
 * 放松入口
 * Created by mulinrui on 2017/10/12.
 */
public class WdyhLbAdapter extends BaseQuickAdapter<GetWdyhOrderDetailModel, BaseViewHolder> {

    public static final String TAG = "WdyhLbAdapter";


    private LinearLayout rootLayout;
    private RoundedImageView portraitIv;
    private TextView nameTv;
    private AgeSexView ageSexView;
    private TextView ztTv;
    private TextView zjTv;
    private TextView yyscTv;
    private TextView yyztTv;
    private TextView yylxTv;


    private List<GetWdyhOrderDetailModel> getWdyhOrderDetailModels = new ArrayList<>();

    public WdyhLbAdapter(Context context) {
        super(R.layout.item_wdyh_lb_adapter);
        this.mContext = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, GetWdyhOrderDetailModel item) {

        rootLayout = (LinearLayout) helper.getView(R.id.item_wdyh_lb_layout);
        portraitIv = (RoundedImageView) helper.getView(R.id.item_wdyh_lb_portrait);
        nameTv = (TextView) helper.getView(R.id.item_wdyh_lb_nickname);
        ageSexView = (AgeSexView) helper.getView(R.id.item_wdyh_lb_age_sex_view);
        ztTv = (TextView) helper.getView(R.id.item_wdyh_lb_zt_tv);
        zjTv = (TextView) helper.getView(R.id.item_wdyh_lb_zj_tv);
        yyscTv = (TextView) helper.getView(R.id.item_wdyh_lb_sc_tv);
        yyztTv = (TextView) helper.getView(R.id.item_wdyh_lb_yyzt_tv);
        yylxTv = (TextView) helper.getView(R.id.item_wdyh_lb_yylx_tv);

        setData(item);
    }

    public void setData(final GetWdyhOrderDetailModel model) {
        if (model == null) {
            return;
        }

        final boolean isPayUser = MeService.getUid().equals(model.getPayUserIdStr());
        boolean isReceiveUser = MeService.getUid().equals(model.getReceiveUserIdStr());
        //正常情况下，后端会做判断，不能同时是付款方和收款方，如果同时都是真的，当成只是付款方来处理
        //如果两者都不是，说明这个订单请求出错，弹个窗出来，不能再进行任何操作，必须退出
        //也就是，同时都是两者是有问题的，同时都不是两者也是有问题的，应该都弹个窗出来，不能进行任何操作，但用户此时已经付款，只能让他找客服了
        if (isPayUser && isReceiveUser) {
            //弹窗报错，退出，让用户找客服
            NToast.shortToast(mContext, "付款方和收款方相同，出错");
            return;
        } else if (!isPayUser && !isReceiveUser) {
            //弹窗报错，退出，让用户找客服
            NToast.shortToast(mContext, "既不是付款方也不是收款方，出错");
            return;
        }

        setUserDetail(model, isPayUser, isReceiveUser);

        SkillModel skillModel = null;
        try {
            skillModel = JsonMananger.jsonToBean(model.getYyxm(), SkillModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (skillModel != null) {
            ztTv.setText("主题：" + skillModel.getName());
        }

        zjTv.setText("¥" + model.getTotalPayment() + "元");
        yyscTv.setText(model.getYysc() + "小时");

        String status = statusToJindu(model.getStatus(), isPayUser, isReceiveUser);
        yyztTv.setText(status);
        String orderType = getOrderType(model.getOrderType(), isPayUser, isReceiveUser);
        yylxTv.setText(orderType);

        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WdyhDetailActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("userId", model.getReceiveUserIdStr());
                bundle.putString("msztOrderId", model.getWdyhOrderId());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });


    }

    //订单显示对方的姓名、性别、年龄、头像
    //根据自己是付款方，还是收款方来显示
    private void setUserDetail(GetWdyhOrderDetailModel model, boolean isPayUser, boolean isReceiveUser) {
        if (isPayUser) {
            GlideUtils.load(mContext, model.getReceiveUser().getPortraitUri(), portraitIv);
            nameTv.setText(model.getReceiveUser().getNickname());
            ageSexView.setVisibility(View.VISIBLE);
            ageSexView.setAgeAndSex(model.getReceiveUser().getAge(), model.getReceiveUser().getSex());
        }
        if (isReceiveUser) {
            GlideUtils.load(mContext, model.getPayUser().getPortraitUri(), portraitIv);
            nameTv.setText(model.getPayUser().getNickname());
            ageSexView.setVisibility(View.VISIBLE);
            ageSexView.setAgeAndSex(model.getPayUser().getAge(), model.getPayUser().getSex());
        }
    }

    //服务器返回的状态，转成相对应的进度
    //这里非常重要，后期一定要仔细测这一部分，考虑全面
    private String statusToJindu(int status, boolean isPayUser, boolean isReceiveUser) {
        if (status == 0) {
            return SealConst.MSZT_ORDER_STRING_DFYFK;
        } else if (status == 1) {
            return SealConst.MSZT_ORDER_STRING_DJS;
        } else if (status == 2) {
            return SealConst.MSZT_ORDER_STRING_DFQK;
        } else if (status == 3) {
            return SealConst.MSZT_ORDER_STRING_DQR;
        } else if (status == 4) {
            return SealConst.MSZT_ORDER_STRING_DPJ;
        } else if (status == 5) {
            return SealConst.MSZT_ORDER_STRING_YiJieShu;
        }
        return SealConst.MSZT_ORDER_STRING_YiJieShu;
    }

    //订单类型
    private String getOrderType(int orderType, boolean isPayUser, boolean isReceiveUser) {
        if (orderType == SealConst.WDYH_ORDER_TYPE_MSZT) {//1是马上租Ta类型
            if (isPayUser) {
                return "我约Ta";
            } else {
                return "Ta约我";
            }
        } else {//2是发布报名类型
            if (isPayUser) {
                return "我的发布";
            } else {
                return "我的报名";
            }
        }
    }

}
