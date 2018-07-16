package com.ritvi.kaajneeti.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.fragment.home.AnalyzeFragment;
import com.ritvi.kaajneeti.pojo.analyze.AnalyzeFeedType;

import java.util.List;

/**
 * Created by sunil on 03-11-2017.
 */

public class ConnectionAnalyzeAdapter extends RecyclerView.Adapter<ConnectionAnalyzeAdapter.ViewHolder> {
    private List<AnalyzeFeedType> items;
    Activity activity;
    Fragment fragment;

    public ConnectionAnalyzeAdapter(Activity activity, Fragment fragment, List<AnalyzeFeedType> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_connect_analyze_feed_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_count.setText(items.get(position).getFeed_count());
        holder.tv_feed_type.setText(items.get(position).getFeed_type());

        holder.ll_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment instanceof AnalyzeFragment) {
                    AnalyzeFragment analyzeFragment = (AnalyzeFragment) fragment;
                    switch (position) {
                        case 0:
                            analyzeFragment.goToFriends();
                            break;
                        case 1:analyzeFragment.goToFollower();
                            break;
                        case 2:
                            analyzeFragment.goToFollowing();
                            break;
                        case 3:
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

        public TextView tv_count;
        public TextView tv_feed_type;
        public LinearLayout ll_feed;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_feed = itemView.findViewById(R.id.ll_feed);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_feed_type = itemView.findViewById(R.id.tv_feed_type);
        }
    }
}
