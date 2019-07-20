package com.codesense.driverapp.ui.vehicle;

public class VehicleListModel {

    String vehicleNo;
    String vehicleName;
    String vehicleStatus;

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public VehicleListModel(String vehicleNo, String vehicleName, String vehicleStatus) {
        this.vehicleNo = vehicleNo;
        this.vehicleName = vehicleName;
        this.vehicleStatus = vehicleStatus;
    }
}
