package com.codesense.driverapp.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.codesense.driverapp.di.module.NetworkModule;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.ui.helper.CrashlyticsHelper;

import javax.inject.Inject;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Inject
    protected AppSharedPreference appSharedPreference;
    @Inject
    protected RequestHandler requestHandler;

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isAvailable());
    }

    private void setNetworkStatus(String status) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkModule networkModule = new NetworkModule(context);
        appSharedPreference = networkModule.provideAppSharedPreference(context);
        requestHandler = networkModule.provideRequestHandler(networkModule.getApiCallInterface());
        String internetStatus = isOnline(context) ? Constant.ONLINE_STATUS : Constant.OFFLINE_STATUS;
        CrashlyticsHelper.d("Inter net status:"+ internetStatus);
        CrashlyticsHelper.d("Request handler object:"+ requestHandler);
        if (null != appSharedPreference)
            appSharedPreference.setNetworkStatus(internetStatus);
    }
}
