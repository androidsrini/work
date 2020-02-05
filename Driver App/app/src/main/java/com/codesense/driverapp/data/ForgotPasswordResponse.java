package com.codesense.driverapp.data;

import com.google.gson.annotations.SerializedName;

public class ForgotPasswordResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("otp_verify")
    private int otp_verify;

    @SerializedName("message")
    private String message;

    @SerializedName("country_dial_code")
    private String country_dial_code;

    @SerializedName("mobile_number")
    private String mobile_number;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("access_token")
    private String access_token;

    @SerializedName("user_type")
    private String user_type;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOtp_verify() {
        return otp_verify;
    }

    public void setOtp_verify(int otp_verify) {
        this.otp_verify = otp_verify;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCountry_dial_code() {
        return country_dial_code;
    }

    public void setCountry_dial_code(String country_dial_code) {
        this.country_dial_code = country_dial_code;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
