package com.ritvi.kaajneeti.pojo.information;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InformationAttachmentPOJO {
    @SerializedName("InformationAttachmentId")
    @Expose
    private String informationAttachmentId;
    @SerializedName("InformationId")
    @Expose
    private String informationId;
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

    public String getInformationAttachmentId() {
        return informationAttachmentId;
    }

    public void setInformationAttachmentId(String informationAttachmentId) {
        this.informationAttachmentId = informationAttachmentId;
    }

    public String getInformationId() {
        return informationId;
    }

    public void setInformationId(String informationId) {
        this.informationId = informationId;
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
