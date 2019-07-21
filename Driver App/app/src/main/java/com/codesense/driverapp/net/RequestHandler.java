package com.codesense.driverapp.net;

import com.google.gson.JsonElement;

import io.reactivex.Observable;

public class RequestHandler {

    private ApiCallInterface apiCallInterface;
    /*private static final RequestHandler requestHandler = new RequestHandler();

    public static RequestHandler GetInstance() {
        return requestHandler;
    }*/

    public RequestHandler(ApiCallInterface apiCallInterface) {
        this.apiCallInterface = apiCallInterface;
        //this.apiCallInterface = NetworkModule.GetInstance().getApiCallInterface();
    }

    public Observable<JsonElement> getApiInfoRequest(String apiKey) {
        return apiCallInterface.getApiInfoRequest(apiKey);
    }
}
