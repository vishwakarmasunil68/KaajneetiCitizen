package com.ritvi.kaajneeti.fragment.Express;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.express.complaint.DepartmentPOJO;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponseList;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateComplaintFragment extends Fragment{

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_create_complaint,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllDepartment();
        iv_schedule_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        ll_complaint_other.setVisibility(View.GONE);
        ll_tag.setVisibility(View.GONE);

        spinner_comp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
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
    }

    public void showDateDialog(){
        final Dialog dialog1 = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dialog_select_date);
        dialog1.setTitle("Select Date");
        dialog1.show();
        dialog1.setCancelable(true);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        DayScrollDatePicker mPicker = (DayScrollDatePicker) dialog1.findViewById(R.id.day_date_picker);

        mPicker.setStartDate(10, 10, 2010);
        mPicker.setEndDate(11, 11, 2011);


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
}
