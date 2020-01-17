package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class AddDriverRequest {

    @SerializedName("user_id")
    private String userId;

    @SerializedName("driver_name")
    private String driverName;
    @SerializedName("invite_code")
    private String inviteCode;
    @SerializedName("country_id")
    private String countryId;
    @SerializedName("email_id")
    private String emailId;
    @SerializedName("password")
    private String password;
    @SerializedName("mobile_number")
    private String mobileNumber;
    @SerializedName("vehicle_id")
    private String vehicleId;

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
