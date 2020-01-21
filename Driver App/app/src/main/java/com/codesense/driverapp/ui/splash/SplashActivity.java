package com.codesense.driverapp.ui.splash;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.data.SiginUpStatusResponse;
import com.codesense.driverapp.data.SignupStatus;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.Constant;
import com.codesense.driverapp.net.NetworkChangeReceiver;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;
import com.codesense.driverapp.ui.addvehicle.AddVehicleActivity;
import com.codesense.driverapp.ui.documentstatus.DocumentStatusActivity;
import com.codesense.driverapp.ui.helper.CrashlyticsHelper;
import com.codesense.driverapp.ui.launchscreen.LaunchScreenActivity;
import com.codesense.driverapp.ui.online.OnlineActivity;
import com.codesense.driverapp.ui.selecttype.SelectTypeActivity;
import com.codesense.driverapp.ui.uploaddocument.UploadDocumentActivity;
import com.codesense.driverapp.ui.verifymobile.VerifyMobileActivity;
import com.google.gson.Gson;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseActivity {

    private static final int MINUTE = 1000;
    private static final int INTERVAL_TIME = 1 * MINUTE;
    @Inject
    RequestHandler requestHandler;
    @Inject
    Utility utility;
    @Inject
    AppSharedPreference appSharedPreference;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected int layoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        //adding user identity to crash report
        if (!TextUtils.isEmpty(appSharedPreference.getAccessTokenKey())
                && !TextUtils.isEmpty(appSharedPreference.getUserID())) {
            fetchOwnerSignupStatusRequest();
        } else {
            createHandler();
        }
    }

    private void createHandler() {
        Handler handler = new Handler();
        handler.postDelayed(()->{
            LaunchScreenActivity.start(this);
            finish();
        }, INTERVAL_TIME);
    }

    /**
     * This method to feth SignUp status and validate user.
     */
    private void fetchOwnerSignupStatusRequest() {
     compositeDisposable.add(requestHandler.fetchOwnerSignupStatusRequest(ApiUtility.getInstance().getApiKeyMetaData())
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .doOnSubscribe(d->handelApiResonse(ApiResponse.loading(ServiceType.GET_OWNER_SIGNUP_STATUS)))
             .subscribe(result->handelApiResonse(ApiResponse.success(ServiceType.GET_OWNER_SIGNUP_STATUS, result)),
                     error->handelApiResonse(ApiResponse.error(ServiceType.GET_OWNER_SIGNUP_STATUS, error))));
    }

    private void handelApiResonse(ApiResponse apiResponse) {
        ServiceType serviceType = apiResponse.getServiceType();
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (ServiceType.GET_OWNER_SIGNUP_STATUS == serviceType) {
                    if (apiResponse.isValidResponse()) {
                        SiginUpStatusResponse siginUpStatusResponse = new Gson().fromJson(apiResponse.data, SiginUpStatusResponse.class);
                        SignupStatus signupStatus = siginUpStatusResponse.getSignupStatus();
                        appSharedPreference.saveIsActivate(utility.parseInt(signupStatus.getIsActivated()));
                        appSharedPreference.saveIsLive(utility.parseInt(signupStatus.getLiveStatus()));
                        if (utility.parseInt(signupStatus.getOtpVerify()) == 0 || ApiResponse.OTP_VALIDATION == apiResponse.getResponseStatus()) {
                            //To show OTP screen.
                            VerifyMobileActivity.start(this, appSharedPreference.getUserID(),
                                    appSharedPreference.getMobileNumberKey(),
                                    ApiResponse.OTP_VALIDATION != apiResponse.getResponseStatus() || utility.parseInt(signupStatus.getOtpVerify()) == 0);
                        } else if (TextUtils.isEmpty(signupStatus.getOwnerTypeId())) {
                            //To show Owner Type select screen
                            SelectTypeActivity.start(this);
                        } else if (utility.parseInt(signupStatus.getAgreementAccept()) == 0) {
                            //To show Agreement screen
                            SelectTypeActivity.start(this);
                        } else if (0 == utility.parseInt(signupStatus.getIsActivated())) {
                            //To show online screen.
                            //OnlineActivity.start(this);
                            if (Constant.OWNER_ID.equals(String.valueOf(signupStatus.getOwnerTypeId())) ||
                                    1 == utility.parseInt(signupStatus.getVehicleActivation())) {
                                appSharedPreference.saveOwnerTypeId(Integer.parseInt(signupStatus.getOwnerTypeId()));
                                DocumentStatusActivity.start(this, Constant.NON_DRIVER_DOCUMENT_STATUS);
                                finish();
                            } else {
                                AddVehicleActivity.start(this);
                                finish();
                            }
                        } else if (utility.parseInt(signupStatus.getIsActivated()) == 1){
                            OnlineActivity.start(this);
                            finish();
                        }else {
                            UploadDocumentActivity.start(this);
                        }
                        finish();
                    } else {
                        LaunchScreenActivity.start(this);
                        finish();
                    }
                }
                break;
            case ERROR:
                utility.dismissDialog();
                CrashlyticsHelper.d("Failed response: " + apiResponse.error);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }
}
