package com.ritvi.kaajneeti.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.fragment.wallet.ContributeAmountFragment;
import com.ritvi.kaajneeti.fragment.wallet.SelectUserForContributionFragment;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sunil on 03-11-2017.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private List<FeedPOJO> items;
    Activity activity;
    Fragment fragment;

    public FriendAdapter(Activity activity, Fragment fragment, List<FeedPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_friends_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_name.setText(items.get(position).getProfiledata().getFirstName() + " " + items.get(position).getProfiledata().getLastName());
        holder.tv_email.setText(items.get(position).getProfiledata().getEmail());
        Glide.with(activity.getApplicationContext())
                .load(items.get(position).getProfiledata().getProfilePhotoPath())
                .placeholder(R.drawable.ic_default_profile_pic)
                .error(R.drawable.ic_default_profile_pic)
                .dontAnimate()
                .into(holder.cv_profile_pic);

        holder.ll_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment instanceof SelectUserForContributionFragment) {
                    SelectUserForContributionFragment selectUserForContributionFragment = (SelectUserForContributionFragment) fragment;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userProfilePOJO", items.get(position).getProfiledata());
                    ContributeAmountFragment contributeAmountFragment = new ContributeAmountFragment();
                    contributeAmountFragment.setArguments(bundle);
                    selectUserForContributionFragment.activityManager.startFragmentForResult(R.id.frame_home, selectUserForContributionFragment,contributeAmountFragment,101);
                }
            }
        });

        holder.itemView.setTag(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cv_profile_pic)
        CircleImageView cv_profile_pic;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_email)
        TextView tv_email;
        @BindView(R.id.ll_user)
        LinearLayout ll_user;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
