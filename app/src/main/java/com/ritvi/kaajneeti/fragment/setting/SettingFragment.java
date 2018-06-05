package com.ritvi.kaajneeti.fragment.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ritvi.kaajneeti.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingFragment extends Fragment{

    @BindView(R.id.iv_app_drop)
    ImageView iv_app_drop;
    @BindView(R.id.ll_app_setting_header)
    LinearLayout ll_app_setting_header;
    @BindView(R.id.ll_app_setting)
    LinearLayout ll_app_setting;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_setting,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        ll_app_setting_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ll_app_setting.getVisibility()==View.VISIBLE){
                    ll_app_setting.setVisibility(View.GONE);
                    iv_app_drop.setImageResource(R.drawable.ic_drop);
                }else{
                    ll_app_setting.setVisibility(View.VISIBLE);
                    iv_app_drop.setImageResource(R.drawable.ic_drop_up);
                }
            }
        });
    }
}
