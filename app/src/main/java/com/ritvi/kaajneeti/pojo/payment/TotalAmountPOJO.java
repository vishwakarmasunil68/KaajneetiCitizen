package com.ritvi.kaajneeti.pojo.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalAmountPOJO {
    @SerializedName("MyPointBalance")
    @Expose
    private Integer myPointBalance;
    @SerializedName("TotalPointDebit")
    @Expose
    private Integer totalPointDebit;
    @SerializedName("TotalPointCredit")
    @Expose
    private String totalPointCredit;
    @SerializedName("MyWalletBalance")
    @Expose
    private String myWalletBalance;
    @SerializedName("TotalWalletDebit")
    @Expose
    private String totalWalletDebit;
    @SerializedName("TotalWalletCredit")
    @Expose
    private String totalWalletCredit;

    public Integer getMyPointBalance() {
        return myPointBalance;
    }

    public void setMyPointBalance(Integer myPointBalance) {
        this.myPointBalance = myPointBalance;
    }

    public Integer getTotalPointDebit() {
        return totalPointDebit;
    }

    public void setTotalPointDebit(Integer totalPointDebit) {
        this.totalPointDebit = totalPointDebit;
    }

    public String getTotalPointCredit() {
        return totalPointCredit;
    }

    public void setTotalPointCredit(String totalPointCredit) {
        this.totalPointCredit = totalPointCredit;
    }

    public String getMyWalletBalance() {
        return myWalletBalance;
    }

    public void setMyWalletBalance(String myWalletBalance) {
        this.myWalletBalance = myWalletBalance;
    }

    public String getTotalWalletDebit() {
        return totalWalletDebit;
    }

    public void setTotalWalletDebit(String totalWalletDebit) {
        this.totalWalletDebit = totalWalletDebit;
    }

    public String getTotalWalletCredit() {
        return totalWalletCredit;
    }

    public void setTotalWalletCredit(String totalWalletCredit) {
        this.totalWalletCredit = totalWalletCredit;
    }
}
