package com.codesense.driverapp.ui.uploaddocument;

import com.codesense.driverapp.data.DocumentsListItem;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class UploadDocumentViewModel extends ViewModel {

    private RequestHandler requestHandler;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<UploadDocumentApiResponse> apiResponseMutableLiveData = new MutableLiveData<>();

    @Inject
    public UploadDocumentViewModel(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
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

    public void uploadDocumentRequest(DocumentsListItem documentsListItem) {
        if(null != requestHandler) {
            disposables.add(requestHandler.uploadDocumentsRequest(ApiUtility.getInstance().getApiKeyMetaData(), documentsListItem).
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
}
