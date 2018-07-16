package com.ritvi.kaajneeti.pojo.allfeeds;

import android.view.textservice.SuggestionsInfo;

import com.google.gson.annotations.SerializedName;
import com.ritvi.kaajneeti.pojo.complaint.ComplaintPOJO;
import com.ritvi.kaajneeti.pojo.event.EventPOJO;
import com.ritvi.kaajneeti.pojo.information.InformationPOJO;
import com.ritvi.kaajneeti.pojo.payment.PaymentDataPOJO;
import com.ritvi.kaajneeti.pojo.poll.PollPOJO;
import com.ritvi.kaajneeti.pojo.post.PostPOJO;
import com.ritvi.kaajneeti.pojo.suggestion.SuggestionPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;

import java.io.Serializable;

/**
 * Created by sunil on 30-03-2018.
 */

public class FeedPOJO implements Serializable{
    @SerializedName("feedtype")
    String feedtype;
    @SerializedName("polldata")
    PollPOJO pollPOJO;
    @SerializedName("eventdata")
    EventPOJO eventPOJO;
    @SerializedName("postdata")
    PostPOJO postPOJO;
    @SerializedName("complaintdata")
    ComplaintPOJO complaintPOJO;
    @SerializedName("profiledata")
    UserProfilePOJO profiledata;
    @SerializedName("suggestiondata")
    SuggestionPOJO suggestiondata;
    @SerializedName("informationdata")
    InformationPOJO informationPOJO;
    @SerializedName("paymentdata")
    PaymentDataPOJO paymentDataPOJO;
    @SerializedName("followerdata")
    UserProfilePOJO followerdataUserProfilePOJO;
    @SerializedName("followingdata")
    UserProfilePOJO followingdataUserProfilePOJO;

    public String getFeedtype() {
        return feedtype;
    }

    public void setFeedtype(String feedtype) {
        this.feedtype = feedtype;
    }

    public PollPOJO getPollPOJO() {
        return pollPOJO;
    }

    public void setPollPOJO(PollPOJO pollPOJO) {
        this.pollPOJO = pollPOJO;
    }

    public EventPOJO getEventPOJO() {
        return eventPOJO;
    }

    public void setEventPOJO(EventPOJO eventPOJO) {
        this.eventPOJO = eventPOJO;
    }

    public PostPOJO getPostPOJO() {
        return postPOJO;
    }

    public void setPostPOJO(PostPOJO postPOJO) {
        this.postPOJO = postPOJO;
    }

    public ComplaintPOJO getComplaintPOJO() {
        return complaintPOJO;
    }

    public void setComplaintPOJO(ComplaintPOJO complaintPOJO) {
        this.complaintPOJO = complaintPOJO;
    }

    public UserProfilePOJO getProfiledata() {
        return profiledata;
    }

    public void setProfiledata(UserProfilePOJO profiledata) {
        this.profiledata = profiledata;
    }

    public SuggestionPOJO getSuggestiondata() {
        return suggestiondata;
    }

    public void setSuggestiondata(SuggestionPOJO suggestiondata) {
        this.suggestiondata = suggestiondata;
    }

    public InformationPOJO getInformationPOJO() {
        return informationPOJO;
    }

    public void setInformationPOJO(InformationPOJO informationPOJO) {
        this.informationPOJO = informationPOJO;
    }

    public PaymentDataPOJO getPaymentDataPOJO() {
        return paymentDataPOJO;
    }

    public void setPaymentDataPOJO(PaymentDataPOJO paymentDataPOJO) {
        this.paymentDataPOJO = paymentDataPOJO;
    }

    public UserProfilePOJO getFollowerdataUserProfilePOJO() {
        return followerdataUserProfilePOJO;
    }

    public void setFollowerdataUserProfilePOJO(UserProfilePOJO followerdataUserProfilePOJO) {
        this.followerdataUserProfilePOJO = followerdataUserProfilePOJO;
    }

    public UserProfilePOJO getFollowingdataUserProfilePOJO() {
        return followingdataUserProfilePOJO;
    }

    public void setFollowingdataUserProfilePOJO(UserProfilePOJO followingdataUserProfilePOJO) {
        this.followingdataUserProfilePOJO = followingdataUserProfilePOJO;
    }
}
