package com.codesense.driverapp.ui.documentstatus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.codesense.driverapp.R;
import com.codesense.driverapp.data.DocumentsListItem;
import com.codesense.driverapp.data.DocumentsListStatusResponse;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.Constant;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.codesense.driverapp.ui.uploaddocument.RecyclerTouchListener;
import com.codesense.driverapp.ui.vehicle.VehicleListActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DocumentStatusActivity extends DrawerActivity implements View.OnClickListener {


    //Button btnUpdate;
    RecyclerView recyclerView;
    DocumentStatusAdapter adapter;
    List<DocumentsListItem> arraylist;
    @Inject protected AppSharedPreference appSharedPreference;
    @Inject protected DocumentStatusViewModel documentStatusViewModel;
    @Inject protected Utility utility;

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

    private void handleApiResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
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
                break;
            case ERROR:
                utility.dismissDialog();
                break;
        }
    }

    private void functionality() {
        //btnUpdate.setOnClickListener(this);
        adapter = new DocumentStatusAdapter(this, arraylist, screenWidth, screenHeight);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Intent intent = new Intent(UploadDocumentActivity.this, UploadDocumentSecondActivity.class);
//                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void setDynamicValue() {
        int topBottomSpace = (int) (screenHeight * 0.0089);

        /*RelativeLayout.LayoutParams btnUpdateLayoutParams = (RelativeLayout.LayoutParams) btnUpdate.getLayoutParams();
        btnUpdateLayoutParams.setMargins(topBottomSpace * 2, 0, topBottomSpace * 2, topBottomSpace * 3);*/
        //btnUpdate.setLayoutParams(btnUpdateLayoutParams);

    }

    private void initially() {

        recyclerView = findViewById(R.id.recyclerView);
        //btnUpdate = findViewById(R.id.btnUpdate);
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