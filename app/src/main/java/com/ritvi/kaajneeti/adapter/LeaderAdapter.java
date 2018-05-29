package com.ritvi.kaajneeti.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.activity.user.AllLeaderActivity;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.AdapterWebService;
import com.ritvi.kaajneeti.webservice.MsgPassInterface;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sunil on 03-11-2017.
 */

public class LeaderAdapter extends RecyclerView.Adapter<LeaderAdapter.ViewHolder> implements WebServicesCallBack {
    private List<UserProfilePOJO> items;
    Activity activity;
    Fragment fragment;
    int device_height = 0;

    public LeaderAdapter(Activity activity, Fragment fragment, List<UserProfilePOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_leaders, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


//
        holder.tv_name.setText(items.get(position).getFirstName()+" "+items.get(position).getLastName());
        holder.tv_email.setText(items.get(position).getEmail());
        holder.iv_tag_check.setVisibility(View.VISIBLE);
        Log.d(TagUtils.getTag(),"fav leader:-"+items.get(position).getMyFavouriteLeader());
        if (activity instanceof AllLeaderActivity) {
            if (items.get(position).getMyFavouriteLeader().equals("1")) {
                holder.iv_tag_check.setVisibility(View.VISIBLE);
            } else {
                holder.iv_tag_check.setVisibility(View.GONE);
            }

        } else {
            holder.iv_tag_check.setVisibility(View.GONE);
        }


        holder.ll_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFavoriteAPI(items.get(position), holder.iv_tag_check);
//                if(fragment!=null&&fragment instanceof ContributeFragment) {
//                    ContributeFragment contributeFragment= (ContributeFragment) fragment;
////                    contributeFragment.showpaymentDialog(items.get(position),items.get(position).getUserProfileLeader().getUserProfileId());
//                }else{
//                    showLeaderProfile(items.get(position));
//                }
            }
        });

        holder.itemView.setTag(items.get(position));
    }

    public void callFavoriteAPI(UserProfilePOJO leaderPOJO, final ImageView favorite_image) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Constants.userProfilePOJO.getUserId()));
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        nameValuePairs.add(new BasicNameValuePair("friend_user_profile_id", leaderPOJO.getUserProfileId()));

        new AdapterWebService(activity, nameValuePairs, false, new MsgPassInterface() {
            @Override
            public void onMsgPassed(String response) {
                Log.d(TagUtils.getTag(), "api called:-" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("status").equals("success")) {
                        ToastClass.showShortToast(activity.getApplicationContext(), jsonObject.optString("message"));
                        if (jsonObject.optString("favourite").equals("0")) {
                            favorite_image.setVisibility(View.GONE);
                        } else {
                            favorite_image.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).executeApi(WebServicesUrls.SET_MY_FAVORITE_LEADER);
    }

    private final String TAG = getClass().getSimpleName();

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onGetMsg(String apicall, String response) {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView cv_profile_pic;
        public ImageView iv_tag_check;
        public TextView tv_email;
        public TextView tv_name;
        public LinearLayout ll_user;

        public ViewHolder(View itemView) {
            super(itemView);
            cv_profile_pic = (CircleImageView) itemView.findViewById(R.id.cv_profile_pic);
            iv_tag_check = (ImageView) itemView.findViewById(R.id.iv_tag_check);
            tv_email = (TextView) itemView.findViewById(R.id.tv_email);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            ll_user = (LinearLayout) itemView.findViewById(R.id.ll_user);
        }
    }
}
