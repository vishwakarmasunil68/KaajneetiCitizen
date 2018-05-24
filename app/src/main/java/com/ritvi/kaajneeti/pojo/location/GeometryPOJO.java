package com.ritvi.kaajneeti.pojo.location;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

public class GeometryPOJO {
    @SerializedName("location")
    private LatLongPOJO location;
    @SerializedName("viewport")
    private ViewPortPOJO viewport;

    public LatLongPOJO getLocation() {
        return location;
    }

    public void setLocation(LatLongPOJO location) {
        this.location = location;
    }

    public ViewPortPOJO getViewport() {
        return viewport;
    }

    public void setViewport(ViewPortPOJO viewport) {
        this.viewport = viewport;
    }
}
