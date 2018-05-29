package com.ritvi.kaajneeti.fragment.myconnection;

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
import android.widget.ImageView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.adapter.GroupAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupFragment extends Fragment{

    @BindView(R.id.rv_groups)
    RecyclerView rv_groups;
    @BindView(R.id.iv_add_group)
    ImageView iv_add_group;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_group,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachAdapter();

        iv_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof HomeActivity){
                    HomeActivity homeActivity= (HomeActivity) getActivity();
                    homeActivity.replaceFragmentinFrameHome(new CreateGroupFragment(),"CreateGroupFragment");
                }
            }
        });
    }


    List<String> groStrings = new ArrayList<>();
    GroupAdapter groupAdapter;

    public void attachAdapter() {
        for(int i=0;i<3;i++){
            groStrings.add("");
        }
        groupAdapter = new GroupAdapter(getActivity(), this, groStrings);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_groups.setHasFixedSize(true);
        rv_groups.setAdapter(groupAdapter);
        rv_groups.setLayoutManager(linearLayoutManager);
        rv_groups.setItemAnimator(new DefaultItemAnimator());
    }
}
