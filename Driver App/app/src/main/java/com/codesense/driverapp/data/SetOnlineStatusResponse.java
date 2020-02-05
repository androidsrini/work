package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class SetOnlineStatusResponse {

    @SerializedName("message")
    private String message;
@SerializedName("vehicle_id")
    private String vehicle_id;

    @SerializedName("status")
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
