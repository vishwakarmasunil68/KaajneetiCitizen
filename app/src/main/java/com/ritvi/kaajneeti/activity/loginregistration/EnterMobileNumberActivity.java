package com.ritvi.kaajneeti.activity.loginregistration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.Pref;
import com.ritvi.kaajneeti.Util.StringUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EnterMobileNumberActivity extends AppCompatActivity {

    @BindView(R.id.btn_verification)
    Button btn_verification;
    @BindView(R.id.et_phone_number)
    EditText et_phone_number;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mobile_number);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString("type");
        }


        btn_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_phone_number.getText().toString().length() != 10) {
                    ToastClass.showShortToast(getApplicationContext(), "Please Enter Valid Mobile Number");
                } else {
                    if(type.equalsIgnoreCase(Constants.ENTER_MOBILE_LOGIN_WITH_OTP_TYPE)){
                        callLoginOTPAPI();
                    }else if(type.equalsIgnoreCase(Constants.ENTER_MOBILE_REGISTRATION_TYPE)){
                        callRegisterAPI();
                    }
                }
//                startActivity(new Intent(EnterMobileNumberActivity.this, OtpVerificationActivity.class).putExtra("mobile", et_phone_number.getText().toString()));
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void callRegisterAPI(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("request_action", "REGISTER_MOBILE"));
        nameValuePairs.add(new BasicNameValuePair("device_token", ""));
        nameValuePairs.add(new BasicNameValuePair("mobile", "+91" + et_phone_number.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("login_type", "1"));
        new WebServiceBase(nameValuePairs, this, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                parseRegisterResponse(response);
            }
        }, Constants.CALL_REGISTER_API, true).execute(WebServicesUrls.REGISTER_URL);
    }

    public void callLoginOTPAPI(){
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("request_action","LOGIN_MOBILE"));
        nameValuePairs.add(new BasicNameValuePair("mobile","+91"+et_phone_number.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("login_type", "1"));
        new WebServiceBase(nameValuePairs, this, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                parseLoginResponse(response);
            }
        }, Constants.CALL_LOGIN_OTP, true).execute(WebServicesUrls.LOGIN_URL);
    }


    public void parseLoginResponse(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.optString("status").equals("success")){
                startActivity(new Intent(EnterMobileNumberActivity.this,OtpVerificationActivity.class).putExtra("mobile_number",et_phone_number.getText().toString()).putExtra("type",type));
            }
            ToastClass.showShortToast(getApplicationContext(),jsonObject.optString("message"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void parseRegisterResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            ToastClass.showShortToast(getApplicationContext(),jsonObject.optString("message"));
            if (jsonObject.optString(Constants.API_STATUS).equals(Constants.API_SUCCESS)) {
                startActivity(new Intent(EnterMobileNumberActivity.this, OtpVerificationActivity.class).putExtra("mobile_number", et_phone_number.getText().toString()).putExtra("type",type));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(), ToastClass.NO_INTERNET_CONNECTION);
        }
    }

}
