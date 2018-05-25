package com.ritvi.kaajneeti.activity.express;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Pref;
import com.ritvi.kaajneeti.Util.StringUtils;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.adapter.LocationAdapter;
import com.ritvi.kaajneeti.adapter.TagPeopleAdapter;
import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.location.LocationPOJO;
import com.ritvi.kaajneeti.pojo.location.LocationResponseListPOJO;
import com.ritvi.kaajneeti.pojo.location.NewLocationPOJO;
import com.ritvi.kaajneeti.webservice.GetWebServices;
import com.ritvi.kaajneeti.webservice.LocationResponseListCallback;
import com.ritvi.kaajneeti.webservice.LocationWebservice;
import com.ritvi.kaajneeti.webservice.ResponseListCallback;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckInActivity extends AppCompatActivity {

    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.rv_location)
    RecyclerView rv_location;
    @BindView(R.id.ic_back)
    ImageView ic_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        ButterKnife.bind(this);
        attachAdapter();
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                callAPI();
            }
        });

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void callAPI() {
        new LocationWebservice(this, new LocationResponseListCallback() {
            @Override
            public void onGetMsg(LocationResponseListPOJO locationResponseListPOJO) {
                Log.d(TagUtils.getTag(),"location callback:-"+locationResponseListPOJO.isSuccess());
                if (locationResponseListPOJO.isSuccess()) {

                    locationPOJOS.clear();
                    locationPOJOS.addAll(locationResponseListPOJO.getResultList());
                    locationAdapter.notifyDataSetChanged();
                }
            }
        }, "GET_LOCATION", false).execute(WebServicesUrls.getLocationAPI(getResources().getString(R.string.google_places_api_key),
                Pref.GetStringPref(getApplicationContext(), StringUtils.CURRENT_LATITUDE, "28.6276928"),
                Pref.GetStringPref(getApplicationContext(), StringUtils.CURRENT_LONGITUDE, "77.3729876"),
                et_search.getText().toString()));
    }


    List<NewLocationPOJO> locationPOJOS = new ArrayList<>();
    LocationAdapter locationAdapter;

    public void attachAdapter() {
        locationAdapter = new LocationAdapter(this, null, locationPOJOS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_location.setHasFixedSize(true);
        rv_location.setAdapter(locationAdapter);
        rv_location.setLayoutManager(linearLayoutManager);
        rv_location.setItemAnimator(new DefaultItemAnimator());
    }

    public void setActivityLocation(NewLocationPOJO newLocationPOJO) {
        if(newLocationPOJO!=null){
            Intent returnIntent = new Intent();
            returnIntent.putExtra("location",newLocationPOJO);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }else{
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }
}
