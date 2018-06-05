package com.ritvi.kaajneeti.activity.loginregistration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.goodiebag.pinview.Pinview;
import com.google.gson.Gson;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.Pref;
import com.ritvi.kaajneeti.Util.StringUtils;
import com.ritvi.kaajneeti.Util.TagUtils;
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

public class OtpVerificationActivity extends AppCompatActivity {

    @BindView(R.id.tv_mobile_number)
    TextView tv_mobile_number;
    @BindView(R.id.pinview)
    Pinview pinview;
    @BindView(R.id.btn_next)
    Button btn_next;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.btn_resend_otp)
    Button btn_resend_otp;
    String mobile_number="";
    String type = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        ButterKnife.bind(this);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            type = bundle.getString("type");
            tv_mobile_number.setText("Please enter the verification code sent to +91 "+bundle.getString("mobile_number",""));
            mobile_number=bundle.getString("mobile_number","");
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pinview.getValue().length() != 4) {
                    ToastClass.showShortToast(getApplicationContext(), "Please Enter Valid OTP");
                } else {
                    if(type.equalsIgnoreCase(Constants.ENTER_MOBILE_LOGIN_WITH_OTP_TYPE)) {
                        callOTPAPI();
                    }else if(type.equalsIgnoreCase(Constants.ENTER_MOBILE_REGISTRATION_TYPE)){
                        registerValidateOTP();
                    }else if(type.equalsIgnoreCase(Constants.ENTER_MOBILE_FORGOT_MPIN)){
                        callVARIFYOTP();
                    }
                }
            }
        });

        btn_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equalsIgnoreCase(Constants.ENTER_MOBILE_LOGIN_WITH_OTP_TYPE)) {
                    callLoginWithOtpVerifyResend();
                }else if(type.equalsIgnoreCase(Constants.ENTER_MOBILE_FORGOT_MPIN)){
                    callForgotMPINResendOTP();
                }else if(type.equalsIgnoreCase(Constants.ENTER_MOBILE_REGISTRATION_TYPE)){
                    callLoginWithOtpVerifyResend();
                }
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        startTimer();
    }

    public void callLoginWithOtpVerifyResend(){
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("mobile","+91"+mobile_number));
        new WebServiceBase(nameValuePairs, this, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.optString("status").equalsIgnoreCase("success")){
                        startTimer();
                    }else{
                        ToastClass.showShortToast(getApplicationContext(),jsonObject.optString("message"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, Constants.CALL_LOGIN_OTP, true).execute(WebServicesUrls.LOGIN_URL);
    }


    private void callForgotMPINResendOTP() {
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("mobile","+91"+mobile_number));
        new WebServiceBase(nameValuePairs, this, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.optString("status").equalsIgnoreCase("success")){
                        startTimer();
                    }else{
                        ToastClass.showShortToast(getApplicationContext(),jsonObject.optString("message"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },"FORGOT_MPIN",true).execute(WebServicesUrls.FORGOT_MPIN);
    }


    public void startTimer(){
//        btn_resend_otp.setEnabled(false);
        resendDisabled();
        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long l) {
                btn_resend_otp.setText("Resend 00:"+String.valueOf((int) (l/1000)));
            }

            @Override
            public void onFinish() {
                btn_resend_otp.setText("Resend");
                resendEnabled();
            }
        }.start();
    }

    public void resendDisabled(){
        btn_resend_otp.setEnabled(false);
        btn_resend_otp.setBackgroundResource(R.drawable.btn_resend);
        btn_resend_otp.setTextColor(Color.parseColor("#C1C1C1"));
    }

    public void resendEnabled(){
        btn_resend_otp.setEnabled(true);
        btn_resend_otp.setBackgroundResource(R.drawable.btn_next);
        btn_resend_otp.setTextColor(Color.parseColor("#FFFFFF"));
    }

    private void callVARIFYOTP() {
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("mobile","+91"+mobile_number));
        nameValuePairs.add(new BasicNameValuePair("reset_code",pinview.getValue().toString()));
        new WebServiceBase(nameValuePairs, this, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                Log.d(TagUtils.getTag(),"response:-"+response);
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.optString("status").equalsIgnoreCase("success")){
                        startActivity(new Intent(OtpVerificationActivity.this,SetMpinActivity.class).putExtra("mobile_number",mobile_number).putExtra("type",type));
                    }else{
                        ToastClass.showShortToast(getApplicationContext(),jsonObject.optString("message"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },"CALL_VALIDATE_MPIN",true).execute(WebServicesUrls.VALIDATE_FORGOT_MPIN);
    }


    public void registerValidateOTP(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("device_token", ""));
        nameValuePairs.add(new BasicNameValuePair("mobile", "+91"+mobile_number));
        nameValuePairs.add(new BasicNameValuePair("otp", pinview.getValue().toString()));
        nameValuePairs.add(new BasicNameValuePair("login_type", "1"));
        new WebServiceBase(nameValuePairs, this, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                parseOTPVerifiedResponse(response);
            }
        }, Constants.CALL_OTP_VERIFIED, true).execute(WebServicesUrls.REGISTER_VALIDATE_MOBILE_OTP);
    }

    public void callOTPAPI() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("mobile", "+91"+mobile_number));
        nameValuePairs.add(new BasicNameValuePair("otp", pinview.getValue().toString()));
        nameValuePairs.add(new BasicNameValuePair("login_type", "1"));
        new WebServiceBase(nameValuePairs, this, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                parseLoginOTPResponse(response);
            }
        }, "CALL_LOGIN_OTP_VERIFY", true).execute(WebServicesUrls.VERIFY_LOGIN_OTP);
    }

    public void parseOTPVerifiedResponse(String response) {
        try {
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.optString(Constants.API_STATUS).equals(Constants.API_SUCCESS)){
                startActivity(new Intent(OtpVerificationActivity.this,SetMpinActivity.class).putExtra("mobile_number",mobile_number).putExtra("type",type));
            }
            ToastClass.showShortToast(getApplicationContext(),jsonObject.optString("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseLoginOTPResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString(Constants.API_STATUS).equals(Constants.API_SUCCESS)) {
                String user_profile = jsonObject.optJSONObject("result").toString();
                Gson gson = new Gson();
                UserProfilePOJO userProfilePOJO = gson.fromJson(user_profile, UserProfilePOJO.class);
                Pref.SetStringPref(getApplicationContext(), StringUtils.USER_PROFILE, user_profile);
                Pref.SetBooleanPref(this, StringUtils.IS_LOGIN, true);
                Constants.userProfilePOJO = userProfilePOJO;
                startActivity(new Intent(this, HomeActivity.class));
                finishAffinity();
            }else{
                ToastClass.showShortToast(getApplicationContext(),jsonObject.optString("message'"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        startTimer();
        getApplicationContext().registerReceiver(mMessageReceiver, new IntentFilter(StringUtils.LOGIN_OTP_CLASS));
    }

    @Override
    protected void onPause() {
        super.onPause();
        getApplicationContext().unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra("message");
            Log.d(TagUtils.getTag(), "result:-" + result);
            if (result != null && result.length() > 0) {
                try {
                    Log.d(TagUtils.getTag(), "message received:-" + result);
//                    String[] msgsplit = result.split("c-");
//                    Log.d(TagUtils.getTag(), "otp:-" + msgsplit[1].trim());
                    result=result.replace(" is the OTP verifying your mobile with kaajneeti","");
                    Log.d(TagUtils.getTag(),"otp:-"+result);
                    pinview.setValue(result);
                    btn_next.callOnClick();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

}
