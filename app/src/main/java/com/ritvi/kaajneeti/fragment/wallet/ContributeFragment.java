package com.ritvi.kaajneeti.fragment.wallet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.adapter.ContributeTransAdapter;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;
import com.ritvi.kaajneeti.pojo.payment.PaymentDataPOJO;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponseList;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ContributeFragment extends FragmentController {

    @BindView(R.id.rv_logs)
    RecyclerView rv_logs;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.ll_contribute)
    LinearLayout ll_contribute;
    @BindView(R.id.tv_total_contribute)
    TextView tv_total_contribute;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_contribute, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachAdapter();
        getContributeLogs();
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ll_contribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("is_contribute", true);
                SelectUserForContributionFragment selectUserForContributionFragment = new SelectUserForContributionFragment();
                selectUserForContributionFragment.setArguments(bundle);
                activityManager.startFragmentForResult(R.id.frame_home, ContributeFragment.this, selectUserForContributionFragment, 101);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getContributeLogs();
            }
        });

    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            Log.d(TagUtils.getTag(), "Refresh Logs");
            getContributeLogs();
        }
    }

    List<FeedPOJO> feedPOJOS = new ArrayList<>();

    public void getContributeLogs() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("payment_point", "Payment"));
        nameValuePairs.add(new BasicNameValuePair("debit_credit", "All"));
        nameValuePairs.add(new BasicNameValuePair("contribute", "1"));
        new WebServiceBaseResponseList<FeedPOJO>(nameValuePairs, getActivity(), new ResponseListCallback<FeedPOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO<FeedPOJO> responseListPOJO) {
                swipeRefreshLayout.setRefreshing(false);
                feedPOJOS.clear();
                try {
                    if (responseListPOJO.isSuccess()) {
                        double money = 0;
                        feedPOJOS.addAll(responseListPOJO.getResultList());
                        for (FeedPOJO feedPOJO : responseListPOJO.getResultList()) {
                            PaymentDataPOJO paymentDataPOJO;
                            if (feedPOJO.getFeedtype().equalsIgnoreCase(Constants.PAYMENT_FEED_MONEY)) {
                                paymentDataPOJO = feedPOJO.getPaymentDataPOJO();
                                if (paymentDataPOJO.getIsContribute().equalsIgnoreCase("1")) {
                                    try {
                                        if (paymentDataPOJO.getPaymentTo().getUserProfileId().equalsIgnoreCase(Constants.userProfilePOJO.getUserProfileId())) {
                                            money += Double.parseDouble(paymentDataPOJO.getTransactionAmount());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
//                                    feedPOJOS.add(feedPOJO);
                                }
                            }
                        }
                        tv_total_contribute.setText("Rs. " + String.valueOf(money));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                contributeTransAdapter.notifyDataSetChanged();
            }

        }, FeedPOJO.class, "GET_ALL_CONTRIBUTION", true).execute(WebServicesUrls.PAYMENT_AND_PAINT_TRANS_LOG);
    }

    ContributeTransAdapter contributeTransAdapter;

    public void attachAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_logs.setLayoutManager(linearLayoutManager);
        contributeTransAdapter = new ContributeTransAdapter(getActivity(), this, feedPOJOS);
        rv_logs.setHasFixedSize(true);
        rv_logs.setAdapter(contributeTransAdapter);
        rv_logs.setNestedScrollingEnabled(false);
        rv_logs.setItemAnimator(new DefaultItemAnimator());

    }

}
