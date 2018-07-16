package com.ritvi.kaajneeti.fragment.event;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.fragment.NotificationFragment;
import com.ritvi.kaajneeti.fragment.search.SearchAllFragment;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.event.EventPOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import butterknife.BindView;

public class EventPreviewFragment extends FragmentController {

    @BindView(R.id.tv_starting_month)
    TextView tv_starting_month;
    @BindView(R.id.tv_start_date)
    TextView tv_start_date;
    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.spinner_interest)
    Spinner spinner_interest;
    @BindView(R.id.ll_more)
    LinearLayout ll_more;
    @BindView(R.id.tv_interested)
    TextView tv_interested;
    @BindView(R.id.tv_not_interested)
    TextView tv_not_interested;
    @BindView(R.id.tv_may_be)
    TextView tv_may_be;
    @BindView(R.id.tv_timing)
    TextView tv_timing;
    @BindView(R.id.tv_full_location)
    TextView tv_full_location;
    @BindView(R.id.tv_description)
    TextView tv_description;
    @BindView(R.id.tv_event_name)
    TextView tv_event_name;
    @BindView(R.id.iv_event)
    ImageView iv_event;
    @BindView(R.id.ll_select_interest_view)
    LinearLayout ll_select_interest_view;
    @BindView(R.id.ll_back)
    public LinearLayout ll_back;
    @BindView(R.id.ll_search)
    public LinearLayout ll_search;
    @BindView(R.id.iv_notification)
    public ImageView iv_notification;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    EventPOJO eventPOJO;
    String event_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_event_view, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            event_id = getArguments().getString("event_id");
        }

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getEventDetail(true);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityManager.startFragment(R.id.frame_home, new NotificationFragment());
            }
        });
        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityManager.startFragment(R.id.frame_home, new SearchAllFragment());
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEventDetail(true);
            }
        });
    }

    public void getEventDetail(boolean is_loading){
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id",Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("event_id",event_id));
        new WebServiceBaseResponse<EventPOJO>(nameValuePairs, getActivity(), new ResponseCallBack<EventPOJO>() {
            @Override
            public void onGetMsg(ResponsePOJO<EventPOJO> responsePOJO) {
                swipeRefreshLayout.setRefreshing(false);
                try{
                    if(responsePOJO.isSuccess()){
                        loadEventDetail(responsePOJO.getResult());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },EventPOJO.class,"GET_EVENT_DETAIL",is_loading).execute(WebServicesUrls.EVENT_DETAIL);
    }


    public void loadEventDetail(EventPOJO eventPOJO){
        if (eventPOJO != null) {
            tv_event_name.setText(eventPOJO.getEventName());
            try {
                String[] dateValues = UtilityFunction.getDateValues(eventPOJO.getStartDate());
                tv_starting_month.setText(String.valueOf(dateValues[1]));
                tv_start_date.setText(String.valueOf(dateValues[0]));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (eventPOJO.getEventProfile().getUserProfileId().equalsIgnoreCase(Constants.userProfilePOJO.getUserProfileId())) {
                ll_select_interest_view.setVisibility(View.GONE);
            } else {
                ll_select_interest_view.setVisibility(View.VISIBLE);
            }

            if (eventPOJO.getEventAttachment().size() > 0) {
                Glide.with(getActivity().getApplicationContext())
                        .load(eventPOJO.getEventAttachment().get(0).getAttachmentFile())
                        .placeholder(R.drawable.ic_default_pic)
                        .error(R.drawable.ic_default_pic)
                        .dontAnimate()
                        .into(iv_event);
            }

            if (eventPOJO.getServerLocationPOJO() != null) {
                tv_location.setText(eventPOJO.getServerLocationPOJO().getLocationVicinity());
                tv_full_location.setText(eventPOJO.getServerLocationPOJO().getLocationAddress());
            }

            spinner_interest.setSelection(0, false);
            spinner_interest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TagUtils.getTag(), "item selected:-" + position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            ll_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            if (eventPOJO.getTotalEventInterestList() != null
                    && eventPOJO.getTotalEventInterestList().size() > 0) {
                tv_interested.setText(String.valueOf(eventPOJO.getTotalEventInterestList().get(0).getTotalCount()));
                tv_not_interested.setText(String.valueOf(eventPOJO.getTotalEventInterestList().get(1).getTotalCount()));
                tv_may_be.setText(String.valueOf(eventPOJO.getTotalEventInterestList().get(2).getTotalCount()));
            }
//            tv_not_interested;
//            tv_may_be;
            tv_timing.setText(UtilityFunction.convertServerDateFromDT(eventPOJO.getStartDate()) + " - " + UtilityFunction.convertServerDateFromDT(eventPOJO.getEndDate()));
            tv_description.setText(eventPOJO.getEventDescription());
        }
    }

    public void refreshOnIncomingNotification(String feedId) {
        if(event_id.equalsIgnoreCase(feedId)){
            getEventDetail(false);
        }
    }
}
