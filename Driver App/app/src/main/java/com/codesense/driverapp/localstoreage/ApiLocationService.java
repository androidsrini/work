package com.codesense.driverapp.localstoreage;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.codesense.driverapp.data.CitiesItem;
import com.codesense.driverapp.data.CountriesItem;
import com.codesense.driverapp.data.ILocationAidlInterface;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ApiLocationService extends Service {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final ILocationAidlInterface.Stub mBinder = new ILocationAidlInterface.Stub() {

        @Override
        public List<CountriesItem> getCountryList() {
            return DatabaseClient.getInstance(ApiLocationService.this).getAppDatabase().countryDao().getCountryListByNormalThread();
        }

        @Override
        public List<CitiesItem> getCitiesList() {
            return DatabaseClient.getInstance(ApiLocationService.this).getAppDatabase().cityDao().getCityListByNormalThread();
        }

        public int addNumbers(int num1, int num2) throws RemoteException {
            return num1 + num2;
        }
    };

    /**
     * This method is used for get country object from data base based on user enter value.
     *
     * @param s Editable argument
     */
    private void findCountryFromCountryName(String s) {
        compositeDisposable.add(DatabaseClient.getInstance(this).getAppDatabase().countryDao().findByCountryName(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {

                }, error -> {

                }));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
