package com.ritvi.kaajneeti.activity.express;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.GPSTracker;
import com.ritvi.kaajneeti.Util.Pref;
import com.ritvi.kaajneeti.Util.StringUtils;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.fragment.Express.CreateComplaintFragment;
import com.ritvi.kaajneeti.fragment.Express.CreateEventFragment;
import com.ritvi.kaajneeti.fragment.Express.CreatePollFragment;
import com.ritvi.kaajneeti.fragment.Express.CreateSuggestionFragment;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ExpressActivity extends AppCompatActivity {

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
    List<UserProfilePOJO> taggeduserInfoPOJOS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);
        ButterKnife.bind(this);

        Glide.with(getApplicationContext())
                .load(Constants.userProfilePOJO.getProfilePhotoPath())
                .placeholder(R.drawable.ic_default_profile_pic)
                .error(R.drawable.ic_default_profile_pic)
                .dontAnimate()
                .into(cv_profile_pic);

        tv_profile_name.setText(Constants.userProfilePOJO.getFirstName()+" "+Constants.userProfilePOJO.getLastName());

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
//                startActivity(new Intent(ExpressActivity.this,CheckInActivity.class));
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

        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateGalleryPicker();
            }
        });
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

        startActivity(new Intent(this, CheckInActivity.class));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
