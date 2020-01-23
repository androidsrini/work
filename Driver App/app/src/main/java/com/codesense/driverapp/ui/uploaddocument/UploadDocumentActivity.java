package com.codesense.driverapp.ui.uploaddocument;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.data.AvailableVehiclesItem;
import com.codesense.driverapp.data.DocumentStatus;
import com.codesense.driverapp.data.DocumentStatusResponse;
import com.codesense.driverapp.data.DocumentsItem;
import com.codesense.driverapp.data.VehicleDetailRequest;
import com.codesense.driverapp.data.VehicleTypeResponse;
import com.codesense.driverapp.data.VehicleTypesItem;
import com.codesense.driverapp.di.utils.PermissionManager;
import com.codesense.driverapp.di.utils.RecyclerTouchListener;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.Constant;
import com.codesense.driverapp.net.ServiceType;
import com.codesense.driverapp.ui.addvehicle.AddVehicleActivity;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.codesense.driverapp.ui.helper.CrashlyticsHelper;
import com.codesense.driverapp.ui.online.OnlineActivity;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.library.fileimagepicker.filepicker.FilePickerBuilder;
import com.library.fileimagepicker.filepicker.FilePickerConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class UploadDocumentActivity extends DrawerActivity {

    public static final int UPLOAD_DOCUMENT_STATUS_INDEX = 0;
    public static final int UPLOAD_DOCUMENT_NAME_INDEX = 1;
    public static final String IS_NEED_TO_UPDATE_STATUS_LIST_ARG = "IsNeedToUpdateStatusListArg";
    private static final String TAG = "Driver";
    public static final int RESULT = 0x0004;
    private static final int IMAGE_PICKER = 0x0001;
    private static final int FILE_PICKER = 0x0002;
    private static final int UPLOAD_DOCUMENT_ICON_NAME_INDEX = 2;
    private static final int RC_PHOTO_PICKER_PERM = 0x00012;
    /**
     * To create UploadDocumentViewModel object.
     */
    @Inject
    protected UploadDocumentViewModel uploadDocumentViewModel;
    /**
     * To create Utility object.
     */
    @Inject
    protected Utility utility;
    @Inject
    protected PermissionManager permissionManager;
    View contentView;
    TextView tvRemaining;
    RelativeLayout vehicleTypeRelativeLayout;
    TextView vehicleTypeTextView;
    ImageView vehicleTypeArrowImageView;
    EditText etVehicleName;
    EditText etVehicleNumber;
    View view;
    View view1;
    View view2;
    View selectVehicleDivider;
    RecyclerView recyclerView;
    Button uploadContentButton;
    RelativeLayout selectVehicleRelativeLayout;
    TextView selectVehicleTextView;
    ImageView selectVehicleArrowImageView;
    //DriverAppUI driverAppUI1;
    UploadDocumentAdapter adapter;
    int mSelectedItemType, mSelectedVehicle = -1;
    boolean typeSelFirstTime, vehicleSelectionFirstTime;
    String typeValue;
    private List<VehicleTypesItem> vehicleTypesItems;
    private List<DocumentsItem> uploadDocumentActionInfos;
    private DocumentsItem selectedDocumetnsListItem;
    private int selectedDocumentsListPosition;
    private List<AvailableVehiclesItem> availableVehiclesItems;
    private boolean isDocumentAdded;
    String from;


    /**
     * This method to start UploadDocumentActivity class
     *
     * @param context
     */
    public static void start(Context context) {
        context.startActivity(new Intent(context, UploadDocumentActivity.class));
        CrashlyticsHelper.startLog(UploadDocumentActivity.class.getName());
    }

    public static Intent findIntent(Context context, String from) {
        Intent intent = new Intent(context, UploadDocumentActivity.class);
        intent.putExtra("from", from);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashlyticsHelper.i("UploadDocument OnCreate start");
        //setContentView(R.layout.activity_upload_document);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_upload_document, null, false);
        frameLayout.addView(contentView);
        //ProductBindView.bind(this);
        uploadDocumentActionInfos = new ArrayList<>();
        vehicleTypesItems = new ArrayList<>();
        availableVehiclesItems = new ArrayList<>();
        titleTextView.setText(getResources().getString(R.string.upload_doc_text));

        Intent intent = getIntent();
        if (intent != null) {
            from = intent.getStringExtra("from");
        }
        uploadDocumentViewModel.getApiResponseMutableLiveData().observe(this, this::handleApiResponse);
        if (from!=null &&from.equalsIgnoreCase("edit")) {
            uploadDocumentViewModel.fetchVehicleTypesRequest();
            if (Constant.OWNER_ID.equals(String.valueOf(appSharedPreference.getOwnerTypeId()))) {
                uploadDocumentViewModel.fetchOwnerCumDriverStatusRequest();
            } else {
                uploadDocumentViewModel.fetchNonDrivingPartnerStatusRequest();
            }
        } else {
            if (Constant.OWNER_ID.equals(String.valueOf(appSharedPreference.getOwnerTypeId()))) {
                uploadDocumentViewModel.fetchDocumentStatusRequest();
            } else {
                uploadDocumentViewModel.fetchDocumentStatusDriverRequest();
            }
        }
        updateCrashDetails();
        initially();
        setDynamicValue();
        functionality();
        //Log.d(TAG, " User type: " + appSharedPreference.getUserType());
    }

    /**
     * This method will update crash details.
     */
    private void updateCrashDetails() {
        Crashlytics.setUserIdentifier(appSharedPreference.getUserID());
        //Crashlytics.setUserEmail(appSharedPreference.);
    }

    /**
     * This method to handle api resonse
     *
     * @param apiResponse
     */
    private void handleApiResponse(ApiResponse apiResponse) {
        ServiceType serviceType = apiResponse.getServiceType();
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (apiResponse.isValidResponse()) {
                    if (ServiceType.GET_DOCUMENTS_STATUS == serviceType) {
                        updateDocumentListUI(apiResponse);
                        uploadDocumentViewModel.fetchVehicleTypesRequest();

                    }
                    if (ServiceType.VEHICLE_TYPES == serviceType) {
                        VehicleTypeResponse vehicleTypeResponse = new Gson().fromJson(apiResponse.data, VehicleTypeResponse.class);
                        vehicleTypesItems.clear();
                        if (null != vehicleTypeResponse && null != vehicleTypeResponse.getVehicleTypes()) {
                            vehicleTypesItems.addAll(vehicleTypeResponse.getVehicleTypes());
                        }
                        /*if (appSharedPreference.getOwnerTypeId() == Constant.OWNER_CUM_DRIVER_TYPE) {
                            uploadDocumentViewModel.fetchDocumentListRequest();
                        } else {
                            uploadDocumentViewModel.fetchVehicleListRequest();
                        }*/
                    } else if (ServiceType.DRIVER == serviceType) {
                        updateDriverDocumentListUI(apiResponse);
                    } else if (ServiceType.VEHICLE == serviceType) {
                        //updateVehicleDocumentListUI(apiResponse);
                    } else if (ServiceType.UPLOAD_DOCUEMNT == serviceType) {
                        utility.showToastMsg("File are uploaded successfully");
                        clearAndUpdateDocumentListUI();
                        clearAllEditTextUI();
                        if (Constant.OWNER_ID.equals(String.valueOf(appSharedPreference.getOwnerTypeId()))) {
                            currentPosition = -1;
                            OnlineActivity.start(this);
                        } else {
                            AddVehicleActivity.start(this);
                        }
                    }
                }
                break;
            case SUCCESS_MULTIPLE:
                utility.dismissDialog();
                if (ServiceType.ALL_DOCUMENT == serviceType) {
                    updateDocumentListUI(apiResponse);
                } else if (ServiceType.UPLOAD_DOCUEMNTS == serviceType) {
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
                        isDocumentAdded = true;
                        utility.showToastMsg("All file are uploaded successfully");
                        if (Constant.OWNER_ID.equals(String.valueOf(appSharedPreference.getOwnerTypeId()))) {
                            currentPosition = -1;
                            OnlineActivity.start(this);
                        } else {
                            AddVehicleActivity.start(this);
                        }
                    }
                    clearAndUpdateDocumentListUI();
                    clearAllEditTextUI();
                }
                break;
            case ERROR:
                utility.dismissDialog();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        utility.dismissDialog();
    }

    /**
     * This method to update Driver DocumentList UI.
     *
     * @param apiResponse
     */
    @Deprecated
    private void updateDriverDocumentListUI(ApiResponse apiResponse) {
        if (apiResponse.isValidResponse()) {
            try {
                JSONObject jsonObject = new JSONObject(apiResponse.data.toString());
                DocumentStatusResponse documentsListStatusResponse = new Gson().fromJson(apiResponse.data, DocumentStatusResponse.class);
                uploadDocumentActionInfos.clear();
                if (null != documentsListStatusResponse && null != documentsListStatusResponse.getDocuments()) {
                    for (DocumentsItem documentsItem : documentsListStatusResponse.getDocuments()) {
                        documentsItem.parseDocumentStatus(jsonObject);
                    }
                    uploadDocumentActionInfos.addAll(documentsListStatusResponse.getDocuments());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method to update Vehicle DocumentList UI.
     *
     * @param apiResponse
     */
    /*@Deprecated
    private void updateVehicleDocumentListUI(ApiResponse apiResponse) {
        if (apiResponse.isValidResponse()) {
            DocumentsListStatusResponse documentsListStatusResponse = new Gson().fromJson(apiResponse.data, DocumentsListStatusResponse.class);
            if (null != documentsListStatusResponse && null != documentsListStatusResponse.getDocumentsList()) {
                uploadDocumentActionInfos.addAll(documentsListStatusResponse.getDocumentsList());
            }
        }
        adapter.notifyDataSetChanged();
    }*/

    /*private void updateDocumentListUI(ApiResponse apiResponse) {
        JsonElement[] response = apiResponse.datas;
        int count = 0;
        uploadDocumentActionInfos.clear();
        do {
            if (apiResponse.isValidResponse(count)) {
                DocumentsListStatusResponse documentsListStatusResponse = new Gson().fromJson(response[count], DocumentsListStatusResponse.class);
                uploadDocumentActionInfos.addAll(documentsListStatusResponse.getDocumentsList());
            }
        } while (++ count < response.length);
        adapter.notifyDataSetChanged();
    }*/
    private void updateDocumentListUI(ApiResponse apiResponse) {
        int count = 0;
        uploadDocumentActionInfos.clear();
        availableVehiclesItems.clear();
        DocumentStatusResponse documentStatusResponse = null;
        if (apiResponse.isValidResponse()) {
            try {
                JSONObject jsonObject = new JSONObject(apiResponse.data.toString());
                documentStatusResponse = new Gson().fromJson(apiResponse.data, DocumentStatusResponse.class);
                if (null != documentStatusResponse) {
                    documentStatusResponse.parseDocumentStatus(jsonObject);
                    for (DocumentsItem documentsItem : documentStatusResponse.getDocuments()) {
                        documentsItem.parseDocumentStatus(jsonObject);
                    }
                }
                if (null != documentStatusResponse && null != documentStatusResponse.getAvailableVehicles()
                        && !documentStatusResponse.getAvailableVehicles().isEmpty()) {
                    availableVehiclesItems.addAll(documentStatusResponse.getAvailableVehicles());
                } else {
                    selectVehicleRelativeLayout.setVisibility(View.GONE);
                    selectVehicleDivider.setVisibility(View.GONE);
                }
                uploadDocumentActionInfos.addAll(documentStatusResponse.getDocuments());
                if (from!=null &&from.equalsIgnoreCase("edit")) {
                    if (documentStatusResponse.getVehicleDetailObject() != null) {
                        int position=-1;
                        for(int i=0;i<availableVehiclesItems.size();i++){
                            if (availableVehiclesItems.get(i).getVehicleId().equalsIgnoreCase(documentStatusResponse.getVehicleDetailObject().getVehicleId())){
                                position = i;
                                mSelectedVehicle = position;
                                vehicleSelectionFirstTime = false;
                            }
                        }
                        AvailableVehiclesItem selectedState = availableVehiclesItems.get(position);
                        updateSelectedVehicleUI(selectedState);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
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

    /**
     * This method to create VehicleDetailRequest Object.
     *
     * @return VehicleDetailRequest
     */
    private VehicleDetailRequest createVehicleDetailRequestObject() {
        VehicleDetailRequest vehicleDetailRequest = new VehicleDetailRequest();
        vehicleDetailRequest.setVehicleTypeId(getVehicleTypeId());
        vehicleDetailRequest.setVehicleName(getEtVehicleName());
        vehicleDetailRequest.setVehicleNumber(getEtVehicleNumber());
        if (mSelectedVehicle >= 0) {
            AvailableVehiclesItem availableVehiclesItem = availableVehiclesItems.get(mSelectedVehicle);
            vehicleDetailRequest.setVehicleId(availableVehiclesItem.getVehicleId());
        }
        return vehicleDetailRequest;
    }

    /**
     * This method to handle all view functionality takes.
     */
    private void functionality() {
        uploadContentButton.setOnClickListener(((view) -> {
            if (Constant.OWNER_ID.equals(String.valueOf(appSharedPreference.getOwnerTypeId()))) {
                if (isValidAllFields()) {
                    if (from!=null &&from.equalsIgnoreCase("edit")) {
                            uploadDocumentViewModel.uploadDocumentRequest(findSelectedDocumentList(), createVehicleDetailRequestObject());

                    }else{
                        if (isValiedAllSelected()) {
                            uploadDocumentViewModel.uploadDocumentRequest(findSelectedDocumentList(), createVehicleDetailRequestObject());
                        }
                    }
                }/*else {
                    utility.showToastMsg("Please select document");
                }*/
            } else {
                if (from!=null &&from.equalsIgnoreCase("edit")) {
                    uploadDocumentViewModel.uploadDocumentRequest(findSelectedDocumentList(), createVehicleDetailRequestObject());
                }else {
                    if (isValiedAllSelected()) {
                        uploadDocumentViewModel.uploadDocumentRequest(findSelectedDocumentList(), createVehicleDetailRequestObject());
                    }
                }
            }
        }));
        adapter = new UploadDocumentAdapter(this, uploadDocumentActionInfos, screenWidth, screenHeight, from);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                selectedDocumentsListPosition = position;
                DocumentStatus documentStatus = uploadDocumentActionInfos.get(position).getDocumentStatus();
                if (documentStatus.getAllowUpdate() != 0) {
                    selectedDocumetnsListItem = uploadDocumentActionInfos.get(position);
                    String filePath = uploadDocumentActionInfos.get(position).getFilePath();
                    if (!TextUtils.isEmpty(filePath)) {
                        utility.showConformationDialog(UploadDocumentActivity.this,
                                "Are you sure you want to delete this file", (DialogInterface.OnClickListener) (dialog, which) -> {
                                    uploadDocumentActionInfos.get(position).setFilePath(null);
                                    adapter.notifyDataSetChanged();
                                    updateUploadContentButtonUI();
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
        vehicleTypeRelativeLayout.setOnClickListener(v -> {
            showListPopupScreen(vehicleTypeRelativeLayout, vehicleTypeTextView, vehicleTypesItems,-1);
        });

        selectVehicleRelativeLayout.setOnClickListener(this::showAvailableVehiclePopupScreen);
    }

    /**
     * This method to update UploadContentButtonUI.
     */
    private void updateUploadContentButtonUI() {
        //adapter.getSelectedFilesCount() > 0 ? Show update document UI button else Disable update document UI button.
        uploadContentButton.setVisibility(adapter.getSelectedFilesCount() > 0 ? View.VISIBLE : View.GONE);
    }

    /**
     * This method to validate and display invalid field toast message.
     *
     * @return boolean
     */
    private boolean isValidAllFields() {
        boolean isValid = true;
        if (TextUtils.isEmpty(getVehicleTypeId())) {
            utility.showToastMsg("Select Vehicle Type");
            isValid = false;
        } else if (TextUtils.isEmpty(getEtVehicleName())) {
            utility.showToastMsg("Vehicle Name mandatory");
            isValid = false;
        } else if (TextUtils.isEmpty(getEtVehicleNumber())) {
            utility.showToastMsg("Vehicle Number mandatory");
            isValid = false;
        }
        return isValid;
    }

    /**
     * This method to find selected document file list.
     *
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
        } while (++count < this.uploadDocumentActionInfos.size());
        return documentsListItems;
    }

    /**
     * This method to find any docuemnt are selected or not by user
     *
     * @return boolean
     */
    private boolean isAnyItemDocumentSelected() {
        for (DocumentsItem documentsListItem : uploadDocumentActionInfos) {
            if (!TextUtils.isEmpty(documentsListItem.getFilePath())) {
                return true;
            }
        }
        return false;
    }

    private boolean isValiedAllSelected() {
        boolean isValied = false;
        for (DocumentsItem documentsListItem : uploadDocumentActionInfos) {
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

    /**
     * This method will return VehicleType based on user selection.
     *
     * @return String
     */
    private String getVehicleTypeTextView() {
        return vehicleTypeTextView.getText().toString().trim();
    }

    /**
     * This method will return VehicleName based on user given.
     *
     * @return String
     */
    private String getEtVehicleName() {
        return etVehicleName.getText().toString().trim();
    }

    /**
     * This method will return VehicleNumber based on user given.
     *
     * @return String
     */
    private String getEtVehicleNumber() {
        return etVehicleNumber.getText().toString().trim();
    }

    /**
     * This method will return Vechicle type id based on user selection vechicle type.
     *
     * @return String
     */
    private String getVehicleTypeId() {
        if (!TextUtils.isEmpty(getVehicleTypeTextView())) {
            for (VehicleTypesItem v : vehicleTypesItems) {
                if (v.getVehicleType().equalsIgnoreCase(getVehicleTypeTextView()))
                    return v.getVehicleTypeId();
            }
        }
        return null;
    }

    /**
     * This method to show dropdown popup.
     *
     * @param view
     * @param updateNameTextView
     * @param list
     */
    private void showListPopupScreen(View view, final TextView updateNameTextView, final List<VehicleTypesItem> list,int ids) {
        View customView = LayoutInflater.from(this).inflate(R.layout.spinner_pop_up_screen, null);
        int leftRightSpace = (int) (screenWidth * 0.0153);
        //int topBottomSpace = (int) (screenHeight * 0.0089);
        final PopupWindow popupWindow;

        popupWindow = new PopupWindow(customView, leftRightSpace * 58, ViewGroup.LayoutParams.WRAP_CONTENT);

       final ListView listView = customView.findViewById(R.id.listItemListView);

        ArrayAdapter<VehicleTypesItem> dataAdapter = new ArrayAdapter<VehicleTypesItem>(this, R.layout.spinner_dropdown_textview, list) {

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = null;
                v = super.getView(position, null, parent);
                TextView tv = (TextView) v;
                if (position == mSelectedItemType) {
                    v.setBackgroundColor(getResources().getColor(R.color.primary_color));
                    tv.setTextColor(getResources().getColor(R.color.secondary_color));
                } else {
                    v.setBackgroundColor(getResources().getColor(R.color.background_color));
                    tv.setTextColor(getResources().getColor(R.color.secondary_color));
                }
                if (typeSelFirstTime) {
                    v.setBackgroundColor(getResources().getColor(R.color.background_color));
                    tv.setTextColor(getResources().getColor(R.color.secondary_color));
                }
                tv.setText(getItem(position).getVehicleType());
                return v;
            }
        };
        listView.setAdapter(dataAdapter);


        final List<VehicleTypesItem> fState;
        fState = list;
        listView.setOnItemClickListener((parent, view3, position, id) -> {
            popupWindow.dismiss();
            VehicleTypesItem selectedState = list.get(position);

            updateNameTextView.setText(selectedState.getVehicleType());
            updateNameTextView.setTextColor(getResources().getColor(R.color.secondary_color));
            mSelectedItemType = position;
            typeSelFirstTime = false;

        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(view);
    }

    private void updateSelectedVehicleUI(AvailableVehiclesItem availableVehiclesItem) {
        //Update select vehicle UI.
        selectVehicleTextView.setText(availableVehiclesItem.getVehicleName());
        selectVehicleTextView.setTextColor(getResources().getColor(R.color.secondary_color));
        //Update vehicle UI
        String vehicleTypeId = availableVehiclesItem.getVehicleTypeId();
        if (!TextUtils.isEmpty(vehicleTypeId)) {
            for (int index = 0; index < vehicleTypesItems.size(); index++) {
                VehicleTypesItem vehicleTypesItem = vehicleTypesItems.get(index);
                if (vehicleTypesItem.getVehicleTypeId().compareTo(vehicleTypeId) == 0) {
                    vehicleTypeTextView.setText(vehicleTypesItem.getVehicleType());
                    mSelectedItemType = index;
                    break;
                }
            }
        }
        etVehicleName.setText(availableVehiclesItem.getVehicleName());
        etVehicleNumber.setText(availableVehiclesItem.getVehicleNumber());
    }

    /**
     * This method to show available vehicle list popup
     *
     * @param view
     */
    private void showAvailableVehiclePopupScreen(View view) {
        View customView = LayoutInflater.from(this).inflate(R.layout.spinner_pop_up_screen, null);
        int leftRightSpace = (int) (screenWidth * 0.0153);
        final PopupWindow popupWindow = new PopupWindow(customView, leftRightSpace * 58, ViewGroup.LayoutParams.WRAP_CONTENT);
        final ListView listView = customView.findViewById(R.id.listItemListView);
        ArrayAdapter<AvailableVehiclesItem> dataAdapter = new ArrayAdapter<AvailableVehiclesItem>(this, R.layout.spinner_dropdown_textview, availableVehiclesItems) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = null;
                v = super.getView(position, null, parent);
                TextView tv = (TextView) v;
                if (position == mSelectedVehicle) {
                    v.setBackgroundColor(getResources().getColor(R.color.primary_color));
                    tv.setTextColor(getResources().getColor(R.color.secondary_color));
                } else {
                    v.setBackgroundColor(getResources().getColor(R.color.background_color));
                    tv.setTextColor(getResources().getColor(R.color.secondary_color));
                }
                if (vehicleSelectionFirstTime) {
                    v.setBackgroundColor(getResources().getColor(R.color.background_color));
                    tv.setTextColor(getResources().getColor(R.color.secondary_color));
                }
                tv.setText(getItem(position).getVehicleName());
                return v;
            }
        };
        listView.setAdapter(dataAdapter);
        listView.setSelection(mSelectedVehicle);
        listView.setOnItemClickListener((parent, view3, position, id) -> {
            popupWindow.dismiss();
            AvailableVehiclesItem selectedState = availableVehiclesItems.get(position);
            selectVehicleTextView.setText(selectedState.getVehicleName());
            selectVehicleTextView.setTextColor(getResources().getColor(R.color.secondary_color));
            mSelectedVehicle = position;
            vehicleSelectionFirstTime = false;
            updateSelectedVehicleUI(selectedState);
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(view);
    }

    /**
     * This method to create object for views.
     */
    private void initially() {
        vehicleTypeArrowImageView = findViewById(R.id.vehicleTypeArrowImageView);
        tvRemaining = findViewById(R.id.tvRemaining);
        vehicleTypeRelativeLayout = findViewById(R.id.vehicleTypeRelativeLayout);
        etVehicleName = findViewById(R.id.etVehicleName);
        etVehicleNumber = findViewById(R.id.etVehicleNumber);
        view = findViewById(R.id.view);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        recyclerView = findViewById(R.id.recyclerView);
        vehicleTypeTextView = findViewById(R.id.vehicleTypeTextView);
        uploadContentButton = findViewById(R.id.uploadContentButton);
        selectVehicleRelativeLayout = findViewById(R.id.selectVehicleRelativeLayout);
        selectVehicleTextView = findViewById(R.id.selectVehicleTextView);
        selectVehicleArrowImageView = findViewById(R.id.selectVehicleArrowImageView);
        selectVehicleDivider = findViewById(R.id.selectVehicleDivider);
    }

    /**
     * This method to resize views and update padding for views dynamically.
     */
    private void setDynamicValue() {
        int topBottomSpace = (int) (screenHeight * 0.0089);
        int imgIconArrowWidth = (int) (screenWidth * 0.055);
        int imgIconArrowHeight = (int) (screenWidth * 0.055);

        RelativeLayout.LayoutParams vehicleTypeArrowImageViewimgLayParams = (RelativeLayout.LayoutParams) vehicleTypeArrowImageView.getLayoutParams();
        vehicleTypeArrowImageViewimgLayParams.width = imgIconArrowWidth;
        vehicleTypeArrowImageViewimgLayParams.height = imgIconArrowHeight;
        vehicleTypeArrowImageView.setLayoutParams(vehicleTypeArrowImageViewimgLayParams);

        RelativeLayout.LayoutParams tvLegalTextLayoutParams = (RelativeLayout.LayoutParams) tvRemaining.getLayoutParams();
        tvLegalTextLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace * 3, 0, 0);
        tvRemaining.setLayoutParams(tvLegalTextLayoutParams);
        tvRemaining.setPadding(0, 0, topBottomSpace * 2, 0);

        RelativeLayout.LayoutParams vehicleTypeRelativeLayoutLayoutParams = (RelativeLayout.LayoutParams) vehicleTypeRelativeLayout.getLayoutParams();
        vehicleTypeRelativeLayoutLayoutParams.setMargins(topBottomSpace * 5, topBottomSpace * 3, topBottomSpace * 5, 0);
        vehicleTypeRelativeLayout.setLayoutParams(vehicleTypeRelativeLayoutLayoutParams);

        RelativeLayout.LayoutParams selectVehicleArrowImageViewParams = (RelativeLayout.LayoutParams) selectVehicleArrowImageView.getLayoutParams();
        selectVehicleArrowImageViewParams.width = imgIconArrowWidth;
        selectVehicleArrowImageViewParams.height = imgIconArrowHeight;
        selectVehicleArrowImageView.setLayoutParams(selectVehicleArrowImageViewParams);

        RelativeLayout.LayoutParams selectVehicleDividerParams = (RelativeLayout.LayoutParams) selectVehicleDivider.getLayoutParams();
        selectVehicleDividerParams.setMargins(topBottomSpace * 5, 0, topBottomSpace * 5, 0);
        selectVehicleDivider.setLayoutParams(selectVehicleDividerParams);

        /*RelativeLayout.LayoutParams selectVehicleTextViewParams = (RelativeLayout.LayoutParams) selectVehicleTextView.getLayoutParams();
        selectVehicleTextViewParams.setMargins(topBottomSpace * 3, topBottomSpace * 3, 0, 0);
        selectVehicleTextView.setLayoutParams(selectVehicleTextViewParams);
        selectVehicleTextView.setPadding(0, 0, topBottomSpace * 2, 0);*/

        RelativeLayout.LayoutParams selectVehicleRelativeLayoutParams = (RelativeLayout.LayoutParams) selectVehicleRelativeLayout.getLayoutParams();
        selectVehicleRelativeLayoutParams.setMargins(topBottomSpace * 5, topBottomSpace * 3, topBottomSpace * 5, 0);
        selectVehicleRelativeLayout.setLayoutParams(selectVehicleRelativeLayoutParams);

        RelativeLayout.LayoutParams etVehicleNameLayoutParams = (RelativeLayout.LayoutParams) etVehicleName.getLayoutParams();
        etVehicleNameLayoutParams.setMargins(topBottomSpace * 5, topBottomSpace * 3, topBottomSpace * 5, 0);
        etVehicleName.setLayoutParams(etVehicleNameLayoutParams);

        RelativeLayout.LayoutParams etVehicleNumberLayoutParams = (RelativeLayout.LayoutParams) etVehicleNumber.getLayoutParams();
        etVehicleNumberLayoutParams.setMargins(topBottomSpace * 5, topBottomSpace * 3, topBottomSpace * 5, 0);
        etVehicleNumber.setLayoutParams(etVehicleNumberLayoutParams);

        RelativeLayout.LayoutParams viewLayoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        viewLayoutParams.setMargins(topBottomSpace * 5, 0, topBottomSpace * 5, 0);
        view.setLayoutParams(viewLayoutParams);

        RelativeLayout.LayoutParams view1LayoutParams = (RelativeLayout.LayoutParams) view1.getLayoutParams();
        view1LayoutParams.setMargins(topBottomSpace * 5, 0, topBottomSpace * 5, 0);
        view1.setLayoutParams(view1LayoutParams);

        RelativeLayout.LayoutParams view2LayoutParams = (RelativeLayout.LayoutParams) view2.getLayoutParams();
        view2LayoutParams.setMargins(topBottomSpace * 5, 0, topBottomSpace * 5, 0);
        view2.setLayoutParams(view2LayoutParams);

        RelativeLayout.LayoutParams recyclerViewLayoutParams = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
        recyclerViewLayoutParams.setMargins(0, topBottomSpace * 2, 0, 0);
        recyclerView.setLayoutParams(recyclerViewLayoutParams);
    }

    /**
     * This method to remove all selected files and update DocumentList UI.
     */
    private void clearAndUpdateDocumentListUI() {
        for (DocumentsItem documentsListItem : uploadDocumentActionInfos)
            documentsListItem.setFilePath(null);
        adapter.notifyDataSetChanged();
    }

    /**
     * This method to clear all EditText UI.
     */
    private void clearAllEditTextUI() {
        vehicleTypeTextView.setText(null);
        etVehicleNumber.setText(null);
        etVehicleName.setText(null);
        selectVehicleTextView.setText(null);
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
                updateUploadContentButtonUI();
            }
        } else {
            utility.showToastMsg("File not found");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(IS_NEED_TO_UPDATE_STATUS_LIST_ARG, isDocumentAdded);
        setResult(Activity.RESULT_OK, intent);
        finish();
        //super.onBackPressed();
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
                        permissionManager.showPermissionNeededDialog(UploadDocumentActivity.this,
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
    /*@Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        showImagePickerScreen();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    */
}
