package com.codesense.driverapp.ui.adddriver;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codesense.driverapp.R;
import com.codesense.driverapp.data.AddDriverRequest;
import com.codesense.driverapp.data.DocumentStatus;
import com.codesense.driverapp.data.DocumentStatusResponse;
import com.codesense.driverapp.data.DocumentsItem;
import com.codesense.driverapp.data.VehiclesListsItem;
import com.codesense.driverapp.data.VehiclesListsResponse;
import com.codesense.driverapp.di.utils.AppSpinnerViewGroup;
import com.codesense.driverapp.di.utils.PermissionManager;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.codesense.driverapp.ui.uploaddocument.RecyclerTouchListener;
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

public class AddDriverActivity extends DrawerActivity {

    private static final String TAG = AddDriverActivity.class.getSimpleName();
    private static final int IMAGE_PICKER = 0x0001;
    private static final int FILE_PICKER = 0x0002;
    EditText etDriverName, etDriverContNum, etDriverEmail, etDriverPassword, etDriverConPassword, inviteCodeEditText;
    RecyclerView recyclerView;
    Button btnAddDriver;
    AppSpinnerViewGroup<VehiclesListsItem> vehicleAppSpinnerViewGroup;
    UploadDocumentAdapter adapter;
    private List<DocumentsItem> uploadDocumentActionInfos;
    @Inject
    Utility utility;
    @Inject
    AppSharedPreference appSharedPreference;
    @Inject
    protected RequestHandler requestHandler;
    @Inject
    DriverViewModel driverViewModel;
    @Inject
    protected PermissionManager permissionManager;
    private int selectedDocumentsListPosition;
    private DocumentsItem selectedDocumetnsListItem;
    private String selectedVehicleId;

    public static void start(Context context) {
        Intent starter = new Intent(context, AddDriverActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_add_driver);*/
        View view = LayoutInflater.from(this).inflate(R.layout.activity_add_driver, null);
        frameLayout.addView(view);
        titleTextView.setText(getResources().getString(R.string.add_driver));
        //init views
        initially(view);
        functionality();
        driverViewModel.fetchVehiclesListAndDocumentStatusRequest();
    }

    private void initially(View view) {
        uploadDocumentActionInfos = new ArrayList<>();
        etDriverName = view.findViewById(R.id.etDriverName);
        etDriverContNum = view.findViewById(R.id.etDriverContNum);
        etDriverEmail = view.findViewById(R.id.etDriverEmail);
        etDriverPassword = view.findViewById(R.id.etDriverPassword);
        etDriverConPassword = view.findViewById(R.id.etDriverConPassword);
        vehicleAppSpinnerViewGroup = view.findViewById(R.id.vehicle_appSpinnerViewGroup);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnAddDriver = view.findViewById(R.id.btnAddDriver);
        inviteCodeEditText = view.findViewById(R.id.invite_code_editText);
        driverViewModel.getApiResponseMutableLiveData().observe(this, this::apiResponseHandler);
    }

    private void functionality() {
        vehicleAppSpinnerViewGroup.setOnItemSelectListener(position -> {
            List<VehiclesListsItem> arrayList = vehicleAppSpinnerViewGroup.getArrayList();
            if (null != arrayList && !arrayList.isEmpty())
                selectedVehicleId = arrayList.get(position).getVehicleId();
        });
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
                        utility.showConformationDialog(AddDriverActivity.this,
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
        btnAddDriver.setOnClickListener(v->{
            if (isValidAddNewDriverFields()) {
                if (isValiedAllSelected()) {
                    driverViewModel.addDriverRequest(createAddDriverRequestObject());
                }
            }
        });
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
            if (!TextUtils.isEmpty(documentsListItem.getFilePath())) {
                documentsListItems.add(documentsListItem);
            }
        } while (++ count < this.uploadDocumentActionInfos.size());
        return documentsListItems;
    }

    private boolean isValiedAllSelected() {
        boolean isValied = false;
        for (DocumentsItem documentsListItem: uploadDocumentActionInfos) {
            if (documentsListItem.getIsMandatory() == 1 && documentsListItem.getDocumentStatus().getAllowUpdate() == 1) {
                if (!TextUtils.isEmpty(documentsListItem.getFilePath())) {
                    isValied = true;
                } else {
                    utility.showToastMsg("Select " + documentsListItem.getName());
                    isValied = false;
                    return false;
                }
            }
        }
        return isValied;
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

    private void apiResponseHandler(ApiResponse apiResponse) {
        ServiceType serviceType = apiResponse.getServiceType();
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                Log.d(TAG, "response: " + apiResponse.data);
                /*if (apiResponse.isValidResponse()) {
                    vehicleAppSpinnerViewGroup.setVisibility(View.VISIBLE);
                }*/
                switch (serviceType) {
                    case ADD_DRIVER:
                        if (apiResponse.isValidResponse()) {
                            String driverId = apiResponse.getResponseJsonObject().optString("driver_id");
                            driverViewModel.uploadDocumentRequest(findSelectedDocumentList(), driverId);
                            clear();
                        } else {
                            utility.showToastMsg(apiResponse.getResponseMessage());
                        }
                        break;
                }
                break;
            case SUCCESS_MULTIPLE:
                utility.dismissDialog();
                switch (serviceType) {
                    case GET_DOCUMENTS_STATUS_DRIVER_AND_VEHICLE_LIST:
                        //Update list and spinner UI
                        JsonElement[] jsonElement = apiResponse.datas;
                        final int VEHICLE_LIST = 0;
                        final int DOCUMENT_STATUS = 1;
                        JsonElement vehicleTypeJsonElement = jsonElement[VEHICLE_LIST];
                        if (null != vehicleTypeJsonElement) {
                            VehiclesListsResponse vehiclesListsResponse = new Gson().fromJson(apiResponse.data, VehiclesListsResponse.class);
                            if (null != vehiclesListsResponse && null != vehiclesListsResponse.getVehiclesLists()) {
                                if (apiResponse.isValidResponse()) {
                                    vehicleAppSpinnerViewGroup.setVisibility(View.VISIBLE);
                                }
                                vehicleListSpinnerUI(vehiclesListsResponse.getVehiclesLists());
                            }
                        }
                        JsonElement documentStatus = jsonElement[DOCUMENT_STATUS];
                        if (null != documentStatus) {
                            updateDocumentListUI(documentStatus);
                        }
                        break;
                    case UPDATE_DRIVER_DOCUMENTS:
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
                            utility.showToastMsg("All file are uploaded successfully");
                        }
                        clearAndUpdateDocumentListUI();
                        /*clearAllEditTextUI();*/
                        break;
                }
                break;
            case ERROR:
                utility.dismissDialog();
                Log.d(TAG, "response error: " + apiResponse.error);
                break;
        }
    }

    /**
     * This method to remove all selected files and update DocumentList UI.
     */
    private void clearAndUpdateDocumentListUI() {
        for (DocumentsItem documentsListItem: uploadDocumentActionInfos)
            documentsListItem.setFilePath(null);
        adapter.notifyDataSetChanged();
    }

    private void updateDocumentListUI(JsonElement jsonElement) {
        uploadDocumentActionInfos.clear();
        if (null != jsonElement) {
            try {
                JSONObject jsonObject = new JSONObject(jsonElement.toString());
                DocumentStatusResponse documentStatusResponse = new Gson().fromJson(jsonElement, DocumentStatusResponse.class);
                if (null != documentStatusResponse) {
                    for (DocumentsItem documentsItem: documentStatusResponse.getDocuments()) {
                        documentsItem.parseDocumentStatus(jsonObject);
                    }
                }
                uploadDocumentActionInfos.addAll(documentStatusResponse.getDocuments());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void vehicleListSpinnerUI(List<VehiclesListsItem> vehiclesListsItemList) {
        vehicleAppSpinnerViewGroup.updateItem(vehiclesListsItemList);
    }

    private String getEtDriverName() {
        return etDriverName.getText().toString().trim();

    }

    private String getInviteCodeEditText() {
        return inviteCodeEditText.getText().toString().trim();
    }

    private String getSelectedVehicleId() {
        return selectedVehicleId;
    }

    /*private String getEtDriverLastName() {
        return etDriverLastName.getText().toString().trim();
    }*/

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

    private boolean isValidAddNewDriverFields() {
        boolean isValid = true;
        if (TextUtils.isEmpty(getEtDriverName())) {
            utility.showToastMsg("Driver Name Required");
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
        } /*else if (TextUtils.isEmpty(getAddVehicleCountryAutoCompleteTextView())) {
                utility.showToastMsg("Country required");
                isValid = false;
            }*/
        return isValid;
    }

        /*private void updateAvailableDriversUI(List<AvailableDriversItem> availableDriversItems) {
            CrashlyticsHelper.d("UpdateAvailableDriversUI");
            ArrayAdapter<AvailableDriversItem> adapter = new ArrayAdapter<AvailableDriversItem>(this, android.R.layout.simple_spinner_item, availableDriversItems);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            etDriverName.setAdapter(adapter);
            etDriverName.setOnItemClickListener((adapterView, view, i, l) -> {
                availableDriversItem = adapter.getItem(i);
                if (null != availableDriversItem) {
                    etDriverFirstName.setText(availableDriversItem.getDriverName());
                    etDriverLastName.setText(availableDriversItem.getDriverLastName());
                    etDriverEmail.setText(availableDriversItem.getDriverEmailId());
                *//*driverPasswordTextInputLayout.setVisibility(View.GONE);
                driverConTextInputLayout.setVisibility(View.GONE);*//*
                }
            });
        }*/

    private AddDriverRequest createAddDriverRequestObject() {
        AddDriverRequest addDriverRequest = new AddDriverRequest();
            /*if (null != countriesItem) {
                addDriverRequest.setCountryId(countriesItem.getCountryId());
            }*/
        addDriverRequest.setDriverName(getEtDriverName());
        addDriverRequest.setInviteCode(getInviteCodeEditText());
        /*addDriverRequest.setDriverLastName(getEtDriverLastName());*/
        addDriverRequest.setEmailId(getEtDriverEmail());
        addDriverRequest.setMobileNumber(getEtDriverContNum());
        addDriverRequest.setPassword(getEtDriverPassword());
        addDriverRequest.setUserId(appSharedPreference.getUserID());
        addDriverRequest.setVehicleId(getSelectedVehicleId());
        return addDriverRequest;
    }

    private void clear() {
        etDriverName.setText(null);
        //etDriverLastName.setText(null);
        etDriverContNum.setText(null);
        etDriverEmail.setText(null);
        etDriverPassword.setText(null);
        etDriverConPassword.setText(null);
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
            if (null != filePath && !filePath.isEmpty()) {
                Log.d(TAG, " The image file path:" + filePath);
                updateDocumentItem(filePath.get(0));
            }
        } else {
            utility.showToastMsg("File not found");
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
                        permissionManager.showPermissionNeededDialog(AddDriverActivity.this,
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
