package com.ritvi.kaajneeti.fragment.complaint;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.adapter.ComplaintHistoryAdapter;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintHistoryPOJO;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponseList;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ComplaintTrackFragment extends FragmentController{

    @BindView(R.id.rv_history)
    RecyclerView rv_history;
    @BindView(R.id.tv_reply)
    TextView tv_reply;
    @BindView(R.id.tv_acceptance)
    TextView tv_acceptance;
    @BindView(R.id.ll_acceptance)
    LinearLayout ll_acceptance;
    @BindView(R.id.cv_leader_profile_pic)
    CircleImageView cv_leader_profile_pic;
    @BindView(R.id.tv_accepted_date)
    TextView tv_accepted_date;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;

    String complaint_id="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_complaint_track,container,false);
        setUpView(getActivity(),this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachAdapter();
        if(getArguments()!=null){
            complaint_id=getArguments().getString("complaint_id");
            getComplaintHistory();

            tv_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("complaint_id",complaint_id);
                    CreateComplaintReplyFragment createComplaintReplyFragment=new CreateComplaintReplyFragment();
                    createComplaintReplyFragment.setArguments(bundle);
                    activityManager.startFragmentForResult(R.id.frame_home,ComplaintTrackFragment.this,createComplaintReplyFragment,101);
                }
            });
        }

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void getComplaintHistory(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("complaint_id", complaint_id));
        new WebServiceBaseResponseList<ComplaintHistoryPOJO>(nameValuePairs, getActivity(), new ResponseListCallback<ComplaintHistoryPOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO<ComplaintHistoryPOJO> responseListPOJO) {
                try {
                    complaintHistoryPOJOS.clear();
                    if (responseListPOJO.isSuccess()) {
                        ll_acceptance.setVisibility(View.VISIBLE);
                        ComplaintHistoryPOJO complaintHistoryPOJO=responseListPOJO.getResultList().get(responseListPOJO.getResultList().size()-1);
                        Glide.with(getActivity().getApplicationContext())
                                .load(complaintHistoryPOJO.getComplaintHistoryProfile().getProfilePhotoPath())
                                .placeholder(R.drawable.ic_default_profile_pic)
                                .error(R.drawable.ic_default_profile_pic)
                                .dontAnimate()
                                .into(cv_leader_profile_pic);
                        String leader_name=complaintHistoryPOJO.getComplaintHistoryProfile().getFirstName()+" "+complaintHistoryPOJO.getComplaintHistoryProfile().getLastName();
                        tv_acceptance.setText(Html.fromHtml("Leader <b>"+leader_name+"</b> accepted the invitation"));
                        tv_accepted_date.setText(UtilityFunction.convertServerDateTime(complaintHistoryPOJO.getAddedOnTime()));

                        if(responseListPOJO.getResultList().size()>0){
                            if(responseListPOJO.getResultList().size()>1){
                                for(int i=0;i<responseListPOJO.getResultList().size()-1;i++){
                                    ComplaintHistoryPOJO complaintHistoryPOJO1=responseListPOJO.getResultList().get(i);
                                    complaintHistoryPOJOS.add(complaintHistoryPOJO1);
                                }
                            }
                        }else{
                            ll_acceptance.setVisibility(View.GONE);
                        }
                    }
                    complaintHistoryAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, ComplaintHistoryPOJO.class, "CALL_COMPLAINT_DESCRIPTION_API", true).execute(WebServicesUrls.COMPLAINT_DETAIL);
    }

    ComplaintHistoryAdapter complaintHistoryAdapter;
    List<ComplaintHistoryPOJO> complaintHistoryPOJOS = new ArrayList<>();

    public void attachAdapter() {

        complaintHistoryAdapter = new ComplaintHistoryAdapter(getActivity(), this, complaintHistoryPOJOS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_history.setHasFixedSize(true);
        rv_history.setAdapter(complaintHistoryAdapter);
        rv_history.setLayoutManager(linearLayoutManager);
        rv_history.setNestedScrollingEnabled(false);
        rv_history.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if(requestCode==101){
            getComplaintHistory();
        }
    }
}
