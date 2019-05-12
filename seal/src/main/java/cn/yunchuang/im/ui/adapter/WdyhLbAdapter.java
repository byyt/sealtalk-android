package cn.yunchuang.im.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itheima.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import cn.yunchuang.im.MeService;
import cn.yunchuang.im.R;
import cn.yunchuang.im.SealConst;
import cn.yunchuang.im.server.response.GetWdyhBaseOrderLbModel;
import cn.yunchuang.im.server.response.GetWdyhOrderLbModel;
import cn.yunchuang.im.server.response.SkillModel;
import cn.yunchuang.im.server.utils.NToast;
import cn.yunchuang.im.server.utils.json.JsonMananger;
import cn.yunchuang.im.ui.activity.WdyhXqActivity;
import cn.yunchuang.im.ui.widget.AgeSexView;
import cn.yunchuang.im.utils.GlideUtils;

/**
 * 放松入口
 * Created by mulinrui on 2017/10/12.
 */
public class WdyhLbAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "WdyhLbAdapter";
    private Context context;

    private List<GetWdyhOrderLbModel> baseOrderModelList = new ArrayList<>();

    public static final int MS_ORDER_TYPE_MSZT = 0;//订单类型是：马上租Ta
    public static final int MS_ORDER_TYPE_MSFB = 1;//订单类型是：发布报名

    public WdyhLbAdapter(Context context) {
        this.context = context;
    }


    public List<GetWdyhOrderLbModel> getData() {
        return baseOrderModelList;
    }

    public void replaceData(@NonNull List<GetWdyhOrderLbModel> data) {
        baseOrderModelList.clear();
        baseOrderModelList.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(@NonNull List<GetWdyhOrderLbModel> newData) {
        baseOrderModelList.addAll(newData);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return baseOrderModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return baseOrderModelList.get(position).getOrderType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        if (viewType == MS_ORDER_TYPE_MSZT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_wdyh_lb_adapter, parent, false);
            return new MsztViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_wdyh_lb_adapter, parent, false);
            return new MsfbViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MsztViewHolder) {
            ((MsztViewHolder) holder).setData(baseOrderModelList.get(position));
        } else {
            ((MsfbViewHolder) holder).setData(baseOrderModelList.get(position));
        }
    }


    private class MsztViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout rootLayout;
        private RoundedImageView portraitIv;
        private TextView nameTv;
        private AgeSexView ageSexView;
        private TextView ztTv;
        private TextView zjTv;
        private TextView yyscTv;
        private TextView yyztTv;
        private TextView yylxTv;

        public MsztViewHolder(View view) {
            super(view);

            rootLayout = (LinearLayout) view.findViewById(R.id.item_wdyh_lb_layout);
            portraitIv = (RoundedImageView) view.findViewById(R.id.item_wdyh_lb_portrait);
            nameTv = (TextView) view.findViewById(R.id.item_wdyh_lb_nickname);
            ageSexView = (AgeSexView) view.findViewById(R.id.item_wdyh_lb_age_sex_view);
            ztTv = (TextView) view.findViewById(R.id.item_wdyh_lb_zt_tv);
            zjTv = (TextView) view.findViewById(R.id.item_wdyh_lb_zj_tv);
            yyscTv = (TextView) view.findViewById(R.id.item_wdyh_lb_sc_tv);
            yyztTv = (TextView) view.findViewById(R.id.item_wdyh_lb_yyzt_tv);
            yylxTv = (TextView) view.findViewById(R.id.item_wdyh_lb_yylx_tv);
        }

        public void setData(GetWdyhBaseOrderLbModel getWdyhBaseOrderLbModel) {
            if (getWdyhBaseOrderLbModel instanceof GetWdyhOrderLbModel) {
                final GetWdyhOrderLbModel model = ((GetWdyhOrderLbModel) getWdyhBaseOrderLbModel);

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
                    NToast.shortToast(context, "付款方和收款方相同，出错");
                    return;
                } else if (!isPayUser && !isReceiveUser) {
                    //弹窗报错，退出，让用户找客服
                    NToast.shortToast(context, "既不是付款方也不是收款方，出错");
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
                        Intent intent = new Intent(context, WdyhXqActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putString("userId", model.getReceiveUserIdStr());
                        bundle.putString("msztOrderId", model.getWdyhOrderId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });

            }

        }

        //订单显示对方的姓名、性别、年龄、头像
        //根据自己是付款方，还是收款方来显示
        private void setUserDetail(GetWdyhOrderLbModel model, boolean isPayUser, boolean isReceiveUser) {
            if (isPayUser) {
                GlideUtils.load(context, model.getReceiveUser().getPortraitUri(), portraitIv);
                nameTv.setText(model.getReceiveUser().getNickname());
                ageSexView.setVisibility(View.VISIBLE);
                ageSexView.setAgeAndSex(model.getReceiveUser().getAge(), model.getReceiveUser().getSex());
            }
            if (isReceiveUser) {
                GlideUtils.load(context, model.getPayUser().getPortraitUri(), portraitIv);
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

    private class MsfbViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView portraitIv;
        private TextView nameTv;
        private AgeSexView ageSexView;
        private TextView ztTv;
        private TextView zjTv;
        private TextView yfkTv;
        private TextView yysjTv;
        private TextView yyscTv;
        private TextView yyddTv;

        public MsfbViewHolder(View view) {
            super(view);

            portraitIv = (RoundedImageView) view.findViewById(R.id.activity_wdxq_xq_portrait);
            nameTv = (TextView) view.findViewById(R.id.activity_wdxq_xq_nickname);
            ageSexView = (AgeSexView) view.findViewById(R.id.activity_wdxq_xq_age_sex_view);
            ztTv = (TextView) view.findViewById(R.id.activity_wdxq_xq_zt_tv);
            zjTv = (TextView) view.findViewById(R.id.activity_wdxq_xq_zj_tv);
            yfkTv = (TextView) view.findViewById(R.id.activity_wdxq_xq_yfk_tv);
            yysjTv = (TextView) view.findViewById(R.id.activity_wdxq_xq_yysj_tv);
            yyscTv = (TextView) view.findViewById(R.id.activity_wdxq_xq_yysc_tv);
            yyddTv = (TextView) view.findViewById(R.id.activity_wdxq_xq_yydd_tv);
        }

        public void setData(GetWdyhBaseOrderLbModel getWdyhBaseOrderLbModel) {

        }

    }

}
