package com.ritvi.kaajneeti.fragment.myconnection;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.adapter.FriendAdapter;
import com.ritvi.kaajneeti.adapter.HomeFeedAdapter;
import com.ritvi.kaajneeti.adapter.IncomingRequestAdapter;
import com.ritvi.kaajneeti.adapter.TagPeopleAdapter;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;
import com.ritvi.kaajneeti.pojo.connection.ConnectionIncomingPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectionsFragment extends FragmentController {

    @BindView(R.id.rv_incoming_request)
    RecyclerView rv_incoming_request;
    @BindView(R.id.rv_connections)
    RecyclerView rv_connections;
    @BindView(R.id.tv_view_sent_request)
    TextView tv_view_sent_request;
    @BindView(R.id.tv_added_me)
    TextView tv_added_me;
    @BindView(R.id.tv_connected)
    TextView tv_connected;
    @BindView(R.id.ll_incoming)
    LinearLayout ll_incoming;
    @BindView(R.id.ll_connections)
    LinearLayout ll_connections;
    @BindView(R.id.ll_view_all_connection)
    LinearLayout ll_view_all_connection;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.pb_loader)
    ProgressBar pb_loader;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_connections, container, false);
        setUpView(getActivity(),this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachAdapter();
        attachFriendAdapter();
        tv_view_sent_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof HomeActivity){
                    HomeActivity homeActivity= (HomeActivity) getActivity();
                    homeActivity.replaceFragmentinFrameHome(new OutGoingRequestFragment(),"OutGoingRequestFragment");
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllData();
            }
        });
        getAllData();

        ll_view_all_connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityManager.startFragment(R.id.frame_home,new FriendFragment());
            }
        });
    }


    public void getAllData(){
        pb_loader.setVisibility(View.VISIBLE);
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("user_profile_id",Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("show_only_limit","3"));

        new WebServiceBaseResponse<ConnectionIncomingPOJO>(nameValuePairs, getActivity(), new ResponseCallBack<ConnectionIncomingPOJO>() {
            @Override
            public void onGetMsg(ResponsePOJO<ConnectionIncomingPOJO> responsePOJO) {
                swipeRefreshLayout.setRefreshing(false);
                pb_loader.setVisibility(View.GONE);
                incomingUserPOJOS.clear();
                friendUserPOJOS.clear();
                try{
                    if(responsePOJO.isSuccess()){
                        if(responsePOJO.getResult().getTotalRequest()==0){
                            ll_incoming.setVisibility(View.GONE);
                        }else{
                            ll_incoming.setVisibility(View.VISIBLE);
                        }

                        if(responsePOJO.getResult().getTotalConnection()==0){
                            ll_connections.setVisibility(View.GONE);
                        }else{
                            ll_connections.setVisibility(View.VISIBLE);
                        }

                        tv_connected.setText("Connected ("+responsePOJO.getResult().getTotalConnection()+")");
                        tv_added_me.setText("Added Me ("+responsePOJO.getResult().getTotalRequest()+")");

                        if(responsePOJO.getResult().getTotalConnection()>3){
                            ll_view_all_connection.setVisibility(View.VISIBLE);
                        }else{
                            ll_view_all_connection.setVisibility(View.GONE);
                        }

                        incomingUserPOJOS.addAll(responsePOJO.getResult().getRequestConnectionPOJOS());
                        friendUserPOJOS.addAll(responsePOJO.getResult().getConnection());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                friendAdapter.notifyDataSetChanged();
                incomingRequestAdapter.notifyDataSetChanged();
            }
        },ConnectionIncomingPOJO.class,"ConnectionIncomingPOJO list",false).execute(WebServicesUrls.CONNECTION_WITH_INCOMING_REQUEST);

    }

    HomeFeedAdapter incomingRequestAdapter;
    List<FeedPOJO> incomingUserPOJOS = new ArrayList<>();

    public void attachAdapter() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_incoming_request.setHasFixedSize(true);
        rv_incoming_request.setLayoutManager(linearLayoutManager);
        incomingRequestAdapter = new HomeFeedAdapter(rv_incoming_request,getActivity(), this, incomingUserPOJOS,getChildFragmentManager());
        rv_incoming_request.setAdapter(incomingRequestAdapter);
        rv_incoming_request.setNestedScrollingEnabled(false);
        rv_incoming_request.setItemAnimator(new DefaultItemAnimator());
    }

    HomeFeedAdapter friendAdapter;
    List<FeedPOJO> friendUserPOJOS = new ArrayList<>();
    public void attachFriendAdapter() {

        rv_connections.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_connections.setLayoutManager(linearLayoutManager);
        friendAdapter = new HomeFeedAdapter(rv_connections,getActivity(), this, friendUserPOJOS,getChildFragmentManager());
        rv_connections.setAdapter(friendAdapter);
        rv_connections.setNestedScrollingEnabled(false);
        rv_connections.setItemAnimator(new DefaultItemAnimator());
    }
}
