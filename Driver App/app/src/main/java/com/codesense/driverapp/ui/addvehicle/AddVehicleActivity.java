package com.codesense.driverapp.ui.addvehicle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.data.AddDriverRequest;
import com.codesense.driverapp.data.AddVehicleRequest;
import com.codesense.driverapp.data.AvailableDriversItem;
import com.codesense.driverapp.data.AvailableDriversResponse;
import com.codesense.driverapp.data.CitiesItem;
import com.codesense.driverapp.data.CitiesListResponse;
import com.codesense.driverapp.data.CountriesItem;
import com.codesense.driverapp.data.CountriesListResponse;
import com.codesense.driverapp.data.VehicleTypeResponse;
import com.codesense.driverapp.data.VehicleTypesItem;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.DatabaseClient;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.Constant;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.codesense.driverapp.ui.helper.CrashlyticsHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddVehicleActivity extends DrawerActivity implements View.OnClickListener {


    ScrollView scrollView;
    //RelativeLayout vehicleTypeRelativeLayout;
    //TextView vehicleTypeTextView;
    //ImageView vehicleTypeArrowImageView;
    //View view, view1, view2, view3, view4, view5, view6, view7;
    EditText etVehicleName;
    EditText etVehicleNumber;
    AppCompatAutoCompleteTextView etDriverName, addVehicleCountryAutoCompleteTextView;
    /*EditText etDriverContNum;
    EditText etDriverEmail;
    EditText etDriverPassword;
    EditText etDriverConPassword;*/
    Button btnAddVehicle, addDriverButton;
    /**
     * These fields for new driver.
     */
    EditText etDriverFirstName, etDriverLastName, etDriverContNum, etDriverEmail, etDriverPassword, etDriverConPassword;
    private ConstraintLayout addDriverDetailsParentConstrainLayout;
    private AppCompatSpinner vehicleTypeAppCompatSpinner;
    private List<VehicleTypesItem> vehicleTypesItems;
    private boolean doesCountryListApiFatched;
    private String selectedCountry;
    private List<AvailableDriversItem> availableDriversItemList;
    /**
     * To create UploadDocumentViewModel object.
     */
    @Inject
    protected RequestHandler requestHandler;
    /**
     * To create Utility object.
     */
    @Inject
    protected Utility utility;
    /**
     * To create AddVehicleViewModel object
     */
    @Inject protected AddVehicleViewModel addVehicleViewModel;
    private CompositeDisposable compositeDisposable;
    private CountriesItem countriesItem;

    /**
     * This method to start AddVehicleActivity
     * @param context
     */
    public static void start(Context context) {
        context.startActivity(new Intent(context, AddVehicleActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_add_vehicle, null, false);
        frameLayout.addView(contentView);
        titleTextView.setText(getResources().getString(R.string.add_vehicle_title));
        availableDriversItemList = new ArrayList<>();
        addVehicleViewModel.getApiResponseMutableLiveData().observe(this, this::handleApiResponse);
        compositeDisposable = new CompositeDisposable();
        initially();
        setDynamicValue();
        functionality();
    }

    private void handleApiResponse(AddVehicleApiResponse addVehicleApiResponse) {
        ApiResponse apiResponse = addVehicleApiResponse.getApiResponse();
        AddVehicleApiResponse.ServiceType serviceType = addVehicleApiResponse.getServiceType();
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (AddVehicleApiResponse.ServiceType.ADD_DRIVER == serviceType) {
                    if (null != apiResponse.getResponseJsonObject() && apiResponse.isValidResponse()) {
                        int driverId = apiResponse.getResponseJsonObject().optInt(Constant.DRIVER_ID, 0);
                        addVehicleViewModel.addVehicleToOwnerRequest(createAddVehicleRequest(String.valueOf(driverId)));
                    }
                } else if (AddVehicleApiResponse.ServiceType.VEHICLE_TYPES == serviceType) {
                    VehicleTypeResponse vehicleTypeResponse = new Gson().fromJson(apiResponse.data, VehicleTypeResponse.class);
                    if (null != vehicleTypeResponse && null != vehicleTypeResponse.getVehicleTypes()) {
                        vehicleTypeSpinnerUI(vehicleTypeResponse.getVehicleTypes());
                    }
                } else if (AddVehicleApiResponse.ServiceType.ADD_VEHICLE == serviceType) {

                }
                break;
            case SUCCESS_MULTIPLE:
                utility.dismissDialog();
                if (AddVehicleApiResponse.ServiceType.VEHICLE_TYPES_AND_DRIVERS_LIST == serviceType) {
                    JsonElement[] jsonElement = apiResponse.datas;
                    final int VEHICLE_TYPE = 0;
                    final int AVAILABLE_DRIVERS = 1;
                    JsonElement vehicleTypeJsonElement = jsonElement[VEHICLE_TYPE];
                    if (null != vehicleTypeJsonElement) {
                        VehicleTypeResponse vehicleTypeResponse = new Gson().fromJson(apiResponse.data, VehicleTypeResponse.class);
                        if (null != vehicleTypeResponse && null != vehicleTypeResponse.getVehicleTypes()) {
                            vehicleTypeSpinnerUI(vehicleTypeResponse.getVehicleTypes());
                        }
                    }
                    JsonElement availableDrivers = jsonElement[AVAILABLE_DRIVERS];
                    if (null != availableDrivers) {
                        //Update available drivers UI.
                        availableDriversItemList.clear();
                        AvailableDriversResponse availableDriversResponse = new Gson().fromJson(availableDrivers, AvailableDriversResponse.class);
                        if (null != availableDriversResponse && null != availableDriversResponse.getAvailableDrivers()) {
                            availableDriversItemList.addAll(availableDriversResponse.getAvailableDrivers());
                            updateAvailableDriversUI(availableDriversResponse.getAvailableDrivers());
                        }
                    }
                } else if (AddVehicleApiResponse.ServiceType.VEHICLE_TYPES_AND_DRIVERS_LIST_COUNTRY_LIST == serviceType) {
                    doesCountryListApiFatched = true;
                    JsonElement[] jsonElement = apiResponse.datas;
                    final int VEHICLE_TYPE = 0;
                    final int AVAILABLE_DRIVERS = 1;
                    final int COUNTRY_LIST = 2;
                    final int CITY_LIST = 3;
                    JsonElement vehicleTypeJsonElement = jsonElement[VEHICLE_TYPE];
                    if (null != vehicleTypeJsonElement) {
                        VehicleTypeResponse vehicleTypeResponse = new Gson().fromJson(apiResponse.data, VehicleTypeResponse.class);
                        if (null != vehicleTypeResponse && null != vehicleTypeResponse.getVehicleTypes()) {
                            vehicleTypeSpinnerUI(vehicleTypeResponse.getVehicleTypes());
                        }
                    }
                    JsonElement availableDrivers = jsonElement[AVAILABLE_DRIVERS];
                    if (null != availableDrivers) {
                        //Update available drivers UI.
                        availableDriversItemList.clear();
                        AvailableDriversResponse availableDriversResponse = new Gson().fromJson(availableDrivers, AvailableDriversResponse.class);
                        if (null != availableDriversResponse && null != availableDriversResponse.getAvailableDrivers()) {
                            availableDriversItemList.addAll(availableDriversResponse.getAvailableDrivers());
                            updateAvailableDriversUI(availableDriversResponse.getAvailableDrivers());
                        }
                    }
                    //JsonElement[] jsonElement = apiResponse.datas;
                    JsonElement countryListJsonElement = jsonElement[COUNTRY_LIST];
                    if (null != countryListJsonElement) {
                        deleteAllCountriesFromDataBase(countryListJsonElement);
                    }
                    JsonElement cityListJsonElement = jsonElement[CITY_LIST];
                    if (null != cityListJsonElement) {
                        //Update available drivers UI.
                        deleteAllCitiesFromDataBase(cityListJsonElement);
                    }
                }
                break;
            case ERROR:
                utility.dismissDialog();
                break;
        }
    }

    private boolean doesDriverNameContainsInDriverAvailableList(String driverName) {
        for (AvailableDriversItem availableDriversItem: availableDriversItemList) {
            String name = availableDriversItem.getDriverFirstName() + " " + availableDriversItem.getDriverLastName();
            if (name.contains(driverName))
                return true;
        }
        return false;
    }

    private void initially() {
        scrollView = findViewById(R.id.scrollView);
        vehicleTypeAppCompatSpinner = findViewById(R.id.vehicleTypeAppCompatSpinner);
        //vehicleTypeRelativeLayout = findViewById(R.id.vehicleTypeRelativeLayout);
        //vehicleTypeTextView = findViewById(R.id.vehicleTypeTextView);
        //vehicleTypeArrowImageView = findViewById(R.id.vehicleTypeArrowImageView);
        etVehicleName = findViewById(R.id.etVehicleName);
        etVehicleNumber = findViewById(R.id.etVehicleNumber);
        etDriverName = findViewById(R.id.etDriverName);
        addDriverDetailsParentConstrainLayout = findViewById(R.id.addDriverDetailsParentConstrainLayout);
        /*etDriverContNum = findViewById(R.id.etDriverContNum);
        etDriverEmail = findViewById(R.id.etDriverEmail);
        etDriverPassword = findViewById(R.id.etDriverPassword);
        etDriverConPassword = findViewById(R.id.etDriverConPassword);*/
        btnAddVehicle = findViewById(R.id.btnAddVehicle);
        addDriverButton = findViewById(R.id.addDriverButton);
        addVehicleCountryAutoCompleteTextView = findViewById(R.id.addVehicleCountryAutoCompleteTextView);
        /**
         * New driver fields
         */
        etDriverFirstName = findViewById(R.id.etDriverFirstName);
        etDriverLastName = findViewById(R.id.etDriverLastName);
        etDriverContNum = findViewById(R.id.etDriverContNum);
        etDriverEmail = findViewById(R.id.etDriverEmail);
        etDriverPassword = findViewById(R.id.etDriverPassword);
        etDriverConPassword = findViewById(R.id.etDriverConPassword);
    }

    private String getAddVehicleCountryAutoCompleteTextView() {
        return selectedCountry;
    }

    private String getEtDriverFirstName() {
        return etDriverFirstName.getText().toString().trim();
    }

    private String getEtDriverLastName() {
        return etDriverLastName.getText().toString().trim();
    }

    private String getEtDriverContNum() {
        return etDriverContNum.getText().toString().trim();
    }

    private String getEtDriverEmail() {
        return etDriverEmail.getText().toString().trim();
    }

    private String getEtDriverPassword() {
        return etDriverPassword.getText().toString().trim();
    }

    private String getEtDriverConPassword() {
        return etDriverConPassword.getText().toString().trim();
    }

    private String getEtVehicleName() {
        return etVehicleName.getText().toString().trim();
    }

    private String getEtVehicleNumber() {
        return etVehicleNumber.getText().toString().trim();
    }

    private String getVehicleTypeId() {
        return ((VehicleTypesItem) vehicleTypeAppCompatSpinner.getSelectedItem()).getVehicleTypeId();
    }

    private String getDriverName() {
        return etDriverName.getText().toString().trim();
    }

    private String getDriverIdFromDriverList() {
        return findDriverId(getDriverName());
    }

    private boolean isValidAddNewDriverFields() {
        boolean isValid = true;
        if (TextUtils.isEmpty(getEtDriverFirstName())) {
            utility.showToastMsg("Driver First Name Required");
            isValid = false;
        } else if (TextUtils.isEmpty(getEtDriverLastName())) {
            utility.showToastMsg("Driver Last Name Required");
            isValid = false;
        } else if (TextUtils.isEmpty(getEtDriverContNum())) {
            utility.showToastMsg("Driver Contact Number Required");
            isValid = false;
        } else if (TextUtils.isEmpty(getEtDriverEmail())) {
            utility.showToastMsg("Driver Email Required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(getEtDriverEmail()).matches()) {
            utility.showToastMsg("Email id not valid");
            isValid = false;
        } else if (TextUtils.isEmpty(getEtDriverPassword())) {
            utility.showToastMsg("Driver Password Required");
            isValid = false;
        } else if (TextUtils.isEmpty(getEtDriverConPassword())) {
            utility.showToastMsg("Driver Confirm Password Required");
            isValid = false;
        } else if (!getEtDriverPassword().equals(getEtDriverConPassword())) {
            utility.showToastMsg("Password does not match");
            isValid = false;
        } else if (TextUtils.isEmpty(getAddVehicleCountryAutoCompleteTextView())) {
            utility.showToastMsg("Country required");
            isValid = false;
        }
        return isValid;
    }

    private boolean isValidVehicleFields() {
        boolean isValid = true;
        if (TextUtils.isEmpty(getEtVehicleName())) {
            utility.showToastMsg("Vehicle Name Required");
            isValid = false;
        } else if (TextUtils.isEmpty(getEtVehicleNumber())) {
            utility.showToastMsg("Vehicle Number Required");
            isValid = false;
        }
        return isValid;
    }

    private String findDriverId(String driverName) {
        for (AvailableDriversItem availableDriversItem: availableDriversItemList) {
            if (availableDriversItem.toString().equals(driverName)) {
                return availableDriversItem.getDriverId();
            }
        }
        return null;
    }

    private void setDynamicValue() {
        int topBottomSpace = (int) (screenHeight * 0.0089);
        RelativeLayout.LayoutParams btnUpdateLayoutParams = (RelativeLayout.LayoutParams) btnAddVehicle.getLayoutParams();
        btnUpdateLayoutParams.setMargins(topBottomSpace * 2, 0, topBottomSpace * 2, topBottomSpace * 3);
        btnAddVehicle.setLayoutParams(btnUpdateLayoutParams);
        btnAddVehicle.setOnClickListener(this);
    }

    private boolean doesDriverDetailsLayoutExpanded() {
        return addDriverDetailsParentConstrainLayout.getVisibility() == View.VISIBLE;
    }

    private void functionality() {
        addDriverButton.setOnClickListener(v -> {
            if (!doesDriverDetailsLayoutExpanded()) {
                addDriverButton.setEnabled(false);
                utility.expand(addDriverDetailsParentConstrainLayout);
            }
        });
        compositeDisposable.add(RxTextView.textChanges(etDriverName)
                .map(charSequence -> {
                    if (charSequence.length() > 1) {
                        CrashlyticsHelper.d("Add driver name " + charSequence);
                        if (!doesDriverNameContainsInDriverAvailableList(charSequence.toString())) {
                            addDriverButton.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                    return false;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
        compositeDisposable.add(RxTextView.textChanges(addVehicleCountryAutoCompleteTextView)
                .map(charSequence -> {
                  if (charSequence.length() > 1) {
                      selectedCountry = charSequence.toString().trim();
                      findCountryFromCountryName(selectedCountry);
                      return true;
                  } else {
                      countriesItem = null;
                      selectedCountry = null;
                      return false;
                  }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe());
        btnAddVehicle.setOnClickListener(view -> driverInformationAndVehicleInformationSendToServer());
        fetchAndUpdateCountryListFromDataBase();
    }

    private void vehicleTypeSpinnerUI(List<VehicleTypesItem> vehicleTypesItems) {
        ArrayAdapter<VehicleTypesItem> adapter = new ArrayAdapter<VehicleTypesItem>(this, android.R.layout.simple_spinner_item, vehicleTypesItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleTypeAppCompatSpinner.setAdapter(adapter);
    }

    private void updateAvailableDriversUI(List<AvailableDriversItem> availableDriversItems) {
        CrashlyticsHelper.d("UpdateAvailableDriversUI");
        ArrayAdapter<AvailableDriversItem> adapter = new ArrayAdapter<AvailableDriversItem>(this, android.R.layout.simple_spinner_item, availableDriversItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etDriverName.setAdapter(adapter);
    }

    /**
     * This method to fetch country list from data base and update in country auto complete UI.
     */
    private void fetchAndUpdateCountryListFromDataBase() {
        compositeDisposable.add(DatabaseClient.getInstance(this).getAppDatabase().countryDao().getCountryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    CrashlyticsHelper.d(" Country List data size is: " + result.size());
                    if (!result.isEmpty()) {
                        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, result);
                        addVehicleCountryAutoCompleteTextView.setAdapter(arrayAdapter);
                        //addVehicleViewModel.fetchVehicleTypeAndDriversList();
                        if (!doesCountryListApiFatched) {
                            addVehicleViewModel.fetchVehicleTypeAndDriversList();
                        }
                    } else if (!doesCountryListApiFatched) {
                        addVehicleViewModel.fetchVehicleAndLocationListRequest();
                        //addVehicleViewModel.fetchLocationListRequest();
                    }
                }, error -> {
                    CrashlyticsHelper.d(" Country List data getting error: " + Log.getStackTraceString(error));
                }));
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
                        fetchAndUpdateCountryListFromDataBase();
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
     * @param jsonElement we are getting server success and error response.
     */
    private void deleteAllCountriesFromDataBase(JsonElement jsonElement) {
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
                        CrashlyticsHelper.d("Country All data is deleted from Data base");
                        /**
                         * Update country list to Data base
                         */
                        if (null != jsonElement) {
                            CountriesListResponse countriesListResponse = new Gson().fromJson(
                                    jsonElement, CountriesListResponse.class);
                            updateCountryListInDataBase(countriesListResponse.getCountries());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Getting error when delete data
                        CrashlyticsHelper.d("Country All data deleted getting error: " + Log.getStackTraceString(e));
                    }
                });
    }

    /**
     * 1.This method will delete all cities data in data base.
     * 2.After delete all data completed it will update new cities list data in data base
     * 3.If data base empty then also we are getting onComplete call back.
     *
     * @param jsonElement we are getting server success and error response.
     */
    private void deleteAllCitiesFromDataBase(JsonElement jsonElement) {
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
                        CrashlyticsHelper.d("Cities All data is deleted from Data base");
                        /**
                         * Update country list to Data base
                         */
                        CitiesListResponse citiesListResponse = new Gson().fromJson(jsonElement, CitiesListResponse.class);
                        updateCitiesListInDataBase(citiesListResponse.getCities());
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Getting error when delete data
                        CrashlyticsHelper.d("Cities All data deleted getting error: " + Log.getStackTraceString(e));
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
                        CrashlyticsHelper.d(" Cities list updated in Data base");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // something went wrong
                        CrashlyticsHelper.d(" Cities list updated getting error");
                    }
                });
    }

    private AddDriverRequest createAddDriverRequestObject() {
        AddDriverRequest addDriverRequest = new AddDriverRequest();
        if (null != countriesItem) {
            addDriverRequest.setCountryId(countriesItem.getCountryId());
        }
        addDriverRequest.setDriverFirstName(getEtDriverFirstName());
        addDriverRequest.setDriverLastName(getEtDriverLastName());
        addDriverRequest.setEmailId(getEtDriverEmail());
        addDriverRequest.setMobileNumber(getEtDriverContNum());
        addDriverRequest.setPassword(getEtDriverPassword());
        addDriverRequest.setUserId(appSharedPreference.getUserID());
        return addDriverRequest;
    }

    private AddVehicleRequest createAddVehicleRequest(String driverID) {
        AddVehicleRequest addVehicleRequest = new AddVehicleRequest();
        addVehicleRequest.setDriverId(driverID);
        addVehicleRequest.setVehicleName(getEtVehicleName());
        addVehicleRequest.setVehicleNumber(getEtVehicleNumber());
        addVehicleRequest.setVehicleTypeId(getVehicleTypeId());
        return addVehicleRequest;
    }

    private void findCountryFromCountryName(String s) {
        compositeDisposable.add(DatabaseClient.getInstance(this).getAppDatabase().countryDao().findByCountryName(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    countriesItem = result;
                    //showCountryErrorMsg();
                }, error -> {
                    countriesItem = null;
                    //showCountryErrorMsg();
                }));
    }

    private void driverInformationAndVehicleInformationSendToServer() {
        if (doesDriverNameContainsInDriverAvailableList(getDriverName())) {
            addVehicleViewModel.addVehicleToOwnerRequest(createAddVehicleRequest(getDriverIdFromDriverList()));
        } else {
            if (isValidVehicleFields() && isValidAddNewDriverFields()) {
                addVehicleViewModel.addDriverRequest(createAddDriverRequestObject());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddVehicle) {
            /*Intent intent = new Intent(this, OnlineActivity.class);
            startActivity(intent);*/
        }
    }
}
