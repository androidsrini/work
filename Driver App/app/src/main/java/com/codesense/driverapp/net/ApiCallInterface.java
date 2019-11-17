package com.codesense.driverapp.net;

import com.google.gson.JsonElement;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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

    @POST(WebserviceUrls.SEND_OTP)
    @FormUrlEncoded
    Observable<JsonElement> sentOTPRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey, @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken,
                                           @FieldMap HashMap<String, String> params);

    @POST(WebserviceUrls.VERIFY_OTP)
    @FormUrlEncoded
    Observable<JsonElement> verifyOTPRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey, @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken,
                                             @FieldMap HashMap<String, String> params);

    /**
     * This method to sign in the owner
     * @param apiKey
     * @param params (email id, password)
     * @return JsonObject
     */
    @POST(WebserviceUrls.SIGNIN_OWNER)
    @FormUrlEncoded
    Observable<JsonElement> signInRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey, @FieldMap HashMap<String, String> params);

    /**
     * This method to fetch owner type from server.
     * @param apiKey
     * @return JsonObject
     */
    @GET(WebserviceUrls.OWNER_TYPES)
    Observable<JsonElement> fetchOwnerTypeRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey,
                                                  @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken);

    /**
     * This method to fetch OwnerAgreement from server.
     * @param apiKey
     * @param accessToken
     * @param param
     * @return JsonObject
     */
    @POST(WebserviceUrls.GET_OWNER_AGREEMENT)
    @FormUrlEncoded
    Observable<JsonElement> getOwnerAgreementRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey,
                                                  @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken,
                                                     @FieldMap HashMap<String, String> param);

    @POST(WebserviceUrls.ACCEPT_LEGAL_AGREEMENT)
    @FormUrlEncoded
    Observable<JsonElement> updateAgreementAcceptRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey,
                                                         @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken,
                                                         @FieldMap HashMap<String, String> param);

    @POST(WebserviceUrls.REGISTATION_OWNER_TYPE)
    @FormUrlEncoded
    Observable<JsonElement> updateRegistationOwnerTypeRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey,
                                                  @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken,
                                                     @FieldMap HashMap<String, String> param);

    /**
     * This method to fetch vehicle types from server.
     * @param apiKey
     * @param accessToken
     * @return
     */
    @GET(WebserviceUrls.VEHICLE_TYPES)
    Observable<JsonElement> fetchVehicleTypesRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey,
                                                  @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken);

    /**
     * This method to fetch Documents List For Owner Cum Driver.
     * @param apiKey
     * @param accessToken
     * @param param
     * @return
     */
    @POST(WebserviceUrls.OWNER_CUM_DRIVER_STATUS)
    @FormUrlEncoded
    Observable<JsonElement> fetchOwnerCumDriverStatusRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey,
                                                  @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken, @FieldMap HashMap<String,String> param);

    /**
     * This method to fetch Documents List For Non Driver
     * @param apiKey
     * @param accessToken
     * @param param
     * @return
     */
    @POST(WebserviceUrls.NON_DRIVING_PARTNER_STATUS)
    @FormUrlEncoded
    Observable<JsonElement> fetchNonDrivingPartnerStatusRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey,
                                                  @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken, @FieldMap HashMap<String,String> param);


    /**
     * This method to update file to server.
     * @param apiKey
     * @param accessToken
     * @return JsonElement
     * @link reference: https://stackoverflow.com/questions/34562950/post-multipart-form-data-using-retrofit-2-0-including-image
     */
    @Multipart
    @POST(WebserviceUrls.UPLOAD_DOCUMENTS)
    Observable<JsonElement> uploadDocumentsRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey,
                                                   @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken,
                                                   @Part MultipartBody.Part fileBody,
                                                   @Part(Constant.USER_ID_PARAM) RequestBody userId,
                                                   @Part(Constant.VEHICLE_TYPE_ID_PARAM) RequestBody vehicleTypeId,
                                                   @Part(Constant.VEHICLE_ID) RequestBody vehicleId,
                                                   @Part(Constant.VEHICLE_NUMBER_PARAM) RequestBody vehicleNumber,
                                                   @Part(Constant.VEHICLE_NAME_PARAM) RequestBody vehicleName);

    @Multipart
    @POST(WebserviceUrls.UPLOAD_DOCUMENTS)
    Observable<JsonElement> uploadDocumentsWithoutVehicleRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey,
                                                   @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken,
                                                   @Part MultipartBody.Part fileBody,
                                                   @Part(Constant.USER_ID_PARAM) RequestBody userId);


    @POST(WebserviceUrls.ADD_VEHICLE_DOCUMENTS)
    @FormUrlEncoded
    Observable<JsonElement> addVehicleDocumentsRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey,
                                                  @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken, @FieldMap HashMap<String,String> param);

    @POST(WebserviceUrls.GET_AVAILABLE_VEHICLES)
    @FormUrlEncoded
    Observable<JsonElement> fetchAvailableVehiclesRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey,
                                                  @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken, @FieldMap HashMap<String,String> param);


    @POST(WebserviceUrls.ADD_VEHICLE_TO_OWNER)
    @FormUrlEncoded
    Observable<JsonElement> addVehicleToOwnerRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey,
                                                  @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken, @FieldMap HashMap<String,String> param);


    @POST(WebserviceUrls.DRIVER)
    @FormUrlEncoded
    Observable<JsonElement> fetchDriverDocumentListRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey,
                                                  @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken, @FieldMap HashMap<String,String> param);


    @POST(WebserviceUrls.VEHICLE)
    @FormUrlEncoded
    Observable<JsonElement> fetchVehicleListRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey,
                                                  @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken, @FieldMap HashMap<String,String> param);


    @POST(WebserviceUrls.GET_OWNER_SIGNUP_STATUS)
    @FormUrlEncoded
    Observable<JsonElement> fetchOwnerSignupStatusRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey,
                                                  @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken,
                                                          @FieldMap HashMap<String,String> param);


    @POST(WebserviceUrls.CHANGE_MOBILE_NUMBER)
    @FormUrlEncoded
    Observable<JsonElement> updateMobileNumber(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey, @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken,
                                               @FieldMap HashMap<String, String> params);

    @POST(WebserviceUrls.GET_AVAILABLE_DRIVERS)
    @FormUrlEncoded
    Observable<JsonElement> fetchAvailableDrivers(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey, @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken,
                                               @FieldMap HashMap<String, String> params);

    @POST(WebserviceUrls.ADD_DRIVER_TO_OWNER)
    @FormUrlEncoded
    Observable<JsonElement> addDriverToOwnerRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey, @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken,
                                                    @FieldMap HashMap<String, String> param);

    @POST(WebserviceUrls.SET_VEHICLE_LIVE_STATUS)
    @FormUrlEncoded
    Observable<JsonElement> setVehicleLiveStatusRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey, @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken,
                                                    @FieldMap HashMap<String, String> param);

    @POST(WebserviceUrls.UPDATE_VEHICLE_LIVE_LOCATION)
    @FormUrlEncoded
    Observable<JsonElement> updateVehicleLiveLocationRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey, @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken,
                                                    @FieldMap HashMap<String, String> param);

    @GET(WebserviceUrls.HOME_SCREEN)
    Observable<JsonElement> fetchHomeDetailRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey);


    @POST(WebserviceUrls.GET_DOCUMENTS_STATUS)
    @FormUrlEncoded
    Observable<JsonElement> fetchDocumentStatusRequest(@Header(Constant.API_AUTH_KEY_PARAM) String apiKey, @Header(Constant.ACCESS_TOKEN_PARAM) String accessToken, @FieldMap HashMap<String, String> param);

}
