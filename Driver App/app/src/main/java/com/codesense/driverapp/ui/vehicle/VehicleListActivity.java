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
import com.codesense.driverapp.data.VehicleListResponse;
import com.codesense.driverapp.data.VehiclesListItem;
import com.codesense.driverapp.di.utils.RecyclerTouchListener;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;
import com.codesense.driverapp.ui.addvehicle.AddVehicleActivity;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


@SuppressLint("Registered")
public class VehicleListActivity extends DrawerActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    Button btnAddVehicle;
    VehicleListAdapter adapter;
    @Inject protected VehicleListViewModel vehicleListViewModel;
    @Inject protected RequestHandler requestHandler;
    @Inject protected Utility utility;
    private List<VehiclesListItem> vehiclesItemList;

    public static void start(Context context) {
        Intent starter = new Intent(context, VehicleListActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_vehicle_list, null, false);
        frameLayout.addView(contentView);
        titleTextView.setText(getResources().getString(R.string.vehicle_list_title));
        vehicleListViewModel.getApiResponseMutableLiveData().observe(this, this::handleApiResponse);
        //Need to implement load more concept
        vehicleListViewModel.fetchOwnerVehiclesRequest();
        initially();
        setDynamicValue();
        initializeListener();
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
                if (ServiceType.GET_OWNER_VEHICLES == serviceType) {
                    VehicleListResponse vehiclesListsResponse = new Gson().fromJson(apiResponse.data, VehicleListResponse.class);
                    if (null != vehiclesListsResponse && null != vehiclesListsResponse.getVehiclesListItems()) {
                        //Clear all old values
                        if (!vehiclesItemList.isEmpty()) {
                            vehiclesItemList.clear();
                        }
                        vehiclesItemList.addAll(vehiclesListsResponse.getVehiclesListItems());
                    }
                    //notify adapter
                    adapter.notifyDataSetChanged();
                }
                break;
            case ERROR:
                utility.dismissDialog();
                break;
        }
    }

    private void initializeListener() {
        btnAddVehicle.setOnClickListener(this);
    }

    private void clearListener() {
        btnAddVehicle.setOnClickListener(this);
    }

    private void functionality() {
        adapter = new VehicleListAdapter(this, vehiclesItemList, screenWidth, screenHeight);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                utility.showConformationDialog(VehicleListActivity.this,
                        getString(R.string.vehicle_edit_confirmation), (dialog, which) -> {
                            VehiclesListItem vehiclesListItem = vehiclesItemList.get(position);
                            AddVehicleActivity.start(VehicleListActivity.this, vehiclesListItem);
                        });
            }

            @Override
            public void onLongClick(View view, int position) {
                //Show vehicle edit screen
                //vehiclesListItem.
            }
        }));
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
        vehiclesItemList = new ArrayList<>();
        btnAddVehicle = findViewById(R.id.btnAddVehicle);
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    protected void onDestroy() {
        clearListener();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddVehicle:
                AddVehicleActivity.start(this);
                break;
                /*Intent intent = new Intent(this, AddVehicleActivity.class);
                startActivity(intent);*/
        }
    }
}
