package com.codesense.driverapp.ui.online;

import android.support.annotation.NonNull;

import com.codesense.driverapp.di.module.NetworkModule;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.ui.helper.CrashlyticsHelper;

import androidx.work.Worker;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LocationWorker extends Worker {
    /**
     * Non ui context access.
     */
    private static NetworkModule networkModule;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    /*LocationWorker() {
        NetworkModule networkModule = new NetworkModule(getApplicationContext());
        onlineViewModel = new OnlineViewModel(networkModule.provideRequestHandler(networkModule.getApiCallInterface()));
        appSharedPreference = networkModule.provideAppSharedPreference(getApplicationContext());
    }*/

    public void updateVehicleLiveLocationRequest(RequestHandler requestHandler, String apikey,
                                                 String userType, String latitude, String longitude,
                                                 float speed) {
        compositeDisposable.add(requestHandler.updateVehicleLiveLocationRequest(apikey, userType,
                latitude, longitude, speed)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //.doOnSubscribe(d->apiResponseMutableLiveData.postValue(ApiResponse.loading()))
                .subscribe());
    }

    @NonNull
    @Override
    public Worker.Result doWork() {
        CrashlyticsHelper.d("LocationWorker do work: OK");
        if (null == networkModule) {
            networkModule = new NetworkModule(getApplicationContext());
            CrashlyticsHelper.d("RequestHandler object not null: " + (null != networkModule.provideRequestHandler(networkModule.getApiCallInterface())));
        }
        //OnlineViewModel onlineViewModel = new OnlineViewModel(networkModule.provideRequestHandler(networkModule.getApiCallInterface()));
        AppSharedPreference appSharedPreference = networkModule.provideAppSharedPreference(getApplicationContext());
        CrashlyticsHelper.d("Gps speed: " + appSharedPreference.getSpeed());
        String apiKey = ApiUtility.getInstance().getApiKeyMetaData();
        try {
            updateVehicleLiveLocationRequest(networkModule.provideRequestHandler(networkModule.getApiCallInterface()), apiKey, appSharedPreference.getUserType(),
                    appSharedPreference.getLastLocationLatitude(), appSharedPreference.getLastLocationLng(),
                    appSharedPreference.getSpeed());
            return Worker.Result.SUCCESS;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return Result.FAILURE;
        }
    }

    @Override
    public void onStopped(boolean cancelled) {
        super.onStopped(cancelled);
        compositeDisposable.clear();
    }
}