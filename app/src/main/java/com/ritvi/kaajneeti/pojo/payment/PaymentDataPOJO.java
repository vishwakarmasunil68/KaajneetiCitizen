package com.ritvi.kaajneeti.pojo.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;

public class PaymentDataPOJO {
    @SerializedName("PaymentTransactionLogId")
    @Expose
    private String paymentTransactionLogId;
    @SerializedName("PaymentGatewayId")
    @Expose
    private String paymentGatewayId;
    @SerializedName("IsWalletUse")
    @Expose
    private Integer isWalletUse;
    @SerializedName("PaymentGatewayName")
    @Expose
    private String paymentGatewayName;
    @SerializedName("TransactionId")
    @Expose
    private String transactionId;
    @SerializedName("TransactionDate")
    @Expose
    private String transactionDate;
    @SerializedName("TransactionAmount")
    @Expose
    private String transactionAmount;
    @SerializedName("GatewayTransaction")
    @Expose
    private String gatewayTransaction;
    @SerializedName("WalletTransaction")
    @Expose
    private String walletTransaction;
    @SerializedName("TransactionShippingAmount")
    @Expose
    private String transactionShippingAmount;
    @SerializedName("DebitOrCredit")
    @Expose
    private String debitOrCredit;
    @SerializedName("TransactionComment")
    @Expose
    private String transactionComment;
    @SerializedName("TransactionStatus")
    @Expose
    private String transactionStatus;
    @SerializedName("IsContribute")
    @Expose
    private String isContribute;
    @SerializedName("AddedOn")
    @Expose
    private String addedOn;
    @SerializedName("AddedOnTime")
    @Expose
    private String addedOnTime;
    @SerializedName("PaymentBy")
    @Expose
    private UserProfilePOJO paymentBy;
    @SerializedName("PaymentTo")
    @Expose
    private UserProfilePOJO paymentTo;

    public String getPaymentTransactionLogId() {
        return paymentTransactionLogId;
    }

    public void setPaymentTransactionLogId(String paymentTransactionLogId) {
        this.paymentTransactionLogId = paymentTransactionLogId;
    }

    public String getPaymentGatewayId() {
        return paymentGatewayId;
    }

    public void setPaymentGatewayId(String paymentGatewayId) {
        this.paymentGatewayId = paymentGatewayId;
    }

    public Integer getIsWalletUse() {
        return isWalletUse;
    }

    public void setIsWalletUse(Integer isWalletUse) {
        this.isWalletUse = isWalletUse;
    }

    public String getPaymentGatewayName() {
        return paymentGatewayName;
    }

    public void setPaymentGatewayName(String paymentGatewayName) {
        this.paymentGatewayName = paymentGatewayName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getGatewayTransaction() {
        return gatewayTransaction;
    }

    public void setGatewayTransaction(String gatewayTransaction) {
        this.gatewayTransaction = gatewayTransaction;
    }

    public String getWalletTransaction() {
        return walletTransaction;
    }

    public void setWalletTransaction(String walletTransaction) {
        this.walletTransaction = walletTransaction;
    }

    public String getTransactionShippingAmount() {
        return transactionShippingAmount;
    }

    public void setTransactionShippingAmount(String transactionShippingAmount) {
        this.transactionShippingAmount = transactionShippingAmount;
    }

    public String getDebitOrCredit() {
        return debitOrCredit;
    }

    public void setDebitOrCredit(String debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }

    public String getTransactionComment() {
        return transactionComment;
    }

    public void setTransactionComment(String transactionComment) {
        this.transactionComment = transactionComment;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getIsContribute() {
        return isContribute;
    }

    public void setIsContribute(String isContribute) {
        this.isContribute = isContribute;
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

    public UserProfilePOJO getPaymentBy() {
        return paymentBy;
    }

    public void setPaymentBy(UserProfilePOJO paymentBy) {
        this.paymentBy = paymentBy;
    }

    public UserProfilePOJO getPaymentTo() {
        return paymentTo;
    }

    public void setPaymentTo(UserProfilePOJO paymentTo) {
        this.paymentTo = paymentTo;
    }
}
