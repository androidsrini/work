package com.codesense.driverapp.ui.pickuplocationaccept;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.data.AcceptResponse;
import com.codesense.driverapp.data.AcceptResponseData;
import com.codesense.driverapp.data.AvaliableTripData;
import com.codesense.driverapp.data.TripDetailData;
import com.codesense.driverapp.data.TripDetailsDataRes;
import com.codesense.driverapp.data.UpdateVehicleResponse;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;
import com.codesense.driverapp.ui.accept.StartActivity;
import com.codesense.driverapp.ui.helper.Utils;
import com.google.gson.Gson;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PickUpLocationAcceptActivity extends BaseActivity {

    @Initialize(R.id.progressBarCircle)
    ProgressBar progressBarCircle;
    @Initialize(R.id.textViewTime)
    TextView textViewTime;
    @Initialize(R.id.tvAddress)
    TextView tvAddress;
    @Initialize(R.id.UserName)
    TextView UserName;
    @Initialize(R.id.tvDestinationAddress)
    TextView tvDestinationAddress;
    @Initialize(R.id.userPhoneNum)
    TextView userPhoneNum;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Inject
    RequestHandler requestHandler;

    @Inject
    Utility utility;

    private long timeCountInMilliSeconds = 60000;
    private int timeCountProgressSeconds = 1;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;


    private CountDownTimer countDownTimer;
    UpdateVehicleResponse updateVehicleResponse;
    List<AvaliableTripData> updateVehicleResponseList;
    String bookingId, vehicleId;

    @Inject
    protected AppSharedPreference appSharedPreference;

    @Override
    protected int layoutRes() {
        return R.layout.activity_pickup_location_accept;
    }

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        ProductBindView.bind(this);

        Intent intent = getIntent();
        String picklocation = intent.getStringExtra("picklocation");
        String customerNum = intent.getStringExtra("customerNum");
        String customerName = intent.getStringExtra("customerName");
        String droplocation = intent.getStringExtra("droplocation");
        bookingId = intent.getStringExtra("bookingId");
        tvAddress.setText(picklocation);
        userPhoneNum.setText(customerNum);
        userPhoneNum.setVisibility(View.GONE);
        UserName.setText(customerName);
        tvDestinationAddress.setText(droplocation);

        Utils.saveStringToPrefs(this, "showAccept", "true");


        startStop();
    }

    private void handleApiResponse(ApiResponse apiResponse) {
        ServiceType serviceType = apiResponse.getServiceType();
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (apiResponse.isValidResponse()) {
                    if (ServiceType.ACCEPT_TRIP == serviceType) {
                        //Update webui.
                        Utils.saveIntegerToPrefs(this, "acceptDrive", 1);

                        List<TripDetailsDataRes> arrayList = new ArrayList<>();
                        if (apiResponse.isValidResponse()) {
                            AcceptResponse acceptResponse = new Gson().fromJson(apiResponse.data, AcceptResponse.class);

                            AcceptResponseData acceptResponseData = new AcceptResponseData();
                            acceptResponseData.setStatus(acceptResponse.getStatus());
                            acceptResponseData.setMessage(acceptResponse.getMessage());
                            if (acceptResponse.getTripDetailDataList()!=null) {
                                    TripDetailData tripDetailData = acceptResponse.getTripDetailDataList();
                                    TripDetailsDataRes tripDetailsDataRes = new TripDetailsDataRes();
                                    tripDetailsDataRes.setBooked_for(tripDetailData.getBooked_for());
                                    tripDetailsDataRes.setBooked_on(tripDetailData.getBooked_on());
                                    tripDetailsDataRes.setBooking_id(tripDetailData.getBooking_id());
                                    tripDetailsDataRes.setCustomer_contact(tripDetailData.getCustomer_contact());
                                    tripDetailsDataRes.setCustomer_name(tripDetailData.getCustomer_name());
                                    tripDetailsDataRes.setIs_advance_booking(tripDetailData.getIs_advance_booking());
                                    tripDetailsDataRes.setNormal_fare(tripDetailData.getNormal_fare());
                                    tripDetailsDataRes.setPayment_mode(tripDetailData.getPayment_mode());
                                    tripDetailsDataRes.setPayment_status(tripDetailData.getPayment_status());
                                    tripDetailsDataRes.setPickup_lat(tripDetailData.getPickup_lat());
                                    tripDetailsDataRes.setPickup_lng(tripDetailData.getPickup_lng());
                                    tripDetailsDataRes.setPickup_location(tripDetailData.getPickup_location());
                                    tripDetailsDataRes.setRide_scheduled_on(tripDetailData.getRide_scheduled_on());
                                    tripDetailsDataRes.setTrip_status(tripDetailData.getTrip_status());
                                    tripDetailsDataRes.setVehicle_type_id(tripDetailData.getVehicle_type_id());

                                acceptResponseData.setTripDetailDataList(tripDetailsDataRes);
                            }

                            Utils.saveStringToPrefs(this,"bookingId",bookingId);
                            Intent intent = new Intent(PickUpLocationAcceptActivity.this, StartActivity.class);
                            intent.putExtra("tripDetails", (AcceptResponseData) acceptResponseData);
                            startActivity(intent);

                        }
                        finish();
                    } else if (ServiceType.CANCEL_TRIP == serviceType) {
                        Utils.saveIntegerToPrefs(this, "acceptDrive", 0);
                        Utils.saveStringToPrefs(this, "cancelbookingId", bookingId);
                        utility.showToastMsg(apiResponse.getResponseMessage());
                        finish();
                    }
                } else {
                    utility.showToastMsg(apiResponse.getResponseMessage());
                    finish();
                }
                break;
            case ERROR:
                utility.showToastMsg(apiResponse.getResponseMessage());
                utility.dismissDialog();
                finish();
                break;
        }
    }

    private void acceptRequest() {
        compositeDisposable.add(requestHandler.acceptRequest(ApiUtility.getInstance().getApiKeyMetaData(), bookingId,Utils.getStringToPrefs(this,"vehicleId"),appSharedPreference.getLastLocationLatitude(),appSharedPreference.getLastLocationLng())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> handleApiResponse(ApiResponse.loading(ServiceType.ACCEPT_TRIP)))
                .subscribe(result -> handleApiResponse(ApiResponse.success(ServiceType.ACCEPT_TRIP, result)),
                        error -> handleApiResponse(ApiResponse.error(ServiceType.ACCEPT_TRIP, error))));
    }

    private void cancelRequest() {
        Utils.saveIntegerToPrefs(this, "acceptDrive", 0);
        Utils.saveStringToPrefs(this, "cancelbookingId", bookingId);
        finish();
       /* compositeDisposable.add(requestHandler.cancelRequest(ApiUtility.getInstance().getApiKeyMetaData(), bookingId,Utils.getStringToPrefs(this,"vehicleId"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> handleApiResponse(ApiResponse.loading(ServiceType.CANCEL_TRIP)))
                .subscribe(result -> handleApiResponse(ApiResponse.success(ServiceType.CANCEL_TRIP, result)),
                        error -> handleApiResponse(ApiResponse.error(ServiceType.CANCEL_TRIP, error))));*/
    }

    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {

            // call to initialize the progress bar values
            setProgressBarValues();

            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();

        } else {
            stopCountDownTimer();
        }

    }


    /**
     * method to start count down timer
     */
    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                timeCountProgressSeconds++;
                progressBarCircle.setProgress(timeCountProgressSeconds);
                progressBarCircle.setMax(120);

            }

            @Override
            public void onFinish() {
                timeCountProgressSeconds++;
                progressBarCircle.setProgress(timeCountProgressSeconds);
                Utils.saveIntegerToPrefs(PickUpLocationAcceptActivity.this, "acceptDrive", 0);
                finish();
                timerStatus = TimerStatus.STOPPED;
            }

        }.start();
        countDownTimer.start();
    }

    private void setProgressBarValues() {

        progressBarCircle.setProgress(timeCountProgressSeconds);
        progressBarCircle.setMax(timeCountProgressSeconds);
    }

    /**
     * method to stop count down timer
     */
    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }


    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
        return hms;
    }

    @Onclick(R.id.btnAccept)
    public void btnAccept(View v) {
        //Utils.saveIntegerToPrefs(this, "acceptDrive", 1);
        //TODO show accept screen
        acceptRequest();
        Utils.saveStringToPrefs(this, "showAccept", "false");

    }

    @Onclick(R.id.btnReject)
    public void btnReject(View v) {
        Utils.saveStringToPrefs(this, "showAccept", "false");
        cancelRequest();
    }


}
