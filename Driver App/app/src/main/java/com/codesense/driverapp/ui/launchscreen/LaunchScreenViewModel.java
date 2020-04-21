package com.codesense.driverapp.ui.launchscreen;

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

public class LaunchScreenViewModel extends ViewModel {

    private RequestHandler requestHandler;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> apiResponseMutableLiveData = new MutableLiveData<>();

    @Inject
    public LaunchScreenViewModel(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public MutableLiveData<ApiResponse> getApiResponseMutableLiveData() {
        return apiResponseMutableLiveData;
    }

    public void loadArticleDetails(){
        if(null != requestHandler) {
            disposables.add(requestHandler.getApiInfoRequest(ApiUtility.getInstance().getApiKeyMetaData()).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    doOnSubscribe(d -> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.API_INFO))).
                    subscribe(result -> apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.API_INFO, result)),
                            error -> {apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.API_INFO, error));}));
        }
    }

    public void fetchHomeDetailRequest() {
        disposables.add(requestHandler.fetchHomeDetailRequest(ApiUtility.getInstance().getApiKeyMetaData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d-> apiResponseMutableLiveData.setValue(ApiResponse.loading(ServiceType.HOME_DETAIL)))
                .subscribe(result-> apiResponseMutableLiveData.setValue(ApiResponse.success(ServiceType.HOME_DETAIL, result)),
                        error -> apiResponseMutableLiveData.setValue(ApiResponse.error(ServiceType.HOME_DETAIL, error))));
    }
}
