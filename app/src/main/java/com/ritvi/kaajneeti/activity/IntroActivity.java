package com.ritvi.kaajneeti.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.Pref;
import com.ritvi.kaajneeti.Util.StringUtils;
import com.ritvi.kaajneeti.activity.loginregistration.EnterMobileNumberActivity;
import com.ritvi.kaajneeti.activity.loginregistration.LoginActivity;
import com.ritvi.kaajneeti.adapter.ViewPagerAdapter;
import com.ritvi.kaajneeti.fragment.IntroFragment;
import com.ritvi.kaajneeti.fragment.home.AnalyzeFragment;
import com.ritvi.kaajneeti.fragment.home.FavoriteFragment;
import com.ritvi.kaajneeti.fragment.home.HomeFragment;
import com.ritvi.kaajneeti.fragment.home.MyConnectionFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.btn_register)
    Button btn_register;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tv_skip)
    TextView tv_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        setupViewPager(viewPager);

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pref.SetBooleanPref(getApplicationContext(), StringUtils.INTRO_COMPLETED,true);
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pref.SetBooleanPref(getApplicationContext(), StringUtils.INTRO_COMPLETED,true);
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pref.SetBooleanPref(getApplicationContext(), StringUtils.INTRO_COMPLETED,true);
                startActivity(new Intent(IntroActivity.this,EnterMobileNumberActivity.class).putExtra("type", Constants.ENTER_MOBILE_REGISTRATION_TYPE));
                finishAffinity();
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {

        IntroFragment introFragment=new IntroFragment("slider1");
        IntroFragment introFragment2=new IntroFragment("slider2");
        IntroFragment introFragment3=new IntroFragment("slider3");

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(introFragment, "intro1");
        adapter.addFrag(introFragment2, "intro2");
        adapter.addFrag(introFragment3, "intro3");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());

    }
}
