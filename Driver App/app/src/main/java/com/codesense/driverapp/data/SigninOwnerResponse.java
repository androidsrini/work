package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class SigninOwnerResponse {

    @SerializedName("email_id")
    private String emailId;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("country_dial_code")
    private String countryDialCode;

    @SerializedName("otp_verify")
    private int otpVerify;

    @SerializedName("user_type")
    private String userType;

    @SerializedName("agreement_accept")
    private int agreementAccept;

    @SerializedName("user_id")
    private String userId;
 @SerializedName("vehicle_id")
    private String vehicleId;

    @SerializedName("owner_type_id")
    private int ownerTypeId;

    @SerializedName("profile_picture")
    private String profilePicture;

    @SerializedName("message")
    private String message;

    @SerializedName("mobile_number")
    private String mobileNumber;
@SerializedName("display_name")
    private String displayName;

    @SerializedName("status")
    private int status;
    @SerializedName("is_activated")
    private int is_activated;
    @SerializedName("vehicle_activation")
    private int is_vehicle_activate;

    @SerializedName("live_status")
    private int liveStatus;


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public int getIs_vehicle_activate() {
        return is_vehicle_activate;
    }

    public void setIs_vehicle_activate(int is_vehicle_activate) {
        this.is_vehicle_activate = is_vehicle_activate;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setCountryDialCode(String countryDialCode) {
        this.countryDialCode = countryDialCode;
    }

    public String getCountryDialCode() {
        return countryDialCode;
    }

    public void setOtpVerify(int otpVerify) {
        this.otpVerify = otpVerify;
    }

    public int getOtpVerify() {
        return otpVerify;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    public void setAgreementAccept(int agreementAccept) {
        this.agreementAccept = agreementAccept;
    }

    public int getAgreementAccept() {
        return agreementAccept;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setOwnerTypeId(int ownerTypeId) {
        this.ownerTypeId = ownerTypeId;
    }

    public int getOwnerTypeId() {
        return ownerTypeId;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public int getIs_activated() {
        return is_activated;
    }

    public void setIs_activated(int is_activated) {
        this.is_activated = is_activated;
    }


    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Override
    public String toString() {
        return
                "SigninOwnerResponse{" +
                        "email_id = '" + emailId + '\'' +
                        ",access_token = '" + accessToken + '\'' +
                        ",country_dial_code = '" + countryDialCode + '\'' +
                        ",otp_verify = '" + otpVerify + '\'' +
                        ",user_type = '" + userType + '\'' +
                        ",agreement_accept = '" + agreementAccept + '\'' +
                        ",user_id = '" + userId + '\'' +
                        ",owner_type_id = '" + ownerTypeId + '\'' +
                        ",profile_picture = '" + profilePicture + '\'' +
                        ",message = '" + message + '\'' +
                        ",mobile_number = '" + mobileNumber + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}