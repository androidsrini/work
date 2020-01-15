package com.codesense.driverapp.ui.adddriver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.codesense.driverapp.R;
import com.codesense.driverapp.data.AddDriverRequest;
import com.codesense.driverapp.data.AvailableVehiclesItem;
import com.codesense.driverapp.di.utils.AppSpinnerViewGroup;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.ui.drawer.DrawerActivity;

import javax.inject.Inject;

public class AddDriverActivity extends DrawerActivity {

    private static final String TAG = AddDriverActivity.class.getSimpleName();
    EditText etDriverFirstName, etDriverLastName, etDriverContNum, etDriverEmail, etDriverPassword, etDriverConPassword;
    AppSpinnerViewGroup<AvailableVehiclesItem> vehicleAppSpinnerViewGroup;
    @Inject
    Utility utility;
    @Inject
    AppSharedPreference appSharedPreference;
    @Inject
    protected RequestHandler requestHandler;
    @Inject
    DriverViewModel driverViewModel;

    public static void start(Context context) {
        Intent starter = new Intent(context, AddDriverActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_add_driver);*/
        View view = LayoutInflater.from(this).inflate(R.layout.activity_add_driver, null);
        frameLayout.addView(view);
        titleTextView.setText(getResources().getString(R.string.add_driver));
        //init views
        etDriverFirstName = view.findViewById(R.id.etDriverName);
        etDriverContNum = view.findViewById(R.id.etDriverContNum);
        etDriverEmail = view.findViewById(R.id.etDriverEmail);
        etDriverPassword = view.findViewById(R.id.etDriverPassword);
        etDriverConPassword = view.findViewById(R.id.etDriverConPassword);
        vehicleAppSpinnerViewGroup = view.findViewById(R.id.vehicle_appSpinnerViewGroup);
            /*if (isValidVehicleFields() && isValidAddNewDriverFields()) {
                addVehicleViewModel.addDriverRequest(createAddDriverRequestObject());
            }*/
        //driverViewModel = ViewModelProviders.of(this, new ViewModelFactory(requestHandler)).get(DriverViewModel.class);
        driverViewModel.getApiResponseMutableLiveData().observe(this, this::apiResponseHandler);
        driverViewModel.fetchVehiclesListRequest();
    }

    private void apiResponseHandler(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                Log.d(TAG, "response: " + apiResponse.data);
                if (apiResponse.isValidResponse()) {
                    vehicleAppSpinnerViewGroup.setVisibility(View.VISIBLE);
                }
                break;
            case ERROR:
                utility.dismissDialog();
                Log.d(TAG, "response error: " + apiResponse.error);
                break;
        }
    }

    private String getEtDriverFirstName() {
        return etDriverFirstName.getText().toString().trim();

    }

    private String getEtDriverLastName() {
        return etDriverLastName.getText().toString().trim();
    }

    private String getEtDriverContNum() {
        return etDriverContNum.getText().toString().trim();
    }

    private String getEtDriverEmail() {
        return etDriverEmail.getText().toString().trim();

    }

    private String getEtDriverPassword() {
        return etDriverPassword.getText().toString().trim();
    }

    private String getEtDriverConPassword() {
        return etDriverConPassword.getText().toString().trim();
    }

    private boolean isValidAddNewDriverFields() {
        boolean isValid = true;
        if (TextUtils.isEmpty(getEtDriverFirstName())) {
            utility.showToastMsg("Driver First Name Required");
            isValid = false;
        } else if (TextUtils.isEmpty(getEtDriverLastName())) {
            utility.showToastMsg("Driver Last Name Required");
            isValid = false;
        } else if (TextUtils.isEmpty(getEtDriverContNum())) {
            utility.showToastMsg("Driver Contact Number Required");
            isValid = false;
        } else if (TextUtils.isEmpty(getEtDriverEmail())) {
            utility.showToastMsg("Driver Email Required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(getEtDriverEmail()).matches()) {
            utility.showToastMsg("Email id not valid");
            isValid = false;
        } else if (TextUtils.isEmpty(getEtDriverPassword())) {
            utility.showToastMsg("Driver Password Required");
            isValid = false;
        } else if (TextUtils.isEmpty(getEtDriverConPassword())) {
            utility.showToastMsg("Driver Confirm Password Required");
            isValid = false;
        } else if (!getEtDriverPassword().equals(getEtDriverConPassword())) {
            utility.showToastMsg("Password does not match");
            isValid = false;
        } /*else if (TextUtils.isEmpty(getAddVehicleCountryAutoCompleteTextView())) {
                utility.showToastMsg("Country required");
                isValid = false;
            }*/
        return isValid;
    }

        /*private void updateAvailableDriversUI(List<AvailableDriversItem> availableDriversItems) {
            CrashlyticsHelper.d("UpdateAvailableDriversUI");
            ArrayAdapter<AvailableDriversItem> adapter = new ArrayAdapter<AvailableDriversItem>(this, android.R.layout.simple_spinner_item, availableDriversItems);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            etDriverName.setAdapter(adapter);
            etDriverName.setOnItemClickListener((adapterView, view, i, l) -> {
                availableDriversItem = adapter.getItem(i);
                if (null != availableDriversItem) {
                    etDriverFirstName.setText(availableDriversItem.getDriverFirstName());
                    etDriverLastName.setText(availableDriversItem.getDriverLastName());
                    etDriverEmail.setText(availableDriversItem.getDriverEmailId());
                *//*driverPasswordTextInputLayout.setVisibility(View.GONE);
                driverConTextInputLayout.setVisibility(View.GONE);*//*
                }
            });
        }*/

    private AddDriverRequest createAddDriverRequestObject() {
        AddDriverRequest addDriverRequest = new AddDriverRequest();
            /*if (null != countriesItem) {
                addDriverRequest.setCountryId(countriesItem.getCountryId());
            }*/
        addDriverRequest.setDriverFirstName(getEtDriverFirstName());
        addDriverRequest.setDriverLastName(getEtDriverLastName());
        addDriverRequest.setEmailId(getEtDriverEmail());
        addDriverRequest.setMobileNumber(getEtDriverContNum());
        addDriverRequest.setPassword(getEtDriverPassword());
        addDriverRequest.setUserId(appSharedPreference.getUserID());
        return addDriverRequest;
    }

    private void clear() {
        etDriverFirstName.setText(null);
        etDriverLastName.setText(null);
        etDriverContNum.setText(null);
        etDriverEmail.setText(null);
        etDriverPassword.setText(null);
        etDriverConPassword.setText(null);
    }

}
