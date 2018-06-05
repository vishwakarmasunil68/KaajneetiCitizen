package com.ritvi.kaajneeti.fragment.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.pojo.express.complaint.DepartmentPOJO;
import com.ritvi.kaajneeti.pojo.user.AddressPOJO;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateAddressFragment extends Fragment {

    @BindView(R.id.spinner_countries)
    Spinner spinner_countries;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.et_address)
    EditText et_address;
    @BindView(R.id.et_landmark)
    EditText et_landmark;
    @BindView(R.id.et_city)
    EditText et_city;
    @BindView(R.id.et_state)
    EditText et_state;
    @BindView(R.id.et_pincode)
    EditText et_pincode;
    @BindView(R.id.tv_done)
    TextView tv_done;
    @BindView(R.id.tv_page_title)
    TextView tv_page_title;

    AddressPOJO addressPOJO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_update_address, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments().getSerializable("addressPOJO")!=null) {
            addressPOJO = (AddressPOJO) getArguments().getSerializable("addressPOJO");
        }

        if(addressPOJO!=null){
            tv_page_title.setText("Update Address");
            setValues();
        }else{
            tv_page_title.setText("Add Address");
        }

        setCountryAdapter();
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddress();
            }
        });
    }

    public void setValues() {
        et_address.setText(addressPOJO.getAddress());
        et_city.setText(addressPOJO.getCity());
//        et_country.setText(addressPOJO.getCountry());
        et_landmark.setText(addressPOJO.getLandmark());
        et_pincode.setText(addressPOJO.getPincode());
        et_state.setText(addressPOJO.getState());
    }

    public void setCountryAdapter() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.dropsimpledown, UtilityFunction.listOfCountries());
        spinner_countries.setAdapter(spinnerArrayAdapter);
    }

    public void saveAddress() {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        if (addressPOJO != null) {
            nameValuePairs.add(new BasicNameValuePair("address_id", addressPOJO.getUserProfileAddressId()));
        }
        nameValuePairs.add(new BasicNameValuePair("address", et_address.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("city", et_city.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("state", et_state.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("landmark", et_landmark.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("country", spinner_countries.getSelectedItem().toString()));
        nameValuePairs.add(new BasicNameValuePair("pincode", et_pincode.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("default", ""));
        nameValuePairs.add(new BasicNameValuePair("home_work", ""));
        nameValuePairs.add(new BasicNameValuePair("private_public", ""));
        new WebServiceBase(nameValuePairs, getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.optString("status").equals("success")){
                        if(addressPOJO!=null) {
                            ToastClass.showShortToast(getActivity().getApplicationContext(), "Address Updated");
                        }else{
                            ToastClass.showShortToast(getActivity().getApplicationContext(), "Address Added");
                        }
                        getActivity().onBackPressed();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, "UPDATE_ADDRESS", true).execute(WebServicesUrls.UPDATE_PROFILE_ADDRESS);
    }
}
