package com.codesense.driverapp.ui.driverdocumentstatus;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.codesense.driverapp.R;
import com.codesense.driverapp.ui.drawer.DrawerActivity;

public class DriverDocumentStatusActivity extends DrawerActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_driver_list, null, false);
        frameLayout.addView(contentView);
        titleTextView.setText(getResources().getString(R.string.drivers_label));
    }
}
