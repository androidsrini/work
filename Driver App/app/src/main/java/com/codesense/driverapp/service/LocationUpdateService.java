package com.codesense.driverapp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.codesense.driverapp.di.module.NetworkModule;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.ui.helper.CrashlyticsHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LocationUpdateService extends Service {

    public static final int UPDATE_LOCATION = 0x001;
    private Messenger messenger = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        messenger = new Messenger(new LocationHandler(this));
        return messenger.getBinder();
    }

    static class LocationHandler extends Handler {
        Context appContext;
        private NetworkModule networkModule;
        private CompositeDisposable compositeDisposable = new CompositeDisposable();

        LocationHandler(Context context) {
            appContext = context.getApplicationContext();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UPDATE_LOCATION:
                    //call location api here.
                    if (null == networkModule) {
                        networkModule = new NetworkModule(appContext);
                        CrashlyticsHelper.d("RequestHandler object not null: " + (null != networkModule.provideRequestHandler(networkModule.getApiCallInterface())));
                    }
                    AppSharedPreference appSharedPreference = networkModule.provideAppSharedPreference(appContext);
                    CrashlyticsHelper.d("Gps speed: " + appSharedPreference.getSpeed());
                    String apiKey = ApiUtility.getInstance().getApiKeyMetaData();
                    try {
                        updateVehicleLiveLocationRequest(networkModule.provideRequestHandler(networkModule.getApiCallInterface()), apiKey, appSharedPreference.getUserType(),
                                appSharedPreference.getLastLocationLatitude(), appSharedPreference.getLastLocationLng(),
                                appSharedPreference.getSpeed());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    CrashlyticsHelper.d("Location handler type is Not supported");
                    break;
            }
            super.handleMessage(msg);
        }

        public void updateVehicleLiveLocationRequest(RequestHandler requestHandler, String apikey,
                                                     String userType, String latitude, String longitude,
                                                     float speed) {
            compositeDisposable.add(requestHandler.updateVehicleLiveLocationRequest(apikey, userType,
                    latitude, longitude, speed)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe());
        }
    }

}
