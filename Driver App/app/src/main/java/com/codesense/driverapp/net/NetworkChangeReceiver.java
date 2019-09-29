package com.codesense.driverapp.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.codesense.driverapp.di.module.NetworkModule;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.ui.helper.CrashlyticsHelper;

import javax.inject.Inject;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Inject
    protected AppSharedPreference appSharedPreference;
    @Inject
    protected RequestHandler requestHandler;
    @Inject
    protected Utility utility;

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkModule networkModule = new NetworkModule(context);
        appSharedPreference = networkModule.provideAppSharedPreference(context);
        requestHandler = networkModule.provideRequestHandler(networkModule.getApiCallInterface());
        utility = networkModule.provideUtility();
        String internetStatus = utility.isOnline(context) ? Constant.ONLINE_STATUS : Constant.OFFLINE_STATUS;
        CrashlyticsHelper.d("Inter net status:"+ internetStatus);
        CrashlyticsHelper.d("Request handler object:"+ requestHandler);
        if (null != appSharedPreference)
            appSharedPreference.setNetworkStatus(internetStatus);
    }
}
