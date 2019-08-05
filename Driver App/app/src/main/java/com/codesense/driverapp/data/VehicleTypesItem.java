package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class VehicleTypesItem {

    @SerializedName("vehicle_type_id")
    private String vehicleTypeId;

    @SerializedName("vehicle_type")
    private String vehicleType;

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
        return
                "VehicleTypesItem{" +
                        "vehicle_type_id = '" + vehicleTypeId + '\'' +
                        ",vehicle_type = '" + vehicleType + '\'' +
                        "}";
    }
}