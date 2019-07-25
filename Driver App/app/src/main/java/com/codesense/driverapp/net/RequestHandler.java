package com.codesense.driverapp.net;

import com.codesense.driverapp.request.RegisterNewUser;
import com.google.gson.JsonElement;

import java.util.HashMap;

import io.reactivex.Observable;

public class RequestHandler {

    private ApiCallInterface apiCallInterface;
    /*private static final RequestHandler requestHandler = new RequestHandler();

    public static RequestHandler GetInstance() {
        return requestHandler;
    }*/

    private HashMap<String, String> getSendOTPRequestParam(String userID, String phoneNumber) {
        HashMap<String, String> param = new HashMap<>();
        param.put(Constant.USER_ID_PARAM, userID);
        param.put(Constant.PHONE_NUMBER_PARAM, phoneNumber);
        return param;
    }

    public RequestHandler(ApiCallInterface apiCallInterface) {
        this.apiCallInterface = apiCallInterface;
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
        return apiCallInterface.registerNewOwnerRequest(apiKey, registerNewUser);
    }

    public Observable<JsonElement> sentOTPRequest(String apiKey, String userID, String phoneNumber) {
        return apiCallInterface.sentOTPRequest(apiKey, getSendOTPRequestParam(userID, phoneNumber));
    }
}
