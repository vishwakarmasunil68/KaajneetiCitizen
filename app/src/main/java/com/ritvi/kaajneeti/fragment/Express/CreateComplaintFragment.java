package com.ritvi.kaajneeti.fragment.Express;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;
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
import com.ritvi.kaajneeti.adapter.TagShowAdapter;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.allfeeds.MediaPOJO;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintAttachmentPOJO;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintPOJO;
import com.ritvi.kaajneeti.pojo.express.complaint.DepartmentPOJO;
import com.ritvi.kaajneeti.pojo.location.GeometryPOJO;
import com.ritvi.kaajneeti.pojo.location.LatLongPOJO;
import com.ritvi.kaajneeti.pojo.location.LocationPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponseList;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;
import com.ritvi.kaajneeti.webservice.WebUploadService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CreateComplaintFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.spinner_department)
    Spinner spinner_department;
    @BindView(R.id.spinner_comp_type)
    Spinner spinner_comp_type;
    @BindView(R.id.iv_schedule_date)
    ImageView iv_schedule_date;
    @BindView(R.id.frame_schedule)
    FrameLayout frame_schedule;
    @BindView(R.id.ll_tag)
    LinearLayout ll_tag;
    @BindView(R.id.ll_complaint_other)
    LinearLayout ll_complaint_other;
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
    @BindView(R.id.frame_location)
    FrameLayout frame_location;
    @BindView(R.id.et_applicant_name)
    EditText et_applicant_name;
    @BindView(R.id.tv_leader_name)
    TextView tv_leader_name;
    @BindView(R.id.et_applicant_father_name)
    EditText et_applicant_father_name;
    @BindView(R.id.et_phone_number)
    EditText et_phone_number;
    @BindView(R.id.et_subject)
    EditText et_subject;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.et_description)
    EditText et_description;
    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.rv_tags)
    RecyclerView rv_tags;
    @BindView(R.id.rv_media)
    RecyclerView rv_media;
    @BindView(R.id.spinner_pubpri)
    Spinner spinner_pubpri;
    @BindView(R.id.tv_post)
    TextView tv_post;
    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @BindView(R.id.cv_profile_pic)
    CircleImageView cv_profile_pic;

    UserProfilePOJO leaderUserProfilePOJO;
    LocationPOJO locationPOJO;
    List<UserProfilePOJO> taggeduserInfoPOJOS = new ArrayList<>();
    ComplaintPOJO complaintPOJO;
    public List<String> deleteServerFiles = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_create_complaint, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            complaintPOJO = (ComplaintPOJO) getArguments().getSerializable("complaintPOJO");
        }

//        getAllDepartment();
        attachMediaAdapter();
        setupPubPrivSpinner();

        tv_profile_name.setText(Constants.userProfilePOJO.getFirstName() + " " + Constants.userProfilePOJO.getLastName());
        Glide.with(getActivity().getApplicationContext())
                .load(Constants.userProfilePOJO.getProfilePhotoPath())
                .error(R.drawable.ic_default_profile_pic)
                .placeholder(R.drawable.ic_default_profile_pic)
                .dontAnimate()
                .into(cv_profile_pic);

        iv_schedule_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDateDialog();
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CreateComplaintFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
        frame_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_schedule_date.callOnClick();
            }
        });
        tv_date.setText(UtilityFunction.getCurrentDate());
        ll_complaint_other.setVisibility(View.GONE);
        ll_tag.setVisibility(View.GONE);

        spinner_comp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        ll_complaint_other.setVisibility(View.GONE);
                        ll_tag.setVisibility(View.GONE);
                        break;
                    case 1:
                        ll_complaint_other.setVisibility(View.GONE);
                        ll_tag.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        ll_complaint_other.setVisibility(View.VISIBLE);
                        ll_tag.setVisibility(View.GONE);
                        break;
                }
                checkPostStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocation();
            }
        });
        frame_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocation();
            }
        });
        iv_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), TagPeopleActivity.class).putExtra("taggedpeople", (Serializable) taggeduserInfoPOJOS), Constants.ACTIVITY_TAG_PEOPLE);
            }
        });
        tv_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComplaint();
            }
        });

        if (complaintPOJO != null) {
            switch (complaintPOJO.getComplaintTypeId()) {
                case "1":
                    spinner_comp_type.setSelection(0);
                    break;
                case "2":
                    spinner_comp_type.setSelection(1);
                    break;
                case "3":
                    spinner_comp_type.setSelection(2);
                    break;
            }

            leaderUserProfilePOJO = complaintPOJO.getComplaintAssigned().get(0);
            tv_leader_name.setText(leaderUserProfilePOJO.getFirstName() + " " + leaderUserProfilePOJO.getLastName());
            et_subject.setText(complaintPOJO.getComplaintSubject());
            et_description.setText(complaintPOJO.getComplaintDescription());
//            tv_date.setText(complaintPOJO.ge);

            tv_date.setText(UtilityFunction.convertServerDateFromDT(complaintPOJO.getScheduleOn()));
            if (complaintPOJO.getComplaintMemberPOJOS() != null && complaintPOJO.getComplaintMemberPOJOS().size() > 0) {
                taggeduserInfoPOJOS.addAll(complaintPOJO.getComplaintMemberPOJOS());
                attachTagPeopleAdapter();
            }
            if (complaintPOJO.getComplaintAttachments() != null && complaintPOJO.getComplaintAttachments().size() > 0) {
                for (ComplaintAttachmentPOJO postAttachmentPOJO : complaintPOJO.getComplaintAttachments()) {
                    MediaPOJO mediaPOJO = new MediaPOJO();
                    mediaPOJO.setPath(postAttachmentPOJO.getAttachmentFile());
                    mediaPOJO.setIs_server(true);
                    mediaPOJO.setId(postAttachmentPOJO.getComplaintAttachmentId());
                    attachPathString.add(mediaPOJO);
                }
                attachMediaAdapter.notifyDataSetChanged();
            }

            if (complaintPOJO.getComplaintPrivacy().equalsIgnoreCase("1")) {
                spinner_pubpri.setSelection(0);
            } else {
                spinner_pubpri.setSelection(1);
            }

//            et_whats_new.setText(postPOJO.getPostTitle());

            if (complaintPOJO.getServerLocationPOJO() != null) {
                LocationPOJO locationPOJO = new LocationPOJO();
                locationPOJO.setPlaceId(complaintPOJO.getServerLocationPOJO().getPlaceId());
                locationPOJO.setFormatted_address(complaintPOJO.getServerLocationPOJO().getLocationAddress());
                locationPOJO.setUrl(complaintPOJO.getServerLocationPOJO().getLocationUrl());
                locationPOJO.setAdr_address(complaintPOJO.getServerLocationPOJO().getLocationAddress());
                locationPOJO.setVicinity(complaintPOJO.getServerLocationPOJO().getLocationVicinity());

                GeometryPOJO geometryPOJO = new GeometryPOJO();
                LatLongPOJO latLongPOJO = new LatLongPOJO();
                latLongPOJO.setLat(UtilityFunction.getFormattedValue(complaintPOJO.getServerLocationPOJO().getLocationLattitude()));
                latLongPOJO.setLng(UtilityFunction.getFormattedValue(complaintPOJO.getServerLocationPOJO().getLocationLongitude()));

                geometryPOJO.setLocation(latLongPOJO);
                locationPOJO.setGeometry(geometryPOJO);
                this.locationPOJO = locationPOJO;

                tv_location.setText(locationPOJO.getFormatted_address());
            }
        }


        checkPostStatus();

        et_subject.addTextChangedListener(textWatcher);
        et_applicant_father_name.addTextChangedListener(textWatcher);
        et_applicant_name.addTextChangedListener(textWatcher);
        et_phone_number.addTextChangedListener(textWatcher);
        et_description.addTextChangedListener(textWatcher);
        tv_date.addTextChangedListener(textWatcher);
        tv_location.addTextChangedListener(textWatcher);

    }

    TextWatcher textWatcher=new TextWatcher() {
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
        if (spinner_comp_type.getSelectedItemPosition() == 1) {
            if (taggeduserInfoPOJOS != null && taggeduserInfoPOJOS.size() > 0) {
//                enable_post = true;
            } else {
                enable_post = false;
            }
        } else if (spinner_comp_type.getSelectedItemPosition() == 2) {
            if(et_applicant_name.getText().toString().length()>0
                    &&et_applicant_father_name.getText().toString().length()>0
                    &&et_phone_number.getText().toString().length()>0){
//                enable_post=true;
            }else{
                enable_post=false;
            }
        }

        if (locationPOJO != null) {
//            enable_post = true;
        } else {
            enable_post = false;
        }

        if (et_subject.getText().toString().length() > 0) {
//            enable_post = true;
        } else {
            enable_post = false;
        }

        if (tv_date.getText().toString().length() > 0) {
//            enable_post = true;
        } else {
            enable_post = false;
        }

        if(et_description.getText().toString().length()==0){
            enable_post=false;
        }

        if(leaderUserProfilePOJO==null){
            enable_post=false;
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
                getActivity(), R.layout.dropsimpledown, pubpriList);
        spinner_pubpri.setAdapter(spinnerArrayAdapter);
    }


    public void checkLocation() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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

    public void showDateDialog() {
        final Dialog dialog1 = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.setCancelable(true);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_select_date);
        dialog1.show();
        dialog1.setCancelable(true);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        DayScrollDatePicker mPicker = (DayScrollDatePicker) dialog1.findViewById(R.id.day_date_picker);

        mPicker.setStartDate(01, 01, 1970);
        mPicker.setEndDate(01, 01, 2030);

        mPicker.getSelectedDate(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@Nullable Date date) {

            }
        });

    }

    List<DepartmentPOJO> departmentPOJOS = new ArrayList<>();

    public void getAllDepartment() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", "3"));
        new WebServiceBaseResponseList<DepartmentPOJO>(nameValuePairs, getActivity(), new ResponseListCallback<DepartmentPOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO<DepartmentPOJO> responseListPOJO) {
                try {
                    if (responseListPOJO.getResultList().size() > 0) {
                        departmentPOJOS.addAll(responseListPOJO.getResultList());

                        List<String> departmentStringList = new ArrayList<>();
                        for (DepartmentPOJO departmentPOJO : departmentPOJOS) {
                            departmentStringList.add(departmentPOJO.getDepartmentName());
                        }


                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                getActivity(), R.layout.dropsimpledown, departmentStringList);
                        spinner_department.setAdapter(spinnerArrayAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, DepartmentPOJO.class, "CALL_DEPARTMENT_GET_API", false).execute(WebServicesUrls.DEPARTMENT_URL);
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
                attachTagPeopleAdapter();
            }
        } else if (requestCode == Constants.ACTIVITY_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                locationPOJO = (LocationPOJO) data.getSerializableExtra("location");
                tv_location.setText(locationPOJO.getFormatted_address());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                locationPOJO = null;
            }
        } else if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
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
        }
        checkPostStatus();
    }

    public boolean isImagePresent(String path) {
        for (MediaPOJO mediaPOJO : attachPathString) {
            if (mediaPOJO.getPath().equalsIgnoreCase(path)) {
                return true;
            }
        }
        return false;
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

    public void attachTagPeopleAdapter() {
        TagShowAdapter tagShowAdapter = new TagShowAdapter(getActivity(), this, taggeduserInfoPOJOS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_tags.setHasFixedSize(true);
        rv_tags.setAdapter(tagShowAdapter);
        rv_tags.setLayoutManager(linearLayoutManager);
        rv_tags.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String month = "";
        String day = "";
        if ((monthOfYear + 1) < 10) {
            month = "0" + (monthOfYear + 1);
        } else {
            month = String.valueOf(monthOfYear + 1);
        }

        if (dayOfMonth < 10) {
            day = "0" + dayOfMonth;
        } else {
            day = String.valueOf(dayOfMonth);
        }

        String date = day + "-" + month + "-" + year;
        tv_date.setText(date);
    }


    public void saveComplaint() {
        try {
            if (leaderUserProfilePOJO != null) {
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("", ""));
                reqEntity.addPart("user_profile_id", new StringBody(Constants.userProfilePOJO.getUserProfileId()));
                reqEntity.addPart("complaint_subject", new StringBody(et_subject.getText().toString()));
                reqEntity.addPart("complaint_description", new StringBody(et_description.getText().toString()));
                UserProfilePOJO userProfilePOJO = Constants.userProfilePOJO;
                reqEntity.addPart("applicant_name", new StringBody(userProfilePOJO.getFirstName() + " " + userProfilePOJO.getLastName()));
                reqEntity.addPart("applicant_father_name", new StringBody(""));
                reqEntity.addPart("applicant_mobile", new StringBody(userProfilePOJO.getMobile()));
                reqEntity.addPart("assign_to_profile_id", new StringBody(leaderUserProfilePOJO.getUserProfileId()));

                switch (spinner_comp_type.getSelectedItemPosition()) {
                    case 0:
                        reqEntity.addPart("complaint_type_id", new StringBody("1"));
                        break;
                    case 1:
                        reqEntity.addPart("complaint_type_id", new StringBody("2"));
                        break;
                    case 2:
                        reqEntity.addPart("complaint_type_id", new StringBody("3"));
                        break;
                }
                reqEntity.addPart("applicant_email", new StringBody(userProfilePOJO.getEmail()));
                reqEntity.addPart("schedule_date", new StringBody(UtilityFunction.getConvertedDate(tv_date.getText().toString())));
                if (spinner_pubpri.getSelectedItemPosition() == 0) {
                    reqEntity.addPart("privacy", new StringBody("1"));
                } else {
                    reqEntity.addPart("privacy", new StringBody("0"));
                }

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
//                if (latLong != null) {
//                    reqEntity.addPart("latitude", new StringBody(String.valueOf(latLong.latitude)));
//                    reqEntity.addPart("longitude", new StringBody(String.valueOf(latLong.longitude)));
//                } else {
                reqEntity.addPart("latitude", new StringBody(""));
                reqEntity.addPart("longitude", new StringBody(""));
//                }
//
//                if (departmentPOJOS.size() > 0) {
//                    reqEntity.addPart("department", new StringBody(departmentPOJOS.get(spinner_department.getSelectedItemPosition()).getDepartmentId()));
//                } else {
//                    reqEntity.addPart("department", new StringBody(""));
//                }

                if (taggeduserInfoPOJOS.size() > 0) {
                    int count = 0;
                    for (UserProfilePOJO userProfilePOJO1 : taggeduserInfoPOJOS) {
                        reqEntity.addPart("complaint_member[" + count + "]", new StringBody(userProfilePOJO1.getUserProfileId()));
                        count++;
                    }
                } else {
                    reqEntity.addPart("complaint_member", new StringBody(""));
                }

                int count = 0;

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

                if (deleteServerFiles.size() > 0) {
                    int delete_count = 0;
                    for (String str : deleteServerFiles) {
                        reqEntity.addPart("delete_image[" + delete_count + "]", new StringBody(str));
                        delete_count++;
                    }
                }

                String url = "";
                if (complaintPOJO != null) {
                    reqEntity.addPart("complaint_id", new StringBody(complaintPOJO.getComplaintId()));
                    reqEntity.addPart("status", new StringBody("1"));
                    url = WebServicesUrls.UPDATE_COMPLAINT;
                } else {
                    url = WebServicesUrls.POST_COMPLAINT;
                }


                new WebUploadService(reqEntity, getActivity(), new WebServicesCallBack() {
                    @Override
                    public void onGetMsg(String apicall, String response) {
                        Log.d(TagUtils.getTag(), apicall + " :- " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            ToastClass.showShortToast(getActivity().getApplicationContext(), jsonObject.optString("message"));
                            if (jsonObject.optString("status").equals("success")) {
//                                startActivity(new Intent(getActivity(), ApplicationSubmittedActivity.class).putExtra("comp_type", "complaint"));
                                startActivity(new Intent(getActivity(), HomeActivity.class));
                                getActivity().finishAffinity();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, "CREATE_COMPLAINT", true).execute(url);
            } else {
                ToastClass.showShortToast(getActivity().getApplicationContext(), "Please Select Leader First");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void removePosition(MediaPOJO mediaPOJO, int position) {
        if (mediaPOJO.isIs_server()) {
            deleteServerFiles.add(mediaPOJO.getId());
        }
        attachPathString.remove(position);
        attachMediaAdapter.notifyDataSetChanged();
    }
}
