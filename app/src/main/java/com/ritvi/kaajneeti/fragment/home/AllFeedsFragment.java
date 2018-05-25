package com.ritvi.kaajneeti.fragment.home;

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

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.adapter.HomeFeedAdapter;
import com.ritvi.kaajneeti.interfaces.OnLoadMoreListener;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponseList;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllFeedsFragment extends Fragment{

    int range = 10;
    @BindView(R.id.rv_feeds)
    RecyclerView rv_feeds;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_all_feeds,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachAdapter();
        getAllData(false,0);
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
        new WebServiceBaseResponseList<FeedPOJO>(nameValuePairs, getActivity(), new ResponseListCallback<FeedPOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO<FeedPOJO> responseListPOJO) {
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


}
