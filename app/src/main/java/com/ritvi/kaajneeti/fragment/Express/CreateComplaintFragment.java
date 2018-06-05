package com.ritvi.kaajneeti.fragment.Express;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.ritvi.kaajneeti.activity.express.ExpressActivity;
import com.ritvi.kaajneeti.activity.express.TagPeopleActivity;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.activity.user.SelectFavoriteLeaderActivity;
import com.ritvi.kaajneeti.adapter.AttachMediaAdapter;
import com.ritvi.kaajneeti.adapter.TagShowAdapter;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.express.complaint.DepartmentPOJO;
import com.ritvi.kaajneeti.pojo.location.NewLocationPOJO;
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
    NewLocationPOJO newLocationPOJO;
    List<UserProfilePOJO> taggeduserInfoPOJOS = new ArrayList<>();

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
        getAllDepartment();
        attachMediaAdapter();
        setupPubPrivSpinner();

        tv_profile_name.setText(Constants.userProfilePOJO.getFirstName()+" "+Constants.userProfilePOJO.getLastName());
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
                newLocationPOJO = (NewLocationPOJO) data.getSerializableExtra("location");
                tv_location.setText(newLocationPOJO.getMain_text() + "," + newLocationPOJO.getSecondary_text());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                newLocationPOJO = null;
            }
        } else if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            List<String> mPaths = (List<String>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PATH);
            if (mPaths.size() > 0) {
                for (String path : mPaths) {
                    if (!attachPathString.contains(path)) {
                        attachPathString.add(path);
                    }
                }
                attachMediaAdapter.notifyDataSetChanged();
            }
        }
    }


    AttachMediaAdapter attachMediaAdapter;
    List<String> attachPathString = new ArrayList<>();

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
                    case 0:reqEntity.addPart("complaint_type_id", new StringBody("1"));
                        break;
                    case 1:reqEntity.addPart("complaint_type_id", new StringBody("2"));
                        break;
                    case 2:reqEntity.addPart("complaint_type_id", new StringBody("3"));
                        break;
                }
                reqEntity.addPart("applicant_email", new StringBody(userProfilePOJO.getEmail()));
                reqEntity.addPart("schedule_date", new StringBody(UtilityFunction.getConvertedDate(tv_date.getText().toString())));
                if (spinner_pubpri.getSelectedItemPosition() == 0) {
                    reqEntity.addPart("privacy", new StringBody("1"));
                } else {
                    reqEntity.addPart("privacy", new StringBody("0"));
                }
                if (newLocationPOJO != null) {
                    reqEntity.addPart("address", new StringBody(newLocationPOJO.getDescription()));
                    reqEntity.addPart("place", new StringBody(newLocationPOJO.getMain_text()));
                }
//                if (latLong != null) {
//                    reqEntity.addPart("latitude", new StringBody(String.valueOf(latLong.latitude)));
//                    reqEntity.addPart("longitude", new StringBody(String.valueOf(latLong.longitude)));
//                } else {
                    reqEntity.addPart("latitude", new StringBody(""));
                    reqEntity.addPart("longitude", new StringBody(""));
//                }

                if (departmentPOJOS.size() > 0) {
                    reqEntity.addPart("department", new StringBody(departmentPOJOS.get(spinner_department.getSelectedItemPosition()).getDepartmentId()));
                } else {
                    reqEntity.addPart("department", new StringBody(""));
                }
                reqEntity.addPart("complaint_member", new StringBody(""));

                int count = 0;

                for (int i = 0; i < attachPathString.size(); i++) {
                    reqEntity.addPart("file[" + (i) + "]", new FileBody(new File(attachPathString.get(i))));
                    reqEntity.addPart("thumb[" + (i) + "]", new StringBody(""));
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
                }, "CREATE_EVENT", true).execute(WebServicesUrls.POST_COMPLAINT);
            } else {
                ToastClass.showShortToast(getActivity().getApplicationContext(), "Please Select Leader First");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
