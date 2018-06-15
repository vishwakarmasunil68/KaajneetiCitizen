package com.ritvi.kaajneeti.fragment.wallet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.StringUtils;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentContants;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class WalletAmountFragment  extends FragmentController {

    @BindView(R.id.cv_profile_pic)
    CircleImageView cv_profile_pic;
    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.et_amount)
    EditText et_amount;
    @BindView(R.id.btn_50)
    Button btn_50;
    @BindView(R.id.btn_100)
    Button btn_100;
    @BindView(R.id.btn_500)
    Button btn_500;
    @BindView(R.id.btn_pay)
    Button btn_pay;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;


    UserProfilePOJO userProfilePOJO;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_wallet_amount,container,false);
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
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        userProfilePOJO= (UserProfilePOJO) getArguments().getSerializable("userProfilePOJO");

        Glide.with(getActivity().getApplicationContext())
                .load(userProfilePOJO.getProfilePhotoPath())
                .placeholder(R.drawable.ic_default_profile_pic)
                .error(R.drawable.ic_default_profile_pic)
                .dontAnimate()
                .into(cv_profile_pic);
        tv_profile_name.setText(userProfilePOJO.getFirstName() + " " + userProfilePOJO.getLastName());
        tv_email.setText(userProfilePOJO.getEmail());

        btn_50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAmount("50");
            }
        });

        btn_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAmount("100");
            }
        });

        btn_500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAmount("500");
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_amount.getText().toString().length()>0&&!et_amount.getText().toString().equalsIgnoreCase("0")) {
                    callpaymentSuccessfullAPI(et_amount.getText().toString(), "1");
                }else{
                    ToastClass.showShortToast(getActivity().getApplicationContext(),"Please Enter proper amount");
                }
            }
        });

    }

    public void callpaymentSuccessfullAPI(String amount, String payment_gateway_id) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

        nameValuePairs.add(new BasicNameValuePair("user_profile_id", userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("payment_gateway_id", payment_gateway_id));
        nameValuePairs.add(new BasicNameValuePair("transaction_id", StringUtils.getRandomString(15)));
        nameValuePairs.add(new BasicNameValuePair("payment_to_user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("transaction_date", UtilityFunction.getCurrentDate()));
        nameValuePairs.add(new BasicNameValuePair("transaction_amount", amount));
        nameValuePairs.add(new BasicNameValuePair("transaction_shipping_amount", amount));
        nameValuePairs.add(new BasicNameValuePair("transaction_status", "1"));
        nameValuePairs.add(new BasicNameValuePair("debit_or_credit", "1"));
        nameValuePairs.add(new BasicNameValuePair("comments", "Adding money to wallet"));
        nameValuePairs.add(new BasicNameValuePair("contribute", "0"));
        new WebServiceBase(nameValuePairs, getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                Log.d(TagUtils.getTag(), "trans response:-" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                        ToastClass.showShortToast(getActivity().getApplicationContext(),"Amount Added Successfully");
                        activityManager.popBackResultFragment(startingFragment, requestCode, FragmentContants.RESULT_OK, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "CALL_TRANS_API", true).execute(WebServicesUrls.SAVE_PAYMENT_TRANSACTIONS);
    }


    public void addAmount(String amount) {
        try {
            int entered_amount = Integer.parseInt(amount);
            if (et_amount.getText().toString().length() == 0) {
                et_amount.setText(amount);
            } else {
                int previous_amount = Integer.parseInt(et_amount.getText().toString());
                et_amount.setText(String.valueOf(previous_amount + entered_amount));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
