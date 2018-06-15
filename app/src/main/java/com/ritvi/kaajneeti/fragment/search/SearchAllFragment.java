package com.ritvi.kaajneeti.fragment.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.adapter.FriendAdapter;
import com.ritvi.kaajneeti.adapter.HomeFeedAdapter;
import com.ritvi.kaajneeti.adapter.TagSearchAdapter;
import com.ritvi.kaajneeti.fragment.analyze.AllComplaintFragment;
import com.ritvi.kaajneeti.fragment.analyze.AllEventFragment;
import com.ritvi.kaajneeti.fragment.analyze.AllInformationFragment;
import com.ritvi.kaajneeti.fragment.analyze.AllPollFragment;
import com.ritvi.kaajneeti.fragment.analyze.AllPostFragment;
import com.ritvi.kaajneeti.fragment.analyze.AllSuggestionFragment;
import com.ritvi.kaajneeti.fragment.myconnection.FriendFragment;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;
import com.ritvi.kaajneeti.pojo.search.AllSearchPOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchAllFragment extends FragmentController {

    @BindView(R.id.rv_tags)
    RecyclerView rv_tags;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.iv_search_close)
    ImageView iv_search_close;
    @BindView(R.id.ll_people)
    LinearLayout ll_people;
    @BindView(R.id.rv_people)
    RecyclerView rv_people;
    @BindView(R.id.rv_posts)
    RecyclerView rv_posts;
    @BindView(R.id.rv_events)
    RecyclerView rv_events;
    @BindView(R.id.tv_all_people)
    TextView tv_all_people;
    @BindView(R.id.tv_all_posts)
    TextView tv_all_posts;
    @BindView(R.id.tv_all_events)
    TextView tv_all_events;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.cv_people)
    CardView cv_people;
    @BindView(R.id.cv_post)
    CardView cv_post;
    @BindView(R.id.cv_event)
    CardView cv_event;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_search_all, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        attachAdapter();
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                callSearchAPI(et_search.getText().toString());
            }
        });

        tv_all_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendFragment friendFragment= new FriendFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.IS_SEARCH, true);
                bundle.putString(Constants.SEARCH_TEXT, et_search.getText().toString());
                friendFragment.setArguments(bundle);

                activityManager.startFragment(R.id.frame_home, friendFragment);
            }
        });

        tv_all_posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllPostFragment allPostFragment = new AllPostFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.IS_SEARCH, true);
                bundle.putString(Constants.SEARCH_TEXT, et_search.getText().toString());
                allPostFragment.setArguments(bundle);

                activityManager.startFragment(R.id.frame_home, allPostFragment);
            }
        });

        tv_all_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AllEventFragment allEventFragment = new AllEventFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.IS_SEARCH, true);
                bundle.putString(Constants.SEARCH_TEXT, et_search.getText().toString());
                allEventFragment.setArguments(bundle);

                activityManager.startFragment(R.id.frame_home, allEventFragment);
            }
        });
    }

    AllSearchPOJO allSearchPOJO;

    public void callSearchAPI(String key) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("q", key));
        nameValuePairs.add(new BasicNameValuePair("start", "0"));
        nameValuePairs.add(new BasicNameValuePair("end", "10"));
        nameValuePairs.add(new BasicNameValuePair("search_in", "all"));
        new WebServiceBaseResponse<AllSearchPOJO>(nameValuePairs, getActivity(), new ResponseCallBack<AllSearchPOJO>() {

            @Override
            public void onGetMsg(ResponsePOJO<AllSearchPOJO> responsePOJO) {
                if (responsePOJO.isSuccess()) {
                    SearchAllFragment.this.allSearchPOJO = responsePOJO.getResult();
                    showData(responsePOJO.getResult());
                } else {
                    ToastClass.showShortToast(getActivity().getApplicationContext(), responsePOJO.getMessage());
                }
            }
        }, AllSearchPOJO.class, "ALL_SEARCH_API", false).execute(WebServicesUrls.ALL_SEARCH_API);
    }


    public void showData(AllSearchPOJO allSearchPOJO) {
        if (allSearchPOJO != null && allSearchPOJO.getProfileFeeds() != null && allSearchPOJO.getProfileFeeds().size() > 0) {

            List<FeedPOJO> profileFeedPOJOs = new ArrayList<>();
            if (allSearchPOJO.getProfileFeeds().size() > 0) {
                cv_people.setVisibility(View.VISIBLE);
                if (allSearchPOJO.getProfileFeeds().size() >= 5) {
                    for (int i = 0; i < 5; i++) {
                        FeedPOJO feedPOJO = allSearchPOJO.getProfileFeeds().get(i);
                        if (feedPOJO.getProfiledata() != null) {
                            profileFeedPOJOs.add(feedPOJO);
                        }
                    }
                } else {
                    profileFeedPOJOs.addAll(allSearchPOJO.getProfileFeeds());
                }
            } else {
                //hide view
                cv_people.setVisibility(View.GONE);
            }

            List<FeedPOJO> postFeedPOJOs = new ArrayList<>();
            if (allSearchPOJO.getPostFeeds().size() > 0) {
                cv_post.setVisibility(View.VISIBLE);
                if (allSearchPOJO.getPostFeeds().size() >= 5) {
                    for (int i = 0; i < 5; i++) {
                        postFeedPOJOs.add(allSearchPOJO.getPostFeeds().get(i));
                    }
                } else {
                    postFeedPOJOs.addAll(allSearchPOJO.getPostFeeds());
                }
            } else {
                cv_post.setVisibility(View.GONE);
            }

            List<FeedPOJO> eventsFeedPOJOs = new ArrayList<>();
            if (allSearchPOJO.getEventFeeds().size() > 0) {
                cv_event.setVisibility(View.VISIBLE);
                if (allSearchPOJO.getEventFeeds().size() >= 5) {
                    for (int i = 0; i < 5; i++) {
                        eventsFeedPOJOs.add(allSearchPOJO.getEventFeeds().get(i));
                    }
                } else {
                    eventsFeedPOJOs.addAll(allSearchPOJO.getEventFeeds());
                }
            } else {
                cv_event.setVisibility(View.GONE);
            }

            attachProfileRecycler(profileFeedPOJOs);
            attachRecyclerView(rv_posts, postFeedPOJOs);
            attachRecyclerView(rv_events, eventsFeedPOJOs);
        } else {
            cv_people.setVisibility(View.GONE);
            cv_post.setVisibility(View.GONE);
            cv_event.setVisibility(View.GONE);
        }
    }

    public void attachProfileRecycler(List<FeedPOJO> feedPOJOS) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_people.setLayoutManager(linearLayoutManager);
        HomeFeedAdapter sentRequestAdapter = new HomeFeedAdapter(rv_people,getActivity(), this, feedPOJOS,getChildFragmentManager());
        rv_people.setHasFixedSize(true);
        rv_people.setAdapter(sentRequestAdapter);
        rv_people.setNestedScrollingEnabled(false);
        rv_people.setItemAnimator(new DefaultItemAnimator());
    }

    public void attachRecyclerView(RecyclerView recyclerView, List<FeedPOJO> feedPOJOS) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        HomeFeedAdapter homeFeedAdapter = new HomeFeedAdapter(recyclerView, getActivity(), this, feedPOJOS, getChildFragmentManager());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(homeFeedAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    List<String> tagsStrings = new ArrayList<>();
    TagSearchAdapter tagSearchAdapter;

    public void attachAdapter() {

        tagsStrings.add(Constants.SEARCH_IN_PEOPLE);
        tagsStrings.add(Constants.SEARCH_IN_POST);
        tagsStrings.add(Constants.SEARCH_IN_EVENT);
        tagsStrings.add(Constants.SEARCH_IN_POLL);
        tagsStrings.add(Constants.SEARCH_IN_COMPLAINT);
        tagsStrings.add(Constants.SEARCH_IN_SUGGESTION);
        tagsStrings.add(Constants.SEARCH_IN_INFORMATION);

        tagSearchAdapter = new TagSearchAdapter(getActivity(), this, tagsStrings);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_tags.setHasFixedSize(true);
        rv_tags.setAdapter(tagSearchAdapter);
        rv_tags.setLayoutManager(linearLayoutManager);
        rv_tags.setItemAnimator(new DefaultItemAnimator());
    }


    public void showPosts(String search_in) {
        if (getActivity() instanceof HomeActivity) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.IS_SEARCH, true);
            bundle.putString(Constants.SEARCH_TEXT, et_search.getText().toString());
            switch (search_in.toLowerCase()) {
                case Constants.SEARCH_IN_POST:
                    AllPostFragment allPostFragment = new AllPostFragment();
                    allPostFragment.setArguments(bundle);
                    activityManager.startFragment(R.id.frame_home, allPostFragment);
                    break;
                case Constants.SEARCH_IN_PEOPLE:
                    FriendFragment friendFragment= new FriendFragment();
                    friendFragment.setArguments(bundle);
                    activityManager.startFragment(R.id.frame_home, friendFragment);
                    break;
                case Constants.SEARCH_IN_EVENT:
                    AllEventFragment allEventFragment = new AllEventFragment();
                    allEventFragment.setArguments(bundle);
                    activityManager.startFragment(R.id.frame_home, allEventFragment);
                    break;
                case Constants.SEARCH_IN_POLL:
                    AllPollFragment allPollFragment = new AllPollFragment();
                    allPollFragment.setArguments(bundle);
                    activityManager.startFragment(R.id.frame_home, allPollFragment);
                    break;
                case Constants.SEARCH_IN_COMPLAINT:
                    AllComplaintFragment allComplaintFragment = new AllComplaintFragment();
                    allComplaintFragment.setArguments(bundle);
                    activityManager.startFragment(R.id.frame_home, allComplaintFragment);
                    break;
                case Constants.SEARCH_IN_SUGGESTION:
                    AllSuggestionFragment allSuggestionFragment = new AllSuggestionFragment();
                    allSuggestionFragment.setArguments(bundle);
                    activityManager.startFragment(R.id.frame_home, allSuggestionFragment);
                    break;
                case Constants.SEARCH_IN_INFORMATION:
                    AllInformationFragment allInformationFragment = new AllInformationFragment();
                    allInformationFragment.setArguments(bundle);
                    activityManager.startFragment(R.id.frame_home, allInformationFragment);
                    break;
            }
        }
    }
}
