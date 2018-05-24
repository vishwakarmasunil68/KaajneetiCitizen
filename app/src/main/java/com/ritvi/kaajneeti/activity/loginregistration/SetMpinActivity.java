package com.ritvi.kaajneeti.activity.loginregistration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.goodiebag.pinview.Pinview;
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

public class SetMpinActivity extends AppCompatActivity {

    @BindView(R.id.btn_next)
    Button btn_next;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.pin_confirm_mpin)
    Pinview pin_confirm_mpin;
    @BindView(R.id.pin_mpin)
    Pinview pin_mpin;
    String mobile_number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_mpin);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mobile_number = bundle.getString("mobile_number");
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pin_confirm_mpin.getValue().toString().length() != 4 || pin_mpin.getValue().toString().length() != 4) {
                    ToastClass.showShortToast(getApplicationContext(), "Please Enter Mpin");
                } else {
                    if (pin_confirm_mpin.getValue().toString().equals(pin_mpin.getValue().toString())) {
                        callSetMpin();
                    } else {
                        ToastClass.showShortToast(getApplicationContext(), "Please Enter same MPIN");
                    }
                }
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void callSetMpin() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("mobile", "+91"+mobile_number));
        nameValuePairs.add(new BasicNameValuePair("mpin", pin_mpin.getValue().toString()));
        nameValuePairs.add(new BasicNameValuePair("mpin_confirm", pin_confirm_mpin.getValue().toString()));
        nameValuePairs.add(new BasicNameValuePair("login_type", "1"));
        new WebServiceBase(nameValuePairs, this, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                parseMpinResponse(response);
            }
        }, Constants.CALL_MPIN_SET, true).execute(WebServicesUrls.REGISTER_SET_MPIN);
    }

    public void parseMpinResponse(String response) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
