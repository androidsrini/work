package com.codesense.driverapp.ui.driver;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DriverListViewModel extends ViewModel {

    private RequestHandler requestHandler;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> apiResponseMutableLiveData = new MutableLiveData<>();

    @Inject
    public DriverListViewModel(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public MutableLiveData<ApiResponse> getApiResponseMutableLiveData() {
        return apiResponseMutableLiveData;
    }

    public void getDriversListRequest(){
        if(null != requestHandler) {
            disposables.add(requestHandler.getDriversListRequest(ApiUtility.getInstance().getApiKeyMetaData()).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.GET_DRIVERS_LIST))).
                    subscribe(result -> apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.GET_DRIVERS_LIST, result)),
                            error -> {apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.GET_DRIVERS_LIST, error));}));
        }
    }

    public void postDrivingActivationRequest(int driverStatus, String vehicleId, String driverId,String driverType){
        if(null != requestHandler) {
            disposables.add(requestHandler.postDrivingActivationRequest(ApiUtility.getInstance().getApiKeyMetaData(), driverStatus, vehicleId, driverId,driverType).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.DRIVING_ACTIVATION))).
                    subscribe(result -> apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.DRIVING_ACTIVATION, result)),
                            error -> {apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.DRIVING_ACTIVATION, error));}));
        }
    }
}
