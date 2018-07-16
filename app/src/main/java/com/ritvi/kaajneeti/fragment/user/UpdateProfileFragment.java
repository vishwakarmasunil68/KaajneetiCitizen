package com.ritvi.kaajneeti.fragment.user;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.FileUtils;
import com.ritvi.kaajneeti.Util.Pref;
import com.ritvi.kaajneeti.Util.SetViews;
import com.ritvi.kaajneeti.Util.StringUtils;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.adapter.AddressAdapter;
import com.ritvi.kaajneeti.adapter.EducationAdapter;
import com.ritvi.kaajneeti.adapter.LeaderAdapter;
import com.ritvi.kaajneeti.adapter.WorkAdapter;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.user.AddressPOJO;
import com.ritvi.kaajneeti.pojo.user.EducationPOJO;
import com.ritvi.kaajneeti.pojo.user.FullProfilePOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.pojo.user.WorkPOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;
import com.ritvi.kaajneeti.webservice.WebUploadService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.yalantis.ucrop.UCrop;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.ll_add_address)
    LinearLayout ll_add_address;
    @BindView(R.id.ll_add_work)
    LinearLayout ll_add_work;
    @BindView(R.id.ll_add_education)
    LinearLayout ll_add_education;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.cv_profile_pic)
    CircleImageView cv_profile_pic;
    @BindView(R.id.iv_personal_drop)
    ImageView iv_personal_drop;
    @BindView(R.id.iv_address_drop)
    ImageView iv_address_drop;
    @BindView(R.id.iv_work_drop)
    ImageView iv_work_drop;
    @BindView(R.id.iv_education_drop)
    ImageView iv_education_drop;
    @BindView(R.id.ll_personal_header)
    LinearLayout ll_personal_header;
    @BindView(R.id.ll_address_header)
    LinearLayout ll_address_header;
    @BindView(R.id.ll_work_header)
    LinearLayout ll_work_header;
    @BindView(R.id.ll_education_header)
    LinearLayout ll_education_header;
    @BindView(R.id.ll_personal_view)
    LinearLayout ll_personal_view;
    @BindView(R.id.ll_address_view)
    LinearLayout ll_address_view;
    @BindView(R.id.ll_work_view)
    LinearLayout ll_work_view;
    @BindView(R.id.ll_education_view)
    LinearLayout ll_education_view;
    @BindView(R.id.iv_profile_pic_edit)
    ImageView iv_profile_pic_edit;
    @BindView(R.id.et_first_name)
    EditText et_first_name;
    @BindView(R.id.et_last_name)
    EditText et_last_name;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_phone_number)
    EditText et_phone_number;
    @BindView(R.id.rg_gender)
    RadioGroup rg_gender;
    @BindView(R.id.rb_male)
    RadioButton rb_male;
    @BindView(R.id.rb_female)
    RadioButton rb_female;
    @BindView(R.id.rb_other)
    RadioButton rb_other;

    @BindView(R.id.rg_martial)
    RadioGroup rg_martial;
    @BindView(R.id.rb_single)
    RadioButton rb_single;
    @BindView(R.id.rb_married)
    RadioButton rb_married;
    @BindView(R.id.et_bio)
    EditText et_bio;
    @BindView(R.id.et_alt_phone_number)
    EditText et_alt_phone_number;
    @BindView(R.id.tv_dob)
    TextView tv_dob;
    @BindView(R.id.iv_dob)
    ImageView iv_dob;
    @BindView(R.id.tv_done)
    TextView tv_done;
    @BindView(R.id.rv_address)
    RecyclerView rv_address;
    @BindView(R.id.rv_work)
    RecyclerView rv_work;
    @BindView(R.id.rv_education)
    RecyclerView rv_education;
    @BindView(R.id.frame_dob)
    FrameLayout frame_dob;


    UserProfilePOJO userProfilePOJO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_update_profile_frag, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userProfilePOJO = (UserProfilePOJO) getArguments().getSerializable("userProfile");
        ll_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("addressPOJO", null);
                    UpdateAddressFragment updateAddressFragment = new UpdateAddressFragment();
                    updateAddressFragment.setArguments(bundle);
                    homeActivity.replaceFragmentinFrameHome(updateAddressFragment, "UpdateAddressFragment");
                }
            }
        });
        ll_add_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("workPOJO", null);
                    UpdateWorkFragment updateWorkFragment = new UpdateWorkFragment();
                    updateWorkFragment.setArguments(bundle);
                    homeActivity.replaceFragmentinFrameHome(updateWorkFragment, "UpdateWorkFragment");
                }
            }
        });
        ll_add_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("educationPOJO", null);
                    UpdateEducationFragment updateEducationFragment = new UpdateEducationFragment();
                    updateEducationFragment.setArguments(bundle);
                    homeActivity.replaceFragmentinFrameHome(updateEducationFragment, "updateEducationFragment");
                }
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        ll_address_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_address_drop.callOnClick();
            }
        });
        ll_work_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_work_drop.callOnClick();
            }
        });
        ll_personal_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_personal_drop.callOnClick();
            }
        });
        ll_education_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_education_drop.callOnClick();
            }
        });

        iv_address_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidePersonal();
                hideWork();
                hideEducation();
                if (ll_address_view.getVisibility() == View.VISIBLE) {
                    hideAddress();
                } else {
                    showAddress();
                }
            }
        });
        iv_work_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidePersonal();
                hideAddress();
                hideEducation();
                if (ll_work_view.getVisibility() == View.VISIBLE) {
                    hideWork();
                } else {
                    showWork();
                }
            }
        });
        iv_education_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidePersonal();
                hideAddress();
                hideWork();
                if (ll_education_view.getVisibility() == View.VISIBLE) {
                    hideEducation();
                } else {
                    showEducation();
                }
            }
        });

        iv_personal_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideEducation();
                hideAddress();
                hideWork();
                if (ll_personal_view.getVisibility() == View.VISIBLE) {
                    hidePersonal();
                } else {
                    showPersonal();
                }
            }
        });

        iv_profile_pic_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TagUtils.getTag(),"profile update cli");
                final PopupMenu menu = new PopupMenu(getActivity(), v);

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuitem) {
                        switch (menuitem.getItemId()) {
                            case R.id.popup_change:
                                initiateGalleryPicker();
                                break;
                            case R.id.popup_remove:
                                removeProfilePic();
                                break;
                        }
                        return false;
                    }
                });
                menu.inflate(R.menu.menu_profile_pic_update);
                menu.show();
            }
        });

        if (userProfilePOJO != null) {
            getAllProfileData();
        }

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePersonalInformation();
            }
        });

        iv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        UpdateProfileFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Date of birth");
            }
        });
        frame_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_dob.callOnClick();
            }
        });

        attachAddressRecycler();
        attachWorkRecycler();
        attachEducationAdapter();
    }


    public void removeProfilePic() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        new WebServiceBase(nameValuePairs, getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("status").equals("success")) {
                        Glide.with(getActivity().getApplicationContext())
                                .load(R.drawable.ic_default_profile_pic)
                                .into(cv_profile_pic);
                        JSONObject result = jsonObject.optJSONObject("result");
                        Pref.SetStringPref(getActivity().getApplicationContext(), StringUtils.USER_PROFILE, result.toString());
                        Constants.userProfilePOJO = new Gson().fromJson(result.toString(), UserProfilePOJO.class);
                        SetViews.changeProfilePics(getActivity().getApplicationContext(), Constants.userProfilePOJO.getProfilePhotoPath());
                    }
                    ToastClass.showShortToast(getActivity().getApplicationContext(), jsonObject.optString("message"));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "REMOVE_PROFILE_API", true).execute(WebServicesUrls.REMOVE_PROFILE_PIC);
    }


    List<AddressPOJO> addressPOJOS = new ArrayList<>();
    AddressAdapter addressAdapter;

    public void attachAddressRecycler() {
        addressAdapter = new AddressAdapter(getActivity(), this, addressPOJOS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_address.setHasFixedSize(true);
        rv_address.setAdapter(addressAdapter);
        rv_address.setLayoutManager(linearLayoutManager);
        rv_address.setItemAnimator(new DefaultItemAnimator());
    }

    List<WorkPOJO> workPOJOS= new ArrayList<>();
    WorkAdapter workAdapter;

    public void attachWorkRecycler() {
        workAdapter = new WorkAdapter(getActivity(), this, workPOJOS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_work.setHasFixedSize(true);
        rv_work.setAdapter(workAdapter);
        rv_work.setLayoutManager(linearLayoutManager);
        rv_work.setItemAnimator(new DefaultItemAnimator());
    }

    List<EducationPOJO> educationPOJOS= new ArrayList<>();
    EducationAdapter educationAdapter;

    public void attachEducationAdapter() {
        educationAdapter = new EducationAdapter(getActivity(), this, educationPOJOS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_education.setHasFixedSize(true);
        rv_education.setAdapter(educationAdapter);
        rv_education.setLayoutManager(linearLayoutManager);
        rv_education.setItemAnimator(new DefaultItemAnimator());
    }

    FullProfilePOJO fullProfilePOJO;

    public void getAllProfileData() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("friend_user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        new WebServiceBaseResponse<FullProfilePOJO>(nameValuePairs, getActivity(), new ResponseCallBack<FullProfilePOJO>() {
            @Override
            public void onGetMsg(ResponsePOJO<FullProfilePOJO> responsePOJO) {
                if (responsePOJO.isSuccess()) {
                    fullProfilePOJO = responsePOJO.getResult();
                    setUpProfileData(responsePOJO.getResult());
                    if (responsePOJO.getResult().getAddressPOJOS().size() > 0) {
                        addressPOJOS.clear();
                        addressPOJOS.addAll(responsePOJO.getResult().getAddressPOJOS());
                    }
                    addressAdapter.notifyDataSetChanged();

                    if (responsePOJO.getResult().getWorkPOJOList().size() > 0) {
                        workPOJOS.clear();
                        workPOJOS.addAll(responsePOJO.getResult().getWorkPOJOList());
                    }
                    workAdapter.notifyDataSetChanged();

                    if (responsePOJO.getResult().getWorkPOJOList().size() > 0) {
                        educationPOJOS.clear();
                        educationPOJOS.addAll(responsePOJO.getResult().getEducationPOJOS());
                    }
                    educationAdapter.notifyDataSetChanged();
                } else {
                    ToastClass.showShortToast(getActivity().getApplicationContext(), responsePOJO.getMessage());
                }
            }
        }, FullProfilePOJO.class, "CALL_PROFILE_API", true).execute(WebServicesUrls.FULL_PROFILE_URL);
    }


    public void setUpProfileData(final FullProfilePOJO fullProfilePOJO) {
        if (fullProfilePOJO != null) {

            Glide.with(getActivity().getApplicationContext())
                    .load(fullProfilePOJO.getProfilePOJO().getProfilePhotoPath())
                    .placeholder(R.drawable.ic_default_profile_pic)
                    .error(R.drawable.ic_default_profile_pic)
                    .dontAnimate()
                    .into(cv_profile_pic);

            et_bio.setText(fullProfilePOJO.getProfilePOJO().getUserBio());

            if(fullProfilePOJO.getProfilePOJO().getFirstName()!=null
                    &&fullProfilePOJO.getProfilePOJO().getFirstName().equalsIgnoreCase("Profile Name")){
            }else{
                et_first_name.setText(fullProfilePOJO.getProfilePOJO().getFirstName());
            }
            et_last_name.setText(fullProfilePOJO.getProfilePOJO().getLastName());
            et_email.setText(fullProfilePOJO.getProfilePOJO().getEmail());
            if (fullProfilePOJO.getProfilePOJO().getMobile().length() > 0) {
                String phone = fullProfilePOJO.getProfilePOJO().getMobile().replace("+91", "");
                et_phone_number.setText(phone);
            }
            et_alt_phone_number.setText(fullProfilePOJO.getProfilePOJO().getAltMobile());
            tv_dob.setText(UtilityFunction.getServerConvertedDate(fullProfilePOJO.getProfilePOJO().getDateOfBirth()));

            switch (fullProfilePOJO.getProfilePOJO().getGender()) {
                case "1":
                    rb_male.setChecked(true);
                    break;
                case "2":
                    rb_female.setChecked(true);
                    break;
                case "3":
                    rb_other.setChecked(true);
                    break;
            }

            switch (fullProfilePOJO.getProfilePOJO().getMaritalStatus()) {
                case "1":
                    rb_single.setChecked(true);
                    break;
                case "2":
                    rb_married.setChecked(true);
                    break;
            }

        }
    }

    private void savePersonalInformation() {
        try {
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            String gender = "0";
            switch (rg_gender.getCheckedRadioButtonId()) {
                case R.id.rb_male:
                    gender = "1";
                    break;
                case R.id.rb_female:
                    gender = "2";
                    break;
                case R.id.rb_other:
                    gender = "3";
                    break;
            }

            String martial_status = "0";
            switch (rg_martial.getCheckedRadioButtonId()) {
                case R.id.rb_single:
                    martial_status = "1";
                    break;
                case R.id.rb_married:
                    martial_status = "2";
                    break;
            }

            reqEntity.addPart("user_id", new StringBody(fullProfilePOJO.getProfilePOJO().getUserId()));
            reqEntity.addPart("user_profile_id", new StringBody(fullProfilePOJO.getProfilePOJO().getUserProfileId()));
            reqEntity.addPart("fullname", new StringBody(et_first_name.getText().toString() + " " + et_last_name.getText().toString()));
            reqEntity.addPart("gender", new StringBody(gender));
            reqEntity.addPart("date_of_birth", new StringBody(tv_dob.getText().toString()));
            reqEntity.addPart("email", new StringBody(et_email.getText().toString()));
            reqEntity.addPart("mobile", new StringBody(et_phone_number.getText().toString()));
            reqEntity.addPart("alt_mobile", new StringBody(et_alt_phone_number.getText().toString()));
            reqEntity.addPart("martial_status", new StringBody(martial_status));
            reqEntity.addPart("bio", new StringBody(et_bio.getText().toString()));

            new WebUploadService(reqEntity, getActivity(), new WebServicesCallBack() {
                @Override
                public void onGetMsg(String apicall, String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                            ToastClass.showShortToast(getActivity().getApplicationContext(), "Profile Info Updated");
                            Pref.SetStringPref(getActivity().getApplicationContext(), StringUtils.USER_PROFILE, jsonObject.optJSONObject("result").optJSONObject("UserProfileCitizen").toString());
                            UserProfilePOJO userProfilePOJO = new Gson().fromJson(jsonObject.optJSONObject("result").optJSONObject("UserProfileCitizen").toString(), UserProfilePOJO.class);
                            Constants.userProfilePOJO = userProfilePOJO;
                            SetViews.changeProfileNames();
                            ll_address_header.callOnClick();
                        } else {
                            ToastClass.showShortToast(getActivity().getApplicationContext(), jsonObject.optString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d(TagUtils.getTag(), apicall + ":-" + response);
                }
            }, "CALL_SAVE_PROFILE_API", true).execute(WebServicesUrls.UPDATE_PROFILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initiateGalleryPicker() {
        //0=camera,1=Gallery,2=Camera and gallery
        new ImagePicker.Builder(getActivity())
                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            List<String> mPaths = (List<String>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PATH);
            //Your Code
//            if (mPaths.size() > 0) {
//                setProfilePic(mPaths.get(0));
//            }
            if(mPaths.size()>0){
                cropPic(mPaths.get(0));
            }

        }else if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            setProfilePic(resultUri.getPath());
        }
    }

    public void cropPic(String source) {
        if (new File(source).exists()) {
            Uri uri = Uri.fromFile(new File(source));
            String destPath = FileUtils.getPhotoFolder() + File.separator + System.currentTimeMillis() + ".png";
            Uri destUri = Uri.fromFile(new File(destPath));
            UCrop.of(uri, destUri)
                    .start(getActivity());
        } else {
            ToastClass.showShortToast(getActivity().getApplicationContext(), "File is corrupted");
        }
    }

    public void setUpPersonalData(UserProfilePOJO profilePOJO) {
        et_first_name.setText(profilePOJO.getFirstName());
        et_last_name.setText(profilePOJO.getLastName());
        et_email.setText(profilePOJO.getEmail());
        et_bio.setText(profilePOJO.getUserBio());
//        et_dob.setText(profilePOJO.getDateOfBirth());
        if (profilePOJO.getMobile().length() > 0) {
            String phone = profilePOJO.getMobile().replace("+91", "");
            et_phone_number.setText(phone);
        }
        et_alt_phone_number.setText(profilePOJO.getAltMobile());


        switch (profilePOJO.getGender()) {
            case "1":
                rb_male.setChecked(true);
                break;
            case "2":
                rb_female.setChecked(true);
                break;
            case "3":
                rb_other.setChecked(true);
                break;
        }

        switch (profilePOJO.getMaritalStatus()) {
            case "1":
                rb_single.setChecked(true);
                break;
            case "2":
                rb_married.setChecked(true);
                break;
        }

        Glide.with(getActivity().getApplicationContext())
                .load(profilePOJO.getProfilePhotoPath())
                .error(R.drawable.ic_default_profile_pic)
                .placeholder(R.drawable.ic_default_profile_pic)
                .dontAnimate()
                .into(cv_profile_pic);

    }

    public void setProfilePic(String image_path_string) {

        if (new File(image_path_string).exists()) {
            Glide.with(getActivity().getApplicationContext())
                    .load(image_path_string)
                    .error(R.drawable.ic_default_profile_pic)
                    .placeholder(R.drawable.ic_default_profile_pic)
                    .dontAnimate()
                    .into(cv_profile_pic);


            try {
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                if (image_path_string.length() > 0 && new File(image_path_string).exists()) {
                    FileBody bin1 = new FileBody(new File(image_path_string));
                    reqEntity.addPart("photo", bin1);
                } else {
                    reqEntity.addPart("photo", new StringBody(""));
                }

                reqEntity.addPart("user_id", new StringBody(userProfilePOJO.getUserId()));
                reqEntity.addPart("user_profile_id", new StringBody(userProfilePOJO.getUserProfileId()));

                new WebUploadService(reqEntity, getActivity(), new WebServicesCallBack() {
                    @Override
                    public void onGetMsg(String apicall, String response) {
                        Log.d(TagUtils.getTag(), apicall + ":-" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("status").equals("success")) {
                                Gson gson = new Gson();
                                Pref.SetStringPref(getActivity().getApplicationContext(), StringUtils.USER_PROFILE, jsonObject.optJSONObject("result").toString());
                                UserProfilePOJO userProfilePOJO = gson.fromJson(jsonObject.optJSONObject("result").toString(), UserProfilePOJO.class);
                                SetViews.changeProfilePics(getActivity().getApplicationContext(), userProfilePOJO.getProfilePhotoPath());
                                Constants.userProfilePOJO = userProfilePOJO;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, "CALL_SAVE_PROFILE_API", true).execute(WebServicesUrls.UPDATE_USER_PROFILE_PHOTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void hideAddress() {
        iv_address_drop.setImageResource(R.drawable.ic_drop);
        ll_address_view.setVisibility(View.GONE);

    }

    public void hidePersonal() {
        iv_personal_drop.setImageResource(R.drawable.ic_drop);
        ll_personal_view.setVisibility(View.GONE);

    }

    public void hideEducation() {
        iv_education_drop.setImageResource(R.drawable.ic_drop);
        ll_education_view.setVisibility(View.GONE);

    }

    public void hideWork() {
        iv_work_drop.setImageResource(R.drawable.ic_drop);
        ll_work_view.setVisibility(View.GONE);

    }

    public void showAddress() {
        iv_address_drop.setImageResource(R.drawable.ic_drop_up);
        ll_address_view.setVisibility(View.VISIBLE);

    }

    public void showPersonal() {
        iv_personal_drop.setImageResource(R.drawable.ic_drop_up);
        ll_personal_view.setVisibility(View.VISIBLE);

    }

    public void showEducation() {
        iv_education_drop.setImageResource(R.drawable.ic_drop_up);
        ll_education_view.setVisibility(View.VISIBLE);

    }

    public void showWork() {
        iv_work_drop.setImageResource(R.drawable.ic_drop_up);
        ll_work_view.setVisibility(View.VISIBLE);

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

        tv_dob.setText(date);
    }
}
