package com.ritvi.kaajneeti.fragment.post;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.activity.express.ExpressActivity;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.fragment.AttachmentViewPagerFragment;
import com.ritvi.kaajneeti.fragment.NotificationFragment;
import com.ritvi.kaajneeti.fragment.search.SearchAllFragment;
import com.ritvi.kaajneeti.fragment.user.UserProfileFragment;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.attachments.AttachmentPOJO;
import com.ritvi.kaajneeti.pojo.post.PostAttachmentPOJO;
import com.ritvi.kaajneeti.pojo.post.PostPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostViewFragment extends FragmentController {

    @BindView(R.id.tv_profile_name)
    public TextView tv_profile_name;
    @BindView(R.id.tv_date)
    public TextView tv_date;
    @BindView(R.id.iv_feed_image)
    public ImageView iv_feed_image;
    @BindView(R.id.iv_post_menu)
    public ImageView iv_post_menu;
    @BindView(R.id.tv_description)
    public TextView tv_description;
    @BindView(R.id.cv_profile_pic)
    public CircleImageView cv_profile_pic;
    @BindView(R.id.ll_news_feed)
    public LinearLayout ll_news_feed;
    @BindView(R.id.iv_1)
    public ImageView iv_1;
    @BindView(R.id.iv_2)
    public ImageView iv_2;
    @BindView(R.id.iv_3)
    public ImageView iv_3;
    @BindView(R.id.iv_4)
    public ImageView iv_4;
    @BindView(R.id.iv_5)
    public ImageView iv_5;
    @BindView(R.id.tv_more_img)
    public TextView tv_more_img;
    @BindView(R.id.ll_image_2)
    public LinearLayout ll_image_2;
    @BindView(R.id.ll_image_3)
    public LinearLayout ll_image_3;
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
    @BindView(R.id.ll_images)
    public LinearLayout ll_images;

    @BindView(R.id.ll_back)
    public LinearLayout ll_back;
    @BindView(R.id.ll_search)
    public LinearLayout ll_search;
    @BindView(R.id.iv_notification)
    public ImageView iv_notification;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    PostPOJO postPOJO;
    String post_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_post_view, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            post_id = getArguments().getString("post_id");
            getPostDetails(true);
        }

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityManager.startFragment(R.id.frame_home, new NotificationFragment());
            }
        });
        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityManager.startFragment(R.id.frame_home, new SearchAllFragment());
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPostDetails(true);
            }
        });
    }

    public void getPostDetails(boolean is_loading) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("post_id", post_id));
        new WebServiceBaseResponse<PostPOJO>(nameValuePairs, getActivity(), new ResponseCallBack<PostPOJO>() {
            @Override
            public void onGetMsg(ResponsePOJO<PostPOJO> responsePOJO) {
                swipeRefreshLayout.setRefreshing(false);
                try {
                    if (responsePOJO.isSuccess()) {
                        postPOJO = responsePOJO.getResult();
                        showPostData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, PostPOJO.class, "GET_POST_DETAILS", is_loading).execute(WebServicesUrls.POST_DETAIL);
    }


    public void showPostData() {
        if (postPOJO != null) {
            try {
                UserProfilePOJO userProfilePOJO = postPOJO.getPostProfile();

                if (postPOJO.getPostAttachment().size() > 0) {

                    if (postPOJO.getPostAttachment().size() == 1) {
                        iv_feed_image.setVisibility(View.VISIBLE);
                        ll_image_2.setVisibility(View.GONE);
                        ll_image_3.setVisibility(View.GONE);
                        Glide.with(getActivity().getApplicationContext())
                                .load(postPOJO.getPostAttachment().get(0).getAttachmentFile())
                                .error(R.drawable.ic_default_profile_pic)
                                .placeholder(R.drawable.ic_default_profile_pic)
                                .dontAnimate()
                                .into(iv_feed_image);
                    } else if (postPOJO.getPostAttachment().size() == 2) {
                        iv_feed_image.setVisibility(View.GONE);
                        ll_image_2.setVisibility(View.VISIBLE);
                        ll_image_3.setVisibility(View.GONE);
                        Glide.with(getActivity().getApplicationContext())
                                .load(postPOJO.getPostAttachment().get(0).getAttachmentFile())
                                .error(R.drawable.ic_default_profile_pic)
                                .placeholder(R.drawable.ic_default_profile_pic)
                                .dontAnimate()
                                .into(iv_1);

                        Glide.with(getActivity().getApplicationContext())
                                .load(postPOJO.getPostAttachment().get(1).getAttachmentFile())
                                .error(R.drawable.ic_default_profile_pic)
                                .placeholder(R.drawable.ic_default_profile_pic)
                                .dontAnimate()
                                .into(iv_2);


                    } else if (postPOJO.getPostAttachment().size() > 2) {
                        iv_feed_image.setVisibility(View.GONE);
                        ll_image_2.setVisibility(View.GONE);
                        ll_image_3.setVisibility(View.VISIBLE);

                        Glide.with(getActivity().getApplicationContext())
                                .load(postPOJO.getPostAttachment().get(0).getAttachmentFile())
                                .error(R.drawable.ic_default_profile_pic)
                                .placeholder(R.drawable.ic_default_profile_pic)
                                .dontAnimate()
                                .into(iv_3);

                        Glide.with(getActivity().getApplicationContext())
                                .load(postPOJO.getPostAttachment().get(1).getAttachmentFile())
                                .error(R.drawable.ic_default_profile_pic)
                                .placeholder(R.drawable.ic_default_profile_pic)
                                .dontAnimate()
                                .into(iv_4);

                        Glide.with(getActivity().getApplicationContext())
                                .load(postPOJO.getPostAttachment().get(2).getAttachmentFile())
                                .error(R.drawable.ic_default_profile_pic)
                                .placeholder(R.drawable.ic_default_profile_pic)
                                .dontAnimate()
                                .into(iv_5);
                        if (postPOJO.getPostAttachment().size() > 3) {
                            tv_more_img.setVisibility(View.VISIBLE);
                            tv_more_img.setText("+" + (postPOJO.getPostAttachment().size() - 3));
                        }
                    }

                    ll_images.setOnClickListener(new View.OnClickListener() {
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

                            if (getActivity() instanceof HomeActivity) {
                                HomeActivity homeActivity = (HomeActivity) getActivity();
                                homeActivity.startFragment(R.id.frame_home, attachmentViewPagerFragment);
                            }

                        }
                    });

                } else {
                    iv_feed_image.setVisibility(View.GONE);
                    ll_image_2.setVisibility(View.GONE);
                    ll_image_3.setVisibility(View.GONE);
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

                tv_profile_name.setText(Html.fromHtml(name + " " + profile_description));
                if (span.size() > 0) {
                    String[] tags = span.toArray(new String[span.size()]);
                    ClickableSpan[] clickableSpans = new ClickableSpan[profilePOJOList.size()];

                    for (int i = 0; i < profilePOJOList.size(); i++) {
                        clickableSpans[i] = returnSpanClick(profilePOJOList.get(i));
                    }

                    makeLinks(tv_profile_name, tags, clickableSpans);
                }


                if (!postPOJO.getPostDescription().equalsIgnoreCase("")) {
                    tv_description.setText(postPOJO.getPostDescription());
                } else {
                    tv_description.setVisibility(View.GONE);
                }
                tv_date.setText(postPOJO.getAddedOn());

                ll_news_feed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getActivity() instanceof HomeActivity) {
                        }
                    }
                });

                tv_profile_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cv_profile_pic.callOnClick();
                    }
                });

                iv_post_menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final PopupMenu menu = new PopupMenu(getActivity(), view);

                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuitem) {
                                switch (menuitem.getItemId()) {
                                    case R.id.popup_edit:
                                        Intent intent = new Intent(getActivity(), ExpressActivity.class);
                                        intent.putExtra("post", postPOJO);
                                        startActivity(intent);
                                        break;
                                    case R.id.popup_delete:
                                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                                        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                                        nameValuePairs.add(new BasicNameValuePair("post_id", postPOJO.getPostId()));
                                        new WebServiceBase(nameValuePairs, getActivity(), new WebServicesCallBack() {
                                            @Override
                                            public void onGetMsg(String apicall, String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                                                        onBackPressed();
                                                    }
                                                    ToastClass.showShortToast(getActivity().getApplicationContext(), jsonObject.optString("message"));
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
                    iv_like.setImageResource(R.drawable.ic_unlike);
                } else if (postPOJO.getMeLike().equalsIgnoreCase("1")) {
                    iv_like.setImageResource(R.drawable.ic_like);
                }

                tv_like.setText(postPOJO.getTotalLikes());
                tv_comments.setText(postPOJO.getTotalComment());

                ll_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getActivity() instanceof HomeActivity) {
                            HomeActivity homeActivity = (HomeActivity) getActivity();
                            homeActivity.showComment(tv_comments, postPOJO);
                        }
                    }
                });

                ll_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (postPOJO.getMeLike().equalsIgnoreCase("0")) {
                                iv_like.setImageResource(R.drawable.ic_like);
                            } else if (postPOJO.getMeLike().equalsIgnoreCase("1")) {
                                iv_like.setImageResource(R.drawable.ic_unlike);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                        nameValuePairs.add(new BasicNameValuePair("post_id", postPOJO.getPostId()));
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
                        }, "CALL_LIKE_API", false).execute(WebServicesUrls.POST_LIKE);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                if (getActivity() instanceof HomeActivity) {

                    HomeActivity homeActivity = (HomeActivity) getActivity();

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

    public void refreshOnIncomingNotification(String feedId) {
        if (post_id.equalsIgnoreCase(feedId)) {
            getPostDetails(false);
        }
    }
}
