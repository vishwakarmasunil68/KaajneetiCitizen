package com.ritvi.kaajneeti.fragment.home;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.SetViews;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.activity.express.ExpressActivity;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.adapter.ViewPagerAdapter;
import com.ritvi.kaajneeti.adapter.ViewPagerWithTitleAdapter;
import com.ritvi.kaajneeti.fragment.user.UserProfileFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.cv_profile_pic)
    CircleImageView cv_profile_pic;
    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @BindView(R.id.tv_what)
    TextView tv_what;
//    @BindView(R.id.cv_express)
//    ImageView cv_express;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SetViews.setProfilePhoto(getActivity().getApplicationContext(), Constants.userProfilePOJO.getProfilePhotoPath(), cv_profile_pic);
        SetViews.setProfileName(Constants.userProfilePOJO.getFirstName() + " " + Constants.userProfilePOJO.getLastName(), tv_profile_name);
        tv_what.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ExpressActivity.class));
            }
        });
        setUpViewPager();

//        cv_express.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), ExpressActivity.class));
//            }
//        });

        cv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof HomeActivity){
                    HomeActivity homeActivity= (HomeActivity) getActivity();
                    UserProfileFragment userProfileFragment=new UserProfileFragment();
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("userProfile",Constants.userProfilePOJO);
                    userProfileFragment.setArguments(bundle);

                    homeActivity.replaceFragmentinFrameHome(userProfileFragment,"UserProfileFragment");
                }
            }
        });
        tv_profile_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv_profile_pic.callOnClick();
            }
        });
    }

    public void setUpViewPager() {
        AllFeedsFragment fragment1=new AllFeedsFragment("All");
        AllFeedsFragment fragment2=new AllFeedsFragment("event");
        AllFeedsFragment fragment3=new AllFeedsFragment("poll");
        AllFeedsFragment fragment4=new AllFeedsFragment("complaint");
        AllFeedsFragment fragment5=new AllFeedsFragment("suggestion");
        AllFeedsFragment fragment6=new AllFeedsFragment("information");

        final List<AllFeedsFragment> allFeedsFragments=new ArrayList<>();
        allFeedsFragments.add(fragment1);
        allFeedsFragments.add(fragment2);
        allFeedsFragments.add(fragment3);
        allFeedsFragments.add(fragment4);
        allFeedsFragments.add(fragment5);
        allFeedsFragments.add(fragment6);

        ViewPagerWithTitleAdapter adapter = new ViewPagerWithTitleAdapter(getChildFragmentManager());
        adapter.addFrag(fragment1, "All");
        adapter.addFrag(fragment2, "Event");
        adapter.addFrag(fragment3, "Poll");
        adapter.addFrag(fragment4, "Complaint");
        adapter.addFrag(fragment5, "Suggestion");
        adapter.addFrag(fragment6, "Information");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());

        tabs.setupWithViewPager(viewPager);

        allFeedsFragments.get(0).initializeData();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                allFeedsFragments.get(position).initializeData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

}
