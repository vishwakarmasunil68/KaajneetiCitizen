package com.ritvi.kaajneeti.fragment.poll;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.SetViews;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.adapter.PollFeedAnalyzeAnsAdapter;
import com.ritvi.kaajneeti.adapter.PollFeedAnsAdapter;
import com.ritvi.kaajneeti.fragment.home.AllFeedsFragment;
import com.ritvi.kaajneeti.fragment.user.UserProfileFragment;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.interfaces.PollAnsClickInterface;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.poll.PollAnsPOJO;
import com.ritvi.kaajneeti.pojo.poll.PollPOJO;
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

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PollPreviewFragment extends FragmentController{

    @BindView(R.id.tv_questions)
    public TextView tv_questions;
    @BindView(R.id.tv_profile_name)
    public TextView tv_profile_name;
    @BindView(R.id.tv_date)
    public TextView tv_date;
    @BindView(R.id.tv_participated)
    public TextView tv_participated;
    @BindView(R.id.cv_profile_pic)
    public CircleImageView cv_profile_pic;
    @BindView(R.id.iv_poll_image)
    public ImageView iv_poll_image;
    @BindView(R.id.iv_poll_menu)
    public ImageView iv_poll_menu;
    @BindView(R.id.ll_already_participated)
    public LinearLayout ll_already_participated;
    @BindView(R.id.rv_ans)
    public RecyclerView rv_ans;
    @BindView(R.id.ll_like)
    public LinearLayout ll_like;
    @BindView(R.id.iv_like)
    public ImageView iv_like;
    @BindView(R.id.tv_like)
    public TextView tv_like;
    @BindView(R.id.tv_comments)
    public TextView tv_comments;
    @BindView(R.id.ll_comment)
    public LinearLayout ll_comment;
    @BindView(R.id.tv_revote)
    public TextView tv_revote;
    @BindView(R.id.tv_poll_ends_in)
    public TextView tv_poll_ends_in;
    @BindView(R.id.tv_total_votes)
    public TextView tv_total_votes;

    PollPOJO pollPOJO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_poll_view,container,false);
        setUpView(getActivity(),this,view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments()!=null){
            pollPOJO= (PollPOJO) getArguments().getSerializable("pollPOJO");
        }

        getPollDetails();

        if(pollPOJO!=null){
            setPollData(pollPOJO);
        }

    }

    public void getPollDetails(){
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id",Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("poll_id",pollPOJO.getPollId()));

        new WebServiceBaseResponse<PollPOJO>(nameValuePairs, getActivity(), new ResponseCallBack<PollPOJO>() {
            @Override
            public void onGetMsg(ResponsePOJO<PollPOJO> responsePOJO) {
                try{
                    if(responsePOJO.isSuccess()){
                        setPollData(responsePOJO.getResult());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },PollPOJO.class,"GET_POLL_DETAILS",true).execute(WebServicesUrls.GET_POLL_DETAIL);

    }

    public void setPollData(final PollPOJO pollPOJO){

        if (pollPOJO.getPollQuestion().length() > 0) {
            tv_questions.setText(pollPOJO.getPollQuestion());
        } else {
            tv_questions.setVisibility(View.GONE);
        }

        if (pollPOJO.getPollImage().length() > 0) {
            iv_poll_image.setVisibility(View.VISIBLE);
            Glide.with(getActivity().getApplicationContext())
                    .load(pollPOJO.getPollImage())
                    .placeholder(R.drawable.ic_default_pic)
                    .error(R.drawable.ic_default_pic)
                    .dontAnimate()
                    .into(iv_poll_image);
        } else {
            iv_poll_image.setVisibility(View.GONE);
        }

        final UserProfilePOJO userProfilePOJO = pollPOJO.getProfileDetailPOJO();
        SetViews.setProfilePhoto(getActivity().getApplicationContext(), userProfilePOJO.getProfilePhotoPath(), cv_profile_pic);

        String name = "";

        name = "<b>" + userProfilePOJO.getFirstName() + " " + userProfilePOJO.getLastName() + "</b>";

        tv_profile_name.setText(Html.fromHtml(name));
        tv_date.setText(pollPOJO.getAddedOn());

        cv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    Bundle bundle = new Bundle();
                    bundle.putString("user_id", userProfilePOJO.getUserId());
                    bundle.putString("profile_id", userProfilePOJO.getUserProfileId());
                    UserProfileFragment userProfileFragment = new UserProfileFragment();
                    userProfileFragment.setArguments(bundle);
                    homeActivity.replaceFragmentinFrameHome(userProfileFragment, userProfileFragment.getClass().getSimpleName());
                }
            }
        });
        tv_profile_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cv_profile_pic.callOnClick();
            }
        });

        boolean is_Ans_Image = false;
        for (PollAnsPOJO pollAnsPOJO : pollPOJO.getPollAnsPOJOS()) {
            if (pollAnsPOJO.getPollAnswerImage().length() > 0) {
                is_Ans_Image = true;
            }
        }
        PollFeedAnalyzeAnsAdapter pollFeedAnsAdapter = new PollFeedAnalyzeAnsAdapter(getActivity(), null, pollPOJO.getPollAnsPOJOS(),pollPOJO);
        if (is_Ans_Image) {
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
            rv_ans.setLayoutManager(layoutManager);
            rv_ans.setHasFixedSize(true);
            rv_ans.setAdapter(pollFeedAnsAdapter);
            rv_ans.setItemAnimator(new DefaultItemAnimator());
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            rv_ans.setLayoutManager(layoutManager);
            rv_ans.setHasFixedSize(true);
            rv_ans.setAdapter(pollFeedAnsAdapter);
            rv_ans.setItemAnimator(new DefaultItemAnimator());
        }
//
        pollFeedAnsAdapter.setOnAnsClicked(new PollAnsClickInterface() {
            @Override
            public void onAnsclicked(String ans_id) {
                Log.d(TagUtils.getTag(), "poll ans clicked:-" + ans_id);
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                nameValuePairs.add(new BasicNameValuePair("poll_id", pollPOJO.getPollId()));
                nameValuePairs.add(new BasicNameValuePair("answer_id", ans_id));
                new WebServiceBase(nameValuePairs, getActivity(), new WebServicesCallBack() {
                    @Override
                    public void onGetMsg(String apicall, String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                                pollPOJO.setMeParticipated(1);

                                ToastClass.showShortToast(getActivity().getApplicationContext(), "Thanks for you participation");
                            } else {
                                ToastClass.showShortToast(getActivity().getApplicationContext(), jsonObject.optString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, "POLL_ANSWERED", true).execute(WebServicesUrls.SAVE_POLL_ANS);
            }
        });

        if (pollPOJO.getMeParticipated() != null) {
            if (pollPOJO.getMeParticipated() == 1) {
//                rv_ans.setVisibility(View.GONE);
                ll_already_participated.setVisibility(View.VISIBLE);
            } else {
//                rv_ans.setVisibility(View.VISIBLE);
                ll_already_participated.setVisibility(View.GONE);
            }
        }

        tv_poll_ends_in.setText("This poll ends in "+String.valueOf(UtilityFunction.getdateDifference(pollPOJO.getValidFromDate(),pollPOJO.getValidEndDate())+" days"));
        tv_total_votes.setText( pollPOJO.getPollTotalParticipation()+" votes");

        iv_poll_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu menu = new PopupMenu(getActivity(), view);

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuitem) {
                        switch (menuitem.getItemId()) {
                            case R.id.popup_edit:

                                break;
                            case R.id.popup_delete:
                                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                                nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                                nameValuePairs.add(new BasicNameValuePair("poll_id", pollPOJO.getPollId()));
                                new WebServiceBase(nameValuePairs, getActivity(), new WebServicesCallBack() {
                                    @Override
                                    public void onGetMsg(String apicall, String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                                            }
                                            ToastClass.showShortToast(getActivity().getApplicationContext(), jsonObject.optString("message"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, "DELETE_POLL", true).execute(WebServicesUrls.DELETE_POLL);
                                break;
                        }
                        return false;
                    }
                });
                if (pollPOJO.getProfileDetailPOJO().getUserProfileId().equalsIgnoreCase(Constants.userProfilePOJO.getUserProfileId())) {
                    menu.inflate(R.menu.menu_my_poll);
                } else {
                    menu.inflate(R.menu.menu_other_poll);
                }
                menu.show();
            }
        });

        if (pollPOJO.getMeLike() == 0) {
            iv_like.setImageResource(R.drawable.ic_unlike);
        } else if (pollPOJO.getMeLike() == 1) {
            iv_like.setImageResource(R.drawable.ic_like);
        }
//
        tv_like.setText(String.valueOf(pollPOJO.getTotalLikes()));
        tv_comments.setText(String.valueOf(pollPOJO.getTotalComment()));

//        ll_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (fragment instanceof AllFeedsFragment) {
//                    AllFeedsFragment allFeedsFragment = (AllFeedsFragment) fragment;
//                    allFeedsFragment.showPollComments(tv_comments, pollPOJO);
//                }
//            }
//        });

        ll_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (pollPOJO.getMeLike() == 0) {
                        iv_like.setImageResource(R.drawable.ic_like);
                    } else if (pollPOJO.getMeLike() == 1) {
                        iv_like.setImageResource(R.drawable.ic_unlike);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                nameValuePairs.add(new BasicNameValuePair("poll_id", pollPOJO.getPollId()));
                new WebServiceBase(nameValuePairs, getActivity(), new WebServicesCallBack() {
                    @Override
                    public void onGetMsg(String apicall, String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("status").equalsIgnoreCase("success")
                                    && jsonObject.optString("result").equalsIgnoreCase("1")) {
                                //post liked
                                iv_like.setImageResource(R.drawable.ic_like);
                            } else if (jsonObject.optString("result").equalsIgnoreCase("0")) {
                                //post unliked
                                iv_like.setImageResource(R.drawable.ic_unlike);
                            }
                            tv_like.setText(jsonObject.optString("result"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, "CALL_LIKE_API", false).execute(WebServicesUrls.LIKE_UNLIKE_POLL);
            }
        });
    }
}
