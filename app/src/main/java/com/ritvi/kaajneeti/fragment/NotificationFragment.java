package com.ritvi.kaajneeti.fragment;

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
import android.widget.LinearLayout;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.adapter.FriendsHorizontalAdapter;
import com.ritvi.kaajneeti.adapter.NotificationAdapter;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.notification.NotificationPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponseList;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NotificationFragment extends FragmentController{

    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.rv_notifications)
    RecyclerView rv_notifications;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_notification,container,false);
        setUpView(getActivity(),this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        attachNotificationAdapter();
        getALlNotifications(true);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getALlNotifications(true);
            }
        });
    }

    public void getALlNotifications(boolean is_loading){
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("start", "0"));
        new WebServiceBaseResponseList<NotificationPOJO>(nameValuePairs, getActivity(), new ResponseListCallback<NotificationPOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO<NotificationPOJO> responseListPOJO) {
                swipeRefreshLayout.setRefreshing(false);
                try{
                    if(responseListPOJO.isSuccess()){
                        notificationPOJOS.clear();
                        notificationPOJOS.addAll(responseListPOJO.getResultList());
                        notificationAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },NotificationPOJO.class,"GET_NOTIFICATIONS",is_loading).execute(WebServicesUrls.GET_ALL_NOTIFICATIONS);
    }



    NotificationAdapter notificationAdapter;
    List<NotificationPOJO> notificationPOJOS=new ArrayList<>();
    public void attachNotificationAdapter() {

        notificationAdapter = new NotificationAdapter(getActivity(), this, notificationPOJOS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_notifications.setHasFixedSize(true);
        rv_notifications.setAdapter(notificationAdapter);
        rv_notifications.setLayoutManager(linearLayoutManager);
        rv_notifications.setItemAnimator(new DefaultItemAnimator());
    }

}
