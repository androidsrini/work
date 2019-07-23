package com.codesense.driverapp.net;

import com.google.gson.JsonElement;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiCallInterface {

    //@Headers(Constant.API_AUTH_KEY_WITH_VALUE)
    @GET(WebserviceUrls.API_INFO)
    Observable<JsonElement> getApiInfoRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey);

    @GET(WebserviceUrls.COUNTRIES_LIST)
    Observable<JsonElement> fetchCountryListRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey);

    @GET(WebserviceUrls.CITIES_LIST)
    Observable<JsonElement> fetchCityListRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey);
}
