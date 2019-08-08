package com.codesense.driverapp.ui.uploaddocument;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.util.SparseArray;

import com.codesense.driverapp.data.DocumentsListItem;
import com.codesense.driverapp.data.DocumentsListStatusResponse;
import com.codesense.driverapp.data.VehicleDetailRequest;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UploadDocumentViewModel extends ViewModel {

    private static final String TAG = "Driver";
    private RequestHandler requestHandler;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<UploadDocumentApiResponse> apiResponseMutableLiveData = new MutableLiveData<>();

    @Inject
    public UploadDocumentViewModel(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    private SparseArray<Observable<JsonElement>> createDocumentListToSparseArray(List<DocumentsListItem> documentsListItems, VehicleDetailRequest vehicleDetailRequest) {
        SparseArray<Observable<JsonElement>> hashMap = new SparseArray<>();
        int count = 0;
        do {
            hashMap.put(count, requestHandler.uploadDocumentsRequest(ApiUtility.getInstance().getApiKeyMetaData(),
                    documentsListItems.get(count), vehicleDetailRequest));
        } while (++ count < documentsListItems.size());
        return hashMap;
    }

    public MutableLiveData<UploadDocumentApiResponse> getApiResponseMutableLiveData() {
        return apiResponseMutableLiveData;
    }

    public void fetchVehicleTypesRequest(){
        if(null != requestHandler) {
            disposables.add(requestHandler.fetchVehicleTypesRequest(ApiUtility.getInstance().getApiKeyMetaData()).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.loading(),
                            UploadDocumentApiResponse.ServiceType.VEHICLE_TYPES))).
                    subscribe(result -> apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.success(result),
                            UploadDocumentApiResponse.ServiceType.VEHICLE_TYPES)),
                            error -> {apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.error(error),
                                    UploadDocumentApiResponse.ServiceType.VEHICLE_TYPES));}));
        }
    }

    /**
     * This method to fetch Driver document list from server.
     */
    @Deprecated
    public void fetchDriverDocumentListRequest(){
        if(null != requestHandler) {
            disposables.add(requestHandler.fetchDriverDocumentListRequest(ApiUtility.getInstance().getApiKeyMetaData()).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.loading(),
                            UploadDocumentApiResponse.ServiceType.DRIVER))).
                    subscribe(result -> apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.success(result),
                            UploadDocumentApiResponse.ServiceType.DRIVER)),
                            error -> {apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.error(error),
                                    UploadDocumentApiResponse.ServiceType.DRIVER));}));
        }
    }

    /**
     * This method to fetch vehicle list from server
     */
    @Deprecated
    public void fetchVehicleListRequest(){
        if(null != requestHandler) {
            disposables.add(requestHandler.fetchVehicleListRequest(ApiUtility.getInstance().getApiKeyMetaData()).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.loading(),
                            UploadDocumentApiResponse.ServiceType.VEHICLE))).
                    subscribe(result -> apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.success(result),
                            UploadDocumentApiResponse.ServiceType.VEHICLE)),
                            error -> {apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.error(error),
                                    UploadDocumentApiResponse.ServiceType.VEHICLE));}));
        }
    }

    /**
     * This method to fetch Driver and vehicle list from server.
     * @reference link: https://stackoverflow.com/questions/30219877/rxjava-android-how-to-use-the-zip-operator
     */
    public void fetchDocumentListRequest() {
        if (null != requestHandler) {
            disposables.add(Observable.zip(requestHandler.fetchDriverDocumentListRequest(ApiUtility.getInstance().getApiKeyMetaData()),
                    requestHandler.fetchVehicleListRequest(ApiUtility.getInstance().getApiKeyMetaData()), MergedResponse::new)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(d->apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.loading(),
                            UploadDocumentApiResponse.ServiceType.ALL_DOCUMENT)))
                    .subscribe(result->{
                        Log.d(TAG, "The fetch all request response: " + result);
                        apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.successMultiple(result.docuemntListStatusResponse),
                                        UploadDocumentApiResponse.ServiceType.ALL_DOCUMENT));
                        /*apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.success(result.vehicleListStatusResponse),
                                        UploadDocumentApiResponse.ServiceType.VEHICLE));*/
                        },
                    error->{apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.error(error),
                            UploadDocumentApiResponse.ServiceType.ALL_DOCUMENT));}));
        }
    }

    /**
     * This method to upload document to server.
     * @param documentsListItem
     * @param vehicleDetailRequest
     */
    @Deprecated
    public void uploadDocumentRequest(DocumentsListItem documentsListItem, VehicleDetailRequest vehicleDetailRequest) {
        if(null != requestHandler) {
            disposables.add(requestHandler.uploadDocumentsRequest(ApiUtility.getInstance().getApiKeyMetaData(), documentsListItem, vehicleDetailRequest).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.loading(),
                            UploadDocumentApiResponse.ServiceType.UPLOAD_DOCUEMNT))).
                    subscribe(result -> apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.success(result),
                            UploadDocumentApiResponse.ServiceType.UPLOAD_DOCUEMNT)),
                            error -> {apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.error(error),
                                    UploadDocumentApiResponse.ServiceType.UPLOAD_DOCUEMNT));}));
        }
    }

    /**
     * This method to upload multiple files to server.
     * This method support 9 Observable.
     * This method will throw IllegalArgumentException based on file size.
     * @param documentsListItem
     * @param vehicleDetailRequest
     */
    public void uploadDocumentRequest(List<DocumentsListItem> documentsListItem, VehicleDetailRequest vehicleDetailRequest) {
        if(null != requestHandler) {
            if (documentsListItem.size() > 10) {
                throw new IllegalArgumentException("uploadDocumentRequest api support 9 items only");
            }
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
                Observable<JsonElement> observable = requestHandler.uploadDocumentsRequest(ApiUtility.getInstance().getApiKeyMetaData(),
                        documentsListItem.get(count), vehicleDetailRequest);
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
            } while (++ count < documentsListItem.size());
            disposables.add(Observable.zip(observable1, observable2, observable3, observable4, observable5, observable6,
                    observable7, observable8, observable9, MergedResponse::new)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(d->apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.loading(),
                            UploadDocumentApiResponse.ServiceType.UPLOAD_DOCUEMNTS)))
                    .subscribe(result->
                                apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.successMultiple(result.docuemntListStatusResponse),
                                        UploadDocumentApiResponse.ServiceType.UPLOAD_DOCUEMNTS)),
                            error->apiResponseMutableLiveData.setValue(UploadDocumentApiResponse.newInstance(ApiResponse.error(error),
                                    UploadDocumentApiResponse.ServiceType.UPLOAD_DOCUEMNTS))));
        }
    }

    private class MergedResponse{
        // this is just a POJO to store all the responses in one object
        private JsonElement[] docuemntListStatusResponse;

        private MergedResponse(JsonElement... documentsListStatusResponse){
            this.docuemntListStatusResponse = documentsListStatusResponse;
        }
    }

}
