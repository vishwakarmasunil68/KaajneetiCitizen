package com.ritvi.kaajneeti.fragment.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.adapter.AnalyzeFeedAdapter;
import com.ritvi.kaajneeti.adapter.ConnectionAnalyzeAdapter;
import com.ritvi.kaajneeti.adapter.LeaderAdapter;
import com.ritvi.kaajneeti.fragment.analyze.AllComplaintFragment;
import com.ritvi.kaajneeti.fragment.analyze.AllEventFragment;
import com.ritvi.kaajneeti.fragment.analyze.AllInformationFragment;
import com.ritvi.kaajneeti.fragment.analyze.AllPollFragment;
import com.ritvi.kaajneeti.fragment.analyze.AllPostFragment;
import com.ritvi.kaajneeti.fragment.analyze.AllSuggestionFragment;
import com.ritvi.kaajneeti.fragment.myconnection.FriendFragment;
import com.ritvi.kaajneeti.fragment.user.FollowerFragment;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.analyze.AnalyzeFeedType;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AnalyzeFragment extends FragmentController {

    @BindView(R.id.ll_feeds)
    LinearLayout ll_feeds;
    @BindView(R.id.ll_feed_header)
    LinearLayout ll_feed_header;
    @BindView(R.id.ll_connections_header)
    LinearLayout ll_connections_header;
    @BindView(R.id.ll_connections)
    LinearLayout ll_connections;
    @BindView(R.id.tv_post)
    TextView tv_post;
    @BindView(R.id.tv_event)
    TextView tv_event;
    @BindView(R.id.tv_poll)
    TextView tv_poll;
    @BindView(R.id.tv_suggestion)
    TextView tv_suggestion;
    @BindView(R.id.tv_complaint)
    TextView tv_complaint;
    @BindView(R.id.tv_information)
    TextView tv_information;
    @BindView(R.id.tv_friends)
    TextView tv_friends;
    @BindView(R.id.tv_favorite_leader)
    TextView tv_favorite_leader;
    @BindView(R.id.tv_followers)
    TextView tv_followers;
    @BindView(R.id.tv_followings)
    TextView tv_followings;


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.pb_loader)
    ProgressBar pb_loader;
    @BindView(R.id.ll_post)
    LinearLayout ll_post;
    @BindView(R.id.ll_friends)
    LinearLayout ll_friends;
    @BindView(R.id.ll_event)
    LinearLayout ll_event;
    @BindView(R.id.ll_poll)
    LinearLayout ll_poll;
    @BindView(R.id.ll_suggestion)
    LinearLayout ll_suggestion;
    @BindView(R.id.ll_complaint)
    LinearLayout ll_complaint;
    @BindView(R.id.ll_information)
    LinearLayout ll_information;
    @BindView(R.id.ll_follower)
    LinearLayout ll_follower;
    @BindView(R.id.ll_following)
    LinearLayout ll_following;
    @BindView(R.id.rv_feeds)
    RecyclerView rv_feeds;
    @BindView(R.id.rv_connections)
    RecyclerView rv_connections;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_analyze, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachFeedAdapter();
        attachConnectionAdapter();

        ll_feed_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_feeds.getVisibility() == View.VISIBLE) {
                    ll_feeds.setVisibility(View.GONE);
                } else {
                    ll_feeds.setVisibility(View.VISIBLE);
                }
            }
        });
        ll_connections_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_connections.getVisibility() == View.VISIBLE) {
                    ll_connections.setVisibility(View.GONE);
                } else {
                    ll_connections.setVisibility(View.VISIBLE);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callAPI();
            }
        });

        ll_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        ll_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ll_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ll_poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ll_suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ll_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ll_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ll_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ll_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    boolean isinitialized = false;

    public void initializeFragment() {
        if (!isinitialized) {
            isinitialized = true;
            callAPI();
        }
    }

    public void goToFriends(){
        FriendFragment friendFragment= new FriendFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_SEARCH, false);
        bundle.putString(Constants.SEARCH_TEXT, "");
        bundle.putString("friend_user_profile_id", Constants.userProfilePOJO.getUserProfileId());
        friendFragment.setArguments(bundle);

        activityManager.startFragment(R.id.frame_home, friendFragment);
    }

    public void goToFollower(){
        FollowerFragment followerFragment= new FollowerFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_follower", false);
        followerFragment.setArguments(bundle);

        activityManager.startFragment(R.id.frame_home, followerFragment);
    }

    public void goToFollowing(){
        AllPostFragment allPostFragment = new AllPostFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_SEARCH, false);
        bundle.putString(Constants.SEARCH_TEXT, "");
        bundle.putString(Constants.FRIEND_USER_PROFILE_ID, Constants.userProfilePOJO.getUserProfileId());
        bundle.putString(Constants.FRIEND_USER_ID, Constants.userProfilePOJO.getUserId());
        allPostFragment.setArguments(bundle);

        activityManager.startFragment(R.id.frame_home, allPostFragment);
    }

    public void goToPosts(){
        AllPostFragment allPostFragment = new AllPostFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_SEARCH, false);
        bundle.putString(Constants.SEARCH_TEXT, "");
        bundle.putString(Constants.FRIEND_USER_PROFILE_ID, Constants.userProfilePOJO.getUserProfileId());
        bundle.putString(Constants.FRIEND_USER_ID, Constants.userProfilePOJO.getUserId());
        allPostFragment.setArguments(bundle);

        activityManager.startFragment(R.id.frame_home, allPostFragment);
    }

    public void goToEvents(){
        AllEventFragment allEventFragment = new AllEventFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_SEARCH, false);
        bundle.putString(Constants.SEARCH_TEXT, "");
        bundle.putString(Constants.FRIEND_USER_PROFILE_ID, Constants.userProfilePOJO.getUserProfileId());
        bundle.putString(Constants.FRIEND_USER_ID, Constants.userProfilePOJO.getUserId());
        allEventFragment.setArguments(bundle);

        activityManager.startFragment(R.id.frame_home, allEventFragment);
    }

    public void goToComplaints(){
        AllComplaintFragment allComplaintFragment = new AllComplaintFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_SEARCH, false);
        bundle.putString(Constants.SEARCH_TEXT, "");
        bundle.putString(Constants.FRIEND_USER_PROFILE_ID, Constants.userProfilePOJO.getUserProfileId());
        bundle.putString(Constants.FRIEND_USER_ID, Constants.userProfilePOJO.getUserId());
        allComplaintFragment.setArguments(bundle);

        activityManager.startFragment(R.id.frame_home, allComplaintFragment);
    }

    public void goToPolls(){
        AllPollFragment allPollFragment = new AllPollFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_SEARCH, false);
        bundle.putString(Constants.SEARCH_TEXT, "");
        bundle.putString(Constants.FRIEND_USER_PROFILE_ID, Constants.userProfilePOJO.getUserProfileId());
        bundle.putString(Constants.FRIEND_USER_ID, Constants.userProfilePOJO.getUserId());
        allPollFragment.setArguments(bundle);

        activityManager.startFragment(R.id.frame_home, allPollFragment);
    }

    public void goToInformation(){
        AllInformationFragment allInformationFragment = new AllInformationFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_SEARCH, false);
        bundle.putString(Constants.SEARCH_TEXT, "");
        bundle.putString(Constants.FRIEND_USER_PROFILE_ID, Constants.userProfilePOJO.getUserProfileId());
        bundle.putString(Constants.FRIEND_USER_ID, Constants.userProfilePOJO.getUserId());
        allInformationFragment.setArguments(bundle);

        activityManager.startFragment(R.id.frame_home, allInformationFragment);
    }

    public void goToSuggestions(){
        AllSuggestionFragment allSuggestionFragment = new AllSuggestionFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_SEARCH, false);
        bundle.putString(Constants.SEARCH_TEXT, "");
        bundle.putString(Constants.FRIEND_USER_PROFILE_ID, Constants.userProfilePOJO.getUserProfileId());
        bundle.putString(Constants.FRIEND_USER_ID, Constants.userProfilePOJO.getUserId());
        allSuggestionFragment.setArguments(bundle);

        activityManager.startFragment(R.id.frame_home, allSuggestionFragment);
    }


    public void callAPI() {
        pb_loader.setVisibility(View.VISIBLE);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        new WebServiceBase(nameValuePairs, getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                swipeRefreshLayout.setRefreshing(false);
                feedTypes.clear();
                pb_loader.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                        JSONObject resultJsonObject = jsonObject.optJSONObject("result");
                        String TotalEvent = resultJsonObject.optString("TotalEvent");
                        String TotalPoll = resultJsonObject.optString("TotalPoll");
                        String TotalPost = resultJsonObject.optString("TotalPost");
                        String TotalSuggestion = resultJsonObject.optString("TotalSuggestion");
                        String TotalInformation = resultJsonObject.optString("TotalInformation");
                        String TotalComplaint = resultJsonObject.optString("TotalComplaint");
                        String TotalConnect = resultJsonObject.optString("TotalConnect");
                        String TotalFavLeader = resultJsonObject.optString("TotalFavLeader");
                        String TotalFollower = resultJsonObject.optString("TotalFollower");
                        String TotalFollowing = resultJsonObject.optString("TotalFollowing");


                        feedTypes.add(getAnalyzeFeedType(TotalPost,R.drawable.ic_post_feed,"Posts"));
                        feedTypes.add(getAnalyzeFeedType(TotalEvent,R.drawable.ic_event_feed,"Event"));
                        feedTypes.add(getAnalyzeFeedType(TotalPoll,R.drawable.ic_poll_feed,"Poll"));
                        feedTypes.add(getAnalyzeFeedType(TotalSuggestion,R.drawable.ic_suggestion_feed,"Suggestion"));
                        feedTypes.add(getAnalyzeFeedType(TotalComplaint,R.drawable.ic_complaint_feed,"Complaint"));
                        feedTypes.add(getAnalyzeFeedType(TotalInformation,R.drawable.ic_information_feed,"Information"));
                        connectionFeedTypes.add(getAnalyzeFeedType(TotalConnect,R.drawable.ic_information_feed,"Connects"));
                        connectionFeedTypes.add(getAnalyzeFeedType(TotalFollower,R.drawable.ic_information_feed,"Followers"));
                        connectionFeedTypes.add(getAnalyzeFeedType(TotalFollowing,R.drawable.ic_information_feed,"Followings"));
                        connectionFeedTypes.add(getAnalyzeFeedType(TotalFavLeader,R.drawable.ic_information_feed,"Favorite Leader"));


                        tv_poll.setText("Poll ( " + TotalPoll + " )");
                        tv_event.setText("Event ( " + TotalEvent + " )");
                        tv_post.setText("Posts ( " + TotalPost + " )");
                        tv_suggestion.setText("Suggestion ( " + TotalSuggestion + " )");
                        tv_information.setText("Information ( " + TotalInformation + " )");
                        tv_complaint.setText("Complaint ( " + TotalComplaint + " )");
                        tv_friends.setText(String.valueOf(TotalConnect));
                        tv_favorite_leader.setText(String.valueOf(TotalFavLeader));
                        tv_followers.setText(String.valueOf(TotalFollower));
                        tv_followings.setText(String.valueOf(TotalFollowing));



                        analyzeFeedAdapter.notifyDataSetChanged();
                        connectionAnalyzeAdapter.notifyDataSetChanged();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "GET_ALL_DATA", false).execute(WebServicesUrls.ALL_SUMMARY_DATA);
    }


    public AnalyzeFeedType getAnalyzeFeedType(String count,int drawable,String feed_type){
        AnalyzeFeedType analyzeFeedType=new AnalyzeFeedType();
        analyzeFeedType.setFeed_count(count);
        analyzeFeedType.setFeed_drawable(drawable);
        analyzeFeedType.setFeed_type(feed_type);
        return analyzeFeedType;
    }


    AnalyzeFeedAdapter analyzeFeedAdapter;
    List<AnalyzeFeedType> feedTypes = new ArrayList<>();
    public void attachFeedAdapter() {
        analyzeFeedAdapter = new AnalyzeFeedAdapter(getActivity(), this, feedTypes);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getActivity(), 2);
        rv_feeds.setHasFixedSize(true);
        rv_feeds.setAdapter(analyzeFeedAdapter);
        rv_feeds.setLayoutManager(gridLayoutManager);
        rv_feeds.setItemAnimator(new DefaultItemAnimator());
        rv_feeds.setNestedScrollingEnabled(false);
    }
    ConnectionAnalyzeAdapter connectionAnalyzeAdapter;
    List<AnalyzeFeedType> connectionFeedTypes = new ArrayList<>();
    public void attachConnectionAdapter() {
        connectionAnalyzeAdapter = new ConnectionAnalyzeAdapter(getActivity(), this, connectionFeedTypes);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getActivity(), 2);
        rv_connections.setHasFixedSize(true);
        rv_connections.setAdapter(connectionAnalyzeAdapter);
        rv_connections.setLayoutManager(gridLayoutManager);
        rv_connections.setItemAnimator(new DefaultItemAnimator());
        rv_connections.setNestedScrollingEnabled(false);
    }
}
