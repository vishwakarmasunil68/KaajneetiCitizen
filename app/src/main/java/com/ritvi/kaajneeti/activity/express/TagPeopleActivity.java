package com.ritvi.kaajneeti.activity.express;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.adapter.TagPeopleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TagPeopleActivity extends AppCompatActivity {
    @BindView(R.id.rv_users)
    RecyclerView rv_users;
    @BindView(R.id.ic_back)
    ImageView ic_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_people);
        ButterKnife.bind(this);

        attachAdapter();

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    List<String> peoples=new ArrayList<>();
    TagPeopleAdapter tagPeopleAdapter;
    public void attachAdapter() {

        for(int i=0;i<10;i++){
            peoples.add("");
        }

        tagPeopleAdapter = new TagPeopleAdapter(this, null, peoples);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_users.setHasFixedSize(true);
        rv_users.setAdapter(tagPeopleAdapter);
        rv_users.setLayoutManager(linearLayoutManager);
        rv_users.setItemAnimator(new DefaultItemAnimator());
    }
}
