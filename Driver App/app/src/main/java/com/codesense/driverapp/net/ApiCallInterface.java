package com.codesense.driverapp.net;

import com.google.gson.JsonElement;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiCallInterface {

    //@Headers(Constant.API_AUTH_KEY_WITH_VALUE)
    @GET(WebserviceUrls.API_INFO)
    Observable<JsonElement> getApiInfoRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey);
}
