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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.activity.express.CheckInActivity;
import com.ritvi.kaajneeti.adapter.HomeFeedAdapter;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;
import com.ritvi.kaajneeti.pojo.location.LocationPOJO;
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

public class AllPollFragment extends Fragment {

    @BindView(R.id.rv_data)
    RecyclerView rv_data;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_filter)
    ImageView iv_filter;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout sliding_layout;
    boolean is_search = false;
    String search_text = "";

    @BindView(R.id.check_posted)
    CheckBox check_posted;
    @BindView(R.id.check_tagged)
    CheckBox check_tagged;
    @BindView(R.id.check_post_liked)
    CheckBox check_post_liked;
    @BindView(R.id.check_post_commented)
    CheckBox check_post_commented;
    @BindView(R.id.check_participated)
    CheckBox check_participated;
    @BindView(R.id.check_ongoing)
    CheckBox check_ongoing;
    @BindView(R.id.check_ended)
    CheckBox check_ended;
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
    @BindView(R.id.tv_range)
    TextView tv_range;
    @BindView(R.id.iv_calendar)
    ImageView iv_calendar;
    @BindView(R.id.et_city_filter)
    EditText et_city_filter;
    @BindView(R.id.iv_location)
    ImageView iv_location;
    @BindView(R.id.btn_apply)
    Button btn_apply;
    @BindView(R.id.iv_delete_filter)
    ImageView iv_delete_filter;
    @BindView(R.id.btn_reset)
    Button btn_reset;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    String posted_by_me = "";
    String tagged_by_me = "";
    String liked_by_me = "";
    String commented_by_me = "";
    String participated_by_me = "";
    String ongoing = "";
    String ended = "";
    String date_from = "";
    String date_to = "";

    String friend_profile_id = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_all_poll, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            is_search = getArguments().getBoolean(Constants.IS_SEARCH);
            search_text = getArguments().getString(Constants.SEARCH_TEXT);
            et_search.setText(search_text);
            friend_profile_id = getArguments().getString(Constants.FRIEND_USER_PROFILE_ID);
            if (is_search) {
                et_search.setVisibility(View.VISIBLE);
                tv_title.setVisibility(View.GONE);
            } else {
                et_search.setVisibility(View.GONE);
                tv_title.setVisibility(View.VISIBLE);
            }
            tv_title.setText("Poll");
        }

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

        attachAdapter();
        callAPI();

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

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        checkFilters();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callAPI();
            }
        });

    }

    public void checkFilters() {

        iv_delete_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_filter.callOnClick();
            }
        });
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

        check_posted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    posted_by_me = "1";
                } else {
                    posted_by_me = "";
                }
            }
        });
        check_tagged.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tagged_by_me = "1";
                } else {
                    tagged_by_me = "";
                }
            }
        });
        check_post_liked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    liked_by_me = "1";
                } else {
                    liked_by_me = "";
                }
            }
        });
        check_post_commented.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    commented_by_me = "1";
                } else {
                    commented_by_me = "";
                }
            }
        });
        check_participated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    participated_by_me = "1";
                } else {
                    participated_by_me = "";
                }
            }
        });
        check_ongoing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ongoing = "1";
                } else {
                    ongoing = "";
                }
            }
        });
        check_ended.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ended = "1";
                } else {
                    ended = "";
                }
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

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPI();
                iv_filter.callOnClick();
            }
        });


        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_posted.setChecked(false);
                check_tagged.setChecked(false);
                check_post_liked.setChecked(false);
                check_post_commented.setChecked(false);
                check_participated.setChecked(false);
                check_ongoing.setChecked(false);
                check_ended.setChecked(false);
                setRangeButtonBack();
                locationPOJO=null;
                et_city_filter.setText("");
                iv_delete_filter.callOnClick();
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
                    date_from = formatDate(calendar.getSelectedDates().get(0));
                    date_to = formatDate(calendar.getSelectedDates().get(calendar.getSelectedDates().size() - 1));
                    tv_range.setText(date_from + "  - " + date_to);
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
        date_from = UtilityFunction.getCurrentDate();
        date_to = UtilityFunction.getCurrentDate();
    }

    public void getLastWeekRange() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        date_from = dateFormat.format(cal.getTime());
        date_to = UtilityFunction.getCurrentDate();
    }

    public void getLastMonthRange() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        date_from = dateFormat.format(cal.getTime());
        date_to = UtilityFunction.getCurrentDate();
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
        nameValuePairs.add(new BasicNameValuePair("friend_profile_id", friend_profile_id));

        nameValuePairs.add(new BasicNameValuePair("posted_by_me", posted_by_me));
        nameValuePairs.add(new BasicNameValuePair("date_from", UtilityFunction.getConvertedDate(date_from)));
        nameValuePairs.add(new BasicNameValuePair("date_to", UtilityFunction.getConvertedDate(date_to)));
        if (locationPOJO != null) {
            nameValuePairs.add(new BasicNameValuePair("location", locationPOJO.getFormatted_address()));
            nameValuePairs.add(new BasicNameValuePair("location_place_id", locationPOJO.getPlaceId()));
        }
        nameValuePairs.add(new BasicNameValuePair("tagged_in", tagged_by_me));
        nameValuePairs.add(new BasicNameValuePair("participated", participated_by_me));
        nameValuePairs.add(new BasicNameValuePair("poll_ongoing", ongoing));
        nameValuePairs.add(new BasicNameValuePair("poll_ended", ended));

        new WebServiceBaseResponseList<FeedPOJO>(nameValuePairs, getActivity(), new ResponseListCallback<FeedPOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO responseListPOJO) {
                swipeRefreshLayout.setRefreshing(false);
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
        }, FeedPOJO.class, "GET_API", true).execute(WebServicesUrls.ALL_POLL_AND_WHERE_I_TAGGED);
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

    LocationPOJO locationPOJO;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ACTIVITY_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                locationPOJO = (LocationPOJO) data.getSerializableExtra("location");
                et_city_filter.setText(locationPOJO.getFormatted_address());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                locationPOJO = null;
            }
        }
    }

}
