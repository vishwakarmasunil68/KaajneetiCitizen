package com.ritvi.kaajneeti.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.fragment.user.UpdateEducationFragment;
import com.ritvi.kaajneeti.fragment.user.UpdateWorkFragment;
import com.ritvi.kaajneeti.pojo.user.EducationPOJO;
import com.ritvi.kaajneeti.pojo.user.WorkPOJO;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunil on 03-11-2017.
 */

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.ViewHolder>{
    private List<EducationPOJO> items;
    Activity activity;
    Fragment fragment;

    public EducationAdapter(Activity activity, Fragment fragment, List<EducationPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_work_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_title.setText(items.get(position).getQualificationUniversity());

        String date=items.get(position).getQualificationFrom();
        if(items.get(position).getPersuing().equals("1")){
            date+=" - Present";
        }else{
            date+=" - "+items.get(position).getQualificationTo();
        }

        holder.tv_date.setText(date);
        holder.tv_position.setText(items.get(position).getQualification());

        holder.ll_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setCancelable(true);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete

                                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                                nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
                                nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                                nameValuePairs.add(new BasicNameValuePair("qualification_id", items.get(position).getUserProfileEducationId()));
                                new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
                                    @Override
                                    public void onGetMsg(String apicall, String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.optString("status").equals("success")) {
                                                items.remove(position);
                                                notifyDataSetChanged();
                                            } else {
                                                ToastClass.showShortToast(activity.getApplicationContext(), jsonObject.optString("message"));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, "CALL_DELETE", true).execute(WebServicesUrls.DELETE_PROFILE_EDUCATION);

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        holder.ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) activity;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("educationPOJO", items.get(position));
                    UpdateEducationFragment updateEducationFragment= new UpdateEducationFragment();
                    updateEducationFragment.setArguments(bundle);
                    homeActivity.replaceFragmentinFrameHome(updateEducationFragment, "updateEducationFragment");
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

        TextView tv_title;
        TextView tv_position;
        TextView tv_date;
        LinearLayout ll_edit;
        LinearLayout ll_delete;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_position=itemView.findViewById(R.id.tv_position);
            tv_date=itemView.findViewById(R.id.tv_date);
            tv_title=itemView.findViewById(R.id.tv_title);
            ll_edit=itemView.findViewById(R.id.ll_edit);
            ll_delete=itemView.findViewById(R.id.ll_delete);
        }
    }
}
