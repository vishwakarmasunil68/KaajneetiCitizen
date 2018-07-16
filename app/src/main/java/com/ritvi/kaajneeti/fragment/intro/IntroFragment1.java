package com.ritvi.kaajneeti.fragment.intro;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class IntroFragment1 extends Fragment{



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_intro,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
}
