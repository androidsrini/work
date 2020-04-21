package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class DropLocationData {

    @SerializedName("booking_id")
    private String booking_id;
@SerializedName("drop_lat")
    private String drop_lat;
@SerializedName("drop_lng")
    private String drop_lng;

@SerializedName("drop_location")
    private String drop_location;

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getDrop_lat() {
        return drop_lat;
    }

    public void setDrop_lat(String drop_lat) {
        this.drop_lat = drop_lat;
    }

    public String getDrop_lng() {
        return drop_lng;
    }

    public void setDrop_lng(String drop_lng) {
        this.drop_lng = drop_lng;
    }

    public String getDrop_location() {
        return drop_location;
    }

    public void setDrop_location(String drop_location) {
        this.drop_location = drop_location;
    }
}
