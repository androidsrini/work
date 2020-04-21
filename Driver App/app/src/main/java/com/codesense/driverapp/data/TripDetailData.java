package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TripDetailData implements Serializable {

    @SerializedName("booking_id")
    private String booking_id;

    @SerializedName("customer_name")
    private String customer_name;

    @SerializedName("customer_contact")
    private String customer_contact;

    @SerializedName("vehicle_type_id")
    private String vehicle_type_id;

    @SerializedName("pickup_lat")
    private String pickup_lat;

    @SerializedName("pickup_lng")
    private String pickup_lng;

    @SerializedName("pickup_location")
    private String pickup_location;

    @SerializedName("is_advance_booking")
    private String is_advance_booking;

    @SerializedName("ride_scheduled_on")
    private String ride_scheduled_on;

    @SerializedName("trip_status")
    private String trip_status;

    @SerializedName("booked_on")
    private String booked_on;

    @SerializedName("booked_for")
    private String booked_for;

    @SerializedName("payment_mode")
    private String payment_mode;

    @SerializedName("normal_fare")
    private String normal_fare;

    @SerializedName("payment_status")
    private String payment_status;

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
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

    public String getVehicle_type_id() {
        return vehicle_type_id;
    }

    public void setVehicle_type_id(String vehicle_type_id) {
        this.vehicle_type_id = vehicle_type_id;
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

    public String getIs_advance_booking() {
        return is_advance_booking;
    }

    public void setIs_advance_booking(String is_advance_booking) {
        this.is_advance_booking = is_advance_booking;
    }

    public String getRide_scheduled_on() {
        return ride_scheduled_on;
    }

    public void setRide_scheduled_on(String ride_scheduled_on) {
        this.ride_scheduled_on = ride_scheduled_on;
    }

    public String getTrip_status() {
        return trip_status;
    }

    public void setTrip_status(String trip_status) {
        this.trip_status = trip_status;
    }

    public String getBooked_on() {
        return booked_on;
    }

    public void setBooked_on(String booked_on) {
        this.booked_on = booked_on;
    }

    public String getBooked_for() {
        return booked_for;
    }

    public void setBooked_for(String booked_for) {
        this.booked_for = booked_for;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getNormal_fare() {
        return normal_fare;
    }

    public void setNormal_fare(String normal_fare) {
        this.normal_fare = normal_fare;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }
}
