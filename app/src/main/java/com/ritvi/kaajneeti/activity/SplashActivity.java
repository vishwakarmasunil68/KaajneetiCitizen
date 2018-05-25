package com.ritvi.kaajneeti.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.Pref;
import com.ritvi.kaajneeti.Util.StringUtils;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.activity.loginregistration.LoginActivity;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Pref.GetBooleanPref(getApplicationContext(), StringUtils.IS_LOGIN, false)) {
                    Constants.userProfilePOJO = new Gson().fromJson(Pref.GetStringPref(getApplicationContext(), StringUtils.USER_PROFILE, ""), UserProfilePOJO.class);
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                } else {
//                    if (Pref.getPermanentBoolean(getApplicationContext(), StringUtils.INTRO_COMPLETED, false)) {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                    } else {
//                        startActivity(new Intent(SplashActivity.this, SliderActivity.class));
//                    }
                }
//                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finishAffinity();
            }
        },2000);
    }
}
