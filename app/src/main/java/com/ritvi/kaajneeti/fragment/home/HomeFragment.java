package com.ritvi.kaajneeti.fragment.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.SetViews;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.adapter.ViewPagerAdapter;
import com.ritvi.kaajneeti.adapter.ViewPagerWithTitleAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment{

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.cv_profile_pic)
    CircleImageView cv_profile_pic;
    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_home,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SetViews.setProfilePhoto(getActivity().getApplicationContext(), Constants.userProfilePOJO.getProfilePhotoPath(), cv_profile_pic);
        SetViews.setProfileName(Constants.userProfilePOJO.getFirstName()+" "+Constants.userProfilePOJO.getLastName(),tv_profile_name);

        setUpViewPager();
    }

    public void setUpViewPager(){

        ViewPagerWithTitleAdapter adapter = new ViewPagerWithTitleAdapter(getChildFragmentManager());
        adapter.addFrag(new AllFeedsFragment(), "All");
//        adapter.addFrag(new FeedsFragment(), "Feeds");
        adapter.addFrag(new FeedsFragment(), "Event");
        adapter.addFrag(new FeedsFragment(), "Poll");
        adapter.addFrag(new FeedsFragment(), "Complaint");
        adapter.addFrag(new FeedsFragment(), "Suggestion");
        adapter.addFrag(new FeedsFragment(), "Information");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());

        tabs.setupWithViewPager(viewPager);
    }

}
