package com.codesense.driverapp.ui.addvehicle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.codesense.driverapp.ui.online.OnlineActivity;

public class AddVehicleActivity extends DrawerActivity implements View.OnClickListener {


    ScrollView scrollView;
    RelativeLayout vehicleTypeRelativeLayout;
    TextView vehicleTypeTextView;
    ImageView vehicleTypeArrowImageView;
    View view, view1, view2, view3, view4, view5, view6, view7;
    EditText etVehicleName;
    EditText etVehicleNumber;
    EditText etDriverName;
    EditText etDriverContNum;
    EditText etDriverEmail;
    EditText etDriverPassword;
    EditText etDriverConPassword;
    Button btnAddVehicle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_add_vehicle, null, false);
        frameLayout.addView(contentView);

        titleTextView.setText(getResources().getString(R.string.add_vehicle_title));
        initially();
        setDynamicValue();
        functionality();
    }

    private void initially() {
        scrollView = findViewById(R.id.scrollView);
        vehicleTypeRelativeLayout = findViewById(R.id.vehicleTypeRelativeLayout);
        vehicleTypeTextView = findViewById(R.id.vehicleTypeTextView);
        vehicleTypeArrowImageView = findViewById(R.id.vehicleTypeArrowImageView);
        etVehicleName = findViewById(R.id.etVehicleName);
        etVehicleNumber = findViewById(R.id.etVehicleNumber);
        etDriverName = findViewById(R.id.etDriverName);
        etDriverContNum = findViewById(R.id.etDriverContNum);
        etDriverEmail = findViewById(R.id.etDriverEmail);
        etDriverPassword = findViewById(R.id.etDriverPassword);
        etDriverConPassword = findViewById(R.id.etDriverConPassword);
        btnAddVehicle = findViewById(R.id.btnAddVehicle);
        view = findViewById(R.id.view);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);
        view5 = findViewById(R.id.view5);
        view6 = findViewById(R.id.view6);
        view7 = findViewById(R.id.view7);
    }

    private void setDynamicValue() {
        int topBottomSpace = (int) (screenHeight * 0.0089);


        int imgIconArrowWidth = (int) (screenWidth * 0.075);
        int imgIconArrowHeight = (int) (screenWidth * 0.075);


        RelativeLayout.LayoutParams vehicleTypeArrowImageViewimgLayParams = (RelativeLayout.LayoutParams) vehicleTypeArrowImageView.getLayoutParams();
        vehicleTypeArrowImageViewimgLayParams.width = imgIconArrowWidth;
        vehicleTypeArrowImageViewimgLayParams.height = imgIconArrowHeight;
        vehicleTypeArrowImageView.setLayoutParams(vehicleTypeArrowImageViewimgLayParams);

        RelativeLayout.LayoutParams scrollViewLayoutParams = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
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
        etDriverConPassword.setLayoutParams(etDriverConPasswordLayoutParams);

        RelativeLayout.LayoutParams btnUpdateLayoutParams = (RelativeLayout.LayoutParams) btnAddVehicle.getLayoutParams();
        btnUpdateLayoutParams.setMargins(topBottomSpace * 2, 0, topBottomSpace * 2, topBottomSpace * 3);
        btnAddVehicle.setLayoutParams(btnUpdateLayoutParams);

        btnAddVehicle.setOnClickListener(this);

    }

    private void functionality() {
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddVehicle) {
            Intent intent = new Intent(this, OnlineActivity.class);
            startActivity(intent);
        }
    }
}
