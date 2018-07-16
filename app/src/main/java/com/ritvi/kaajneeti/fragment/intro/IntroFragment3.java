package com.ritvi.kaajneeti.fragment.intro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.Pref;
import com.ritvi.kaajneeti.Util.StringUtils;
import com.ritvi.kaajneeti.activity.IntroActivity;
import com.ritvi.kaajneeti.activity.loginregistration.EnterMobileNumberActivity;
import com.ritvi.kaajneeti.activity.loginregistration.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class IntroFragment3 extends Fragment {

    @BindView(R.id.ll_signup)
    LinearLayout ll_signup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_intro_3, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        ll_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.INTRO_COMPLETED,true);
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finishAffinity();
            }
        });
    }
}
