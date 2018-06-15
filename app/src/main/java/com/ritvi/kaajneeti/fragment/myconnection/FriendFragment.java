package com.ritvi.kaajneeti.fragment.myconnection;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.adapter.HomeFeedAdapter;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.interfaces.OnLoadMoreListener;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;
import com.ritvi.kaajneeti.pojo.search.AllSearchPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponseList;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FriendFragment extends FragmentController{

    @BindView(R.id.rv_friends)
    RecyclerView rv_friends;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.tv_title)
    TextView tv_title;

    boolean is_search=false;
    String search_text="";
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

        if(getArguments()!=null){
            is_search=getArguments().getBoolean(Constants.IS_SEARCH);
            search_text=getArguments().getString(Constants.SEARCH_TEXT);
            et_search.setText(search_text);

            if(is_search){
                et_search.setVisibility(View.VISIBLE);
                tv_title.setVisibility(View.GONE);
            }else{
                et_search.setVisibility(View.GONE);
                tv_title.setVisibility(View.VISIBLE);
            }
        }

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        attachAdapter();
        callAPI();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callAPI();
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                callAPI();
            }
        });
    }

    public void callAPI() {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));

        String url="";
        if(is_search){
            nameValuePairs.add(new BasicNameValuePair("q", et_search.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("search_in", "people"));
            nameValuePairs.add(new BasicNameValuePair("start", "0"));
            nameValuePairs.add(new BasicNameValuePair("end", "0"));
            url=WebServicesUrls.ALL_SEARCH_API;

            new WebServiceBaseResponse<AllSearchPOJO>(nameValuePairs, getActivity(), new ResponseCallBack<AllSearchPOJO>() {

                @Override
                public void onGetMsg(ResponsePOJO<AllSearchPOJO> responsePOJO) {
                    feedPOJOS.clear();
                    try {
                        if (responsePOJO.isSuccess()) {
                            feedPOJOS.addAll(responsePOJO.getResult().getProfileFeeds());
                        } else {
                            ToastClass.showShortToast(getActivity().getApplicationContext(), responsePOJO.getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    homeFeedAdapter.notifyDataSetChanged();
                }
            }, AllSearchPOJO.class, "ALL_SEARCH_API", false).execute(url);
        }else{
            url=WebServicesUrls.MY_FRIENDS;
            new WebServiceBaseResponseList<FeedPOJO>(nameValuePairs, getActivity(), new ResponseListCallback<FeedPOJO>() {
                @Override
                public void onGetMsg(ResponseListPOJO<FeedPOJO> responseListPOJO) {
                    try {
                        feedPOJOS.clear();
                        if (responseListPOJO.isSuccess()) {
                            Log.d(TagUtils.getTag(), "response length:-" + responseListPOJO.getResultList().size());
                            feedPOJOS.addAll(responseListPOJO.getResultList());
                        } else {
                            ToastClass.showShortToast(getActivity(), responseListPOJO.getMessage());
                        }
                        homeFeedAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, FeedPOJO.class, "CALL_ALL_REQUEST_API", true).execute(url);
        }
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

//        homeFeedAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                Log.d(TagUtils.getTag(), "item loading");
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        FeedPOJO feedPOJO = new FeedPOJO();
//                        feedPOJO.setFeedtype(null);
//                        feedPOJOS.add(feedPOJO);
//                        homeFeedAdapter.notifyItemInserted(feedPOJOS.size() - 1);
//                        getAllData(false,feedPOJOS.size());
//                    }
//                }, 0);
//            }
//        });

    }

}
