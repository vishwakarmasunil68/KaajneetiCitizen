package com.ritvi.kaajneeti.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.adapter.FavoriteLeaderAdapter;
import com.ritvi.kaajneeti.fragment.user.SearchUserResultPOJO;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponseList;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectFavoriteLeaderActivity extends AppCompatActivity {

    @BindView(R.id.iv_add_leader)
    ImageView iv_add_leader;
    @BindView(R.id.rv_favorite_leader)
    RecyclerView rv_favorite_leader;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.et_search)
    EditText et_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_favorite_leader);
        ButterKnife.bind(this);

        attachAdapter();
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et_search.getText().length()>0){
                    callLeaderSearchAPI();
                }else{
                    callLeaderAPI();
                }
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iv_add_leader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectFavoriteLeaderActivity.this, AllLeaderActivity.class));
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        callLeaderAPI();
    }

    FavoriteLeaderAdapter leaderAdapter;
    List<UserProfilePOJO> leaderPOJOS = new ArrayList<>();
    public void attachAdapter() {
        leaderAdapter = new FavoriteLeaderAdapter(this, null, leaderPOJOS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_favorite_leader.setHasFixedSize(true);
        rv_favorite_leader.setAdapter(leaderAdapter);
        rv_favorite_leader.setLayoutManager(linearLayoutManager);
        rv_favorite_leader.setItemAnimator(new DefaultItemAnimator());
    }

    public void callLeaderAPI() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("q", et_search.getText().toString()));
        new WebServiceBaseResponseList<UserProfilePOJO>(nameValuePairs, this, new ResponseListCallback<UserProfilePOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO<UserProfilePOJO> responseListPOJO) {
                leaderPOJOS.clear();
                if (responseListPOJO.isSuccess()) {
                    leaderPOJOS.addAll(responseListPOJO.getResultList());
                }
                leaderAdapter.notifyDataSetChanged();
            }
        }, UserProfilePOJO.class, "CALL_LEADER_API", false).execute(WebServicesUrls.GET_MY_FAVORITE_LEADER);
    }


    public void callLeaderSearchAPI() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("search", et_search.getText().toString()));

        new WebServiceBaseResponse<SearchUserResultPOJO>(nameValuePairs, this, new ResponseCallBack<SearchUserResultPOJO>() {
            @Override
            public void onGetMsg(ResponsePOJO<SearchUserResultPOJO> responsePOJO) {
                leaderPOJOS.clear();
                if (responsePOJO.isSuccess()) {
                    leaderPOJOS.addAll(responsePOJO.getResult().getLeaderUserInfoPOJOS());
                } else {
                    ToastClass.showShortToast(getApplicationContext(), "No Leader Found");
                }
                leaderAdapter.notifyDataSetChanged();
            }
        },SearchUserResultPOJO.class,"CALL_ALL_LEADER",false).execute(WebServicesUrls.SEARCH_LEADER_PROFILE);
    }


    public void selectLeader(UserProfilePOJO userProfilePOJO) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("userprofile",userProfilePOJO);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
