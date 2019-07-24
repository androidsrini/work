package com.codesense.driverapp.ui.vehicle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.codesense.driverapp.R;
import com.codesense.driverapp.ui.addvehicle.AddVehicleActivity;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.product.annotationbuilder.ProductBindView;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("Registered")
public class VehicleListActivity extends DrawerActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    Button btnAddVehicle;
    VehicleListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_vehicle_list, null, false);
        frameLayout.addView(contentView);
//        ProductBindView.bind(this);
        titleTextView.setText(getResources().getString(R.string.vehicle_list_title));
        initially();
        setDynamicValue();
        functionality();
    }

    private void functionality() {
        btnAddVehicle.setOnClickListener(this);
        List<VehicleListModel> arrayList = new ArrayList<>();
        adapter = new VehicleListAdapter(this, arrayList, screenWidth, screenHeight);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setDynamicValue() {

        int topBottomSpace = (int) (screenHeight * 0.0089);

        ConstraintLayout.LayoutParams recyclerViewLayoutParams = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
        recyclerViewLayoutParams.setMargins(0, topBottomSpace * 2, 0, 0);
        recyclerView.setLayoutParams(recyclerViewLayoutParams);

        ConstraintLayout.LayoutParams btnUpdateLayoutParams = (ConstraintLayout.LayoutParams) btnAddVehicle.getLayoutParams();
        btnUpdateLayoutParams.setMargins(topBottomSpace * 2, 0, topBottomSpace * 2, topBottomSpace * 3);
        btnAddVehicle.setLayoutParams(btnUpdateLayoutParams);

    }

    private void initially() {
        btnAddVehicle = findViewById(R.id.btnAddVehicle);
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnAddVehicle:
                Intent intent = new Intent(this, AddVehicleActivity.class);
                startActivity(intent);
        }
    }
}
