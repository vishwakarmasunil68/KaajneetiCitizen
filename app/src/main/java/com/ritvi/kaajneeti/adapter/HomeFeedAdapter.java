package com.ritvi.kaajneeti.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.SetViews;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.activity.express.ExpressActivity;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.fragment.AttachmentViewPagerFragment;
import com.ritvi.kaajneeti.fragment.ComplaintDetailFragment;
import com.ritvi.kaajneeti.fragment.event.EventPreviewFragment;
import com.ritvi.kaajneeti.fragment.poll.PollPreviewFragment;
import com.ritvi.kaajneeti.fragment.post.PostViewFragment;
import com.ritvi.kaajneeti.fragment.suggestion.SuggestionDetailFragment;
import com.ritvi.kaajneeti.fragment.user.UserProfileFragment;
import com.ritvi.kaajneeti.interfaces.OnLoadMoreListener;
import com.ritvi.kaajneeti.interfaces.PollAnsClickInterface;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;
import com.ritvi.kaajneeti.pojo.attachments.AttachmentPOJO;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintAttachmentPOJO;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintPOJO;
import com.ritvi.kaajneeti.pojo.event.EventPOJO;
import com.ritvi.kaajneeti.pojo.information.InformationAttachmentPOJO;
import com.ritvi.kaajneeti.pojo.information.InformationPOJO;
import com.ritvi.kaajneeti.pojo.poll.PollAnsPOJO;
import com.ritvi.kaajneeti.pojo.poll.PollPOJO;
import com.ritvi.kaajneeti.pojo.post.PostAttachmentPOJO;
import com.ritvi.kaajneeti.pojo.post.PostPOJO;
import com.ritvi.kaajneeti.pojo.suggestion.SuggestionAttachmentPOJO;
import com.ritvi.kaajneeti.pojo.suggestion.SuggestionPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.view.SwipeRevealLayout;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sunil on 03-11-2017.
 */

public class HomeFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FeedPOJO> items;
    Activity activity;
    Fragment fragment;
    FragmentManager fragmentManager;
    RecyclerView recyclerView;

    private boolean isLoading;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;

    public HomeFeedAdapter(RecyclerView recyclerView, Activity activity, Fragment fragment, List<FeedPOJO> items, FragmentManager fragmentManager) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
        this.fragmentManager = fragmentManager;
        this.recyclerView = recyclerView;
        setHasStableIds(true);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (items.get(position).getFeedtype() != null) {
                switch (items.get(position).getFeedtype()) {
                    case "poll":
                        return 0;
                    case "event":
                        return 1;
                    case "post":
                        return 2;
                    case "complaint":
                        return 3;
                    case "profile":
                        return 4;
                    case "suggestion":
                        return 5;
                    case "information":
                        return 6;
                    case "follower":
                        return 7;
                    case "following":
                        return 8;
                }
                return super.getItemViewType(position);
            } else {
                return 101;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case 0:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_poll_feed, parent, false);
                return new PollViewHolder(v);
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_event_feed, parent, false);
                return new EventViewHolder(v);
            case 2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_news_feeds, parent, false);
                return new PostViewHolder(v);
            case 3:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_complaint_feed, parent, false);
                return new ComplaintViewHolder(v);
            case 4:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_search_user_profile, parent, false);
                return new UserProfileViewHolder(v);
            case 5:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_suggestion_feed, parent, false);
                return new SuggestionViewHolder(v);
            case 6:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_information_feed, parent, false);
                return new InformationViewHolder(v);
            case 7:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_search_user_profile, parent, false);
                return new UserProfileViewHolder(v);
            case 8:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_search_user_profile, parent, false);
                return new UserProfileViewHolder(v);
            case 101:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_item_loading, parent, false);
                return new LoadingViewHolder(v);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (items.get(position).getFeedtype() == null) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        } else {
            switch (items.get(position).getFeedtype()) {
                case "poll":
                    PollViewHolder pollViewHolder = (PollViewHolder) holder;
                    inflatePollData(pollViewHolder, items.get(position).getPollPOJO(), position);
                    break;
                case "event":
                    EventViewHolder eventViewHolder = (EventViewHolder) holder;
                    inflateEventData(eventViewHolder, items.get(position).getEventPOJO(), position);
                    break;
                case "post":
                    PostViewHolder postViewHolder = (PostViewHolder) holder;
                    inflatePostData(postViewHolder, items.get(position).getPostPOJO(), position);
                    break;
                case "complaint":
                    ComplaintViewHolder complaintViewHolder = (ComplaintViewHolder) holder;
                    inflateComplaintData(complaintViewHolder, items.get(position).getComplaintPOJO(), position);
                    break;
                case "profile":
                    UserProfileViewHolder userProfileViewHolder = (UserProfileViewHolder) holder;
                    inflateUserData(userProfileViewHolder, items.get(position).getProfiledata(), position);
                    break;
                case "follower":
                    UserProfileViewHolder followerUserProfileViewHolder = (UserProfileViewHolder) holder;
                    inflateUserData(followerUserProfileViewHolder, items.get(position).getFollowerdataUserProfilePOJO(), position);
                    break;
                case "following":
                    UserProfileViewHolder followingUserProfileViewHolder = (UserProfileViewHolder) holder;
                    inflateUserData(followingUserProfileViewHolder, items.get(position).getFollowingdataUserProfilePOJO(), position);
                    break;
                case "suggestion":
                    SuggestionViewHolder suggestionViewHolder = (SuggestionViewHolder) holder;
                    inflateSuggestionData(suggestionViewHolder, items.get(position).getSuggestiondata(), position);
                    break;
                case "information":
                    InformationViewHolder informationViewHolder = (InformationViewHolder) holder;
                    inflateInformationData(informationViewHolder, items.get(position).getInformationPOJO(), position);
                    break;
            }
        }

        holder.itemView.setTag(items.get(position));

    }

    public void inflateUserData(final UserProfileViewHolder userProfileViewHolder, final UserProfilePOJO userProfilePOJO, final int position) {
        if (userProfilePOJO != null) {
            userProfileViewHolder.tv_user_name.setText(userProfilePOJO.getFirstName() + " " + userProfilePOJO.getLastName());
            Glide.with(activity.getApplicationContext())
                    .load(userProfilePOJO.getProfilePhotoPath())
                    .placeholder(R.drawable.ic_default_profile_pic)
                    .error(R.drawable.ic_default_profile_pic)
                    .dontAnimate()
                    .into(userProfileViewHolder.cv_profile_pic);
            userProfileViewHolder.swipeRevealLayout.lockDrag(true);
            userProfileViewHolder.btn_accept.setVisibility(View.GONE);
            switch (userProfilePOJO.getMyFriend()) {
                case 0:
                    userProfileViewHolder.btn_accept.setText("+ Add");
                    userProfileViewHolder.btn_accept.setVisibility(View.VISIBLE);
                    userProfileViewHolder.swipeRevealLayout.lockDrag(true);
                    break;
                case 1:
                    userProfileViewHolder.btn_accept.setVisibility(View.GONE);
                    userProfileViewHolder.swipeRevealLayout.lockDrag(false);
                    break;
                case 2:
                    userProfileViewHolder.btn_accept.setVisibility(View.VISIBLE);
                    userProfileViewHolder.btn_accept.setText("Accept");
                    userProfileViewHolder.swipeRevealLayout.lockDrag(false);
                    break;
                case 3:
                    userProfileViewHolder.btn_accept.setVisibility(View.GONE);
                    userProfileViewHolder.swipeRevealLayout.lockDrag(false);
                    break;
                case 4:
                    userProfileViewHolder.btn_accept.setText("Follow");
                    userProfileViewHolder.btn_accept.setVisibility(View.VISIBLE);
                    break;
            }

            userProfileViewHolder.btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (userProfilePOJO.getMyFriend()) {
                        case 0:
                            sendFriendRequest(userProfilePOJO);
                            break;
                        case 2:
                            acceptRequest(userProfilePOJO);
                            break;
                        case 4:
                            followUser(userProfilePOJO);
                            userProfileViewHolder.btn_accept.setVisibility(View.GONE);
                            break;
                    }
                    userProfileViewHolder.swipeRevealLayout.close(true);
                }
            });

            userProfileViewHolder.ll_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (userProfilePOJO.getMyFriend()) {
                        case 1:
                            undoFriendRequest(userProfilePOJO);
                            break;
                        case 2:
                            cancelFriendRequest(userProfilePOJO);
                            break;
                        case 3:
                            sendFriendRequest(userProfilePOJO);
                            break;
                    }
                    userProfileViewHolder.swipeRevealLayout.close(true);
                }
            });

            userProfileViewHolder.tv_email.setText(userProfilePOJO.getEmail());

            userProfileViewHolder.ll_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (activity instanceof HomeActivity) {
                        HomeActivity homeActivity = (HomeActivity) activity;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user_profile_id", userProfilePOJO.getUserProfileId());
                        UserProfileFragment userProfileFragment = new UserProfileFragment();
                        userProfileFragment.setArguments(bundle);
                        homeActivity.replaceFragmentinFrameHome(userProfileFragment, userProfileFragment.getClass().getSimpleName());
                    }
                }
            });

            if (userProfilePOJO.getUserTypeId().equalsIgnoreCase("1")) {
                userProfileViewHolder.iv_crown.setVisibility(View.GONE);
            } else {
                userProfileViewHolder.iv_crown.setVisibility(View.VISIBLE);
            }
        }
    }

    public void followUser(final UserProfilePOJO userProfilePOJO) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("friend_user_profile_id", userProfilePOJO.getUserProfileId()));
        new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                        userProfilePOJO.setFollowing(jsonObject.optInt("follow"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "FOLLOW_UNFOLLOW", true).execute(WebServicesUrls.FOLLOW_UNFOLLOW_PEOPLE);
    }


    public void sendFriendRequest(final UserProfilePOJO userProfilePOJO) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("friend_user_profile_id", userProfilePOJO.getUserProfileId()));
        new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                        userProfilePOJO.setMyFriend(jsonObject.optInt("friend"));
                        notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "CALL_ADD_FRIEND_API", true).execute(WebServicesUrls.SEND_FRIEND_REQUEST);
    }

    public void undoFriendRequest(final UserProfilePOJO userProfilePOJO) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("friend_user_profile_id", userProfilePOJO.getUserProfileId()));
        new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
//                userProfilePOJO.setMyFriend(0);
//                notifyDataSetChanged();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                        userProfilePOJO.setMyFriend(jsonObject.optInt("friend"));
                        notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "CALL_ADD_FRIEND_API", true).execute(WebServicesUrls.UNDO_FRIEND_REQUEST);
    }

    public void cancelFriendRequest(final UserProfilePOJO userProfilePOJO) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("friend_user_profile_id", userProfilePOJO.getUserProfileId()));
        new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
//                userProfilePOJO.setMyFriend(0);
//                notifyDataSetChanged();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                        userProfilePOJO.setMyFriend(jsonObject.optInt("friend"));
                        notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "CALL_ADD_FRIEND_API", true).execute(WebServicesUrls.CANCEL_FRIEND_REQUEST);
    }

    public void acceptRequest(final UserProfilePOJO userProfilePOJO) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("friend_user_profile_id", userProfilePOJO.getUserProfileId()));
        new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
//                userProfilePOJO.setMyFriend(3);
//                notifyDataSetChanged();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                        userProfilePOJO.setMyFriend(jsonObject.optInt("friend"));
                        notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "CALL_ADD_FRIEND_API", true).execute(WebServicesUrls.SEND_FRIEND_REQUEST);
    }


    public void inflatePollData(final PollViewHolder pollViewHolder, final PollPOJO pollPOJO, final int position) {

        if (pollPOJO.getPollQuestion().length() > 0) {
            pollViewHolder.tv_questions.setText(pollPOJO.getPollQuestion());
        } else {
            pollViewHolder.tv_questions.setVisibility(View.GONE);
        }

        if (pollPOJO.getPollImage().length() > 0) {
            pollViewHolder.iv_poll_image.setVisibility(View.VISIBLE);
            Glide.with(activity.getApplicationContext())
                    .load(pollPOJO.getPollImage())
                    .placeholder(R.drawable.ic_default_pic)
                    .error(R.drawable.ic_default_pic)
                    .dontAnimate()
                    .into(pollViewHolder.iv_poll_image);
        } else {
            pollViewHolder.iv_poll_image.setVisibility(View.GONE);
        }

        final UserProfilePOJO userProfilePOJO = pollPOJO.getProfileDetailPOJO();
        SetViews.setProfilePhoto(activity.getApplicationContext(), userProfilePOJO.getProfilePhotoPath(), pollViewHolder.cv_profile_pic);

        String name = "";

        name = "<b>" + userProfilePOJO.getFirstName() + " " + userProfilePOJO.getLastName() + "</b>";

        pollViewHolder.tv_profile_name.setText(Html.fromHtml(name));
        pollViewHolder.tv_date.setText(pollPOJO.getAddedOn());

        pollViewHolder.cv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) activity;
                    Bundle bundle = new Bundle();
                    bundle.putString("user_id", userProfilePOJO.getUserId());
                    bundle.putString("profile_id", userProfilePOJO.getUserProfileId());
                    UserProfileFragment userProfileFragment = new UserProfileFragment();
                    userProfileFragment.setArguments(bundle);
                    homeActivity.replaceFragmentinFrameHome(userProfileFragment, userProfileFragment.getClass().getSimpleName());
                }
            }
        });
        pollViewHolder.tv_profile_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pollViewHolder.cv_profile_pic.callOnClick();
            }
        });

        boolean is_Ans_Image = false;
        for (PollAnsPOJO pollAnsPOJO : pollPOJO.getPollAnsPOJOS()) {
            if (pollAnsPOJO.getPollAnswerImage().length() > 0) {
                is_Ans_Image = true;
            }
        }
        final PollFeedAnsAdapter pollFeedAnsAdapter = new PollFeedAnsAdapter(activity, null, pollPOJO.getPollAnsPOJOS(), pollPOJO);
        if (is_Ans_Image) {
            GridLayoutManager layoutManager = new GridLayoutManager(activity, 2);
            pollViewHolder.rv_ans.setLayoutManager(layoutManager);
            pollViewHolder.rv_ans.setHasFixedSize(true);
            pollViewHolder.rv_ans.setAdapter(pollFeedAnsAdapter);
            pollViewHolder.rv_ans.setItemAnimator(new DefaultItemAnimator());
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            pollViewHolder.rv_ans.setLayoutManager(layoutManager);
            pollViewHolder.rv_ans.setHasFixedSize(true);
            pollViewHolder.rv_ans.setAdapter(pollFeedAnsAdapter);
            pollViewHolder.rv_ans.setItemAnimator(new DefaultItemAnimator());
        }
//
        pollFeedAnsAdapter.setOnAnsClicked(new PollAnsClickInterface() {
            @Override
            public void onAnsclicked(PollPOJO pollPOJO) {
                items.get(position).setPollPOJO(pollPOJO);
                notifyDataSetChanged();
            }
        });

        if (pollPOJO.getMeParticipated() != null) {
            if (pollPOJO.getMeParticipated() == 1) {
//                pollViewHolder.rv_ans.setVisibility(View.GONE);
                pollViewHolder.ll_already_participated.setVisibility(View.VISIBLE);
            } else {
//                pollViewHolder.rv_ans.setVisibility(View.VISIBLE);
                pollViewHolder.ll_already_participated.setVisibility(View.GONE);
            }
        }

        pollViewHolder.ll_poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("poll_id", pollPOJO.getPollId());

                PollPreviewFragment pollPreviewFragment = new PollPreviewFragment();
                pollPreviewFragment.setArguments(bundle);

                if (activity instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) activity;
                    homeActivity.startFragment(R.id.frame_home, pollPreviewFragment);
                }
            }
        });

        pollViewHolder.tv_poll_ends_in.setText("This poll ends in " + String.valueOf(UtilityFunction.getdateDifference(pollPOJO.getValidFromDate(), pollPOJO.getValidEndDate()) + " days"));
        pollViewHolder.tv_total_votes.setText(pollPOJO.getPollTotalParticipation() + " votes");

        pollViewHolder.iv_poll_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu menu = new PopupMenu(activity, view);

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuitem) {
                        switch (menuitem.getItemId()) {
                            case R.id.popup_edit:
                                Intent intent = new Intent(activity, ExpressActivity.class);
                                intent.putExtra("pollPOJO", pollPOJO);
                                activity.startActivity(intent);
                                break;
                            case R.id.popup_delete:
                                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                                nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                                nameValuePairs.add(new BasicNameValuePair("poll_id", pollPOJO.getPollId()));
                                new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
                                    @Override
                                    public void onGetMsg(String apicall, String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                                                items.remove(position);
                                                notifyDataSetChanged();
                                            }
                                            ToastClass.showShortToast(activity.getApplicationContext(), jsonObject.optString("message"));
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
            pollViewHolder.iv_like.setImageResource(R.drawable.ic_unlike);
        } else if (pollPOJO.getMeLike() == 1) {
            pollViewHolder.iv_like.setImageResource(R.drawable.ic_like);
        }
//
        pollViewHolder.tv_like.setText(String.valueOf(pollPOJO.getTotalLikes()));
        pollViewHolder.tv_comments.setText(String.valueOf(pollPOJO.getTotalComment()));

        pollViewHolder.ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) activity;
                    homeActivity.showPollComments(pollViewHolder.tv_comments, pollPOJO);
                }
            }
        });

        pollViewHolder.ll_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (pollPOJO.getMeLike() == 0) {
                        pollViewHolder.iv_like.setImageResource(R.drawable.ic_like);
                    } else if (pollPOJO.getMeLike() == 1) {
                        pollViewHolder.iv_like.setImageResource(R.drawable.ic_unlike);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                nameValuePairs.add(new BasicNameValuePair("poll_id", pollPOJO.getPollId()));
                new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
                    @Override
                    public void onGetMsg(String apicall, String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("status").equalsIgnoreCase("success")
                                    && jsonObject.optString("result").equalsIgnoreCase("1")) {
                                //post liked
                                pollViewHolder.iv_like.setImageResource(R.drawable.ic_like);
                            } else if (jsonObject.optString("result").equalsIgnoreCase("0")) {
                                //post unliked
                                pollViewHolder.iv_like.setImageResource(R.drawable.ic_unlike);
                            }
                            pollViewHolder.tv_like.setText(jsonObject.optString("result"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, "CALL_LIKE_API", false).execute(WebServicesUrls.LIKE_UNLIKE_POLL);
            }
        });


    }

    public void inflateEventData(final EventViewHolder eventViewHolder, final EventPOJO eventPOJO, final int position) {

        eventViewHolder.tv_name.setText(eventPOJO.getEventName());

        Glide.with(activity.getApplicationContext())
                .load(eventPOJO.getEventProfile().getProfilePhotoPath())
                .error(R.drawable.ic_default_profile_pic)
                .placeholder(R.drawable.ic_default_profile_pic)
                .dontAnimate()
                .into(eventViewHolder.cv_profile_pic);
        Log.d(TagUtils.getTag(), "event cover photo:-" + eventPOJO.getEventCoverPhoto());
//        if (eventPOJO.getEventAttacehment().size() > 0) {
        Glide.with(activity.getApplicationContext())
                .load(eventPOJO.getEventCoverPhoto())
                .error(R.drawable.ic_default_pic)
                .placeholder(R.drawable.ic_default_pic)
                .dontAnimate()
                .into(eventViewHolder.iv_event_image);
//        }

        String name = "";
        final UserProfilePOJO userProfilePOJO = eventPOJO.getEventProfile();

        eventViewHolder.cv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activity instanceof HomeActivity) {

                    HomeActivity homeActivity = (HomeActivity) activity;

                    UserProfileFragment userProfileFragment = new UserProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user_profile_id", userProfilePOJO.getUserProfileId());
                    userProfileFragment.setArguments(bundle);

                    homeActivity.startFragment(R.id.frame_home, userProfileFragment);
                }
            }
        });

        name = "<b>" + userProfilePOJO.getFirstName() + " " + userProfilePOJO.getLastName() + "</b> created an <b>event</b>";

        eventViewHolder.ll_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity instanceof HomeActivity) {

                    Bundle bundle = new Bundle();
                    bundle.putString("event_id", eventPOJO.getEventId());

                    EventPreviewFragment eventPreviewFragment = new EventPreviewFragment();
                    eventPreviewFragment.setArguments(bundle);

                    HomeActivity homeActivity = (HomeActivity) activity;
                    homeActivity.startFragment(R.id.frame_home, eventPreviewFragment);
                }
            }
        });

        eventViewHolder.cv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (activity instanceof HomeActivity) {
//                    HomeActivity homeActivity = (HomeActivity) activity;
//                    homeActivity.showUserProfileFragment(eventPOJO.getEventProfile().getUserId(), eventPOJO.getEventProfile().getUserProfileId());
//                }
            }
        });
        eventViewHolder.tv_profile_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventViewHolder.cv_profile_pic.callOnClick();
            }
        });

        try {
            String startDate = UtilityFunction.getServerConvertedFullDate(eventPOJO.getStartDate().split(" ")[0]);
            String endDate = UtilityFunction.getServerConvertedFullDate(eventPOJO.getEndDate().split(" ")[0]);

            eventViewHolder.tv_event_date.setText(startDate + "  to " + endDate);

            String[] dateValues = UtilityFunction.getDateValues(eventPOJO.getStartDate().split(" ")[0]);

        } catch (Exception e) {
            e.printStackTrace();
            eventViewHolder.tv_event_date.setText(eventPOJO.getStartDate() + "  to " + eventPOJO.getEndDate());
        }
        eventViewHolder.tv_place.setText(eventPOJO.getEventLocation());
//        eventViewHolder.tv_month.setText(eventPOJO.getEveryMonth());

        eventViewHolder.tv_profile_name.setText(Html.fromHtml(name));
        eventViewHolder.tv_date.setText(eventPOJO.getAddedOn());

        if (!eventPOJO.getEventDescription().equalsIgnoreCase("")) {
            eventViewHolder.tv_description.setText(eventPOJO.getEventDescription());
        }

        eventViewHolder.iv_event_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu menu = new PopupMenu(activity, view);

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuitem) {
                        return false;
                    }
                });
//                if (eventPOJO.getEventProfile().getUserProfileId().equalsIgnoreCase(Constants.userProfilePOJO.getUserProfileId())) {
//                    menu.inflate(R.menu.menu_my_feed);
//                } else {
//                    menu.inflate(R.menu.menu_friend_feed);
//                }
//                menu.show();
            }
        });

        if (eventPOJO.getEventProfile().getUserProfileId().equalsIgnoreCase(Constants.userProfilePOJO.getUserProfileId())) {
            eventViewHolder.frame_event_interest.setVisibility(View.GONE);
        } else {
            eventViewHolder.frame_event_interest.setVisibility(View.VISIBLE);
            switch (eventPOJO.getMeInterested()) {
                case 0:
                    eventViewHolder.spinner_interest.setSelection(0, false);
                    break;
                case 1:
                    eventViewHolder.spinner_interest.setSelection(1, false);
                    break;
                case 2:
                    eventViewHolder.spinner_interest.setSelection(2, false);
                    break;
                case 3:
                    eventViewHolder.spinner_interest.setSelection(3, false);
                    break;
            }
            eventViewHolder.spinner_interest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TagUtils.getTag(), "event spinner item select:-" + position);

                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                    nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                    nameValuePairs.add(new BasicNameValuePair("event_id", eventPOJO.getEventId()));
                    nameValuePairs.add(new BasicNameValuePair("interest_type", String.valueOf(eventViewHolder.spinner_interest.getSelectedItemPosition())));

                    new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
                        @Override
                        public void onGetMsg(String apicall, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.optString("status").equals("success")) {

                                }
                                ToastClass.showShortToast(activity.getApplicationContext(), jsonObject.optString("message"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, "UPDATE_EVENT_INTEREST", false).execute(WebServicesUrls.EVENT_INTEREST_UPDATE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


        eventViewHolder.iv_event_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu menu = new PopupMenu(activity, view);

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuitem) {
                        switch (menuitem.getItemId()) {
                            case R.id.popup_edit:
                                Intent intent = new Intent(activity, ExpressActivity.class);
                                intent.putExtra("eventPOJO", eventPOJO);
                                activity.startActivity(intent);
                                break;
                            case R.id.popup_delete:
                                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                                nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                                nameValuePairs.add(new BasicNameValuePair("event_id", eventPOJO.getEventId()));
                                new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
                                    @Override
                                    public void onGetMsg(String apicall, String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                                                items.remove(position);
                                                notifyDataSetChanged();
                                            }
                                            ToastClass.showShortToast(activity.getApplicationContext(), jsonObject.optString("message"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, "DELETE_POLL", true).execute(WebServicesUrls.DELETE_EVENT);
                                break;
                        }
                        return false;
                    }
                });
                if (eventPOJO.getEventProfile().getUserProfileId().equalsIgnoreCase(Constants.userProfilePOJO.getUserProfileId())) {
                    menu.inflate(R.menu.menu_my_poll);
                } else {
                    menu.inflate(R.menu.menu_other_poll);
                }
                menu.show();
            }
        });

        if (eventPOJO.getMeLike() == 0) {
            eventViewHolder.iv_like.setImageResource(R.drawable.ic_unlike);
        } else if (eventPOJO.getMeLike() == 1) {
            eventViewHolder.iv_like.setImageResource(R.drawable.ic_like);
        }
//
        eventViewHolder.tv_like.setText(String.valueOf(eventPOJO.getTotalLikes()));
        eventViewHolder.tv_comments.setText(String.valueOf(eventPOJO.getTotalComment()));

        eventViewHolder.ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) activity;
                    homeActivity.showEventComment(eventViewHolder.tv_comments, eventPOJO);
                }
            }
        });

        eventViewHolder.ll_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (eventPOJO.getMeLike() == 0) {
                        eventViewHolder.iv_like.setImageResource(R.drawable.ic_like);
                    } else if (eventPOJO.getMeLike() == 1) {
                        eventViewHolder.iv_like.setImageResource(R.drawable.ic_unlike);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                nameValuePairs.add(new BasicNameValuePair("event_id", eventPOJO.getEventId()));
                new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
                    @Override
                    public void onGetMsg(String apicall, String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("status").equalsIgnoreCase("success")
                                    && jsonObject.optString("result").equalsIgnoreCase("1")) {
                                //post liked
                                eventViewHolder.iv_like.setImageResource(R.drawable.ic_like);
                            } else if (jsonObject.optString("result").equalsIgnoreCase("0")) {
                                //post unliked
                                eventViewHolder.iv_like.setImageResource(R.drawable.ic_unlike);
                            }
                            eventViewHolder.tv_like.setText(jsonObject.optString("result"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, "CALL_LIKE_API", false).execute(WebServicesUrls.EVENT_LIKE_UNLIKE);
            }
        });


    }

    public void makeLinks(TextView textView, String[] links, ClickableSpan[] clickableSpans) {
        SpannableString spannableString = new SpannableString(textView.getText());
        for (int i = 0; i < links.length; i++) {
            ClickableSpan clickableSpan = clickableSpans[i];
            String link = links[i];

            int startIndexOfLink = textView.getText().toString().indexOf(link);
            spannableString.setSpan(clickableSpan, startIndexOfLink,
                    startIndexOfLink + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setHighlightColor(
                Color.TRANSPARENT); // prevent TextView change background when highlight
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    public ClickableSpan returnSpanClick(final UserProfilePOJO userProfilePOJO) {
        ClickableSpan profileClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(activity.getApplicationContext(), "Hi :- "+userProfilePOJO.getFirstName(), Toast.LENGTH_SHORT)
//                        .show();
                if (activity instanceof HomeActivity) {

                    HomeActivity homeActivity = (HomeActivity) activity;

                    UserProfileFragment userProfileFragment = new UserProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user_profile_id", userProfilePOJO.getUserProfileId());
                    userProfileFragment.setArguments(bundle);

                    homeActivity.startFragment(R.id.frame_home, userProfileFragment);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };

        return profileClickSpan;
    }

    public void inflatePostData(final PostViewHolder postViewHolder, final PostPOJO postPOJO, final int position) {

        try {
            UserProfilePOJO userProfilePOJO = postPOJO.getPostProfile();

            if (postPOJO.getPostAttachment().size() > 0) {

                if (postPOJO.getPostAttachment().size() == 1) {
                    postViewHolder.iv_feed_image.setVisibility(View.VISIBLE);
                    postViewHolder.ll_image_2.setVisibility(View.GONE);
                    postViewHolder.ll_image_3.setVisibility(View.GONE);
                    Glide.with(activity.getApplicationContext())
                            .load(postPOJO.getPostAttachment().get(0).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(postViewHolder.iv_feed_image);
                } else if (postPOJO.getPostAttachment().size() == 2) {
                    postViewHolder.iv_feed_image.setVisibility(View.GONE);
                    postViewHolder.ll_image_2.setVisibility(View.VISIBLE);
                    postViewHolder.ll_image_3.setVisibility(View.GONE);
                    Glide.with(activity.getApplicationContext())
                            .load(postPOJO.getPostAttachment().get(0).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(postViewHolder.iv_1);

                    Glide.with(activity.getApplicationContext())
                            .load(postPOJO.getPostAttachment().get(1).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(postViewHolder.iv_2);


                } else if (postPOJO.getPostAttachment().size() > 2) {
                    postViewHolder.iv_feed_image.setVisibility(View.GONE);
                    postViewHolder.ll_image_2.setVisibility(View.GONE);
                    postViewHolder.ll_image_3.setVisibility(View.VISIBLE);

                    Glide.with(activity.getApplicationContext())
                            .load(postPOJO.getPostAttachment().get(0).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(postViewHolder.iv_3);

                    Glide.with(activity.getApplicationContext())
                            .load(postPOJO.getPostAttachment().get(1).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(postViewHolder.iv_4);

                    Glide.with(activity.getApplicationContext())
                            .load(postPOJO.getPostAttachment().get(2).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(postViewHolder.iv_5);
                    if (postPOJO.getPostAttachment().size() > 3) {
                        postViewHolder.tv_more_img.setVisibility(View.VISIBLE);
                        postViewHolder.tv_more_img.setText("+" + (postPOJO.getPostAttachment().size() - 3));
                    }
                }

                postViewHolder.ll_images.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<AttachmentPOJO> attachmentPOJOS = new ArrayList<>();
                        for (PostAttachmentPOJO postAttachmentPOJO : postPOJO.getPostAttachment()) {
                            AttachmentPOJO attachmentPOJO = new AttachmentPOJO();
                            attachmentPOJO.setFile_name(postAttachmentPOJO.getAttachmentFile());
                            attachmentPOJO.setFile_path(postAttachmentPOJO.getAttachmentFile());
                            attachmentPOJO.setFile_type(postAttachmentPOJO.getAttachmentFile());
                            attachmentPOJO.setFeed_type("post");
                            attachmentPOJO.setDescription(postPOJO.getPostDescription());

                            attachmentPOJOS.add(attachmentPOJO);
                        }

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("attachments", (Serializable) attachmentPOJOS);

                        AttachmentViewPagerFragment attachmentViewPagerFragment = new AttachmentViewPagerFragment();
                        attachmentViewPagerFragment.setArguments(bundle);

                        if (activity instanceof HomeActivity) {
                            HomeActivity homeActivity = (HomeActivity) activity;
                            homeActivity.startFragment(R.id.frame_home, attachmentViewPagerFragment);
                        }

                    }
                });

            } else {
                postViewHolder.iv_feed_image.setVisibility(View.GONE);
                postViewHolder.ll_image_2.setVisibility(View.GONE);
                postViewHolder.ll_image_3.setVisibility(View.GONE);
            }

            List<String> span = new ArrayList<>();
            List<UserProfilePOJO> profilePOJOList = new ArrayList<>();

            String name = "";
            String profile_name = userProfilePOJO.getFirstName() + " " + userProfilePOJO.getLastName();
            span.add(profile_name);
            profilePOJOList.add(userProfilePOJO);
            name = "<b>" + profile_name + "</b>";

            String profile_description = "";
            boolean containDescribe = false;
            if (postPOJO.getPostTag().size() > 0) {
                containDescribe = true;
            }

            if (postPOJO.getFeelingDataPOJOS().size() > 0) {
                containDescribe = true;
            }

            if (postPOJO.getServerLocationPOJO() != null) {
                containDescribe = true;
            }

            if (containDescribe) {
                profile_description += " is ";

                if (postPOJO.getFeelingDataPOJOS().size() > 0) {
                    profile_description += "<b>feeling " + postPOJO.getFeelingDataPOJOS().get(0).getFeelingName() + "</b>";
                }

                if (postPOJO.getPostTag().size() > 0) {
                    profile_description += " with ";
                    if (postPOJO.getPostTag().size() > 2) {
                        String profile_tag = postPOJO.getPostTag().get(0).getFirstName() + " " + postPOJO.getPostTag().get(0).getLastName();
                        span.add(profile_tag);
                        profilePOJOList.add(postPOJO.getPostTag().get(0));
                        profile_description += "<b>" + profile_tag + " and " + (postPOJO.getPostTag().size() - 1) + " other" + "</b>";
                    } else if (postPOJO.getPostTag().size() == 2) {
                        String profile_tag_1 = postPOJO.getPostTag().get(0).getFirstName() + " " + postPOJO.getPostTag().get(0).getLastName();
                        String profile_tag_2 = postPOJO.getPostTag().get(1).getFirstName() + " " + postPOJO.getPostTag().get(1).getLastName();
                        profile_description += "<b>" + profile_tag_1 +
                                " and " + profile_tag_2 + "</b>";
                        span.add(profile_tag_1);
                        span.add(profile_tag_2);

                        profilePOJOList.add(postPOJO.getPostTag().get(0));
                        profilePOJOList.add(postPOJO.getPostTag().get(1));

                    } else {
                        String profile_tag = postPOJO.getPostTag().get(0).getFirstName() + " " + postPOJO.getPostTag().get(0).getLastName();
                        profile_description += "<b>" + profile_tag + "</b>";
                        span.add(profile_tag);
                        profilePOJOList.add(postPOJO.getPostTag().get(0));
                    }
                }

                if (postPOJO.getServerLocationPOJO() != null) {
                    profile_description += " - at <b>" + postPOJO.getServerLocationPOJO().getLocationAddress() + "</b>";
                }
            }

            Glide.with(activity.getApplicationContext())
                    .load(postPOJO.getPostProfile().getProfilePhotoPath())
                    .placeholder(R.drawable.ic_default_profile_pic)
                    .error(R.drawable.ic_default_profile_pic)
                    .dontAnimate()
                    .into(postViewHolder.cv_profile_pic);

            postViewHolder.tv_profile_name.setText(Html.fromHtml(name + " " + profile_description));
            if (span.size() > 0) {
                String[] tags = span.toArray(new String[span.size()]);
                ClickableSpan[] clickableSpans = new ClickableSpan[profilePOJOList.size()];

                for (int i = 0; i < profilePOJOList.size(); i++) {
                    clickableSpans[i] = returnSpanClick(profilePOJOList.get(i));
                }

                makeLinks(postViewHolder.tv_profile_name, tags, clickableSpans);
            }


            if (!postPOJO.getPostDescription().equalsIgnoreCase("")) {
                postViewHolder.tv_description.setText(postPOJO.getPostDescription());
            } else {
                postViewHolder.tv_description.setVisibility(View.GONE);
            }
            postViewHolder.tv_date.setText(postPOJO.getAddedOn());

            postViewHolder.ll_news_feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (activity instanceof HomeActivity) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("post_id", postPOJO.getPostId());
                        PostViewFragment postViewFragment = new PostViewFragment();
                        postViewFragment.setArguments(bundle);
                        HomeActivity homeActivity = (HomeActivity) activity;
                        homeActivity.startFragment(R.id.frame_home, postViewFragment);
                    }
                }
            });

            postViewHolder.tv_profile_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postViewHolder.cv_profile_pic.callOnClick();
                }
            });

            postViewHolder.iv_post_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final PopupMenu menu = new PopupMenu(activity, view);

                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuitem) {
                            switch (menuitem.getItemId()) {
                                case R.id.popup_edit:
                                    Intent intent = new Intent(activity, ExpressActivity.class);
                                    intent.putExtra("post", postPOJO);
                                    activity.startActivity(intent);
                                    break;
                                case R.id.popup_delete:
                                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                                    nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                                    nameValuePairs.add(new BasicNameValuePair("post_id", postPOJO.getPostId()));
                                    new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
                                        @Override
                                        public void onGetMsg(String apicall, String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                                                    items.remove(position);
                                                    notifyDataSetChanged();
                                                }
                                                ToastClass.showShortToast(activity.getApplicationContext(), jsonObject.optString("message"));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, "DELETE_POLL", true).execute(WebServicesUrls.DELETE_POST);
                                    break;
                            }
                            return false;
                        }
                    });
                    if (postPOJO.getPostProfile().getUserProfileId().equalsIgnoreCase(Constants.userProfilePOJO.getUserProfileId())) {
                        menu.inflate(R.menu.menu_my_poll);
                    } else {
                        menu.inflate(R.menu.menu_other_poll);
                    }
                    menu.show();
                }
            });


            if (postPOJO.getMeLike().equalsIgnoreCase("0")) {
                postViewHolder.iv_like.setImageResource(R.drawable.ic_unlike);
            } else if (postPOJO.getMeLike().equalsIgnoreCase("1")) {
                postViewHolder.iv_like.setImageResource(R.drawable.ic_like);
            }

            postViewHolder.tv_like.setText(postPOJO.getTotalLikes());
            postViewHolder.tv_comments.setText(postPOJO.getTotalComment());

            postViewHolder.ll_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity instanceof HomeActivity) {
                        HomeActivity homeActivity = (HomeActivity) activity;
                        homeActivity.showComment(postViewHolder.tv_comments, postPOJO);
                    }
                }
            });

            postViewHolder.ll_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (postPOJO.getMeLike().equalsIgnoreCase("0")) {
                            postViewHolder.iv_like.setImageResource(R.drawable.ic_like);
                        } else if (postPOJO.getMeLike().equalsIgnoreCase("1")) {
                            postViewHolder.iv_like.setImageResource(R.drawable.ic_unlike);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                    nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                    nameValuePairs.add(new BasicNameValuePair("post_id", postPOJO.getPostId()));
                    new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
                        @Override
                        public void onGetMsg(String apicall, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.optString("status").equalsIgnoreCase("success")
                                        && jsonObject.optString("result").equalsIgnoreCase("1")) {
                                    //post liked
                                    postViewHolder.iv_like.setImageResource(R.drawable.ic_like);
                                } else if (jsonObject.optString("result").equalsIgnoreCase("0")) {
                                    //post unliked
                                    postViewHolder.iv_like.setImageResource(R.drawable.ic_unlike);
                                }

                                postViewHolder.tv_like.setText(jsonObject.optString("result"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, "CALL_LIKE_API", false).execute(WebServicesUrls.POST_LIKE);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void inflateComplaintData(final ComplaintViewHolder complaintViewHolder, final ComplaintPOJO complaintPOJO, final int position) {
        try {
            UserProfilePOJO userProfilePOJO = complaintPOJO.getComplaintProfile();
            SetViews.setProfilePhoto(activity.getApplicationContext(), userProfilePOJO.getProfilePhotoPath(), complaintViewHolder.cv_profile_pic);

            if (complaintPOJO.getComplaintAttachments().size() > 0) {

                if (complaintPOJO.getComplaintAttachments().size() == 1) {
                    complaintViewHolder.iv_feed_image.setVisibility(View.VISIBLE);
                    complaintViewHolder.ll_image_2.setVisibility(View.GONE);
                    complaintViewHolder.ll_image_3.setVisibility(View.GONE);
                    Glide.with(activity.getApplicationContext())
                            .load(complaintPOJO.getComplaintAttachments().get(0).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(complaintViewHolder.iv_feed_image);
                } else if (complaintPOJO.getComplaintAttachments().size() == 2) {
                    complaintViewHolder.iv_feed_image.setVisibility(View.GONE);
                    complaintViewHolder.ll_image_2.setVisibility(View.VISIBLE);
                    complaintViewHolder.ll_image_3.setVisibility(View.GONE);
                    Glide.with(activity.getApplicationContext())
                            .load(complaintPOJO.getComplaintAttachments().get(0).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(complaintViewHolder.iv_1);

                    Glide.with(activity.getApplicationContext())
                            .load(complaintPOJO.getComplaintAttachments().get(1).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(complaintViewHolder.iv_2);


                } else if (complaintPOJO.getComplaintAttachments().size() > 2) {
                    complaintViewHolder.iv_feed_image.setVisibility(View.GONE);
                    complaintViewHolder.ll_image_2.setVisibility(View.GONE);
                    complaintViewHolder.ll_image_3.setVisibility(View.VISIBLE);

                    Glide.with(activity.getApplicationContext())
                            .load(complaintPOJO.getComplaintAttachments().get(0).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(complaintViewHolder.iv_3);

                    Glide.with(activity.getApplicationContext())
                            .load(complaintPOJO.getComplaintAttachments().get(1).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(complaintViewHolder.iv_4);

                    Glide.with(activity.getApplicationContext())
                            .load(complaintPOJO.getComplaintAttachments().get(2).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(complaintViewHolder.iv_5);
                    if (complaintPOJO.getComplaintAttachments().size() > 3) {
                        complaintViewHolder.tv_more_img.setVisibility(View.VISIBLE);
                        complaintViewHolder.tv_more_img.setText("+" + (complaintPOJO.getComplaintAttachments().size() - 3));
                    }
                }

                complaintViewHolder.ll_images.setOnClickListener(new View.OnClickListener() {
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

                        if (activity instanceof HomeActivity) {
                            HomeActivity homeActivity = (HomeActivity) activity;
                            homeActivity.startFragment(R.id.frame_home, attachmentViewPagerFragment);
                        }

                    }
                });


            } else {
                complaintViewHolder.iv_feed_image.setVisibility(View.GONE);
                complaintViewHolder.ll_image_2.setVisibility(View.GONE);
                complaintViewHolder.ll_image_3.setVisibility(View.GONE);
            }
            List<String> span = new ArrayList<>();
            List<UserProfilePOJO> profilePOJOList = new ArrayList<>();
            String name = "";
            String user_name = userProfilePOJO.getFirstName() + " " + userProfilePOJO.getLastName();
            name = "<b>" + user_name + "</b>";
            span.add(user_name);
            profilePOJOList.add(userProfilePOJO);
            if (complaintPOJO.getComplaintAssigned().size() > 0) {
                String leader_name = complaintPOJO.getComplaintAssigned().get(0).getFirstName() + " " + complaintPOJO.getComplaintAssigned().get(0).getLastName();
                name += " has raised a Complaint with <b>" + leader_name + "</b>";
                span.add(leader_name);
                profilePOJOList.add(complaintPOJO.getComplaintAssigned().get(0));
            }

            complaintViewHolder.tv_profile_name.setText(Html.fromHtml(name));

            if (span.size() > 0) {
                String[] tags = span.toArray(new String[span.size()]);
                ClickableSpan[] clickableSpans = new ClickableSpan[profilePOJOList.size()];

                for (int i = 0; i < profilePOJOList.size(); i++) {
                    clickableSpans[i] = returnSpanClick(profilePOJOList.get(i));
                }

                makeLinks(complaintViewHolder.tv_profile_name, tags, clickableSpans);
            }


//            String profile_description = "";
//            boolean containDescribe = false;
//            if (complaintPOJO.getPostTag().size() > 0) {
//                containDescribe = true;
//            }
//
//            if (complaintPOJO.getFeelingDataPOJOS().size() > 0) {
//                containDescribe = true;
//            }
//
//            if (complaintPOJO.getPostLocation().length() > 0) {
//                containDescribe = true;
//            }
//
//            if (containDescribe) {
//                profile_description += " is ";
//
//                if (complaintPOJO.getFeelingDataPOJOS().size() > 0) {
//                    profile_description += "<b>feeling " + complaintPOJO.getFeelingDataPOJOS().get(0).getFeelingName() + "</b>";
//                }
//
//                if (complaintPOJO.getPostTag().size() > 0) {
//                    profile_description += " with ";
//                    if (complaintPOJO.getPostTag().size() > 2) {
//                        profile_description += "<b>" + complaintPOJO.getPostTag().get(0).getFirstName() + " and " + (complaintPOJO.getPostTag().size() - 1) + " other" + "</b>";
//                    } else if (complaintPOJO.getPostTag().size() == 2) {
//                        profile_description += "<b>" + complaintPOJO.getPostTag().get(0).getFirstName() + " " + complaintPOJO.getPostTag().get(0).getLastName() +
//                                " and " + complaintPOJO.getPostTag().get(1).getFirstName() + " " + complaintPOJO.getPostTag().get(1).getLastName() + "</b>";
//                    } else {
//                        profile_description += "<b>" + complaintPOJO.getPostTag().get(0).getFirstName() + " " + complaintPOJO.getPostTag().get(0).getLastName() + "</b>";
//                    }
//                }
//
//                if (complaintPOJO.getPostLocation().length() > 0) {
//                    profile_description += " - at <b>" + complaintPOJO.getPostLocation() + "</b>";
//                }
//            }
            complaintViewHolder.tv_title.setText("Subject : " + complaintPOJO.getComplaintSubject());

            if (!complaintPOJO.getComplaintDescription().equalsIgnoreCase("")) {
                complaintViewHolder.tv_description.setText(complaintPOJO.getComplaintDescription());
            } else {
                complaintViewHolder.tv_description.setVisibility(View.GONE);
            }
            complaintViewHolder.tv_date.setText(complaintPOJO.getAddedOn());

            complaintViewHolder.tv_profile_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    complaintViewHolder.cv_profile_pic.callOnClick();
                }
            });


            if (complaintPOJO.getMeLike() == 0) {
                complaintViewHolder.iv_like.setImageResource(R.drawable.ic_unlike);
            } else if (complaintPOJO.getMeLike() == 1) {
                complaintViewHolder.iv_like.setImageResource(R.drawable.ic_like);
            }
//
            complaintViewHolder.tv_like.setText(String.valueOf(complaintPOJO.getTotalLikes()));
            complaintViewHolder.tv_comments.setText(String.valueOf(complaintPOJO.getTotalComment()));

            complaintViewHolder.ll_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity instanceof HomeActivity) {
                        HomeActivity homeActivity = (HomeActivity) activity;
                        homeActivity.showComplaintComments(complaintViewHolder.tv_comments, complaintPOJO);
                    }
                }
            });

            complaintViewHolder.ll_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (complaintPOJO.getMeLike() == 0) {
                            complaintViewHolder.iv_like.setImageResource(R.drawable.ic_like);
                        } else if (complaintPOJO.getMeLike() == 1) {
                            complaintViewHolder.iv_like.setImageResource(R.drawable.ic_unlike);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                    nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                    nameValuePairs.add(new BasicNameValuePair("complaint_id", complaintPOJO.getComplaintId()));
                    new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
                        @Override
                        public void onGetMsg(String apicall, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.optString("status").equalsIgnoreCase("success")
                                        && jsonObject.optString("result").equalsIgnoreCase("1")) {
                                    //post liked
                                    complaintViewHolder.iv_like.setImageResource(R.drawable.ic_like);
                                } else if (jsonObject.optString("result").equalsIgnoreCase("0")) {
                                    //post unliked
                                    complaintViewHolder.iv_like.setImageResource(R.drawable.ic_unlike);
                                }
                                complaintViewHolder.tv_like.setText(jsonObject.optString("result"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, "CALL_LIKE_API", false).execute(WebServicesUrls.COMPLAINT_LIKE_UNLIKE);
                }
            });

            complaintViewHolder.ll_news_feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity instanceof HomeActivity) {
                        HomeActivity homeActivity = (HomeActivity) activity;
                        Bundle bundle = new Bundle();
                        bundle.putString("complaint_id", complaintPOJO.getComplaintId());
                        ComplaintDetailFragment complaintDetailFragment = new ComplaintDetailFragment();
                        complaintDetailFragment.setArguments(bundle);
                        homeActivity.startFragment(R.id.frame_home, complaintDetailFragment);
                    }
                }
            });


            complaintViewHolder.iv_post_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final PopupMenu menu = new PopupMenu(activity, view);

                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuitem) {
                            switch (menuitem.getItemId()) {
                                case R.id.popup_edit:
                                    if (complaintPOJO.getComplaintStatus().equalsIgnoreCase("1") ||
                                            complaintPOJO.getComplaintStatus().equalsIgnoreCase("5")
                                            ) {
                                        Intent intent = new Intent(activity, ExpressActivity.class);
                                        intent.putExtra("complaintPOJO", complaintPOJO);
                                        activity.startActivity(intent);
                                    } else {
                                        ToastClass.showShortToast(activity.getApplicationContext(), "You cannnot edit this complaint");
                                    }

                                    break;
                                case R.id.popup_delete:
                                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                                    nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                                    nameValuePairs.add(new BasicNameValuePair("complaint_id", complaintPOJO.getComplaintId()));
                                    new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
                                        @Override
                                        public void onGetMsg(String apicall, String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                                                    items.remove(position);
                                                    notifyDataSetChanged();
                                                }
                                                ToastClass.showShortToast(activity.getApplicationContext(), jsonObject.optString("message"));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, "DELETE_COMPLAINT", true).execute(WebServicesUrls.DELETE_COMPLAINT);
                                    break;
                            }
                            return false;
                        }
                    });
                    if (complaintPOJO.getComplaintProfile().getUserProfileId().equalsIgnoreCase(Constants.userProfilePOJO.getUserProfileId())) {
                        menu.inflate(R.menu.menu_my_poll);
                    } else {
                        menu.inflate(R.menu.menu_other_poll);
                    }
                    menu.show();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void inflateSuggestionData(final SuggestionViewHolder suggestionViewHolder, final SuggestionPOJO suggestionPOJO, final int position) {
        try {
            UserProfilePOJO userProfilePOJO = suggestionPOJO.getSuggestionProfile();
            SetViews.setProfilePhoto(activity.getApplicationContext(), userProfilePOJO.getProfilePhotoPath(), suggestionViewHolder.cv_profile_pic);

            if (suggestionPOJO.getSuggestionAttachment().size() > 0) {

                if (suggestionPOJO.getSuggestionAttachment().size() == 1) {
                    suggestionViewHolder.iv_feed_image.setVisibility(View.VISIBLE);
                    suggestionViewHolder.ll_image_2.setVisibility(View.GONE);
                    suggestionViewHolder.ll_image_3.setVisibility(View.GONE);
                    Glide.with(activity.getApplicationContext())
                            .load(suggestionPOJO.getSuggestionAttachment().get(0).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(suggestionViewHolder.iv_feed_image);
                } else if (suggestionPOJO.getSuggestionAttachment().size() == 2) {
                    suggestionViewHolder.iv_feed_image.setVisibility(View.GONE);
                    suggestionViewHolder.ll_image_2.setVisibility(View.VISIBLE);
                    suggestionViewHolder.ll_image_3.setVisibility(View.GONE);
                    Glide.with(activity.getApplicationContext())
                            .load(suggestionPOJO.getSuggestionAttachment().get(0).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(suggestionViewHolder.iv_1);

                    Glide.with(activity.getApplicationContext())
                            .load(suggestionPOJO.getSuggestionAttachment().get(1).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(suggestionViewHolder.iv_2);


                } else if (suggestionPOJO.getSuggestionAttachment().size() > 2) {
                    suggestionViewHolder.iv_feed_image.setVisibility(View.GONE);
                    suggestionViewHolder.ll_image_2.setVisibility(View.GONE);
                    suggestionViewHolder.ll_image_3.setVisibility(View.VISIBLE);

                    Glide.with(activity.getApplicationContext())
                            .load(suggestionPOJO.getSuggestionAttachment().get(0).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(suggestionViewHolder.iv_3);

                    Glide.with(activity.getApplicationContext())
                            .load(suggestionPOJO.getSuggestionAttachment().get(1).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(suggestionViewHolder.iv_4);

                    Glide.with(activity.getApplicationContext())
                            .load(suggestionPOJO.getSuggestionAttachment().get(2).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(suggestionViewHolder.iv_5);
                    if (suggestionPOJO.getSuggestionAttachment().size() > 3) {
                        suggestionViewHolder.tv_more_img.setVisibility(View.VISIBLE);
                        suggestionViewHolder.tv_more_img.setText("+" + (suggestionPOJO.getSuggestionAttachment().size() - 3));
                    }
                }

                suggestionViewHolder.ll_images.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<AttachmentPOJO> attachmentPOJOS = new ArrayList<>();
                        for (SuggestionAttachmentPOJO suggestionAttachmentPOJO : suggestionPOJO.getSuggestionAttachment()) {
                            AttachmentPOJO attachmentPOJO = new AttachmentPOJO();
                            attachmentPOJO.setFile_name(suggestionAttachmentPOJO.getAttachmentFile());
                            attachmentPOJO.setFile_path(suggestionAttachmentPOJO.getAttachmentFile());
                            attachmentPOJO.setFile_type(suggestionAttachmentPOJO.getAttachmentFile());
                            attachmentPOJO.setFeed_type("suggestion");
                            attachmentPOJO.setDescription(suggestionPOJO.getSuggestionSubject());

                            attachmentPOJOS.add(attachmentPOJO);
                        }

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("attachments", (Serializable) attachmentPOJOS);

                        AttachmentViewPagerFragment attachmentViewPagerFragment = new AttachmentViewPagerFragment();
                        attachmentViewPagerFragment.setArguments(bundle);

                        if (activity instanceof HomeActivity) {
                            HomeActivity homeActivity = (HomeActivity) activity;
                            homeActivity.startFragment(R.id.frame_home, attachmentViewPagerFragment);
                        }

                    }
                });


            } else {
                suggestionViewHolder.iv_feed_image.setVisibility(View.GONE);
                suggestionViewHolder.ll_image_2.setVisibility(View.GONE);
                suggestionViewHolder.ll_image_3.setVisibility(View.GONE);
            }

            List<String> span = new ArrayList<>();
            List<UserProfilePOJO> profilePOJOList = new ArrayList<>();

            String name = "";
            String profile_name = userProfilePOJO.getFirstName() + " " + userProfilePOJO.getLastName();
            name = "<b>" + profile_name + "</b>";

            span.add(profile_name);
            profilePOJOList.add(userProfilePOJO);

            if (suggestionPOJO.getSuggestionAssigned().size() > 0) {
                String leader_name = suggestionPOJO.getSuggestionAssigned().get(0).getFirstName() + " " + suggestionPOJO.getSuggestionAssigned().get(0).getLastName();
                name += " has suggested <b>" + leader_name;

                span.add(leader_name);
                profilePOJOList.add(suggestionPOJO.getSuggestionAssigned().get(0));

            }

            suggestionViewHolder.tv_profile_name.setText(Html.fromHtml(name));

            if (span.size() > 0) {
                String[] tags = span.toArray(new String[span.size()]);
                ClickableSpan[] clickableSpans = new ClickableSpan[profilePOJOList.size()];

                for (int i = 0; i < profilePOJOList.size(); i++) {
                    clickableSpans[i] = returnSpanClick(profilePOJOList.get(i));
                }

                makeLinks(suggestionViewHolder.tv_profile_name, tags, clickableSpans);
            }


            suggestionViewHolder.tv_title.setText("Subject : " + suggestionPOJO.getSuggestionSubject());

            if (!suggestionPOJO.getSuggestionDescription().equalsIgnoreCase("")) {
                suggestionViewHolder.tv_description.setText(suggestionPOJO.getSuggestionDescription());
            } else {
                suggestionViewHolder.tv_description.setVisibility(View.GONE);
            }
            suggestionViewHolder.tv_date.setText(suggestionPOJO.getAddedOn());

            suggestionViewHolder.tv_profile_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    suggestionViewHolder.cv_profile_pic.callOnClick();
                }
            });

            suggestionViewHolder.ll_news_feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity instanceof HomeActivity) {
                        HomeActivity homeActivity = (HomeActivity) activity;
                        Bundle bundle = new Bundle();
                        bundle.putString("suggestion_id", suggestionPOJO.getSuggestionId());
                        SuggestionDetailFragment suggestionDetailFragments = new SuggestionDetailFragment();
                        suggestionDetailFragments.setArguments(bundle);
                        homeActivity.startFragment(R.id.frame_home, suggestionDetailFragments);
                    }
                }
            });

            suggestionViewHolder.iv_post_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final PopupMenu menu = new PopupMenu(activity, view);

                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuitem) {
                            switch (menuitem.getItemId()) {
                                case R.id.popup_edit:

                                    break;
                                case R.id.popup_delete:
                                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                                    nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                                    nameValuePairs.add(new BasicNameValuePair("suggestion_id", suggestionPOJO.getSuggestionId()));
                                    new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
                                        @Override
                                        public void onGetMsg(String apicall, String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                                                    items.remove(position);
                                                    notifyDataSetChanged();
                                                }
                                                ToastClass.showShortToast(activity.getApplicationContext(), jsonObject.optString("message"));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, "DELETE_COMPLAINT", true).execute(WebServicesUrls.DELETE_SUGGESTION);
                                    break;
                            }
                            return false;
                        }
                    });
                    if (suggestionPOJO.getSuggestionProfile().getUserProfileId().equalsIgnoreCase(Constants.userProfilePOJO.getUserProfileId())) {
                        menu.inflate(R.menu.menu_my_poll);
                    } else {
                        menu.inflate(R.menu.menu_other_poll);
                    }
                    menu.show();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inflateInformationData(final InformationViewHolder informationViewHolder, final InformationPOJO informationPOJO, final int position) {
        try {
            final UserProfilePOJO userProfilePOJO = informationPOJO.getInformationProfile();
            SetViews.setProfilePhoto(activity.getApplicationContext(), userProfilePOJO.getProfilePhotoPath(), informationViewHolder.cv_profile_pic);

            informationViewHolder.cv_profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity instanceof HomeActivity) {

                        HomeActivity homeActivity = (HomeActivity) activity;

                        UserProfileFragment userProfileFragment = new UserProfileFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user_profile_id", userProfilePOJO.getUserProfileId());
                        userProfileFragment.setArguments(bundle);

                        homeActivity.startFragment(R.id.frame_home, userProfileFragment);
                    }
                }
            });
            if (informationPOJO.getInformationAttachment().size() > 0) {

                if (informationPOJO.getInformationAttachment().size() == 1) {
                    informationViewHolder.iv_feed_image.setVisibility(View.VISIBLE);
                    informationViewHolder.ll_image_2.setVisibility(View.GONE);
                    informationViewHolder.ll_image_3.setVisibility(View.GONE);
                    Glide.with(activity.getApplicationContext())
                            .load(informationPOJO.getInformationAttachment().get(0).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(informationViewHolder.iv_feed_image);
                } else if (informationPOJO.getInformationAttachment().size() == 2) {
                    informationViewHolder.iv_feed_image.setVisibility(View.GONE);
                    informationViewHolder.ll_image_2.setVisibility(View.VISIBLE);
                    informationViewHolder.ll_image_3.setVisibility(View.GONE);
                    Glide.with(activity.getApplicationContext())
                            .load(informationPOJO.getInformationAttachment().get(0).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(informationViewHolder.iv_1);

                    Glide.with(activity.getApplicationContext())
                            .load(informationPOJO.getInformationAttachment().get(1).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(informationViewHolder.iv_2);


                } else if (informationPOJO.getInformationAttachment().size() > 2) {
                    informationViewHolder.iv_feed_image.setVisibility(View.GONE);
                    informationViewHolder.ll_image_2.setVisibility(View.GONE);
                    informationViewHolder.ll_image_3.setVisibility(View.VISIBLE);

                    Glide.with(activity.getApplicationContext())
                            .load(informationPOJO.getInformationAttachment().get(0).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(informationViewHolder.iv_3);

                    Glide.with(activity.getApplicationContext())
                            .load(informationPOJO.getInformationAttachment().get(1).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(informationViewHolder.iv_4);

                    Glide.with(activity.getApplicationContext())
                            .load(informationPOJO.getInformationAttachment().get(2).getAttachmentFile())
                            .error(R.drawable.ic_default_profile_pic)
                            .placeholder(R.drawable.ic_default_profile_pic)
                            .dontAnimate()
                            .into(informationViewHolder.iv_5);
                    if (informationPOJO.getInformationAttachment().size() > 3) {
                        informationViewHolder.tv_more_img.setVisibility(View.VISIBLE);
                        informationViewHolder.tv_more_img.setText("+" + (informationPOJO.getInformationAttachment().size() - 3));
                    }
                }

                informationViewHolder.ll_images.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<AttachmentPOJO> attachmentPOJOS = new ArrayList<>();
                        for (InformationAttachmentPOJO informationAttachmentPOJO : informationPOJO.getInformationAttachment()) {
                            AttachmentPOJO attachmentPOJO = new AttachmentPOJO();
                            attachmentPOJO.setFile_name(informationAttachmentPOJO.getAttachmentFile());
                            attachmentPOJO.setFile_path(informationAttachmentPOJO.getAttachmentFile());
                            attachmentPOJO.setFile_type(informationAttachmentPOJO.getAttachmentFile());
                            attachmentPOJO.setFeed_type("information");
                            attachmentPOJO.setDescription(informationPOJO.getInformationSubject());

                            attachmentPOJOS.add(attachmentPOJO);
                        }

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("attachments", (Serializable) attachmentPOJOS);

                        AttachmentViewPagerFragment attachmentViewPagerFragment = new AttachmentViewPagerFragment();
                        attachmentViewPagerFragment.setArguments(bundle);

                        if (activity instanceof HomeActivity) {
                            HomeActivity homeActivity = (HomeActivity) activity;
                            homeActivity.startFragment(R.id.frame_home, attachmentViewPagerFragment);
                        }

                    }
                });


            } else {
                informationViewHolder.iv_feed_image.setVisibility(View.GONE);
                informationViewHolder.ll_image_2.setVisibility(View.GONE);
                informationViewHolder.ll_image_3.setVisibility(View.GONE);
            }

            String name = "";
            name = "<b>" + userProfilePOJO.getFirstName() + " " + userProfilePOJO.getLastName() + "</b> has post an <b>Information</b>";
//            if (informationPOJO.get().size() > 0) {
//                name += " has suggested <b>" + informationPOJO.getSuggestionAssigned().get(0).getFirstName() + " " + informationPOJO.getSuggestionAssigned().get(0).getLastName();
//            }

            informationViewHolder.tv_title.setText("Subject : " + informationPOJO.getInformationSubject());
            informationViewHolder.tv_profile_name.setText(Html.fromHtml(name));
            if (!informationPOJO.getInformationDescription().equalsIgnoreCase("")) {
                informationViewHolder.tv_description.setText(informationPOJO.getInformationDescription());
            } else {
                informationViewHolder.tv_description.setVisibility(View.GONE);
            }
            informationViewHolder.tv_date.setText(informationPOJO.getAddedOn());

            informationViewHolder.tv_profile_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    informationViewHolder.cv_profile_pic.callOnClick();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView tv_title, tv_description;
//        public LinearLayout ll_analyze;

        public ViewHolder(View itemView) {
            super(itemView);
//            tv_title = itemView.findViewById(R.id.tv_title);
//            tv_description = itemView.findViewById(R.id.tv_description);
        }
    }

    class PollViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_questions;
        public TextView tv_profile_name;
        public TextView tv_date;
        public TextView tv_participated;
        public CircleImageView cv_profile_pic;
        public ImageView iv_poll_image;
        public ImageView iv_poll_menu;
        public LinearLayout ll_already_participated;
        public RecyclerView rv_ans;
        public LinearLayout ll_like;
        public ImageView iv_like;
        public TextView tv_like;
        public TextView tv_comments;
        public LinearLayout ll_comment;
        public TextView tv_poll_ends_in;
        public TextView tv_total_votes;
        public LinearLayout ll_poll;


        public PollViewHolder(View itemView) {
            super(itemView);
            tv_questions = itemView.findViewById(R.id.tv_questions);
            ll_already_participated = itemView.findViewById(R.id.ll_already_participated);
            tv_profile_name = itemView.findViewById(R.id.tv_profile_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            cv_profile_pic = itemView.findViewById(R.id.cv_profile_pic);
            iv_poll_image = itemView.findViewById(R.id.iv_poll_image);
            iv_poll_menu = itemView.findViewById(R.id.iv_poll_menu);
            tv_participated = itemView.findViewById(R.id.tv_participated);
            ll_like = itemView.findViewById(R.id.ll_like);
            rv_ans = itemView.findViewById(R.id.rv_ans);
            iv_like = itemView.findViewById(R.id.iv_like);
            tv_like = itemView.findViewById(R.id.tv_like);
            tv_comments = itemView.findViewById(R.id.tv_comments);
            ll_comment = itemView.findViewById(R.id.ll_comment);
            tv_poll_ends_in = itemView.findViewById(R.id.tv_poll_ends_in);
            tv_total_votes = itemView.findViewById(R.id.tv_total_votes);
            ll_poll = itemView.findViewById(R.id.ll_poll);
        }
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public TextView tv_profile_name;
        public TextView tv_date;
        public ImageView iv_event_image;
        public ImageView iv_event_menu;
        public ImageView cv_profile_pic;
        public TextView tv_event_date;
        public TextView tv_description;
        public TextView tv_place;
        public LinearLayout ll_event;
        public Spinner spinner_interest;
        public LinearLayout ll_like;
        public ImageView iv_like;
        public TextView tv_like;
        public TextView tv_comments;
        public LinearLayout ll_comment;
        public FrameLayout frame_event_interest;

        public EventViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_profile_name = itemView.findViewById(R.id.tv_profile_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_event_image = itemView.findViewById(R.id.iv_event_image);
            iv_event_menu = itemView.findViewById(R.id.iv_event_menu);
            cv_profile_pic = itemView.findViewById(R.id.cv_profile_pic);
            tv_event_date = itemView.findViewById(R.id.tv_event_date);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_place = itemView.findViewById(R.id.tv_place);
            ll_event = itemView.findViewById(R.id.ll_event);
            spinner_interest = itemView.findViewById(R.id.spinner_interest);
            ll_like = itemView.findViewById(R.id.ll_like);
            iv_like = itemView.findViewById(R.id.iv_like);
            tv_like = itemView.findViewById(R.id.tv_like);
            tv_comments = itemView.findViewById(R.id.tv_comments);
            ll_comment = itemView.findViewById(R.id.ll_comment);
            frame_event_interest = itemView.findViewById(R.id.frame_event_interest);
        }
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_profile_name;
        public TextView tv_date;
        public ImageView iv_feed_image;
        public ImageView iv_post_menu;
        public TextView tv_description;
        public CircleImageView cv_profile_pic;
        public LinearLayout ll_news_feed;
        public ImageView iv_1;
        public ImageView iv_2;
        public ImageView iv_3;
        public ImageView iv_4;
        public ImageView iv_5;
        public TextView tv_more_img;
        public LinearLayout ll_image_2;
        public LinearLayout ll_image_3;
        public LinearLayout ll_like;
        public ImageView iv_like;
        public TextView tv_like;
        public TextView tv_comments;
        public LinearLayout ll_comment;
        public LinearLayout ll_images;

        public PostViewHolder(View itemView) {
            super(itemView);
            tv_profile_name = itemView.findViewById(R.id.tv_profile_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_feed_image = itemView.findViewById(R.id.iv_feed_image);
            iv_post_menu = itemView.findViewById(R.id.iv_post_menu);
            tv_description = itemView.findViewById(R.id.tv_description);
            cv_profile_pic = itemView.findViewById(R.id.cv_profile_pic);
            ll_news_feed = itemView.findViewById(R.id.ll_news_feed);
            iv_1 = itemView.findViewById(R.id.iv_1);
            iv_2 = itemView.findViewById(R.id.iv_2);
            iv_3 = itemView.findViewById(R.id.iv_3);
            iv_4 = itemView.findViewById(R.id.iv_4);
            iv_5 = itemView.findViewById(R.id.iv_5);
            tv_more_img = itemView.findViewById(R.id.tv_more_img);
            ll_image_2 = itemView.findViewById(R.id.ll_image_2);
            ll_image_3 = itemView.findViewById(R.id.ll_image_3);
            ll_like = itemView.findViewById(R.id.ll_like);
            iv_like = itemView.findViewById(R.id.iv_like);
            tv_like = itemView.findViewById(R.id.tv_like);
            tv_comments = itemView.findViewById(R.id.tv_comments);
            ll_comment = itemView.findViewById(R.id.ll_comment);
            ll_like = itemView.findViewById(R.id.ll_like);
            iv_like = itemView.findViewById(R.id.iv_like);
            tv_like = itemView.findViewById(R.id.tv_like);
            tv_comments = itemView.findViewById(R.id.tv_comments);
            ll_comment = itemView.findViewById(R.id.ll_comment);
            ll_images = itemView.findViewById(R.id.ll_images);
        }
    }

    class ComplaintViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_profile_name;
        public TextView tv_title;
        public TextView tv_date;
        public ImageView iv_feed_image;
        public ImageView iv_post_menu;
        public TextView tv_description;
        public CircleImageView cv_profile_pic;
        public LinearLayout ll_news_feed;
        public ImageView iv_1;
        public ImageView iv_2;
        public ImageView iv_3;
        public ImageView iv_4;
        public ImageView iv_5;
        public TextView tv_more_img;
        public LinearLayout ll_image_2;
        public LinearLayout ll_image_3;
        public LinearLayout ll_like;
        public ImageView iv_like;
        public TextView tv_like;
        public TextView tv_comments;
        public LinearLayout ll_comment;
        public LinearLayout ll_images;

        public ComplaintViewHolder(View itemView) {
            super(itemView);
            tv_profile_name = itemView.findViewById(R.id.tv_profile_name);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_feed_image = itemView.findViewById(R.id.iv_feed_image);
            iv_post_menu = itemView.findViewById(R.id.iv_post_menu);
            tv_description = itemView.findViewById(R.id.tv_description);
            cv_profile_pic = itemView.findViewById(R.id.cv_profile_pic);
            ll_news_feed = itemView.findViewById(R.id.ll_news_feed);
            iv_1 = itemView.findViewById(R.id.iv_1);
            iv_2 = itemView.findViewById(R.id.iv_2);
            iv_3 = itemView.findViewById(R.id.iv_3);
            iv_4 = itemView.findViewById(R.id.iv_4);
            iv_5 = itemView.findViewById(R.id.iv_5);
            tv_more_img = itemView.findViewById(R.id.tv_more_img);
            ll_image_2 = itemView.findViewById(R.id.ll_image_2);
            ll_image_3 = itemView.findViewById(R.id.ll_image_3);
            ll_like = itemView.findViewById(R.id.ll_like);
            iv_like = itemView.findViewById(R.id.iv_like);
            tv_like = itemView.findViewById(R.id.tv_like);
            tv_comments = itemView.findViewById(R.id.tv_comments);
            ll_comment = itemView.findViewById(R.id.ll_comment);
            ll_images = itemView.findViewById(R.id.ll_images);
        }
    }

    class SuggestionViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_profile_name;
        public TextView tv_title;
        public TextView tv_date;
        public ImageView iv_post_menu;
        public TextView tv_description;
        public CircleImageView cv_profile_pic;
        public LinearLayout ll_news_feed;
        public ImageView iv_feed_image;
        public ImageView iv_1;
        public ImageView iv_2;
        public ImageView iv_3;
        public ImageView iv_4;
        public ImageView iv_5;
        public TextView tv_more_img;
        public LinearLayout ll_image_2;
        public LinearLayout ll_image_3;
        public LinearLayout ll_like;
        public ImageView iv_like;
        public TextView tv_like;
        public TextView tv_comments;
        public LinearLayout ll_comment;
        public LinearLayout ll_images;

        public SuggestionViewHolder(View itemView) {
            super(itemView);
            tv_profile_name = itemView.findViewById(R.id.tv_profile_name);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_feed_image = itemView.findViewById(R.id.iv_feed_image);
            iv_post_menu = itemView.findViewById(R.id.iv_post_menu);
            tv_description = itemView.findViewById(R.id.tv_description);
            cv_profile_pic = itemView.findViewById(R.id.cv_profile_pic);
            ll_news_feed = itemView.findViewById(R.id.ll_news_feed);
            iv_1 = itemView.findViewById(R.id.iv_1);
            iv_2 = itemView.findViewById(R.id.iv_2);
            iv_3 = itemView.findViewById(R.id.iv_3);
            iv_4 = itemView.findViewById(R.id.iv_4);
            iv_5 = itemView.findViewById(R.id.iv_5);
            tv_more_img = itemView.findViewById(R.id.tv_more_img);
            ll_image_2 = itemView.findViewById(R.id.ll_image_2);
            ll_image_3 = itemView.findViewById(R.id.ll_image_3);
            ll_like = itemView.findViewById(R.id.ll_like);
            iv_like = itemView.findViewById(R.id.iv_like);
            tv_like = itemView.findViewById(R.id.tv_like);
            tv_comments = itemView.findViewById(R.id.tv_comments);
            ll_comment = itemView.findViewById(R.id.ll_comment);
            ll_images = itemView.findViewById(R.id.ll_images);
        }
    }

    class InformationViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_profile_name;
        public TextView tv_title;
        public TextView tv_date;
        public ImageView iv_feed_image;
        public ImageView iv_post_menu;
        public TextView tv_description;
        public CircleImageView cv_profile_pic;
        public LinearLayout ll_news_feed;
        public ImageView iv_1;
        public ImageView iv_2;
        public ImageView iv_3;
        public ImageView iv_4;
        public ImageView iv_5;
        public TextView tv_more_img;
        public LinearLayout ll_image_2;
        public LinearLayout ll_image_3;
        public LinearLayout ll_like;
        public ImageView iv_like;
        public TextView tv_like;
        public TextView tv_comments;
        public LinearLayout ll_comment;
        public LinearLayout ll_images;

        public InformationViewHolder(View itemView) {
            super(itemView);
            tv_profile_name = itemView.findViewById(R.id.tv_profile_name);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_feed_image = itemView.findViewById(R.id.iv_feed_image);
            iv_post_menu = itemView.findViewById(R.id.iv_post_menu);
            tv_description = itemView.findViewById(R.id.tv_description);
            cv_profile_pic = itemView.findViewById(R.id.cv_profile_pic);
            ll_news_feed = itemView.findViewById(R.id.ll_news_feed);
            iv_1 = itemView.findViewById(R.id.iv_1);
            iv_2 = itemView.findViewById(R.id.iv_2);
            iv_3 = itemView.findViewById(R.id.iv_3);
            iv_4 = itemView.findViewById(R.id.iv_4);
            iv_5 = itemView.findViewById(R.id.iv_5);
            tv_more_img = itemView.findViewById(R.id.tv_more_img);
            ll_image_2 = itemView.findViewById(R.id.ll_image_2);
            ll_image_3 = itemView.findViewById(R.id.ll_image_3);
            ll_like = itemView.findViewById(R.id.ll_like);
            iv_like = itemView.findViewById(R.id.iv_like);
            tv_like = itemView.findViewById(R.id.tv_like);
            tv_comments = itemView.findViewById(R.id.tv_comments);
            ll_comment = itemView.findViewById(R.id.ll_comment);
            ll_images = itemView.findViewById(R.id.ll_images);
        }
    }

    public static class UserProfileViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView cv_profile_pic;
        public TextView tv_user_name, tv_email;
        public LinearLayout ll_user;
        public Button btn_accept;
        public LinearLayout ll_delete;
        public ImageView iv_crown;
        public SwipeRevealLayout swipeRevealLayout;

        public UserProfileViewHolder(View itemView) {
            super(itemView);
            cv_profile_pic = itemView.findViewById(R.id.cv_profile_pic);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            tv_email = itemView.findViewById(R.id.tv_email);
            ll_user = itemView.findViewById(R.id.ll_user);
            btn_accept = itemView.findViewById(R.id.btn_accept);
            ll_delete = itemView.findViewById(R.id.ll_delete);
            iv_crown = itemView.findViewById(R.id.iv_crown);
            swipeRevealLayout = itemView.findViewById(R.id.swipeRevealLayout);
        }
    }


    public void setLoaded() {
        isLoading = false;
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
