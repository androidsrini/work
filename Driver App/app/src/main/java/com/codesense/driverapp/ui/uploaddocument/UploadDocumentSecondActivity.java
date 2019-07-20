package com.codesense.driverapp.ui.uploaddocument;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.codesense.driverapp.R;
import com.codesense.driverapp.ui.documentstatus.DocumentStatusActivity;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.codesense.driverapp.ui.helper.Utils;
import com.product.annotationbuilder.ProductBindView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("Registered")
public class UploadDocumentSecondActivity extends DrawerActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    UploadDocumentSecondAdapter adapter;
    List<UploadDocumentModel> arraylist;
    Button btnVerify;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_upload_document_second, null, false);
        frameLayout.addView(contentView);
        ProductBindView.bind(this);
        arraylist = new ArrayList<>();
        titleTextView.setText(getResources().getString(R.string.upload_doc_text));
        initially();
        setDynamicValue();
        setOnClicklistener();
        functionality();
    }

    private void setOnClicklistener() {
        btnVerify.setOnClickListener(this);
    }

    private void functionality() {

        int storeInfoActionListSize = getResources().getInteger(R.integer.upload_document2_count);
        List<TypedArray> typedArrayList = Utils.getMultiTypedArray(this, Utils.UPLOAD_DOCUMENT2);
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

        adapter = new UploadDocumentSecondAdapter(this, arraylist, screenWidth, screenHeight);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setDynamicValue() {

        int topBottomSpace = (int) (screenHeight * 0.0089);


        ConstraintLayout.LayoutParams rlVehicleRegLayoutParams = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
        rlVehicleRegLayoutParams.setMargins(0, topBottomSpace * 6, 0, 0);
        recyclerView.setLayoutParams(rlVehicleRegLayoutParams);


        ConstraintLayout.LayoutParams btnVerifyLayoutParams = (ConstraintLayout.LayoutParams) btnVerify.getLayoutParams();
        btnVerifyLayoutParams.setMargins(topBottomSpace * 5, 0, topBottomSpace * 5, topBottomSpace * 10);
        btnVerify.setLayoutParams(btnVerifyLayoutParams);
    }

    private void initially() {


        btnVerify = findViewById(R.id.btnVerify);
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnVerify:
                Intent intent = new Intent(this, DocumentStatusActivity.class);
                startActivity(intent);
        }
    }
}
