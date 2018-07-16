package com.ritvi.kaajneeti.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.interfaces.PollAnsClickInterface;
import com.ritvi.kaajneeti.pojo.poll.PollAnsPOJO;
import com.ritvi.kaajneeti.pojo.poll.PollPOJO;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PollFeedAnsAdapter extends RecyclerView.Adapter<PollFeedAnsAdapter.ViewHolder> {
    private List<PollAnsPOJO> items;
    Activity activity;
    Fragment fragment;
    PollAnsClickInterface pollAnsClickInterface;
    PollPOJO pollPOJO;

    public PollFeedAnsAdapter(Activity activity, Fragment fragment, List<PollAnsPOJO> items, PollPOJO pollPOJO) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
        this.pollPOJO=pollPOJO;
    }

    public void setOnAnsClicked(PollAnsClickInterface pollAnsClickInterface){
        this.pollAnsClickInterface=pollAnsClickInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_poll_feed_ans_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        boolean is_media=false;
        for(PollAnsPOJO pollAnsPOJO:items){
            if(pollAnsPOJO.getPollAnswerImage().length()>0){
                is_media=true;
            }
        }

        if(is_media){
            holder.iv_poll_image.setVisibility(View.VISIBLE);
        }else{
            holder.iv_poll_image.setVisibility(View.GONE);
        }

        Glide.with(activity.getApplicationContext())
                .load(items.get(position).getPollAnswerImage())
                .placeholder(R.drawable.ic_default_pic)
                .error(R.drawable.ic_default_pic)
                .dontAnimate()
                .into(holder.iv_poll_image);

        if(items.get(position).getPollAnswer().length()>0){

        }else{
            holder.tv_poll_media_ans.setVisibility(View.GONE);
        }

        holder.ll_poll_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pollAnsClickInterface.onAnsclicked(items.get(position).getPollAnswerId());
                boolean me_participated=false;
//                for(PollAnsPOJO pollAnsPOJO:items){
//                    if(pollAnsPOJO.getMeAnsweredYesNo().equals("1")){
//                        me_participated=true;
//                    }
//                }
                String url="";
                if(pollPOJO.getMeParticipated()==1){
                    url=WebServicesUrls.POLL_REPARTICIPATE;
                }else{
                    url=WebServicesUrls.SAVE_POLL_ANS;
                }

                Log.d(TagUtils.getTag(), "poll ans clicked:-" + items.get(position).getPollAnswerId());
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                nameValuePairs.add(new BasicNameValuePair("poll_id", items.get(position).getPollId()));
                nameValuePairs.add(new BasicNameValuePair("answer_id", items.get(position).getPollAnswerId()));
                new WebServiceBase(nameValuePairs, activity, new WebServicesCallBack() {
                    @Override
                    public void onGetMsg(String apicall, String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                                holder.iv_check.setVisibility(View.VISIBLE);
                                if(pollPOJO.getMeParticipated()==1){
                                    PollPOJO pollPOJO=new Gson().fromJson(jsonObject.optJSONObject("result").toString(),PollPOJO.class);
                                    pollAnsClickInterface.onAnsclicked(pollPOJO);
                                }
                                ToastClass.showShortToast(activity.getApplicationContext(), "Thanks for you participation");
                            } else {
                                ToastClass.showShortToast(activity.getApplicationContext(), jsonObject.optString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, "POLL_ANSWERED", true).execute(url);
            }
        });

        if(items.get(position).getMeAnsweredYesNo()==1){
            holder.iv_check.setVisibility(View.VISIBLE);
        }else{
            holder.iv_check.setVisibility(View.GONE);
        }

        holder.tv_poll_media_ans.setText(items.get(position).getPollAnswer());

        holder.itemView.setTag(items.get(position));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_poll_image;
        public TextView tv_poll_media_ans;
        public FrameLayout ll_poll_ans;
        public ImageView iv_check;
        public ViewHolder(View itemView) {
            super(itemView);
            ll_poll_ans=itemView.findViewById(R.id.ll_poll_ans);
            tv_poll_media_ans=itemView.findViewById(R.id.tv_poll_media_ans);
            iv_poll_image=itemView.findViewById(R.id.iv_poll_image);
            iv_check=itemView.findViewById(R.id.iv_check);
        }
    }


}
