package com.codesense.driverapp.net;

import android.text.TextUtils;

import com.codesense.driverapp.data.AddDriverRequest;
import com.codesense.driverapp.data.AddVehicleRequest;
import com.codesense.driverapp.data.DocumentsItem;
import com.codesense.driverapp.data.DocumentsListItem;
import com.codesense.driverapp.data.VehicleDetailRequest;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.request.RegisterNewUser;
import com.google.gson.JsonElement;

import java.io.File;
import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
        param.put(Constant.USER_TYPE_REQUEST, appSharedPreference.getUserType());
        return param;
    }

    private HashMap<String, String> getVerifyOTPRequestParam(String userID, String otp) {
        HashMap<String, String> param = new HashMap<>();
        param.put(Constant.USER_ID_PARAM, userID);
        param.put(Constant.OTP_PARAM, otp);
        param.put(Constant.USER_TYPE_REQUEST, appSharedPreference.getUserType());
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
        param.put(Constant.USER_TYPE_REQUEST, appSharedPreference.getUserType());
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

    private HashMap<String, String> getChangeMobileNumberRequestParam(String userID, String phoneNumber) {
        HashMap<String, String> param = new HashMap<>();
        param.put(Constant.USER_ID_PARAM, userID);
        param.put(Constant.MOBILE_NUMBER_PARAM, phoneNumber);
        param.put(Constant.USER_TYPE_REQUEST, appSharedPreference.getUserType());
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

    private HashMap<String, String> getAddDriverParam(AddDriverRequest addDriverRequest) {
        HashMap<String, String> param = new HashMap<>();
        param.put(Constant.USER_ID_PARAM, appSharedPreference.getUserID());
        param.put(Constant.COUNTRY_ID_PARAM, addDriverRequest.getCountryId());
        param.put(Constant.EMAIL_ID_PARAM, addDriverRequest.getEmailId());
        param.put(Constant.PASSWORD_PARAM, addDriverRequest.getPassword());
        param.put(Constant.MOBILE_NUMBER_PARAM, addDriverRequest.getMobileNumber());
        param.put(Constant.DRIVER_FIRST_NAME_PARAM, addDriverRequest.getDriverFirstName());
        param.put(Constant.DRIVER_LAST_NAME_PARAM, addDriverRequest.getDriverLastName());
        return param;
    }

    private HashMap<String, String> getAddVehicleParam(AddVehicleRequest addVehicleRequest) {
        HashMap<String, String> param = new HashMap<>();
        param.put(Constant.USER_ID_PARAM, appSharedPreference.getUserID());
        param.put(Constant.VEHICLE_NUMBER_PARAM, addVehicleRequest.getVehicleNumber());
        param.put(Constant.VEHICLE_NAME_PARAM, addVehicleRequest.getVehicleName());
        param.put(Constant.VEHICLE_TYPE_ID_PARAM, addVehicleRequest.getVehicleTypeId());
        param.put(Constant.AVAILABLE_DRIVER_ID, addVehicleRequest.getAvailableDriverId());
        param.put(Constant.USER_TYPE_REQUEST, appSharedPreference.getUserType());
        param.put(Constant.COUNTRY_ID_PARAM, addVehicleRequest.getCountryId());
        param.put(Constant.EMAIL_ID_PARAM, addVehicleRequest.getEmailId());
        param.put(Constant.PASSWORD_PARAM, addVehicleRequest.getPassword());
        param.put(Constant.MOBILE_NUMBER_PARAM, addVehicleRequest.getMobileNumber());
        param.put(Constant.DRIVER_FIRST_NAME_PARAM, addVehicleRequest.getDriverFirstName());
        param.put(Constant.DRIVER_LAST_NAME_PARAM, addVehicleRequest.getDriverLastName());
        return param;
    }

    private HashMap<String, String> getOnlineStatusParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put(Constant.USER_ID_PARAM, appSharedPreference.getUserID());
        param.put(Constant.LIVE_STATUS, appSharedPreference.isUserStatusOnline() ? Constant.ONLINE_CODE
                : Constant.OFFLINE_CODE);
        param.put(Constant.LATITUDE, appSharedPreference.getLastLocationLatitude());
        param.put(Constant.LONGITUDE, appSharedPreference.getLastLocationLng());
        param.put(Constant.USER_TYPE_REQUEST, appSharedPreference.getUserType());
        return param;
    }

    private HashMap<String, String> getUpdateVehicleLocationParam(String userType, String latitude,
                                                                  String longitude, float speed) {
        HashMap<String, String> param = new HashMap<>();
        param.put(Constant.USER_ID_PARAM, appSharedPreference.getUserID());
        param.put(Constant.USER_TYPE_REQUEST, userType);
        param.put(Constant.LATITUDE, latitude);
        param.put(Constant.LONGITUDE, longitude);
        param.put(Constant.SPEED_PER_MINUTE, String.valueOf(speed));
        return param;
    }

    private MultipartBody.Part getUploadDocumentFileRequest(DocumentsItem documentsListItem) {
        File file = new File(documentsListItem.getFilePath());
        RequestBody requestFile = RequestBody.create(MediaType.parse(Constant.MULTIPART_FORM_DATA), file);
        return MultipartBody.Part.createFormData(documentsListItem.getName(), file.getName(), requestFile);
    }

    private MultipartBody.Part getUploadDocumentFileRequest(DocumentsListItem documentsListItem) {
        File file = new File(documentsListItem.getFilePath());
        RequestBody requestFile = RequestBody.create(MediaType.parse(Constant.MULTIPART_FORM_DATA), file);
        return MultipartBody.Part.createFormData(documentsListItem.getName(), file.getName(), requestFile);
    }

    private RequestBody getUploadDocumentFileName(DocumentsListItem documentsListItem) {
        return RequestBody.create(MediaType.parse(Constant.TEXT_PLAIN), documentsListItem.getName());
    }

    private RequestBody getUploadDocumentUserID() {
        return RequestBody.create(MediaType.parse(Constant.TEXT_PLAIN), appSharedPreference.getUserID());
    }

    private RequestBody getVehicleTypeId(VehicleDetailRequest vehicleDetailRequest) {
        return RequestBody.create(MediaType.parse(Constant.TEXT_PLAIN), vehicleDetailRequest.getVehicleTypeId());
    }

    private RequestBody getVehicleId(VehicleDetailRequest vehicleDetailRequest) {
        if (!TextUtils.isEmpty(vehicleDetailRequest.getVehicleId())) {
            return RequestBody.create(MediaType.parse(Constant.TEXT_PLAIN), vehicleDetailRequest.getVehicleId());
        } else {
            return null;
        }
    }

    private RequestBody getVehicleNumber(VehicleDetailRequest vehicleDetailRequest) {
        return RequestBody.create(MediaType.parse(Constant.TEXT_PLAIN), vehicleDetailRequest.getVehicleNumber());
    }

    private RequestBody getVehicleName(VehicleDetailRequest vehicleDetailRequest) {
        return RequestBody.create(MediaType.parse(Constant.TEXT_PLAIN), vehicleDetailRequest.getVehicleName());
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
        return apiCallInterface.updateAgreementAcceptRequest(apikey, appSharedPreference.getAccessTokenKey(), getAgreementAcceptRequestParam(agreementAccept));
    }

    public Observable<JsonElement> updateRegistationOwnerTypeRequest(String apikey, String ownerTypeId) {
        return apiCallInterface.updateRegistationOwnerTypeRequest(apikey, appSharedPreference.getAccessTokenKey(), getOwnerTypeRequestParam(ownerTypeId));
    }

    public Observable<JsonElement> fetchVehicleTypesRequest(String apikey) {
        return apiCallInterface.fetchVehicleTypesRequest(apikey, appSharedPreference.getAccessTokenKey());
    }

    public Observable<JsonElement> fetchOwnerCumDriverStatusRequest(String apikey) {
        return apiCallInterface.fetchOwnerCumDriverStatusRequest(apikey, appSharedPreference.getAccessTokenKey(), getUserIDRequestParam());
    }

    public Observable<JsonElement> fetchNonDrivingPartnerStatusRequest(String apikey) {
        return apiCallInterface.fetchNonDrivingPartnerStatusRequest(apikey, appSharedPreference.getAccessTokenKey(), getUserIDRequestParam());
    }

    public Observable<JsonElement> fetchAvailableVehiclesRequest(String apikey) {
        return apiCallInterface.fetchAvailableVehiclesRequest(apikey, appSharedPreference.getAccessTokenKey(), getUserIDRequestParam());
    }

    public Observable<JsonElement> fetchDriverDocumentListRequest(String apikey) {
        return apiCallInterface.fetchDriverDocumentListRequest(apikey, appSharedPreference.getAccessTokenKey(), getUserIDRequestParam());
    }

    public Observable<JsonElement> fetchVehicleListRequest(String apikey) {
        return apiCallInterface.fetchVehicleListRequest(apikey, appSharedPreference.getAccessTokenKey(), getUserIDRequestParam());
    }

    public Observable<JsonElement> uploadDocumentsRequest(String api, DocumentsItem documentsListItem, VehicleDetailRequest vehicleDetailRequest) {
        return apiCallInterface.uploadDocumentsRequest(api, appSharedPreference.getAccessTokenKey(),
                getUploadDocumentFileRequest(documentsListItem), getUploadDocumentUserID(),
                getVehicleTypeId(vehicleDetailRequest),
                getVehicleId(vehicleDetailRequest),
                getVehicleNumber(vehicleDetailRequest),
                getVehicleName(vehicleDetailRequest));
    }

    public Observable<JsonElement> uploadDocumentsRequest(String api, DocumentsListItem documentsListItem, VehicleDetailRequest vehicleDetailRequest) {
        return apiCallInterface.uploadDocumentsRequest(api, appSharedPreference.getAccessTokenKey(),
                getUploadDocumentFileRequest(documentsListItem), getUploadDocumentUserID(),
                getVehicleTypeId(vehicleDetailRequest),
                getVehicleId(vehicleDetailRequest),
                getVehicleNumber(vehicleDetailRequest),
                getVehicleName(vehicleDetailRequest));
    }

    public Observable<JsonElement> uploadDocumentsWithoutVehicleRequest(String api, DocumentsItem documentsListItem) {
        return apiCallInterface.uploadDocumentsWithoutVehicleRequest(api, appSharedPreference.getAccessTokenKey(),
                getUploadDocumentFileRequest(documentsListItem), getUploadDocumentUserID());
    }

    public Observable<JsonElement> fetchOwnerSignupStatusRequest(String api) {
        return apiCallInterface.fetchOwnerSignupStatusRequest(api, appSharedPreference.getAccessTokenKey(),
                getUserIDRequestParam());
    }

    public Observable<JsonElement> updateMobileNumber(String apiKey, String userID, String phoneNumber) {
        return apiCallInterface.updateMobileNumber(apiKey, appSharedPreference.getAccessTokenKey(), getChangeMobileNumberRequestParam(userID, phoneNumber));
    }

    public Observable<JsonElement> fetchAvailableDrivers(String apiKey) {
        return apiCallInterface.fetchAvailableDrivers(apiKey, appSharedPreference.getAccessTokenKey(), getUserIDRequestParam());
    }

    public Observable<JsonElement> addDriverToOwnerRequest(String apiKey, AddDriverRequest addDriverRequest) {
        return apiCallInterface.addDriverToOwnerRequest(apiKey, appSharedPreference.getAccessTokenKey(), getAddDriverParam(addDriverRequest));
    }

    public Observable<JsonElement> addVehicleToOwnerRequest(String apiKey, AddVehicleRequest addVehicleRequest) {
        return apiCallInterface.addVehicleToOwnerRequest(apiKey, appSharedPreference.getAccessTokenKey(), getAddVehicleParam(addVehicleRequest));
    }

    public Observable<JsonElement> setVehicleLiveStatusRequest(String apiKey) {
        return apiCallInterface.setVehicleLiveStatusRequest(apiKey, appSharedPreference.getAccessTokenKey(),
                getOnlineStatusParam());
    }

    public Observable<JsonElement> updateVehicleLiveLocationRequest(String apiKey, String userType,
                                                                    String latitude, String longitude,
                                                                    float speed) {
        return apiCallInterface.updateVehicleLiveLocationRequest(apiKey, appSharedPreference.getAccessTokenKey(),
                getUpdateVehicleLocationParam(userType, latitude, longitude, speed));
    }

    public Observable<JsonElement> fetchHomeDetailRequest(String apiKey) {
        return apiCallInterface.fetchHomeDetailRequest(apiKey);
    }

    public Observable<JsonElement> fetchDocumentStatusRequest(String apiKey) {
        return apiCallInterface.fetchDocumentStatusRequest(apiKey, appSharedPreference.getAccessTokenKey(), getUserIDRequestParam());
    }

    public Observable<JsonElement> fetchVehiclesListRequest(String apiKey) {
        return apiCallInterface.fetchVehiclesListRequest(apiKey, appSharedPreference.getAccessTokenKey(), getUserIDRequestParam());
    }

    public Observable<JsonElement> fetchDocumentStatusVehicleRequest(String apiKey) {
        return apiCallInterface.fetchDocumentStatusVehicleRequest(apiKey, appSharedPreference.getAccessTokenKey(), getUserIDRequestParam());
    }

    public Observable<JsonElement> uploadOwnerVehicleRequest(String api, DocumentsItem documentsListItem, VehicleDetailRequest vehicleDetailRequest) {
        return apiCallInterface.uploadOwnerVehicleRequest(api, appSharedPreference.getAccessTokenKey(),
                getUploadDocumentFileRequest(documentsListItem), getUploadDocumentUserID(),
                getVehicleTypeId(vehicleDetailRequest),
                getVehicleId(vehicleDetailRequest),
                getVehicleNumber(vehicleDetailRequest),
                getVehicleName(vehicleDetailRequest));
    }

    public Observable<JsonElement> fetchDocumentStatusDriverRequest(String apiKey) {
        return apiCallInterface.fetchDocumentStatusDriverRequest(apiKey, appSharedPreference.getAccessTokenKey(), getUserIDRequestParam());
    }
}
