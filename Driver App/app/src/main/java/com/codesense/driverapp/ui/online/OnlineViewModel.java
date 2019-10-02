package com.codesense.driverapp.ui.online;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class OnlineViewModel extends ViewModel {

    private RequestHandler requestHandler;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> apiResponseMutableLiveData = new MutableLiveData<>();

    @Inject
    public OnlineViewModel(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void updateLocationRequest(String apiKey) {
        disposables.add(requestHandler.setVehicleLiveStatusRequest(apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d->apiResponseMutableLiveData.setValue(ApiResponse.loading()))
                .subscribe(result->apiResponseMutableLiveData.setValue(ApiResponse.success(result)),
                        error->apiResponseMutableLiveData.setValue(ApiResponse.error(error))));
    }

    public void updateVehicleLiveLocationRequest(String apikey, String userType, String latitude,
                                                 String longitude, float speed) {
        disposables.add(requestHandler.updateVehicleLiveLocationRequest(apikey, userType, latitude,
                longitude, speed)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d->apiResponseMutableLiveData.postValue(ApiResponse.loading()))
                .subscribe(result->apiResponseMutableLiveData.postValue(ApiResponse.success(result)),
                        error->apiResponseMutableLiveData.postValue(ApiResponse.error(error))));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
