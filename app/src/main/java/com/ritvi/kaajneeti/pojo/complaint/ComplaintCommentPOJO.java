package com.ritvi.kaajneeti.pojo.complaint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;

public class ComplaintCommentPOJO {

    @SerializedName("ComplaintCommentId")
    @Expose
    private String complaintCommentId;
    @SerializedName("ComplaintId")
    @Expose
    private String complaintId;
    @SerializedName("AddedBy")
    @Expose
    private String addedBy;
    @SerializedName("CommentText")
    @Expose
    private String commentText;
    @SerializedName("CommentPhoto")
    @Expose
    private String commentPhoto;
    @SerializedName("ParentId")
    @Expose
    private String parentId;
    @SerializedName("CommentStatus")
    @Expose
    private String commentStatus;
    @SerializedName("CommentOn")
    @Expose
    private String commentOn;
    @SerializedName("CommentOnTime")
    @Expose
    private String commentOnTime;
    @SerializedName("CommentProfile")
    @Expose
    private UserProfilePOJO commentProfile;

    public String getComplaintCommentId() {
        return complaintCommentId;
    }

    public void setComplaintCommentId(String complaintCommentId) {
        this.complaintCommentId = complaintCommentId;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentPhoto() {
        return commentPhoto;
    }

    public void setCommentPhoto(String commentPhoto) {
        this.commentPhoto = commentPhoto;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(String commentStatus) {
        this.commentStatus = commentStatus;
    }

    public String getCommentOn() {
        return commentOn;
    }

    public void setCommentOn(String commentOn) {
        this.commentOn = commentOn;
    }

    public String getCommentOnTime() {
        return commentOnTime;
    }

    public void setCommentOnTime(String commentOnTime) {
        this.commentOnTime = commentOnTime;
    }

    public UserProfilePOJO getCommentProfile() {
        return commentProfile;
    }

    public void setCommentProfile(UserProfilePOJO commentProfile) {
        this.commentProfile = commentProfile;
    }
}
