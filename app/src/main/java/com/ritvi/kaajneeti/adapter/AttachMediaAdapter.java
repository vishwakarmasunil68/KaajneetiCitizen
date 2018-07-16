package com.ritvi.kaajneeti.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.activity.express.ExpressActivity;
import com.ritvi.kaajneeti.fragment.Express.CreateComplaintFragment;
import com.ritvi.kaajneeti.interfaces.ItemSizeChangeListener;
import com.ritvi.kaajneeti.pojo.allfeeds.MediaPOJO;
import java.util.List;

/**
 * Created by sunil on 03-11-2017.
 */

public class AttachMediaAdapter extends RecyclerView.Adapter<AttachMediaAdapter.ViewHolder>{
    private List<MediaPOJO> items;
    Activity activity;
    Fragment fragment;
    ItemSizeChangeListener itemSizeChangeListener;

    public AttachMediaAdapter(Activity activity, Fragment fragment, List<MediaPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    public void setOnItemChanged(ItemSizeChangeListener itemSizeChangeListener){
        this.itemSizeChangeListener=itemSizeChangeListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_attach_media, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Glide.with(activity.getApplicationContext())
                .load(items.get(position).getPath())
                .into(holder.iv_image);

        holder.iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment==null&&activity instanceof ExpressActivity) {
                    ExpressActivity expressActivity= (ExpressActivity) activity;
                    expressActivity.removePosition(items.get(position),position);
                }else if(fragment instanceof CreateComplaintFragment){
                    CreateComplaintFragment createComplaintFragment= (CreateComplaintFragment) fragment;
                    createComplaintFragment.removePosition(items.get(position),position);
                }else{
                    items.remove(position);
                    notifyDataSetChanged();
                }
            }
        });

        holder.itemView.setTag(items.get(position));
    }



    @Override
    public int getItemCount() {
//        Log.d(TagUtils.getTag(),"item count:-"+items.size());
        if(itemSizeChangeListener!=null) {
            itemSizeChangeListener.onItemSizeChanged();
        }
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_image;
        ImageView iv_close;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_image=itemView.findViewById(R.id.iv_image);
            iv_close=itemView.findViewById(R.id.iv_close);
        }
    }
}
