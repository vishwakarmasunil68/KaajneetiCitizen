package com.ritvi.kaajneeti.fragment.wallet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.adapter.AlreadyTaggedPeopleAdapter;
import com.ritvi.kaajneeti.adapter.FriendAdapter;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentContants;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;
import com.ritvi.kaajneeti.pojo.search.AllSearchPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SelectUserForContributionFragment extends FragmentController {
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.rv_users)
    RecyclerView rv_users;
    @BindView(R.id.et_search)
    EditText et_search;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_select_user_for_contribution,container,false);
        setUpView(getActivity(),this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
                searchUser(et_search.getText().toString());
            }
        });

    }

    AllSearchPOJO allSearchPOJO;
    public void searchUser(String key){
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("user_profile_id",Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("q",key));
        nameValuePairs.add(new BasicNameValuePair("start","0"));
        nameValuePairs.add(new BasicNameValuePair("end","10"));
        nameValuePairs.add(new BasicNameValuePair("search_in","people"));

        new WebServiceBaseResponse<AllSearchPOJO>(nameValuePairs, getActivity(), new ResponseCallBack<AllSearchPOJO>() {

            @Override
            public void onGetMsg(ResponsePOJO<AllSearchPOJO> responsePOJO) {
                feedPOJOS.clear();
                if (responsePOJO.isSuccess()) {
                    SelectUserForContributionFragment.this.allSearchPOJO = responsePOJO.getResult();
//                    populateUser(responsePOJO.getResult().getProfileFeeds());
                    feedPOJOS.addAll(responsePOJO.getResult().getProfileFeeds());
                }
                friendAdapter.notifyDataSetChanged();
            }
        }, AllSearchPOJO.class, "ALL_SEARCH_API", false).execute(WebServicesUrls.ALL_SEARCH_API);
    }

    FriendAdapter friendAdapter;
    List<FeedPOJO> feedPOJOS=new ArrayList<>();
    public void attachAdapter() {
        friendAdapter = new FriendAdapter(getActivity(), this, feedPOJOS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_users.setHasFixedSize(true);
        rv_users.setAdapter(friendAdapter);
        rv_users.setLayoutManager(linearLayoutManager);
        rv_users.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if(requestCode==101){
            activityManager.popBackResultFragment(startingFragment, requestCode, FragmentContants.RESULT_OK, null);
        }
    }
}
