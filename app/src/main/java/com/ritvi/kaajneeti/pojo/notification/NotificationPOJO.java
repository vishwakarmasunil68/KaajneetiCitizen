package com.ritvi.kaajneeti.pojo.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;

import java.io.Serializable;

public class NotificationPOJO implements Serializable{
    @SerializedName("NotificationId")
    @Expose
    private String notificationId;
    @SerializedName("NotificationFeedId")
    @Expose
    private String notificationFeedId;
    @SerializedName("NotificationAddedOn")
    @Expose
    private String notificationAddedOn;
    @SerializedName("NotificationFrom")
    @Expose
    private String notificationFrom;
    @SerializedName("NotificationFromProfile")
    @Expose
    private UserProfilePOJO notificationFromProfile;
    @SerializedName("NotificationType")
    @Expose
    private String notificationType;
    @SerializedName("NotificationDescription")
    @Expose
    private String notificationDescription;
    @SerializedName("NotificationSentYesNo")
    @Expose
    private String notificationSentYesNo;
    @SerializedName("NotificationReceivedYesNo")
    @Expose
    private String notificationReceivedYesNo;
    @SerializedName("NotificationReadOn")
    @Expose
    private String notificationReadOn;
    @SerializedName("NotificationFromToStatus")
    @Expose
    private String notificationFromToStatus;

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationFeedId() {
        return notificationFeedId;
    }

    public void setNotificationFeedId(String notificationFeedId) {
        this.notificationFeedId = notificationFeedId;
    }

    public String getNotificationAddedOn() {
        return notificationAddedOn;
    }

    public void setNotificationAddedOn(String notificationAddedOn) {
        this.notificationAddedOn = notificationAddedOn;
    }

    public String getNotificationFrom() {
        return notificationFrom;
    }

    public void setNotificationFrom(String notificationFrom) {
        this.notificationFrom = notificationFrom;
    }

    public UserProfilePOJO getNotificationFromProfile() {
        return notificationFromProfile;
    }

    public void setNotificationFromProfile(UserProfilePOJO notificationFromProfile) {
        this.notificationFromProfile = notificationFromProfile;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationDescription() {
        return notificationDescription;
    }

    public void setNotificationDescription(String notificationDescription) {
        this.notificationDescription = notificationDescription;
    }

    public String getNotificationSentYesNo() {
        return notificationSentYesNo;
    }

    public void setNotificationSentYesNo(String notificationSentYesNo) {
        this.notificationSentYesNo = notificationSentYesNo;
    }

    public String getNotificationReceivedYesNo() {
        return notificationReceivedYesNo;
    }

    public void setNotificationReceivedYesNo(String notificationReceivedYesNo) {
        this.notificationReceivedYesNo = notificationReceivedYesNo;
    }

    public String getNotificationReadOn() {
        return notificationReadOn;
    }

    public void setNotificationReadOn(String notificationReadOn) {
        this.notificationReadOn = notificationReadOn;
    }

    public String getNotificationFromToStatus() {
        return notificationFromToStatus;
    }

    public void setNotificationFromToStatus(String notificationFromToStatus) {
        this.notificationFromToStatus = notificationFromToStatus;
    }
}
