package com.codesense.driverapp.ui.online;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;

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
                .doOnSubscribe(d->apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.VEHICLE_LIVE_STATUS)))
                .subscribe(result->apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.VEHICLE_LIVE_STATUS, result)),
                        error->apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.VEHICLE_LIVE_STATUS, error))));
    }

    public void updateVehicleLiveLocationRequest(String apikey, String userType, String latitude,
                                                 String longitude, float speed) {
        disposables.add(requestHandler.updateVehicleLiveLocationRequest(apikey, userType, latitude,
                longitude, speed)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d->apiResponseMutableLiveData.postValue(ApiResponse.loading(ServiceType.UPDATE_VEHICLE_LIVE_LOCATION)))
                .subscribe(result->apiResponseMutableLiveData.postValue(ApiResponse.success(ServiceType.UPDATE_VEHICLE_LIVE_LOCATION, result)),
                        error->apiResponseMutableLiveData.postValue(ApiResponse.error(ServiceType.UPDATE_VEHICLE_LIVE_LOCATION, error))));
    }

    public MutableLiveData<ApiResponse> getApiResponseMutableLiveData() {
        return apiResponseMutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
