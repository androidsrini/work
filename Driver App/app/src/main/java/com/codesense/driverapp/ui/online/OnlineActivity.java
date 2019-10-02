package com.codesense.driverapp.ui.online;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.codesense.driverapp.R;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.codesense.driverapp.ui.helper.CrashlyticsHelper;
import com.codesense.driverapp.ui.helper.LocationMonitoringService;
import com.codesense.driverapp.ui.helper.OnSwipeTouchListener;
import com.codesense.driverapp.ui.helper.Utils;
import com.codesense.driverapp.ui.paymentType.PaymentActivity;
import com.codesense.driverapp.ui.pickuplocationaccept.PickUpLocationAcceptActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class OnlineActivity extends DrawerActivity implements OnMapReadyCallback {


    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private static int TIME_OUT = 30000; //Time to launch the another activity

    private boolean mAlreadyStartedService = false;
    BroadcastReceiver broadcastReceiver;
    private String currentLat, currentLng;
    RelativeLayout rlAccept, rlEndTrip;
    GoogleMap map;
    @Inject
    protected RequestHandler requestHandler;
    @Inject
    protected OnlineViewModel onlineViewModel;

    /**
     * This method to start this activity
     * @param context
     */
    public static void start(Context context) {
        context.startActivity(new Intent(context, OnlineActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View contentView = inflater.inflate(R.layout.activity_online, null, false);
        frameLayout.addView(contentView);
        isSignedIn = true;

        rlAccept = findViewById(R.id.rlAccept);
        rlEndTrip = findViewById(R.id.rlEndTrip);
        titleTextView.setText(getResources().getString(R.string.online_text));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
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

        LocalBroadcastManager.getInstance(this).registerReceiver(
                broadcastReceiver, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );

        new Handler().postDelayed(() -> {
            Intent i = new Intent(OnlineActivity.this, PickUpLocationAcceptActivity.class);
            startActivity(i);
        }, TIME_OUT);

        rlEndTrip.setOnTouchListener(new OnSwipeTouchListener(OnlineActivity.this) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
//                Toast.makeText(OnlineActivity.this, "right", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OnlineActivity.this, PaymentActivity.class);
                startActivity(intent);
            }

            public void onSwipeLeft() {
            }

            public void onSwipeBottom() {
            }

        });
        updateSwitchUI(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean onlineStatus = utility.isOnline(this);
        boolean isUserStatus = appSharedPreference.isUserStatusOnline();
        CrashlyticsHelper.d("App internet enabled: " + onlineStatus);
        CrashlyticsHelper.d("App user status enabled: " + isUserStatus);
        //String status = onlineStatus ? Constant.ONLINE_STATUS : Constant.OFFLINE_STATUS;
        if (utility.isOnline(this) && isUserStatus) {
            refreshLocation();
        }
    }

    public void refreshLocation() {
        //define constraints
        Constraints myConstraints = new Constraints.Builder()
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .build();
        PeriodicWorkRequest refreshCpnWork =
                new PeriodicWorkRequest.Builder(LocationWorker.class, 2, TimeUnit.SECONDS)
                        .setConstraints(myConstraints)
                        .build();
        WorkManager.getInstance().enqueue(refreshCpnWork);
    }

    private void checkAcceptable() {
        if (Utils.getIntegerToPrefs(this, "acceptDrive") == 1) {
            rlAccept.setVisibility(View.VISIBLE);
        } else {
            rlAccept.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        startStep1();
        checkAcceptable();
        loadMenu();
    }

    /**
     * Step 1: Check Google Play services
     */
    private void startStep1() {

        //Check whether this user has installed Google play service which is being used by Location updates.
        if (isGooglePlayServicesAvailable()) {
            //Passing null to indicate that it is executing for the first time.
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
        AlertDialog.Builder builder = new AlertDialog.Builder(OnlineActivity.this);
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

        //And it will be keep running until you close the entire application from task manager.
        //This method will executed only once.

        if (!mAlreadyStartedService) {


            if (isLocationEnabled(this)) {

                //Start location sharing service to app server.........
                Intent intent = new Intent(this, LocationMonitoringService.class);
                startService(intent);
            } else {
                createLocationServiceError(OnlineActivity.this);
            }

            mAlreadyStartedService = true;
            //Ends................................................
        } else {
            Intent intent = new Intent(this, LocationMonitoringService.class);
            stopService(intent);
            if (isLocationEnabled(this)) {
                //Start location sharing service to app server.........
                Intent intent1 = new Intent(this, LocationMonitoringService.class);
                startService(intent1);
            } else {
                createLocationServiceError(OnlineActivity.this);
            }
        }
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
                android.Manifest.permission.ACCESS_FINE_LOCATION);
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
                        android.Manifest.permission.ACCESS_FINE_LOCATION);
        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);
        // Provide an additional rationale to the img_user. This would happen if the img_user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (!shouldProvideRationale || !shouldProvideRationale2) {
            ActivityCompat.requestPermissions(OnlineActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
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

    public void createLocationServiceError(final Activity activityObj) {
        AlertDialog alert;
        // show alert dialog if Internet is not connected
        AlertDialog.Builder builder = new AlertDialog.Builder(activityObj);
        builder.setMessage(
                "You need to activate location service to use this feature. Please turn on network or GPS mode in location settings")
                .setTitle("Need to enable location")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        (dialog, id) -> {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            activityObj.startActivity(intent);
                            dialog.dismiss();
                        })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.dismiss());
        alert = builder.create();
        alert.show();
    }


    @Override
    public void onDestroy() {
        updateSwitchUI(false);
        //Stop location sharing service to app server.........
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        stopService(new Intent(OnlineActivity.this, LocationMonitoringService.class));
        mAlreadyStartedService = false;
        //Ends................................................
        super.onDestroy();
    }

    @SuppressLint("ObsoleteSdkInt")
    public static boolean isLocationEnabled(Context context) {
        int locationMode;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }


    private void updateLocationUI() {
        map.clear();
        LatLng loc = new LatLng(Double.parseDouble(currentLat), Double.parseDouble(currentLng));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        MarkerOptions markerOptions = new MarkerOptions();
        // Setting the position for the marker
        markerOptions.position(loc);
        map.setMyLocationEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
        map.addMarker(markerOptions);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
    }
}
