package com.codesense.driverapp.net;

import com.google.gson.JsonElement;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiCallInterface {

    //@Headers(Constant.API_AUTH_KEY_WITH_VALUE)
    @GET(WebserviceUrls.API_INFO)
    Observable<JsonElement> getApiInfoRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey);

    @GET(WebserviceUrls.COUNTRIES_LIST)
    Observable<JsonElement> fetchCountryListRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey);

    @GET(WebserviceUrls.CITIES_LIST)
    Observable<JsonElement> fetchCityListRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey);

    @POST(WebserviceUrls.REGISTER_NEW_OWNER)
    @FormUrlEncoded
    Observable<JsonElement> registerNewOwnerRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey, @FieldMap HashMap<String, String> param);

    @POST(WebserviceUrls.SENT_OTP)
    @FormUrlEncoded
    Observable<JsonElement> sentOTPRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey, @FieldMap HashMap<String, String> params);

    @POST(WebserviceUrls.VERIFY_OTP)
    @FormUrlEncoded
    Observable<JsonElement> verifyOTPRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey, @FieldMap HashMap<String, String> params);
}
