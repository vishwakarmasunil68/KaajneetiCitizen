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
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.fragment.user.UserProfileFragment;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sunil on 03-11-2017.
 */

public class FriendsHorizontalAdapter extends RecyclerView.Adapter<FriendsHorizontalAdapter.ViewHolder> {
    private List<UserProfilePOJO> items;
    Activity activity;
    Fragment fragment;
    boolean is_complaint;

    public FriendsHorizontalAdapter(Activity activity, Fragment fragment, List<UserProfilePOJO> items, boolean is_complaint) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
        this.is_complaint = is_complaint;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_friends_horizontal_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Glide.with(activity.getApplicationContext())
                .load(items.get(position).getProfilePhotoPath())
                .placeholder(R.drawable.ic_default_profile_pic)
                .error(R.drawable.ic_default_profile_pic)
                .dontAnimate()
                .into(holder.cv_profile_pic);

        holder.tv_profile_name.setText(items.get(position).getFirstName() + " " + items.get(position).getLastName());
        holder.ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) activity;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user_profile_id", items.get(position).getUserProfileId());
                    UserProfileFragment userProfileFragment = new UserProfileFragment();
                    userProfileFragment.setArguments(bundle);
                    homeActivity.replaceFragmentinFrameHome(userProfileFragment, userProfileFragment.getClass().getSimpleName());
                }
            }
        });

        if (is_complaint) {
            holder.tv_complaint_status.setVisibility(View.VISIBLE);
            if (items.get(position).getAcceptedYesNo().equalsIgnoreCase("1")) {
                holder.tv_complaint_status.setBackgroundResource(R.drawable.complaint_accepter_back);
                holder.tv_complaint_status.setTextColor(activity.getResources().getColor(R.color.complaint_accepted));
                holder.tv_complaint_status.setText("Accepted");
            } else if (items.get(position).getAcceptedYesNo().equalsIgnoreCase("-1")) {
                holder.tv_complaint_status.setBackgroundResource(R.drawable.complaint_rejected_back);
                holder.tv_complaint_status.setTextColor(activity.getResources().getColor(R.color.complaint_rejected));
                holder.tv_complaint_status.setText("Rejected");
            } else {
                holder.tv_complaint_status.setBackgroundResource(R.drawable.complaint_pending);
                holder.tv_complaint_status.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
                holder.tv_complaint_status.setText("Pending");
            }
        } else {
            holder.tv_complaint_status.setVisibility(View.GONE);
        }

        holder.itemView.setTag(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_profile_name)
        public TextView tv_profile_name;
        @BindView(R.id.cv_profile_pic)
        public CircleImageView cv_profile_pic;
        @BindView(R.id.ll_profile)
        public LinearLayout ll_profile;
        @BindView(R.id.tv_complaint_status)
        public TextView tv_complaint_status;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
