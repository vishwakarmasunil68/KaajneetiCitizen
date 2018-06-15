package com.ritvi.kaajneeti.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Line;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.adapter.FriendsHorizontalAdapter;
import com.ritvi.kaajneeti.fragment.complaint.ComplaintTrackFragment;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.attachments.AttachmentPOJO;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintAttachmentPOJO;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintPOJO;
import com.ritvi.kaajneeti.pojo.information.InformationAttachmentPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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

    String complaint_id="";
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
        if(getArguments()!=null){
            complaint_id=getArguments().getString("complaint_id");
            getComplaintDetail();
        }
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
    }
    ComplaintPOJO complaintPOJO;
    public void getComplaintDetail() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("complaint_id", complaint_id));
        new WebServiceBaseResponse<ComplaintPOJO>(nameValuePairs, getActivity(), new ResponseCallBack<ComplaintPOJO>() {
            @Override
            public void onGetMsg(ResponsePOJO<ComplaintPOJO> responsePOJO) {
                try {
                    if (responsePOJO.isSuccess()) {
                        complaintPOJO=responsePOJO.getResult();
                        loadView();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, ComplaintPOJO.class, "COMPLAINT_DETAIL", true).execute(WebServicesUrls.GET_COMPLAINT_DETAIL);
    }

    public void loadView(){
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


        if(complaintPOJO!=null) {
            Log.d(TagUtils.getTag(),"image:-"+complaintPOJO.getComplaintProfile().getProfilePhotoPath());
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

            tv_user_name.setText(complaintPOJO.getComplaintProfile().getFirstName() + " " + complaintPOJO.getComplaintProfile().getLastName());
            tv_leader_profile_name.setText(complaintPOJO.getComplaintAssigned().get(0).getFirstName() + " " + complaintPOJO.getComplaintAssigned().get(0).getLastName());
            tv_complaint_subject.setText(complaintPOJO.getComplaintSubject());
            tv_description.setText(complaintPOJO.getComplaintDescription());
            tv_location.setText(complaintPOJO.getComplaintAddress());
            if(complaintPOJO.getServerLocationPOJO()!=null){
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
                        List<AttachmentPOJO> attachmentPOJOS=new ArrayList<>();
                        for(ComplaintAttachmentPOJO complaintAttachmentPOJO:complaintPOJO.getComplaintAttachments()){
                            AttachmentPOJO attachmentPOJO=new AttachmentPOJO();
                            attachmentPOJO.setFile_name(complaintAttachmentPOJO.getAttachmentFile());
                            attachmentPOJO.setFile_path(complaintAttachmentPOJO.getAttachmentFile());
                            attachmentPOJO.setFile_type(complaintAttachmentPOJO.getAttachmentFile());
                            attachmentPOJO.setFeed_type("complaint");
                            attachmentPOJO.setDescription(complaintPOJO.getComplaintSubject());

                            attachmentPOJOS.add(attachmentPOJO);
                        }

                        Bundle bundle=new Bundle();
                        bundle.putSerializable("attachments", (Serializable) attachmentPOJOS);

                        AttachmentViewPagerFragment attachmentViewPagerFragment=new AttachmentViewPagerFragment();
                        attachmentViewPagerFragment.setArguments(bundle);

                        if(getActivity() instanceof HomeActivity){
                            HomeActivity homeActivity= (HomeActivity) getActivity();
                            homeActivity.startFragment(R.id.frame_home,attachmentViewPagerFragment);
                        }

                    }
                });

            } else {
                ll_attachments.setVisibility(View.GONE);
                iv_feed_image.setVisibility(View.GONE);
                ll_image_2.setVisibility(View.GONE);
                ll_image_3.setVisibility(View.GONE);
            }


            if(complaintPOJO.getComplaintStatus().equalsIgnoreCase("2")||
                    complaintPOJO.getComplaintStatus().equalsIgnoreCase("4")||
                    complaintPOJO.getComplaintStatus().equalsIgnoreCase("6")
                    ){
                tv_track.setVisibility(View.VISIBLE);
            }else{
                tv_track.setVisibility(View.GONE);
            }

            tv_track.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle=new Bundle();
                    bundle.putString("complaint_id",complaint_id);

                    ComplaintTrackFragment complaintTrackFragment=new ComplaintTrackFragment();
                    complaintTrackFragment.setArguments(bundle);
                    activityManager.startFragment(R.id.frame_home,complaintTrackFragment);
                }
            });
        }

    }

    FriendsHorizontalAdapter friendsHorizontalAdapter;
    List<UserProfilePOJO> membersUserProfilePOJOS=new ArrayList<>();
    public void attachFriendsAdapter() {

        friendsHorizontalAdapter = new FriendsHorizontalAdapter(getActivity(), this, membersUserProfilePOJOS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_complaint_members.setHasFixedSize(true);
        rv_complaint_members.setAdapter(friendsHorizontalAdapter);
        rv_complaint_members.setLayoutManager(linearLayoutManager);
        rv_complaint_members.setItemAnimator(new DefaultItemAnimator());
    }

}
