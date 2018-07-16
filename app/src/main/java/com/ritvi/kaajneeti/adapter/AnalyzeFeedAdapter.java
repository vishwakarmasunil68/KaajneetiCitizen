package com.ritvi.kaajneeti.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.activity.user.AllLeaderActivity;
import com.ritvi.kaajneeti.fragment.home.AnalyzeFragment;
import com.ritvi.kaajneeti.pojo.analyze.AnalyzeFeedType;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.AdapterWebService;
import com.ritvi.kaajneeti.webservice.MsgPassInterface;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sunil on 03-11-2017.
 */

public class AnalyzeFeedAdapter extends RecyclerView.Adapter<AnalyzeFeedAdapter.ViewHolder>{
    private List<AnalyzeFeedType> items;
    Activity activity;
    Fragment fragment;

    public AnalyzeFeedAdapter(Activity activity, Fragment fragment, List<AnalyzeFeedType> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_feed_type, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.iv_feed_post.setImageResource(items.get(position).getFeed_drawable());
        holder.tv_feed_type.setText(items.get(position).getFeed_type()+" ( "+items.get(position).getFeed_count()+" )");

        holder.ll_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment instanceof AnalyzeFragment) {
                    AnalyzeFragment analyzeFragment= (AnalyzeFragment) fragment;
                    switch (position) {
                        case 0:
                            analyzeFragment.goToPosts();
                            break;
                        case 1:
                            analyzeFragment.goToEvents();
                            break;
                        case 2:
                            analyzeFragment.goToPolls();
                            break;
                        case 3:
                            analyzeFragment.goToSuggestions();
                            break;
                        case 4:
                            analyzeFragment.goToComplaints();
                            break;
                        case 5:
                            analyzeFragment.goToInformation();
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

        public ImageView iv_feed_post;
        public TextView tv_feed_type;
        public LinearLayout ll_feed;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_feed = itemView.findViewById(R.id.ll_feed);
            iv_feed_post = itemView.findViewById(R.id.iv_feed_post);
            tv_feed_type = itemView.findViewById(R.id.tv_feed_type);
        }
    }
}
