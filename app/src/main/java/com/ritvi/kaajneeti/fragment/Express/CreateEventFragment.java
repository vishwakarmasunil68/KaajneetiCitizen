package com.ritvi.kaajneeti.fragment.Express;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.ritvi.kaajneeti.adapter.TagShowAdapter;
import com.ritvi.kaajneeti.pojo.allfeeds.MediaPOJO;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintAttachmentPOJO;
import com.ritvi.kaajneeti.pojo.event.EventPOJO;
import com.ritvi.kaajneeti.pojo.location.GeometryPOJO;
import com.ritvi.kaajneeti.pojo.location.LatLongPOJO;
import com.ritvi.kaajneeti.pojo.location.LocationPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;
import com.ritvi.kaajneeti.webservice.WebUploadService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CreateEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @BindView(R.id.cv_profile_pic)
    CircleImageView cv_profile_pic;
    @BindView(R.id.iv_event_image)
    ImageView iv_event_image;
    @BindView(R.id.iv_select_image)
    ImageView iv_select_image;
    @BindView(R.id.et_event_name)
    EditText et_event_name;
    @BindView(R.id.tv_event_location)
    TextView tv_event_location;
    @BindView(R.id.iv_location)
    ImageView iv_location;
    @BindView(R.id.tv_start_date)
    TextView tv_start_date;
    @BindView(R.id.iv_start_date)
    ImageView iv_start_date;
    @BindView(R.id.tv_end_date)
    TextView tv_end_date;
    @BindView(R.id.iv_end_date)
    ImageView iv_end_date;
    @BindView(R.id.et_description)
    EditText et_description;
    @BindView(R.id.rv_tags)
    RecyclerView rv_tags;
    @BindView(R.id.iv_tag)
    ImageView iv_tag;
    @BindView(R.id.tv_post)
    TextView tv_post;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.spinner_pubpri)
    Spinner spinner_pubpri;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.frame_location)
    FrameLayout frame_location;
    @BindView(R.id.frame_start_date)
    FrameLayout frame_start_date;
    @BindView(R.id.frame_end_date)
    FrameLayout frame_end_date;

    boolean start_date = false;
    boolean start_time = false;

    private final String COVER_IMAGE = "cover_image";
    private final String COVER_IMAGE_GALLERY = "cover_image_gallery";
    private final String ATTACH_CAMERA = "attach_image";
    private final String ATTACH_CAMERA_GALLERY = "attach_image_gallery";
    private final String ATTACH_VIDEO = "attach_video";
    private final String ATTACH_VIDEO_GALLERY = "attach_video_gallery";

    String attach_type = "";

    String cover_image_path = "";

    String profile_description = "";
    String tagging_description = "";
    LocationPOJO locationPOJO;

    List<UserProfilePOJO> taggeduserInfoPOJOS = new ArrayList<>();
    String check_in;
    String privPublic;
    String event_name;
    List<String> mediaFiles = new ArrayList<>();

    EventPOJO eventPOJO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_create_event, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            eventPOJO = (EventPOJO) getArguments().getSerializable("eventPOJO");
        }

        Glide.with(getActivity().getApplicationContext())
                .load(Constants.userProfilePOJO.getProfilePhotoPath())
                .error(R.drawable.ic_default_profile_pic)
                .placeholder(R.drawable.ic_default_profile_pic)
                .dontAnimate()
                .into(cv_profile_pic);

        tv_profile_name.setText(Constants.userProfilePOJO.getFirstName() + " " + Constants.userProfilePOJO.getLastName());

        iv_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_date = true;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CreateEventFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
        frame_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_start_date.callOnClick();
            }
        });
        frame_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_end_date.callOnClick();
            }
        });
        tv_start_date.setText(UtilityFunction.getCurrentDate());
        getNextMonth();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelEvent();
            }
        });

        iv_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_date = false;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CreateEventFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
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
        frame_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocation();
            }
        });

        tv_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEvent();
            }
        });


        iv_select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attach_type = COVER_IMAGE;
//                selectMedia();
                final PopupMenu menu = new PopupMenu(getActivity(), view);

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuitem) {
                        switch (menuitem.getItemId()) {
                            case R.id.popup_camera:
                                initiateCameraPicker();
                                break;
                            case R.id.popup_gallery:
                                initiateGalleryPicker();
                                break;
                            case R.id.popup_remove:
                                delete_image="1";
                                cover_image_path = "";
                                Glide.with(getActivity().getApplicationContext())
                                        .load(R.drawable.ic_default_pic)
                                        .into(iv_event_image);
                                break;
                        }
                        return false;
                    }
                });
                menu.inflate(R.menu.menu_media_picker);
                menu.show();
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelEvent();
            }
        });


        if (eventPOJO != null) {

            et_event_name.setText(eventPOJO.getEventName());
            et_description.setText(eventPOJO.getEventDescription());
            tv_start_date.setText(UtilityFunction.getServerConvertedDate(eventPOJO.getStartDate()));
            tv_end_date.setText(UtilityFunction.getServerConvertedDate(eventPOJO.getEndDate()));

//            if (eventPOJO.getEventAttachment().size() > 0) {
                Glide.with(getActivity().getApplicationContext())
                        .load(eventPOJO.getEventCoverPhoto())
                        .error(R.drawable.ic_default_profile_pic)
                        .placeholder(R.drawable.ic_default_profile_pic)
                        .dontAnimate()
                        .into(iv_event_image);
//            }

            if(eventPOJO.getEventAttendee()!=null&&eventPOJO.getEventAttendee().size()>0){
                taggeduserInfoPOJOS=eventPOJO.getEventAttendee();
                attachTagPeopleAdapter();
            }

//            taggeduserInfoPOJOS.addAll(eventPOJO.getEventAttendee());

//            if (eventPOJO.getprivac().equalsIgnoreCase("1")) {
//                spinner_pubpri.setSelection(0);
//            } else {
//                spinner_pubpri.setSelection(1);
//            }

//            et_whats_new.setText(postPOJO.getPostTitle());

            if (eventPOJO.getServerLocationPOJO() != null) {
                LocationPOJO locationPOJO = new LocationPOJO();
                locationPOJO.setPlaceId(eventPOJO.getServerLocationPOJO().getPlaceId());
                locationPOJO.setFormatted_address(eventPOJO.getServerLocationPOJO().getLocationAddress());
                locationPOJO.setUrl(eventPOJO.getServerLocationPOJO().getLocationUrl());
                locationPOJO.setAdr_address(eventPOJO.getServerLocationPOJO().getLocationAddress());
                locationPOJO.setVicinity(eventPOJO.getServerLocationPOJO().getLocationVicinity());

                GeometryPOJO geometryPOJO = new GeometryPOJO();
                LatLongPOJO latLongPOJO = new LatLongPOJO();
                latLongPOJO.setLat(UtilityFunction.getFormattedValue(eventPOJO.getServerLocationPOJO().getLocationLattitude()));
                latLongPOJO.setLng(UtilityFunction.getFormattedValue(eventPOJO.getServerLocationPOJO().getLocationLongitude()));

                geometryPOJO.setLocation(latLongPOJO);
                locationPOJO.setGeometry(geometryPOJO);
                this.locationPOJO = locationPOJO;

                tv_event_location.setText(locationPOJO.getFormatted_address());
            }
        }


        checkPostStatus();
        et_event_name.addTextChangedListener(textWatcher);
        tv_event_location.addTextChangedListener(textWatcher);
        tv_start_date.addTextChangedListener(textWatcher);
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



        if (et_event_name.getText().toString().length() == 0) {
            enable_post = false;
        }

        if (tv_event_location.getText().toString().length() == 0) {
            enable_post = false;
        }

        if (tv_start_date.getText().toString().length() == 0) {
            enable_post = false;
        }

        if (tv_end_date.getText().toString().length() == 0) {
            enable_post = false;
        }

        if (cover_image_path.length() == 0) {
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


    public void getNextMonth() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 30);
        tv_end_date.setText(dateFormat.format(cal.getTime()));
    }


    String delete_image="0";

    public void initiateCameraPicker() {
        //0=camera,1=Gallery,2=Camera and gallery
        new ImagePicker.Builder(getActivity())
                .mode(ImagePicker.Mode.CAMERA)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();
    }

    public void initiateGalleryPicker() {
        //0=camera,1=Gallery,2=Camera and gallery
        new ImagePicker.Builder(getActivity())
                .mode(ImagePicker.Mode.GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();
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
        if (start_date) {
            tv_start_date.setText(date);
        } else {
            tv_end_date.setText(date);
        }
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }

    public void saveEvent() {
        try {
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            reqEntity.addPart("user_profile_id", new StringBody(Constants.userProfilePOJO.getUserProfileId()));
            reqEntity.addPart("event_name", new StringBody(et_event_name.getText().toString()));
            reqEntity.addPart("event_description", new StringBody(et_description.getText().toString()));
            reqEntity.addPart("event_location", new StringBody(""));

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
            } else {
                reqEntity.addPart("event_location", new StringBody(""));
            }

            String start_date = getFormattedDate(tv_start_date.getText().toString());
            String end_date = getFormattedDate(tv_end_date.getText().toString());

            Log.d(TagUtils.getTag(), "start_date:-" + start_date);
            Log.d(TagUtils.getTag(), "end_date:-" + end_date);

            reqEntity.addPart("start_date", new StringBody(start_date));
            reqEntity.addPart("end_date", new StringBody(end_date));
            reqEntity.addPart("EveryYear", new StringBody(""));
            reqEntity.addPart("EveryMonth", new StringBody(""));

            int tagged_count=0;
            for(UserProfilePOJO userProfilePOJO:taggeduserInfoPOJOS){
                reqEntity.addPart("event_attendee["+tagged_count+"]", new StringBody(userProfilePOJO.getUserProfileId()));
                tagged_count++;
            }

            Log.d(TagUtils.getTag(),"delete_image:-"+delete_image);
            reqEntity.addPart("delete_image", new StringBody(delete_image));

            if (spinner_pubpri.getSelectedItemPosition() == 0) {
                reqEntity.addPart("privacy", new StringBody("1"));
            } else {
                reqEntity.addPart("privacy", new StringBody("0"));
            }
            boolean is_cover_set = false;
            int count = 0;
            if (new File(cover_image_path).exists()) {
                is_cover_set = true;
                FileBody coverFileBody = new FileBody(new File(cover_image_path));
                reqEntity.addPart("file[0]", coverFileBody);
                reqEntity.addPart("thumb[0]", new StringBody(""));
                count++;
            }

            String url="";
            if(eventPOJO!=null){
                reqEntity.addPart("event_id", new StringBody(eventPOJO.getEventId()));
                url=WebServicesUrls.UPDATE_EVENT;
            }else{
                url=WebServicesUrls.SAVE_EVENT;
            }

            printData();



            new WebUploadService(reqEntity, getActivity(), new WebServicesCallBack() {
                @Override
                public void onGetMsg(String apicall, String response) {
                    Log.d(TagUtils.getTag(), apicall + " :- " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        ToastClass.showShortToast(getActivity().getApplicationContext(), jsonObject.optString("message"));
                        if (jsonObject.optString("status").equals("success")) {
                            getActivity().finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "CREATE_EVENT", true).execute(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void printData(){
        try{
            ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("event_name",et_event_name.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("event_description",et_description.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("event_location",""));

            if (locationPOJO != null) {
                try {
                    nameValuePairs.add(new BasicNameValuePair("location_detail[place_id]",locationPOJO.getPlaceId()));
                    nameValuePairs.add(new BasicNameValuePair("location_detail[location_name]",locationPOJO.getFormatted_address()));
                    nameValuePairs.add(new BasicNameValuePair("location_detail[location_lant]",String.valueOf(locationPOJO.getGeometry().getLocation().getLat())));
                    nameValuePairs.add(new BasicNameValuePair("location_detail[location_long]",String.valueOf(locationPOJO.getGeometry().getLocation().getLng())));
                    nameValuePairs.add(new BasicNameValuePair("location_detail[location_url]",locationPOJO.getUrl()));
                    nameValuePairs.add(new BasicNameValuePair("location_detail[location_address]",locationPOJO.getAdr_address()));
                    nameValuePairs.add(new BasicNameValuePair("location_detail[location_vicinity]",locationPOJO.getVicinity()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                nameValuePairs.add(new BasicNameValuePair("event_location",""));
            }

            String start_date = getFormattedDate(tv_start_date.getText().toString());
            String end_date = getFormattedDate(tv_end_date.getText().toString());

            Log.d(TagUtils.getTag(), "start_date:-" + start_date);
            Log.d(TagUtils.getTag(), "end_date:-" + end_date);

            nameValuePairs.add(new BasicNameValuePair("start_date",start_date));
            nameValuePairs.add(new BasicNameValuePair("end_date",end_date));
            nameValuePairs.add(new BasicNameValuePair("EveryYear",""));
            nameValuePairs.add(new BasicNameValuePair("EveryMonth",""));

            int tagged_count=0;
            for(UserProfilePOJO userProfilePOJO:taggeduserInfoPOJOS){
                nameValuePairs.add(new BasicNameValuePair("event_attendee["+tagged_count+"]",userProfilePOJO.getUserProfileId()));
                tagged_count++;
            }

            Log.d(TagUtils.getTag(),"delete_image:-"+delete_image);
            nameValuePairs.add(new BasicNameValuePair("delete_image",delete_image));

            if (spinner_pubpri.getSelectedItemPosition() == 0) {
                nameValuePairs.add(new BasicNameValuePair("privacy","1"));
            } else {
                nameValuePairs.add(new BasicNameValuePair("privacy","0"));
            }
            boolean is_cover_set = false;
            int count = 0;
            if (new File(cover_image_path).exists()) {
                is_cover_set = true;
                nameValuePairs.add(new BasicNameValuePair("file[0]", cover_image_path));
                nameValuePairs.add(new BasicNameValuePair("thumb[0]",""));
                count++;
            }

            if(eventPOJO!=null){
                nameValuePairs.add(new BasicNameValuePair("event_id",eventPOJO.getEventId()));
            }

            String nmv = "";
            for (NameValuePair nameValuePair : nameValuePairs) {
                nmv = nmv + nameValuePair.getName() + " : " + nameValuePair.getValue() + "\n";
            }
            Log.d(TagUtils.getTag(), "nmv:-" + nmv);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public String getFormattedDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date d = sdf.parse(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formated_date = simpleDateFormat.format(d);
            return formated_date;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            List<String> mPaths = (List<String>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PATH);
            if (mPaths.size() > 0) {
                cover_image_path = mPaths.get(0);
                delete_image="1";
                Log.d(TagUtils.getTag(), "image paths:-" + mPaths.toString());
                updateCoverImage(cover_image_path);
            }
        } else if (requestCode == Constants.ACTIVITY_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                LocationPOJO locationPOJO = (LocationPOJO) data.getSerializableExtra("location");
                CreateEventFragment.this.locationPOJO = locationPOJO;
                check_in = locationPOJO.getFormatted_address();
                tv_event_location.setText(check_in);
            }
        } else if (requestCode == Constants.ACTIVITY_TAG_PEOPLE) {
            if (resultCode == Activity.RESULT_OK) {
                taggeduserInfoPOJOS = (List<UserProfilePOJO>) data.getSerializableExtra("taggedpeople");
                attachTagPeopleAdapter();
            }
        }
        checkPostStatus();
    }

    public void attachTagPeopleAdapter() {
        TagShowAdapter tagShowAdapter = new TagShowAdapter(getActivity(), this, taggeduserInfoPOJOS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_tags.setHasFixedSize(true);
        rv_tags.setAdapter(tagShowAdapter);
        rv_tags.setLayoutManager(linearLayoutManager);
        rv_tags.setItemAnimator(new DefaultItemAnimator());

    }

    public void updateCoverImage(String cover_image_path) {
        if (new File(cover_image_path).exists()) {
            Glide.with(getActivity().getApplicationContext())
                    .load(cover_image_path)
                    .error(R.drawable.ic_default_pic)
                    .placeholder(R.drawable.ic_default_pic)
                    .into(iv_event_image);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.ACCESS_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    UtilityFunction.getLocation(getActivity().getApplicationContext());
                }
                return;
            }

        }
    }

    public void cancelEvent() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("Do you want to discard Event?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finishAffinity();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

}
