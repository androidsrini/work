package com.codesense.driverapp.ui.documentstatus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.codesense.driverapp.R;
import com.codesense.driverapp.data.DocumentStatusResponse;
import com.codesense.driverapp.data.DocumentsItem;
import com.codesense.driverapp.data.DocumentsListItem;
import com.codesense.driverapp.data.DocumentsListStatusResponse;
import com.codesense.driverapp.data.VehicleDetailRequest;
import com.codesense.driverapp.data.VehicleDetails;
import com.codesense.driverapp.di.utils.PermissionManager;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.ServiceType;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.codesense.driverapp.ui.helper.CrashlyticsHelper;
import com.codesense.driverapp.ui.uploaddocument.UploadDocumentActivity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.library.fileimagepicker.filepicker.FilePickerBuilder;
import com.library.fileimagepicker.filepicker.FilePickerConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class  DocumentStatusActivity extends DrawerActivity implements View.OnClickListener {

    private static final int IMAGE_PICKER = 0x0001;
    //Button btnUpdate;
    protected RecyclerView recyclerView;
    protected Button uploadContentButton;
    DocumentStatusAdapter adapter;
    List<DocumentsItem> arraylist;
    @Inject protected AppSharedPreference appSharedPreference;
    @Inject protected DocumentStatusViewModel documentStatusViewModel;
    @Inject protected Utility utility;
    @Inject protected PermissionManager permissionManager;
    private DocumentsListItem selectedDocumetnsListItem;
    private int selectedItemPosition;
    private DocumentsListStatusResponse documentsListStatusResponse;

    /**
     * This method to start DocumentStatusActivity class.
     * @param context
     */
    public static void start(Context context) {
        context.startActivity(new Intent(context, DocumentStatusActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_document_status, null, false);
        frameLayout.addView(contentView);

        arraylist = new ArrayList<>();
        titleTextView.setText(getResources().getString(R.string.document_status_title));
        documentStatusViewModel.getApiResponseMutableLiveData().observe(this, this::handleApiResponse);
        /*if (!TextUtils.isEmpty(appSharedPreference.getOwnerType())) {
            if (Constant.OWNER_CUM_DRIVER.equalsIgnoreCase(appSharedPreference.getOwnerType())) {
                documentStatusViewModel.fetchOwnerCumDriverStatusRequest();
            } else {
                documentStatusViewModel.fetchNonDrivingPartnerStatusRequest();
            }
        }*/

        /*if (Constant.OWNER_ID.equals(String.valueOf(appSharedPreference.getOwnerTypeId()))) {
            documentStatusViewModel.fetchOwnerCumDriverStatusRequest();
        }else {
            documentStatusViewModel.fetchNonDrivingPartnerStatusRequest();
        }*/
        documentStatusViewModel.fetchOwnerCumDriverStatusRequest();
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
                if (ServiceType.OWNER_CUM_DRIVER_STATUS == serviceType || ServiceType.NON_DRIVING_PARTNER_STATUS == serviceType) {
                    updateDocumentListUI(apiResponse);
                }
                else if (ServiceType.UPLOAD_DOCUEMNT == serviceType) {
                    utility.showToastMsg("File are uploaded successfully");
                    clearAndUpdateDocumentListUI();
                }/* else {
                    if (apiResponse.isValidResponse()) {
                        documentsListStatusResponse = new Gson().fromJson(apiResponse.data, DocumentsListStatusResponse.class);
                        if (documentsListStatusResponse != null) {
                            arraylist.clear();
                            if (null != documentsListStatusResponse.getDocumentsList()) {
                                arraylist.addAll(documentsListStatusResponse.getDocumentsList());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }*/
                break;
            case SUCCESS_MULTIPLE:
                utility.dismissDialog();
                if (ServiceType.UPLOAD_DOCUEMNT == serviceType) {
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
                }
                break;
            case ERROR:
                utility.dismissDialog();
                break;
        }
    }

    private void updateDocumentListUI(ApiResponse apiResponse) {
        int count = 0;
        arraylist.clear();
        if (apiResponse.isValidResponse()) {
            try {
                JSONObject jsonObject = new JSONObject(apiResponse.data.toString());
                DocumentStatusResponse documentStatusResponse = new Gson().fromJson(apiResponse.data, DocumentStatusResponse.class);
                if (null != documentStatusResponse) {
                    documentStatusResponse.parseDocumentStatus(jsonObject);
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
                arraylist.addAll(documentStatusResponse.getDocuments());
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
        for (DocumentsItem documentsListItem: arraylist)
            documentsListItem.setFilePath(null);
        adapter.notifyDataSetChanged();
    }

    private void functionality() {
        //btnUpdate.setOnClickListener(this);
        adapter = new DocumentStatusAdapter(this, arraylist, screenWidth, screenHeight);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    /**
     * This method to show Image Galary from external storage.
     */
    private void showImageFromGalary() {
        String [] storage = permissionManager.getStorageReadAndWrightPermission();
        //int totalPermission = storage.length;
        if (permissionManager.initPermissionDialog(this, storage)) {
            showImagePickerScreen();
        }
    }

    private void showImagePickerScreen() {
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
    }

    /**
     * This method to update UploadContentButtonUI.
     */
   /* private void updateUploadContentButtonUI() {
        //adapter.getSelectedFilesCount() > 0 ? Show update document UI button else Disable update document UI button.
        uploadContentButton.setVisibility(adapter.getSelectedFilesCount() > 0 ? View.VISIBLE : View.GONE);
    }*/

    private void setDynamicValue() {
        int topBottomSpace = (int) (screenHeight * 0.0089);

        /*RelativeLayout.LayoutParams btnUpdateLayoutParams = (RelativeLayout.LayoutParams) btnUpdate.getLayoutParams();
        btnUpdateLayoutParams.setMargins(topBottomSpace * 2, 0, topBottomSpace * 2, topBottomSpace * 3);*/
        //btnUpdate.setLayoutParams(btnUpdateLayoutParams);

    }

    private VehicleDetailRequest getVehicleDetailRequest() {
        VehicleDetailRequest vehicleDetailRequest = new VehicleDetailRequest();
        if (null != documentsListStatusResponse) {
            VehicleDetails vehicleDetails = documentsListStatusResponse.getVehicleDetails();
            vehicleDetailRequest.setVehicleName(vehicleDetails.getVehicleNumber());
            vehicleDetailRequest.setVehicleNumber(vehicleDetails.getVehicleNumber());
            vehicleDetailRequest.setVehicleTypeId(vehicleDetails.getVehicleTypeId());
        }
        return vehicleDetailRequest;
    }

    private void initially() {
        uploadContentButton = findViewById(R.id.uploadContentButton);
        recyclerView = findViewById(R.id.recyclerView);
        uploadContentButton.setOnClickListener(((view) -> {
            /*if (isAnyItemDocumentSelected()) {
                documentStatusViewModel.uploadDocumentRequest(findSelectedDocumentList(), getVehicleDetailRequest());
            } else {
                utility.showToastMsg("Please select document");
            }*/
            UploadDocumentActivity.start(this);
        }));
    }

    /**
     * This method to find any docuemnt are selected or not by user
     * @return boolean
     */
    private boolean isAnyItemDocumentSelected() {
        for (DocumentsItem documentsListItem: arraylist) {
            if (!TextUtils.isEmpty(documentsListItem.getFilePath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method to find selected document file list.
     * @return List DocumentsListItem.
     */
    private List<DocumentsItem> findSelectedDocumentList() {
        List<DocumentsItem> documentsListItems = new ArrayList<>();
        int count = 0;
        do {
            DocumentsItem documentsListItem = arraylist.get(count);
            if (!TextUtils.isEmpty(documentsListItem.getFilePath())) {
                documentsListItems.add(documentsListItem);
            }
        } while (++ count < this.arraylist.size());
        return documentsListItems;
    }

    /**
     * This method to update DocumetnsListItem and update adapter UI.
     * @param path image path
     */
    private void updateDocumentItem(@NonNull String path) {
        if (null != arraylist) {
            selectedDocumetnsListItem.setFilePath(path);
            adapter.notifyItemChanged(selectedItemPosition);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            List<String> filePath = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
            //Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
            //.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
            if (null != filePath && !filePath.isEmpty()) {
                CrashlyticsHelper.d("Update The image file path:" + filePath);
                updateDocumentItem(filePath.get(0));
//                updateUploadContentButtonUI();
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
                showImagePickerScreen();
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                permissionManager.showRequestPermissionDialog(this, permissions, new PermissionManager.PermissionAskListener() {
                    @Override
                    public void onNeedPermission() {

                    }

                    @Override
                    public void onPermissionPreviouslyDenied(String permission) {
                        permissionManager.showPermissionNeededDialog(DocumentStatusActivity.this,
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

    @Override
    public void onClick(View v) {

        /*switch (v.getId()) {
            case R.id.btnUpdate:
                Intent intent = new Intent(this, VehicleListActivity.class);
                startActivity(intent);
        }*/
    }
}