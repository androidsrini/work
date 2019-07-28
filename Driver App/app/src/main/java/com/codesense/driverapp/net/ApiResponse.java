package com.codesense.driverapp.net;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codesense.driverapp.net.Status.ERROR;
import static com.codesense.driverapp.net.Status.LOADING;
import static com.codesense.driverapp.net.Status.SUCCESS;

public class ApiResponse {

    /**
     * Api response not getting field
     */
    public static final int NO_RESPONSE = -1;
    /**
     * Api Error response status
     */
    public static final int ERROR_RESPONSE = 0;
    /**
     * Api Success response status
     */
    public static final int SUCCESS_RESPONSE = 1;
    /**
     * Api Unauthorized  or Invalid response status
     */
    public static final int UNAUTHORIZED_INVALID_API_KEY = 401;
    /**
     * Api invalid public key or decrypt failed with private key  response status
     */
    public static final int INVALID_PUBLIC_KEY_OR_DECRYPT_FAILED_WITH_PRIVATE_KEY = 3000;
    /**
     * Api Some fields are missing in body request response status
     */
    public static final int SOME_FIELDS_ARE_MISSING_IN_BODY_REQUEST = 3001;
    /**
     * Api Validation Error response status
     */
    public static final int VALIDATION_ERROR = 3002;
    /**
     * Api Invalid Access Token response status
     */
    public static final int INVALID_ACCESS_TOKEN = 3003;
    /**
     * Api Open OTP Input for OTP Validation response status
     */
    public static final int OTP_VALIDATION = 3004;

    public final Status status;

    @Nullable
    public final JsonElement data;

    @Nullable
    private final Throwable error;
    /**
     * This variable to parse and store JsonElement to JSONObject.
     */
    @Nullable
    private final JSONObject jsonObject;

    private ApiResponse(Status status, @Nullable JsonElement data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
        this.jsonObject = findJSONObject(data);
    }

    /**
     * This method will create JSONObject based on response
     * This method handle JSONException.
     * @param jsonElement response
     * @return JSONObject or null.
     */
    private JSONObject findJSONObject(JsonElement jsonElement) {
        try {
            return (null != jsonElement) ? new JSONObject(jsonElement.toString()) : null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method is used for getting valid response or not
     * @return boolean => true - valid, false => not valid.
     */
    public boolean isValidResponse() {
        return (null != jsonObject) && jsonObject.optInt(Constant.STATUS_PARAM) == SUCCESS_RESPONSE;
    }

    public int getResponseStatus() {
        return (null != jsonObject) ? jsonObject.optInt(Constant.STATUS_PARAM) : NO_RESPONSE;
    }

    @Nullable
    public JSONObject getResponseJsonObject() {
        return jsonObject;
    }

    public static ApiResponse loading() {
        return new ApiResponse(LOADING, null, null);
    }

    public static ApiResponse success(@NonNull JsonElement data) {
        return new ApiResponse(SUCCESS, data, null);
    }

    public static ApiResponse error(@NonNull Throwable error) {
        return new ApiResponse(ERROR, null, error);
    }
}
