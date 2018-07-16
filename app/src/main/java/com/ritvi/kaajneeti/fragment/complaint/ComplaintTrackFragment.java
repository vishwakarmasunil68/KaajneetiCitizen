package com.ritvi.kaajneeti.fragment.complaint;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.adapter.ComplaintHistoryAdapter;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintHistoryPOJO;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintPOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponseList;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;
import com.ritvi.kaajneeti.webservice.WebUploadService;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class ComplaintTrackFragment extends FragmentController {

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
    @BindView(R.id.et_reply)
    EditText et_reply;
    @BindView(R.id.iv_send_comment)
    ImageView iv_send_comment;
    @BindView(R.id.frame_reply)
    LinearLayout frame_reply;
    @BindView(R.id.iv_attach)
    ImageView iv_attach;
    @BindView(R.id.reveal_items)
    LinearLayout mRevealView;

    boolean hidden = true;
    String complaint_id = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_complaint_track, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachAdapter();
        if (getArguments() != null) {
            complaint_id = getArguments().getString("complaint_id");
            getComplaintHistory();
            getComplaintDetail();
            tv_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("complaint_id", complaint_id);
                    CreateComplaintReplyFragment createComplaintReplyFragment = new CreateComplaintReplyFragment();
                    createComplaintReplyFragment.setArguments(bundle);
                    activityManager.startFragmentForResult(R.id.frame_home, ComplaintTrackFragment.this, createComplaintReplyFragment, 101);
                }
            });
        }

        mRevealView.setVisibility(View.INVISIBLE);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iv_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_reply.getText().toString().length() > 0) {
                    saveComplaintReply();
                }
            }
        });
        iv_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAttachAnimation();
            }
        });
    }

    public void startAttachAnimation() {
        int cx = (mRevealView.getLeft());
        int cy = mRevealView.getBottom();

        int radius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {


            SupportAnimator animator =
                    ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(800);

            SupportAnimator animator_reverse = animator.reverse();

            if (hidden) {
                mRevealView.setVisibility(View.VISIBLE);
                animator.start();
                hidden = false;
            } else {
                animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        mRevealView.setVisibility(View.INVISIBLE);
                        hidden = true;

                    }

                    @Override
                    public void onAnimationCancel() {

                    }

                    @Override
                    public void onAnimationRepeat() {

                    }
                });
                animator_reverse.start();

            }
        } else {
            if (hidden) {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
                mRevealView.setVisibility(View.VISIBLE);
                anim.start();
                hidden = false;

            } else {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, radius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mRevealView.setVisibility(View.INVISIBLE);
                        hidden = true;
                    }
                });
                anim.start();

            }
        }

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
                        complaintPOJO = responsePOJO.getResult();
                        if (complaintPOJO.getComplaintStatus().equalsIgnoreCase("4")) {
                            frame_reply.setVisibility(View.GONE);
                        } else {
                            frame_reply.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, ComplaintPOJO.class, "COMPLAINT_DETAIL", false).execute(WebServicesUrls.GET_COMPLAINT_DETAIL);
    }


    public void getComplaintHistory() {
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
                        ComplaintHistoryPOJO complaintHistoryPOJO = responseListPOJO.getResultList().get(responseListPOJO.getResultList().size() - 1);
                        Glide.with(getActivity().getApplicationContext())
                                .load(complaintHistoryPOJO.getComplaintHistoryProfile().getProfilePhotoPath())
                                .placeholder(R.drawable.ic_default_profile_pic)
                                .error(R.drawable.ic_default_profile_pic)
                                .dontAnimate()
                                .into(cv_leader_profile_pic);
                        String leader_name = complaintHistoryPOJO.getComplaintHistoryProfile().getFirstName() + " " + complaintHistoryPOJO.getComplaintHistoryProfile().getLastName();
                        tv_acceptance.setText(Html.fromHtml("Leader <b>" + leader_name + "</b> accepted your complaint."));
                        tv_accepted_date.setText(UtilityFunction.convertServerDateTime(complaintHistoryPOJO.getAddedOnTime()));

                        if (responseListPOJO.getResultList().size() > 0) {
                            if (responseListPOJO.getResultList().size() > 1) {
                                for (int i = 0; i < responseListPOJO.getResultList().size() - 1; i++) {
                                    ComplaintHistoryPOJO complaintHistoryPOJO1 = responseListPOJO.getResultList().get(i);
                                    complaintHistoryPOJOS.add(complaintHistoryPOJO1);
                                }
                            }
                        } else {
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

    public void refreshOnIncomingNotification(String feed_id) {
        if (complaint_id.equalsIgnoreCase(feed_id)) {
            getComplaintHistory();
        }
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

    public void saveComplaintReply() {
        try {
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("", ""));
            reqEntity.addPart("user_profile_id", new StringBody(Constants.userProfilePOJO.getUserProfileId()));
            reqEntity.addPart("complaint_id", new StringBody(complaint_id));
            reqEntity.addPart("history_id", new StringBody(""));
            reqEntity.addPart("title", new StringBody("title"));
            reqEntity.addPart("description", new StringBody(et_reply.getText().toString()));

            new WebUploadService(reqEntity, getActivity(), new WebServicesCallBack() {
                @Override
                public void onGetMsg(String apicall, String response) {
                    Log.d(TagUtils.getTag(), apicall + " :- " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        ToastClass.showShortToast(getActivity().getApplicationContext(), jsonObject.optString("message"));
                        if (jsonObject.optString("status").equals("success")) {
                            getComplaintHistory();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "CREATE COMPLAINT REPLY", true).execute(WebServicesUrls.SAVE_COMPLAINT_HISTORY);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        et_reply.setText("");
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            getComplaintDetail();
            getComplaintHistory();
        }
    }
}
