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

    /**
     * This method will create sigin in api request as hashmap
     * @param emailId
     * @param password
     * @return HashMap.
     */
    private HashMap<String, String> getSignInRequestParam(String emailId, String password) {
        HashMap<String, String> param = new HashMap<>();
        param.put(Constant.EMAIL_ID_PARAM, emailId);
        param.put(Constant.PASSWORD_PARAM, password);
        return param;
    }


    /**
     * This method to create userId param value as HashMap.
     * @return HashMap;
     */
    private HashMap<String, String> getUserIDRequestParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put(Constant.USER_ID_PARAM, appSharedPreference.getUserID());
        return param;
    }

    /**
     * This method to create Agreement Accept request.
     * @param agreementAccept
     * @return HashMap
     */
    private HashMap<String, String> getAgreementAcceptRequestParam(String agreementAccept) {
        HashMap<String, String> param = new HashMap<>();
        param.put(Constant.USER_ID_PARAM, appSharedPreference.getUserID());
        param.put(Constant.AGREEMENT_ACCEPT_REQUEST, agreementAccept);
        return param;
    }

    /**
     * This method to create Owner type request.
     * @param ownerType
     * @return HashMap
     */
    private HashMap<String, String> getOwnerTypeRequestParam(String ownerType) {
        HashMap<String, String> param = new HashMap<>();
        param.put(Constant.USER_ID_PARAM, appSharedPreference.getUserID());
        param.put(Constant.OWNER_TYPE_ID_PARAM, ownerType);
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

    public Observable<JsonElement> signInRequest(String apiKey, String emailId, String password) {
        return apiCallInterface.signInRequest(apiKey, getSignInRequestParam(emailId, password));
    }

    public Observable<JsonElement> fetchOwnerTypeRequest(String apikey) {
        return apiCallInterface.fetchOwnerTypeRequest(apikey, appSharedPreference.getAccessTokenKey());
    }

    public Observable<JsonElement> getOwnerAgreementRequest(String apikey) {
        return apiCallInterface.getOwnerAgreementRequest(apikey, appSharedPreference.getAccessTokenKey(), getUserIDRequestParam());
    }

    public Observable<JsonElement> updateAgreementAcceptRequest(String apikey, String agreementAccept) {
        return apiCallInterface.fetchAgreementAcceptRequest(apikey, appSharedPreference.getAccessTokenKey(), getAgreementAcceptRequestParam(agreementAccept));
    }

    public Observable<JsonElement> updateRegistationOwnerTypeRequest(String apikey, String ownerTypeId) {
        return apiCallInterface.updateRegistationOwnerTypeRequest(apikey, appSharedPreference.getAccessTokenKey(), getOwnerTypeRequestParam(ownerTypeId));
    }

    public Observable<JsonElement> fetchVehicleTypesRequest(String apikey) {
        return apiCallInterface.fetchVehicleTypesRequest(apikey, appSharedPreference.getAccessTokenKey());
    }
}
