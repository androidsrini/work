package com.codesense.driverapp.ui.adddriver;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Observable;
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

    public void fetchVehiclesListAndDocumentStatusRequest() {
        Observable<JsonElement> vehiclesListObservable = requestHandler.fetchVehiclesListRequest(ApiUtility.getInstance().getApiKeyMetaData());
        Observable<JsonElement> documentStatusDriverObservable = requestHandler.fetchDocumentStatusDriverRequest(ApiUtility.getInstance().getApiKeyMetaData());
        disposables.add(Observable.zip(vehiclesListObservable, documentStatusDriverObservable, MergedResponse::new)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(d->apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.GET_DOCUMENTS_STATUS_DRIVER_AND_VEHICLE_LIST)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result->apiResponseMutableLiveData.setValue(ApiResponse.successMultiple(ServiceType.GET_DOCUMENTS_STATUS_DRIVER_AND_VEHICLE_LIST,
                        result.jsonElementsResponse)),
                        error->apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.GET_DOCUMENTS_STATUS_DRIVER_AND_VEHICLE_LIST,
                                error))));
    }

    private class MergedResponse {
        // this is just a POJO to store all the responses in one object
        private JsonElement[] jsonElementsResponse;

        private MergedResponse(JsonElement... jsonElementsResponse) {
            this.jsonElementsResponse = jsonElementsResponse;
        }
    }
}
