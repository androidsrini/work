package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class AddDriverRequest {

    @SerializedName("user_id")
    private String userId;

    @SerializedName("driver_first_name")
    private String driverFirstName;

    @SerializedName("driver_last_name")
    private String driverLastName;

    @SerializedName("country_id")
    private String countryId;

    @SerializedName("email_id")
    private String emailId;

    @SerializedName("password")
    private String password;

    @SerializedName("mobile_number")
    private String mobileNumber;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
