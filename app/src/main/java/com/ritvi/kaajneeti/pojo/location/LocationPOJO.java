package com.ritvi.kaajneeti.pojo.location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationPOJO {
    @SerializedName("geometry")
    private GeometryPOJO geometry;
    @SerializedName("icon")
    private String icon;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("place_id")
    private String placeId;
    @SerializedName("reference")
    private String reference;
    @SerializedName("scope")
    private String scope;
    @SerializedName("types")
    private List<String> types = null;
    @SerializedName("vicinity")
    private String vicinity;

    public GeometryPOJO getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryPOJO geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
}
