package com.ritvi.kaajneeti.fragment.Express;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.activity.express.CheckInActivity;
import com.ritvi.kaajneeti.activity.express.TagPeopleActivity;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.activity.user.SelectFavoriteLeaderActivity;
import com.ritvi.kaajneeti.adapter.AttachMediaAdapter;
import com.ritvi.kaajneeti.pojo.allfeeds.MediaPOJO;
import com.ritvi.kaajneeti.pojo.location.LocationPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;
import com.ritvi.kaajneeti.webservice.WebUploadService;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CreateInformationFragment extends Fragment {

    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.frame_fav_leader)
    FrameLayout frame_fav_leader;
    @BindView(R.id.iv_camera)
    ImageView iv_camera;
    @BindView(R.id.iv_tag)
    ImageView iv_tag;
    @BindView(R.id.iv_location)
    ImageView iv_location;
    @BindView(R.id.rv_media)
    RecyclerView rv_media;
    @BindView(R.id.et_subject)
    EditText et_subject;
    @BindView(R.id.spinner_pubpri)
    Spinner spinner_pubpri;
    @BindView(R.id.tv_leader_name)
    TextView tv_leader_name;
    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @BindView(R.id.tv_post)
    TextView tv_post;
    @BindView(R.id.et_description)
    EditText et_description;
    @BindView(R.id.cv_profile_pic)
    CircleImageView cv_profile_pic;

    UserProfilePOJO leaderUserProfilePOJO;
    LocationPOJO locationPOJO;
    List<UserProfilePOJO> taggeduserInfoPOJOS = new ArrayList<>();

    String tagging_description = "";
    String profile_description = "";
    String place_description = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_create_information, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        attachMediaAdapter();
        setupPubPrivSpinner();

        profile_description = "<b>" + Constants.userProfilePOJO.getFirstName() + " " + Constants.userProfilePOJO.getLastName() + "</b> ";
        updateProfileStatus();

        Glide.with(getActivity().getApplicationContext())
                .load(Constants.userProfilePOJO.getProfilePhotoPath())
                .error(R.drawable.ic_default_profile_pic)
                .placeholder(R.drawable.ic_default_profile_pic)
                .dontAnimate()
                .into(cv_profile_pic);


        frame_fav_leader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SelectFavoriteLeaderActivity.class);
                startActivityForResult(i, Constants.ACTIVITY_SELECT_LEADER);
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateGalleryPicker();
            }
        });
        iv_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), TagPeopleActivity.class).putExtra("taggedpeople", (Serializable) taggeduserInfoPOJOS), Constants.ACTIVITY_TAG_PEOPLE);
            }
        });

        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocation();
            }
        });
        tv_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInformation();
            }
        });


        checkPostStatus();
        et_subject.addTextChangedListener(textWatcher);
        tv_leader_name.addTextChangedListener(textWatcher);
        et_description.addTextChangedListener(textWatcher);
    }


    TextWatcher textWatcher = new TextWatcher() {
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
    };

    public void checkPostStatus() {
        boolean enable_post = true;


        if (et_subject.getText().toString().length() == 0) {
            enable_post = false;
        }

        if (et_description.getText().toString().length() == 0) {
            enable_post = false;
        }

        if (leaderUserProfilePOJO == null) {
            enable_post = false;
        }

        if (enable_post) {
            tv_post.setEnabled(true);
            tv_post.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            tv_post.setEnabled(false);
            tv_post.setTextColor(Color.parseColor("#90FFFFFF"));
        }

    }


    public void saveInformation() {
        try {
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            UserProfilePOJO userProfilePOJO = Constants.userProfilePOJO;

            reqEntity.addPart("user_profile_id", new StringBody(Constants.userProfilePOJO.getUserProfileId()));
            reqEntity.addPart("information_subject", new StringBody(et_subject.getText().toString()));
            reqEntity.addPart("information_description", new StringBody(et_description.getText().toString()));
            reqEntity.addPart("applicant_name", new StringBody(userProfilePOJO.getFirstName() + " " + userProfilePOJO.getLastName()));
            reqEntity.addPart("applicant_father_name", new StringBody(""));
            reqEntity.addPart("applicant_mobile", new StringBody(userProfilePOJO.getMobile()));
            reqEntity.addPart("applicant_email", new StringBody(userProfilePOJO.getEmail()));

            int count = 0;

            for (MediaPOJO mediaPOJO : attachPathString) {
                reqEntity.addPart("file[" + (count) + "]", new FileBody(new File(mediaPOJO.getPath())));
                reqEntity.addPart("thumb[" + (count) + "]", new StringBody(""));
                count++;
            }

            new WebUploadService(reqEntity, getActivity(), new WebServicesCallBack() {
                @Override
                public void onGetMsg(String apicall, String response) {
                    Log.d(TagUtils.getTag(), apicall + " :- " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        ToastClass.showShortToast(getActivity().getApplicationContext(), jsonObject.optString("message"));
                        if (jsonObject.optString("status").equals("success")) {
                            startActivity(new Intent(getActivity(), HomeActivity.class));
                            getActivity().finishAffinity();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "CREATE_INFORMATION", true).execute(WebServicesUrls.SAVE_INFORMATION);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public void initiateGalleryPicker() {
        //0=camera,1=Gallery,2=Camera and gallery
        new ImagePicker.Builder(getActivity())
                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .allowMultipleImages(true)
                .enableDebuggingMode(true)
                .build();
    }


    public void checkLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        } else {
            UtilityFunction.getLocation(getActivity().getApplicationContext());
            startActivityForResult(new Intent(getActivity(), CheckInActivity.class), Constants.ACTIVITY_LOCATION);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkLocationPermission() {
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.ACCESS_LOCATION);
            return;
        } else {
            UtilityFunction.getLocation(getActivity());
            startActivityForResult(new Intent(getActivity(), CheckInActivity.class), Constants.ACTIVITY_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.ACCESS_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    UtilityFunction.getLocation(getActivity().getApplicationContext());
                    startActivityForResult(new Intent(getActivity(), CheckInActivity.class), Constants.ACTIVITY_LOCATION);
                }
                return;
            }
        }
    }

    AttachMediaAdapter attachMediaAdapter;
    List<MediaPOJO> attachPathString = new ArrayList<>();

    public void attachMediaAdapter() {
        attachMediaAdapter = new AttachMediaAdapter(getActivity(), this, attachPathString);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_media.setHasFixedSize(true);
        rv_media.setAdapter(attachMediaAdapter);
        rv_media.setLayoutManager(linearLayoutManager);
        rv_media.setItemAnimator(new DefaultItemAnimator());

    }

    public void setupPubPrivSpinner() {
        List<String> pubpriList = new ArrayList<>();

        pubpriList.add("Public");
        pubpriList.add("Private");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.dropsimpledown, pubpriList);
        spinner_pubpri.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ACTIVITY_SELECT_LEADER && resultCode == Activity.RESULT_OK) {
            leaderUserProfilePOJO = (UserProfilePOJO) data.getSerializableExtra("userprofile");
            tv_leader_name.setText(leaderUserProfilePOJO.getFirstName() + " " + leaderUserProfilePOJO.getLastName());
        } else if (requestCode == Constants.ACTIVITY_TAG_PEOPLE) {
            if (resultCode == Activity.RESULT_OK) {
                taggeduserInfoPOJOS = (List<UserProfilePOJO>) data.getSerializableExtra("taggedpeople");
                tagging_description = getTaggedDescription(taggeduserInfoPOJOS);
                updateProfileStatus();
            }
        } else if (requestCode == Constants.ACTIVITY_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                if (resultCode == Activity.RESULT_OK) {
                    LocationPOJO locationPOJO = (LocationPOJO) data.getSerializableExtra("location");
                    this.locationPOJO = locationPOJO;
                    String check_in = locationPOJO.getFormatted_address();
                    place_description = " - at <b>" + locationPOJO.getFormatted_address() + "</b>";
                    updateProfileStatus();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                locationPOJO = null;
            }
        } else if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            List<String> mPaths = (List<String>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PATH);
            if (mPaths.size() > 0) {
                for (String path : mPaths) {
//                    if (!attachPathString.contains(path)) {
//                        attachPathString.add(path);
//                    }
                }
                attachMediaAdapter.notifyDataSetChanged();
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
}
