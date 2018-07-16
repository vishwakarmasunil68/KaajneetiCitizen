package com.ritvi.kaajneeti.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.pojo.notification.NotificationPOJO;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sunil on 03-11-2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationPOJO> items;
    Activity activity;
    Fragment fragment;

    public NotificationAdapter(Activity activity, Fragment fragment, List<NotificationPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_notificaiton_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String notification_msg = items.get(position).getNotificationFromProfile().getFirstName() + " " + items.get(position).getNotificationFromProfile().getLastName() + " " + items.get(position).getNotificationDescription();
        holder.tv_notification.setText(notification_msg);

        Glide.with(activity.getApplicationContext())
                .load(items.get(position).getNotificationFromProfile().getProfilePhotoPath())
                .placeholder(R.drawable.ic_default_profile_pic)
                .error(R.drawable.ic_default_profile_pic)
                .dontAnimate()
                .into(holder.cv_profile_pic);

        holder.tv_time.setText(items.get(position).getNotificationAddedOn());

        if (items.get(position).getNotificationReceivedYesNo().equalsIgnoreCase("0")) {
            holder.ll_notification.setBackgroundResource(R.drawable.not_back_unread);
        } else {
            holder.ll_notification.setBackgroundResource(R.drawable.not_back_read);
        }

        holder.ll_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items.get(position).getNotificationReceivedYesNo().equalsIgnoreCase("0")) {
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                    nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                    nameValuePairs.add(new BasicNameValuePair("notification_id", items.get(position).getNotificationId()));
                    new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
                        @Override
                        public void onGetMsg(String apicall, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                                    holder.ll_notification.setBackgroundResource(R.drawable.not_back_read);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, "READ_NOTIFICATION", false).execute(WebServicesUrls.MARK_MY_NOTIFICATION_READ);
                }
                if (activity instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) activity;
                    homeActivity.passNotificationType(items.get(position).getNotificationType(), items.get(position).getNotificationFeedId());
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
        @BindView(R.id.tv_notification)
        public TextView tv_notification;
        @BindView(R.id.tv_time)
        public TextView tv_time;
        @BindView(R.id.cv_profile_pic)
        public CircleImageView cv_profile_pic;
        @BindView(R.id.ll_notification)
        public LinearLayout ll_notification;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
