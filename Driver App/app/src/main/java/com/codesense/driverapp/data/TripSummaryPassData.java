package com.codesense.driverapp.data;

import java.io.Serializable;

public class TripSummaryPassData implements Serializable {


    private int trip_status;
    private String completed_on;
    private String end_lat;
    private String end_lng;
    private int waiting_fare;
    private String actual_fare;
    private String total_travelled_km;
    private String booking_id;
    private String pickuped_on;
    private String pickup_lat;
    private String pickup_lng;
    private String booking_no;
    private String payment_mode;
    private String time_taken;

    public int getTrip_status() {
        return trip_status;
    }

    public String getBooking_no() {
        return booking_no;
    }

    public void setBooking_no(String booking_no) {
        this.booking_no = booking_no;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getTime_taken() {
        return time_taken;
    }

    public void setTime_taken(String time_taken) {
        this.time_taken = time_taken;
    }

    public void setTrip_status(int trip_status) {
        this.trip_status = trip_status;
    }

    public String getCompleted_on() {
        return completed_on;
    }

    public void setCompleted_on(String completed_on) {
        this.completed_on = completed_on;
    }

    public String getEnd_lat() {
        return end_lat;
    }

    public void setEnd_lat(String end_lat) {
        this.end_lat = end_lat;
    }

    public String getEnd_lng() {
        return end_lng;
    }

    public void setEnd_lng(String end_lng) {
        this.end_lng = end_lng;
    }

    public int getWaiting_fare() {
        return waiting_fare;
    }

    public void setWaiting_fare(int waiting_fare) {
        this.waiting_fare = waiting_fare;
    }

    public String getActual_fare() {
        return actual_fare;
    }

    public void setActual_fare(String actual_fare) {
        this.actual_fare = actual_fare;
    }

    public String getTotal_travelled_km() {
        return total_travelled_km;
    }

    public void setTotal_travelled_km(String total_travelled_km) {
        this.total_travelled_km = total_travelled_km;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getPickuped_on() {
        return pickuped_on;
    }

    public void setPickuped_on(String pickuped_on) {
        this.pickuped_on = pickuped_on;
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
}
