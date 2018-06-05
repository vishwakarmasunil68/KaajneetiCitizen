package com.ritvi.kaajneeti.pojo.user;

import com.google.gson.annotations.SerializedName;

public class SummaryPOJO {
    @SerializedName("type")
    String type;
    @SerializedName("total")
    String total;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
