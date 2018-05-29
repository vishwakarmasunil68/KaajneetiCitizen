package com.ritvi.kaajneeti.fragment.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.adapter.ActivityAdapter;
import com.ritvi.kaajneeti.adapter.ContactAdapter;
import com.ritvi.kaajneeti.adapter.FriendsHorizontalAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserProfileFragment extends Fragment {

    @BindView(R.id.rv_activity)
    RecyclerView rv_activity;
    @BindView(R.id.rv_connects)
    RecyclerView rv_connects;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_user_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachAdapter();
        attachFriendsAdapter();

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    List<String> groStrings = new ArrayList<>();
    ActivityAdapter groupAdapter;

    public void attachAdapter() {
        groStrings.add("Complaints");
        groStrings.add("Post");
        groStrings.add("Poll");
        groStrings.add("Events");
        groStrings.add("Suggestion");
        groStrings.add("Information");
        groupAdapter = new ActivityAdapter(getActivity(), this, groStrings);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_activity.setHasFixedSize(true);
        rv_activity.setAdapter(groupAdapter);
        rv_activity.setLayoutManager(linearLayoutManager);
        rv_activity.setItemAnimator(new DefaultItemAnimator());
    }

    List<String> connectionString = new ArrayList<>();
    FriendsHorizontalAdapter friendsHorizontalAdapter;

    public void attachFriendsAdapter() {
        for (int i = 0; i < 10; i++) {
            connectionString.add("");
        }
        friendsHorizontalAdapter = new FriendsHorizontalAdapter(getActivity(), this, connectionString);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_connects.setHasFixedSize(true);
        rv_connects.setAdapter(friendsHorizontalAdapter);
        rv_connects.setLayoutManager(linearLayoutManager);
        rv_connects.setItemAnimator(new DefaultItemAnimator());
    }
}
