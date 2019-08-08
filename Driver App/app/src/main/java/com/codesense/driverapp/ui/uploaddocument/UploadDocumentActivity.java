package com.codesense.driverapp.ui.uploaddocument;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.codesense.driverapp.data.DocumentsListItem;
import com.codesense.driverapp.data.DocumentsListStatusResponse;
import com.codesense.driverapp.data.VehicleDetailRequest;
import com.codesense.driverapp.data.VehicleTypeResponse;
import com.codesense.driverapp.data.VehicleTypesItem;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class UploadDocumentActivity extends DrawerActivity {

    public static final int UPLOAD_DOCUMENT_STATUS_INDEX = 0;
    public static final int UPLOAD_DOCUMENT_NAME_INDEX = 1;
    private static final String TAG = "Driver";
    private static final int IMAGE_PICKER = 0x0001;
    private static final int UPLOAD_DOCUMENT_ICON_NAME_INDEX = 2;
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
    RecyclerView recyclerView;
    Button uploadContentButton;
    //DriverAppUI driverAppUI1;
    UploadDocumentAdapter adapter;
    int mSelectedItemType;
    boolean typeSelFirstTime;
    String typeValue;
    private List<VehicleTypesItem> vehicleTypesItems;
    private List<DocumentsListItem> uploadDocumentActionInfos;
    private DocumentsListItem selectedDocumetnsListItem;

    /**
     * This method to start UploadDocumentActivity class
     *
     * @param context
     */
    public static void start(Context context) {
        context.startActivity(new Intent(context, UploadDocumentActivity.class));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_upload_document);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_upload_document, null, false);
        frameLayout.addView(contentView);
        //ProductBindView.bind(this);
        uploadDocumentActionInfos = new ArrayList<>();
        vehicleTypesItems = new ArrayList<>();
        titleTextView.setText(getResources().getString(R.string.upload_doc_text));
        uploadDocumentViewModel.getApiResponseMutableLiveData().observe(this, this::handleApiResponse);
        uploadDocumentViewModel.fetchVehicleTypesRequest();
        initially();
        setDynamicValue();
        functionality();
    }

    /**
     * This method to handle api resonse
     *
     * @param uploadDocumentApiResponse
     */
    private void handleApiResponse(UploadDocumentApiResponse uploadDocumentApiResponse) {
        ApiResponse apiResponse = uploadDocumentApiResponse.getApiResponse();
        UploadDocumentApiResponse.ServiceType serviceType = uploadDocumentApiResponse.getServiceType();
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (apiResponse.isValidResponse()) {
                    if (UploadDocumentApiResponse.ServiceType.VEHICLE_TYPES == serviceType) {
                        VehicleTypeResponse vehicleTypeResponse = new Gson().fromJson(apiResponse.data, VehicleTypeResponse.class);
                        vehicleTypesItems.clear();
                        if (null != vehicleTypeResponse && null != vehicleTypeResponse.getVehicleTypes()) {
                            vehicleTypesItems.addAll(vehicleTypeResponse.getVehicleTypes());
                        }
                        uploadDocumentViewModel.fetchDocumentListRequest();
                    } else if (UploadDocumentApiResponse.ServiceType.DRIVER == serviceType) {
                        updateDriverDocumentListUI(apiResponse);
                    } else if (UploadDocumentApiResponse.ServiceType.VEHICLE == serviceType) {
                        updateVehicleDocumentListUI(apiResponse);
                    }
                }
                break;
            case SUCCESS_MULTIPLE:
                utility.dismissDialog();
                if (UploadDocumentApiResponse.ServiceType.ALL_DOCUMENT == serviceType) {
                    updateDocumentListUI(apiResponse);
                } else if (UploadDocumentApiResponse.ServiceType.UPLOAD_DOCUEMNTS == serviceType) {
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
                }
                break;
            case ERROR:
                utility.dismissDialog();
                break;
        }
    }

    /**
     * This method to update Driver DocumentList UI.
     * @param apiResponse
     */
    @Deprecated
    private void updateDriverDocumentListUI(ApiResponse apiResponse) {
        if (apiResponse.isValidResponse()) {
            DocumentsListStatusResponse documentsListStatusResponse = new Gson().fromJson(apiResponse.data, DocumentsListStatusResponse.class);
            uploadDocumentActionInfos.clear();
            if (null != documentsListStatusResponse && null != documentsListStatusResponse.getDocumentsList()) {
                uploadDocumentActionInfos.addAll(documentsListStatusResponse.getDocumentsList());
            }
        }
    }

    /**
     * This method to update Vehicle DocumentList UI.
     *
     * @param apiResponse
     */
    @Deprecated
    private void updateVehicleDocumentListUI(ApiResponse apiResponse) {
        if (apiResponse.isValidResponse()) {
            DocumentsListStatusResponse documentsListStatusResponse = new Gson().fromJson(apiResponse.data, DocumentsListStatusResponse.class);
            if (null != documentsListStatusResponse && null != documentsListStatusResponse.getDocumentsList()) {
                uploadDocumentActionInfos.addAll(documentsListStatusResponse.getDocumentsList());
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void updateDocumentListUI(ApiResponse apiResponse) {
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
    }

    /**
     * This method to update DocumetnsListItem and update adapter UI.
     *
     * @param path image path
     */
    private void updateDocumentItem(@NonNull String path) {
        if (null != selectedDocumetnsListItem) {
            selectedDocumetnsListItem.setFilePath(path);
            adapter.notifyDataSetChanged();
        }
    }

    private VehicleDetailRequest createVehicleDetailRequestObject() {
        VehicleDetailRequest vehicleDetailRequest = new VehicleDetailRequest();
        vehicleDetailRequest.setVehicleTypeId(1);
        vehicleDetailRequest.setVehicleName("MARUTHI OMNI");
        vehicleDetailRequest.setVehicleNumber("TN38 AT0298");
        return vehicleDetailRequest;
    }

    private void functionality() {
        uploadContentButton.setOnClickListener(((view) -> {
            if (isAnyItemDocumentSelected()) {
                uploadDocumentViewModel.uploadDocumentRequest(findSelectedDocumentList(), createVehicleDetailRequestObject());
            } else {
                utility.showToastMsg("Please select document");
            }
        }));
        adapter = new UploadDocumentAdapter(this, uploadDocumentActionInfos, screenWidth, screenHeight);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                /*Intent intent = new Intent(UploadDocumentActivity.this, UploadDocumentSecondActivity.class);
                startActivity(intent);*/
                selectedDocumetnsListItem = uploadDocumentActionInfos.get(position);
                showImageFromGalary();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        vehicleTypeRelativeLayout.setOnClickListener(v -> {
            showListPopupScreen(vehicleTypeRelativeLayout, vehicleTypeTextView, vehicleTypesItems);
        });
    }

    private List<DocumentsListItem> findSelectedDocumentList() {
        List<DocumentsListItem> documentsListItems = new ArrayList<>();
        int count = 0;
        do {
            DocumentsListItem documentsListItem = documentsListItems.get(count);
            if (!TextUtils.isEmpty(documentsListItem.getFilePath())) {
                documentsListItems.add(documentsListItem);
            }
        } while (++ count < this.uploadDocumentActionInfos.size());
        return documentsListItems;
    }

    private boolean isAnyItemDocumentSelected() {
        for (DocumentsListItem documentsListItem: uploadDocumentActionInfos) {
            if (!TextUtils.isEmpty(documentsListItem.getFilePath())) {
                return true;
            }
        }
        return false;
    }

    private void showImageFromGalary() {
        Intent intent = new Intent(this, ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_CAMERA, false);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
        startActivityForResult(intent, IMAGE_PICKER);
    }

    private void showListPopupScreen(View view, final TextView updateNameTextView, final List<VehicleTypesItem> list) {
        View customView = LayoutInflater.from(this).inflate(R.layout.spinner_pop_up_screen, null);
        int leftRightSpace = (int) (screenWidth * 0.0153);
        int topBottomSpace = (int) (screenHeight * 0.0089);
        final PopupWindow popupWindow;

        popupWindow = new PopupWindow(customView, leftRightSpace * 58, ViewGroup.LayoutParams.WRAP_CONTENT);

        final ListView listView = customView.findViewById(R.id.listItemListView);

        ArrayAdapter<VehicleTypesItem> dataAdapter = new ArrayAdapter<VehicleTypesItem>(this, R.layout.spinner_dropdown_textview, list) {

            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
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
        listView.setSelection(mSelectedItemType);

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
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
            Log.d(TAG, " The image file path:" + filePath);
            updateDocumentItem(filePath);
        } else {
            utility.showToastMsg("File not found");
        }
    }
}
