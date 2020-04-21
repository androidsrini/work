package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AcceptResponse implements Serializable {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("message_deliver_status")
    private int message_deliver_status;

    @SerializedName("trip_details")
    private TripDetailData tripDetailDataList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessage_deliver_status() {
        return message_deliver_status;
    }

    public void setMessage_deliver_status(int message_deliver_status) {
        this.message_deliver_status = message_deliver_status;
    }

    public TripDetailData getTripDetailDataList() {
        return tripDetailDataList;
    }

    public void setTripDetailDataList(TripDetailData tripDetailDataList) {
        this.tripDetailDataList = tripDetailDataList;
    }
}
