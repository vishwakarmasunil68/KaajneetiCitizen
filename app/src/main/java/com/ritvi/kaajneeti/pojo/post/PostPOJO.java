package com.ritvi.kaajneeti.pojo.post;

import com.google.gson.annotations.SerializedName;
import com.ritvi.kaajneeti.pojo.allfeeds.FeelingDataPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sunil on 30-03-2018.
 */

public class PostPOJO implements Serializable {
    @SerializedName("PostId")
    private String postId;
    @SerializedName("UserProfileId")
    private String userProfileId;
    @SerializedName("PostTitle")
    private String postTitle;
    @SerializedName("PostStatus")
    private String postStatus;
    @SerializedName("PostLocation")
    private String postLocation;
    @SerializedName("PostDescription")
    private String postDescription;
    @SerializedName("PostURL")
    private String postURL;
    @SerializedName("AddedOn")
    private String addedOn;
    @SerializedName("AddedOnTime")
    private String addedOnTime;
    @SerializedName("UpdatedOn")
    private String updatedOn;
    @SerializedName("UpdatedOnTime")
    private String updatedOnTime;
    @SerializedName("FeelingData")
    private List<FeelingDataPOJO> feelingDataPOJOS;
    @SerializedName("PostProfile")
    private UserProfilePOJO postProfile;
    @SerializedName("PostTag")
    private List<UserProfilePOJO> postTag;
    @SerializedName("PostAttachment")
    private List<PostAttachmentPOJO> postAttachment;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(String userProfileId) {
        this.userProfileId = userProfileId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public String getPostLocation() {
        return postLocation;
    }

    public void setPostLocation(String postLocation) {
        this.postLocation = postLocation;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostURL() {
        return postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getAddedOnTime() {
        return addedOnTime;
    }

    public void setAddedOnTime(String addedOnTime) {
        this.addedOnTime = addedOnTime;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedOnTime() {
        return updatedOnTime;
    }

    public void setUpdatedOnTime(String updatedOnTime) {
        this.updatedOnTime = updatedOnTime;
    }

    public List<FeelingDataPOJO> getFeelingDataPOJOS() {
        return feelingDataPOJOS;
    }

    public void setFeelingDataPOJOS(List<FeelingDataPOJO> feelingDataPOJOS) {
        this.feelingDataPOJOS = feelingDataPOJOS;
    }

    public UserProfilePOJO getPostProfile() {
        return postProfile;
    }

    public void setPostProfile(UserProfilePOJO postProfile) {
        this.postProfile = postProfile;
    }

    public List<UserProfilePOJO> getPostTag() {
        return postTag;
    }

    public void setPostTag(List<UserProfilePOJO> postTag) {
        this.postTag = postTag;
    }

    public List<PostAttachmentPOJO> getPostAttachment() {
        return postAttachment;
    }

    public void setPostAttachment(List<PostAttachmentPOJO> postAttachment) {
        this.postAttachment = postAttachment;
    }
}
