package com.ritvi.kaajneeti.fragment.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.adapter.HomeFeedAdapter;
import com.ritvi.kaajneeti.fragment.feed.CommentFragment;
import com.ritvi.kaajneeti.fragment.feed.ComplaintCommentFragment;
import com.ritvi.kaajneeti.fragment.feed.EventCommentFragment;
import com.ritvi.kaajneeti.fragment.feed.PollCommentFragment;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.interfaces.OnLoadMoreListener;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintPOJO;
import com.ritvi.kaajneeti.pojo.event.EventPOJO;
import com.ritvi.kaajneeti.pojo.poll.PollPOJO;
import com.ritvi.kaajneeti.pojo.post.PostPOJO;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponseList;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class AllFeedsFragment extends FragmentController{

    int range = 10;
    @BindView(R.id.rv_feeds)
    RecyclerView rv_feeds;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.pb_loader)
    ProgressBar pb_loader;

    String feed_type="";
    public AllFeedsFragment(String feed_type){
        this.feed_type=feed_type;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_all_feeds,container,false);
        setUpView(getActivity(),this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachAdapter();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllData(true,0);
            }
        });
    }

    boolean initialize=false;

    public void initializeData(){
        if(!initialize){
            initialize=true;
            getAllData(false,0);
        }
    }

    HomeFeedAdapter homeFeedAdapter;
    List<FeedPOJO> feedPOJOS = new ArrayList<>();

    public void attachAdapter() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_feeds.setHasFixedSize(true);
        rv_feeds.setLayoutManager(linearLayoutManager);
        homeFeedAdapter = new HomeFeedAdapter(rv_feeds, getActivity(), this, feedPOJOS, getChildFragmentManager());
        rv_feeds.setAdapter(homeFeedAdapter);
        rv_feeds.setNestedScrollingEnabled(true);
        rv_feeds.setItemAnimator(new DefaultItemAnimator());

        homeFeedAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d(TagUtils.getTag(), "item loading");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FeedPOJO feedPOJO = new FeedPOJO();
                        feedPOJO.setFeedtype(null);
                        feedPOJOS.add(feedPOJO);
                        homeFeedAdapter.notifyItemInserted(feedPOJOS.size() - 1);
                        getAllData(false,feedPOJOS.size());
                    }
                }, 0);
            }
        });

    }

    public void getAllData(final boolean is_refreshing, int start_position) {


        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(start_position)));
        nameValuePairs.add(new BasicNameValuePair("end", String.valueOf(range)));
        if(!feed_type.equalsIgnoreCase("all")) {
            nameValuePairs.add(new BasicNameValuePair("feed_type", feed_type));
        }
        new WebServiceBaseResponseList<FeedPOJO>(nameValuePairs, getActivity(), new ResponseListCallback<FeedPOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO<FeedPOJO> responseListPOJO) {
                pb_loader.setVisibility(View.GONE);
                try {
//                    feedPOJOS.clear();
                    removeLastPosition();
                    if (responseListPOJO.isSuccess()) {
                        Log.d(TagUtils.getTag(), "response length:-" + responseListPOJO.getResultList().size());
                        if (is_refreshing) {
                            feedPOJOS.clear();
                        }
                        feedPOJOS.addAll(responseListPOJO.getResultList());
                        homeFeedAdapter.notifyDataSetChanged();

                    } else {
                        ToastClass.showShortToast(getActivity(), responseListPOJO.getMessage());
                    }
                    swipeRefreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, FeedPOJO.class, "CALL_FEED_DATA", false).execute(WebServicesUrls.HOME_PAGE_DATA);

    }

    public void removeLastPosition() {

        if (feedPOJOS.size() > 0) {
            if(feedPOJOS.size()>1&&feedPOJOS.get(feedPOJOS.size()-1).getFeedtype()==null) {
                feedPOJOS.remove(feedPOJOS.size() - 1);
                homeFeedAdapter.notifyItemRemoved(feedPOJOS.size());
            }
        }
    }

    TextView tv_comments;
    public void showComment(TextView tv_comments, PostPOJO postPOJO) {
        this.tv_comments=tv_comments;
        CommentFragment commentFragment=new CommentFragment();
        Bundle bundle=new Bundle();
        bundle.putString("post_id",postPOJO.getPostId());
        commentFragment.setArguments(bundle);
        activityManager.startFragmentForResult(R.id.frame_home,AllFeedsFragment.this,commentFragment,101);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if(requestCode==101){
            try {
                int count = data.getInt("total_comments_added");
                Log.d(TagUtils.getTag(),"count added:-"+count);
                count=count+Integer.parseInt(tv_comments.getText().toString());
                tv_comments.setText(String.valueOf(count));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void showPollComments(TextView tv_comments, PollPOJO pollPOJO) {
        this.tv_comments=tv_comments;
        PollCommentFragment commentFragment=new PollCommentFragment();
        Bundle bundle=new Bundle();
        bundle.putString("poll_id",pollPOJO.getPollId());
        commentFragment.setArguments(bundle);
        activityManager.startFragmentForResult(R.id.frame_home,AllFeedsFragment.this,commentFragment,101);
    }

    public void showEventComment(TextView tv_comments, EventPOJO eventPOJO) {
        this.tv_comments=tv_comments;
        EventCommentFragment commentFragment=new EventCommentFragment();
        Bundle bundle=new Bundle();
        bundle.putString("event_id",eventPOJO.getEventId());
        commentFragment.setArguments(bundle);
        activityManager.startFragmentForResult(R.id.frame_home,AllFeedsFragment.this,commentFragment,101);
    }

    public void showComplaintComments(TextView tv_comments, ComplaintPOJO complaintPOJO) {
        this.tv_comments=tv_comments;
        ComplaintCommentFragment commentFragment=new ComplaintCommentFragment();
        Bundle bundle=new Bundle();
        bundle.putString("complaint_id",complaintPOJO.getComplaintId());
        commentFragment.setArguments(bundle);
        activityManager.startFragmentForResult(R.id.frame_home,AllFeedsFragment.this,commentFragment,101);
    }
}
