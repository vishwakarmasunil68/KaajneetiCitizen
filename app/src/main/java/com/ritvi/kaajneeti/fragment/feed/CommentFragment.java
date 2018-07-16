package com.ritvi.kaajneeti.fragment.feed;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.adapter.PostCommentAdapter;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentContants;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.post.PostCommentPOJO;
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

public class CommentFragment extends FragmentController {

    @BindView(R.id.rv_comments)
    RecyclerView rv_comments;
    @BindView(R.id.et_comment)
    EditText et_comment;
    @BindView(R.id.iv_send_comment)
    ImageView iv_send_comment;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    String post_id = "";
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

        post_id = getArguments().getString("post_id");
        attachAdapter();

        getAllPostComments();
        iv_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComment();
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void saveComment() {
        if (et_comment.getText().toString().length() > 0) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
            nameValuePairs.add(new BasicNameValuePair("post_id", post_id));
            nameValuePairs.add(new BasicNameValuePair("your_comment", et_comment.getText().toString()));

            new WebServiceBaseResponse<PostCommentPOJO>(nameValuePairs, getActivity(), new ResponseCallBack<PostCommentPOJO>() {
                @Override
                public void onGetMsg(ResponsePOJO<PostCommentPOJO> responsePOJO) {
                    try {
                        if (responsePOJO.isSuccess()) {
                            postCommentPOJOS.add(responsePOJO.getResult());
                            comment_added++;
                            et_comment.setText("");
                            postCommentAdapter.notifyDataSetChanged();
                            setCommentValue();
                        } else {
                            ToastClass.showShortToast(getActivity(), responsePOJO.getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, PostCommentPOJO.class, "SAVE_COMMENT", false).execute(WebServicesUrls.POST_COMMENTS);
        }
    }

    public void getAllPostComments() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("post_id", post_id));
        new WebServiceBaseResponseList<PostCommentPOJO>(nameValuePairs, getActivity(), new ResponseListCallback<PostCommentPOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO<PostCommentPOJO> responseListPOJO) {
                postCommentPOJOS.clear();
                try {
                    Collections.reverse(responseListPOJO.getResultList());
                    comment_added = responseListPOJO.getResultList().size();
                    postCommentPOJOS.addAll(responseListPOJO.getResultList());
                    setCommentValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postCommentAdapter.notifyDataSetChanged();
            }
        }, PostCommentPOJO.class, "post_comments", true).execute(WebServicesUrls.GET_ALL_POST_COMMENTS);
    }

    PostCommentAdapter postCommentAdapter;
    List<PostCommentPOJO> postCommentPOJOS = new ArrayList<>();

    public void attachAdapter() {
        postCommentAdapter = new PostCommentAdapter(getActivity(), this, postCommentPOJOS);
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

//    @Override
//    public void onBackPressed() {
//        Log.d(TagUtils.getTag(), "on back pressed");
////        if (getActivity() instanceof HomeActivity) {
////            HomeActivity homeActivity = (HomeActivity) getActivity();
////            Bundle bundle = new Bundle();
////            bundle.putInt("total_comments_added", comment_added);
////            activityManager.popBackResultFragment(startingFragment, requestCode, FragmentContants.RESULT_OK, bundle);
////            homeActivity.onActivityResult(Constants.FRAG_FEED_COMMENT, Activity.RESULT_OK, null);
////        }
////        activityManager.popFragment();
////        Bundle bundle = new Bundle();
////        bundle.putInt("total_comments_added", comment_added);
////        activityManager.popBackResultFragment(startingFragment, requestCode, FragmentContants.RESULT_OK, bundle);
//    }
}
