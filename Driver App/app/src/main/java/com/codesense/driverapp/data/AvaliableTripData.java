package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AvaliableTripData  {

    @SerializedName("booking_id")
    private String booking_id;

    @SerializedName("vehicle_id")
    private String vehicle_id;

    @SerializedName("vehicle_type_id")
    private String vehicle_type_id;

    @SerializedName("customer_name")
    private String customer_name;

    @SerializedName("customer_contact")
    private String customer_contact;

    @SerializedName("pickup_lat")
    private String pickup_lat;

    @SerializedName("pickup_lng")
    private String pickup_lng;

    @SerializedName("pickup_location")
    private String pickup_location;

    @SerializedName("ride_scheduled_on")
    private String ride_scheduled_on;

    @SerializedName("distance")
    private String distance;

    @SerializedName("duration")
    private String duration;

    @SerializedName("driving_distance")
    private String driving_distance;

    @SerializedName("drop_locations")
    private List<DropLocationData> dropLocationDataList;


    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getVehicle_type_id() {
        return vehicle_type_id;
    }

    public void setVehicle_type_id(String vehicle_type_id) {
        this.vehicle_type_id = vehicle_type_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_contact() {
        return customer_contact;
    }

    public void setCustomer_contact(String customer_contact) {
        this.customer_contact = customer_contact;
    }

    public String getPickup_lat() {
        return pickup_lat;
    }

    public void setPickup_lat(String pickup_lat) {
        this.pickup_lat = pickup_lat;
    }

    public String getPickup_lng() {
        return pickup_lng;
    }

    public void setPickup_lng(String pickup_lng) {
        this.pickup_lng = pickup_lng;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public void setPickup_location(String pickup_location) {
        this.pickup_location = pickup_location;
    }

    public String getRide_scheduled_on() {
        return ride_scheduled_on;
    }

    public void setRide_scheduled_on(String ride_scheduled_on) {
        this.ride_scheduled_on = ride_scheduled_on;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDriving_distance() {
        return driving_distance;
    }

    public void setDriving_distance(String driving_distance) {
        this.driving_distance = driving_distance;
    }

    public List<DropLocationData> getDropLocationDataList() {
        return dropLocationDataList;
    }

    public void setDropLocationDataList(List<DropLocationData> dropLocationDataList) {
        this.dropLocationDataList = dropLocationDataList;
    }
}
