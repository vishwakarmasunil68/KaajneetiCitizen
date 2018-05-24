package com.ritvi.kaajneeti.fragment.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.adapter.ViewPagerAdapter;
import com.ritvi.kaajneeti.adapter.ViewPagerWithTitleAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment{

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabs;

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

        setUpViewPager();
    }

    public void setUpViewPager(){

        ViewPagerWithTitleAdapter adapter = new ViewPagerWithTitleAdapter(getChildFragmentManager());
        adapter.addFrag(new FeedsFragment(), "All");
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
