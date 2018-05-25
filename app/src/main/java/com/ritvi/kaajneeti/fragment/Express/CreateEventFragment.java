package com.ritvi.kaajneeti.fragment.Express;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.FileUtils;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.Util.UtilityFunction;
import com.ritvi.kaajneeti.activity.express.CheckInActivity;
import com.ritvi.kaajneeti.activity.express.TagPeopleActivity;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.pojo.location.NewLocationPOJO;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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
    @BindView(R.id.tv_tag_people)
    TextView tv_tag_people;
    @BindView(R.id.iv_tag)
    ImageView iv_tag;
    @BindView(R.id.iv_post)
    ImageView iv_post;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.spinner_pubpri)
    Spinner spinner_pubpri;

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

    List<UserProfilePOJO> taggeduserInfoPOJOS = new ArrayList<>();
    String check_in;
    String privPublic;
    String event_name;
    List<String> mediaFiles = new ArrayList<>();


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

        iv_post.setOnClickListener(new View.OnClickListener() {
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
    }

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
            reqEntity.addPart("event_location", new StringBody(tv_event_location.getText().toString()));

            String start_date = getFormattedDate(tv_start_date.getText().toString());
            String end_date = getFormattedDate(tv_end_date.getText().toString());

            Log.d(TagUtils.getTag(), "start_date:-" + start_date);
            Log.d(TagUtils.getTag(), "end_date:-" + end_date);

            reqEntity.addPart("start_date", new StringBody(start_date));
            reqEntity.addPart("end_date", new StringBody(end_date));
            reqEntity.addPart("EveryYear", new StringBody(""));
            reqEntity.addPart("EveryMonth", new StringBody(""));
            reqEntity.addPart("event_attendee", new StringBody(""));

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
            }, "CREATE_EVENT", true).execute(WebServicesUrls.SAVE_EVENT);
        } catch (UnsupportedEncodingException e) {
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
                Log.d(TagUtils.getTag(),"image paths:-"+mPaths.toString());
                updateCoverImage(cover_image_path);
            }
        } else if (requestCode == Constants.ACTIVITY_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                NewLocationPOJO newLocationPOJO = (NewLocationPOJO) data.getSerializableExtra("location");
                check_in = newLocationPOJO.getMain_text() + "," + newLocationPOJO.getSecondary_text();
                tv_event_location.setText(check_in);
            }
        } else if (requestCode == Constants.ACTIVITY_TAG_PEOPLE) {
            if (resultCode == Activity.RESULT_OK) {
                taggeduserInfoPOJOS = (List<UserProfilePOJO>) data.getSerializableExtra("taggedpeople");

//                tagging_description = getTaggedDescription(taggeduserInfoPOJOS);
//                updateProfileStatus();
            }
        }
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
