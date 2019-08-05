package com.codesense.driverapp.ui.documentstatus;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;

import javax.inject.Inject;

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

    public MutableLiveData<ApiResponse> getApiResponseMutableLiveData() {
        return apiResponseMutableLiveData;
    }

    public void fetchOwnerCumDriverStatusRequest(){
        if(null != requestHandler) {
            disposables.add(requestHandler.fetchOwnerCumDriverStatusRequest(ApiUtility.getInstance().getApiKeyMetaData()).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading())).
                    subscribe(result -> apiResponseMutableLiveData.setValue(ApiResponse.success(result)),
                            error -> {apiResponseMutableLiveData.setValue(ApiResponse.error(error));}));
        }
    }

    public void fetchNonDrivingPartnerStatusRequest(){
        if(null != requestHandler) {
            disposables.add(requestHandler.fetchNonDrivingPartnerStatusRequest(ApiUtility.getInstance().getApiKeyMetaData()).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading())).
                    subscribe(result -> apiResponseMutableLiveData.setValue(ApiResponse.success(result)),
                            error -> {apiResponseMutableLiveData.setValue(ApiResponse.error(error));}));
        }
    }
}
