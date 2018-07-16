package com.ritvi.kaajneeti.fragment.user;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.activity.express.CheckInActivity;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.fragment.Express.CreateEventFragment;
import com.ritvi.kaajneeti.pojo.location.LocationPOJO;
import com.ritvi.kaajneeti.pojo.user.AddressPOJO;
import com.ritvi.kaajneeti.pojo.user.WorkPOJO;
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

public class UpdateWorkFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.ll_end_date)
    LinearLayout ll_end_date;
    @BindView(R.id.check_work)
    CheckBox check_work;
    @BindView(R.id.et_work)
    EditText et_work;
    @BindView(R.id.et_position)
    EditText et_position;
    @BindView(R.id.et_location)
    EditText et_location;
    @BindView(R.id.iv_end_date)
    ImageView iv_end_date;
    @BindView(R.id.tv_end_date)
    TextView tv_end_date;
    @BindView(R.id.tv_start_date)
    TextView tv_start_date;
    @BindView(R.id.iv_start_date)
    ImageView iv_start_date;
    @BindView(R.id.tv_done)
    TextView tv_done;
    @BindView(R.id.frame_location)
    FrameLayout frame_location;
    @BindView(R.id.iv_location)
    ImageView iv_location;

    boolean is_start_date=false;

    WorkPOJO workPOJO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_update_work,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if(getArguments().getSerializable("workPOJO")!=null) {
            workPOJO = (WorkPOJO) getArguments().getSerializable("workPOJO");
        }


        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        check_work.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ll_end_date.setVisibility(View.GONE);
                }else{
                    ll_end_date.setVisibility(View.VISIBLE);
                }
            }
        });

        iv_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_start_date=true;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        UpdateWorkFragment.this,
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
                        UpdateWorkFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "End Date");
            }
        });
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UtilityFunction.checkEdits(et_work,et_position,et_location)){
                    saveWork();
                }else{
                    ToastClass.showShortToast(getActivity().getApplicationContext(),"Please Enter All Fields Properly");
                }
            }
        });

        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocation();
            }
        });

        if(workPOJO!=null){
            setUpViews();
        }
    }

    public void setUpViews(){
        et_work.setText(workPOJO.getWorkCompany());
        et_position.setText(workPOJO.getWorkPosition());
        et_location.setText(workPOJO.getWorkLocation());
        tv_start_date.setText(UtilityFunction.getServerConvertedDate(workPOJO.getWorkFrom()));


        if(workPOJO.getCurrentlyWorking().equalsIgnoreCase("1")){
            check_work.setChecked(true);
        }else{
            check_work.setChecked(false);
            tv_end_date.setText(UtilityFunction.getServerConvertedDate(workPOJO.getWorkTo()));
        }
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
            UtilityFunction.getLocation(getActivity().getApplicationContext());
            startActivityForResult(new Intent(getActivity(), CheckInActivity.class), Constants.ACTIVITY_LOCATION);
        }
    }


    public void saveWork(){

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        if(workPOJO!=null) {
            nameValuePairs.add(new BasicNameValuePair("work_id", workPOJO.getUserProfileWorkId()));
        }else{
            nameValuePairs.add(new BasicNameValuePair("work_id", ""));
        }
        nameValuePairs.add(new BasicNameValuePair("work", et_work.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("place", et_position.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("location", et_location.getText().toString()));

        String start_date=UtilityFunction.getConvertedDate(tv_start_date.getText().toString());
        String end_date=UtilityFunction.getConvertedDate(tv_end_date.getText().toString());
        nameValuePairs.add(new BasicNameValuePair("from", start_date));
        nameValuePairs.add(new BasicNameValuePair("to", end_date));
        if (check_work.isChecked()) {
            nameValuePairs.add(new BasicNameValuePair("currently", "1"));
        } else {
            nameValuePairs.add(new BasicNameValuePair("currently", "0"));
        }
        nameValuePairs.add(new BasicNameValuePair("private_public", "1"));

        new WebServiceBase(nameValuePairs, getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.optString("status").equals("success")){
                        if(workPOJO!=null) {
                            ToastClass.showShortToast(getActivity().getApplicationContext(), "Work Updated");
                        }else{
                            ToastClass.showShortToast(getActivity().getApplicationContext(), "Work Added");
                        }
                        getActivity().onBackPressed();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, "CALL_WORK_API", true).execute(WebServicesUrls.UPDATE_PROFILE_WORK);
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
    LocationPOJO locationPOJO;
    String check_in="";
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ACTIVITY_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                LocationPOJO locationPOJO = (LocationPOJO) data.getSerializableExtra("location");
                this.locationPOJO = locationPOJO;
                check_in = locationPOJO.getFormatted_address();
                et_location.setText(check_in);
            }
        }
    }
}
