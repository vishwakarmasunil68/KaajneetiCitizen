package com.ritvi.kaajneeti.fragment.analyze;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.adapter.HomeFeedAdapter;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;
import com.ritvi.kaajneeti.pojo.search.AllSearchPOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponseList;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;
import com.savvi.rangedatepicker.CalendarPickerView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllComplaintFragment extends Fragment {

    @BindView(R.id.rv_data)
    RecyclerView rv_data;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;

    @BindView(R.id.iv_filter)
    ImageView iv_filter;
    @BindView(R.id.iv_delete_filter)
    ImageView iv_delete_filter;
    @BindView(R.id.check_posted)
    CheckBox check_posted;
    @BindView(R.id.check_associated)
    CheckBox check_associated;
    @BindView(R.id.btn_today)
    Button btn_today;
    @BindView(R.id.btn_last_week)
    Button btn_last_week;
    @BindView(R.id.btn_last_month)
    Button btn_last_month;
    @BindView(R.id.btn_custom)
    Button btn_custom;
    @BindView(R.id.tv_range)
    TextView tv_range;
    @BindView(R.id.iv_calendar)
    ImageView iv_calendar;
    @BindView(R.id.frame_range)
    FrameLayout frame_range;
    @BindView(R.id.btn_apply)
    Button btn_apply;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout sliding_layout;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.tv_title)
    TextView tv_title;

    boolean is_search=false;
    String search_text="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_all_complaint, container, false);
        ButterKnife.bind(this, view);
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

        attachAdapter();
        callAPI();

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        checkfilters();
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

    String date_start_range = "";
    String date_end_range = "";

    public void checkfilters() {
        btn_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRangeButtonBack();
                btn_today.setBackgroundResource(R.drawable.btn_next);
                getTodayRange();
            }
        });
        btn_last_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRangeButtonBack();
                btn_last_week.setBackgroundResource(R.drawable.btn_next);
                getLastWeekRange();
            }
        });
        btn_last_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRangeButtonBack();
                btn_last_month.setBackgroundResource(R.drawable.btn_next);
                getLastMonthRange();
            }
        });
        btn_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRangeButtonBack();
                btn_custom.setBackgroundResource(R.drawable.btn_next);
                frame_range.setVisibility(View.VISIBLE);
            }
        });

        iv_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomRangeDialog();
            }
        });

        iv_delete_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_associated.setChecked(false);
                check_posted.setChecked(false);
                date_start_range="";
                date_end_range="";
                posted_by_me="";
                me_associated="";
                setRangeButtonBack();
                iv_filter.callOnClick();
            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TagUtils.getTag(),"start date:-"+date_start_range);
                Log.d(TagUtils.getTag(),"end date:-"+date_end_range);
                if(check_posted.isChecked()){
                    posted_by_me="1";
                }else{
                    posted_by_me="";
                }

                if(check_associated.isChecked()){
                    me_associated="1";
                }else{
                    me_associated="";
                }
                iv_filter.callOnClick();
                callAPI();
            }
        });

        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sliding_layout != null &&
                        (sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                    sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                } else {
                    sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }
            }
        });
    }

    public void showCustomRangeDialog() {
        final Dialog dialog1 = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dialog_range);
        dialog1.show();
        dialog1.setCancelable(true);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button btn_done = dialog1.findViewById(R.id.btn_done);

        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -10);
        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 10);

        final CalendarPickerView calendar = (CalendarPickerView) dialog1.findViewById(R.id.calendar_view);

        calendar.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDate(new Date());


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calendar.getSelectedDates().size() > 0) {
                    date_start_range = formatDate(calendar.getSelectedDates().get(0));
                    date_end_range = formatDate(calendar.getSelectedDates().get(calendar.getSelectedDates().size() - 1));
                    tv_range.setText(date_start_range + "  - " + date_end_range);
                    dialog1.dismiss();
                } else {
                    ToastClass.showShortToast(getActivity().getApplicationContext(), "please select proper date");
                }
            }
        });
    }

    public String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(date);
    }

    public void getTodayRange() {
        date_start_range = UtilityFunction.getCurrentDate();
        date_end_range = UtilityFunction.getCurrentDate();
    }

    public void getLastWeekRange() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        date_start_range = dateFormat.format(cal.getTime());
        date_end_range = UtilityFunction.getCurrentDate();
    }

    public void getLastMonthRange() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        date_start_range = dateFormat.format(cal.getTime());
        date_end_range = UtilityFunction.getCurrentDate();
    }


    public void setRangeButtonBack() {
        btn_today.setBackgroundResource(R.drawable.btn_resend);
        btn_last_month.setBackgroundResource(R.drawable.btn_resend);
        btn_last_week.setBackgroundResource(R.drawable.btn_resend);
        btn_custom.setBackgroundResource(R.drawable.btn_resend);

        frame_range.setVisibility(View.GONE);
    }

    String posted_by_me="";
    String me_associated="";

    public void callAPI() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("friend_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("search_in", "complaint"));
        nameValuePairs.add(new BasicNameValuePair("posted_by_me", posted_by_me));
        nameValuePairs.add(new BasicNameValuePair("me_associated", me_associated));
        nameValuePairs.add(new BasicNameValuePair("date_from", UtilityFunction.getConvertedDate(date_start_range)));
        nameValuePairs.add(new BasicNameValuePair("date_to", UtilityFunction.getConvertedDate(date_end_range)));

        String url="";
        if(is_search){
            nameValuePairs.add(new BasicNameValuePair("q", et_search.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
            url=WebServicesUrls.ALL_SEARCH_API;

            new WebServiceBaseResponse<AllSearchPOJO>(nameValuePairs, getActivity(), new ResponseCallBack<AllSearchPOJO>() {

                @Override
                public void onGetMsg(ResponsePOJO<AllSearchPOJO> responsePOJO) {
                    complaintPOJOS.clear();
                    try {
                        if (responsePOJO.isSuccess()) {
                            complaintPOJOS.addAll(responsePOJO.getResult().getComplaintFeeds());
                        } else {
                            ToastClass.showShortToast(getActivity().getApplicationContext(), responsePOJO.getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    homeFeedAdapter.notifyDataSetChanged();
                }
            }, AllSearchPOJO.class, "ALL_SEARCH_API", false).execute(url);
        }else{
            url=WebServicesUrls.COMPLAINT_LIST;
            new WebServiceBaseResponseList<FeedPOJO>(nameValuePairs, getActivity(), new ResponseListCallback<FeedPOJO>() {

                @Override
                public void onGetMsg(ResponseListPOJO<FeedPOJO> responseListPOJO) {
                    complaintPOJOS.clear();
                    try {
                        if (responseListPOJO.isSuccess()) {
                            complaintPOJOS.addAll(responseListPOJO.getResultList());
                        } else {
                            ToastClass.showShortToast(getActivity().getApplicationContext(), responseListPOJO.getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    homeFeedAdapter.notifyDataSetChanged();
                }
            }, FeedPOJO.class, "call_complaint_list_api", true).execute(url);
        }


    }


    List<FeedPOJO> complaintPOJOS = new ArrayList<>();
    HomeFeedAdapter homeFeedAdapter;

    public void attachAdapter() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_data.setLayoutManager(linearLayoutManager);
        homeFeedAdapter = new HomeFeedAdapter(rv_data, getActivity(), this, complaintPOJOS, getChildFragmentManager());
        rv_data.setHasFixedSize(true);
        rv_data.setAdapter(homeFeedAdapter);
        rv_data.setItemAnimator(new DefaultItemAnimator());
    }


}
