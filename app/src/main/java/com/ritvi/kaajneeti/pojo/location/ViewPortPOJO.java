package com.ritvi.kaajneeti.pojo.location;

import com.google.gson.annotations.SerializedName;

public class ViewPortPOJO {
    @SerializedName("northeast")
    LocationPOJO northeastLocation;
    @SerializedName("southwest")
    LocationPOJO southwestLocation;

    public LocationPOJO getNortheastLocation() {
        return northeastLocation;
    }

    public void setNortheastLocation(LocationPOJO northeastLocation) {
        this.northeastLocation = northeastLocation;
    }

    public LocationPOJO getSouthwestLocation() {
        return southwestLocation;
    }

    public void setSouthwestLocation(LocationPOJO southwestLocation) {
        this.southwestLocation = southwestLocation;
    }
}
