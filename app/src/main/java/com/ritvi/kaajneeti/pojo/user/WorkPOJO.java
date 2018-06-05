package com.ritvi.kaajneeti.pojo.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sunil on 12-04-2018.
 */

public class WorkPOJO implements Serializable{
    @SerializedName("UserProfileWorkId")
    @Expose
    private String userProfileWorkId;
    @SerializedName("UserProfileId")
    @Expose
    private String userProfileId;
    @SerializedName("WorkCompany")
    @Expose
    private String workCompany;
    @SerializedName("WorkPosition")
    @Expose
    private String workPosition;
    @SerializedName("WorkLocation")
    @Expose
    private String workLocation;
    @SerializedName("WorkFrom")
    @Expose
    private String workFrom;
    @SerializedName("WorkTo")
    @Expose
    private String workTo;
    @SerializedName("CurrentlyWorking")
    @Expose
    private String currentlyWorking;
    @SerializedName("PrivatePublic")
    @Expose
    private String privatePublic;
    @SerializedName("AddedOn")
    @Expose
    private String addedOn;
    @SerializedName("UpdatedOn")
    @Expose
    private String updatedOn;
    @SerializedName("AddedOnTime")
    @Expose
    private String addedOnTime;
    @SerializedName("UpdatedOnTime")
    @Expose
    private String updatedOnTime;

    public String getUserProfileWorkId() {
        return userProfileWorkId;
    }

    public void setUserProfileWorkId(String userProfileWorkId) {
        this.userProfileWorkId = userProfileWorkId;
    }

    public String getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(String userProfileId) {
        this.userProfileId = userProfileId;
    }

    public String getWorkCompany() {
        return workCompany;
    }

    public void setWorkCompany(String workCompany) {
        this.workCompany = workCompany;
    }

    public String getWorkPosition() {
        return workPosition;
    }

    public void setWorkPosition(String workPosition) {
        this.workPosition = workPosition;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public String getWorkFrom() {
        return workFrom;
    }

    public void setWorkFrom(String workFrom) {
        this.workFrom = workFrom;
    }

    public String getWorkTo() {
        return workTo;
    }

    public void setWorkTo(String workTo) {
        this.workTo = workTo;
    }

    public String getCurrentlyWorking() {
        return currentlyWorking;
    }

    public void setCurrentlyWorking(String currentlyWorking) {
        this.currentlyWorking = currentlyWorking;
    }

    public String getPrivatePublic() {
        return privatePublic;
    }

    public void setPrivatePublic(String privatePublic) {
        this.privatePublic = privatePublic;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getAddedOnTime() {
        return addedOnTime;
    }

    public void setAddedOnTime(String addedOnTime) {
        this.addedOnTime = addedOnTime;
    }

    public String getUpdatedOnTime() {
        return updatedOnTime;
    }

    public void setUpdatedOnTime(String updatedOnTime) {
        this.updatedOnTime = updatedOnTime;
    }
}
