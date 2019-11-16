package com.codesense.driverapp.ui.documentstatus;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.codesense.driverapp.data.DocumentsListItem;
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

public class DocumentStatusViewModel extends ViewModel {

    private RequestHandler requestHandler;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> apiResponseMutableLiveData = new MutableLiveData<>();

    @Inject
    public DocumentStatusViewModel(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    private Observable<MergedResponse> createObservableObject(List<DocumentsListItem> documentsListItem, VehicleDetailRequest vehicleDetailRequest) {
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
        } while (++count < documentsListItem.size());
        Observable<MergedResponse> observable = null;
        switch (size) {
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
        }
        return observable;
    }

    public MutableLiveData<ApiResponse> getApiResponseMutableLiveData() {
        return apiResponseMutableLiveData;
    }

    public void fetchOwnerCumDriverStatusRequest(){
        if(null != requestHandler) {
            disposables.add(requestHandler.fetchOwnerCumDriverStatusRequest(ApiUtility.getInstance().getApiKeyMetaData()).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.OWNER_CUM_DRIVER_STATUS))).
                    subscribe(result -> apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.OWNER_CUM_DRIVER_STATUS, result)),
                            error -> {apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.OWNER_CUM_DRIVER_STATUS, error));}));
        }
    }

    public void fetchNonDrivingPartnerStatusRequest(){
        if(null != requestHandler) {
            disposables.add(requestHandler.fetchNonDrivingPartnerStatusRequest(ApiUtility.getInstance().getApiKeyMetaData()).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.NON_DRIVING_PARTNER_STATUS))).
                    subscribe(result -> apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.NON_DRIVING_PARTNER_STATUS, result)),
                            error -> {apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.NON_DRIVING_PARTNER_STATUS, error));}));
        }
    }

    /**
     * This method to upload multiple files to server.
     * This method support 9 Observable.
     * This method will throw IllegalArgumentException based on file size.
     *
     * @param documentsListItem
     */
    public void uploadDocumentRequest(List<DocumentsListItem> documentsListItem, VehicleDetailRequest vehicleDetailRequest) {
        if (null != requestHandler && null != documentsListItem) {
            if (1 == documentsListItem.size()) {
                uploadDocumentRequest(documentsListItem.get(0), vehicleDetailRequest);
                return;
            }
            disposables.add(createObservableObject(documentsListItem, vehicleDetailRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.UPLOAD_DOCUEMNT)))
                    .subscribe(result ->
                                    apiResponseMutableLiveData.setValue(ApiResponse.successMultiple(ServiceType.UPLOAD_DOCUEMNT, result.docuemntListStatusResponse)),
                            error -> apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.UPLOAD_DOCUEMNT, error))));
        }
    }

    /**
     * This method to upload document to server.
     *
     * @param documentsListItem
     */
    public void uploadDocumentRequest(DocumentsListItem documentsListItem, VehicleDetailRequest vehicleDetailRequest) {
        if (null != requestHandler) {
            disposables.add(requestHandler.uploadDocumentsRequest(ApiUtility.getInstance().getApiKeyMetaData(),
                    documentsListItem, vehicleDetailRequest).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.UPLOAD_DOCUEMNT))).
                    subscribe(result -> apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.UPLOAD_DOCUEMNT, result)),
                            error -> {
                                apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.UPLOAD_DOCUEMNT, error));
                            }));
        }
    }

    private class MergedResponse {
        // this is just a POJO to store all the responses in one object
        private JsonElement[] docuemntListStatusResponse;

        private MergedResponse(JsonElement... documentsListStatusResponse) {
            this.docuemntListStatusResponse = documentsListStatusResponse;
        }
    }
}
