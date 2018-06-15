package com.ritvi.kaajneeti.pojo.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ritvi.kaajneeti.pojo.event.EventAttachmentPOJO;
import com.ritvi.kaajneeti.pojo.event.EventInterestPOJO;
import com.ritvi.kaajneeti.pojo.location.ServerLocationPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sunil on 30-03-2018.
 */

public class EventPOJO implements Serializable {
    @SerializedName("EventId")
    private String eventId;
    @SerializedName("EventUniqueId")
    private String eventUniqueId;
    @SerializedName("EventName")
    private String eventName;
    @SerializedName("EventCoverPhoto")
    private String eventCoverPhoto;
    @SerializedName("EventDescription")
    private String eventDescription;
    @SerializedName("EventLocation")
    private String eventLocation;
    @SerializedName("StartDate")
    private String startDate;
    @SerializedName("EndDate")
    private String endDate;
    @SerializedName("EveryYear")
    private String everyYear;
    @SerializedName("EveryMonth")
    private String everyMonth;
    @SerializedName("EventStatus")
    private String eventStatus;
    @SerializedName("AddedOn")
    private String addedOn;
    @SerializedName("AddedOnTime")
    private String addedOnTime;
    @SerializedName("UpdatedOn")
    private String updatedOn;
    @SerializedName("UpdatedOnTime")
    private String updatedOnTime;
    @SerializedName("EventInterestTotal")
    private String eventInterestTotal;
    @SerializedName("EventProfile")
    private UserProfilePOJO eventProfile;
    @SerializedName("EventAttendee")
    private List<UserProfilePOJO> eventAttendee;
    @SerializedName("EventAttachment")
    private List<EventAttachmentPOJO> eventAttachment;
    @SerializedName("TotalEventInterest")
    private List<EventInterestPOJO> TotalEventInterestList;
    @SerializedName("MeInterested")
    private Integer MeInterested;
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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventUniqueId() {
        return eventUniqueId;
    }

    public void setEventUniqueId(String eventUniqueId) {
        this.eventUniqueId = eventUniqueId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEveryYear() {
        return everyYear;
    }

    public void setEveryYear(String everyYear) {
        this.everyYear = everyYear;
    }

    public String getEveryMonth() {
        return everyMonth;
    }

    public void setEveryMonth(String everyMonth) {
        this.everyMonth = everyMonth;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
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

    public UserProfilePOJO getEventProfile() {
        return eventProfile;
    }

    public void setEventProfile(UserProfilePOJO eventProfile) {
        this.eventProfile = eventProfile;
    }

    public List<UserProfilePOJO> getEventAttendee() {
        return eventAttendee;
    }

    public void setEventAttendee(List<UserProfilePOJO> eventAttendee) {
        this.eventAttendee = eventAttendee;
    }

    public List<EventAttachmentPOJO> getEventAttachment() {
        return eventAttachment;
    }

    public void setEventAttachment(List<EventAttachmentPOJO> eventAttachment) {
        this.eventAttachment = eventAttachment;
    }

    public Integer getMeInterested() {
        return MeInterested;
    }

    public void setMeInterested(Integer meInterested) {
        MeInterested = meInterested;
    }

    public List<EventInterestPOJO> getTotalEventInterestList() {
        return TotalEventInterestList;
    }

    public void setTotalEventInterestList(List<EventInterestPOJO> totalEventInterestList) {
        TotalEventInterestList = totalEventInterestList;
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

    public String getEventInterestTotal() {
        return eventInterestTotal;
    }

    public void setEventInterestTotal(String eventInterestTotal) {
        this.eventInterestTotal = eventInterestTotal;
    }

    public String getEventCoverPhoto() {
        return eventCoverPhoto;
    }

    public void setEventCoverPhoto(String eventCoverPhoto) {
        this.eventCoverPhoto = eventCoverPhoto;
    }
}
