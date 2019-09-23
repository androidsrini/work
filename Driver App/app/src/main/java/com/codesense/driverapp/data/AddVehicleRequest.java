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

    @SerializedName("available_driver_id")
    private String availableDriverId;

    @SerializedName("driver_first_name")
    private String driverFirstName;

    @SerializedName("driver_last_name")
    private String driverLastName;

    @SerializedName("country_id")
    private String countryId;

    @SerializedName("email_id")
    private String emailId;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @SerializedName("mobile_number")
    private String mobileNumber;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @SerializedName("password")
    private String password;

    public String getAvailableDriverId() {
        return availableDriverId;
    }

    public void setAvailableDriverId(String availableDriverId) {
        this.availableDriverId = availableDriverId;
    }

    public String getDriverFirstName() {
        return driverFirstName;
    }

    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    public String getDriverLastName() {
        return driverLastName;
    }

    public void setDriverLastName(String driverLastName) {
        this.driverLastName = driverLastName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

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
