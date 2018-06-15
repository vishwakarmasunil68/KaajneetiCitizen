package com.ritvi.kaajneeti.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintHistoryPOJO;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sunil on 03-11-2017.
 */

public class ComplaintHistoryAdapter extends RecyclerView.Adapter<ComplaintHistoryAdapter.ViewHolder> {
    private List<ComplaintHistoryPOJO> items;
    Activity activity;
    Fragment fragment;

    public ComplaintHistoryAdapter(Activity activity, Fragment fragment, List<ComplaintHistoryPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_complaint_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_date.setText(items.get(position).getAddedOn());
        holder.tv_description.setText(items.get(position).getHistoryDescription());

        String profile_name = items.get(position).getComplaintHistoryProfile().getFirstName() + " " + items.get(position).getComplaintHistoryProfile().getLastName() + "</b>";
        holder.tv_profile_user.setText(Html.fromHtml(profile_name));

        Glide.with(activity.getApplicationContext())
                .load(items.get(position).getComplaintHistoryProfile().getProfilePhotoPath())
                .placeholder(R.drawable.ic_default_profile_pic)
                .error(R.drawable.ic_default_profile_pic)
                .dontAnimate()
                .into(holder.cv_profile_pic);

        holder.cv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity instanceof HomeActivity) {

                }
            }
        });
        if(position==items.size()-1){
            holder.view_bottom.setVisibility(View.GONE);
        }else{
            holder.view_bottom.setVisibility(View.VISIBLE);
        }

        holder.itemView.setTag(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_description, tv_profile_user, tv_date;
        public CircleImageView cv_profile_pic;
        public View view_bottom;

        public ViewHolder(View itemView) {
            super(itemView);
            cv_profile_pic = itemView.findViewById(R.id.cv_profile_pic);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_profile_user = itemView.findViewById(R.id.tv_profile_user);
            tv_date = itemView.findViewById(R.id.tv_date);
            view_bottom = itemView.findViewById(R.id.view_bottom);
        }
    }
}
