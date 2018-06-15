package com.ritvi.kaajneeti.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintCommentPOJO;
import com.ritvi.kaajneeti.pojo.event.EventCommentPOJO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sunil on 03-11-2017.
 */

public class ComplaintCommentAdapter extends RecyclerView.Adapter<ComplaintCommentAdapter.ViewHolder> {
    private List<ComplaintCommentPOJO> items;
    Activity activity;
    Fragment fragment;

    public ComplaintCommentAdapter(Activity activity, Fragment fragment, List<ComplaintCommentPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_post_comments, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_name.setText(items.get(position).getCommentProfile().getFirstName() + " " + items.get(position).getCommentProfile().getLastName());
        holder.tv_comment.setText(items.get(position).getCommentText());
        Glide.with(activity.getApplicationContext())
                .load(items.get(position).getCommentProfile().getProfilePhotoPath())
                .placeholder(R.drawable.ic_default_profile_pic)
                .error(R.drawable.ic_default_profile_pic)
                .dontAnimate()
                .into(holder.cv_profile_pic);

        holder.tv_commented_on.setText(items.get(position).getCommentOn());
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
        @BindView(R.id.tv_comment)
        TextView tv_comment;
        @BindView(R.id.tv_commented_on)
        TextView tv_commented_on;
        @BindView(R.id.ll_user)
        LinearLayout ll_user;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
