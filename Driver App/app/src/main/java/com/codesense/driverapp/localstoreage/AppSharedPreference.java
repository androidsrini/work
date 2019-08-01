package com.codesense.driverapp.localstoreage;

import android.content.Context;
import android.content.SharedPreferences;
import com.codesense.driverapp.R;

import javax.inject.Inject;

public class AppSharedPreference {

    private static final String ACCESS_TOKEN_KEY = "AccessTokenKey";
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
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null);
    }
}
