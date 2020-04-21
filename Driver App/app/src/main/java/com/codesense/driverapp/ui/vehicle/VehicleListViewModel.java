package com.codesense.driverapp.ui.vehicle;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VehicleListViewModel extends ViewModel {

    private RequestHandler requestHandler;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> apiResponseMutableLiveData = new MutableLiveData<>();

    @Inject
    public VehicleListViewModel(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public MutableLiveData<ApiResponse> getApiResponseMutableLiveData() {
        return apiResponseMutableLiveData;
    }

    public void fetchOwnerVehiclesRequest(){
        if(null != requestHandler) {
            disposables.add(requestHandler.getOwnerVehiclesRequest(ApiUtility.getInstance().getApiKeyMetaData()).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.GET_OWNER_VEHICLES))).
                    subscribe(result -> apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.GET_OWNER_VEHICLES, result)),
                            error -> {apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.GET_OWNER_VEHICLES, error));}));
        }
    }
}
