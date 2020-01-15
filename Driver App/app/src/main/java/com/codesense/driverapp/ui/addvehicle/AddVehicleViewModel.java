package com.codesense.driverapp.ui.addvehicle;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.codesense.driverapp.data.AddDriverRequest;
import com.codesense.driverapp.data.AddVehicleRequest;
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

public class AddVehicleViewModel extends ViewModel {

    private RequestHandler requestHandler;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> apiResponseMutableLiveData = new MutableLiveData<>();

    @Inject
    public AddVehicleViewModel(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public MutableLiveData<ApiResponse> getApiResponseMutableLiveData() {
        return apiResponseMutableLiveData;
    }

    /*public void fetchVehicleTypesRequest(){
        if(null != requestHandler) {
            disposables.add(requestHandler.fetchVehicleTypesRequest(ApiUtility.getInstance().getApiKeyMetaData()).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading())).
                    subscribe(result -> apiResponseMutableLiveData.setValue(ApiResponse.success(result)),
                            error -> {apiResponseMutableLiveData.setValue(ApiResponse.error(error));}));
        }
    }

    private void fetchAvailableDrivers() {
        disposables.add(requestHandler.fetchAvailableDrivers(ApiUtility.getInstance().getApiKeyMetaData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> apiResponseMutableLiveData.postValue(ApiResponse.loading()))
                .subscribe(result -> apiResponseMutableLiveData.postValue(ApiResponse.success(result)),
                        error -> apiResponseMutableLiveData.postValue(ApiResponse.error(error))));
    }*/

    public void fetchVehicleTypeAndDriversList() {
        Observable<JsonElement> vehicleTypesObservable = requestHandler.fetchVehicleTypesRequest(ApiUtility.getInstance().getApiKeyMetaData());
        Observable<JsonElement> driversObservable = requestHandler.fetchAvailableDrivers(ApiUtility.getInstance().getApiKeyMetaData());
        disposables.add(Observable.zip(vehicleTypesObservable, driversObservable, MergedResponse::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> apiResponseMutableLiveData.postValue(ApiResponse.loading(ServiceType.VEHICLE_TYPES_AND_DRIVERS_LIST)))
                .subscribe(result-> apiResponseMutableLiveData.postValue(ApiResponse.successMultiple(ServiceType.VEHICLE_TYPES_AND_DRIVERS_LIST, result.jsonElementsResponse)),
                        error -> apiResponseMutableLiveData.postValue(ApiResponse.error(ServiceType.VEHICLE_TYPES_AND_DRIVERS_LIST, error))));
    }

    /**
     * This method to fetch country and city list form server.
     */
    public void fetchLocationListRequest() {
        Observable<JsonElement> countryObservable = requestHandler.fetchCountryListRequest(ApiUtility.getInstance().getApiKeyMetaData());
        Observable<JsonElement> cityObservable = requestHandler.fetchCountryListRequest(ApiUtility.getInstance().getApiKeyMetaData());
        disposables.add(Observable.zip(countryObservable, cityObservable, MergedResponse::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposables -> apiResponseMutableLiveData.postValue(ApiResponse.loading(ServiceType.COUNTRY_LIST)))
                .subscribe(result -> apiResponseMutableLiveData.postValue(ApiResponse.successMultiple(ServiceType.COUNTRY_LIST, result.jsonElementsResponse)),
                        error -> apiResponseMutableLiveData.postValue(ApiResponse.error(ServiceType.COUNTRY_LIST, error))));
    }

    public void fetchVehicleAndLocationListRequest() {
        Observable<JsonElement> vehicleTypesObservable = requestHandler.fetchVehicleTypesRequest(ApiUtility.getInstance().getApiKeyMetaData());
        Observable<JsonElement> driversObservable = requestHandler.fetchAvailableDrivers(ApiUtility.getInstance().getApiKeyMetaData());
        Observable<JsonElement> countryObservable = requestHandler.fetchCountryListRequest(ApiUtility.getInstance().getApiKeyMetaData());
        Observable<JsonElement> cityObservable = requestHandler.fetchCityListRequest(ApiUtility.getInstance().getApiKeyMetaData());
        disposables.add(Observable.zip(vehicleTypesObservable, driversObservable, countryObservable, cityObservable, MergedResponse::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposables -> apiResponseMutableLiveData.postValue(ApiResponse.loading(ServiceType.VEHICLE_TYPES_AND_DRIVERS_LIST_COUNTRY_LIST)))
                .subscribe(result -> apiResponseMutableLiveData.postValue(ApiResponse.successMultiple(ServiceType.VEHICLE_TYPES_AND_DRIVERS_LIST_COUNTRY_LIST, result.jsonElementsResponse)),
                        error -> apiResponseMutableLiveData.postValue(ApiResponse.error(ServiceType.VEHICLE_TYPES_AND_DRIVERS_LIST_COUNTRY_LIST, error))));
    }

    public void addDriverRequest(AddDriverRequest addDriverRequest) {
        disposables.add(requestHandler.addDriverToOwnerRequest(ApiUtility.getInstance().getApiKeyMetaData(), addDriverRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> apiResponseMutableLiveData.postValue(ApiResponse.loading(ServiceType.ADD_DRIVER)))
                .subscribe(result -> apiResponseMutableLiveData.postValue(ApiResponse.success(ServiceType.ADD_DRIVER, result)),
                        error -> apiResponseMutableLiveData.postValue(ApiResponse.error(ServiceType.ADD_DRIVER, error))));
    }

    public void addVehicleToOwnerRequest(AddVehicleRequest addVehicleRequest) {
        disposables.add(requestHandler.addVehicleToOwnerRequest(ApiUtility.getInstance().getApiKeyMetaData(), addVehicleRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> apiResponseMutableLiveData.postValue(ApiResponse.loading(ServiceType.ADD_VEHICLE)))
                .subscribe(result -> apiResponseMutableLiveData.postValue(ApiResponse.success(ServiceType.ADD_VEHICLE, result)),
                        error -> apiResponseMutableLiveData.postValue(ApiResponse.error(ServiceType.ADD_VEHICLE, error))));
    }

    public void fetchDocumentStatusVehicleRequest() {
        disposables.add(requestHandler.fetchDocumentStatusVehicleRequest(ApiUtility.getInstance().getApiKeyMetaData())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(d->apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.GET_DOCUMENTS_STATUS_VEHICLE)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result->apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.GET_DOCUMENTS_STATUS_VEHICLE, result)),
                        error->apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.GET_DOCUMENTS_STATUS_VEHICLE, error))));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    private class MergedResponse {
        // this is just a POJO to store all the responses in one object
        private JsonElement[] jsonElementsResponse;

        private MergedResponse(JsonElement... jsonElementsResponse) {
            this.jsonElementsResponse = jsonElementsResponse;
        }
    }
}
