package com.codesense.driverapp.ui.adddriver;

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

public class DriverViewModel extends ViewModel {

    private RequestHandler requestHandler;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> apiResponseMutableLiveData = new MutableLiveData<>();

    @Inject
    public DriverViewModel(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public MutableLiveData<ApiResponse> getApiResponseMutableLiveData() {
        return apiResponseMutableLiveData;
    }

    public void fetchVehiclesListRequest() {
        disposables.add(requestHandler.fetchVehiclesListRequest(ApiUtility.getInstance().getApiKeyMetaData())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(d->apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.OWNER_VEHICLES)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result->apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.OWNER_TYPES, result)),
                        error->apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.OWNER_TYPES, error))));
    }
}
