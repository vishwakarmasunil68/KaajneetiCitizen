package com.ritvi.kaajneeti.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.fragment.Express.CreatePollFragment;
import com.ritvi.kaajneeti.fragment.search.SearchAllFragment;
import com.ritvi.kaajneeti.pojo.poll.PollMediaAnsPOJO;

import java.io.File;
import java.util.List;

/**
 * Created by sunil on 03-11-2017.
 */

public class PollAnsAdapter extends RecyclerView.Adapter<PollAnsAdapter.ViewHolder>{
    private List<PollMediaAnsPOJO> items;
    Activity activity;
    Fragment fragment;

    public PollAnsAdapter(Activity activity, Fragment fragment, List<PollMediaAnsPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_poll_media_ans, parent, false);
        return new ViewHolder(v);
    }

    public boolean checkImage(){
        boolean image_present=false;
        for(PollMediaAnsPOJO pollMediaAnsPOJO:items){
            if(pollMediaAnsPOJO.getImage().length()>0){
                image_present=true;
            }
        }
        return image_present;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(checkImage()){
            holder.iv_ans.setVisibility(View.VISIBLE);
        }else{
            holder.iv_ans.setVisibility(View.GONE);
        }



        if(items.get(position).getImage()!=null&&(new File(items.get(position).getImage()).exists())){
            holder.iv_ans.setVisibility(View.VISIBLE);
            Glide.with(activity.getApplicationContext())
                    .load(items.get(position).getImage())
                    .into(holder.iv_ans);
        }else{
            Glide.with(activity.getApplicationContext())
                    .load(R.drawable.ic_default_pic)
                    .into(holder.iv_ans);
//            holder.iv_ans.setVisibility(View.GONE);
        }

        holder.et_ans.setText(items.get(position).getAns());

        holder.iv_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu menu = new PopupMenu(activity, v);

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuitem) {
                        switch (menuitem.getItemId()) {
                            case R.id.popup_change:
                                holder.iv_select_image.callOnClick();
                                break;
                            case R.id.popup_remove:
                                Glide.with(activity.getApplicationContext())
                                        .load(R.drawable.ic_default_pic)
                                        .into(holder.iv_ans);
                                items.get(position).setImage("");
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

        holder.iv_select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment instanceof CreatePollFragment){
                    CreatePollFragment createPollFragment= (CreatePollFragment) fragment;
                    createPollFragment.selectImage(items.get(position),holder.iv_ans);
                }
            }
        });

        holder.et_ans.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                items.get(position).setAns(holder.et_ans.getText().toString());
            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(items.size()!=2) {
                    items.remove(position);
                    notifyDataSetChanged();
                }
            }
        });

        holder.itemView.setTag(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText et_ans;
        public ImageView iv_select_image;
        public ImageView iv_delete;
        public ImageView iv_ans;

        public ViewHolder(View itemView) {
            super(itemView);
            et_ans = itemView.findViewById(R.id.et_ans);
            iv_select_image = itemView.findViewById(R.id.iv_select_image);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            iv_ans = itemView.findViewById(R.id.iv_ans);
        }
    }
}
