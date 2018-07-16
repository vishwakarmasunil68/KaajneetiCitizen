package com.ritvi.kaajneeti.fragment.Express;

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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.ritvi.kaajneeti.adapter.PollAnsAdapter;
import com.ritvi.kaajneeti.pojo.location.LocationPOJO;
import com.ritvi.kaajneeti.pojo.poll.PollAnsPOJO;
import com.ritvi.kaajneeti.pojo.poll.PollMediaAnsPOJO;
import com.ritvi.kaajneeti.pojo.poll.PollPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;
import com.ritvi.kaajneeti.webservice.WebUploadService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CreatePollFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.iv_camera)
    ImageView iv_camera;
    @BindView(R.id.iv_tag)
    ImageView iv_tag;
    @BindView(R.id.iv_location)
    ImageView iv_location;
    @BindView(R.id.tv_post)
    TextView tv_post;
    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @BindView(R.id.spinner_pubpri)
    Spinner spinner_pubpri;
    @BindView(R.id.et_question)
    EditText et_question;
    @BindView(R.id.cv_profile_pic)
    CircleImageView cv_profile_pic;
    @BindView(R.id.tv_add_option)
    TextView tv_add_option;
    @BindView(R.id.rv_options)
    RecyclerView rv_options;
    @BindView(R.id.tv_start_date)
    TextView tv_start_date;
    @BindView(R.id.iv_start_date)
    ImageView iv_start_date;
    @BindView(R.id.tv_end_date)
    TextView tv_end_date;
    @BindView(R.id.iv_end_date)
    ImageView iv_end_date;
    @BindView(R.id.iv_select_image)
    ImageView iv_select_image;
    @BindView(R.id.iv_poll_image)
    ImageView iv_poll_image;
    @BindView(R.id.frame_start_date)
    FrameLayout frame_start_date;
    @BindView(R.id.frame_end_date)
    FrameLayout frame_end_date;

    boolean start_date = true;

    LocationPOJO locationPOJO;
    List<UserProfilePOJO> taggeduserInfoPOJOS = new ArrayList<>();

    String tagging_description = "";
    String profile_description = "";
    String place_description = "";
    boolean question_image = true;
    PollPOJO pollPOJO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_create_poll, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        if (getArguments() != null) {
            pollPOJO = (PollPOJO) getArguments().getSerializable("pollPOJO");
        }

        Glide.with(getActivity().getApplicationContext())
                .load(Constants.userProfilePOJO.getProfilePhotoPath())
                .error(R.drawable.ic_default_profile_pic)
                .placeholder(R.drawable.ic_default_profile_pic)
                .dontAnimate()
                .into(cv_profile_pic);
        profile_description = "<b>" + Constants.userProfilePOJO.getFirstName() + " " + Constants.userProfilePOJO.getLastName() + "</b> ";
        updateProfileStatus();

        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateGalleryPicker();
            }
        });
        iv_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), TagPeopleActivity.class).putExtra("taggedpeople", (Serializable) taggeduserInfoPOJOS), Constants.ACTIVITY_TAG_PEOPLE);
            }
        });
        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocation();
            }
        });

        attachListAdapter();


        tv_add_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pollMediaAnsPOJOS.size() < 5) {
                    addAns();
                    Log.d(TagUtils.getTag(), "poll ans size:-" + pollMediaAnsPOJOS.size());
                }
            }
        });

        tv_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPoll();
            }
        });

        tv_start_date.setText(UtilityFunction.getCurrentDate());
        getNextWeek();
        iv_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_date = true;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CreatePollFragment.this,
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

        iv_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_date = false;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CreatePollFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });

        frame_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_end_date.callOnClick();
            }
        });


        iv_select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question_image = true;
                chooseImage();
            }
        });
        iv_poll_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu menu = new PopupMenu(getActivity(), v);

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuitem) {
                        switch (menuitem.getItemId()) {
                            case R.id.popup_change:
                                iv_select_image.callOnClick();
                                break;
                            case R.id.popup_remove:
                                Glide.with(getActivity().getApplicationContext())
                                        .load(R.drawable.ic_default_pic)
                                        .into(iv_poll_image);
                                question_image_path = "";
                                iv_select_image.setVisibility(View.GONE);
//                                holder.iv_ans.setVisibility(View.GONE);
                                break;
                        }
                        return false;
                    }
                });
                menu.inflate(R.menu.menu_poll);
                menu.show();
            }
        });

        if (pollPOJO != null) {
            Log.d(TagUtils.getTag(), "poll id:-" + pollPOJO.getPollId());
            tv_profile_name.setText(pollPOJO.getProfileDetailPOJO().getFirstName() + " " + pollPOJO.getProfileDetailPOJO().getLastName());
            tv_end_date.setText(pollPOJO.getValidEndDate());
            tv_start_date.setText(pollPOJO.getValidFromDate());
            if (pollPOJO.getPollPrivacy().equalsIgnoreCase("1")) {
                spinner_pubpri.setSelection(1);
            } else {
                spinner_pubpri.setSelection(0);
            }
            et_question.setText(pollPOJO.getPollQuestion());
            if (pollPOJO.getPollImage().length() > 0) {
                iv_poll_image.setVisibility(View.VISIBLE);
                Glide.with(getActivity().getApplicationContext())
                        .load(pollPOJO.getPollImage())
                        .placeholder(R.drawable.ic_default_pic)
                        .error(R.drawable.ic_default_pic)
                        .dontAnimate()
                        .into(iv_poll_image);
            } else {
                iv_poll_image.setVisibility(View.GONE);
            }

            if (pollPOJO.getPollAnsPOJOS().size() > 0) {
                for (PollAnsPOJO pollAnsPOJO : pollPOJO.getPollAnsPOJOS()) {
                    PollMediaAnsPOJO pollMediaAnsPOJO = new PollMediaAnsPOJO();
                    pollMediaAnsPOJO.setImage(pollAnsPOJO.getPollAnswerImage());
                    pollMediaAnsPOJO.setAns(pollAnsPOJO.getPollAnswer());
                    pollMediaAnsPOJO.setServer(true);

                    pollMediaAnsPOJOS.add(pollMediaAnsPOJO);
                }
                pollAnsAdapter.notifyDataSetChanged();
            }
//            if (pollPOJO.getServerLocationPOJO() != null) {
//                LocationPOJO locationPOJO = new LocationPOJO();
//                locationPOJO.setPlaceId(eventPOJO.getServerLocationPOJO().getPlaceId());
//                locationPOJO.setFormatted_address(eventPOJO.getServerLocationPOJO().getLocationAddress());
//                locationPOJO.setUrl(eventPOJO.getServerLocationPOJO().getLocationUrl());
//                locationPOJO.setAdr_address(eventPOJO.getServerLocationPOJO().getLocationAddress());
//                locationPOJO.setVicinity(eventPOJO.getServerLocationPOJO().getLocationVicinity());
//
//                GeometryPOJO geometryPOJO = new GeometryPOJO();
//                LatLongPOJO latLongPOJO = new LatLongPOJO();
//                latLongPOJO.setLat(UtilityFunction.getFormattedValue(eventPOJO.getServerLocationPOJO().getLocationLattitude()));
//                latLongPOJO.setLng(UtilityFunction.getFormattedValue(eventPOJO.getServerLocationPOJO().getLocationLongitude()));
//
//                geometryPOJO.setLocation(latLongPOJO);
//                locationPOJO.setGeometry(geometryPOJO);
//                this.locationPOJO = locationPOJO;
//
//                tv_event_location.setText(locationPOJO.getFormatted_address());
//            }

        } else {
            addAns();
            addAns();
        }
    }

    public void getNextWeek() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        tv_end_date.setText(dateFormat.format(cal.getTime()));
    }


    public void checkPoll() {
        boolean image_present = false;
        boolean all_image_present = true;
        boolean all_text_present = true;
        for (PollMediaAnsPOJO pollMediaAnsPOJO : pollMediaAnsPOJOS) {
            if (pollMediaAnsPOJO.getImage().length() > 0) {
                image_present = true;
            } else {
                all_image_present = false;
            }

            if (pollMediaAnsPOJO.getAns().trim().length() == 0) {
                all_text_present = false;
            }
        }

        if (image_present) {
            if (all_image_present) {
                if (all_text_present) {
                    createPoll();
                } else {
                    ToastClass.showShortToast(getActivity().getApplicationContext(), "Please Enter all anwers text");
                }
            } else {
                ToastClass.showShortToast(getActivity().getApplicationContext(), "Please Select image in all ans");
            }
        } else {
            if (all_text_present) {
                createPoll();
            } else {
                ToastClass.showShortToast(getActivity().getApplicationContext(), "Please Enter all anwers text");
            }
        }

    }

    public void addAns() {
        PollMediaAnsPOJO pollMediaAnsPOJO = new PollMediaAnsPOJO();
        pollMediaAnsPOJOS.add(pollMediaAnsPOJO);
//        pollAnsAdapter.notifyDataSetChanged();
        attachListAdapter();
    }


    PollAnsAdapter pollAnsAdapter;
    List<PollMediaAnsPOJO> pollMediaAnsPOJOS = new ArrayList<>();

    public void attachListAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_options.setLayoutManager(linearLayoutManager);
        pollAnsAdapter = new PollAnsAdapter(getActivity(), this, pollMediaAnsPOJOS);
        rv_options.setHasFixedSize(true);
        rv_options.setAdapter(pollAnsAdapter);
        rv_options.setNestedScrollingEnabled(false);
        rv_options.setItemAnimator(new DefaultItemAnimator());

    }

    PollMediaAnsPOJO selectedpollAnsPOJO;
    ImageView selectedPollImage;

    public void selectImage(PollMediaAnsPOJO pollMediaAnsPOJO, ImageView iv_ans) {
        this.selectedpollAnsPOJO = pollMediaAnsPOJO;
        this.selectedPollImage = iv_ans;
        question_image = false;
        chooseImage();
    }

    public void chooseImage() {
        new ImagePicker.Builder(getActivity())
                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();
    }


    public void createPoll() {
        try {
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("user_profile_id", new StringBody(Constants.userProfilePOJO.getUserProfileId()));
            reqEntity.addPart("poll_question", new StringBody(et_question.getText().toString()));
            if (spinner_pubpri.getSelectedItemPosition() == 0) {
                reqEntity.addPart("privacy", new StringBody("1"));
            } else {
                reqEntity.addPart("privacy", new StringBody("0"));
            }
            reqEntity.addPart("valid_from_date", new StringBody(tv_start_date.getText().toString()));
            reqEntity.addPart("valid_end_date", new StringBody(tv_end_date.getText().toString()));

            int count = 0;
            for (PollMediaAnsPOJO pollMediaAnsPOJO : pollMediaAnsPOJOS) {
                if(pollMediaAnsPOJO.isServer()){
                    reqEntity.addPart("file[" + count + "]", new StringBody(pollMediaAnsPOJO.getImage()));
                }else {
                    if (pollMediaAnsPOJO.getImage().length() > 0 && new File(pollMediaAnsPOJO.getImage()).exists()) {
                        reqEntity.addPart("file[" + count + "]", new FileBody(new File(pollMediaAnsPOJO.getImage())));
                    } else {
                        reqEntity.addPart("file[" + count + "]", new StringBody(""));
                    }
                }
                Log.d(TagUtils.getTag(), "poll ans:-" + pollMediaAnsPOJO.getAns());
                reqEntity.addPart("poll_answer[" + count + "]", new StringBody(pollMediaAnsPOJO.getAns()));
                count++;
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

//            reqEntity.addPart("poll_answer[" + 0 + "]", new StringBody(et_ans1.getText().toString()));
//            reqEntity.addPart("poll_answer[" + 1 + "]", new StringBody(et_ans2.getText().toString()));
//
            if (question_image_path.length() > 0 && new File(question_image_path).exists()) {
                reqEntity.addPart("question", new FileBody(new File(question_image_path)));
            } else {
                reqEntity.addPart("question", new StringBody(et_question.getText().toString()));
            }
            String url = "";
            if (pollPOJO != null) {
                reqEntity.addPart("poll_id", new StringBody(pollPOJO.getPollId()));
                url = WebServicesUrls.UPDATE_MY_POLL;
            } else {
                url = WebServicesUrls.SAVE_MY_POLL;
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
                        } else {
                            ToastClass.showShortToast(getActivity().getApplicationContext(), jsonObject.optString(jsonObject.optString("message")));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "CREATE_POLL", true).execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
//
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

    String question_image_path = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.ACTIVITY_TAG_PEOPLE) {
            if (resultCode == Activity.RESULT_OK) {
                taggeduserInfoPOJOS = (List<UserProfilePOJO>) data.getSerializableExtra("taggedpeople");
                tagging_description = getTaggedDescription(taggeduserInfoPOJOS);
                updateProfileStatus();
            }
        } else if (requestCode == Constants.ACTIVITY_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                locationPOJO = (LocationPOJO) data.getSerializableExtra("location");
                place_description = " - at <b>" + locationPOJO.getFormatted_address() + "</b>";
                updateProfileStatus();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                locationPOJO = null;
            }
        } else if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            List<String> mPaths = (List<String>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PATH);
            if (mPaths.size() > 0) {
                Log.d(TagUtils.getTag(), "image paths:-" + mPaths.toString());
                if (question_image) {
                    question_image_path = mPaths.get(0);
                    Glide.with(getActivity().getApplicationContext())
                            .load(mPaths.get(0))
                            .into(iv_poll_image);
                    iv_poll_image.setVisibility(View.VISIBLE);
                } else {
                    selectedpollAnsPOJO.setImage(mPaths.get(0));
                    pollAnsAdapter.notifyDataSetChanged();
                }
            }
        }
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
}
