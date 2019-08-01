package com.codesense.driverapp.net;

import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.request.RegisterNewUser;
import com.google.gson.JsonElement;

import java.util.HashMap;

import io.reactivex.Observable;

public class RequestHandler {

    private ApiCallInterface apiCallInterface;

    protected AppSharedPreference appSharedPreference;

    private HashMap<String, String> getSaveNewUserRequestParam(RegisterNewUser registerNewUser) {
        HashMap<String, String> param = new HashMap<>();
        param.put(Constant.FIRST_NAME_PARAM, registerNewUser.getFirstName());
        param.put(Constant.LAST_NAME_PARAM, registerNewUser.getLastName());
        param.put(Constant.MOBILE_NUMBER_PARAM, registerNewUser.getMobileNumber());
        param.put(Constant.PASSWORD_PARAM, registerNewUser.getPassword());
        param.put(Constant.COUNTRY_ID_PARAM, registerNewUser.getCountryId());
        param.put(Constant.CITY_ID_PARAM, registerNewUser.getCityId());
        param.put(Constant.INVITE_CODE_PARAM, registerNewUser.getInviteCode());
        param.put(Constant.EMAIL_ID_PARAM, registerNewUser.getEmailId());
        return param;
    }

    private HashMap<String, String> getSendOTPRequestParam(String userID, String phoneNumber) {
        HashMap<String, String> param = new HashMap<>();
        param.put(Constant.USER_ID_PARAM, userID);
        param.put(Constant.PHONE_NUMBER_PARAM, phoneNumber);
        return param;
    }

    private HashMap<String, String> getVerifyOTPRequestParam(String userID, String otp) {
        HashMap<String, String> param = new HashMap<>();
        param.put(Constant.USER_ID_PARAM, userID);
        param.put(Constant.OTP_PARAM, otp);
        return param;
    }

    public RequestHandler(ApiCallInterface apiCallInterface, AppSharedPreference appSharedPreference) {
        this.apiCallInterface = apiCallInterface;
        this.appSharedPreference = appSharedPreference;
        //this.apiCallInterface = NetworkModule.GetInstance().getApiCallInterface();
    }

    public Observable<JsonElement> getApiInfoRequest(String apiKey) {
        return apiCallInterface.getApiInfoRequest(apiKey);
    }

    public Observable<JsonElement> fetchCountryListRequest(String apiKey) {
        return apiCallInterface.fetchCountryListRequest(apiKey);
    }

    public Observable<JsonElement> fetchCityListRequest(String apiKey) {
        return apiCallInterface.fetchCityListRequest(apiKey);
    }

    public Observable<JsonElement> registerNewOwnerRequest(String apiKey, RegisterNewUser registerNewUser) {
        return apiCallInterface.registerNewOwnerRequest(apiKey, getSaveNewUserRequestParam(registerNewUser));
    }

    public Observable<JsonElement> sentOTPRequest(String apiKey, String userID, String phoneNumber) {
        return apiCallInterface.sentOTPRequest(apiKey, appSharedPreference.getAccessTokenKey(), getSendOTPRequestParam(userID, phoneNumber));
    }

    public Observable<JsonElement> verifyOTPRequest(String apiKey, String userID, String otp) {
        return apiCallInterface.verifyOTPRequest(apiKey, appSharedPreference.getAccessTokenKey(), getVerifyOTPRequestParam(userID, otp));
    }
}
