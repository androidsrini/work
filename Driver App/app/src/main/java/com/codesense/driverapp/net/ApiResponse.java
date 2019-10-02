package com.codesense.driverapp.net;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codesense.driverapp.net.Status.ERROR;
import static com.codesense.driverapp.net.Status.LOADING;
import static com.codesense.driverapp.net.Status.SUCCESS;
import static com.codesense.driverapp.net.Status.SUCCESS_MULTIPLE;

public class ApiResponse {

    /**
     * To use default method without index based argument.
     */
    private static final int DEFAULT_INDEX_POSITION = 0;
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
    public final Throwable error;
    /**
     * This variable to parse and store JsonElement to JSONObject.
     */
    @Nullable
    private final JSONObject[] jsonObject;

    /**
     * To store multi response json element.
     */
    @NonNull
    public final JsonElement[] datas;

    private ApiResponse(Status status, @Nullable JsonElement data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
        this.jsonObject = findJSONObject(data);
        datas = new JsonElement[]{data};
    }

    private ApiResponse(Status status, @Nullable JsonElement... data) {
        this.status = status;
        this.datas = data;
        this.error = null;
        this.jsonObject = findJSONObject(data);
        this.data = data.length > 0 ? data[0] : null;
    }

    /**
     * This method will create JSONObject based on response
     * This method handle JSONException.
     * @param jsonElement response
     * @return JSONObject or null.
     */
    private JSONObject[] findJSONObject(JsonElement... jsonElement) {
        if (null != jsonElement) {
            JSONObject[] object = new JSONObject[jsonElement.length];
            int count = 0;
            do {
                try {
                    object[count] = null != jsonElement[count]
                            ? new JSONObject(jsonElement[count].toString()) : null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (++ count < jsonElement.length);
            return object;
        }
        return null;
    }

    /**
     * This method to find getting valied response or not based on index
     * This method support multiple json response.
     * @param position
     * @return
     */
    public boolean isValidResponse(int position) {
        return (null != jsonObject) && jsonObject[position].optInt(Constant.STATUS_PARAM) == SUCCESS_RESPONSE;
    }

    /**
     * This method is used for getting valid response or not
     * @return boolean => true - valid, false => not valid.
     */
    public boolean isValidResponse() {
        return isValidResponse(DEFAULT_INDEX_POSITION);
    }

    /**
     * This method to find response status based on position
     * @param position
     * @return
     */
    public int getResponseStatus(int position) {
        return (null != jsonObject) ? jsonObject[position].optInt(Constant.STATUS_PARAM) : NO_RESPONSE;
    }

    public int getResponseStatus() {
        return getResponseStatus(DEFAULT_INDEX_POSITION);
    }

    /**
     * This method to return api response message based on position
     * @param position
     * @return
     */
    public String getResponseMessage(int position) {
        return (null != jsonObject) ? jsonObject[position].optString(Constant.MESSAGE_RESPONSE) : null;
    }

    public String getResponseMessage() {
        return getResponseMessage(DEFAULT_INDEX_POSITION);
    }

    /**
     * This method to return api response jsonObject based on position
     * @param position
     * @return
     */
    @Nullable
    public JSONObject getResponseJsonObject(int position) {
        return null != jsonObject ? jsonObject[position] : null;
    }

    @Nullable
    public JSONObject getResponseJsonObject() {
        return getResponseJsonObject(DEFAULT_INDEX_POSITION);
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

    public static ApiResponse successMultiple(@NonNull JsonElement... jsonElement) {
        return new ApiResponse(SUCCESS_MULTIPLE, jsonElement);
    }
}
