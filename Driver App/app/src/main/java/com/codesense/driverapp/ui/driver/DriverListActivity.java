package com.codesense.driverapp.ui.driver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.codesense.driverapp.R;
import com.codesense.driverapp.data.DriverListResponse;
import com.codesense.driverapp.data.DriversListItem;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.Constant;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;
import com.codesense.driverapp.ui.adddriver.AddDriverActivity;
import com.codesense.driverapp.ui.documentstatus.DocumentStatusActivity;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DriverListActivity extends DrawerActivity {

    @Inject
    protected DriverListViewModel driverListViewModel;
    @Inject protected RequestHandler requestHandler;
    @Inject protected Utility utility;
    private RecyclerView driverRecyclerView;
    private DriverListAdapter driverListAdapter;
    private List<DriversListItem> driversListItemList;
    private AppCompatTextView tvNodrivers;

    public static void start(Context context) {
        Intent starter = new Intent(context, DriverListActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_driver_list);
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_driver_list, null, false);
        frameLayout.addView(contentView);
        titleTextView.setText(getResources().getString(R.string.drivers_label));
        driverRecyclerView = contentView.findViewById(R.id.driver_recyclerView);
        tvNodrivers = contentView.findViewById(R.id.tvNodrivers);
        //Initialize array list and adapter object
        driversListItemList = new ArrayList<>();
        driverListAdapter = new DriverListAdapter(this, driversListItemList, screenWidth, screenHeight, new DriverListAdapter.OnItemActionListener() {
            @Override
            public void onViewClick(int position) {
                //show driver document status screen.
                DriversListItem driversListItem = driversListItemList.get(position);
                DocumentStatusActivity.start(DriverListActivity.this, Constant.DRIVER_DOCUMENT_STATUS,driversListItem);
            }

            @Override
            public void onButtonClick(int position, boolean isChecked) {
                //call active api here.
                DriversListItem driversListItem = driversListItemList.get(position);
                driverListViewModel.postDrivingActivationRequest(isChecked ? Constant.ACTIVE :
                        Constant.INACTIVE, driversListItem.getVehicleId(), driversListItem.getDriverId());
            }

            @Override
            public void onEditActionClick(int position) {
                utility.showConformationDialog(DriverListActivity.this,
                        getString(R.string.vehicle_edit_confirmation), (dialog, which) -> {
                            DriversListItem driversListItem = driversListItemList.get(position);
                            AddDriverActivity.start(DriverListActivity.this, driversListItem);
                        });
            }
        });
        driverRecyclerView.setAdapter(driverListAdapter);
        //initialize view modle observer
        driverListViewModel.getApiResponseMutableLiveData().observe(this, this::handleApiResponse);
        contentView.findViewById(R.id.btnAddDriver).setOnClickListener(v -> AddDriverActivity.start(DriverListActivity.this));
        driverListViewModel.getDriversListRequest();
    }

    private void handleApiResponse(ApiResponse apiResponse) {
        ServiceType serviceType = apiResponse.getServiceType();
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (ServiceType.GET_DRIVERS_LIST == serviceType) {
                    DriverListResponse driverListResponse = new Gson().fromJson(apiResponse.data, DriverListResponse.class);
                    if (null != driverListResponse && driverListResponse.getDriversList().size()>0) {
                        driversListItemList.addAll(driverListResponse.getDriversList());
                        driverRecyclerView.setVisibility(View.VISIBLE);
                        tvNodrivers.setVisibility(View.GONE);
                        driverListAdapter.notifyDataSetChanged();
                    } else{
                        driverRecyclerView.setVisibility(View.GONE);
                        tvNodrivers.setVisibility(View.VISIBLE);
                    }/*else {
                        //To show static driver list for testing
                        String driverList = utility.findAssetFileString(this, Utility.GET_DRIVER_LIST);
                        driverListResponse = new Gson().fromJson(driverList, DriverListResponse.class);
                        driversListItemList.addAll(driverListResponse.getDriversList());
                    }*/
                } else if (ServiceType.DRIVING_ACTIVATION == serviceType) {
                    if (apiResponse.isValidResponse()) {
                        utility.showToastMsg("Updated");
                    } else {
                        utility.showToastMsg(apiResponse.getResponseMessage());
                    }
                }
                break;
            case ERROR:
                utility.dismissDialog();
                break;
        }
    }
}
