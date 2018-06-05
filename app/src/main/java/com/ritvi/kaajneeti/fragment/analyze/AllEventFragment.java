package com.ritvi.kaajneeti.fragment.analyze;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.activity.express.CheckInActivity;
import com.ritvi.kaajneeti.adapter.HomeFeedAdapter;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;
import com.ritvi.kaajneeti.pojo.location.NewLocationPOJO;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
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

public class AllEventFragment extends Fragment {

    @BindView(R.id.rv_data)
    RecyclerView rv_data;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout sliding_layout;
    @BindView(R.id.iv_filter)
    ImageView iv_filter;

    @BindView(R.id.iv_delete_filter)
    ImageView iv_delete_filter;
    @BindView(R.id.check_posted)
    CheckBox check_posted;
    @BindView(R.id.check_participated)
    CheckBox check_participated;
    @BindView(R.id.ll_interested)
    LinearLayout ll_interested;
    @BindView(R.id.rb_interested)
    RadioButton rb_interested;
    @BindView(R.id.rb_not_interested)
    RadioButton rb_not_interested;
    @BindView(R.id.rb_may_be)
    RadioButton rb_may_be;
    @BindView(R.id.check_tagged)
    CheckBox check_tagged;
    @BindView(R.id.btn_today)
    Button btn_today;
    @BindView(R.id.btn_last_week)
    Button btn_last_week;
    @BindView(R.id.btn_last_month)
    Button btn_last_month;
    @BindView(R.id.btn_custom)
    Button btn_custom;
    @BindView(R.id.frame_range)
    FrameLayout frame_range;
    @BindView(R.id.iv_calendar)
    ImageView iv_calendar;
    @BindView(R.id.tv_range)
    TextView tv_range;
    @BindView(R.id.et_city_filter)
    EditText et_city_filter;
    @BindView(R.id.btn_apply)
    Button btn_apply;
    @BindView(R.id.iv_location)
    ImageView iv_location;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_all_events, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        attachAdapter();
        callAPI();

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        checkfilters();
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

        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocation();
            }
        });

        check_participated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ll_interested.setVisibility(View.VISIBLE);
                }else{
                    ll_interested.setVisibility(View.GONE);
                }
            }
        });

        iv_delete_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_posted.setChecked(false);
                check_participated.setChecked(false);
                rb_may_be.setChecked(false);
                rb_not_interested.setChecked(false);
                rb_interested.setChecked(false);
                check_tagged.setChecked(false);

                date_start_range = "";
                date_end_range = "";

                posted_by_me = "";
                event_tagged = "";
                event_interested = "";
                event_not_interested = "";
                event_may_be = "";

                setRangeButtonBack();
                iv_filter.callOnClick();
            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TagUtils.getTag(), "start date:-" + date_start_range);
                Log.d(TagUtils.getTag(), "end date:-" + date_end_range);
                if (check_posted.isChecked()) {
                    posted_by_me = "1";
                } else {
                    posted_by_me = "";
                }
                if (check_tagged.isChecked()) {
                    event_tagged = "1";
                } else {
                    event_tagged = "";
                }
                if(rb_interested.isChecked()){
                    event_interested="1";
                }else{
                    event_interested="0";
                }
                if(rb_not_interested.isChecked()){
                    event_not_interested="1";
                }else{
                    event_not_interested="0";
                }
                if(rb_may_be.isChecked()){
                    event_may_be="1";
                }else{
                    event_may_be="0";
                }

                if(!check_participated.isChecked()){
                    event_interested="0";
                    event_not_interested="0";
                    event_may_be="0";
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

    String posted_by_me = "";
    String event_tagged = "";
    String event_interested = "";
    String event_not_interested = "";
    String event_may_be = "";

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


    public void callAPI() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("friend_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        new WebServiceBaseResponseList<FeedPOJO>(nameValuePairs, getActivity(), new ResponseListCallback<FeedPOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO responseListPOJO) {
                feedPOJOS.clear();
                try {
                    if (responseListPOJO.isSuccess()) {
                        feedPOJOS.addAll(responseListPOJO.getResultList());
                    } else {
                        ToastClass.showShortToast(getActivity().getApplicationContext(), responseListPOJO.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                homeFeedAdapter.notifyDataSetChanged();
            }
        }, FeedPOJO.class, "call_complaint_list_api", true).execute(WebServicesUrls.ALL_EVENT);
    }


    HomeFeedAdapter homeFeedAdapter;
    List<FeedPOJO> feedPOJOS = new ArrayList<>();

    public void attachAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_data.setLayoutManager(linearLayoutManager);
        homeFeedAdapter = new HomeFeedAdapter(rv_data, getActivity(), this, feedPOJOS, getChildFragmentManager());
        rv_data.setHasFixedSize(true);
        rv_data.setAdapter(homeFeedAdapter);
        rv_data.setItemAnimator(new DefaultItemAnimator());

    }

    public void checkLocation() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        } else {
            UtilityFunction.getLocation(getActivity().getApplicationContext());
            startActivityForResult(new Intent(getActivity(), CheckInActivity.class), Constants.ACTIVITY_LOCATION);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkLocationPermission() {
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.ACCESS_LOCATION);
            return;
        } else {
            UtilityFunction.getLocation(getActivity().getApplicationContext());
            startActivityForResult(new Intent(getActivity(), CheckInActivity.class), Constants.ACTIVITY_LOCATION);
        }
    }

    NewLocationPOJO newLocationPOJO;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ACTIVITY_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                NewLocationPOJO newLocationPOJO = (NewLocationPOJO) data.getSerializableExtra("location");
                et_city_filter.setText(newLocationPOJO.getMain_text() + "," + newLocationPOJO.getSecondary_text());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                newLocationPOJO = null;
            }
        }
    }
}
