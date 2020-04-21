package com.codesense.driverapp.ui.accept;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.data.AcceptResponse;
import com.codesense.driverapp.data.AcceptResponseData;
import com.codesense.driverapp.data.CompleteTripData;
import com.codesense.driverapp.data.CompleteTripResponse;
import com.codesense.driverapp.data.TripDetailData;
import com.codesense.driverapp.data.TripDetailsDataRes;
import com.codesense.driverapp.data.TripSummaryPassData;
import com.codesense.driverapp.data.UpdateVehicleResponse;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;
import com.codesense.driverapp.ui.helper.LocationMonitoringService;
import com.codesense.driverapp.ui.helper.OnSwipeTouchListener;
import com.codesense.driverapp.ui.helper.Utils;
import com.codesense.driverapp.ui.online.OnlineViewModel;
import com.codesense.driverapp.ui.paymentsummary.PaymentSummaryNewActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class StartActivity extends BaseActivity implements OnMapReadyCallback {


    @Inject
    RequestHandler requestHandler;
    /**
     * To create Utility object.
     */
    @Inject
    Utility utility;
    @Initialize(R.id.tvPickLocation)
    TextView tvPickLocation;
    @Initialize(R.id.pickupTitleTextView)
    TextView pickupTitleTextView;
    @Initialize(R.id.UserName)
    TextView UserName;
    @Initialize(R.id.imgCall)
    ImageView imgCall;
    @Initialize(R.id.startTripConstrainLinearLayout)
    LinearLayout startTripConstrainLinearLayout;
    @Initialize(R.id.endTripConstrainRelativeLayout)
    RelativeLayout endTripConstrainRelativeLayout;

    @Initialize(R.id.toolbarClose)
    ImageView toolbarClose;
    @Initialize(R.id.tvTitle)
    TextView tvTitle;
    @Initialize(R.id.btnAccept)
    Button btnAccept;
    @Initialize(R.id.btnReject)
    Button btnReject;
    GoogleMap map;
    AcceptResponseData acceptResponseData;
    ImageView imgClose;
    public boolean isRideStart = false;

    Intent intentService;

    double totalDistance, passedDistance;
    static double totalDist = 0.0;
    boolean isFirst;


    Marker m;
    private List<LatLng> startlist = new ArrayList<>();
    private double kDegreesToRadians = Math.PI / 180.0;
    private double kRadiansToDegrees = 180.0 / Math.PI;

    @Inject
    protected OnlineViewModel onlineViewModel;


    ScheduledExecutorService scheduleTaskExecutor;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;


    @Inject
    protected AppSharedPreference appSharedPreference;

    BottomSheetDialog bottomSheetDialog;
    View v;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    BroadcastReceiver broadcastReceiver;
    String currentLat, currentLng;
    Double picklat, picklng;
    Double deslat, deslng;
    String updateFare;
    List<HashMap<String, String>> wayPointList = new ArrayList<>();
    static String totalFare = "0";


    @Override
    protected int layoutRes() {
        return R.layout.activity_accept;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);

        setDynamicValue();

        Intent intent = getIntent();
//        AcceptResponseData acceptResponseData = (AcceptResponseData) getIntent().getSerializableExtra("tripDetails");
        acceptResponseData = (AcceptResponseData) intent.getSerializableExtra("tripDetails");

        if (acceptResponseData != null) {
            picklat = Double.parseDouble(acceptResponseData.getTripDetailDataList().getPickup_lat());
            picklng = Double.parseDouble(acceptResponseData.getTripDetailDataList().getPickup_lng());
            UserName.setText(acceptResponseData.getTripDetailDataList().getCustomer_name());
        }

        tvPickLocation.setText(utility.getCompleteAddressString(this, picklat, picklng));
        pickupTitleTextView.setText(getResources().getString(R.string.online_pick_up_location_title));
        toolbarClose.setBackgroundResource(R.drawable.ic_close);
        tvTitle.setText("Start Ride");
        endTripConstrainRelativeLayout.setOnTouchListener(new OnSwipeTouchListener(StartActivity.this) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                if (wayPointList.size() > 0) {
                    for (int i = 0; i < wayPointList.size(); i++) {
                        if (wayPointList.get(i).get("fare") != null) {
                            totalFare = String.valueOf(Double.parseDouble(totalFare) + Double.parseDouble(wayPointList.get(i).get("fare")));
                        }
                        if (wayPointList.get(i).get("distance") != null) {
                            totalDist = totalDist + Double.parseDouble(wayPointList.get(i).get("distance"));
                        }
                    }
                }

                sendEndRequest();

            }

            public void onSwipeLeft() {
            }

            public void onSwipeBottom() {
            }

        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                currentLat = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                currentLng = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);
                if (currentLng != null && currentLat != null) {
                    updateLocationUI();
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST));


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);


        startStep1();

//        acceptMapView.getMapAsync(this);

    }

    private void updateLocationUI() {

        LatLng des;
        map.clear();
        if (currentLat != null && currentLng != null) {


            int image = R.drawable.image2;
            LatLng loc = new LatLng(Double.parseDouble(currentLat), Double.parseDouble(currentLng));
            if (isRideStart) {
                des = new LatLng(deslat, deslng);
            } else {
                des = new LatLng(picklat, picklng);
            }


            MarkerOptions options = new MarkerOptions().position(loc).
                    icon(BitmapDescriptorFactory.fromResource(image));
            m = map.addMarker(options);

            map.setMyLocationEnabled(false);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.getUiSettings().setCompassEnabled(false);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));

            MarkerOptions a2 = new MarkerOptions()
                    .position(des);
            map.addMarker(a2);
            a2.position(des);

            if (!isRideStart) {
                String url = getDirectionsUrl(loc, new LatLng(picklat, picklng));
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
            } else {

                if (isFirst) {
                    passedDistance = 0.0;
                    if (totalDistance == 0.0) {
                        totalDistance = CalculationByDistance(loc, des);
                    }
                } else {
                    double passedDistance1 = CalculationByDistance(loc, des);
                    passedDistance = Math.abs(totalDistance - passedDistance1);
                }
                if (utility.isOnline(this)) {
                    updateVehicleLiveLocationRequest(ApiUtility.getInstance().getApiKeyMetaData(), appSharedPreference.getUserType(), currentLat, currentLng, appSharedPreference.getSpeed());
                }
            }


            startlist.add(loc);

            animateMarker(m, startlist);

        }
    }


    public void updateVehicleLiveLocationRequest(String apikey, String userType, String latitude,
                                                 String longitude, float speed) {
        compositeDisposable.add(requestHandler.updateVehicleLiveLocationRequest(apikey, userType, latitude,
                longitude, speed)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> apiResponse(ApiResponse.loading(ServiceType.UPDATE_VEHICLE_LIVE_LOCATION)))
                .subscribe(result -> apiResponse(ApiResponse.success(ServiceType.UPDATE_VEHICLE_LIVE_LOCATION, result)),
                        error -> apiResponse(ApiResponse.error(ServiceType.UPDATE_VEHICLE_LIVE_LOCATION, error))));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        stopServices();

        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    private void makeCallToStore() {
        TelephonyManager tm = (TelephonyManager) StartActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
        //Log.d(TAG,"storeMobileNumber" + storeMobileNumber);
        if (!TextUtils.isEmpty(acceptResponseData.getTripDetailDataList().getCustomer_contact()) && acceptResponseData.getTripDetailDataList().getCustomer_contact().length() > 0) {
            /*if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE && tm != null && tm.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {*/
            String uri = "tel:" + acceptResponseData.getTripDetailDataList().getCustomer_contact();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
            /*} else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error_text), Toast.LENGTH_SHORT).show();
            }*/

        } else {
            Toast.makeText(getApplicationContext(), "Mobile number not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Onclick(R.id.imgCall)
    public void imgCall(View v) {
        makeCallToStore();
    }

    @Onclick(R.id.toolbarClose)
    public void toolbarClose(View v) {
        finish();
    }

    @Onclick(R.id.btnReject)
    public void btnReject(View v) {
        cancelRequest();
    }

    @Onclick(R.id.btnAccept)
    public void btnAccept(View v) {
        if (btnAccept.getText().toString().equalsIgnoreCase(getResources().getString(R.string.arrived_button))) {
            arrivedRequest();
        } else {
            sendDataStartRequest();
        }
    }


    @SuppressLint({"ResourceAsColor", "InflateParams"})
    public void showBottomSheetDialog() {
        v = getLayoutInflater().inflate(R.layout.bottomsheet_confirmation_code, null);

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(v);
        Object clipboardService = this.getSystemService(CLIPBOARD_SERVICE);


        imgClose = v.findViewById(R.id.img_close);
        Button btnContinue = v.findViewById(R.id.btnContinue);
        PinView pinView = v.findViewById(R.id.pinView);

        imgClose.setImageResource(R.drawable.ic_close);


        int imgIconWidth = (int) (screenWidth * 0.075);
        int imgEditheight = (int) (screenWidth * 0.105);
        int imgIconHeight = (int) (screenWidth * 0.075);

        RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) imgClose.getLayoutParams();
        imgLayParams.width = imgIconWidth;
        imgLayParams.height = imgIconHeight;
        imgClose.setLayoutParams(imgLayParams);

        imgClose.setBackgroundResource(R.drawable.ic_close);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appSharedPreference.saveStringToPrefs(getApplicationContext(), "OTP", pinView.getText().toString().trim());
                bottomSheetDialog.dismiss();
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAccept.setText("Arrived");
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }

    public void cancelRequest() {
        compositeDisposable.add(requestHandler.cancelRequest(ApiUtility.getInstance().getApiKeyMetaData(), acceptResponseData.getTripDetailDataList().getBooking_id(), Utils.getStringToPrefs(this, "vehicleId"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> apiResponse(ApiResponse.loading(ServiceType.CANCEL_TRIP)))
                .subscribe(result -> apiResponse(ApiResponse.success(ServiceType.CANCEL_TRIP, result)),
                        error -> apiResponse(ApiResponse.error(ServiceType.CANCEL_TRIP, error))));
    }

    public void sendDataStartRequest() {
        compositeDisposable.add(requestHandler.startRequest(ApiUtility.getInstance().getApiKeyMetaData(), acceptResponseData.getTripDetailDataList().getBooking_id(), Utils.getStringToPrefs(this, "vehicleId"), appSharedPreference.getStringToPrefs(getApplicationContext(), "OTP"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> apiResponse(ApiResponse.loading(ServiceType.START_TRIP)))
                .subscribe(result -> apiResponse(ApiResponse.success(ServiceType.START_TRIP, result)),
                        error -> apiResponse(ApiResponse.error(ServiceType.START_TRIP, error))));
    }

    public void sendEndRequest() {
        compositeDisposable.add(requestHandler.completeRequest(ApiUtility.getInstance().getApiKeyMetaData(), acceptResponseData.getTripDetailDataList().getBooking_id(), Utils.getStringToPrefs(this, "vehicleId"), String.valueOf(totalDist), totalFare, currentLat, currentLng, wayPointList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> apiResponse(ApiResponse.loading(ServiceType.COMPLETE_TRIP)))
                .subscribe(result -> apiResponse(ApiResponse.success(ServiceType.COMPLETE_TRIP, result)),
                        error -> apiResponse(ApiResponse.error(ServiceType.COMPLETE_TRIP, error))));
    }

    private void arrivedRequest() {
        compositeDisposable.add(requestHandler.arrivedRequest(ApiUtility.getInstance().getApiKeyMetaData(), Utils.getStringToPrefs(this,"bookingId"),Utils.getStringToPrefs(this,"vehicleId"),appSharedPreference.getLastLocationLatitude(),appSharedPreference.getLastLocationLng())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> apiResponse(ApiResponse.loading(ServiceType.ARRIVED_TRIP)))
                .subscribe(result -> apiResponse(ApiResponse.success(ServiceType.ARRIVED_TRIP, result)),
                        error -> apiResponse(ApiResponse.error(ServiceType.ARRIVED_TRIP, error))));
    }

    private void apiResponse(ApiResponse apiResponse) {
        ServiceType serviceType = apiResponse.getServiceType();
        switch (apiResponse.status) {
            case LOADING:
                break;
            case SUCCESS:
                if (null != apiResponse.getResponseJsonObject() && apiResponse.getResponseStatus() != 0) {
                    if (ServiceType.START_TRIP == serviceType) {
                        if (apiResponse.isValidResponse()) {
                            AcceptResponse acceptResponse = new Gson().fromJson(apiResponse.data, AcceptResponse.class);

                            AcceptResponseData acceptResponseData = new AcceptResponseData();
                            acceptResponseData.setStatus(acceptResponse.getStatus());
                            acceptResponseData.setMessage(acceptResponse.getMessage());
                            if (acceptResponse.getTripDetailDataList() != null) {
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
                            isRideStart = true;
                            isFirst = true;
                            endTripConstrainRelativeLayout.setVisibility(View.VISIBLE);
                            startTripConstrainLinearLayout.setVisibility(View.GONE);
                            deslat = Double.parseDouble(Utils.getStringToPrefs(this, "dropLat"));
                            deslng = Double.parseDouble(Utils.getStringToPrefs(this, "dropLng"));
                            tvPickLocation.setText(utility.getCompleteAddressString(this, deslat, deslng));
                            pickupTitleTextView.setText(getResources().getString(R.string.online_des_up_location_title));
                            map.clear();
                        }


                    } else if (ServiceType.ARRIVED_TRIP == serviceType) {
                        btnAccept.setText("Start");
                        showBottomSheetDialog();
                    }else if (ServiceType.CANCEL_TRIP == serviceType) {
                        utility.showToastMsg(apiResponse.getResponseMessage());
                        finish();
                    } else if (ServiceType.UPDATE_VEHICLE_LIVE_LOCATION == serviceType) {

                        if (apiResponse.isValidResponse()) {
                            LatLng loc = new LatLng(Double.parseDouble(currentLat), Double.parseDouble(currentLng));

                            UpdateVehicleResponse updateVehicleResponse = new Gson().fromJson(apiResponse.data, UpdateVehicleResponse.class);
                            if (updateVehicleResponse != null) {
                                updateFare = updateVehicleResponse.getFare_per_km();
                                String url = getDirectionsUrl(loc, new LatLng(deslat, deslng));
                                DownloadTask downloadTask = new DownloadTask();

                                // Start downloading json data from Google Directions API
                                downloadTask.execute(url);
                            }
                        }
                    } else if (ServiceType.COMPLETE_TRIP == serviceType) {
                        if (apiResponse.isValidResponse()) {
                            stopService(intentService);
                            stopServices();

                            CompleteTripResponse completeTripResponse = new Gson().fromJson(apiResponse.data, CompleteTripResponse.class);

                            TripSummaryPassData tripSummaryPassData = null;
                            if (completeTripResponse.getCompleteTripData()!=null){
                                CompleteTripData completeTripData = completeTripResponse.getCompleteTripData();
                                 tripSummaryPassData = new TripSummaryPassData();
                                tripSummaryPassData.setActual_fare(completeTripData.getActual_fare());
                                tripSummaryPassData.setBooking_id(completeTripData.getBooking_id());
                                tripSummaryPassData.setCompleted_on(completeTripData.getCompleted_on());
                                tripSummaryPassData.setEnd_lat(completeTripData.getEnd_lat());
                                tripSummaryPassData.setEnd_lng(completeTripData.getEnd_lng());
                                tripSummaryPassData.setPickup_lat(completeTripData.getPickup_lat());
                                tripSummaryPassData.setPickup_lng(completeTripData.getPickup_lng());
                                tripSummaryPassData.setPickuped_on(completeTripData.getPickuped_on());
                                tripSummaryPassData.setTotal_travelled_km(completeTripData.getTotal_travelled_km());
                                tripSummaryPassData.setWaiting_fare(completeTripData.getWaiting_fare());
                                tripSummaryPassData.setBooking_no(completeTripData.getBooking_no());
                                tripSummaryPassData.setPayment_mode(completeTripData.getPayment_mode());
                                tripSummaryPassData.setTime_taken(completeTripData.getTime_taken());
                            }
                            Intent intent = new Intent(StartActivity.this, PaymentSummaryNewActivity.class);
                            intent.putExtra("tripSummary", (TripSummaryPassData) tripSummaryPassData);
                            startActivity(intent);

                        }
                    }
                } else {
                    utility.showToastMsg(apiResponse.getResponseMessage());
                }
                break;
            case ERROR:
                break;
        }
    }

    @UiThread
    private void setDynamicValue() {
        int topBottomSpace = (int) (screenHeight * 0.0089);
        int imgIconWidth = (int) (screenWidth * 0.075);
        int imgEditheight = (int) (screenWidth * 0.105);
        int imgIconHeight = (int) (screenWidth * 0.075);

        RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) toolbarClose.getLayoutParams();
        imgLayParams.width = imgIconWidth;
        imgLayParams.height = imgIconHeight;
        toolbarClose.setLayoutParams(imgLayParams);
    }


    public void startServices() {
        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                intentService = new Intent(StartActivity.this, LocationMonitoringService.class);
                startService(intentService);
            }
        }, 0, 10, TimeUnit.SECONDS);

    }

    public void stopServices() {
        isRideStart = false;
        scheduleTaskExecutor.shutdown();
        finish();
    }

    /**
     * Step 1: Check Google Play services
     */
    private void startStep1() {

        //Check whether this user has installed Google play service which is being used by Location updates.
        if (isGooglePlayServicesAvailable()) {
            startStep2(null);
        }
    }


    /**
     * Step 2: Check & Prompt Internet connection
     */
    private Boolean startStep2(DialogInterface dialog) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            promptInternetConnect();
            return false;
        }


        if (dialog != null) {
            dialog.dismiss();
        }

        //Yes there is active internet connection. Next check Location is granted by user or not.

        if (checkPermissions()) { //Yes permissions are granted by the user. Go to the next step.
            startStep3();
        } else {  //No user has not granted the permissions yet. Request now.
            requestPermissions();
        }
        return true;
    }

    /**
     * Show A Dialog with button to refresh the internet state.
     */
    private void promptInternetConnect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
        builder.setTitle("No Internet");
        builder.setMessage("No Internet");

        String positiveText = "Refresh";
        builder.setPositiveButton(positiveText,
                (dialog, which) -> {


                    //Block the Application Execution until user grants the permissions
                    if (startStep2(dialog)) {

                        //Now make sure about location permission.
                        if (checkPermissions()) {

                            //Step 2: Start the Location Monitor Service
                            //Everything is there to start the service.
                            startStep3();
                        } else if (!checkPermissions()) {
                            requestPermissions();
                        }

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Step 3: Start the Location Monitor Service
     */
    private void startStep3() {

        startServices();
    }

    /**
     * Return the availability of GooglePlayServices
     */
    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }


    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionState2 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Start permissions requests.
     */
    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);
        // Provide an additional rationale to the img_user. This would happen if the img_user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (!shouldProvideRationale || !shouldProvideRationale2) {
            ActivityCompat.requestPermissions(StartActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);

        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startStep3();

            }
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            parserTask.execute(result);

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "0.0", duration = "0";
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.BLACK);
                lineOptions.geodesic(true);
                if (lineOptions != null) {
                    map.addPolyline(lineOptions);
                }

            }

// Drawing polyline in the Google Map for the i-th route


            if (isRideStart) {
                HashMap<String, String> items = new HashMap<String, String>();
                items.put("lat", currentLat);
                items.put("lng", currentLng);
                items.put("distance", String.valueOf(passedDistance));
                items.put("fare", updateFare);
                wayPointList.add(items);
            }
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String key = "key=" + getResources().getString(R.string.api_key);


        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private void animateMarker(final Marker m, final List<LatLng> startlist) {

        TypeEvaluator<LatLng> evaluator = new TypeEvaluator<LatLng>() {
            @Override
            public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
                double angle = getAngle(startValue, endValue);
                m.setRotation((float) angle);
                return spherical.interpolate(fraction, startValue, endValue);
            }
        };

        Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "Position");
        ObjectAnimator animator = ObjectAnimator.ofObject(m, property, evaluator, startlist.get(0));
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (startlist.size() > 1) {
//                    if (startlist.get(0).equals(startlist.get(1))) {
//                        return;
//                    } else {
                    startlist.remove(0);
                    animateMarker(m, startlist);
//                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(2000);
        animator.start();
    }

    private double getAngle(LatLng curretLatLng, LatLng toLatLng) {
        double fromLong = curretLatLng.longitude * kDegreesToRadians;
        double toLong = toLatLng.longitude * kDegreesToRadians;

        double fromLat = curretLatLng.latitude * kDegreesToRadians;
        double toLat = toLatLng.latitude * kDegreesToRadians;

        double dlon = toLong - fromLong;
        double y = Math.sin(dlon) * Math.cos(toLat);
        double x = Math.cos(fromLat) * Math.sin(toLat) - Math.sin(fromLat) * Math.cos(toLat) * Math.cos(dlon);

        double direction = Math.atan2(y, x);

        // convert to degrees
        direction = direction * kRadiansToDegrees;
        // normalize
        double fr = direction + 360.0;
        long fr_long = (long) fr;
        double fr_final = fr - fr_long;
        direction += fr_final;

        return direction;
    }


    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }



}
