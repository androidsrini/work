package com.codesense.driverapp.ui.documentstatus;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import com.codesense.driverapp.data.DocumentsListItem;
import com.codesense.driverapp.data.DocumentsListStatusResponse;
import com.codesense.driverapp.di.utils.PermissionManager;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.Constant;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.codesense.driverapp.ui.helper.CrashlyticsHelper;
import com.codesense.driverapp.ui.uploaddocument.RecyclerTouchListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.library.fileimagepicker.filepicker.FilePickerBuilder;
import com.library.fileimagepicker.filepicker.FilePickerConst;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DocumentStatusActivity extends DrawerActivity implements View.OnClickListener {

    private static final int IMAGE_PICKER = 0x0001;
    //Button btnUpdate;
    protected RecyclerView recyclerView;
    protected Button uploadContentButton;
    DocumentStatusAdapter adapter;
    List<DocumentsListItem> arraylist;
    @Inject protected AppSharedPreference appSharedPreference;
    @Inject protected DocumentStatusViewModel documentStatusViewModel;
    @Inject protected Utility utility;
    @Inject protected PermissionManager permissionManager;
    private DocumentsListItem selectedDocumetnsListItem;
    private int selectedItemPosition;

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
        if (!TextUtils.isEmpty(appSharedPreference.getOwnerType())) {
            if (Constant.OWNER_CUM_DRIVER.equalsIgnoreCase(appSharedPreference.getOwnerType())) {
                documentStatusViewModel.fetchOwnerCumDriverStatusRequest();
            } else {
                documentStatusViewModel.fetchNonDrivingPartnerStatusRequest();
            }
        }
        initially();
        setDynamicValue();
        functionality();

    }

    private void handleApiResponse(DocumentStatusApiResponse documentStatusApiResponse) {
        ApiResponse apiResponse = documentStatusApiResponse.getApiResponse();
        DocumentStatusApiResponse.ServiceType serviceType = documentStatusApiResponse.getServiceType();
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (DocumentStatusApiResponse.ServiceType.UPLOAD_DOCUEMNT == serviceType) {
                    utility.showToastMsg("File are uploaded successfully");
                    clearAndUpdateDocumentListUI();
                } else {
                    if (apiResponse.isValidResponse()) {
                        DocumentsListStatusResponse documentsListStatusResponse = new Gson().fromJson(apiResponse.data, DocumentsListStatusResponse.class);
                        if (documentsListStatusResponse != null) {
                            arraylist.clear();
                            if (null != documentsListStatusResponse.getDocumentsList()) {
                                arraylist.addAll(documentsListStatusResponse.getDocumentsList());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
                break;
            case SUCCESS_MULTIPLE:
                utility.dismissDialog();
                if (DocumentStatusApiResponse.ServiceType.UPLOAD_DOCUEMNT == serviceType) {
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

    /**
     * This method to remove all selected files and update DocumentList UI.
     */
    private void clearAndUpdateDocumentListUI() {
        for (DocumentsListItem documentsListItem: arraylist)
            documentsListItem.setFilePath(null);
        adapter.notifyDataSetChanged();
    }

    private void functionality() {
        //btnUpdate.setOnClickListener(this);
        adapter = new DocumentStatusAdapter(this, arraylist, screenWidth, screenHeight);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                selectedItemPosition = position;
                selectedDocumetnsListItem = arraylist.get(position);
                String filePath = selectedDocumetnsListItem.getFilePath();
                if (!TextUtils.isEmpty(filePath)) {
                    utility.showConformationDialog(DocumentStatusActivity.this,
                            "Are you sure you want to delete this file", (DialogInterface.OnClickListener) (dialog, which) -> {
                                selectedDocumetnsListItem.setFilePath(null);
                                adapter.notifyDataSetChanged();
                                updateUploadContentButtonUI();
                            });
                } else {
                    showImageFromGalary();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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
    private void updateUploadContentButtonUI() {
        //adapter.getSelectedFilesCount() > 0 ? Show update document UI button else Disable update document UI button.
        uploadContentButton.setVisibility(adapter.getSelectedFilesCount() > 0 ? View.VISIBLE : View.GONE);
    }

    private void setDynamicValue() {
        int topBottomSpace = (int) (screenHeight * 0.0089);

        /*RelativeLayout.LayoutParams btnUpdateLayoutParams = (RelativeLayout.LayoutParams) btnUpdate.getLayoutParams();
        btnUpdateLayoutParams.setMargins(topBottomSpace * 2, 0, topBottomSpace * 2, topBottomSpace * 3);*/
        //btnUpdate.setLayoutParams(btnUpdateLayoutParams);

    }

    private void initially() {
        uploadContentButton = findViewById(R.id.uploadContentButton);
        recyclerView = findViewById(R.id.recyclerView);
        uploadContentButton.setOnClickListener(((view) -> {
            if (isAnyItemDocumentSelected()) {
                documentStatusViewModel.uploadDocumentRequest(findSelectedDocumentList());
            } else {
                utility.showToastMsg("Please select document");
            }
        }));
    }

    /**
     * This method to find any docuemnt are selected or not by user
     * @return boolean
     */
    private boolean isAnyItemDocumentSelected() {
        for (DocumentsListItem documentsListItem: arraylist) {
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
    private List<DocumentsListItem> findSelectedDocumentList() {
        List<DocumentsListItem> documentsListItems = new ArrayList<>();
        int count = 0;
        do {
            DocumentsListItem documentsListItem = arraylist.get(count);
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
                updateUploadContentButtonUI();
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