package com.codesense.driverapp.ui.uploaddocument;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.codesense.driverapp.ui.helper.Utils;
import com.product.annotationbuilder.ProductBindView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UploadDocumentActivity extends DrawerActivity implements View.OnClickListener {

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

    int mSelectedItemType;
    boolean typeSelFirstTime;
    String typeValue;


    RecyclerView recyclerView;
    UploadDocumentAdapter adapter;

    //DriverAppUI driverAppUI1;

    public static final int UPLOAD_DOCUMENT_STATUS_INDEX = 0;
    public static final int UPLOAD_DOCUMENT_NAME_INDEX = 1;
    private static final int UPLOAD_DOCUMENT_ICON_NAME_INDEX = 2;
    private List<UploadDocumentModel> uploadDocumentActionInfos;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_upload_document, null, false);
        frameLayout.addView(contentView);
        ProductBindView.bind(this);
        uploadDocumentActionInfos = new ArrayList<>();
        titleTextView.setText(getResources().getString(R.string.upload_doc_text));
        initially();
        setDynamicValue();
        setonClickListener();
        functionality();
    }

    private void functionality() {

        int storeInfoActionListSize = getResources().getInteger(R.integer.upload_document_count);
        List<TypedArray> typedArrayList = Utils.getMultiTypedArray(this, Utils.UPLOAD_DOCUMENT);
        if (storeInfoActionListSize > typedArrayList.size()) {
            storeInfoActionListSize = typedArrayList.size();
        }
        for (int index = 0; index < storeInfoActionListSize; index++) {
            TypedArray typedArray = typedArrayList.get(index);
            String status = typedArray.getString(UPLOAD_DOCUMENT_STATUS_INDEX);
            String title = typedArray.getString(UPLOAD_DOCUMENT_NAME_INDEX);
            int icon = 0;
            uploadDocumentActionInfos.add(new UploadDocumentModel(status, title, icon));
        }

        adapter = new UploadDocumentAdapter(this, uploadDocumentActionInfos, screenWidth, screenHeight);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(UploadDocumentActivity.this, UploadDocumentSecondActivity.class);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        String[] itemNames = getResources().getStringArray(R.array.vehicleType);


        final ArrayList<String> vehicle = new ArrayList<>(Arrays.asList(itemNames));


        vehicleTypeRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // stateSelectSpinner.performClick();
                showListPopupScreen(vehicleTypeRelativeLayout, vehicleTypeTextView, "vehicle", vehicle);

            }
        });

    }

    private void showListPopupScreen(View view, final TextView updateNameTextView, final String selectionItem, final List<String> list) {
        View customView = LayoutInflater.from(this).inflate(R.layout.spinner_pop_up_screen, null);
        int leftRightSpace = (int) (screenWidth * 0.0153);
        int topBottomSpace = (int) (screenHeight * 0.0089);
        final PopupWindow popupWindow;

        popupWindow = new PopupWindow(customView, leftRightSpace * 58, ViewGroup.LayoutParams.WRAP_CONTENT);

        final ListView listView = customView.findViewById(R.id.listItemListView);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_textview, list) {

            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = null;
                v = super.getView(position, null, parent);
                TextView tv = (TextView) v;
                if (selectionItem.equalsIgnoreCase("vehicle")) {
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
                }


                return v;
            }
        };
        listView.setAdapter(dataAdapter);
        listView.setSelection(mSelectedItemType);

        final List<String> fState;
        fState = list;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                String selectedState = list.get(position);
                int selectedPosition = fState.indexOf(selectedState);
                // Here is your corresponding country code
                updateNameTextView.setText(selectedState);
                updateNameTextView.setTextColor(getResources().getColor(R.color.secondary_color));
                mSelectedItemType = position;
                typeSelFirstTime = false;

            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(view);
    }

    private void setonClickListener() {
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
    }

    private void setDynamicValue() {

        int topBottomSpace = (int) (screenHeight * 0.0089);

        int imgIconArrowWidth = (int) (screenWidth * 0.075);
        int imgIconArrowHeight = (int) (screenWidth * 0.075);


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
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

        }
    }
}
