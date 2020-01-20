package com.codesense.driverapp.ui.adddriver;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.codesense.driverapp.data.AddDriverRequest;
import com.codesense.driverapp.data.DocumentsItem;
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

    private Observable<MergedResponse> createObservableObject(List<DocumentsItem> documentsListItem, String driverId) {
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
            Observable<JsonElement> observable = requestHandler.uploadDriverDocumentRequest(ApiUtility.getInstance().getApiKeyMetaData(),
                    documentsListItem, driverId);
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

    public void fetchVehiclesListAndDocumentStatusRequest() {
        Observable<JsonElement> vehiclesListObservable = requestHandler.fetchVehiclesListRequest(ApiUtility.getInstance().getApiKeyMetaData());
        Observable<JsonElement> documentStatusDriverObservable = requestHandler.fetchDocumentStatusRequest(ApiUtility.getInstance().getApiKeyMetaData());
        disposables.add(Observable.zip(vehiclesListObservable, documentStatusDriverObservable, MergedResponse::new)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(d->apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.GET_DOCUMENTS_STATUS_DRIVER_AND_VEHICLE_LIST)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result->apiResponseMutableLiveData.setValue(ApiResponse.successMultiple(ServiceType.GET_DOCUMENTS_STATUS_DRIVER_AND_VEHICLE_LIST,
                        result.docuemntListStatusResponse)),
                        error->apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.GET_DOCUMENTS_STATUS_DRIVER_AND_VEHICLE_LIST,
                                error))));
    }

    /**
     * This method to upload multiple files to server.
     * This method support 9 Observable.
     * This method will throw IllegalArgumentException based on file size.
     *
     * @param documentsListItem
     * @param driverId
     */
    public void uploadDocumentRequest(List<DocumentsItem> documentsListItem, String driverId) {
        if (null != requestHandler && null != documentsListItem) {
            disposables.add(requestHandler.uploadDriverDocumentRequest(ApiUtility.getInstance().getApiKeyMetaData(),
                    documentsListItem, driverId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.UPDATE_DRIVER_DOCUMENTS)))
                    .subscribe(result ->
                                    apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.UPDATE_DRIVER_DOCUMENTS, result)),
                            error -> apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.UPDATE_DRIVER_DOCUMENTS, error))));
        }
            /*if (1 == documentsListItem.size()) {
                uploadDocumentRequest(documentsListItem.get(0), driverId);
                return;
            } else if (documentsListItem.size() > 10) {
                List<DocumentsItem> documents1 = new ArrayList<>();
                List<DocumentsItem> documents2 = new ArrayList<>();
                for (int i=0; i<documentsListItem.size(); i++) {
                    if (i < 9) {
                        documents1.add(documentsListItem.get(i));
                    } else {
                        documents2.add(documentsListItem.get(i));
                    }
                }
                Observable<MergedResponse> observable1 = createObservableObject(documents1, driverId);
                Observable<MergedResponse> observable2 = null;
                if (documents2.size() == 1) {
                    uploadDocumentRequest(documents2.get(0), driverId);
                } else {
                    observable2 = createObservableObject(documents1, driverId);
                } if (null != observable2) {
                    disposables.add(Observable.zip(observable1, observable2, MergedResponse::new)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.UPDATE_DRIVER_DOCUMENTS)))
                            .subscribe(result -> {
                                        MergedResponse mergedResponse = result.mergedResponse;
                                        MergedResponse mergedResponse1 = result.mergedResponse1;
                                        apiResponseMutableLiveData.setValue(ApiResponse.successMultiple(ServiceType.UPDATE_DRIVER_DOCUMENTS, mergedResponse.docuemntListStatusResponse));
                                        apiResponseMutableLiveData.setValue(ApiResponse.successMultiple(ServiceType.UPDATE_DRIVER_DOCUMENTS, mergedResponse1.docuemntListStatusResponse));
                                    },
                                    error -> apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.UPDATE_DRIVER_DOCUMENTS, error))));
                } else {
                    disposables.add(observable1
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.UPDATE_DRIVER_DOCUMENTS)))
                            .subscribe(result -> {
                                        apiResponseMutableLiveData.setValue(ApiResponse.successMultiple(ServiceType.UPDATE_DRIVER_DOCUMENTS, result.docuemntListStatusResponse));
                                    },
                                    error -> apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.UPDATE_DRIVER_DOCUMENTS, error))));
                }
                //throw new IllegalArgumentException("uploadDocumentRequest api support 9 items only");
            } else {
                disposables.add(createObservableObject(documentsListItem, driverId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.UPDATE_DRIVER_DOCUMENTS)))
                        .subscribe(result ->
                                        apiResponseMutableLiveData.setValue(ApiResponse.successMultiple(ServiceType.UPDATE_DRIVER_DOCUMENTS, result.docuemntListStatusResponse)),
                                error -> apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.UPDATE_DRIVER_DOCUMENTS, error))));
            }
        }*/
    }

    /**
     * This method to upload document to server.
     *
     * @param
     * @param
     */
    /*public void uploadDocumentRequest(List<DocumentsItem> documentsListItem, String driverId) {
        if (null != requestHandler) {
            disposables.add(requestHandler.uploadDriverDocumentRequest(ApiUtility.getInstance().getApiKeyMetaData(), documentsListItem, driverId).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.UPDATE_DRIVER_DOCUMENTS))).
                    subscribe(result -> apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.UPDATE_DRIVER_DOCUMENTS, result)),
                            error -> {
                                apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.UPDATE_DRIVER_DOCUMENTS, error));
                            }));
        }
    }*/

    public void addDriverRequest(AddDriverRequest addDriverRequest) {
        if (null != requestHandler) {
            disposables.add(requestHandler.addVehicleDriverRequest(ApiUtility.getInstance().getApiKeyMetaData(), addDriverRequest).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.ADD_DRIVER))).
                    subscribe(result -> apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.ADD_DRIVER, result)),
                            error -> {
                                apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.ADD_DRIVER, error));
                            }));
        }
    }

    /**
     * This method to fetch Country list from server.
     */
    public void fetchCountryListRequest() {
        disposables.add(requestHandler.fetchCountryListRequest(ApiUtility.getInstance().getApiKeyMetaData())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.COUNTRY_LIST)))
                .subscribe(result -> apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.COUNTRY_LIST, result)),
                        error -> apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.COUNTRY_LIST, error))));
    }

    public void fetchDriverDetailsRequest(String driverId) {
        disposables.add(requestHandler.fetchDriverDetailsRequest(ApiUtility.getInstance().getApiKeyMetaData(), driverId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.DRIVER_DETAILS)))
                .subscribe(result -> apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.DRIVER_DETAILS, result)),
                        error -> apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.DRIVER_DETAILS, error))));
    }


    public void editVehicleDriverRequest(AddDriverRequest addDriverRequest) {
        disposables.add(requestHandler.editVehicleDriverRequest(ApiUtility.getInstance().getApiKeyMetaData(), addDriverRequest)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.EDIT_VEHICLE_DRIVER)))
                .subscribe(result -> apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.EDIT_VEHICLE_DRIVER, result)),
                        error -> apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.EDIT_VEHICLE_DRIVER, error))));
    }


    private class MergedResponse {
        // this is just a POJO to store all the responses in one object
        private JsonElement[] docuemntListStatusResponse;
        private MergedResponse mergedResponse;
        private MergedResponse mergedResponse1;

        private MergedResponse(JsonElement... documentsListStatusResponse) {
            this.docuemntListStatusResponse = documentsListStatusResponse;
        }

        public MergedResponse(MergedResponse mergedResponse, MergedResponse mergedResponse1) {
            this.mergedResponse = mergedResponse;
            this.mergedResponse1 = mergedResponse1;
        }
    }
}
