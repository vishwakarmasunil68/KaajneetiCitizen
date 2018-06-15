package com.ritvi.kaajneeti.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.FileUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.activity.loginregistration.LoginActivity;
import com.ritvi.kaajneeti.adapter.ViewPagerAdapter;
import com.ritvi.kaajneeti.fragment.search.SearchAllFragment;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.attachments.AttachmentPOJO;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AttachmentViewPagerFragment extends FragmentController {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.iv_menu)
    ImageView iv_menu;
    @BindView(R.id.ll_save_phone)
    LinearLayout ll_save_phone;
    @BindView(R.id.ll_share_external)
    LinearLayout ll_share_external;
    @BindView(R.id.ll_report)
    LinearLayout ll_report;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout sliding_layout;

    List<AttachmentPOJO> attachmentPOJOS = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_attachment_viewpager, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            attachmentPOJOS.addAll((ArrayList<AttachmentPOJO>) getArguments().getSerializable("attachments"));
            if (attachmentPOJOS.size() > 0) {
                setupViewPager(viewPager);
            }
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityManager.startFragment(R.id.frame_home,new SearchAllFragment());
            }
        });

        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sliding_layout != null &&
                        (sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                    sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                } else {
                    sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }
            }
        });

        ll_save_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_menu.callOnClick();
                saveImage(attachmentPOJOS.get(viewPager.getCurrentItem()).getFile_path(),false);
            }
        });

        ll_share_external.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_menu.callOnClick();
                saveImage(attachmentPOJOS.get(viewPager.getCurrentItem()).getFile_path(),true);
            }
        });
    }

    public void saveImage(final String picture_url, final boolean is_share){
        new AsyncTask<Void, Void, Void>() {
            File file = new File(FileUtils.getdownloadVideo() + File.separator +  System.currentTimeMillis() + ".png");

            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                try {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                InputStream input = null;
                OutputStream output = null;
                try {

                    URL url = new URL(picture_url);
                    input = url.openStream();
                    output = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int bytesRead = 0;
                    while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                        output.write(buffer, 0, bytesRead);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(progressDialog!=null&&progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                if(is_share){
                    try {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/png");
                        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.toString()));
                        startActivity(Intent.createChooser(share, "Share Image"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    ToastClass.showShortToast(getActivity().getApplicationContext(),"photo saved");
                }
            }
        }.execute();
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (AttachmentPOJO attachmentPOJO : attachmentPOJOS) {
            AttachmentsViewFragment attachmentsViewFragment = new AttachmentsViewFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("attachment", attachmentPOJO);
            attachmentsViewFragment.setArguments(bundle);
            adapter.addFrag(attachmentsViewFragment, "attachmentsViewFragment");
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());

    }
}
