package com.ritvi.kaajneeti.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.fragment.analyze.AllComplaintFragment;
import com.ritvi.kaajneeti.fragment.analyze.AllEventFragment;
import com.ritvi.kaajneeti.fragment.analyze.AllPollFragment;
import com.ritvi.kaajneeti.fragment.analyze.AllPostFragment;
import com.ritvi.kaajneeti.pojo.user.SummaryPOJO;

import java.util.List;

/**
 * Created by sunil on 03-11-2017.
 */

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.ViewHolder> {
    private List<SummaryPOJO> items;
    Activity activity;
    Fragment fragment;

    String user_id;
    String profile_id;
    public SummaryAdapter(Activity activity, Fragment fragment, List<SummaryPOJO> items, String user_id, String profile_id) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
        this.user_id=user_id;
        this.profile_id=profile_id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.infalte_activity_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

//        Log.d(TagUtils.getTag(),"summary:-"+items.get(position).toString());

        holder.tv_total.setText(items.get(position).getTotal());
        holder.tv_type.setText(items.get(position).getType());


        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.IS_SEARCH, false);
                bundle.putString(Constants.SEARCH_TEXT, "");
                bundle.putString(Constants.FRIEND_USER_PROFILE_ID, profile_id);
                bundle.putString(Constants.FRIEND_USER_ID, user_id);

                if (activity instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) activity;
                    switch (items.get(position).getType().toLowerCase()) {
                        case "connect":
//                            FriendsListFragment friendsListFragment = new FriendsListFragment(user_id,profile_id);
//                            homeActivity.replaceFragmentinFrameHome(friendsListFragment,"friendsListFragment");
                            break;
                        case "event":
                            AllEventFragment allEventFragment = new AllEventFragment();
                            allEventFragment.setArguments(bundle);
                            homeActivity.startFragment(R.id.frame_home,allEventFragment);
                            break;
                        case "poll":
                            AllPollFragment allPollFragment = new AllPollFragment();
                            allPollFragment.setArguments(bundle);
                            homeActivity.replaceFragmentinFrameHome(allPollFragment,"allPollFragment");
                            break;
                        case "post":
                            AllPostFragment allPostListFragment = new AllPostFragment();
                            allPostListFragment.setArguments(bundle);
                            homeActivity.replaceFragmentinFrameHome(allPostListFragment,"allPostListFragment");
                            break;
                        case "complaint":
                            AllComplaintFragment complaintListFragment = new AllComplaintFragment();
                            complaintListFragment.setArguments(bundle);
                            homeActivity.replaceFragmentinFrameHome(complaintListFragment,"complaintListFragment");
                            break;
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
        public TextView tv_total;
        public TextView tv_type;
        public FrameLayout ll_item;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_item = itemView.findViewById(R.id.ll_item);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_total = itemView.findViewById(R.id.tv_total);
        }
    }
}
