package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class CompleteTripResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("trip_details")
    private CompleteTripData completeTripData;

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

    public CompleteTripData getCompleteTripData() {
        return completeTripData;
    }

    public void setCompleteTripData(CompleteTripData completeTripData) {
        this.completeTripData = completeTripData;
    }
}
