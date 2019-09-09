package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class AddVehicleRequest {

    @SerializedName("driver_id")
    private String driverId;

    @SerializedName("vehicle_type_id")
    private String vehicleTypeId;

    @SerializedName("vehicle_number")
    private String vehicleNumber;
    @SerializedName("vehicle_name")
    private String vehicleName;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }
}
