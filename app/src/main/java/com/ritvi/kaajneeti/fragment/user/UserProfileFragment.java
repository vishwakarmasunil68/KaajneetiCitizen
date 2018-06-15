package com.ritvi.kaajneeti.fragment.user;

import android.app.Dialog;
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
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.SetViews;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.adapter.FriendsHorizontalAdapter;
import com.ritvi.kaajneeti.adapter.HomeFeedAdapter;
import com.ritvi.kaajneeti.adapter.SummaryAdapter;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;
import com.ritvi.kaajneeti.pojo.user.AddressPOJO;
import com.ritvi.kaajneeti.pojo.user.EducationPOJO;
import com.ritvi.kaajneeti.pojo.user.FullProfilePOJO;
import com.ritvi.kaajneeti.pojo.user.SummaryPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.pojo.user.WorkPOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponseList;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {

    @BindView(R.id.rv_activity)
    RecyclerView rv_activity;
    @BindView(R.id.rv_friends)
    RecyclerView rv_friends;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.iv_edit)
    ImageView iv_edit;
    @BindView(R.id.cv_profile_pic)
    CircleImageView cv_profile_pic;
    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_work)
    TextView tv_work;
    @BindView(R.id.tv_education)
    TextView tv_education;
    @BindView(R.id.tv_bio)
    TextView tv_bio;
    @BindView(R.id.tv_view_less)
    TextView tv_view_less;
    @BindView(R.id.ll_email)
    LinearLayout ll_email;
    @BindView(R.id.ll_phone)
    LinearLayout ll_phone;
    @BindView(R.id.ll_location)
    LinearLayout ll_location;
    @BindView(R.id.ll_work)
    LinearLayout ll_work;
    @BindView(R.id.ll_education)
    LinearLayout ll_education;
    @BindView(R.id.ll_user_info)
    LinearLayout ll_user_info;
    @BindView(R.id.tv_addfriend)
    TextView tv_addfriend;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rv_feeds)
    RecyclerView rv_feeds;
    @BindView(R.id.ll_feeds)
    LinearLayout ll_feeds;

    UserProfilePOJO userProfilePOJO;
    List<UserProfilePOJO> friendProfilePOJOS = new ArrayList<>();

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

        userProfilePOJO = (UserProfilePOJO) getArguments().getSerializable("userProfile");
        tv_title.setText(userProfilePOJO.getFirstName() + " " + userProfilePOJO.getLastName());
//        if(userProfilePOJO!=null&&userProfilePOJO.getUserProfileId().equals(Constants.userProfilePOJO.getProfilePhotoId())){
        tv_addfriend.setVisibility(View.GONE);
//        }
        attachAdapter();

        attachFriendsAdapter();

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();

                    UpdateProfileFragment userProfileFragment = new UpdateProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userProfile", Constants.userProfilePOJO);
                    userProfileFragment.setArguments(bundle);

                    homeActivity.replaceFragmentinFrameHome(userProfileFragment, "UpdateProfileFragment");
                }
            }
        });


        if (userProfilePOJO != null) {
            if (userProfilePOJO.getUserProfileId().equals(Constants.userProfilePOJO.getUserProfileId())) {
                iv_edit.setVisibility(View.VISIBLE);
            } else {
                iv_edit.setVisibility(View.GONE);
            }
            getAllProfileData();
            getFriendSummary();
            getAllHomeData();
        }

        tv_view_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_user_info.getVisibility() == View.VISIBLE) {
                    ll_user_info.setVisibility(View.GONE);
                    tv_view_less.setText("View More");
                } else {
                    ll_user_info.setVisibility(View.VISIBLE);
                    tv_view_less.setText("View Less");
                }
            }
        });
    }

    FullProfilePOJO fullProfilePOJO;

    public void getAllProfileData() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("friend_user_profile_id", userProfilePOJO.getUserProfileId()));
        new WebServiceBaseResponse<FullProfilePOJO>(nameValuePairs, getActivity(), new ResponseCallBack<FullProfilePOJO>() {
            @Override
            public void onGetMsg(ResponsePOJO<FullProfilePOJO> responsePOJO) {
                if (responsePOJO.isSuccess()) {
                    fullProfilePOJO = responsePOJO.getResult();
                    setUpProfileData(responsePOJO.getResult());
                } else {
                    ToastClass.showShortToast(getActivity().getApplicationContext(), responsePOJO.getMessage());
                }
            }
        }, FullProfilePOJO.class, "CALL_PROFILE_API", true).execute(WebServicesUrls.FULL_PROFILE_URL);
    }

    public void getAllHomeData() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("friend_user_profile_id", userProfilePOJO.getUserProfileId()));

        new WebServiceBaseResponseList<FeedPOJO>(nameValuePairs, getActivity(), new ResponseListCallback<FeedPOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO<FeedPOJO> responseListPOJO) {
                try {
                    feedPOJOS.clear();
                    if (responseListPOJO.isSuccess()) {

                        feedPOJOS.addAll(responseListPOJO.getResultList());
                        homeFeedAdapter.notifyDataSetChanged();
                        ll_feeds.setVisibility(View.VISIBLE);
                    } else {
                        ll_feeds.setVisibility(View.GONE);
                        ToastClass.showShortToast(getActivity(), responseListPOJO.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, FeedPOJO.class, "CALL_FEED_DATA", true).execute(WebServicesUrls.FRIEND_HOME_PAGE_DATA);
    }

    HomeFeedAdapter homeFeedAdapter;
    List<FeedPOJO> feedPOJOS = new ArrayList<>();

    public void attachAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_feeds.setLayoutManager(linearLayoutManager);
        homeFeedAdapter = new HomeFeedAdapter(rv_feeds, getActivity(), this, feedPOJOS, getChildFragmentManager());
        rv_feeds.setHasFixedSize(true);
        rv_feeds.setAdapter(homeFeedAdapter);
        rv_feeds.setNestedScrollingEnabled(false);
        rv_feeds.setItemAnimator(new DefaultItemAnimator());

    }

    public void setUpProfileData(final FullProfilePOJO fullProfilePOJO) {
        if (fullProfilePOJO != null) {

            Glide.with(getActivity().getApplicationContext())
                    .load(fullProfilePOJO.getProfilePOJO().getProfilePhotoPath())
                    .placeholder(R.drawable.ic_default_profile_pic)
                    .error(R.drawable.ic_default_profile_pic)
                    .dontAnimate()
                    .into(cv_profile_pic);

            if (Constants.userProfilePOJO.getUserProfileId().equals(fullProfilePOJO.getProfilePOJO().getUserProfileId())) {
                SetViews.changeProfilePics(getActivity().getApplicationContext(), fullProfilePOJO.getProfilePOJO().getProfilePhotoPath());
            }
            tv_profile_name.setText(fullProfilePOJO.getProfilePOJO().getFirstName() + " " + fullProfilePOJO.getProfilePOJO().getLastName());
            tv_title.setText(fullProfilePOJO.getProfilePOJO().getFirstName() + " " + fullProfilePOJO.getProfilePOJO().getLastName());

            if (fullProfilePOJO.getProfilePOJO().getEmail().length() > 0) {
                tv_email.setText(fullProfilePOJO.getProfilePOJO().getEmail());
            } else {
                ll_email.setVisibility(View.GONE);
            }
            if (fullProfilePOJO.getProfilePOJO().getMobile().length() > 0) {
                tv_mobile.setText(fullProfilePOJO.getProfilePOJO().getMobile());
            } else {
                ll_phone.setVisibility(View.GONE);
            }
            if (fullProfilePOJO.getProfilePOJO().getAddress().length() > 0) {
                tv_address.setText(fullProfilePOJO.getProfilePOJO().getAddress());
            } else {
                ll_location.setVisibility(View.GONE);
            }

            if (fullProfilePOJO.getAddressPOJOS().size() > 0) {
                AddressPOJO addressPOJO = fullProfilePOJO.getAddressPOJOS().get(fullProfilePOJO.getAddressPOJOS().size() - 1);
                tv_address.setText(addressPOJO.getAddress() + " , " + addressPOJO.getCity() + " , " + addressPOJO.getState() + " , " + addressPOJO.getCountry());
            } else {
                ll_location.setVisibility(View.GONE);
            }

            if (fullProfilePOJO.getWorkPOJOList().size() > 0) {
                WorkPOJO workPOJO = fullProfilePOJO.getWorkPOJOList().get(fullProfilePOJO.getWorkPOJOList().size() - 1);
                tv_work.setText(workPOJO.getWorkPosition() + " , " + workPOJO.getWorkLocation() + " , " + workPOJO.getWorkLocation());
            } else {
                ll_work.setVisibility(View.GONE);
            }

            if (fullProfilePOJO.getEducationPOJOS().size() > 0) {
                EducationPOJO educationPOJO = fullProfilePOJO.getEducationPOJOS().get(fullProfilePOJO.getEducationPOJOS().size() - 1);
                tv_education.setText(educationPOJO.getQualification() + " , " + educationPOJO.getQualificationUniversity() + " , " + educationPOJO.getQualificationLocation());
            } else {
                ll_education.setVisibility(View.GONE);
            }

            if (fullProfilePOJO.getProfilePOJO().getUserBio().length() > 0) {
                tv_bio.setText(fullProfilePOJO.getProfilePOJO().getUserBio());
            }
            if (fullProfilePOJO.getFriendsProfilePOJOS().size() > 0) {
                friendProfilePOJOS.clear();
                friendProfilePOJOS.addAll(fullProfilePOJO.getFriendsProfilePOJOS());
                friendsHorizontalAdapter.notifyDataSetChanged();
            }

//            if (fullProfilePOJO.getProfilePOJO().getUserTypeId() != null) {
//                if (fullProfilePOJO.getProfilePOJO().getUserTypeId().equalsIgnoreCase("2")) {
//                    iv_leader_icon.setVisibility(View.VISIBLE);
//                } else {
//                    iv_leader_icon.setVisibility(View.GONE);
//                }
//            }

//            if (fullProfilePOJO.getProfilePOJO().getUserProfileId().equalsIgnoreCase(Constants.userProfilePOJO.getUserProfileId())) {
//                ll_contribute.setVisibility(View.GONE);
//            } else {
//                ll_contribute.setVisibility(View.VISIBLE);
//            }
//
//            ll_contribute.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (getActivity() instanceof HomeActivity) {
//                        HomeActivity homeActivity = (HomeActivity) getActivity();
//                        homeActivity.replaceFragmentinFrameHome(new ContributeAmountFragment(fullProfilePOJO.getProfilePOJO()), "ContributeAmountFragment");
//                    }
//                }
//            });

            tv_bio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateBIO(tv_bio.getText().toString());
                }
            });
//            ll_incoming_request.setVisibility(View.GONE);
//            switch (fullProfilePOJO.getProfilePOJO().getMyFriend()) {
//                case 0:
//                    tv_add_friend.setText("Add Friend");
//                    break;
//                case 1:
//                    tv_add_friend.setText("Cancel Request");
//                    break;
//                case 2:
//                    ll_incoming_request.setVisibility(View.VISIBLE);
//                    tv_add_friend.setText("Respond");
//                    break;
//                case 3:
//                    tv_add_friend.setText("Unfriend");
//                    break;
//                case 4:
//                    tv_add_friend.setText("Follow");
//                    break;
//                case -1:
//                    break;
//            }
//
//            ll_friend.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    switch (fullProfilePOJO.getProfilePOJO().getMyFriend()) {
//                        case 0:
//                            sendFriendRequest(fullProfilePOJO.getProfilePOJO());
//                            break;
//                        case 1:
//                            undoFriendRequest(fullProfilePOJO.getProfilePOJO());
//                            break;
//                        case 2:
//                            break;
//                        case 3:
//                            sendFriendRequest(fullProfilePOJO.getProfilePOJO());
//                            break;
//                        case 4:
//                            break;
//                        case -1:
//                            break;
//                    }
//                }
//            });
//
//            btn_confirm_friend.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    acceptRequest(fullProfilePOJO.getProfilePOJO());
//                }
//            });
//
//            btn_cancel_request.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    cancelFriendRequest(fullProfilePOJO.getProfilePOJO());
//                }
//            });

        }
    }

    public void updateBIO(String bio) {
        final Dialog dialog1 = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dialog_update_bio);
        dialog1.setTitle("Update Bio");
        dialog1.show();
        dialog1.setCancelable(true);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView btn_update = dialog1.findViewById(R.id.btn_update);
        final EditText et_bio = dialog1.findViewById(R.id.et_bio);
        et_bio.setText(bio);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_bio.getText().toString().length() > 0) {
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                    nameValuePairs.add(new BasicNameValuePair("user_id", userProfilePOJO.getUserId()));
                    nameValuePairs.add(new BasicNameValuePair("user_profile_id", userProfilePOJO.getUserProfileId()));
                    nameValuePairs.add(new BasicNameValuePair("user_bio", et_bio.getText().toString()));

                    new WebServiceBase(nameValuePairs, getActivity(), new WebServicesCallBack() {
                        @Override
                        public void onGetMsg(String apicall, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.optString("status").equals("success")) {
                                    tv_bio.setText(et_bio.getText().toString());
                                    dialog1.dismiss();
                                } else {
                                    ToastClass.showShortToast(getActivity().getApplicationContext(), jsonObject.optString("message"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, "UPDAT_BIO", true).execute(WebServicesUrls.UPDATE_BIO);
                } else {
                    ToastClass.showShortToast(getActivity().getApplicationContext(), "Please Enter Bio Description");
                }
            }
        });

    }

    public void getFriendSummary() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("friend_user_profile_id", userProfilePOJO.getUserProfileId()));
        new WebServiceBaseResponseList<SummaryPOJO>(nameValuePairs, getActivity(), new ResponseListCallback<SummaryPOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO<SummaryPOJO> responseListPOJO) {
                try {
                    if (responseListPOJO.isSuccess()) {
                        SummaryAdapter summaryAdapter = new SummaryAdapter(getActivity(), UserProfileFragment.this, responseListPOJO.getResultList(), fullProfilePOJO.getProfilePOJO().getUserId(), fullProfilePOJO.getProfilePOJO().getUserProfileId());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        rv_activity.setHasFixedSize(true);
                        rv_activity.setAdapter(summaryAdapter);
                        rv_activity.setLayoutManager(linearLayoutManager);
                        rv_activity.setItemAnimator(new DefaultItemAnimator());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, SummaryPOJO.class, "USER SUMMARY", true).execute(WebServicesUrls.GET_MY_FRIEND_TOTAL_SUMMARY);
    }

    FriendsHorizontalAdapter friendsHorizontalAdapter;

    public void attachFriendsAdapter() {

        friendsHorizontalAdapter = new FriendsHorizontalAdapter(getActivity(), this, friendProfilePOJOS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_friends.setHasFixedSize(true);
        rv_friends.setAdapter(friendsHorizontalAdapter);
        rv_friends.setLayoutManager(linearLayoutManager);
        rv_friends.setItemAnimator(new DefaultItemAnimator());
    }
}
