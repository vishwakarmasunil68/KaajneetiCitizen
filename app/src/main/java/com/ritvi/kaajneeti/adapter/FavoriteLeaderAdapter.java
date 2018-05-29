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
import com.ritvi.kaajneeti.activity.user.SelectFavoriteLeaderActivity;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sunil on 03-11-2017.
 */

public class FavoriteLeaderAdapter extends RecyclerView.Adapter<FavoriteLeaderAdapter.ViewHolder> implements WebServicesCallBack {
    private List<UserProfilePOJO> items;
    Activity activity;
    Fragment fragment;
    int device_height = 0;

    public FavoriteLeaderAdapter(Activity activity, Fragment fragment, List<UserProfilePOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_leaders, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_name.setText(items.get(position).getFirstName()+" "+items.get(position).getLastName());
        holder.tv_email.setText(items.get(position).getEmail());

        Glide.with(activity.getApplicationContext())
                .load(items.get(position).getProfilePhotoPath())
                .placeholder(R.drawable.ic_default_profile_pic)
                .error(R.drawable.ic_default_profile_pic)
                .into(holder.cv_profile_pic);

        holder.ll_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activity instanceof SelectFavoriteLeaderActivity){
                    SelectFavoriteLeaderActivity favoriteLeaderActivity= (SelectFavoriteLeaderActivity) activity;
                    favoriteLeaderActivity.selectLeader(items.get(position));
                }
            }
        });

        holder.itemView.setTag(items.get(position));
    }

    private final String TAG = getClass().getSimpleName();

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onGetMsg(String apicall, String response) {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView cv_profile_pic;
        public TextView tv_name;
        public TextView tv_email;
        public ImageView iv_tag_check;
        public LinearLayout ll_user;

        public ViewHolder(View itemView) {
            super(itemView);
            cv_profile_pic = itemView.findViewById(R.id.cv_profile_pic);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_email = itemView.findViewById(R.id.tv_email);
            ll_user = itemView.findViewById(R.id.ll_user);
            iv_tag_check = itemView.findViewById(R.id.iv_tag_check);
        }
    }
}
