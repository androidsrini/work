package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UpdateVehicleResponse implements Serializable {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("fare_per_km")
    private String fare_per_km;


    @SerializedName("available_trips")
    private List<AvaliableTripData> avaliableTripDataList;

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

    public String getFare_per_km() {
        return fare_per_km;
    }

    public void setFare_per_km(String fare_per_km) {
        this.fare_per_km = fare_per_km;
    }

    public List<AvaliableTripData> getAvaliableTripDataList() {
        return avaliableTripDataList;
    }

    public void setAvaliableTripDataList(List<AvaliableTripData> avaliableTripDataList) {
        this.avaliableTripDataList = avaliableTripDataList;
    }
}
