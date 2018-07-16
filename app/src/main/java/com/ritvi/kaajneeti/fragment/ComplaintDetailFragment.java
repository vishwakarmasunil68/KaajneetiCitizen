package com.ritvi.kaajneeti.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.activity.SignActivity;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.adapter.FriendsHorizontalAdapter;
import com.ritvi.kaajneeti.fragment.complaint.ComplaintTrackFragment;
import com.ritvi.kaajneeti.fragment.user.UserProfileFragment;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.attachments.AttachmentPOJO;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintAttachmentPOJO;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;
import com.ritvi.kaajneeti.webservice.WebUploadService;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ComplaintDetailFragment extends FragmentController {

    @BindView(R.id.cv_profile_pic)
    CircleImageView cv_profile_pic;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.cv_leader_profile_pic)
    CircleImageView cv_leader_profile_pic;
    @BindView(R.id.tv_leader_profile_name)
    TextView tv_leader_profile_name;
    @BindView(R.id.tv_complaint_subject)
    TextView tv_complaint_subject;
    @BindView(R.id.tv_description)
    TextView tv_description;
    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.tv_mobile_number)
    TextView tv_mobile_number;
    @BindView(R.id.ll_applicant_detail)
    LinearLayout ll_applicant_detail;
    @BindView(R.id.ll_complaint_members)
    LinearLayout ll_complaint_members;
    @BindView(R.id.rv_complaint_members)
    RecyclerView rv_complaint_members;
    @BindView(R.id.ll_attachments)
    LinearLayout ll_attachments;
    @BindView(R.id.iv_feed_image)
    ImageView iv_feed_image;
    @BindView(R.id.iv_1)
    ImageView iv_1;
    @BindView(R.id.iv_2)
    ImageView iv_2;
    @BindView(R.id.iv_3)
    ImageView iv_3;
    @BindView(R.id.iv_4)
    ImageView iv_4;
    @BindView(R.id.iv_5)
    ImageView iv_5;
    @BindView(R.id.tv_more_img)
    TextView tv_more_img;
    @BindView(R.id.ll_image_2)
    LinearLayout ll_image_2;
    @BindView(R.id.ll_image_3)
    LinearLayout ll_image_3;
    @BindView(R.id.tv_track)
    TextView tv_track;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.ll_images)
    LinearLayout ll_images;
    @BindView(R.id.ll_user_profile)
    LinearLayout ll_user_profile;
    @BindView(R.id.ll_leader_profile)
    LinearLayout ll_leader_profile;
    @BindView(R.id.ll_attende)
    LinearLayout ll_attende;
    @BindView(R.id.btn_accept)
    Button btn_accept;
    @BindView(R.id.btn_reject)
    Button btn_reject;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    String complaint_id = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_complaint_detail, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            complaint_id = getArguments().getString("complaint_id");
            getComplaintDetail(true);
        }
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getComplaintDetail(true);
            }
        });
    }

    ComplaintPOJO complaintPOJO;

    public void getComplaintDetail(boolean is_loading) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("complaint_id", complaint_id));
        new WebServiceBaseResponse<ComplaintPOJO>(nameValuePairs, getActivity(), new ResponseCallBack<ComplaintPOJO>() {
            @Override
            public void onGetMsg(ResponsePOJO<ComplaintPOJO> responsePOJO) {
                swipeRefreshLayout.setRefreshing(false);
                try {
                    if (responsePOJO.isSuccess()) {
                        complaintPOJO = responsePOJO.getResult();
                        loadView(complaintPOJO);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, ComplaintPOJO.class, "COMPLAINT_DETAIL", is_loading).execute(WebServicesUrls.GET_COMPLAINT_DETAIL);
    }

    public void loadView(final ComplaintPOJO complaintPOJO) {
        /*
         * complaint status
         * 1=new created or not accepted by leader
         * 2=accepted by leader
         * 3=rejected by leader
         * 4=completed
         * 5=inactive
         * 6=request to close
         * -1=deleted
         * */


        if (complaintPOJO != null) {
            Log.d(TagUtils.getTag(), "image:-" + complaintPOJO.getComplaintProfile().getProfilePhotoPath());
            Glide.with(getActivity().getApplicationContext())
                    .load(complaintPOJO.getComplaintProfile().getProfilePhotoPath())
                    .placeholder(R.drawable.ic_default_profile_pic)
                    .error(R.drawable.ic_default_profile_pic)
                    .dontAnimate()
                    .into(cv_profile_pic);
            Glide.with(getActivity().getApplicationContext())
                    .load(complaintPOJO.getComplaintAssigned().get(0).getProfilePhotoPath())
                    .placeholder(R.drawable.ic_default_profile_pic)
                    .error(R.drawable.ic_default_profile_pic)
                    .dontAnimate()
                    .into(cv_leader_profile_pic);

            ll_user_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() instanceof HomeActivity) {

                        HomeActivity homeActivity = (HomeActivity) getActivity();

                        UserProfileFragment userProfileFragment = new UserProfileFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user_profile_id", complaintPOJO.getComplaintProfile().getUserProfileId());
                        userProfileFragment.setArguments(bundle);

                        homeActivity.startFragment(R.id.frame_home, userProfileFragment);
                    }
                }
            });

            ll_leader_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() instanceof HomeActivity) {

                        HomeActivity homeActivity = (HomeActivity) getActivity();

                        UserProfileFragment userProfileFragment = new UserProfileFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user_profile_id", complaintPOJO.getComplaintAssigned().get(0).getUserProfileId());
                        userProfileFragment.setArguments(bundle);

                        homeActivity.startFragment(R.id.frame_home, userProfileFragment);
                    }
                }
            });

            tv_user_name.setText(complaintPOJO.getComplaintProfile().getFirstName() + " " + complaintPOJO.getComplaintProfile().getLastName());
            tv_leader_profile_name.setText(complaintPOJO.getComplaintAssigned().get(0).getFirstName() + " " + complaintPOJO.getComplaintAssigned().get(0).getLastName());
            tv_complaint_subject.setText(complaintPOJO.getComplaintSubject());
            tv_description.setText(complaintPOJO.getComplaintDescription());
            tv_location.setText(complaintPOJO.getComplaintAddress());
            if (complaintPOJO.getServerLocationPOJO() != null) {
                tv_location.setText(complaintPOJO.getServerLocationPOJO().getLocationAddress());
            }
            tv_date.setText(UtilityFunction.convertServerDateTime(complaintPOJO.getAddedOnTime()));

            if (complaintPOJO.getComplaintTypeId().equalsIgnoreCase("3")) {
                ll_applicant_detail.setVisibility(View.VISIBLE);
                tv_name.setText(complaintPOJO.getApplicantName());
                tv_mobile_number.setText(complaintPOJO.getApplicantMobile());
                tv_email.setText(complaintPOJO.getApplicantFatherName());
            } else {
                ll_applicant_detail.setVisibility(View.GONE);
            }
            attachFriendsAdapter();
            if (complaintPOJO.getComplaintMemberPOJOS().size() > 0) {
                ll_complaint_members.setVisibility(View.VISIBLE);
                membersUserProfilePOJOS.clear();
                membersUserProfilePOJOS.addAll(complaintPOJO.getComplaintMemberPOJOS());
                friendsHorizontalAdapter.notifyDataSetChanged();
            } else {
                ll_complaint_members.setVisibility(View.GONE);
            }

            if (complaintPOJO.getComplaintAttachments().size() > 0) {

                if (complaintPOJO.getComplaintAttachments().size() == 1) {
                    iv_feed_image.setVisibility(View.VISIBLE);
                    ll_image_2.setVisibility(View.GONE);
                    ll_image_3.setVisibility(View.GONE);
                    Glide.with(getActivity().getApplicationContext())
                            .load(complaintPOJO.getComplaintAttachments().get(0).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(iv_feed_image);
                } else if (complaintPOJO.getComplaintAttachments().size() == 2) {
                    iv_feed_image.setVisibility(View.GONE);
                    ll_image_2.setVisibility(View.VISIBLE);
                    ll_image_3.setVisibility(View.GONE);
                    Glide.with(getActivity().getApplicationContext())
                            .load(complaintPOJO.getComplaintAttachments().get(0).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(iv_1);

                    Glide.with(getActivity().getApplicationContext())
                            .load(complaintPOJO.getComplaintAttachments().get(1).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(iv_2);


                } else if (complaintPOJO.getComplaintAttachments().size() > 2) {
                    iv_feed_image.setVisibility(View.GONE);
                    ll_image_2.setVisibility(View.GONE);
                    ll_image_3.setVisibility(View.VISIBLE);

                    Glide.with(getActivity().getApplicationContext())
                            .load(complaintPOJO.getComplaintAttachments().get(0).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(iv_3);

                    Glide.with(getActivity().getApplicationContext())
                            .load(complaintPOJO.getComplaintAttachments().get(1).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(iv_4);

                    Glide.with(getActivity().getApplicationContext())
                            .load(complaintPOJO.getComplaintAttachments().get(2).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(iv_5);
                    if (complaintPOJO.getComplaintAttachments().size() > 3) {
                        tv_more_img.setVisibility(View.VISIBLE);
                        tv_more_img.setText("+" + (complaintPOJO.getComplaintAttachments().size() - 3));
                    }
                }

                ll_images.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<AttachmentPOJO> attachmentPOJOS = new ArrayList<>();
                        for (ComplaintAttachmentPOJO complaintAttachmentPOJO : complaintPOJO.getComplaintAttachments()) {
                            AttachmentPOJO attachmentPOJO = new AttachmentPOJO();
                            attachmentPOJO.setFile_name(complaintAttachmentPOJO.getAttachmentFile());
                            attachmentPOJO.setFile_path(complaintAttachmentPOJO.getAttachmentFile());
                            attachmentPOJO.setFile_type(complaintAttachmentPOJO.getAttachmentFile());
                            attachmentPOJO.setFeed_type("complaint");
                            attachmentPOJO.setDescription(complaintPOJO.getComplaintSubject());

                            attachmentPOJOS.add(attachmentPOJO);
                        }

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("attachments", (Serializable) attachmentPOJOS);

                        AttachmentViewPagerFragment attachmentViewPagerFragment = new AttachmentViewPagerFragment();
                        attachmentViewPagerFragment.setArguments(bundle);

                        if (getActivity() instanceof HomeActivity) {
                            HomeActivity homeActivity = (HomeActivity) getActivity();
                            homeActivity.startFragment(R.id.frame_home, attachmentViewPagerFragment);
                        }

                    }
                });

            } else {
                ll_attachments.setVisibility(View.GONE);
                iv_feed_image.setVisibility(View.GONE);
                ll_image_2.setVisibility(View.GONE);
                ll_image_3.setVisibility(View.GONE);
            }


            if (complaintPOJO.getComplaintStatus().equalsIgnoreCase("2") ||
                    complaintPOJO.getComplaintStatus().equalsIgnoreCase("4") ||
                    complaintPOJO.getComplaintStatus().equalsIgnoreCase("6")
                    ) {
                tv_track.setVisibility(View.VISIBLE);
            } else {
                tv_track.setVisibility(View.GONE);
            }

            tv_track.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("complaint_id", complaint_id);

                    ComplaintTrackFragment complaintTrackFragment = new ComplaintTrackFragment();
                    complaintTrackFragment.setArguments(bundle);
                    activityManager.startFragment(R.id.frame_home, complaintTrackFragment);
                }
            });


            if (complaintPOJO.getComplaintMemberPOJOS() != null && complaintPOJO.getComplaintMemberPOJOS().size() > 0) {
                boolean show_ll_attendee = false;
                boolean is_accepted = false;
                boolean is_rejected=false;
                for (UserProfilePOJO userProfilePOJO : complaintPOJO.getComplaintMemberPOJOS()) {
                    if (userProfilePOJO.getUserProfileId().equalsIgnoreCase(Constants.userProfilePOJO.getUserProfileId())) {
                        show_ll_attendee = true;
                        if (userProfilePOJO.getAcceptedYesNo().equalsIgnoreCase("1")) {
                            is_accepted = true;
                        }else if(userProfilePOJO.getAcceptedYesNo().equalsIgnoreCase("-1")){
                            is_rejected=true;
                        }
                    }
                }

                if(is_rejected){
                    rejectDialog();
                }
                if (show_ll_attendee) {
                    if (is_accepted) {
                        ll_attende.setVisibility(View.GONE);
                    } else {
                        ll_attende.setVisibility(View.VISIBLE);
                    }

                    btn_accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(getActivity(), SignActivity.class), Constants.ACTIVITY_SIGNATURE);
                        }
                    });

                    btn_reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rejectedComplaintDialog();
                        }
                    });

                } else {
                    ll_attende.setVisibility(View.GONE);
                }
            } else {
                ll_attende.setVisibility(View.GONE);
            }
        }
    }

    public void rejectedComplaintDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("Do you want to rejected the invitation of this complaint");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                rejectComplaint();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    public void rejectDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Warning");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("you rejected the complaint invitation.");
        alertDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });
        alertDialog.show();
    }

    FriendsHorizontalAdapter friendsHorizontalAdapter;
    List<UserProfilePOJO> membersUserProfilePOJOS = new ArrayList<>();

    public void attachFriendsAdapter() {

        friendsHorizontalAdapter = new FriendsHorizontalAdapter(getActivity(), this, membersUserProfilePOJOS,true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_complaint_members.setHasFixedSize(true);
        rv_complaint_members.setAdapter(friendsHorizontalAdapter);
        rv_complaint_members.setLayoutManager(linearLayoutManager);
        rv_complaint_members.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ACTIVITY_SIGNATURE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TagUtils.getTag(), "on activity result");
                String file_path = data.getStringExtra("result");
                updateInvitaion(file_path);
            }
        }
    }


    public void updateInvitaion(String file_path) {
        try {
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("", ""));
            reqEntity.addPart("user_profile_id", new StringBody(Constants.userProfilePOJO.getUserProfileId()));
            reqEntity.addPart("complaint_id", new StringBody(complaintPOJO.getComplaintId()));
            FileBody fileBody = new FileBody(new File(file_path));
            reqEntity.addPart("file", fileBody);

            new WebUploadService(reqEntity, getActivity(), new WebServicesCallBack() {
                @Override
                public void onGetMsg(String apicall, String response) {
                    Log.d(TagUtils.getTag(), apicall + " :- " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        ToastClass.showShortToast(getActivity().getApplicationContext(), jsonObject.optString("message"));
                        if (jsonObject.optString("status").equals("success")) {
                            ComplaintPOJO complaintPOJO = new Gson().fromJson(jsonObject.optString("result").toString(), ComplaintPOJO.class);
                            ComplaintDetailFragment.this.complaintPOJO = complaintPOJO;
                            loadView(complaintPOJO);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "UPDATE_COMPLAINT", true).execute(WebServicesUrls.UPDATE_COMPLAINT_INVITATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rejectComplaint() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("complaint_id", complaintPOJO.getComplaintId()));
        new WebServiceBase(nameValuePairs, getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("status").equals("success")) {
                        ComplaintPOJO complaintPOJO = new Gson().fromJson(jsonObject.optString("result").toString(), ComplaintPOJO.class);
                        ComplaintDetailFragment.this.complaintPOJO = complaintPOJO;
                        loadView(complaintPOJO);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "UPDATE_COMPLAINT_CANCEL", true).execute(WebServicesUrls.DELETE_COMPLAINT_INVITATION);
    }

    public void refreshOnIncomingNotification(String feedId) {
        if(complaint_id.equalsIgnoreCase(feedId)){
            getComplaintDetail(false);
        }
    }
}
