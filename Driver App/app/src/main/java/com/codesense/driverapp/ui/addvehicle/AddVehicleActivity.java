package com.codesense.driverapp.ui.addvehicle;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import com.codesense.driverapp.BuildConfig;
import com.codesense.driverapp.R;
import com.codesense.driverapp.data.AvailableDriversItem;
import com.codesense.driverapp.data.AvailableDriversResponse;
import com.codesense.driverapp.data.CountriesItem;
import com.codesense.driverapp.data.DocumentStatus;
import com.codesense.driverapp.data.DocumentStatusResponse;
import com.codesense.driverapp.data.DocumentsItem;
import com.codesense.driverapp.data.VehicleDetailRequest;
import com.codesense.driverapp.data.VehicleTypeResponse;
import com.codesense.driverapp.data.VehicleTypesItem;
import com.codesense.driverapp.data.VehiclesListItem;
import com.codesense.driverapp.di.utils.PermissionManager;
import com.codesense.driverapp.di.utils.RecyclerTouchListener;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;
import com.codesense.driverapp.ui.camera.CameraActivity;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.codesense.driverapp.ui.helper.Utils;
import com.codesense.driverapp.ui.uploaddocument.UploadDocumentAdapter;
import com.codesense.driverapp.ui.vehicle.VehicleListActivity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.library.fileimagepicker.filepicker.FilePickerBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class AddVehicleActivity extends DrawerActivity implements View.OnClickListener {

    public static final String VEHICLES_LIST_ITEM_ARG = "VehiclesListItemArg";

    private static final int IMAGE_PICKER = 0x0001;
    private static final int FILE_PICKER = 0x0002;
    public static final int RESULT = 0x0003;

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
    private VehiclesListItem vehiclesListItem;

    /**
     * This method to start AddVehicleActivity
     * @param context
     */
    public static void start(Context context) {
        context.startActivity(new Intent(context, AddVehicleActivity.class));
    }

    public static Intent findStartIntent(Context context, VehiclesListItem driversListItem) {
        Intent starter = new Intent(context, AddVehicleActivity.class);
        starter.putExtra(VEHICLES_LIST_ITEM_ARG, driversListItem);
        return starter;
    }
    public static void start(Context context, VehiclesListItem vehiclesListItem) {
        Intent intent = new Intent(context, AddVehicleActivity.class);
        intent.putExtra(VEHICLES_LIST_ITEM_ARG, vehiclesListItem);
        context.startActivity(intent);
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
        initializeListener();
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
                        /*int driverId = apiResponse.getResponseJsonObject().optInt(Constant.DRIVER_ID, 0);
                        addVehicleViewModel.addVehicleToOwnerRequest(createAddVehicleRequest(String.valueOf(driverId)));*/
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
                } else if (ServiceType.UPLOAD_OWNER_VEHICLE == serviceType) {
                    if (apiResponse.isValidResponse()) {
                        utility.showToastMsg("All file are uploaded successfully");
                        clearAndUpdateDocumentListUI();
                        clear();
                        VehicleListActivity.start(this);
                    } else {
                        utility.showToastMsg(apiResponse.getResponseMessage());
                    }
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
                    /*JsonElement countryListJsonElement = jsonElement[COUNTRY_LIST];
                    if (null != countryListJsonElement) {
                        deleteAllCountriesFromDataBase(countryListJsonElement);
                    }
                    JsonElement cityListJsonElement = jsonElement[CITY_LIST];
                    if (null != cityListJsonElement) {
                        //Update available drivers UI.
                        deleteAllCitiesFromDataBase(cityListJsonElement);
                    }*/
                } else if (ServiceType.GET_DOCUMENTS_STATUS_VEHICLE_AND_VEHICLE_TYPES == serviceType) {
                    //Update status ui and vahile type UI
                    JsonElement[] jsonElement = apiResponse.datas;
                    final int GET_DOCUMENT_STATUS = 0;
                    final int VEHICLE_TYPE = 1;
                    JsonElement documentStatus = jsonElement[GET_DOCUMENT_STATUS];
                    if (null != documentStatus) {
                        updateDocumentListUI(documentStatus);
                    }
                    JsonElement vehicleTypeJsonElement = jsonElement[VEHICLE_TYPE];
                    if (null != vehicleTypeJsonElement) {
                        VehicleTypeResponse vehicleTypeResponse = new Gson().fromJson(vehicleTypeJsonElement, VehicleTypeResponse.class);
                        if (null != vehicleTypeResponse && null != vehicleTypeResponse.getVehicleTypes()) {
                            vehicleTypeSpinnerUI(vehicleTypeResponse.getVehicleTypes());
                        }
                    }
                } else if (ServiceType.UPLOAD_OWNER_VEHICLE == serviceType) {
                    //update list screen and clear fields
                    JsonElement[] jsonElements = apiResponse.datas;
                    boolean allAreSuccess = true;
                    int count = 0;
                    do {
                        if (!apiResponse.isValidResponse(count)) {
                            utility.showToastMsg(apiResponse.getResponseMessage(count));
                            allAreSuccess = false;
                        }
                    } while (++ count < jsonElements.length);
                    if (allAreSuccess) {
                        VehicleListActivity.start(this);
                        utility.showToastMsg("All file are uploaded successfully");
                    }
                    clearAndUpdateDocumentListUI();
                    clear();
                }
                break;
            case ERROR:
                utility.dismissDialog();
                break;
        }
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
        driverPasswordTextInputLayout = findViewById(R.id.driverPasswordTextInputLayout);
        driverConTextInputLayout = findViewById(R.id.driverConTextInputLayout);
    }

    private void updateUI(List<VehicleTypesItem> vehicleTypesItems) {
        etVehicleName.setText(vehiclesListItem.getVehicleName());
        etVehicleNumber.setText(vehiclesListItem.getVehicleNumber());
        int selected = 0;
        for (int index=0; index<vehicleTypesItems.size(); index++) {
            VehicleTypesItem vehicleTypesItem = vehicleTypesItems.get(index);
            if (vehicleTypesItem.getVehicleTypeId().equals(vehiclesListItem.getVehicleTypeId())) {
                selected = index;
                break;
            }
        }
        vehicleTypeAppCompatSpinner.setSelection(selected);
    }

    private String getAddVehicleCountryAutoCompleteTextView() {
        return selectedCountry;
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
    }

    private boolean doesDriverDetailsLayoutExpanded() {
        return addDriverDetailsParentConstrainLayout.getVisibility() == View.VISIBLE;
    }

    private void initializeListener() {
        btnAddVehicle.setOnClickListener(this);
    }

    private void clearListener() {
        btnAddVehicle.setOnClickListener(null);
    }

    private void functionality() {
        Intent intent = getIntent();
        if (null != intent) {
            vehiclesListItem = intent.getParcelableExtra(VEHICLES_LIST_ITEM_ARG);
        }
        adapter = new UploadDocumentAdapter(this, uploadDocumentActionInfos, screenWidth, screenHeight,
                isEditVehicle() ? "edit" : null);
        if (isEditVehicle()){
            titleTextView.setText(getResources().getString(R.string.update_vehicle_title));
        }
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                selectedDocumentsListPosition = position;
                DocumentStatus documentStatus =uploadDocumentActionInfos.get(position).getDocumentStatus();
                if (documentStatus.getAllowUpdate() != 0) {
                    selectedDocumetnsListItem = uploadDocumentActionInfos.get(position);
                    String filePath = uploadDocumentActionInfos.get(position).getFileName();
                    if (!TextUtils.isEmpty(filePath)) {
                        utility.showConformationDialog(AddVehicleActivity.this,
                                "Are you sure you want to delete this file", (DialogInterface.OnClickListener) (dialog, which) -> {
                                    //uploadDocumentActionInfos.get(position).setFileName(null);
                                    uploadDocumentActionInfos.get(position).setFileName(null);
                                    uploadDocumentActionInfos.get(position).setSelectedFileUri(null);
                                    uploadDocumentActionInfos.get(position).setImageFile(false);
                                    adapter.notifyDataSetChanged();
                                    //updateUploadContentButtonUI();
                                });
                    } else {
                        utility.showListDialog(AddVehicleActivity.this, utility.selectSourceOption(),
                                "Select Source", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            startActivityForResult(new Intent(AddVehicleActivity.this, CameraActivity.class),
                                                    CameraActivity.REQUEST_CAMERA_ACTIVITY);
                                        } else if (which == 1) {
                                            pickImage();
                                        } else if (which == 2) {
                                            pickFile();
                                        }
                                        /*else if (which == 1) {
                                            String[] supportFormat = selectedDocumetnsListItem.getSuportedFormats().toArray(new String[0]);
                                            showImageFromGalary(supportFormat, utility.parseDouble(selectedDocumetnsListItem.getMaxSize()));
                                        }*/
                                    }
                                });
                    }
                } else {
                    utility.showToastMsg("Not allowed to update this");
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        if (isEditVehicle()){
            addVehicleViewModel.fetchVehicleTypesAndDocumentStatusVehicleRequestRequestEdit(vehiclesListItem.getVehicleId());
        }else {
            addVehicleViewModel.fetchVehicleTypesAndDocumentStatusVehicleRequestRequest();
        }
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
                    .enableSelectAll(true)
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
                    .enableSelectAll(true)
                    .enableImagePicker(false)
                    .setMaxCount(1)
                    .enableDocSupport(false)
                    .addFileSupport(supportedFileType)
                    .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .pickFile(this, FILE_PICKER);
        }
    }

    private void vehicleTypeSpinnerUI(List<VehicleTypesItem> vehicleTypesItems) {
        ArrayAdapter<VehicleTypesItem> adapter = new ArrayAdapter<VehicleTypesItem>(this, android.R.layout.simple_spinner_item, vehicleTypesItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleTypeAppCompatSpinner.setAdapter(adapter);
        if (null != this.vehiclesListItem) {
            updateUI(vehicleTypesItems);
        }
    }

    private void updateDocumentListUI(ApiResponse apiResponse) {
        if (apiResponse.isValidResponse()) {
            updateDocumentListUI(apiResponse.data);
        }
    }

    private void updateDocumentListUI(JsonElement jsonElement) {
        uploadDocumentActionInfos.clear();
        if (null != jsonElement) {
            try {
                JSONObject jsonObject = new JSONObject(jsonElement.toString());
                DocumentStatusResponse documentStatusResponse = new Gson().fromJson(jsonElement, DocumentStatusResponse.class);
                if (null != documentStatusResponse && null != documentStatusResponse.getDocuments()) {
                    for (DocumentsItem documentsItem: documentStatusResponse.getDocuments()) {
                        documentsItem.parseDocumentStatus(jsonObject);
                    }
                } else {
                    //For testing parse static value
                    String staticResult = utility.findAssetFileString(this, Utility.GET_DOCUMENT_STATUS_VEHICLE);
                    jsonObject = new JSONObject(staticResult);
                    documentStatusResponse = new Gson().fromJson(staticResult, DocumentStatusResponse.class);
                    if (null != documentStatusResponse) {
                        for (DocumentsItem documentsItem : documentStatusResponse.getDocuments()) {
                            documentsItem.parseDocumentStatus(jsonObject);
                        }
                    }
                }
                uploadDocumentActionInfos.addAll(documentStatusResponse.getDocuments());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * This method to remove all selected files and update DocumentList UI.
     */
    private void clearAndUpdateDocumentListUI() {
        for (DocumentsItem documentsListItem: uploadDocumentActionInfos)
            documentsListItem.setFileName(null);
        adapter.notifyDataSetChanged();
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
            selectedDocumetnsListItem.setFileName(path);
            adapter.notifyItemChanged(selectedDocumentsListPosition);
        }
    }

    private void updateDocumentItem(@NonNull Uri uri) {
        if (null != selectedDocumetnsListItem) {
            String[] types = {"jpeg", "JPEG", "jpg", "JPG", "png", "png"};
            String extension = getContentResolver().getType(uri);
            String name = Utils.getFileName(this, uri);
            boolean isImageFile = false;
            if (!TextUtils.isEmpty(extension)) {
                isImageFile = !extension.contains(types[0]) ?
                        !extension.contains(types[1]) ? !extension.contains(types[2]) ?
                                !extension.contains(types[3]) ? !extension.contains(types[4]) ?
                                        extension.contains(types[5]) : true : true: true: true: true;
            } else {
                String[] array = name.split("\\.");
                if (array.length > 1) {
                    String fileExe = array[array.length - 1];
                    isImageFile = Arrays.asList(types).contains(fileExe);
                }
            }
            selectedDocumetnsListItem.setFileName(name);
            selectedDocumetnsListItem.setSelectedFileUri(uri);
            selectedDocumetnsListItem.setImageFile(isImageFile);
            adapter.notifyItemChanged(selectedDocumentsListPosition);
        }
    }

    /**
     * This method used for validate to check all Mandatory files user choose or not
     * @return TRUE/FALSE {@code true} if all Mandatory file choose otherwise it will return {@code false}
     */
    private boolean isValiedAllSelected() {
        boolean isValied = false;
        for (DocumentsItem documentsListItem: uploadDocumentActionInfos) {
            if (documentsListItem.getIsMandatory() == 1 && documentsListItem.getDocumentStatus().getAllowUpdate() == 1) {
                if (!TextUtils.isEmpty(documentsListItem.getFileName())) {
                    isValied = true;
                } else {
                    utility.showToastMsg("Select " + documentsListItem.getName());
                    return false;
                }
            }
        }
        return isValied;
    }

    /**
     * This method to find selected document file list.
     * @return List DocumentsListItem.
     */
    private List<DocumentsItem> findSelectedDocumentList() {
        List<DocumentsItem> documentsListItems = new ArrayList<>();
        int count = 0;
        do {
            DocumentsItem documentsListItem = uploadDocumentActionInfos.get(count);
            if (!TextUtils.isEmpty(documentsListItem.getFileName())) {
                documentsListItems.add(documentsListItem);
            }
        } while (++ count < this.uploadDocumentActionInfos.size());
        return documentsListItems;
    }

    /**
     * This method to create VehicleDetailRequest Object.
     * @return VehicleDetailRequest
     */
    private VehicleDetailRequest createVehicleDetailRequestObject() {
        VehicleDetailRequest vehicleDetailRequest = new VehicleDetailRequest();
        vehicleDetailRequest.setVehicleTypeId(getVehicleTypeId());
        vehicleDetailRequest.setVehicleName(getEtVehicleName());
        vehicleDetailRequest.setVehicleNumber(getEtVehicleNumber());
        vehicleDetailRequest.setVehicleId(getVehicleTypeId());
        if (isEditVehicle()) {
            vehicleDetailRequest.setVehicleId(vehiclesListItem.getVehicleId());
        }
        return vehicleDetailRequest;
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeType = {"image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType);
        startActivityForResult(intent, FILE_PICKER);
    }

    private void pickFile() {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        String[] mimeType = {"application/pdf"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType);
        startActivityForResult(intent, FILE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_PICKER && resultCode == Activity.RESULT_OK) {
            //data.getData return the content URI for the selected Image
            Uri uri = data.getData();
            if (null != uri) {
                if (BuildConfig.DEBUG) Log.d(TAG, " The image file path:" + uri);
                updateDocumentItem(uri);
                //updateUploadContentButtonUI();
            }
        } else if (requestCode == CameraActivity.REQUEST_CAMERA_ACTIVITY && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(CameraActivity.CAPTURE_CAMERA_IMAGE);
            if (null != filePath && !filePath.isEmpty()) {
                Log.d(TAG, " The image file path:" + filePath);
                updateDocumentItem(Uri.fromFile(new File(filePath)));
                //updateDocumentItem(filePath);
                //updateUploadContentButtonUI();
            }
        } else {
            utility.showToastMsg("File not found");
        }
        /*if (requestCode == IMAGE_PICKER || requestCode == FILE_PICKER && resultCode == Activity.RESULT_OK) {
            List<String> filePath = data.getStringArrayListExtra(requestCode == IMAGE_PICKER ?
                    FilePickerConst.KEY_SELECTED_MEDIA : FilePickerConst.KEY_SELECTED_DOCS);
            if (null != filePath && !filePath.isEmpty()) {
                Log.d(TAG, " The image file path:" + filePath);
                updateDocumentItem(filePath.get(0));
            }
        }*/
    }

    private boolean isEditVehicle() {
        return this.vehiclesListItem != null;
    }

    /**
     * When clear instance for this activity we will receive this callback
     */
    @Override
    protected void onDestroy() {
        clearListener();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddVehicle) {
            if (isValidVehicleFields()) {
                if (isEditVehicle()) {
                   /* utility.showConformationDialog(AddVehicleActivity.this,
                            "Confirmation", (dialog, which) ->
                            {*/
                                addVehicleViewModel.uploadDocumentRequest(findSelectedDocumentList(), createVehicleDetailRequestObject());
                           /* });*/
                } else if (isValiedAllSelected()) {
                    addVehicleViewModel.uploadDocumentRequest(findSelectedDocumentList(), createVehicleDetailRequestObject());
                }
            }
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
