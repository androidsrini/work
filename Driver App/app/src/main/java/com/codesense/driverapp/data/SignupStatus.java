package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class SignupStatus {

    @SerializedName("otp_verify")
    private String otpVerify;

    @SerializedName("agreement_accept")
    private String agreementAccept;

    @SerializedName("is_activated")
    private String isActivated;

    @SerializedName("owner_type_id")
    private String ownerTypeId;

    @SerializedName("vehicle_activation")
    private String vehicleActivation;

    @SerializedName("live_status")
    private String liveStatus;

    @SerializedName("vehicle_id")
    private String vehicleId;

    public void setOtpVerify(String otpVerify) {
        this.otpVerify = otpVerify;
    }

    public String getOtpVerify() {
        return otpVerify;
    }

    public void setAgreementAccept(String agreementAccept) {
        this.agreementAccept = agreementAccept;
    }

    public String getAgreementAccept() {
        return agreementAccept;
    }

    public void setIsActivated(String isActivated) {
        this.isActivated = isActivated;
    }

    public String getIsActivated() {
        return isActivated;
    }

    public void setOwnerTypeId(String ownerTypeId) {
        this.ownerTypeId = ownerTypeId;
    }

    public String getOwnerTypeId() {
        return ownerTypeId;
    }

    public void setVehicleActivation(String vehicleActivation) {
        this.vehicleActivation = vehicleActivation;
    }

    public String getVehicleActivation() {
        return vehicleActivation;
    }

    public void setLiveStatus(String liveStatus) {
        this.liveStatus = liveStatus;
    }

    public String getLiveStatus() {
        return liveStatus;
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
                "SignupStatus{" +
                        "otp_verify = '" + otpVerify + '\'' +
                        ",agreement_accept = '" + agreementAccept + '\'' +
                        ",is_activated = '" + isActivated + '\'' +
                        ",owner_type_id = '" + ownerTypeId + '\'' +
                        ",vehicle_activation = '" + vehicleActivation + '\'' +
                        ",live_status = '" + liveStatus + '\'' +
                        "}";
    }
}