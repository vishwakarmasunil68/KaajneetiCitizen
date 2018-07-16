package com.ritvi.kaajneeti.fragment.feed;

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
import android.widget.EditText;
import android.widget.ImageView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.adapter.ComplaintCommentAdapter;
import com.ritvi.kaajneeti.adapter.EventCommentAdapter;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentContants;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintCommentPOJO;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintPOJO;
import com.ritvi.kaajneeti.pojo.event.EventCommentPOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponseList;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class ComplaintCommentFragment extends FragmentController {

    @BindView(R.id.rv_comments)
    RecyclerView rv_comments;
    @BindView(R.id.et_comment)
    EditText et_comment;
    @BindView(R.id.iv_send_comment)
    ImageView iv_send_comment;
    String complaint_id = "";
    int comment_added = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_comment, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        complaint_id = getArguments().getString("complaint_id");
        attachAdapter();

        getAllPostComments();
        iv_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComment();
            }
        });
    }

    public void saveComment() {
        if (et_comment.getText().toString().length() > 0) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
            nameValuePairs.add(new BasicNameValuePair("complaint_id", complaint_id));
            nameValuePairs.add(new BasicNameValuePair("your_comment", et_comment.getText().toString()));

            new WebServiceBaseResponse<ComplaintCommentPOJO>(nameValuePairs, getActivity(), new ResponseCallBack<ComplaintCommentPOJO>() {
                @Override
                public void onGetMsg(ResponsePOJO<ComplaintCommentPOJO> responsePOJO) {
                    try {
                        if (responsePOJO.isSuccess()) {
                            postCommentPOJOS.add(responsePOJO.getResult());
                            comment_added++;
                            et_comment.setText("");
                            setCommentValue();
                            postCommentAdapter.notifyDataSetChanged();
                        } else {
                            ToastClass.showShortToast(getActivity(), responsePOJO.getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, ComplaintCommentPOJO.class, "SAVE_COMMENT", true).execute(WebServicesUrls.SAVE_COMPLAINT_COMMENT);
        }
    }

    public void getAllPostComments() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("complaint_id", complaint_id));
        new WebServiceBaseResponseList<ComplaintCommentPOJO>(nameValuePairs, getActivity(), new ResponseListCallback<ComplaintCommentPOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO<ComplaintCommentPOJO> responseListPOJO) {
                postCommentPOJOS.clear();
                try {
                    Collections.reverse(responseListPOJO.getResultList());
                    comment_added=responseListPOJO.getResultList().size();
                    postCommentPOJOS.addAll(responseListPOJO.getResultList());
                    setCommentValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postCommentAdapter.notifyDataSetChanged();
            }
        }, ComplaintCommentPOJO.class, "post_comments", true).execute(WebServicesUrls.ALL_COMPLAINT_COMMENT);
    }

    ComplaintCommentAdapter postCommentAdapter;
    List<ComplaintCommentPOJO> postCommentPOJOS = new ArrayList<>();

    public void attachAdapter() {
        postCommentAdapter = new ComplaintCommentAdapter(getActivity(), this, postCommentPOJOS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_comments.setHasFixedSize(true);
        rv_comments.setAdapter(postCommentAdapter);
        rv_comments.setLayoutManager(linearLayoutManager);
        rv_comments.setItemAnimator(new DefaultItemAnimator());
    }


    public void setCommentValue() {
        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.setCommentCount(String.valueOf(comment_added));
        }
    }
}
