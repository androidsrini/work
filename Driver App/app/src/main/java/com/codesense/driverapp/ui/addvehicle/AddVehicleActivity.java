package com.codesense.driverapp.ui.addvehicle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.data.VehicleTypeResponse;
import com.codesense.driverapp.data.VehicleTypesItem;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

public class AddVehicleActivity extends DrawerActivity implements View.OnClickListener {


    ScrollView scrollView;
    //RelativeLayout vehicleTypeRelativeLayout;
    //TextView vehicleTypeTextView;
    //ImageView vehicleTypeArrowImageView;
    //View view, view1, view2, view3, view4, view5, view6, view7;
    EditText etVehicleName;
    EditText etVehicleNumber;
    EditText etDriverName;
    EditText etDriverContNum;
    EditText etDriverEmail;
    EditText etDriverPassword;
    EditText etDriverConPassword;
    Button btnAddVehicle;
    private AppCompatSpinner vehicleTypeAppCompatSpinner;
    private List<VehicleTypesItem> vehicleTypesItems;
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
        addVehicleViewModel.getApiResponseMutableLiveData().observe(this, this::handleApiResponse);
        addVehicleViewModel.fetchVehicleTypesRequest();
        initially();
        setDynamicValue();
        functionality();
    }

    private void handleApiResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (apiResponse.isValidResponse()) {
                    VehicleTypeResponse vehicleTypeResponse = new Gson().fromJson(apiResponse.data, VehicleTypeResponse.class);
                    if (null != vehicleTypeResponse && null != vehicleTypeResponse.getVehicleTypes()) {
                        vehicleTypeSpinnerUI(vehicleTypeResponse.getVehicleTypes());
                    }
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
        //vehicleTypeRelativeLayout = findViewById(R.id.vehicleTypeRelativeLayout);
        //vehicleTypeTextView = findViewById(R.id.vehicleTypeTextView);
        //vehicleTypeArrowImageView = findViewById(R.id.vehicleTypeArrowImageView);
        etVehicleName = findViewById(R.id.etVehicleName);
        etVehicleNumber = findViewById(R.id.etVehicleNumber);
        etDriverName = findViewById(R.id.etDriverName);
        etDriverContNum = findViewById(R.id.etDriverContNum);
        etDriverEmail = findViewById(R.id.etDriverEmail);
        etDriverPassword = findViewById(R.id.etDriverPassword);
        etDriverConPassword = findViewById(R.id.etDriverConPassword);
        btnAddVehicle = findViewById(R.id.btnAddVehicle);
       /* view = findViewById(R.id.view);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);
        view5 = findViewById(R.id.view5);
        view6 = findViewById(R.id.view6);
        view7 = findViewById(R.id.view7);*/
    }

    private void setDynamicValue() {
        int topBottomSpace = (int) (screenHeight * 0.0089);


        int imgIconArrowWidth = (int) (screenWidth * 0.075);
        int imgIconArrowHeight = (int) (screenWidth * 0.075);


        /*RelativeLayout.LayoutParams vehicleTypeArrowImageViewimgLayParams = (RelativeLayout.LayoutParams) vehicleTypeArrowImageView.getLayoutParams();
        vehicleTypeArrowImageViewimgLayParams.width = imgIconArrowWidth;
        vehicleTypeArrowImageViewimgLayParams.height = imgIconArrowHeight;
        vehicleTypeArrowImageView.setLayoutParams(vehicleTypeArrowImageViewimgLayParams);*/

        /*RelativeLayout.LayoutParams scrollViewLayoutParams = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
        scrollViewLayoutParams.setMargins(topBottomSpace * 5, topBottomSpace * 6, topBottomSpace * 5, 0);
        scrollView.setLayoutParams(scrollViewLayoutParams);

        ConstraintLayout.LayoutParams vehicleTypeRelativeLayoutLayoutParams = (ConstraintLayout.LayoutParams) etVehicleName.getLayoutParams();
        vehicleTypeRelativeLayoutLayoutParams.setMargins(0, topBottomSpace * 3, 0, 0);
        etVehicleName.setLayoutParams(vehicleTypeRelativeLayoutLayoutParams);

        ConstraintLayout.LayoutParams etVehicleNumberLayoutParams = (ConstraintLayout.LayoutParams) etVehicleNumber.getLayoutParams();
        etVehicleNumberLayoutParams.setMargins(0, topBottomSpace * 3, 0, 0);
        etVehicleNumber.setLayoutParams(etVehicleNumberLayoutParams);

        ConstraintLayout.LayoutParams etDriverNameLayoutParams = (ConstraintLayout.LayoutParams) etDriverName.getLayoutParams();
        etDriverNameLayoutParams.setMargins(0, topBottomSpace * 3, 0, 0);
        etDriverName.setLayoutParams(etDriverNameLayoutParams);

        ConstraintLayout.LayoutParams etDriverContNumLayoutParams = (ConstraintLayout.LayoutParams) etDriverContNum.getLayoutParams();
        etDriverContNumLayoutParams.setMargins(0, topBottomSpace * 3, 0, 0);
        etDriverContNum.setLayoutParams(etDriverContNumLayoutParams);

        ConstraintLayout.LayoutParams etDriverEmailLayoutParams = (ConstraintLayout.LayoutParams) etDriverEmail.getLayoutParams();
        etDriverEmailLayoutParams.setMargins(0, topBottomSpace * 3, 0, 0);
        etDriverEmail.setLayoutParams(etDriverEmailLayoutParams);

        ConstraintLayout.LayoutParams etDriverPasswordLayoutParams = (ConstraintLayout.LayoutParams) etDriverPassword.getLayoutParams();
        etDriverPasswordLayoutParams.setMargins(0, topBottomSpace * 3, 0, 0);
        etDriverPassword.setLayoutParams(etDriverPasswordLayoutParams);

        ConstraintLayout.LayoutParams etDriverConPasswordLayoutParams = (ConstraintLayout.LayoutParams) etDriverConPassword.getLayoutParams();
        etDriverConPasswordLayoutParams.setMargins(0, topBottomSpace * 3, 0, 0);
        etDriverConPassword.setLayoutParams(etDriverConPasswordLayoutParams);*/

        RelativeLayout.LayoutParams btnUpdateLayoutParams = (RelativeLayout.LayoutParams) btnAddVehicle.getLayoutParams();
        btnUpdateLayoutParams.setMargins(topBottomSpace * 2, 0, topBottomSpace * 2, topBottomSpace * 3);
        btnAddVehicle.setLayoutParams(btnUpdateLayoutParams);

        btnAddVehicle.setOnClickListener(this);

    }

    private void functionality() {
    }

    private void vehicleTypeSpinnerUI(List<VehicleTypesItem> vehicleTypesItems) {
        ArrayAdapter<VehicleTypesItem> adapter = new ArrayAdapter<VehicleTypesItem>(this, android.R.layout.simple_spinner_item, vehicleTypesItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleTypeAppCompatSpinner.setAdapter(adapter);
        vehicleTypeAppCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String companyName = vehicleTypesItems.get(i).getVehicleType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddVehicle) {
            /*Intent intent = new Intent(this, OnlineActivity.class);
            startActivity(intent);*/
        }
    }
}
