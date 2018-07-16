package com.ritvi.kaajneeti.fragment.complaint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.adapter.AttachMediaAdapter;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentContants;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.allfeeds.MediaPOJO;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintPOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;
import com.ritvi.kaajneeti.webservice.WebUploadService;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunil on 18-04-2018.
 */

public class CreateComplaintReplyFragment extends FragmentController {

    static final int OPEN_MEDIA_PICKER = 1;

    @BindView(R.id.et_title)
    EditText et_title;
    @BindView(R.id.et_description)
    EditText et_description;
    @BindView(R.id.iv_attach)
    ImageView iv_attach;
    @BindView(R.id.rv_attachments)
    RecyclerView rv_attachments;
    @BindView(R.id.btn_post)
    Button btn_post;
    @BindView(R.id.spinner_complaint_status)
    Spinner spinner_complaint_status;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;

    String complaint_id="";
    ComplaintPOJO complaintPOJO;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_create_complaint_reply, container, false);
        setUpView(getActivity(),this,view);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments()!=null) {
            complaint_id = getArguments().getString("complaint_id");
            getComplaintDetail();
        }
        attachMediaAdapter();

        iv_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiateGalleryPicker();
            }
        });

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveComplaint();
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    public void getComplaintDetail(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("complaint_id", complaint_id));
        new WebServiceBaseResponse<ComplaintPOJO>(nameValuePairs, getActivity(), new ResponseCallBack<ComplaintPOJO>() {
            @Override
            public void onGetMsg(ResponsePOJO<ComplaintPOJO> responsePOJO) {
                try {
                    if (responsePOJO.isSuccess()) {
                        complaintPOJO = responsePOJO.getResult();
                        if(complaintPOJO.getComplaintStatus().equalsIgnoreCase("4")){
                            btn_post.setVisibility(View.GONE);
                        }else{
                            btn_post.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, ComplaintPOJO.class, "COMPLAINT_DETAIL", false).execute(WebServicesUrls.GET_COMPLAINT_DETAIL);
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

    AttachMediaAdapter attachMediaAdapter;
    List<MediaPOJO> attachPathString = new ArrayList<>();

    public void attachMediaAdapter() {
        attachMediaAdapter = new AttachMediaAdapter(getActivity(), this, attachPathString);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_attachments.setHasFixedSize(true);
        rv_attachments.setAdapter(attachMediaAdapter);
        rv_attachments.setLayoutManager(linearLayoutManager);
        rv_attachments.setItemAnimator(new DefaultItemAnimator());

    }



    public void saveComplaint() {
        try {

            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("", ""));
            reqEntity.addPart("user_profile_id", new StringBody(Constants.userProfilePOJO.getUserProfileId()));
            reqEntity.addPart("complaint_id", new StringBody(complaint_id));
            reqEntity.addPart("history_id", new StringBody(""));
            reqEntity.addPart("title", new StringBody("title"));
            reqEntity.addPart("description", new StringBody(et_description.getText().toString()));
//            if(spinner_progress.getSelectedItemPosition()==0) {
//                reqEntity.addPart("current_status", new StringBody("0"));
//            }else{
//                reqEntity.addPart("current_status", new StringBody("4"));
//            }

            int count = 0;
            if(spinner_complaint_status.getSelectedItemPosition()==1){
                reqEntity.addPart("current_status", new StringBody("4"));
            }

//
//            for (String file_path : mediaFiles) {
//                reqEntity.addPart("file[" + (count) + "]", new FileBody(new File(file_path)));
//                reqEntity.addPart("thumb[" + (count) + "]", new StringBody(""));
//            }

            for (int i = 0; i < attachPathString.size(); i++) {
                reqEntity.addPart("file[" + (i) + "]", new FileBody(new File(attachPathString.get(i).getPath())));
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
                            activityManager.popBackResultFragment(startingFragment, requestCode, FragmentContants.RESULT_OK, null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "CREATE COMPLAINT REPLY", true).execute(WebServicesUrls.SAVE_COMPLAINT_HISTORY);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            List<String> mPaths = (List<String>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PATH);
            if (mPaths.size() > 0) {
                for (String path : mPaths) {
                    if (!isImagePresent(path)) {
                        MediaPOJO mediaPOJO = new MediaPOJO();
                        mediaPOJO.setPath(path);
                        attachPathString.add(mediaPOJO);
                    }
                }
                attachMediaAdapter.notifyDataSetChanged();
            }
        }
    }


    public boolean isImagePresent(String path) {
        for (MediaPOJO mediaPOJO : attachPathString) {
            if (mediaPOJO.getPath().equalsIgnoreCase(path)) {
                return true;
            }
        }
        return false;
    }
}
