package com.codesense.driverapp.localstoreage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.codesense.driverapp.R;

import javax.inject.Inject;

public class AppSharedPreference {

    private static final String ACCESS_TOKEN_KEY = "AccessTokenKey";
    private static final String USER_ID_KEY = "UserIDKey";
    private static final String USER_TYPE = "userType";
    private static final String MOBILE_NUMBER_KEY = "MobileNumberKey";
    private static final String OWNER_TYPE_ID_KEY = "OwnerTypeIdKey";
    private static final String OWNER_TYPE_KEY = "OwnerTypeKey";
    private static final String OTP_VERIFY_KEY = "OtpVerifyKey";
    private static final String USER_TYPE_KEY = "UserTypeKey";
    private static final String PERMISSION_KEY = "PermissionKey";
    private static final String DEFAULT_VALUE = null;
    private SharedPreferences sharedPreferences;

    @Inject
    public AppSharedPreference(Context context) {
        this.sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public void saveAccessToken(String accessToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCESS_TOKEN_KEY, accessToken);
        editor.apply();
    }

    public String getAccessTokenKey() {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, DEFAULT_VALUE);
    }

    public void saveUserID(String userID) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID_KEY, userID);
        editor.apply();
    }

    public void saveUserType(String userType) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_TYPE, userType);
        editor.apply();
    }

    public String getUserID() {
        return sharedPreferences.getString(USER_ID_KEY, DEFAULT_VALUE);
    }

    public String getUserType() {
        return sharedPreferences.getString(USER_TYPE, DEFAULT_VALUE);
    }

    public void saveMobileNumber(String mobileNumber) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MOBILE_NUMBER_KEY, mobileNumber);
        editor.apply();
    }

    public String getMobileNumberKey() {
        return sharedPreferences.getString(MOBILE_NUMBER_KEY, null);
    }

    public void saveOwnerTypeId(int ownerTypeId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(OWNER_TYPE_ID_KEY, ownerTypeId);
        editor.apply();
    }

    public void saveOtpVerify(int otpVerify) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(OTP_VERIFY_KEY, otpVerify);
        editor.apply();
    }

    public int getOtpVerify() {
        return sharedPreferences.getInt(OTP_VERIFY_KEY, 0);
    }

    public int getOwnerTypeId() {
        return sharedPreferences.getInt(OWNER_TYPE_ID_KEY, -1);
    }

    public void saveOwnerType(String ownerType) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OWNER_TYPE_KEY, ownerType);
        editor.apply();
    }

    public String getOwnerType() {
        return sharedPreferences.getString(OWNER_TYPE_KEY, null);
    }

    public void savePermission(String permission, boolean isFirstTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(permission, isFirstTime);
        editor.apply();
    }

    public boolean isUserIdAvailable() {
        return !TextUtils.isEmpty(getUserID());
    }

    public boolean isFirstTime(String permission) {
        return sharedPreferences.getBoolean(permission, true);
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
