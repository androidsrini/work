package com.codesense.driverapp.ui.uploadDriver;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.codesense.driverapp.R;
import com.codesense.driverapp.data.CountriesItem;
import com.codesense.driverapp.data.Data;
import com.codesense.driverapp.data.DocumentStatus;
import com.codesense.driverapp.data.DocumentStatusResponse;
import com.codesense.driverapp.data.DocumentsItem;
import com.codesense.driverapp.data.DriversListItem;
import com.codesense.driverapp.data.VehiclesListsItem;
import com.codesense.driverapp.di.utils.AppSpinnerViewGroup;
import com.codesense.driverapp.di.utils.PermissionManager;
import com.codesense.driverapp.di.utils.RecyclerTouchListener;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;
import com.codesense.driverapp.ui.adddriver.AddDriverActivity;
import com.codesense.driverapp.ui.adddriver.DriverViewModel;
import com.codesense.driverapp.ui.camera.CameraActivity;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.codesense.driverapp.ui.driver.DriverListActivity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.library.fileimagepicker.filepicker.FilePickerBuilder;
import com.library.fileimagepicker.filepicker.FilePickerConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class UploadDocumentDriverActivity extends DrawerActivity {


    private static final String TAG = AddDriverActivity.class.getSimpleName();
    public static final String DRIVERS_LIST_ITEM_ARG = "DriversListItemArg";
    private static final int IMAGE_PICKER = 0x0001;
    private static final int FILE_PICKER = 0x0002;
    EditText etDriverFirstName, etDriverLastName, etDriverContNum, etDriverEmail, etDriverPassword, etDriverConPassword, inviteCodeEditText;
    RecyclerView recyclerView;
    Button btnAddDriver;
    AutoCompleteTextView countryAutoCompleteTextView;
    AppSpinnerViewGroup<VehiclesListsItem> vehicleAppSpinnerViewGroup;
    UploadDocumentDriverAdapter adapter;
    private List<DocumentsItem> uploadDocumentActionInfos;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
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
    private CountriesItem countriesItem;
    private DriversListItem driversListItem;
    private Data data;
    private List<CountriesItem> countriesItemList;
    private TextInputLayout inviteCodeTextInputLayout;
    private CheckedTextView changePasswordCheckedTextView;
    private LinearLayout passwordContainerLinearLayout;
    String driverId;
    String from;

    public static void start(Context context, String driverId) {
        Intent starter = new Intent(context, UploadDocumentDriverActivity.class);
        starter.putExtra("driver_id", driverId);
        starter.putExtra("from", "upload");
        context.startActivity(starter);
    }

    public static void start(Context context, String driverId,String from) {
        Intent starter = new Intent(context, UploadDocumentDriverActivity.class);
        starter.putExtra("driver_id", driverId);
        starter.putExtra("from", from);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_add_driver);*/
        View view = LayoutInflater.from(this).inflate(R.layout.activity_upload_document_driver, null);
        frameLayout.addView(view);
        titleTextView.setText(getResources().getString(R.string.upload_document_driver));

        initially(view);
        functionality();
    }

    private void initially(View view) {
        uploadDocumentActionInfos = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        btnAddDriver = view.findViewById(R.id.btnUploadDriver);
        inviteCodeEditText = view.findViewById(R.id.invite_code_editText);
        countryAutoCompleteTextView = view.findViewById(R.id.country_autoCompleteTextView);
        inviteCodeTextInputLayout = view.findViewById(R.id.invite_code_textInputLayout);
        changePasswordCheckedTextView = view.findViewById(R.id.change_password_checkedTextView);
        passwordContainerLinearLayout = view.findViewById(R.id.password_container_linearLayout);
        driverViewModel.getApiResponseMutableLiveData().observe(this, this::apiResponseHandler);

        appSharedPreference.saveAccessToken(appSharedPreference.getAccessTokenKey());

    }

    private boolean isEditDriver() {
        return null != driversListItem;
    }

    private void functionality() {
        Intent intent = getIntent();
        if (null != intent) {
            //Fetch driver details from server.
            driverId = intent.getStringExtra("driver_id");
            from = intent.getStringExtra("from");
        }

        if ("edit".equalsIgnoreCase(from)){
            titleTextView.setText(getResources().getString(R.string.update_document_driver));
            driverViewModel.fetchDocumentStatusDriverRequest(driverId);
        }else{
            driverViewModel.fetchVehiclesListAndDocumentStatusRequest();
        }

        adapter = new UploadDocumentDriverAdapter(this, uploadDocumentActionInfos, screenWidth, screenHeight,from);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                selectedDocumentsListPosition = position;
                DocumentStatus documentStatus = uploadDocumentActionInfos.get(position).getDocumentStatus();
/*
                if (documentStatus.getAllowUpdate() != 0) {
*/
                    selectedDocumetnsListItem = uploadDocumentActionInfos.get(position);
                    String filePath = uploadDocumentActionInfos.get(position).getFileName();
                if (!TextUtils.isEmpty(filePath)) {
                    utility.showConformationDialog(UploadDocumentDriverActivity.this,
                            "Are you sure you want to delete this file", (DialogInterface.OnClickListener) (dialog, which) -> {
                                uploadDocumentActionInfos.get(position).setFileName(null);
                                adapter.notifyDataSetChanged();
                            });
                } else {
                    utility.showListDialog(UploadDocumentDriverActivity.this, utility.selectSourceOption(), "Select Source", (dialog, which) -> {
                        if (which == 0) {
                            //CameraActivity.start(UploadDocumentActivity.this);
                            startActivityForResult(new Intent(UploadDocumentDriverActivity.this, CameraActivity.class),
                                    CameraActivity.REQUEST_CAMERA_ACTIVITY);
                        } else {
                            String[] supportFormat = selectedDocumetnsListItem.getSuportedFormats().toArray(new String[0]);
                            showImageFromGalary(supportFormat, utility.parseDouble(selectedDocumetnsListItem.getMaxSize()));
                        }
                    });
                }/*else {
                    String filePath = uploadDocumentActionInfos.get(position).getFileName();
                    if (!TextUtils.isEmpty(filePath)) {
                        utility.showConformationDialog(UploadDocumentDriverActivity.this,
                                "Are you sure you want to delete this file", (DialogInterface.OnClickListener) (dialog, which) -> {
                                    uploadDocumentActionInfos.get(position).setFileName(null);
                                    adapter.notifyDataSetChanged();
                                    //updateUploadContentButtonUI();
                                });
                    } else {
>>>>>>> 8e22bbe2acaf43c1895cfb5ac80bd0560d7cdac5
                        String[] supportFormat = selectedDocumetnsListItem.getSuportedFormats().toArray(new String[0]);
                        showImageFromGalary(supportFormat, utility.parseDouble(selectedDocumetnsListItem.getMaxSize()));
                    }*/
                /*} else {
                    utility.showToastMsg("Not allowed to update this");
                }*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        btnAddDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driverViewModel.uploadDocumentRequest(findSelectedDocumentList(), driverId);
            }
        });
    }


    private List<DocumentsItem> findSelectedDocumentList() {
        List<DocumentsItem> documentsListItems = new ArrayList<>();
        int count = 0;
        do {
            DocumentsItem documentsListItem = uploadDocumentActionInfos.get(count);
            if (!TextUtils.isEmpty(documentsListItem.getFileName())) {
                documentsListItems.add(documentsListItem);
            }
        } while (++count < this.uploadDocumentActionInfos.size());
        return documentsListItems;
    }

    private boolean isValiedAllSelected() {
        boolean isValied = false;
        for (DocumentsItem documentsListItem : uploadDocumentActionInfos) {
            if (documentsListItem.getIsMandatory() == 1 && documentsListItem.getDocumentStatus().getAllowUpdate() == 1) {
                if (!TextUtils.isEmpty(documentsListItem.getFileName())) {
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
        String[] storage = permissionManager.getStorageReadAndWrightPermission();
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

    private void apiResponseHandler(ApiResponse apiResponse) {
        ServiceType serviceType = apiResponse.getServiceType();
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                /*if (apiResponse.isValidResponse()) {
                    vehicleAppSpinnerViewGroup.setVisibility(View.VISIBLE);
                }*/
               if (ServiceType.UPDATE_DRIVER_DOCUMENTS == serviceType){
                        if (apiResponse.isValidResponse()) {
                            currentPosition = -1;
                            DriverListActivity.start(this);
                            utility.showToastMsg("All file are uploaded successfully");
                            clearAndUpdateDocumentListUI();
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
                        } while (++count < jsonElements.length);
                        if (allAreSuccess) {
                            utility.showToastMsg("All file are uploaded successfully");
                        }
                        currentPosition = -1;
                        DriverListActivity.start(this);
                        clearAndUpdateDocumentListUI();
                        /*clearAllEditTextUI();*/
                        break;
                }
                break;
            case ERROR:
                utility.dismissDialog();
                break;
        }
    }


    /**
     * This method to remove all selected files and update DocumentList UI.
     */
    private void clearAndUpdateDocumentListUI() {
        for (DocumentsItem documentsListItem : uploadDocumentActionInfos)
            documentsListItem.setFileName(null);
        adapter.notifyDataSetChanged();
    }

    private void updateDocumentListUI(JsonElement jsonElement) {
        uploadDocumentActionInfos.clear();
        if (null != jsonElement) {
            try {
                JSONObject jsonObject = new JSONObject(jsonElement.toString());
                DocumentStatusResponse documentStatusResponse = new Gson().fromJson(jsonElement, DocumentStatusResponse.class);
                if (null != documentStatusResponse) {
                    for (DocumentsItem documentsItem : documentStatusResponse.getDocuments()) {
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
        }else if (requestCode == CameraActivity.REQUEST_CAMERA_ACTIVITY && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(CameraActivity.CAPTURE_CAMERA_IMAGE);
            if (null != filePath && !filePath.isEmpty()) {
                Log.d(TAG, " The image file path:" + filePath);
                updateDocumentItem(filePath);
                //updateUploadContentButtonUI();
            }
        }  else {
            utility.showToastMsg("File not found");
        }
    }

    private void updateDocumentItem(@NonNull String path) {
        if (null != selectedDocumetnsListItem) {
            selectedDocumetnsListItem.setFileName(path);
            adapter.notifyItemChanged(selectedDocumentsListPosition);
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
                        permissionManager.showPermissionNeededDialog(UploadDocumentDriverActivity.this,
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
