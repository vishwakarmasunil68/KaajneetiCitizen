package com.ritvi.kaajneeti.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.attachments.AttachmentPOJO;

import butterknife.BindView;

public class AttachmentsViewFragment extends FragmentController {


    @BindView(R.id.iv_image)
    ImageView iv_image;
    @BindView(R.id.tv_description)
    TextView tv_description;
    @BindView(R.id.tv_likes)
    TextView tv_likes;
    @BindView(R.id.tv_comments)
    TextView tv_comments;
    @BindView(R.id.ll_like)
    LinearLayout ll_like;
    @BindView(R.id.ll_comment)
    LinearLayout ll_comment;
    @BindView(R.id.pb_loader)
    ProgressBar pb_loader;

    AttachmentPOJO attachmentPOJO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_attachments_view, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            attachmentPOJO = (AttachmentPOJO) getArguments().getSerializable("attachment");
            loadImage();
        }
    }

    public void loadImage() {

        pb_loader.setVisibility(View.VISIBLE);
        Glide.with(getActivity())
                .load(attachmentPOJO.getFile_path())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        pb_loader.setVisibility(View.GONE);
                        return false;
                    }
                }).into(iv_image);


        tv_description.setText(attachmentPOJO.getDescription());
    }


}
