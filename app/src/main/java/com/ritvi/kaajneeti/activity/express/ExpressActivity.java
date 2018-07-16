package com.ritvi.kaajneeti.activity.express;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.GPSTracker;
import com.ritvi.kaajneeti.Util.Pref;
import com.ritvi.kaajneeti.Util.StringUtils;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.adapter.AttachMediaAdapter;
import com.ritvi.kaajneeti.fragment.Express.CreateComplaintFragment;
import com.ritvi.kaajneeti.fragment.Express.CreateEventFragment;
import com.ritvi.kaajneeti.fragment.Express.CreateInformationFragment;
import com.ritvi.kaajneeti.fragment.Express.CreatePollFragment;
import com.ritvi.kaajneeti.fragment.Express.CreateSuggestionFragment;
import com.ritvi.kaajneeti.fragmentcontroller.ActivityManager;
import com.ritvi.kaajneeti.interfaces.ItemSizeChangeListener;
import com.ritvi.kaajneeti.pojo.allfeeds.MediaPOJO;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintPOJO;
import com.ritvi.kaajneeti.pojo.event.EventPOJO;
import com.ritvi.kaajneeti.pojo.location.GeometryPOJO;
import com.ritvi.kaajneeti.pojo.location.LatLongPOJO;
import com.ritvi.kaajneeti.pojo.location.LocationPOJO;
import com.ritvi.kaajneeti.pojo.poll.PollPOJO;
import com.ritvi.kaajneeti.pojo.post.PostAttachmentPOJO;
import com.ritvi.kaajneeti.pojo.post.PostPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;
import com.ritvi.kaajneeti.webservice.WebUploadService;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ExpressActivity extends ActivityManager {

    @BindView(R.id.iv_tag)
    ImageView iv_tag;
    @BindView(R.id.cv_profile_pic)
    CircleImageView cv_profile_pic;
    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.iv_camera)
    ImageView iv_camera;
    @BindView(R.id.iv_location)
    ImageView iv_location;
    @BindView(R.id.ll_complaint)
    LinearLayout ll_complaint;
    @BindView(R.id.ll_suggestion)
    LinearLayout ll_suggestion;
    @BindView(R.id.ll_event)
    LinearLayout ll_event;
    @BindView(R.id.ll_poll)
    LinearLayout ll_poll;
    @BindView(R.id.ll_information)
    LinearLayout ll_information;
    @BindView(R.id.rv_media)
    RecyclerView rv_media;
    @BindView(R.id.spinner_pubpri)
    Spinner spinner_pubpri;
    @BindView(R.id.tv_post)
    TextView tv_post;
    @BindView(R.id.et_whats_new)
    TextView et_whats_new;
    List<UserProfilePOJO> taggeduserInfoPOJOS = new ArrayList<>();

    String tagging_description = "";
    String profile_description = "";
    String place_description = "";

    LocationPOJO locationPOJO;
    PostPOJO postPOJO;
    EventPOJO eventPOJO;
    PollPOJO pollPOJO;
    ComplaintPOJO complaintPOJO;
    List<String> deleteServerFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);
        ButterKnife.bind(this);

        postPOJO = (PostPOJO) getIntent().getSerializableExtra("post");
        eventPOJO = (EventPOJO) getIntent().getSerializableExtra("eventPOJO");
        complaintPOJO = (ComplaintPOJO) getIntent().getSerializableExtra("complaintPOJO");
        pollPOJO = (PollPOJO) getIntent().getSerializableExtra("pollPOJO");

        if (eventPOJO != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("eventPOJO", eventPOJO);
            CreateEventFragment createEventFragment = new CreateEventFragment();
            createEventFragment.setArguments(bundle);
            startFragment(R.id.frame_main, createEventFragment);
        }

        if (complaintPOJO != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("complaintPOJO", complaintPOJO);
            CreateComplaintFragment createComplaintFragment = new CreateComplaintFragment();
            createComplaintFragment.setArguments(bundle);
            startFragment(R.id.frame_main, createComplaintFragment);
        }

        if (pollPOJO != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("pollPOJO", pollPOJO);
            CreatePollFragment createPollFragment = new CreatePollFragment();
            createPollFragment.setArguments(bundle);
            startFragment(R.id.frame_main, createPollFragment);
        }

        setupPubPrivSpinner();

        profile_description = "<b>" + Constants.userProfilePOJO.getFirstName() + " " + Constants.userProfilePOJO.getLastName() + "</b> ";

        Glide.with(getApplicationContext())
                .load(Constants.userProfilePOJO.getProfilePhotoPath())
                .placeholder(R.drawable.ic_default_profile_pic)
                .error(R.drawable.ic_default_profile_pic)
                .dontAnimate()
                .into(cv_profile_pic);

        updateProfileStatus();
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iv_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ExpressActivity.this, TagPeopleActivity.class).putExtra("taggedpeople", (Serializable) taggeduserInfoPOJOS), Constants.ACTIVITY_TAG_PEOPLE);
            }
        });

        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocation();
            }
        });
        ll_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createComplaintFragment();
            }
        });
        ll_poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creatPollFragment();
            }
        });
        ll_suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSuggestionFragment();
            }
        });
        ll_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEventFragment();
            }
        });
        ll_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInformation();
            }
        });

        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateGalleryPicker();
            }
        });

        attachAdapter();

        tv_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePost();
            }
        });

        if (postPOJO != null) {
            if (postPOJO.getPostTag() != null && postPOJO.getPostTag().size() > 0) {
                taggeduserInfoPOJOS.addAll(postPOJO.getPostTag());
                tagging_description = getTaggedDescription(taggeduserInfoPOJOS);
                updateProfileStatus();
            }
            if (postPOJO.getPostAttachment() != null && postPOJO.getPostAttachment().size() > 0) {
                for (PostAttachmentPOJO postAttachmentPOJO : postPOJO.getPostAttachment()) {
                    MediaPOJO mediaPOJO = new MediaPOJO();
                    mediaPOJO.setPath(postAttachmentPOJO.getAttachmentFile());
                    mediaPOJO.setIs_server(true);
                    mediaPOJO.setId(postAttachmentPOJO.getPostAttachmentId());
                    attachPathString.add(mediaPOJO);
                }
                attachMediaAdapter.notifyDataSetChanged();
            }

            if (postPOJO.getPostPrivacy().equalsIgnoreCase("1")) {
                spinner_pubpri.setSelection(0);
            } else {
                spinner_pubpri.setSelection(1);
            }

            et_whats_new.setText(postPOJO.getPostTitle());

            if (postPOJO.getServerLocationPOJO() != null) {
                LocationPOJO locationPOJO = new LocationPOJO();
                locationPOJO.setPlaceId(postPOJO.getServerLocationPOJO().getPlaceId());
                locationPOJO.setFormatted_address(postPOJO.getServerLocationPOJO().getLocationAddress());
                locationPOJO.setUrl(postPOJO.getServerLocationPOJO().getLocationUrl());
                locationPOJO.setAdr_address(postPOJO.getServerLocationPOJO().getLocationAddress());
                locationPOJO.setVicinity(postPOJO.getServerLocationPOJO().getLocationVicinity());

                GeometryPOJO geometryPOJO = new GeometryPOJO();
                LatLongPOJO latLongPOJO = new LatLongPOJO();
                latLongPOJO.setLat(UtilityFunction.getFormattedValue(postPOJO.getServerLocationPOJO().getLocationLattitude()));
                latLongPOJO.setLng(UtilityFunction.getFormattedValue(postPOJO.getServerLocationPOJO().getLocationLongitude()));

                geometryPOJO.setLocation(latLongPOJO);
                locationPOJO.setGeometry(geometryPOJO);
                this.locationPOJO = locationPOJO;

                place_description = " - at <b>" + locationPOJO.getFormatted_address() + "</b>";
                updateProfileStatus();
            }
        }

        checkPostStatus();
        et_whats_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkPostStatus();
            }
        });

    }

    public void checkPostStatus() {
        boolean enable_post = false;
        if (taggeduserInfoPOJOS != null && taggeduserInfoPOJOS.size() > 0) {
            enable_post = true;
        }

        if (attachPathString.size() > 0) {
            enable_post = true;
        }

        if (locationPOJO != null) {
            enable_post = true;
        }

        if (et_whats_new.getText().toString().length() > 0) {
            enable_post = true;
        }

        if (enable_post) {
            tv_post.setEnabled(true);
            tv_post.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            tv_post.setEnabled(false);
            tv_post.setTextColor(Color.parseColor("#90FFFFFF"));
        }

    }


    public void setupPubPrivSpinner() {
        List<String> pubpriList = new ArrayList<>();

        pubpriList.add("Public");
        pubpriList.add("Private");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.dropsimpledown, pubpriList);
        spinner_pubpri.setAdapter(spinnerArrayAdapter);
    }

    public void initiateGalleryPicker() {
        //0=camera,1=Gallery,2=Camera and gallery
        new ImagePicker.Builder(this)
                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .allowMultipleImages(true)
                .enableDebuggingMode(true)
                .build();
    }

    AttachMediaAdapter attachMediaAdapter;
    List<MediaPOJO> attachPathString = new ArrayList<>();

    public void attachAdapter() {
        attachMediaAdapter = new AttachMediaAdapter(this, null, attachPathString);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_media.setHasFixedSize(true);
        rv_media.setAdapter(attachMediaAdapter);
        rv_media.setLayoutManager(linearLayoutManager);
        rv_media.setItemAnimator(new DefaultItemAnimator());

        attachMediaAdapter.setOnItemChanged(new ItemSizeChangeListener() {
            @Override
            public void onItemSizeChanged() {
                checkPostStatus();
            }
        });

    }

    public void createComplaintFragment() {
        CreateComplaintFragment createComplaintFragment = new CreateComplaintFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frame_main, createComplaintFragment, "createComplaintFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void creatPollFragment() {
        CreatePollFragment createPollFragment = new CreatePollFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frame_main, createPollFragment, "createPollFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void createSuggestionFragment() {
        CreateSuggestionFragment createSuggestionFragment = new CreateSuggestionFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frame_main, createSuggestionFragment, "createSuggestionFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void createEventFragment() {
        CreateEventFragment createEventFragment = new CreateEventFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frame_main, createEventFragment, "createEventFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void createInformation() {
        CreateInformationFragment createInformationFragment = new CreateInformationFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frame_main, createInformationFragment, "createInformationFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void checkLocation() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        } else {
            getLocation();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.ACCESS_LOCATION);
            return;
        } else {
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.ACCESS_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {

                }
                return;
            }

        }
    }

    GPSTracker gps;

    public void getLocation() {
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Log.d(TagUtils.getTag(), "location:-latitude:-" + latitude);
            Log.d(TagUtils.getTag(), "location:-longitude:-" + longitude);

            Pref.SetStringPref(getApplicationContext(), StringUtils.CURRENT_LATITUDE, String.valueOf(latitude));
            Pref.SetStringPref(getApplicationContext(), StringUtils.CURRENT_LONGITUDE, String.valueOf(longitude));
        } else {
            gps.showSettingsAlert();
        }

        startActivityForResult(new Intent(this, CheckInActivity.class), Constants.ACTIVITY_LOCATION);
    }

    public boolean isImagePresent(String path) {
        for (MediaPOJO mediaPOJO : attachPathString) {
            if (mediaPOJO.getPath().equalsIgnoreCase(path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean isFragAvailable = false;
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null) {
                isFragAvailable = true;
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
        Log.d(TagUtils.getTag(), "frag available:-" + isFragAvailable);
        if (!isFragAvailable) {
            if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
                List<String> mPaths = (List<String>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PATH);
                if (mPaths.size() > 0) {
                    for (String path : mPaths) {
                        if (!isImagePresent(path)) {
                            MediaPOJO mediaPOJO = new MediaPOJO();
                            mediaPOJO.setPath(path);
                            attachPathString.add(mediaPOJO);
                        }
                    }
                    attachMediaAdapter.notifyDataSetChanged();
                }

            } else if (requestCode == Constants.ACTIVITY_TAG_PEOPLE) {
                if (resultCode == Activity.RESULT_OK) {
                    taggeduserInfoPOJOS = (List<UserProfilePOJO>) data.getSerializableExtra("taggedpeople");
                    tagging_description = getTaggedDescription(taggeduserInfoPOJOS);
                    updateProfileStatus();
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
            } else if (requestCode == Constants.ACTIVITY_LOCATION) {
                if (resultCode == Activity.RESULT_OK) {
                    locationPOJO = (LocationPOJO) data.getSerializableExtra("location");
                    place_description = " - at <b>" + locationPOJO.getFormatted_address() + "</b>";
                    updateProfileStatus();
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                    locationPOJO = null;
                }
            }
        }
        checkPostStatus();
    }


    public void updateProfileStatus() {
        String profile = "";
        if (tagging_description.length() == 0 && place_description.length() == 0) {
            profile = profile_description;
        } else {
            profile = profile_description + " is " + tagging_description + place_description;
        }
        tv_profile_name.setText(Html.fromHtml(profile));
    }


    public String getTaggedDescription(List<UserProfilePOJO> stringList) {
        String description = "";
        if (stringList.size() == 1) {
            UserProfilePOJO userProfilePOJO = stringList.get(0);
            description = " with <b>" + userProfilePOJO.getFirstName() + " " + userProfilePOJO.getLastName() + "</b> ";
        } else if (stringList.size() == 2) {
            UserProfilePOJO userProfilePOJO1 = stringList.get(0);
            UserProfilePOJO userProfilePOJO2 = stringList.get(1);
            description = " with <b>" + userProfilePOJO1.getFirstName() + " " + userProfilePOJO1.getLastName() + "</b> and <b>" +
                    userProfilePOJO2.getFirstName() + " " + userProfilePOJO2.getLastName() + "</b>";
        } else if (stringList.size() > 2) {
            UserProfilePOJO userProfilePOJO = stringList.get(0);
            description = " with <b>" + userProfilePOJO.getFirstName() + " " + userProfilePOJO.getLastName() + "</b> and <b>" + (stringList.size() - 1) + " others";
        }
        return description;
    }

    public void savePost() {
        try {

            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            String activity = "";

            reqEntity.addPart("user_profile_id", new StringBody(Constants.userProfilePOJO.getUserProfileId()));
            reqEntity.addPart("title", new StringBody(et_whats_new.getText().toString()));

            if (locationPOJO != null) {
                try {
                    reqEntity.addPart("location_detail[place_id]", new StringBody(locationPOJO.getPlaceId()));
                    reqEntity.addPart("location_detail[location_name]", new StringBody(locationPOJO.getFormatted_address()));
                    reqEntity.addPart("location_detail[location_lant]", new StringBody(String.valueOf(locationPOJO.getGeometry().getLocation().getLat())));
                    reqEntity.addPart("location_detail[location_long]", new StringBody(String.valueOf(locationPOJO.getGeometry().getLocation().getLng())));
                    reqEntity.addPart("location_detail[location_url]", new StringBody(locationPOJO.getUrl()));
                    reqEntity.addPart("location_detail[location_address]", new StringBody(locationPOJO.getAdr_address()));
                    reqEntity.addPart("location_detail[location_vicinity]", new StringBody(locationPOJO.getVicinity()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            reqEntity.addPart("description", new StringBody(et_whats_new.getText().toString()));
            reqEntity.addPart("url", new StringBody(""));
            reqEntity.addPart("feeling", new StringBody(activity));

            for (int i = 0; i < taggeduserInfoPOJOS.size(); i++) {
                reqEntity.addPart("post_tag[" + i + "]", new StringBody(taggeduserInfoPOJOS.get(i).getUserProfileId()));
            }

            int count = 0;
            if (spinner_pubpri.getSelectedItemPosition() == 0) {
                reqEntity.addPart("privacy", new StringBody("1"));
            } else {
                reqEntity.addPart("privacy", new StringBody("0"));
            }

            for (MediaPOJO mediaPOJO : attachPathString) {
                if (!mediaPOJO.isIs_server()) {
                    if (mediaPOJO.getPath().contains(".mp4")
                            || mediaPOJO.getPath().contains(".MP4")) {
                        reqEntity.addPart("file[" + (count) + "]", new FileBody(new File(mediaPOJO.getPath())));
//                    reqEntity.addPart("thumb[" + (count) + "]", new FileBody(new File(UtilityFunction.saveThumbFile(new File(file_path)))));
                    } else {
                        reqEntity.addPart("file[" + (count) + "]", new FileBody(new File(mediaPOJO.getPath())));
                        reqEntity.addPart("thumb[" + (count) + "]", new StringBody(""));
                    }
                    count++;
                }
            }
            String url = "";
            if (postPOJO != null) {
                reqEntity.addPart("post_id", new StringBody(postPOJO.getPostId()));
                reqEntity.addPart("status", new StringBody("1"));
                url = WebServicesUrls.UPDATE_POST;
            } else {
                url = WebServicesUrls.SAVE_POST;
            }

            if (deleteServerFiles.size() > 0) {
                int delete_count = 0;
                for (String str : deleteServerFiles) {
                    reqEntity.addPart("delete_image[" + delete_count + "]", new StringBody(str));
                    delete_count++;
                }
            }

            printData();
            new WebUploadService(reqEntity, this, new WebServicesCallBack() {
                @Override
                public void onGetMsg(String apicall, String response) {
                    Log.d(TagUtils.getTag(), apicall + " :- " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        ToastClass.showShortToast(getApplicationContext(), jsonObject.optString("message"));
                        if (jsonObject.optString("status").equals("success")) {
                            ToastClass.showShortToast(getApplicationContext(), "Posted Successfully");
                            startActivity(new Intent(ExpressActivity.this, HomeActivity.class));
                            finishAffinity();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "CREATE_POST", true).execute(url);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public void printData() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("title", et_whats_new.getText().toString()));

        if (locationPOJO != null) {
            try {
                nameValuePairs.add(new BasicNameValuePair("location_detail[place_id]", locationPOJO.getPlaceId()));
                nameValuePairs.add(new BasicNameValuePair("location_detail[location_name]", locationPOJO.getFormatted_address()));
                nameValuePairs.add(new BasicNameValuePair("location_detail[location_lant]", String.valueOf(locationPOJO.getGeometry().getLocation().getLat())));
                nameValuePairs.add(new BasicNameValuePair("location_detail[location_long]", String.valueOf(locationPOJO.getGeometry().getLocation().getLng())));
                nameValuePairs.add(new BasicNameValuePair("location_detail[location_url]", locationPOJO.getUrl()));
                nameValuePairs.add(new BasicNameValuePair("location_detail[location_address]", locationPOJO.getAdr_address()));
                nameValuePairs.add(new BasicNameValuePair("location_detail[location_vicinity]", locationPOJO.getVicinity()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        nameValuePairs.add(new BasicNameValuePair("description", et_whats_new.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("url", ""));

        for (int i = 0; i < taggeduserInfoPOJOS.size(); i++) {
            nameValuePairs.add(new BasicNameValuePair("post_tag[" + i + "]", taggeduserInfoPOJOS.get(i).getUserProfileId()));
        }

        int count = 0;
        if (spinner_pubpri.getSelectedItemPosition() == 0) {
            nameValuePairs.add(new BasicNameValuePair("privacy", "1"));
        } else {
            nameValuePairs.add(new BasicNameValuePair("privacy", "0"));
        }

        for (MediaPOJO mediaPOJO : attachPathString) {
            if (mediaPOJO.getPath().contains(".mp4")
                    || mediaPOJO.getPath().contains(".MP4")) {
                nameValuePairs.add(new BasicNameValuePair("file[" + (count) + "]", mediaPOJO.getPath()));
//                nameValuePairs.add(new BasicNameValuePair("thumb[" + (count) + "]", UtilityFunction.saveThumbFile(new File(file_path))));
            } else {
                nameValuePairs.add(new BasicNameValuePair("file[" + (count) + "]", mediaPOJO.getPath()));
                nameValuePairs.add(new BasicNameValuePair("thumb[" + (count) + "]", ""));
            }
            count++;
        }

        if (deleteServerFiles.size() > 0) {
            int delete_count = 0;
            for (String str : deleteServerFiles) {
                nameValuePairs.add(new BasicNameValuePair("delete_image[" + delete_count + "]", str));
                delete_count++;
            }
        }

        String nmv = "";
        for (NameValuePair nameValuePair : nameValuePairs) {
            nmv = nmv + nameValuePair.getName() + " : " + nameValuePair.getValue() + "\n";
        }
        Log.d(TagUtils.getTag(), "nmv:-" + nmv);
    }

    public void removePosition(MediaPOJO mediaPOJO, int position) {
        if (mediaPOJO.isIs_server()) {
            deleteServerFiles.add(mediaPOJO.getId());
        }
        attachPathString.remove(position);
        attachMediaAdapter.notifyDataSetChanged();
    }
}
