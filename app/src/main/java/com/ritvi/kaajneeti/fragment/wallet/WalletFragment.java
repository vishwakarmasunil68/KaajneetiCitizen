package com.ritvi.kaajneeti.fragment.wallet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.all.All;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.adapter.ContributeTransAdapter;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentContants;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;
import com.ritvi.kaajneeti.pojo.payment.PaymentDataPOJO;
import com.ritvi.kaajneeti.pojo.payment.PaymentTransPOJO;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponseList;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WalletFragment extends FragmentController{

    private final static int PAYMENT_REQUEST=101;

    @BindView(R.id.tv_wallet_amount)
    TextView tv_wallet_amount;
    @BindView(R.id.ll_add_money)
    LinearLayout ll_add_money;
    @BindView(R.id.rv_logs)
    RecyclerView rv_logs;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_wallet,container,false);
        setUpView(getActivity(),this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        attachAdapter();
        getPaymentLogs();
        ll_add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalletAmountFragment walletAmountFragment=new WalletAmountFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("userProfilePOJO",Constants.userProfilePOJO);
                walletAmountFragment.setArguments(bundle);
                activityManager.startFragmentForResult(R.id.frame_home,WalletFragment.this,walletAmountFragment,PAYMENT_REQUEST);
            }
        });
    }

    double walletAmount=0.0;

    public void getPaymentLogs() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("payment_point", "Payment"));
        nameValuePairs.add(new BasicNameValuePair("debit_credit", "All"));
        new WebServiceBaseResponseList<FeedPOJO>(nameValuePairs, getActivity(), new ResponseListCallback<FeedPOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO<FeedPOJO> responseListPOJO) {
                feedPOJOS.clear();
                if (responseListPOJO.isSuccess()) {
                    feedPOJOS.addAll(responseListPOJO.getResultList());
                    double amount = 0;
                    for (FeedPOJO feedPOJO : responseListPOJO.getResultList()) {
                        if (feedPOJO.getFeedtype().equalsIgnoreCase(Constants.PAYMENT_FEED_MONEY)) {
                            PaymentDataPOJO paymentDataPOJO = feedPOJO.getPaymentDataPOJO();
                            if (paymentDataPOJO.getPaymentTo().getUserProfileId().equals(Constants.userProfilePOJO.getUserProfileId())) {
                                amount += UtilityFunction.getTransAmount(paymentDataPOJO.getTransactionAmount());
                            } else if(paymentDataPOJO.getPaymentBy().getUserProfileId().equals(Constants.userProfilePOJO.getUserProfileId())){
                                amount -= UtilityFunction.getTransAmount(paymentDataPOJO.getTransactionAmount());
                            }
                        }
                    }
                    walletAmount = amount;
                    tv_wallet_amount.setText("Rs. " + String.valueOf(amount));
                }
                contributeTransAdapter.notifyDataSetChanged();
            }

        }, FeedPOJO.class, "CALL_PAYMENT_LOGS_API", true).execute(WebServicesUrls.PAYMENT_AND_PAINT_TRANS_LOG);
    }


    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if(requestCode==PAYMENT_REQUEST){
            if(resultCode== FragmentContants.RESULT_OK){
                getPaymentLogs();
            }
        }
    }

    ContributeTransAdapter contributeTransAdapter;
    List<FeedPOJO> feedPOJOS=new ArrayList<>();
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
