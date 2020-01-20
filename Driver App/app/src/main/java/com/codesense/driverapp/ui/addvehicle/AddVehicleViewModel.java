package com.codesense.driverapp.ui.addvehicle;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.codesense.driverapp.data.AddDriverRequest;
import com.codesense.driverapp.data.AddVehicleRequest;
import com.codesense.driverapp.data.DocumentsItem;
import com.codesense.driverapp.data.VehicleDetailRequest;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;
import com.google.gson.JsonElement;

import java.util.List;

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

    private Observable<MergedResponse> createObservableObject(List<DocumentsItem> documentsListItem, VehicleDetailRequest vehicleDetailRequest) {
        if (documentsListItem.size() > 10) {
            throw new IllegalArgumentException("uploadDocumentRequest api support 9 items only");
        }
        int size = documentsListItem.size();
        Observable<JsonElement> observable1 = null;
        Observable<JsonElement> observable2 = null;
        Observable<JsonElement> observable3 = null;
        Observable<JsonElement> observable4 = null;
        Observable<JsonElement> observable5 = null;
        Observable<JsonElement> observable6 = null;
        Observable<JsonElement> observable7 = null;
        Observable<JsonElement> observable8 = null;
        Observable<JsonElement> observable9 = null;
        int count = 0;
        do {
            Observable<JsonElement> observable = requestHandler.uploadOwnerVehicleRequest(ApiUtility.getInstance().getApiKeyMetaData(),
                    documentsListItem, vehicleDetailRequest);
            switch (count) {
                case 0:
                    observable1 = observable;
                    break;
                case 1:
                    observable2 = observable;
                    break;
                case 2:
                    observable3 = observable;
                    break;
                case 3:
                    observable4 = observable;
                    break;
                case 4:
                    observable5 = observable;
                    break;
                case 5:
                    observable6 = observable;
                    break;
                case 6:
                    observable7 = observable;
                    break;
                case 7:
                    observable8 = observable;
                    break;
                case 8:
                    observable9 = observable;
                    break;
            }
        } while (++count < documentsListItem.size());
        Observable<MergedResponse> observable = null;
        switch (size) {
            /*case 1:
                observable = requestHandler.uploadDocumentsRequest(ApiUtility.getInstance().getApiKeyMetaData(),
                        documentsListItem.get(count), vehicleDetailRequest);
                break;*/
            case 2:
                observable = Observable.zip(observable1, observable2, MergedResponse::new);
                break;
            case 3:
                observable = Observable.zip(observable1, observable2, observable3, MergedResponse::new);
                break;
            case 4:
                observable = Observable.zip(observable1, observable2, observable3, observable4, MergedResponse::new);
                break;
            case 5:
                observable = Observable.zip(observable1, observable2, observable3, observable4, observable5, MergedResponse::new);
                break;
            case 6:
                observable = Observable.zip(observable1, observable2, observable3, observable4, observable5, observable6, MergedResponse::new);
                break;
            case 7:
                observable = Observable.zip(observable1, observable2, observable3, observable4, observable5, observable6, observable7, MergedResponse::new);
                break;
            case 8:
                observable = Observable.zip(observable1, observable2, observable3, observable4, observable5, observable6, observable7, observable8, MergedResponse::new);
                break;
            case 9:
                observable = Observable.zip(observable1, observable2, observable3, observable4, observable5, observable6, observable7, observable8, observable9, MergedResponse::new);
                break;
            /*default:
                observable = requestHandler.uploadDocumentsRequest(ApiUtility.getInstance().getApiKeyMetaData(),
                        documentsListItem.get(count), vehicleDetailRequest);
                break;*/
        }
        return observable;
    }

    public MutableLiveData<ApiResponse> getApiResponseMutableLiveData() {
        return apiResponseMutableLiveData;
    }

    public void fetchVehicleTypesAndDocumentStatusVehicleRequestRequest() {
        if (null != requestHandler) {
            Observable<JsonElement> documentStatus = requestHandler.fetchDocumentStatusVehicleRequest(ApiUtility.getInstance().getApiKeyMetaData());
            Observable<JsonElement> vehicleType = requestHandler.fetchVehicleTypesRequest(ApiUtility.getInstance().getApiKeyMetaData());
            disposables.add(Observable.zip(documentStatus, vehicleType, MergedResponse::new).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.GET_DOCUMENTS_STATUS_VEHICLE_AND_VEHICLE_TYPES))).
                    subscribe(result -> apiResponseMutableLiveData.setValue(ApiResponse.successMultiple(ServiceType.GET_DOCUMENTS_STATUS_VEHICLE_AND_VEHICLE_TYPES, result.jsonElementsResponse)),
                            error -> {
                                apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.GET_DOCUMENTS_STATUS_VEHICLE_AND_VEHICLE_TYPES, error));
                            }));
        }
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

    /**
     * This method to upload document to server.
     *
     * @param documentsListItem
     * @param vehicleDetailRequest
     */
    public void uploadDocumentRequest(List<DocumentsItem> documentsListItem, VehicleDetailRequest vehicleDetailRequest) {
        if (null != requestHandler) {
            disposables.add(requestHandler.uploadOwnerVehicleRequest(ApiUtility.getInstance().getApiKeyMetaData(),
                    documentsListItem, vehicleDetailRequest).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.UPLOAD_OWNER_VEHICLE))).
                    subscribe(result -> apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.UPLOAD_OWNER_VEHICLE, result)),
                            error -> {
                                apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.UPLOAD_OWNER_VEHICLE, error));
                            }));
        }
    }

    /**
     * This method to upload multiple files to server.
     * This method support 9 Observable.
     * This method will throw IllegalArgumentException based on file size.
     *
     * @param documentsListItem
     * @param vehicleDetailRequest
     */
    /*public void uploadDocumentRequest(List<DocumentsItem> documentsListItem, VehicleDetailRequest vehicleDetailRequest) {
        if (null != requestHandler && null != documentsListItem) {
            *//*if (1 == documentsListItem.size()) {
                uploadDocumentRequest(documentsListItem.get(0), vehicleDetailRequest);
                return;
            }*//*
            disposables.add(requestHandler.uploadOwnerVehicleRequest(ApiUtility.getInstance().getApiKeyMetaData(),
                    documentsListItem, vehicleDetailRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.UPLOAD_OWNER_VEHICLE)))
                    .subscribe(result ->
                                    apiResponseMutableLiveData.setValue(ApiResponse.successMultiple(ServiceType.UPLOAD_OWNER_VEHICLE, result)),
                            error -> apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.UPLOAD_OWNER_VEHICLE, error))));
        }
    }*/

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
