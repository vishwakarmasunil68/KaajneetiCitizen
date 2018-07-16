package com.ritvi.kaajneeti.fragment.wallet;

import android.app.Dialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.adapter.AlreadyTaggedPeopleAdapter;
import com.ritvi.kaajneeti.adapter.FriendAdapter;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentContants;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;
import com.ritvi.kaajneeti.pojo.payment.TotalAmountPOJO;
import com.ritvi.kaajneeti.pojo.search.AllSearchPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class SelectUserForContributionFragment extends FragmentController {
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.rv_users)
    RecyclerView rv_users;
    @BindView(R.id.et_search)
    EditText et_search;
    boolean is_contribute=false;
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

        if(getArguments()!=null){
            is_contribute=getArguments().getBoolean("is_contribute");
        }

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
        friendAdapter = new FriendAdapter(getActivity(), this, feedPOJOS,is_contribute);
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

    public void showPointSendDialog(final UserProfilePOJO profiledata) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        new WebServiceBaseResponse<TotalAmountPOJO>(nameValuePairs, getActivity(), new ResponseCallBack<TotalAmountPOJO>() {
            @Override
            public void onGetMsg(ResponsePOJO<TotalAmountPOJO> responsePOJO) {
                try {
                    if (responsePOJO.isSuccess()) {
//                        totalAmountPOJO = responsePOJO.getResult();
//                        tv_points.setText(String.valueOf(responsePOJO.getResult().getMyPointBalance()));
//                        if(for_transfer){
//                            try {
//                                if (totalAmountPOJO != null) {
                                    UserPointDialog(profiledata,responsePOJO.getResult().getMyPointBalance());
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, TotalAmountPOJO.class, "GET_TOTAL_AMOUNT", true).execute(WebServicesUrls.GET_MY_TOTAL_AMOUNT);
    }

    public void UserPointDialog(UserProfilePOJO userProfilePOJO, final int points){
        final Dialog dialog1 = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.setCancelable(true);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_send_points);
        dialog1.setTitle("Convert Points");
        dialog1.show();
        dialog1.setCancelable(true);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button btn_send = dialog1.findViewById(R.id.btn_send);
        final EditText et_points = dialog1.findViewById(R.id.et_points);
        TextView tv_total_points = dialog1.findViewById(R.id.tv_total_points);
        TextView tv_email = dialog1.findViewById(R.id.tv_email);
        TextView tv_profile_name = dialog1.findViewById(R.id.tv_profile_name);
        CircleImageView cv_profile_pic = dialog1.findViewById(R.id.cv_profile_pic);
        ImageView iv_close = dialog1.findViewById(R.id.iv_close);


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_points.getText().toString().length() > 0) {
                    try {
                        int sending_point = Integer.parseInt(et_points.getText().toString());
                        double remaining_balance = points;
                        if (sending_point >= remaining_balance) {
                            ToastClass.showShortToast(getActivity().getApplicationContext(), "Entered Points should not be greater than you remaining points");
                        } else {
//                            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
//                            nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
//                            nameValuePairs.add(new BasicNameValuePair("rupee", String.valueOf(balance)));
//                            new WebServiceBase(nameValuePairs, getActivity(), new WebServicesCallBack() {
//                                @Override
//                                public void onGetMsg(String apicall, String response) {
//                                    try {
//                                        JSONObject jsonObject = new JSONObject(response);
//                                        if (jsonObject.optString("status").equals("success")) {
//                                            ToastClass.showShortToast(getActivity().getApplicationContext(), "Rupees Converted");
//                                            dialog1.dismiss();
//                                            getPointsLogs(false);
//                                        } else {
//                                            ToastClass.showShortToast(getActivity().getApplicationContext(), jsonObject.optString("message"));
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }, "CONVERT_POINT_TO_RUPEE", true).execute(WebServicesUrls.CONVERT_RUPEES_TO_POINTS);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
