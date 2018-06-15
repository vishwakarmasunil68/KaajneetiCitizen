package com.ritvi.kaajneeti.fragment.wallet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.StringUtils;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentContants;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.payment.PaymentTransPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponseList;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;

public class CompleteContributionFragment extends FragmentController {


    @BindView(R.id.tv_total_amount)
    TextView tv_total_amount;
    @BindView(R.id.tv_available_balance)
    TextView tv_available_balance;
    @BindView(R.id.tv_wallet_amount)
    TextView tv_wallet_amount;
    @BindView(R.id.tv_remaining_amount)
    TextView tv_remaining_amount;
    @BindView(R.id.check_balance)
    CheckBox check_balance;
    @BindView(R.id.ll_payment_gateway)
    LinearLayout ll_payment_gateway;
    @BindView(R.id.btn_pay)
    Button btn_pay;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;

    @BindView(R.id.rv_payment_options)
    RecyclerView rv_payment_options;

    String calculated_amount = "";

    UserProfilePOJO userProfilePOJO;
    String total_contribution;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_complete_contri, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userProfilePOJO = (UserProfilePOJO) getArguments().getSerializable("userProfilePOJO");
        total_contribution = getArguments().getString("amount");

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_total_amount.setText("Rs. " + total_contribution);
        tv_remaining_amount.setText("Rs. " + total_contribution);

        check_balance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (walletAmount >= UtilityFunction.getTransAmount(total_contribution)) {
                        tv_remaining_amount.setText(String.valueOf(0));
                    } else {
                        tv_remaining_amount.setText(String.valueOf(Math.abs(walletAmount - UtilityFunction.getTransAmount(total_contribution))));
                    }
                } else {
                    tv_remaining_amount.setText(String.valueOf(Math.abs(UtilityFunction.getTransAmount(total_contribution))));
                }
            }
        });

        getPaymentLogs();

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callpaymentSuccessfullAPI(total_contribution, "4");
            }
        });

    }

    double walletAmount = 0.0;

    public void getPaymentLogs() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        new WebServiceBaseResponseList<PaymentTransPOJO>(nameValuePairs, getActivity(), new ResponseListCallback<PaymentTransPOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO<PaymentTransPOJO> responseListPOJO) {
                if (responseListPOJO.isSuccess()) {
                    double amount = 0;
                    for (PaymentTransPOJO paymentTransPOJO : responseListPOJO.getResultList()) {
                        if (paymentTransPOJO.getDebitOrCredit().equals("0")) {
                            amount -= UtilityFunction.getTransAmount(paymentTransPOJO.getTransactionAmount());
                        } else {
                            amount += UtilityFunction.getTransAmount(paymentTransPOJO.getTransactionAmount());
                        }
                    }
                    walletAmount = amount;
                    tv_wallet_amount.setText("Rs. " + String.valueOf(amount));
                    tv_available_balance.setText("Available Balance is Rs. " + String.valueOf(amount));

                    if (check_balance.isChecked()) {
                        if (walletAmount >= UtilityFunction.getTransAmount(total_contribution)) {
                            tv_remaining_amount.setText(String.valueOf(0));
                        } else {
                            tv_remaining_amount.setText(String.valueOf(Math.abs(walletAmount - UtilityFunction.getTransAmount(total_contribution))));
                        }
                    }
                }
            }

        }, PaymentTransPOJO.class, "CALL_PAYMENT_LOGS_API", true).execute(WebServicesUrls.GET_PAYMENT_TRANS_LOGS);
    }

    public void callpaymentSuccessfullAPI(String amount, String payment_gateway_id) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("payment_gateway_id", payment_gateway_id));
        nameValuePairs.add(new BasicNameValuePair("transaction_id", StringUtils.getRandomString(15)));
        nameValuePairs.add(new BasicNameValuePair("payment_to_user_profile_id", userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("transaction_date", UtilityFunction.getCurrentDate()));
        nameValuePairs.add(new BasicNameValuePair("transaction_amount", amount));
        nameValuePairs.add(new BasicNameValuePair("transaction_shipping_amount", amount));
        nameValuePairs.add(new BasicNameValuePair("transaction_status", "1"));
        nameValuePairs.add(new BasicNameValuePair("debit_or_credit", "0"));
        nameValuePairs.add(new BasicNameValuePair("comments", "Contribution to " + userProfilePOJO.getFirstName() + " " + userProfilePOJO.getLastName()));
        nameValuePairs.add(new BasicNameValuePair("contribute", "1"));
        new WebServiceBase(nameValuePairs, getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                Log.d(TagUtils.getTag(), "trans response:-" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                        ToastClass.showShortToast(getActivity().getApplicationContext(), "Thanks for your contribution");
                        activityManager.popBackResultFragment(startingFragment, requestCode, FragmentContants.RESULT_OK, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "CALL_TRANS_API", true).execute(WebServicesUrls.SAVE_PAYMENT_TRANSACTIONS);
    }

}
