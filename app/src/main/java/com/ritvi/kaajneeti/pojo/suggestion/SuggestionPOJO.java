package com.ritvi.kaajneeti.pojo.suggestion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;

import java.io.Serializable;
import java.util.List;

public class SuggestionPOJO implements Serializable{
    @SerializedName("SuggestionId")
    @Expose
    private String suggestionId;
    @SerializedName("SuggestionUniqueId")
    @Expose
    private String suggestionUniqueId;
    @SerializedName("ApplicantName")
    @Expose
    private String applicantName;
    @SerializedName("ApplicantFatherName")
    @Expose
    private String applicantFatherName;
    @SerializedName("ApplicantGender")
    @Expose
    private String applicantGender;
    @SerializedName("ApplicantMobile")
    @Expose
    private String applicantMobile;
    @SerializedName("ApplicantEmail")
    @Expose
    private String applicantEmail;
    @SerializedName("ApplicantAadhaarNumber")
    @Expose
    private String applicantAadhaarNumber;
    @SerializedName("ApplicantAddress")
    @Expose
    private String applicantAddress;
    @SerializedName("ApplicantPlace")
    @Expose
    private String applicantPlace;
    @SerializedName("SuggestionLatitude")
    @Expose
    private String suggestionLatitude;
    @SerializedName("SuggestionLongitude")
    @Expose
    private String suggestionLongitude;
    @SerializedName("SuggestionSubject")
    @Expose
    private String suggestionSubject;
    @SerializedName("SuggestionDescription")
    @Expose
    private String suggestionDescription;
    @SerializedName("SuggestionStatus")
    @Expose
    private String suggestionStatus;
    @SerializedName("AddedOn")
    @Expose
    private String addedOn;
    @SerializedName("AddedOnTime")
    @Expose
    private String addedOnTime;
    @SerializedName("UpdatedOn")
    @Expose
    private String updatedOn;
    @SerializedName("UpdatedOnTime")
    @Expose
    private String updatedOnTime;
    @SerializedName("SuggestionProfile")
    @Expose
    private UserProfilePOJO suggestionProfile;
    @SerializedName("SuggestionAssigned")
    @Expose
    private List<UserProfilePOJO> suggestionAssigned = null;
    @SerializedName("SuggestionAttachment")
    @Expose
    private List<SuggestionAttachmentPOJO> suggestionAttachment = null;
    @SerializedName("CountSuggestionHistory")
    @Expose
    private Integer countSuggestionHistory;

    public String getSuggestionId() {
        return suggestionId;
    }

    public void setSuggestionId(String suggestionId) {
        this.suggestionId = suggestionId;
    }

    public String getSuggestionUniqueId() {
        return suggestionUniqueId;
    }

    public void setSuggestionUniqueId(String suggestionUniqueId) {
        this.suggestionUniqueId = suggestionUniqueId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantFatherName() {
        return applicantFatherName;
    }

    public void setApplicantFatherName(String applicantFatherName) {
        this.applicantFatherName = applicantFatherName;
    }

    public String getApplicantGender() {
        return applicantGender;
    }

    public void setApplicantGender(String applicantGender) {
        this.applicantGender = applicantGender;
    }

    public String getApplicantMobile() {
        return applicantMobile;
    }

    public void setApplicantMobile(String applicantMobile) {
        this.applicantMobile = applicantMobile;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public String getApplicantAadhaarNumber() {
        return applicantAadhaarNumber;
    }

    public void setApplicantAadhaarNumber(String applicantAadhaarNumber) {
        this.applicantAadhaarNumber = applicantAadhaarNumber;
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(String applicantAddress) {
        this.applicantAddress = applicantAddress;
    }

    public String getApplicantPlace() {
        return applicantPlace;
    }

    public void setApplicantPlace(String applicantPlace) {
        this.applicantPlace = applicantPlace;
    }

    public String getSuggestionLatitude() {
        return suggestionLatitude;
    }

    public void setSuggestionLatitude(String suggestionLatitude) {
        this.suggestionLatitude = suggestionLatitude;
    }

    public String getSuggestionLongitude() {
        return suggestionLongitude;
    }

    public void setSuggestionLongitude(String suggestionLongitude) {
        this.suggestionLongitude = suggestionLongitude;
    }

    public String getSuggestionSubject() {
        return suggestionSubject;
    }

    public void setSuggestionSubject(String suggestionSubject) {
        this.suggestionSubject = suggestionSubject;
    }

    public String getSuggestionDescription() {
        return suggestionDescription;
    }

    public void setSuggestionDescription(String suggestionDescription) {
        this.suggestionDescription = suggestionDescription;
    }

    public String getSuggestionStatus() {
        return suggestionStatus;
    }

    public void setSuggestionStatus(String suggestionStatus) {
        this.suggestionStatus = suggestionStatus;
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

    public UserProfilePOJO getSuggestionProfile() {
        return suggestionProfile;
    }

    public void setSuggestionProfile(UserProfilePOJO suggestionProfile) {
        this.suggestionProfile = suggestionProfile;
    }

    public List<UserProfilePOJO> getSuggestionAssigned() {
        return suggestionAssigned;
    }

    public void setSuggestionAssigned(List<UserProfilePOJO> suggestionAssigned) {
        this.suggestionAssigned = suggestionAssigned;
    }

    public List<SuggestionAttachmentPOJO> getSuggestionAttachment() {
        return suggestionAttachment;
    }

    public void setSuggestionAttachment(List<SuggestionAttachmentPOJO> suggestionAttachment) {
        this.suggestionAttachment = suggestionAttachment;
    }

    public Integer getCountSuggestionHistory() {
        return countSuggestionHistory;
    }

    public void setCountSuggestionHistory(Integer countSuggestionHistory) {
        this.countSuggestionHistory = countSuggestionHistory;
    }
}
