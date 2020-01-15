package com.codesense.driverapp.ui.addvehicle;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.data.AddVehicleRequest;
import com.codesense.driverapp.data.AvailableDriversItem;
import com.codesense.driverapp.data.AvailableDriversResponse;
import com.codesense.driverapp.data.CitiesItem;
import com.codesense.driverapp.data.CitiesListResponse;
import com.codesense.driverapp.data.CountriesItem;
import com.codesense.driverapp.data.CountriesListResponse;
import com.codesense.driverapp.data.DocumentStatus;
import com.codesense.driverapp.data.DocumentStatusResponse;
import com.codesense.driverapp.data.DocumentsItem;
import com.codesense.driverapp.data.VehicleTypeResponse;
import com.codesense.driverapp.data.VehicleTypesItem;
import com.codesense.driverapp.di.utils.PermissionManager;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.DatabaseClient;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.Constant;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.codesense.driverapp.ui.helper.CrashlyticsHelper;
import com.codesense.driverapp.ui.uploaddocument.RecyclerTouchListener;
import com.codesense.driverapp.ui.uploaddocument.UploadDocumentActivity;
import com.codesense.driverapp.ui.uploaddocument.UploadDocumentAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.library.fileimagepicker.filepicker.FilePickerBuilder;
import com.library.fileimagepicker.filepicker.FilePickerConst;

import org.json.JSONException;
import org.json.JSONObject;

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

    private static final int IMAGE_PICKER = 0x0001;
    private static final int FILE_PICKER = 0x0002;
    private static final String TAG = AddVehicleActivity.class.getSimpleName();
    ScrollView scrollView;
    //RelativeLayout vehicleTypeRelativeLayout;
    //TextView vehicleTypeTextView;
    //ImageView vehicleTypeArrowImageView;
    //View view, view1, view2, view3, view4, view5, view6, view7;
    EditText etVehicleName;
    EditText etVehicleNumber;
    AppCompatAutoCompleteTextView etDriverName/*, addVehicleCountryAutoCompleteTextView*/;
    /*EditText etDriverContNum;
    EditText etDriverEmail;
    EditText etDriverPassword;
    EditText etDriverConPassword;*/
    Button btnAddVehicle;
    /**
     * These fields for new driver.
     */
    //
    TextInputLayout driverPasswordTextInputLayout, driverConTextInputLayout;
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
    @Inject
    protected PermissionManager permissionManager;
    private CompositeDisposable compositeDisposable;
    private CountriesItem countriesItem;
    private AvailableDriversItem availableDriversItem;
    private List<DocumentsItem> uploadDocumentActionInfos;
    private UploadDocumentAdapter adapter;
    private RecyclerView recyclerView;
    private int selectedDocumentsListPosition;
    private DocumentsItem selectedDocumetnsListItem;

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
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_add_vehicle, null, false);
        frameLayout.addView(contentView);
        titleTextView.setText(getResources().getString(R.string.add_vehicle_title));
        uploadDocumentActionInfos = new ArrayList<>();
        availableDriversItemList = new ArrayList<>();
        addVehicleViewModel.getApiResponseMutableLiveData().observe(this, this::handleApiResponse);
        compositeDisposable = new CompositeDisposable();
        initially();
        setDynamicValue();
        functionality();
    }

    private void handleApiResponse(ApiResponse apiResponse) {
        ServiceType serviceType = apiResponse.getServiceType();
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (ServiceType.ADD_DRIVER == serviceType) {
                    if (null != apiResponse.getResponseJsonObject() && apiResponse.isValidResponse()) {
                        int driverId = apiResponse.getResponseJsonObject().optInt(Constant.DRIVER_ID, 0);
                        addVehicleViewModel.addVehicleToOwnerRequest(createAddVehicleRequest(String.valueOf(driverId)));
                    }
                } else if (ServiceType.VEHICLE_TYPES == serviceType) {
                    VehicleTypeResponse vehicleTypeResponse = new Gson().fromJson(apiResponse.data, VehicleTypeResponse.class);
                    if (null != vehicleTypeResponse && null != vehicleTypeResponse.getVehicleTypes()) {
                        vehicleTypeSpinnerUI(vehicleTypeResponse.getVehicleTypes());
                    }
                } else if (ServiceType.ADD_VEHICLE == serviceType) {
                    clear();
                } else if (ServiceType.GET_DOCUMENTS_STATUS_VEHICLE == serviceType) {
                    updateDocumentListUI(apiResponse);
                }
                break;
            case SUCCESS_MULTIPLE:
                utility.dismissDialog();
                if (ServiceType.VEHICLE_TYPES_AND_DRIVERS_LIST == serviceType) {
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
                            //updateAvailableDriversUI(availableDriversResponse.getAvailableDrivers());
                        }
                    }
                } else if (ServiceType.VEHICLE_TYPES_AND_DRIVERS_LIST_COUNTRY_LIST == serviceType) {
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
                            //updateAvailableDriversUI(availableDriversResponse.getAvailableDrivers());
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
        etVehicleName = findViewById(R.id.etVehicleName);
        etVehicleNumber = findViewById(R.id.etVehicleNumber);
        etDriverName = findViewById(R.id.etDriverName);
        addDriverDetailsParentConstrainLayout = findViewById(R.id.addDriverDetailsParentConstrainLayout);
        btnAddVehicle = findViewById(R.id.btnAddVehicle);
        recyclerView = findViewById(R.id.recyclerView);
        //addVehicleCountryAutoCompleteTextView = findViewById(R.id.addVehicleCountryAutoCompleteTextView);
        /**
         * New driver fields
         */
        /**/
        driverPasswordTextInputLayout = findViewById(R.id.driverPasswordTextInputLayout);
        driverConTextInputLayout = findViewById(R.id.driverConTextInputLayout);
    }

    private String getAddVehicleCountryAutoCompleteTextView() {
        return selectedCountry;
    }

    /**/

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
        adapter = new UploadDocumentAdapter(this, uploadDocumentActionInfos, screenWidth, screenHeight);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                selectedDocumentsListPosition = position;
                DocumentStatus documentStatus =uploadDocumentActionInfos.get(position).getDocumentStatus();
                if (documentStatus.getAllowUpdate() != 0) {
                    selectedDocumetnsListItem = uploadDocumentActionInfos.get(position);
                    String filePath = uploadDocumentActionInfos.get(position).getFilePath();
                    if (!TextUtils.isEmpty(filePath)) {
                        utility.showConformationDialog(AddVehicleActivity.this,
                                "Are you sure you want to delete this file", (DialogInterface.OnClickListener) (dialog, which) -> {
                                    uploadDocumentActionInfos.get(position).setFilePath(null);
                                    adapter.notifyDataSetChanged();
                                    //updateUploadContentButtonUI();
                                });
                    } else {
                        String[] supportFormat = selectedDocumetnsListItem.getSuportedFormats().toArray(new String[0]);
                        showImageFromGalary(supportFormat, utility.parseDouble(selectedDocumetnsListItem.getMaxSize()));
                    }
                } else {
                    utility.showToastMsg("Not allowed to update this");
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        /*compositeDisposable.add(RxTextView.textChanges(addVehicleCountryAutoCompleteTextView)
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
                .observeOn(AndroidSchedulers.mainThread()).subscribe());*/
        btnAddVehicle.setOnClickListener(view -> driverInformationAndVehicleInformationSendToServer());
        //fetchAndUpdateCountryListFromDataBase();
        addVehicleViewModel.fetchDocumentStatusVehicleRequest();
    }

    /**
     * This method to show Image Galary from external storage.
     */
    private void showImageFromGalary(String[] supportedFileType, double fileSize) {
        String [] storage = permissionManager.getStorageReadAndWrightPermission();
        //int totalPermission = storage.length;
        if (permissionManager.initPermissionDialog(this, storage)) {
            showImagePickerScreen(supportedFileType, fileSize);
        }
    }

    private void showImagePickerScreen(String[] supportedFileType, double fileSize) {
        if (0 == supportedFileType.length) {
            FilePickerBuilder.getInstance()
                    .setActivityTitle("Please select image")
                    .enableVideoPicker(false)
                    .enableCameraSupport(true)
                    .showGifs(false)
                    .showFolderView(true)
                    .enableSelectAll(false)
                    .enableImagePicker(true)
                    .setMaxCount(1)
                    .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .pickPhoto(this, IMAGE_PICKER);
        } else {
            FilePickerBuilder.getInstance()
                    .setActivityTitle("Please select file")
                    .enableVideoPicker(false)
                    .enableCameraSupport(true)
                    .showGifs(false)
                    .showFolderView(true)
                    .enableSelectAll(false)
                    .enableImagePicker(false)
                    .setMaxCount(1)
                    .enableDocSupport(false)
                    .fileSize(fileSize)
                    .addFileSupport(supportedFileType)
                    .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .pickFile(this, FILE_PICKER);
        }
    }

    private void vehicleTypeSpinnerUI(List<VehicleTypesItem> vehicleTypesItems) {
        ArrayAdapter<VehicleTypesItem> adapter = new ArrayAdapter<VehicleTypesItem>(this, android.R.layout.simple_spinner_item, vehicleTypesItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleTypeAppCompatSpinner.setAdapter(adapter);
    }

    private void updateDocumentListUI(ApiResponse apiResponse) {
        int count = 0;
        uploadDocumentActionInfos.clear();
        //availableVehiclesItems.clear();
        if (apiResponse.isValidResponse()) {
            try {
                JSONObject jsonObject = new JSONObject(apiResponse.data.toString());
                DocumentStatusResponse documentStatusResponse = new Gson().fromJson(apiResponse.data, DocumentStatusResponse.class);
                if (null != documentStatusResponse) {
                    for (DocumentsItem documentsItem: documentStatusResponse.getDocuments()) {
                        documentsItem.parseDocumentStatus(jsonObject);
                    }
                }
                /*if (null != documentStatusResponse && null != documentStatusResponse.getAvailableVehicles()
                        && !documentStatusResponse.getAvailableVehicles().isEmpty()) {
                    availableVehiclesItems.addAll(documentStatusResponse.getAvailableVehicles());
                } else {
                    selectVehicleRelativeLayout.setVisibility(View.GONE);
                    selectVehicleDivider.setVisibility(View.GONE);
                }*/
                uploadDocumentActionInfos.addAll(documentStatusResponse.getDocuments());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * This method to fetch country list from data base and update in country auto complete UI.
     */
    /*private void fetchAndUpdateCountryListFromDataBase() {
        compositeDisposable.add(DatabaseClient.getInstance(this).getAppDatabase().countryDao().getCountryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    CrashlyticsHelper.d(" Country List data size is: " + result.size());
                    if (!result.isEmpty()) {
                        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, result);
                        //addVehicleCountryAutoCompleteTextView.setAdapter(arrayAdapter);
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
    }*/

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
                        //fetchAndUpdateCountryListFromDataBase();
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

    private AddVehicleRequest createAddVehicleRequest(String driverId) {
        AddVehicleRequest addVehicleRequest = new AddVehicleRequest();
        addVehicleRequest.setAvailableDriverId(driverId);
        addVehicleRequest.setVehicleName(getEtVehicleName());
        addVehicleRequest.setVehicleNumber(getEtVehicleNumber());
        addVehicleRequest.setVehicleTypeId(getVehicleTypeId());
        /*addVehicleRequest.setDriverFirstName(getEtDriverFirstName());
        addVehicleRequest.setDriverLastName(getEtDriverLastName());
        addVehicleRequest.setEmailId(getEtDriverEmail());
        addVehicleRequest.setMobileNumber(getEtDriverContNum());
        addVehicleRequest.setPassword(getEtDriverPassword());*/
        if (null != countriesItem) {
            addVehicleRequest.setCountryId(countriesItem.getCountryId());
        }
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
        }
    }

    private void clear() {
        selectedCountry = null;
        availableDriversItem = null;
        etVehicleName.setText(null);
        etVehicleNumber.setText(null);
        etDriverName.setText(null);
        //addVehicleCountryAutoCompleteTextView.setText(null);
    }

    /**
     * This method to update DocumetnsListItem and update adapter UI.
     *
     * @param path image path
     */
    private void updateDocumentItem(@NonNull String path) {
        if (null != selectedDocumetnsListItem) {
            selectedDocumetnsListItem.setFilePath(path);
            adapter.notifyItemChanged(selectedDocumentsListPosition);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER || requestCode == FILE_PICKER && resultCode == Activity.RESULT_OK) {
            List<String> filePath = data.getStringArrayListExtra(requestCode == IMAGE_PICKER ?
                    FilePickerConst.KEY_SELECTED_MEDIA : FilePickerConst.KEY_SELECTED_DOCS);
            //Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
            //.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
            if (null != filePath && !filePath.isEmpty()) {
                Log.d(TAG, " The image file path:" + filePath);
                updateDocumentItem(filePath.get(0));
                //updateUploadContentButtonUI();
            }
        } else {
            utility.showToastMsg("File not found");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddVehicle) {
            /*Intent intent = new Intent(this, OnlineActivity.class);
            startActivity(intent);*/
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionManager.PERMISSIONS_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                String[] supportFile = selectedDocumetnsListItem.getSuportedFormats().toArray(new String[0]);
                showImagePickerScreen(supportFile, utility.parseDouble(selectedDocumetnsListItem.getMaxSize()));
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                permissionManager.showRequestPermissionDialog(this, permissions, new PermissionManager.PermissionAskListener() {
                    @Override
                    public void onNeedPermission() {

                    }

                    @Override
                    public void onPermissionPreviouslyDenied(String permission) {
                        permissionManager.showPermissionNeededDialog(AddVehicleActivity.this,
                                getString(R.string.storage_picker), (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                });
                    }

                    @Override
                    public void onPermissionGranted() {

                    }
                });
            }

        }
    }
}
