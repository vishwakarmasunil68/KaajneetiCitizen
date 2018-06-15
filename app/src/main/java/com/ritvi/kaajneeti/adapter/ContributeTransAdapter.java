package com.ritvi.kaajneeti.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.fragment.wallet.ContributeAmountFragment;
import com.ritvi.kaajneeti.fragment.wallet.SelectUserForContributionFragment;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;
import com.ritvi.kaajneeti.pojo.payment.PaymentDataPOJO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sunil on 03-11-2017.
 */

public class ContributeTransAdapter extends RecyclerView.Adapter<ContributeTransAdapter.ViewHolder> {
    private List<FeedPOJO> items;
    Activity activity;
    Fragment fragment;

    public ContributeTransAdapter(Activity activity, Fragment fragment, List<FeedPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_contribute_trans_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(items.get(position).getFeedtype().equalsIgnoreCase(Constants.PAYMENT_FEED_MONEY)){
            PaymentDataPOJO paymentDataPOJO=items.get(position).getPaymentDataPOJO();
            if(paymentDataPOJO.getPaymentBy().getUserProfileId().equalsIgnoreCase(Constants.userProfilePOJO.getUserProfileId())
                    &&paymentDataPOJO.getPaymentTo().getUserProfileId().equalsIgnoreCase(Constants.userProfilePOJO.getUserProfileId())) {
                holder.iv_money_status.setImageResource(R.drawable.ic_add_money);
                holder.tv_money_status.setText("Money Added");
                holder.tv_profile_name.setText("From: "+paymentDataPOJO.getPaymentBy().getFirstName()+" "+paymentDataPOJO.getPaymentBy().getLastName());
                holder.tv_amount.setText("+ Rs. "+paymentDataPOJO.getTransactionAmount());
                holder.tv_amount.setTextColor(Color.parseColor("#000000"));
            }else if(paymentDataPOJO.getPaymentBy().getUserProfileId().equalsIgnoreCase(Constants.userProfilePOJO.getUserProfileId())){
                holder.iv_money_status.setImageResource(R.drawable.ic_sent_money);
                holder.tv_money_status.setText("Money Sent");
                holder.tv_profile_name.setText("To: "+paymentDataPOJO.getPaymentTo().getFirstName()+" "+paymentDataPOJO.getPaymentTo().getLastName());
                holder.tv_amount.setText("- Rs. "+paymentDataPOJO.getTransactionAmount());
                holder.tv_amount.setTextColor(Color.parseColor("#FF0000"));
            }else if(paymentDataPOJO.getPaymentTo().getUserProfileId().equalsIgnoreCase(Constants.userProfilePOJO.getUserProfileId())){
                holder.iv_money_status.setImageResource(R.drawable.ic_add_money);
                holder.tv_money_status.setText("Money Received");
                holder.tv_profile_name.setText("From: "+paymentDataPOJO.getPaymentBy().getFirstName()+" "+paymentDataPOJO.getPaymentBy().getLastName());
                holder.tv_amount.setText("+ Rs. "+paymentDataPOJO.getTransactionAmount());
                holder.tv_amount.setTextColor(Color.parseColor("#000000"));
            }
            holder.tv_time.setText(paymentDataPOJO.getAddedOnTime());
        }


        holder.itemView.setTag(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_money_status)
        ImageView iv_money_status;
        @BindView(R.id.tv_money_status)
        TextView tv_money_status;
        @BindView(R.id.tv_profile_name)
        TextView tv_profile_name;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_amount)
        TextView tv_amount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
