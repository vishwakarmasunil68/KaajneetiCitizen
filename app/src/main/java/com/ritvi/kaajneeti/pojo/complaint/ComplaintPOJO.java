package com.ritvi.kaajneeti.pojo.complaint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ritvi.kaajneeti.pojo.location.ServerLocationPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sunil on 31-03-2018.
 */

public class ComplaintPOJO implements Serializable {

    @SerializedName("ComplaintId")
    private String complaintId;
    @SerializedName("ComplaintUniqueId")
    private String complaintUniqueId;
    @SerializedName("ComplaintTypeId")
    private String complaintTypeId;
    @SerializedName("ApplicantName")
    private String applicantName;
    @SerializedName("ApplicantFatherName")
    private String applicantFatherName;
    @SerializedName("ApplicantMobile")
    private String ApplicantMobile;
    @SerializedName("ComplaintDepartment")
    private String complaintDepartment;
    @SerializedName("DepartmentName")
    private String departmentName;
    @SerializedName("ComplaintPrivacy")
    private String complaintPrivacy;
    @SerializedName("ComplaintSubject")
    private String complaintSubject;
    @SerializedName("ComplaintDescription")
    private String complaintDescription;
    @SerializedName("ComplaintStatus")
    private String complaintStatus;
    @SerializedName("ComplaintStatusName")
    private String complaintStatusName;
    @SerializedName("ComplaintPlace")
    private String complaintPlace;
    @SerializedName("ComplaintAddress")
    private String complaintAddress;
    @SerializedName("ComplaintLatitude")
    private String complaintLatitude;
    @SerializedName("ComplaintLongitude")
    private String complaintLongitude;
    @SerializedName("AddedOn")
    private String addedOn;
    @SerializedName("AddedOnTime")
    private String addedOnTime;
    @SerializedName("UpdatedOn")
    private String updatedOn;
    @SerializedName("UpdatedOnTime")
    private String updatedOnTime;
    @SerializedName("ScheduleOn")
    private String scheduleOn;
    @SerializedName("ComplaintProfile")
    private UserProfilePOJO complaintProfile;
    @SerializedName("ComplaintMember")
    private List<UserProfilePOJO> complaintMemberPOJOS;
    @SerializedName("ComplaintAttachment")
    private List<ComplaintAttachmentPOJO> complaintAttachments;
    @SerializedName("ComplaintAssigned")
    private List<UserProfilePOJO> ComplaintAssigned;
    @SerializedName("TotalLikes")
    @Expose
    private Integer totalLikes;
    @SerializedName("TotalUnLikes")
    @Expose
    private Integer totalUnLikes;
    @SerializedName("MeLike")
    @Expose
    private Integer meLike;
    @SerializedName("MeUnLike")
    @Expose
    private Integer meUnLike;
    @SerializedName("TotalComment")
    @Expose
    private Integer totalComment;
    @SerializedName("LocationDetail")
    private ServerLocationPOJO serverLocationPOJO;

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getComplaintUniqueId() {
        return complaintUniqueId;
    }

    public void setComplaintUniqueId(String complaintUniqueId) {
        this.complaintUniqueId = complaintUniqueId;
    }

    public String getComplaintTypeId() {
        return complaintTypeId;
    }

    public void setComplaintTypeId(String complaintTypeId) {
        this.complaintTypeId = complaintTypeId;
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

    public String getComplaintDepartment() {
        return complaintDepartment;
    }

    public void setComplaintDepartment(String complaintDepartment) {
        this.complaintDepartment = complaintDepartment;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getComplaintPrivacy() {
        return complaintPrivacy;
    }

    public void setComplaintPrivacy(String complaintPrivacy) {
        this.complaintPrivacy = complaintPrivacy;
    }

    public String getComplaintSubject() {
        return complaintSubject;
    }

    public void setComplaintSubject(String complaintSubject) {
        this.complaintSubject = complaintSubject;
    }

    public String getComplaintDescription() {
        return complaintDescription;
    }

    public void setComplaintDescription(String complaintDescription) {
        this.complaintDescription = complaintDescription;
    }

    public String getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public String getComplaintStatusName() {
        return complaintStatusName;
    }

    public void setComplaintStatusName(String complaintStatusName) {
        this.complaintStatusName = complaintStatusName;
    }

    public String getComplaintPlace() {
        return complaintPlace;
    }

    public void setComplaintPlace(String complaintPlace) {
        this.complaintPlace = complaintPlace;
    }

    public String getComplaintAddress() {
        return complaintAddress;
    }

    public void setComplaintAddress(String complaintAddress) {
        this.complaintAddress = complaintAddress;
    }

    public String getComplaintLatitude() {
        return complaintLatitude;
    }

    public void setComplaintLatitude(String complaintLatitude) {
        this.complaintLatitude = complaintLatitude;
    }

    public String getComplaintLongitude() {
        return complaintLongitude;
    }

    public void setComplaintLongitude(String complaintLongitude) {
        this.complaintLongitude = complaintLongitude;
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

    public UserProfilePOJO getComplaintProfile() {
        return complaintProfile;
    }

    public void setComplaintProfile(UserProfilePOJO complaintProfile) {
        this.complaintProfile = complaintProfile;
    }

    public List<UserProfilePOJO> getComplaintMemberPOJOS() {
        return complaintMemberPOJOS;
    }

    public void setComplaintMemberPOJOS(List<UserProfilePOJO> complaintMemberPOJOS) {
        this.complaintMemberPOJOS = complaintMemberPOJOS;
    }

    public List<ComplaintAttachmentPOJO> getComplaintAttachments() {
        return complaintAttachments;
    }

    public void setComplaintAttachments(List<ComplaintAttachmentPOJO> complaintAttachments) {
        this.complaintAttachments = complaintAttachments;
    }

    public List<UserProfilePOJO> getComplaintAssigned() {
        return ComplaintAssigned;
    }

    public void setComplaintAssigned(List<UserProfilePOJO> complaintAssigned) {
        ComplaintAssigned = complaintAssigned;
    }

    public String getApplicantMobile() {
        return ApplicantMobile;
    }

    public void setApplicantMobile(String applicantMobile) {
        ApplicantMobile = applicantMobile;
    }

    public Integer getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
    }

    public Integer getTotalUnLikes() {
        return totalUnLikes;
    }

    public void setTotalUnLikes(Integer totalUnLikes) {
        this.totalUnLikes = totalUnLikes;
    }

    public Integer getMeLike() {
        return meLike;
    }

    public void setMeLike(Integer meLike) {
        this.meLike = meLike;
    }

    public Integer getMeUnLike() {
        return meUnLike;
    }

    public void setMeUnLike(Integer meUnLike) {
        this.meUnLike = meUnLike;
    }

    public Integer getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(Integer totalComment) {
        this.totalComment = totalComment;
    }

    public ServerLocationPOJO getServerLocationPOJO() {
        return serverLocationPOJO;
    }

    public void setServerLocationPOJO(ServerLocationPOJO serverLocationPOJO) {
        this.serverLocationPOJO = serverLocationPOJO;
    }

    public String getScheduleOn() {
        return scheduleOn;
    }

    public void setScheduleOn(String scheduleOn) {
        this.scheduleOn = scheduleOn;
    }
}
