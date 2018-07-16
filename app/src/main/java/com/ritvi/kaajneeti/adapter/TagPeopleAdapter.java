package com.ritvi.kaajneeti.adapter;

import android.app.Activity;
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
import com.ritvi.kaajneeti.activity.express.TagPeopleActivity;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sunil on 03-11-2017.
 */

public class TagPeopleAdapter extends RecyclerView.Adapter<TagPeopleAdapter.ViewHolder> {
    private List<UserProfilePOJO> items;
    Activity activity;
    Fragment fragment;
    List<UserProfilePOJO> taggedUserProfilePOJOS;

    public TagPeopleAdapter(Activity activity, Fragment fragment, List<UserProfilePOJO> items, List<UserProfilePOJO> taggedUserProfilePOJOS) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
        this.taggedUserProfilePOJOS = taggedUserProfilePOJOS;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.infalte_tag_people, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_name.setText(items.get(position).getFirstName() + " " + items.get(position).getLastName());
        holder.tv_email.setText(items.get(position).getEmail());

        Glide.with(activity.getApplicationContext())
                .load(items.get(position).getProfilePhotoPath())
                .placeholder(R.drawable.ic_default_profile_pic)
                .error(R.drawable.ic_default_profile_pic)
                .into(holder.cv_profile_pic);


        holder.ll_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        boolean is_checked = false;
        for (UserProfilePOJO userProfilePOJO : taggedUserProfilePOJOS) {
            if (userProfilePOJO.getUserProfileId().equalsIgnoreCase(items.get(position).getUserProfileId())) {
                is_checked = true;
            }
        }

        if (is_checked) {
            holder.iv_tag_check.setVisibility(View.VISIBLE);
        } else {
            holder.iv_tag_check.setVisibility(View.GONE);
        }
        if (items.get(position).getUserTypeId().equalsIgnoreCase("1")) {
            holder.iv_crown.setVisibility(View.GONE);
        } else {
            holder.iv_crown.setVisibility(View.VISIBLE);
        }
        holder.ll_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof TagPeopleActivity) {
                    TagPeopleActivity tagPeopleActivity = (TagPeopleActivity) activity;
                    if (holder.iv_tag_check.getVisibility() == View.VISIBLE) {
                        holder.iv_tag_check.setVisibility(View.GONE);
                        tagPeopleActivity.removeUser(items.get(position));
                    } else {
                        holder.iv_tag_check.setVisibility(View.VISIBLE);
                        tagPeopleActivity.addUser(items.get(position));
                    }
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
        public CircleImageView cv_profile_pic;
        public TextView tv_name;
        public TextView tv_email;
        public ImageView iv_tag_check;
        public ImageView iv_crown;
        public LinearLayout ll_user;

        public ViewHolder(View itemView) {
            super(itemView);
            cv_profile_pic = itemView.findViewById(R.id.cv_profile_pic);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_email = itemView.findViewById(R.id.tv_email);
            ll_user = itemView.findViewById(R.id.ll_user);
            iv_tag_check = itemView.findViewById(R.id.iv_tag_check);
            iv_crown = itemView.findViewById(R.id.iv_crown);
        }
    }
}
