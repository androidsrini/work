package com.codesense.driverapp.ui.launchscreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.data.CitiesItem;
import com.codesense.driverapp.data.CitiesListResponse;
import com.codesense.driverapp.data.CountriesItem;
import com.codesense.driverapp.data.CountriesListResponse;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.localstoreage.DatabaseClient;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.ui.register.RegisterActivity;
import com.codesense.driverapp.ui.signin.LoginActivity;
import com.codesense.driverapp.ui.uploaddocument.UploadDocumentActivity;
import com.google.gson.Gson;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Launch screen of the application and get public-key
 */
public class LaunchScreenActivity extends BaseActivity {

    private static final String TAG = "Driver";
    /**
     * CompositeDisposable object rxjava async process.
     */
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Initialize(R.id.tvWelcomeScreen)
    protected TextView tvWelcomeScreen;
    @Initialize(R.id.tvdriver)
    protected TextView tvdriver;
    @Initialize(R.id.tvRide)
    protected TextView tvRide;
    @Initialize(R.id.ll_btn)
    protected LinearLayout ll_btn;
    @Initialize(R.id.btnSignIn)
    protected Button btnSignIn;
    @Initialize(R.id.btnRegister)
    protected Button btnRegister;
    @Initialize(R.id.view)
    protected View view;
    @Initialize(R.id.tvAppName)
    protected TextView tvAppName;
    @Inject
    protected LaunchScreenViewModel launchScreenViewModel;
    @Inject
    protected Utility utility;
    @Inject
    protected RequestHandler requestHandler;
    @Inject
    protected AppSharedPreference appSharedPreference;

    /**
     * This method to launch LaunchScreenActivity
     * @param context
     */
    public static void start(Context context) {
        context.startActivity(new Intent(context, LaunchScreenActivity.class));
    }

    /**
     * This method to fetch Country list from server.
     */
    private void fetchCountryListRequest() {
        compositeDisposable.add(requestHandler.fetchCountryListRequest(ApiUtility.getInstance().getApiKeyMetaData())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> profileApiResponse(ApiResponse.loading(), ServiceType.COUNTRY_LIST))
                .subscribe(result -> profileApiResponse(ApiResponse.success(result), ServiceType.COUNTRY_LIST),
                        error -> profileApiResponse(ApiResponse.error(error), ServiceType.COUNTRY_LIST)));
    }

    /**
     * This method to fetch cities form server.
     */
    private void fetchCityListRequest() {
        compositeDisposable.add(requestHandler.fetchCityListRequest(ApiUtility.getInstance().getApiKeyMetaData())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> profileApiResponse(ApiResponse.loading(), ServiceType.CITIES))
                .subscribe(result -> profileApiResponse(ApiResponse.success(result), ServiceType.CITIES),
                        error -> profileApiResponse(ApiResponse.error(error), ServiceType.CITIES)));
    }

    /**
     * This method to update country list in data base
     *
     * @param countryList argument
     */
    private void updateCountryListInDataBase(List<CountriesItem> countryList) {
        //compositeDisposable.add();
        Completable.fromAction(() -> DatabaseClient.getInstance(this).getAppDatabase().countryDao().insertAllCountry(countryList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // just like with a Single
                    }

                    @Override
                    public void onComplete() {
                        // action was completed successfully
                        // Country list updated in DB.
                    }

                    @Override
                    public void onError(Throwable e) {
                        // something went wrong
                    }
                });
    }

    /**
     * 1.This method will delete all country data in data base.
     * 2.After delete all data completed it will update new country list data in data base
     * 3.If data base empty then also we are getting onComplete call back.
     *
     * @param apiResponse we are getting server success and error response.
     */
    private void deleteAllCountriesFromDataBase(ApiResponse apiResponse) {
        Completable.fromAction(() -> DatabaseClient.getInstance(this).getAppDatabase().countryDao().deleteAllRowFromDataBase())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        // Delete all row is completed.
                        Log.d(TAG, "Country All data is deleted from Data base");
                        /**
                         * Update country list to Data base
                         */
                        if (apiResponse.isValidResponse()) {
                            CountriesListResponse countriesListResponse = new Gson().fromJson(apiResponse.data, CountriesListResponse.class);
                            updateCountryListInDataBase(countriesListResponse.getCountries());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Getting error when delete data
                        Log.d(TAG, "Country All data deleted getting error: " + Log.getStackTraceString(e));
                    }
                });
    }

    /**
     * 1.This method will delete all cities data in data base.
     * 2.After delete all data completed it will update new cities list data in data base
     * 3.If data base empty then also we are getting onComplete call back.
     *
     * @param apiResponse we are getting server success and error response.
     */
    private void deleteAllCitiesFromDataBase(ApiResponse apiResponse) {
        Completable.fromAction(() -> DatabaseClient.getInstance(this).getAppDatabase().cityDao().deleteAllRowFromDataBase())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        // Delete all row is completed.
                        Log.d(TAG, "Cities All data is deleted from Data base");
                        /**
                         * Update country list to Data base
                         */
                        if (apiResponse.isValidResponse()) {
                            CitiesListResponse citiesListResponse = new Gson().fromJson(apiResponse.data, CitiesListResponse.class);
                            updateCitiesListInDataBase(citiesListResponse.getCities());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Getting error when delete data
                        Log.d(TAG, "Cities All data deleted getting error: " + Log.getStackTraceString(e));
                    }
                });
    }

    /**
     * This Method will update cities to Data base.
     *
     * @param cityList we are getting from server.
     */
    private void updateCitiesListInDataBase(List<CitiesItem> cityList) {
        //compositeDisposable.add();
        Completable.fromAction(() -> DatabaseClient.getInstance(this).getAppDatabase().cityDao().insertAllCity(cityList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // just like with a Single
                    }

                    @Override
                    public void onComplete() {
                        // action was completed successfully
                        // Country list updated in DB.
                        Log.d(TAG, " Cities list updated in Data base");
                        if (!TextUtils.isEmpty(appSharedPreference.getAccessTokenKey())) {
                            UploadDocumentActivity.start(LaunchScreenActivity.this);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // something went wrong
                        Log.d(TAG, " Cities list updated getting error");
                    }
                });
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_launch_screen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);
        launchScreenViewModel.getApiResponseMutableLiveData().observe(this, (apiResponse) -> {
            if (null != apiResponse)
                profileApiResponse(apiResponse, ServiceType.API_INFO);
        });
        launchScreenViewModel.loadArticleDetails();
        Log.d(TAG, " Request handler object value: " + requestHandler);
        setDynamicValue();
    }

    private void profileApiResponse(ApiResponse apiResponse, ServiceType serviceType) {
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (apiResponse.isValidResponse()) {
                    Log.d(TAG, "Success data: " + apiResponse.data);
                }
                if (ServiceType.API_INFO == serviceType) {
                    /**
                     * Fetch country list request api call.
                     */
                    fetchCountryListRequest();
                } else if (ServiceType.COUNTRY_LIST == serviceType) {
                    /**
                     * Fetch cities list from server request.
                     * And update country table in data base
                     */
                    deleteAllCountriesFromDataBase(apiResponse);
                    fetchCityListRequest();
                } else if (ServiceType.CITIES == serviceType) {
                    /**
                     * Update City table in Data base.
                     */
                    deleteAllCitiesFromDataBase(apiResponse);
                }
                break;
            case ERROR:
                utility.dismissDialog();
                Log.d(TAG, "Error data: " + apiResponse.data + " ,Service type: " + serviceType);
                break;
        }
    }

    private void setDynamicValue() {
        int topBottomSpace = (int) (screenHeight * 0.0089);

        LinearLayout.LayoutParams tvWelcomeScreenLayoutParams = (LinearLayout.LayoutParams) tvWelcomeScreen.getLayoutParams();
        tvWelcomeScreenLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace, 0, 0);
        tvWelcomeScreen.setLayoutParams(tvWelcomeScreenLayoutParams);

        LinearLayout.LayoutParams tvAppNameLayoutParams = (LinearLayout.LayoutParams) tvAppName.getLayoutParams();
        tvAppNameLayoutParams.setMargins(0, topBottomSpace, topBottomSpace * 3, 0);
        tvAppName.setLayoutParams(tvAppNameLayoutParams);

        LinearLayout.LayoutParams tvdriverLayoutParams = (LinearLayout.LayoutParams) tvdriver.getLayoutParams();
        tvdriverLayoutParams.setMargins(topBottomSpace * 3, 0, topBottomSpace * 3, 0);
        tvdriver.setLayoutParams(tvdriverLayoutParams);

        LinearLayout.LayoutParams ll_btnLayoutParams = (LinearLayout.LayoutParams) ll_btn.getLayoutParams();
        ll_btnLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace * 5, topBottomSpace * 3, 0);
        ll_btn.setLayoutParams(ll_btnLayoutParams);

        LinearLayout.LayoutParams btn_signInLayoutParams = (LinearLayout.LayoutParams) btnSignIn.getLayoutParams();
        btn_signInLayoutParams.setMargins(0, 0, topBottomSpace, 0);
        btnSignIn.setLayoutParams(btn_signInLayoutParams);

        LinearLayout.LayoutParams btn_registerLayoutParams = (LinearLayout.LayoutParams) btnRegister.getLayoutParams();
        btn_registerLayoutParams.setMargins(topBottomSpace, 0, 0, 0);
        btnRegister.setLayoutParams(btn_registerLayoutParams);

        LinearLayout.LayoutParams viewLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        viewLayoutParams.setMargins(0, topBottomSpace * 4, 0, 0);
        view.setLayoutParams(viewLayoutParams);

        LinearLayout.LayoutParams tvRideLayoutParams = (LinearLayout.LayoutParams) tvRide.getLayoutParams();
        tvRideLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 2, 0, 0);
        tvRide.setLayoutParams(tvRideLayoutParams);
    }

    @Onclick(R.id.btnRegister)
    public void btnRegister(View v) {
        RegisterActivity.start(this);
    }

    @Onclick(R.id.btnSignIn)
    public void btnSignIn(View v) {
        LoginActivity.start(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private enum ServiceType {
        API_INFO, COUNTRY_LIST, CITIES
    }
}
