package com.codesense.driverapp.net;

import com.codesense.driverapp.di.utils.ApiUtility;

public final class Constant {
    public static final String API_AUTH_KEY_PARAM = "api_auth_key";
    public static final String API_AUTH_KEY_WITH_VALUE = API_AUTH_KEY_PARAM + ":" + ApiUtility.getInstance().getApiKeyMetaData();
    public static final String DRIVER_APP_API_KEY = "com.codesense.driverapp.API_KEY" ;
}
