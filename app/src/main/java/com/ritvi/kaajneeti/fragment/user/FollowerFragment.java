package com.ritvi.kaajneeti.fragment.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.adapter.HomeFeedAdapter;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponseList;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FollowerFragment extends FragmentController{

    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_friends)
    RecyclerView rv_friends;
    boolean isFollower=false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_friends,container,false);
        setUpView(getActivity(),this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isFollower=getArguments().getBoolean("is_follower");

        et_search.setVisibility(View.GONE);
        tv_title.setVisibility(View.VISIBLE);
        if(isFollower) {
            tv_title.setText("My Followers");
        }else{
            tv_title.setText("My Followings");
        }
        attachAdapter();
        callAPI();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callAPI();
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public void callAPI(){
        String url="";
        if(isFollower){
            url=WebServicesUrls.GET_MY_FOLLOWERS;
        }else{
            url=WebServicesUrls.GET_MY_FOLLOWINGS;
        }

        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("user_profile_id",Constants.userProfilePOJO.getUserProfileId()));

        new WebServiceBaseResponseList<FeedPOJO>(nameValuePairs, getActivity(), new ResponseListCallback<FeedPOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO<FeedPOJO> responseListPOJO) {
                swipeRefreshLayout.setRefreshing(false);
                try{
                    if(responseListPOJO.isSuccess()) {
                        feedPOJOS.clear();
                        feedPOJOS.addAll(responseListPOJO.getResultList());
                        homeFeedAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }else{
                        ToastClass.showShortToast(getActivity().getApplicationContext(),responseListPOJO.getMessage());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },FeedPOJO.class,"GET_RESPONSE",true).execute(url);

    }

    HomeFeedAdapter homeFeedAdapter;
    List<FeedPOJO> feedPOJOS = new ArrayList<>();

    public void attachAdapter() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_friends.setHasFixedSize(true);
        rv_friends.setLayoutManager(linearLayoutManager);
        homeFeedAdapter = new HomeFeedAdapter(rv_friends, getActivity(), this, feedPOJOS, getChildFragmentManager());
        rv_friends.setAdapter(homeFeedAdapter);
        rv_friends.setNestedScrollingEnabled(true);
        rv_friends.setItemAnimator(new DefaultItemAnimator());
    }
}
