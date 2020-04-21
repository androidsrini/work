package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WayPointsModel implements Serializable {

    @SerializedName("lat")
    String lat;
    @SerializedName("lng")
    String lng;
    @SerializedName("fare")
    String fare;
    @SerializedName("distance")
    String distance;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
