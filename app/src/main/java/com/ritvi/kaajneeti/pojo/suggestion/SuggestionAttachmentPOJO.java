package com.ritvi.kaajneeti.pojo.suggestion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SuggestionAttachmentPOJO implements Serializable{
    @SerializedName("SuggestionAttachmentId")
    @Expose
    private String suggestionAttachmentId;
    @SerializedName("SuggestionId")
    @Expose
    private String suggestionId;
    @SerializedName("AttachmentTypeId")
    @Expose
    private String attachmentTypeId;
    @SerializedName("AttachmentType")
    @Expose
    private String attachmentType;
    @SerializedName("AttachmentFile")
    @Expose
    private String attachmentFile;
    @SerializedName("AttachmentOrginalFile")
    @Expose
    private String attachmentOrginalFile;
    @SerializedName("AttachmentThumb")
    @Expose
    private String attachmentThumb;
    @SerializedName("AttachmentOrder")
    @Expose
    private String attachmentOrder;
    @SerializedName("AttachmentStatus")
    @Expose
    private String attachmentStatus;
    @SerializedName("AddedOn")
    @Expose
    private String addedOn;
    @SerializedName("AddedOnTime")
    @Expose
    private String addedOnTime;

    public String getSuggestionAttachmentId() {
        return suggestionAttachmentId;
    }

    public void setSuggestionAttachmentId(String suggestionAttachmentId) {
        this.suggestionAttachmentId = suggestionAttachmentId;
    }

    public String getSuggestionId() {
        return suggestionId;
    }

    public void setSuggestionId(String suggestionId) {
        this.suggestionId = suggestionId;
    }

    public String getAttachmentTypeId() {
        return attachmentTypeId;
    }

    public void setAttachmentTypeId(String attachmentTypeId) {
        this.attachmentTypeId = attachmentTypeId;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getAttachmentFile() {
        return attachmentFile;
    }

    public void setAttachmentFile(String attachmentFile) {
        this.attachmentFile = attachmentFile;
    }

    public String getAttachmentOrginalFile() {
        return attachmentOrginalFile;
    }

    public void setAttachmentOrginalFile(String attachmentOrginalFile) {
        this.attachmentOrginalFile = attachmentOrginalFile;
    }

    public String getAttachmentThumb() {
        return attachmentThumb;
    }

    public void setAttachmentThumb(String attachmentThumb) {
        this.attachmentThumb = attachmentThumb;
    }

    public String getAttachmentOrder() {
        return attachmentOrder;
    }

    public void setAttachmentOrder(String attachmentOrder) {
        this.attachmentOrder = attachmentOrder;
    }

    public String getAttachmentStatus() {
        return attachmentStatus;
    }

    public void setAttachmentStatus(String attachmentStatus) {
        this.attachmentStatus = attachmentStatus;
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
}
