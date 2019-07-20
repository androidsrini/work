package com.codesense.driverapp.ui.documentstatus;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.codesense.driverapp.R;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.codesense.driverapp.ui.helper.Utils;
import com.codesense.driverapp.ui.uploaddocument.RecyclerTouchListener;
import com.codesense.driverapp.ui.uploaddocument.UploadDocumentActivity;
import com.codesense.driverapp.ui.uploaddocument.UploadDocumentModel;
import com.codesense.driverapp.ui.vehicle.VehicleListActivity;

import java.util.ArrayList;
import java.util.List;

public class DocumentStatusActivity extends DrawerActivity implements View.OnClickListener {


    Button btnUpdate;
    RecyclerView recyclerView;
    DocumentStatusAdapter adapter;
    List<UploadDocumentModel> arraylist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_document_status, null, false);
        frameLayout.addView(contentView);

        arraylist = new ArrayList<>();
        titleTextView.setText(getResources().getString(R.string.document_status_title));
        initially();
        setDynamicValue();
        functionality();

    }

    private void functionality() {

        btnUpdate.setOnClickListener(this);
        int storeInfoActionListSize = getResources().getInteger(R.integer.document_status_count);
        List<TypedArray> typedArrayList = Utils.getMultiTypedArray(this, Utils.DOCUMENT_STATUS);
        if (storeInfoActionListSize > typedArrayList.size()) {
            storeInfoActionListSize = typedArrayList.size();
        }
        for (int index = 0; index < storeInfoActionListSize; index++) {
            TypedArray typedArray = typedArrayList.get(index);
            String status = typedArray.getString(UploadDocumentActivity.UPLOAD_DOCUMENT_STATUS_INDEX);
            String title = typedArray.getString(UploadDocumentActivity.UPLOAD_DOCUMENT_NAME_INDEX);
            int icon = 0;
            arraylist.add(new UploadDocumentModel(status, title, icon));
        }

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

        RelativeLayout.LayoutParams btnUpdateLayoutParams = (RelativeLayout.LayoutParams) btnUpdate.getLayoutParams();
        btnUpdateLayoutParams.setMargins(topBottomSpace * 2, 0, topBottomSpace * 2, topBottomSpace * 3);
        btnUpdate.setLayoutParams(btnUpdateLayoutParams);

    }

    private void initially() {

        recyclerView = findViewById(R.id.recyclerView);
        btnUpdate = findViewById(R.id.btnUpdate);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnUpdate:
                Intent intent = new Intent(this, VehicleListActivity.class);
                startActivity(intent);
        }
    }
}