package com.codesense.driverapp.ui.online;

import android.support.annotation.NonNull;

import com.codesense.driverapp.di.module.NetworkModule;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;

import androidx.work.Worker;

public class LocationWorker extends Worker {

    private OnlineViewModel onlineViewModel;
    private AppSharedPreference appSharedPreference;

    LocationWorker() {
        NetworkModule networkModule = new NetworkModule(getApplicationContext());
        onlineViewModel = new OnlineViewModel(networkModule.provideRequestHandler(networkModule.getApiCallInterface()));
        appSharedPreference = networkModule.provideAppSharedPreference(getApplicationContext());
    }

    @NonNull
    @Override
    public Worker.Result doWork() {
        //sending work status to caller
        String apiKey = ApiUtility.getInstance().getApiKeyMetaData();
        onlineViewModel.updateVehicleLiveLocationRequest(apiKey, appSharedPreference.getUserType(),
                appSharedPreference.getLastLocationLatitude(), appSharedPreference.getLastLocationLng());
        return Worker.Result.SUCCESS;
    }
}
