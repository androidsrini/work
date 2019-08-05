package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleTypeResponse {

    @SerializedName("vehicle_types")
    private List<VehicleTypesItem> vehicleTypes;

    @SerializedName("status")
    private int status;

    public List<VehicleTypesItem> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setVehicleTypes(List<VehicleTypesItem> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "VehicleTypeResponse{" +
                        "vehicle_types = '" + vehicleTypes + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}