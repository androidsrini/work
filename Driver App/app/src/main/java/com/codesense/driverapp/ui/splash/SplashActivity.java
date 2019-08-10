package com.codesense.driverapp.ui.splash;

import android.content.SharedPreferences;
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
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.ui.launchscreen.LaunchScreenActivity;
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

    @Override
    protected int layoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchOwnerSignupStatusRequest();
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
        }, INTERVAL_TIME);
    }

    /**
     * This method to feth SignUp status and validate user.
     */
    private void fetchOwnerSignupStatusRequest() {
     compositeDisposable.add(requestHandler.fetchOwnerSignupStatusRequest(ApiUtility.getInstance().getApiKeyMetaData())
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .doOnSubscribe(d->handelApiResonse(ApiResponse.loading()))
             .subscribe(result->handelApiResonse(ApiResponse.success(result)),
                     error->handelApiResonse(ApiResponse.error(error))));
    }

    private void handelApiResonse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (apiResponse.isValidResponse()) {
                    SiginUpStatusResponse siginUpStatusResponse = new Gson().fromJson(apiResponse.data, SiginUpStatusResponse.class);
                    SignupStatus signupStatus = siginUpStatusResponse.getSignupStatus();
                    if (utility.parseInt(signupStatus.getOtpVerify()) == 0) {
                        //To show OTP screen.
                        VerifyMobileActivity.start(this, appSharedPreference.getUserID(), appSharedPreference.getMobileNumberKey());
                    } else if (TextUtils.isEmpty(signupStatus.getOwnerTypeId())) {
                        //To show Owner Type select screen
                        SelectTypeActivity.start(this);
                    } else if (utility.parseInt(signupStatus.getAgreementAccept()) == 0) {
                        //To show Agreement screen
                        SelectTypeActivity.start(this);
                    } else {
                        UploadDocumentActivity.start(this);
                    }
                    finish();
                } else {
                    UploadDocumentActivity.start(this);
                    finish();
                }
                break;
            case ERROR:
                utility.dismissDialog();
                break;
        }
    }
}