package com.ritvi.kaajneeti.activity.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.Pref;
import com.ritvi.kaajneeti.Util.StringUtils;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.activity.express.ExpressActivity;
import com.ritvi.kaajneeti.activity.loginregistration.LoginActivity;
import com.ritvi.kaajneeti.adapter.ViewPagerAdapter;
import com.ritvi.kaajneeti.fragment.ComplaintDetailFragment;
import com.ritvi.kaajneeti.fragment.NotificationFragment;
import com.ritvi.kaajneeti.fragment.complaint.ComplaintTrackFragment;
import com.ritvi.kaajneeti.fragment.event.EventPreviewFragment;
import com.ritvi.kaajneeti.fragment.feed.CommentFragment;
import com.ritvi.kaajneeti.fragment.feed.ComplaintCommentFragment;
import com.ritvi.kaajneeti.fragment.feed.EventCommentFragment;
import com.ritvi.kaajneeti.fragment.feed.PollCommentFragment;
import com.ritvi.kaajneeti.fragment.home.AllMenuFragment;
import com.ritvi.kaajneeti.fragment.home.AnalyzeFragment;
import com.ritvi.kaajneeti.fragment.home.FavoriteFragment;
import com.ritvi.kaajneeti.fragment.home.HomeFragment;
import com.ritvi.kaajneeti.fragment.home.MyConnectionFragment;
import com.ritvi.kaajneeti.fragment.poll.PollPreviewFragment;
import com.ritvi.kaajneeti.fragment.post.PostViewFragment;
import com.ritvi.kaajneeti.fragment.search.SearchAllFragment;
import com.ritvi.kaajneeti.fragment.user.UserProfileFragment;
import com.ritvi.kaajneeti.fragmentcontroller.ActivityManager;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintPOJO;
import com.ritvi.kaajneeti.pojo.event.EventPOJO;
import com.ritvi.kaajneeti.pojo.poll.PollPOJO;
import com.ritvi.kaajneeti.pojo.post.PostPOJO;
import com.ritvi.kaajneeti.view.CustomViewPager;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends ActivityManager {

    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    @BindView(R.id.iv_home)
    ImageView iv_home;
    @BindView(R.id.iv_connect)
    ImageView iv_connect;
    @BindView(R.id.iv_analyze)
    ImageView iv_analyze;
    @BindView(R.id.iv_favorite)
    ImageView iv_favorite;
    @BindView(R.id.iv_ham)
    ImageView iv_ham;
    @BindView(R.id.ll_sub_menu)
    LinearLayout ll_sub_menu;
    @BindView(R.id.cv_express)
    ImageView cv_express;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.iv_notification)
    ImageView iv_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setupViewPager(viewPager);
        viewPager.setPagingEnabled(false);

        Bundle bundle = new Bundle();
        if (bundle != null) {
            String data = bundle.getString("data");
            Log.d(TagUtils.getTag(), "data:-" + data);
        } else {
            Log.d(TagUtils.getTag(), "data:- bundle empty");
        }


        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        iv_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        iv_analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });
        iv_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
            }
        });
        ll_sub_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(4);
            }
        });

        cv_express.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ExpressActivity.class));
            }
        });
        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addFragmentinFrameHome(new SearchAllFragment(), "SearchFragment");
                startFragment(R.id.frame_home, new SearchAllFragment());
            }
        });

//        ll_sub_menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final PopupMenu menu = new PopupMenu(HomeActivity.this, v);
//
//                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuitem) {
//                        switch (menuitem.getItemId()) {
//                            case R.id.popup_logout:
//                                    showLogoutDialog();
//                                break;
//                        }
//                        return false;
//                    }
//                });
//                menu.inflate(R.menu.home_sub_menu);
//                menu.show();
//            }
//        });

        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragment(R.id.frame_home, new NotificationFragment());
            }
        });
    }

    public void showLogoutDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Logout");
        alertDialog.setMessage("Do you want to logout?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_LOGIN, false);
                Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_PROFILE_COMPLETED, false);
                Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_PROFILE_SKIPPED, false);
                Pref.SetIntPref(getApplicationContext(), StringUtils.USER_TYPE, Constants.USER_TYPE_NONE);
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void setupViewPager(ViewPager viewPager) {

        HomeFragment homeFragment = new HomeFragment();
        final MyConnectionFragment connectFragment = new MyConnectionFragment();
        final AnalyzeFragment analyzeFragment = new AnalyzeFragment();
        final FavoriteFragment favoriteFragment = new FavoriteFragment();
        AllMenuFragment allMenuFragment = new AllMenuFragment();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(homeFragment, "Rural");
        adapter.addFrag(connectFragment, "Urban");
        adapter.addFrag(analyzeFragment, "Urban");
        adapter.addFrag(favoriteFragment, "Urban");
        adapter.addFrag(allMenuFragment, "allmenu");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setFooterTint(position);
                if (position == 1) {
                    connectFragment.initializeFragment();
                } else if (position == 2) {
                    analyzeFragment.initializeFragment();
                } else if (position == 3) {
                    favoriteFragment.initializeFragment();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public void setFooterTint(int position) {

        iv_home.setImageResource(R.drawable.ic_home);
        iv_connect.setImageResource(R.drawable.ic_connect_people);
        iv_analyze.setImageResource(R.drawable.ic_analyze);
        iv_favorite.setImageResource(R.drawable.ic_favorite);
        iv_ham.setImageResource(R.drawable.ic_ham);
        switch (position) {
            case 0:
                iv_home.setImageResource(R.drawable.ic_home_filled);
                break;
            case 1:
                iv_connect.setImageResource(R.drawable.ic_connect_people_filled);
                break;
            case 2:
                iv_analyze.setImageResource(R.drawable.ic_analyze_filled);
                break;
            case 3:
                iv_favorite.setImageResource(R.drawable.ic_favorite_filled);
                break;
            case 4:
                iv_ham.setImageResource(R.drawable.ic_ham_filled);
                break;
        }
    }

    public void replaceFragmentinFrameHome(Fragment fragment, String fragment_name) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_home, fragment)
                .addToBackStack(fragment_name)
                .commit();
    }

    public void addFragmentinFrameHome(Fragment fragment, String fragment_name) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_home, fragment)
                .addToBackStack(fragment_name)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            try {
                String data = intent.getStringExtra("data");
                String type = intent.getStringExtra("type");

                JSONObject jsonObject = new JSONObject(data);
                String feedId = jsonObject.optString("FeedId");

                passNotificationType(type, feedId);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void passNotificationType(String type, String feedId) {
        switch (type) {
            case "post-generated":
            case "post-commented":
            case "post-liked":
            case "post-tagged":
                parsePostGeneratedNotification(feedId);
                break;
            case "complaint-tagged":
            case "complaint-generated":
            case "complaint-liked":
            case "complaint-commented":
            case "complaint-invitation-accepted":
            case "complaint-invitation-rejected":
            case "complaint-accepted":
            case "complaint-declined":
            case "complaint-rejected":
            case "complaint-status-updated":
                parseComplaintNotification(feedId);
                break;
            case "event-generated":
            case "event-tagged":
            case "event-interested":
            case "event-liked":
            case "event-commented":
                parseEventNotification(feedId);
                break;
            case "poll-generated":
            case "poll-tagged":
            case "poll-answered":
            case "poll-liked":
            case "poll-commented":
                parsePollNotification(feedId);
            case "user-connect":
            case "user-favourite":
            case "user-follow":
                parseUserNotification(feedId);
                break;
        }
    }

    public void parseUserNotification(String feed_id) {
        try {
            Bundle bundle = new Bundle();
            bundle.putSerializable("user_profile_id", feed_id);
            UserProfileFragment postViewFragment = new UserProfileFragment();
            postViewFragment.setArguments(bundle);
            startFragment(R.id.frame_home, postViewFragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseEventNotification(String feed_id) {
        try {
            Bundle bundle = new Bundle();
            bundle.putSerializable("event_id", feed_id);
            EventPreviewFragment postViewFragment = new EventPreviewFragment();
            postViewFragment.setArguments(bundle);
            startFragment(R.id.frame_home, postViewFragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parsePollNotification(String feed_id) {
        try {
            Bundle bundle = new Bundle();
            bundle.putSerializable("poll_id", feed_id);
            PollPreviewFragment postViewFragment = new PollPreviewFragment();
            postViewFragment.setArguments(bundle);
            startFragment(R.id.frame_home, postViewFragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parsePostGeneratedNotification(String feed_id) {
        try {
            Bundle bundle = new Bundle();
            bundle.putSerializable("post_id", feed_id);
            PostViewFragment postViewFragment = new PostViewFragment();
            postViewFragment.setArguments(bundle);
            startFragment(R.id.frame_home, postViewFragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseComplaintNotification(String feed_id) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("complaint_id", feed_id);
            ComplaintDetailFragment postViewFragment = new ComplaintDetailFragment();
            postViewFragment.setArguments(bundle);
            startFragment(R.id.frame_home, postViewFragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    TextView tv_comments;

    public void showComment(TextView tv_comments, PostPOJO postPOJO) {
        this.tv_comments = tv_comments;
        CommentFragment commentFragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("post_id", postPOJO.getPostId());
        commentFragment.setArguments(bundle);
        startFragment(R.id.frame_home, commentFragment);
    }

    public void showPollComments(TextView tv_comments, PollPOJO pollPOJO) {
        this.tv_comments = tv_comments;
        PollCommentFragment commentFragment = new PollCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("poll_id", pollPOJO.getPollId());
        commentFragment.setArguments(bundle);
        startFragment(R.id.frame_home, commentFragment);
    }

    public void showEventComment(TextView tv_comments, EventPOJO eventPOJO) {
        this.tv_comments = tv_comments;
        EventCommentFragment commentFragment = new EventCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("event_id", eventPOJO.getEventId());
        commentFragment.setArguments(bundle);
        startFragment(R.id.frame_home, commentFragment);
    }

    public void showComplaintComments(TextView tv_comments, ComplaintPOJO complaintPOJO) {
        this.tv_comments = tv_comments;
        ComplaintCommentFragment commentFragment = new ComplaintCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("complaint_id", complaintPOJO.getComplaintId());
        commentFragment.setArguments(bundle);
        startFragment(R.id.frame_home, commentFragment);
    }

    public void setCommentCount(String value) {
        try {
            tv_comments.setText(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getApplicationContext().registerReceiver(mMessageReceiver, new IntentFilter(StringUtils.UPDATE_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        getApplicationContext().unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String type = intent.getStringExtra("type");
                JSONObject jsonObject = new JSONObject(intent.getStringExtra("data"));
                String feedId = jsonObject.optString("FeedId");
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    if (fragment instanceof NotificationFragment) {
                        NotificationFragment notificationFragment = (NotificationFragment) fragment;
                        notificationFragment.getALlNotifications(false);
                    }
                    if (checkNotificationType(type,new String[]{"poll-generated","poll-tagged","poll-answered","poll-liked","poll-commented"})
                            && fragment instanceof PollPreviewFragment) {
                        PollPreviewFragment pollPreviewFragment = (PollPreviewFragment) fragment;
                        pollPreviewFragment.refreshOnIncomingNotification(feedId);
                    }
                    if (checkNotificationType(type,new String[]{"complaint-tagged","complaint-generated","complaint-liked","complaint-commented",
                            "complaint-invitation-accepted","complaint-invitation-rejected","complaint-accepted","complaint-declined","complaint-rejected","complaint-status-updated"})) {
                        if(type.equalsIgnoreCase("complaint-status-updated")&&fragment instanceof ComplaintTrackFragment){
                            ComplaintTrackFragment complaintTrackFragment= (ComplaintTrackFragment) fragment;
                            complaintTrackFragment.refreshOnIncomingNotification(feedId);
                        }else if(fragment instanceof ComplaintDetailFragment){
                            ComplaintDetailFragment complaintDetailFragment = (ComplaintDetailFragment) fragment;
                            complaintDetailFragment.refreshOnIncomingNotification(feedId);
                        }
                    }
                    if (checkNotificationType(type,new String[]{"post-generated","post-commented","post-liked","post-tagged"})
                            && fragment instanceof PostViewFragment) {
                        PostViewFragment complaintDetailFragment = (PostViewFragment) fragment;
                        complaintDetailFragment.refreshOnIncomingNotification(feedId);
                    }
                    if (checkNotificationType(type,new String[]{"event-generated","event-tagged","event-interested","event-liked","event-commented"})
                            && fragment instanceof EventPreviewFragment) {
                        EventPreviewFragment complaintDetailFragment = (EventPreviewFragment) fragment;
                        complaintDetailFragment.refreshOnIncomingNotification(feedId);
                    }
                    if (checkNotificationType(type,new String[]{"user-connect","user-favourite","user-follow"})
                            && fragment instanceof UserProfileFragment) {
                        UserProfileFragment userProfileFragment = (UserProfileFragment) fragment;
                        userProfileFragment.refreshOnIncomingNotification(feedId);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public boolean checkNotificationType(String type, String[] types) {
        Log.d(TagUtils.getTag(),"type:-"+type);
        for (String s : types) {
//            Log.d(TagUtils.getTag(),"types:-"+s);
            if (s.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

}
