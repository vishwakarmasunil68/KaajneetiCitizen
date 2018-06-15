package com.ritvi.kaajneeti.fragment.information;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.information.InformationPOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class InformationDetailFragment extends FragmentController {

    @BindView(R.id.cv_profile_pic)
    CircleImageView cv_profile_pic;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.cv_leader_profile_pic)
    CircleImageView cv_leader_profile_pic;
    @BindView(R.id.tv_leader_profile_name)
    TextView tv_leader_profile_name;
    @BindView(R.id.tv_suggestion_subject)
    TextView tv_suggestion_subject;
    @BindView(R.id.tv_description)
    TextView tv_description;
    @BindView(R.id.tv_date)
    TextView tv_date;
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
    @BindView(R.id.ll_back)
    LinearLayout ll_back;

    String information_id = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_information_detail, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            information_id = getArguments().getString("information_id");
            getSuggestionDetails();
        }


        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    InformationPOJO informationPOJO;

    public void getSuggestionDetails() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("information_id", information_id));
        new WebServiceBaseResponse<InformationPOJO>(nameValuePairs, getActivity(), new ResponseCallBack<InformationPOJO>() {
            @Override
            public void onGetMsg(ResponsePOJO<InformationPOJO> responsePOJO) {
                try {
                    if (responsePOJO.isSuccess()) {
                        informationPOJO = responsePOJO.getResult();
                        loadView();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, InformationPOJO.class, "COMPLAINT_DETAIL", true).execute(WebServicesUrls.GET_INFORMATION_DETAIL);
    }

    public void loadView() {
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


        if (informationPOJO != null) {
            Glide.with(getActivity().getApplicationContext())
                    .load(informationPOJO.getInformationProfile().getProfilePhotoPath())
                    .placeholder(R.drawable.ic_default_profile_pic)
                    .error(R.drawable.ic_default_profile_pic)
                    .dontAnimate()
                    .into(cv_profile_pic);

//            tv_user_name.setText(informationPOJO.getSuggestionProfile().getFirstName() + " " + informationPOJO.getSuggestionProfile().getLastName());
//            tv_leader_profile_name.setText(informationPOJO.getSuggestionAssigned().get(0).getFirstName() + " " + informationPOJO.getSuggestionAssigned().get(0).getLastName());
//            tv_suggestion_subject.setText(informationPOJO.getSuggestionSubject());
//            tv_description.setText(informationPOJO.getSuggestionDescription());
//            tv_date.setText(UtilityFunction.convertServerDateTime(informationPOJO.getAddedOnTime()));
//
//
//            if (informationPOJO.getSuggestionAttachment().size() > 0) {
//
//                if (informationPOJO.getSuggestionAttachment().size() == 1) {
//                    iv_feed_image.setVisibility(View.VISIBLE);
//                    ll_image_2.setVisibility(View.GONE);
//                    ll_image_3.setVisibility(View.GONE);
//                    Glide.with(getActivity().getApplicationContext())
//                            .load(informationPOJO.getSuggestionAttachment().get(0).getAttachmentFile())
//                            .error(R.drawable.ic_default_profile_pic)
//                            .placeholder(R.drawable.ic_default_profile_pic)
//                            .dontAnimate()
//                            .into(iv_feed_image);
//                } else if (informationPOJO.getSuggestionAttachment().size() == 2) {
//                    iv_feed_image.setVisibility(View.GONE);
//                    ll_image_2.setVisibility(View.VISIBLE);
//                    ll_image_3.setVisibility(View.GONE);
//                    Glide.with(getActivity().getApplicationContext())
//                            .load(informationPOJO.getSuggestionAttachment().get(0).getAttachmentFile())
//                            .error(R.drawable.ic_default_profile_pic)
//                            .placeholder(R.drawable.ic_default_profile_pic)
//                            .dontAnimate()
//                            .into(iv_1);
//
//                    Glide.with(getActivity().getApplicationContext())
//                            .load(informationPOJO.getSuggestionAttachment().get(1).getAttachmentFile())
//                            .error(R.drawable.ic_default_profile_pic)
//                            .placeholder(R.drawable.ic_default_profile_pic)
//                            .dontAnimate()
//                            .into(iv_2);
//
//
//                } else if (informationPOJO.getSuggestionAttachment().size() > 2) {
//                    iv_feed_image.setVisibility(View.GONE);
//                    ll_image_2.setVisibility(View.GONE);
//                    ll_image_3.setVisibility(View.VISIBLE);
//
//                    Glide.with(getActivity().getApplicationContext())
//                            .load(informationPOJO.getSuggestionAttachment().get(0).getAttachmentFile())
//                            .error(R.drawable.ic_default_profile_pic)
//                            .placeholder(R.drawable.ic_default_profile_pic)
//                            .dontAnimate()
//                            .into(iv_3);
//
//                    Glide.with(getActivity().getApplicationContext())
//                            .load(informationPOJO.getSuggestionAttachment().get(1).getAttachmentFile())
//                            .error(R.drawable.ic_default_profile_pic)
//                            .placeholder(R.drawable.ic_default_profile_pic)
//                            .dontAnimate()
//                            .into(iv_4);
//
//                    Glide.with(getActivity().getApplicationContext())
//                            .load(informationPOJO.getSuggestionAttachment().get(2).getAttachmentFile())
//                            .error(R.drawable.ic_default_profile_pic)
//                            .placeholder(R.drawable.ic_default_profile_pic)
//                            .dontAnimate()
//                            .into(iv_5);
//                    if (informationPOJO.getSuggestionAttachment().size() > 3) {
//                        tv_more_img.setVisibility(View.VISIBLE);
//                        tv_more_img.setText("+" + (informationPOJO.getSuggestionAttachment().size() - 3));
//                    }
//                }
//
//            } else {
//                ll_attachments.setVisibility(View.GONE);
//                iv_feed_image.setVisibility(View.GONE);
//                ll_image_2.setVisibility(View.GONE);
//                ll_image_3.setVisibility(View.GONE);
//            }

        }

    }
}
