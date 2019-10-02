package com.codesense.driverapp.ui.online;

import android.support.annotation.NonNull;

import com.codesense.driverapp.di.module.NetworkModule;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.ui.helper.CrashlyticsHelper;

import androidx.work.Worker;

public class LocationWorker extends Worker {
    /**
     * Non ui context access.
     */
    private static NetworkModule networkModule;

    /*LocationWorker() {
        NetworkModule networkModule = new NetworkModule(getApplicationContext());
        onlineViewModel = new OnlineViewModel(networkModule.provideRequestHandler(networkModule.getApiCallInterface()));
        appSharedPreference = networkModule.provideAppSharedPreference(getApplicationContext());
    }*/

    @NonNull
    @Override
    public Worker.Result doWork() {
        CrashlyticsHelper.d("LocationWorker do work: OK");
        if (null == networkModule) {
            networkModule = new NetworkModule(getApplicationContext());
            CrashlyticsHelper.d("RequestHandler object not null: " + (null != networkModule.provideRequestHandler(networkModule.getApiCallInterface())));
        }
        OnlineViewModel onlineViewModel = new OnlineViewModel(networkModule.provideRequestHandler(networkModule.getApiCallInterface()));
        AppSharedPreference appSharedPreference = networkModule.provideAppSharedPreference(getApplicationContext());
        String apiKey = ApiUtility.getInstance().getApiKeyMetaData();
        try {
            onlineViewModel.updateVehicleLiveLocationRequest(apiKey, appSharedPreference.getUserType(),
                    appSharedPreference.getLastLocationLatitude(), appSharedPreference.getLastLocationLng());
            return Worker.Result.SUCCESS;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return Result.FAILURE;
        }
    }
}
