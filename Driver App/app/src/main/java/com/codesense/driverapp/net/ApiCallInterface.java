package com.codesense.driverapp.net;

import com.codesense.driverapp.request.RegisterNewUser;
import com.google.gson.JsonElement;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.QueryMap;

public interface ApiCallInterface {

    //@Headers(Constant.API_AUTH_KEY_WITH_VALUE)
    @GET(WebserviceUrls.API_INFO)
    Observable<JsonElement> getApiInfoRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey);

    @GET(WebserviceUrls.COUNTRIES_LIST)
    Observable<JsonElement> fetchCountryListRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey);

    @GET(WebserviceUrls.CITIES_LIST)
    Observable<JsonElement> fetchCityListRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey);

    @GET(WebserviceUrls.REGISTER_NEW_OWNER)
    Observable<JsonElement> registerNewOwnerRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey, @Body RegisterNewUser registerNewUser);

    @GET(WebserviceUrls.SENT_OTP)
    Observable<JsonElement> sentOTPRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey, @QueryMap HashMap<String, String> params);
}
