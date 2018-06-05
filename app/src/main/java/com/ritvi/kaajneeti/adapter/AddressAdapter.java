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
import com.ritvi.kaajneeti.fragment.user.UpdateAddressFragment;
import com.ritvi.kaajneeti.pojo.user.AddressPOJO;
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

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder>{
    private List<AddressPOJO> items;
    Activity activity;
    Fragment fragment;

    public AddressAdapter(Activity activity, Fragment fragment, List<AddressPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_address_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_title.setText(items.get(position).getAddress()+" , "+items.get(position).getCity()+" , "+items.get(position).getState());

        holder.ll_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setCancelable(true);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete

                                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                                nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
                                nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                                nameValuePairs.add(new BasicNameValuePair("address_id", items.get(position).getUserProfileAddressId()));
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
                                }, "CALL_DELETE", true).execute(WebServicesUrls.DELETE_PROFILE_ADDRESS);

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
                    bundle.putSerializable("addressPOJO", items.get(position));
                    UpdateAddressFragment updateAddressFragment = new UpdateAddressFragment();
                    updateAddressFragment.setArguments(bundle);
                    homeActivity.replaceFragmentinFrameHome(updateAddressFragment, "UpdateAddressFragment");
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

        TextView tv_description;
        TextView tv_title;
        LinearLayout ll_edit;
        LinearLayout ll_delete;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_description=itemView.findViewById(R.id.tv_description);
            tv_title=itemView.findViewById(R.id.tv_title);
            ll_edit=itemView.findViewById(R.id.ll_edit);
            ll_delete=itemView.findViewById(R.id.ll_delete);
        }
    }
}
