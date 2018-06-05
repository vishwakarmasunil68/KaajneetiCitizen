package com.ritvi.kaajneeti.pojo.information;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;

import java.util.List;

public class InformationPOJO {
    @SerializedName("InformationId")
    @Expose
    private String informationId;
    @SerializedName("InformationUniqueId")
    @Expose
    private String informationUniqueId;
    @SerializedName("InformationPrivacy")
    @Expose
    private String informationPrivacy;
    @SerializedName("ApplicantName")
    @Expose
    private String applicantName;
    @SerializedName("ApplicantFatherName")
    @Expose
    private String applicantFatherName;
    @SerializedName("ApplicantGender")
    @Expose
    private Object applicantGender;
    @SerializedName("ApplicantMobile")
    @Expose
    private String applicantMobile;
    @SerializedName("ApplicantEmail")
    @Expose
    private Object applicantEmail;
    @SerializedName("ApplicantAadhaarNumber")
    @Expose
    private Object applicantAadhaarNumber;
    @SerializedName("InformationSubject")
    @Expose
    private String informationSubject;
    @SerializedName("InformationDescription")
    @Expose
    private String informationDescription;
    @SerializedName("InformationStatus")
    @Expose
    private String informationStatus;
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
    @SerializedName("InformationProfile")
    @Expose
    private UserProfilePOJO informationProfile;
    @SerializedName("InformationAttachment")
    @Expose
    private List<InformationAttachmentPOJO> informationAttachment = null;

    public String getInformationId() {
        return informationId;
    }

    public void setInformationId(String informationId) {
        this.informationId = informationId;
    }

    public String getInformationUniqueId() {
        return informationUniqueId;
    }

    public void setInformationUniqueId(String informationUniqueId) {
        this.informationUniqueId = informationUniqueId;
    }

    public String getInformationPrivacy() {
        return informationPrivacy;
    }

    public void setInformationPrivacy(String informationPrivacy) {
        this.informationPrivacy = informationPrivacy;
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

    public Object getApplicantGender() {
        return applicantGender;
    }

    public void setApplicantGender(Object applicantGender) {
        this.applicantGender = applicantGender;
    }

    public String getApplicantMobile() {
        return applicantMobile;
    }

    public void setApplicantMobile(String applicantMobile) {
        this.applicantMobile = applicantMobile;
    }

    public Object getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(Object applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public Object getApplicantAadhaarNumber() {
        return applicantAadhaarNumber;
    }

    public void setApplicantAadhaarNumber(Object applicantAadhaarNumber) {
        this.applicantAadhaarNumber = applicantAadhaarNumber;
    }

    public String getInformationSubject() {
        return informationSubject;
    }

    public void setInformationSubject(String informationSubject) {
        this.informationSubject = informationSubject;
    }

    public String getInformationDescription() {
        return informationDescription;
    }

    public void setInformationDescription(String informationDescription) {
        this.informationDescription = informationDescription;
    }

    public String getInformationStatus() {
        return informationStatus;
    }

    public void setInformationStatus(String informationStatus) {
        this.informationStatus = informationStatus;
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

    public UserProfilePOJO getInformationProfile() {
        return informationProfile;
    }

    public void setInformationProfile(UserProfilePOJO informationProfile) {
        this.informationProfile = informationProfile;
    }

    public List<InformationAttachmentPOJO> getInformationAttachment() {
        return informationAttachment;
    }

    public void setInformationAttachment(List<InformationAttachmentPOJO> informationAttachment) {
        this.informationAttachment = informationAttachment;
    }
}
