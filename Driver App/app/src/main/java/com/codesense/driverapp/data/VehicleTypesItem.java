package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class VehicleTypesItem {

    @SerializedName("vehicle_type_id")
    private String vehicleTypeId;

    @SerializedName("vehicle_type")
    private String vehicleType;

    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Override
    public String toString() {
        return vehicleType;
    }
}