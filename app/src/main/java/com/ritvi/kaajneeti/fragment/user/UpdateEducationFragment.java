package com.ritvi.kaajneeti.fragment.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.pojo.user.EducationPOJO;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateEducationFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.et_college)
    EditText et_college;
    @BindView(R.id.et_qualification)
    EditText et_qualification;
    @BindView(R.id.check_pursuing)
    CheckBox check_pursuing;
    @BindView(R.id.ll_end_date)
    LinearLayout ll_end_date;
    @BindView(R.id.tv_start_date)
    TextView tv_start_date;
    @BindView(R.id.tv_end_date)
    TextView tv_end_date;
    @BindView(R.id.et_location)
    EditText et_location;
    @BindView(R.id.iv_location)
    ImageView iv_location;
    @BindView(R.id.tv_done)
    TextView tv_done;
    @BindView(R.id.iv_start_date)
    ImageView iv_start_date;
    @BindView(R.id.iv_end_date)
    ImageView iv_end_date;

    boolean is_start_date=false;

    EducationPOJO educationPOJO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_update_education,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        educationPOJO= (EducationPOJO) getArguments().getSerializable("educationPOJO");

        if(educationPOJO!=null){
            setValues();
        }

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        check_pursuing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ll_end_date.setVisibility(View.GONE);
                }else{
                    ll_end_date.setVisibility(View.VISIBLE);
                }
            }
        });

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEducation();
            }
        });
        iv_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_start_date=true;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        UpdateEducationFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Start Date");
            }
        });

        iv_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_start_date=false;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        UpdateEducationFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "End Date");
            }
        });

    }


    public void setValues() {
        et_college.setText(educationPOJO.getQualificationUniversity());
        et_qualification.setText(educationPOJO.getQualification());
        et_location.setText(educationPOJO.getQualificationLocation());
        tv_start_date.setText(educationPOJO.getQualificationFrom());
        tv_end_date.setText(educationPOJO.getQualificationTo());
        if (educationPOJO.getPersuing().equals("1")) {
            check_pursuing.setChecked(true);
        } else {
            check_pursuing.setChecked(false);
        }
    }

    public void saveEducation() {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        if (educationPOJO != null) {
            nameValuePairs.add(new BasicNameValuePair("education_id", educationPOJO.getUserProfileEducationId()));
        } else {
            nameValuePairs.add(new BasicNameValuePair("education_id", ""));
        }
        nameValuePairs.add(new BasicNameValuePair("qualification", et_qualification.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("location", et_location.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("university", et_college.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("from", tv_start_date.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("to", tv_end_date.getText().toString()));
        if (check_pursuing.isChecked()) {
            nameValuePairs.add(new BasicNameValuePair("persuing", "1"));
        } else {
            nameValuePairs.add(new BasicNameValuePair("persuing", "0"));
        }
        nameValuePairs.add(new BasicNameValuePair("private_public", ""));

        new WebServiceBase(nameValuePairs, getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("status").equals("success")) {
                        if (educationPOJO != null) {
                            ToastClass.showShortToast(getActivity().getApplicationContext(), "Education Updated");
                        } else {
                            ToastClass.showShortToast(getActivity().getApplicationContext(), "Education Added");
                        }
                        getActivity().onBackPressed();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "CALL_COLLEGE_SAVE", true).execute(WebServicesUrls.UPDATE_PROFILE_EDUCATION);
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
        if (is_start_date) {
            tv_start_date.setText(date);
        } else {
            tv_end_date.setText(date);
        }
    }
}
