package com.codesense.driverapp.ui.helper;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseApplication;
import com.codesense.driverapp.data.DropLocationData;
import com.codesense.driverapp.data.UpdateVehicleResponse;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;
import com.codesense.driverapp.ui.online.OnlineActivity;
import com.codesense.driverapp.ui.pickuplocationaccept.PickUpLocationAcceptActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * This class to access location lat and long values in background
 *
 * @link https://stackoverflow.com/questions/36321661/how-can-i-use-fused-location-provider-inside-a-service-class
 */
public class LocationMonitoringService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private static final String TAG = LocationMonitoringService.class.getSimpleName();
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    public static final String ACTION_LOCATION_BROADCAST = LocationMonitoringService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    private static final long INTERVAL = 1000 * 30 * 60;
    private static final long FASTEST_INTERVAL = 1000 * 25 * 60;
    private static final long MEDIUM_INTERVAL = 1000 * 30 * 60;
    AppSharedPreference appSharedPreference;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Inject
    RequestHandler requestHandler;
    private MutableLiveData<ApiResponse> apiResponseMutableLiveData = new MutableLiveData<>();
    @Inject
    Utility utility;

    /**
     * This method to find location permission enabled or not
     *
     * @return boolean
     */
    private boolean hasLocationPermissionEnabled() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * This method to create location request object
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setSmallestDisplacement(1);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * This method to start location
     * it will call while create new object for this service class
     */
    private void startLocationUpdates() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                        mLocationRequest, this);

            }
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
        /*if (mGoogleApiClient != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            Log.d(TAG, "Location update started ..............: ");
        }*/
    }

    /**
     * This method to stop location
     * it will call while stop this service
     */
    private void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && LocationServices.FusedLocationApi != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
            Log.d(TAG, "Location update stopped .......................");
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * This method to find speed per seconds and update the values in preferences.
     * convert Meters/Second to  kmph-1   then you need to multipl the  Meters/Second answer from 3.6
     *
     * @param speed
     * @link http://mycodingworld1.blogspot.com/2015/12/calculate-speed-from-gps-location.html
     */
    private void updateSpeed(float speed) {
        float nSpeed = speed * 3.6f;
        //Convert meters/second to miles/hour
        nSpeed = nSpeed * 2.2369362920544f / 3.6f;
        appSharedPreference.setSpeed(nSpeed);
//        showToast("speed: " + nSpeed);
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void updateVehicleLiveLocationRequest(String apikey, String userType, String latitude,
                                                 String longitude, float speed) {
        compositeDisposable.add(requestHandler.updateVehicleLiveLocationRequest(apikey, userType, latitude,
                longitude, speed)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> apiResponseHandler(ApiResponse.loading(ServiceType.UPDATE_VEHICLE_LIVE_LOCATION)))
                .subscribe(result -> apiResponseHandler(ApiResponse.success(ServiceType.UPDATE_VEHICLE_LIVE_LOCATION, result)),
                        error -> apiResponseHandler(ApiResponse.error(ServiceType.UPDATE_VEHICLE_LIVE_LOCATION, error))));
    }

    /**
     * This callback method will call when create object.
     *
     * @link https://stackoverflow.com/questions/29343922/googleapiclient-is-throwing-googleapiclient-is-not-connected-yet-after-onconne
     */
    @Override
    public void onCreate() {
        super.onCreate();
        if (null == appSharedPreference) {
            appSharedPreference = new AppSharedPreference(getApplicationContext());
        }
        ((BaseApplication) getApplication()).getAppComponent(getApplicationContext()).inject(this);
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //
        if (Build.VERSION_CODES.M >= Build.VERSION.SDK_INT) {
            if (hasLocationPermissionEnabled()) {
                if (!mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                } else {
                    startLocationUpdates();
                }
            }
        } else {
            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            } else {
                startLocationUpdates();
            }
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * LOCATION CALLBACKS
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        CrashlyticsHelper.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }

    //to get the location change
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location changed");
        if (location != null) {
            //showToast("Location changed");
            Log.d(TAG, "== location != null");
            updateSpeed(location.getSpeed());
            appSharedPreference.setLastLocationLatitude(String.valueOf(location.getLatitude()));
            appSharedPreference.setLastLocationLong(String.valueOf(location.getLongitude()));
            sendMessageToUI(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            if (appSharedPreference.getIsActivate() == 1) {
                updateVehicleLiveLocationRequest(ApiUtility.getInstance().getApiKeyMetaData(), appSharedPreference.getUserType(), String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), appSharedPreference.getSpeed());
            }
        }
    }

    private void sendMessageToUI(String lat, String lng) {
        Log.d(TAG, "Sending info...");
        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(EXTRA_LATITUDE, lat);
        intent.putExtra(EXTRA_LONGITUDE, lng);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (hasLocationPermissionEnabled()) {
            stopLocationUpdates();
        }
        CrashlyticsHelper.d(TAG, "Service Stopped!");
    }


    private void generateNotification(Context context, @androidx.annotation.NonNull String message, String title) {
        long when = System.currentTimeMillis();
        PendingIntent pendingIntentEmpty = PendingIntent.getActivity(context,
                0, new Intent(), 0);
        String CHANNEL_ID = "my_channel_02";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.mipmap.icon);
            builder.setColor(context.getResources().getColor(R.color.primary_color));
        } else {
            builder.setSmallIcon(R.mipmap.icon);
        }

        builder.setContentText(message)
                .setContentTitle(title != null ? title : context.getString(R.string.app_name))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setWhen(when)
                .setAutoCancel(true)
                .setContentIntent(pendingIntentEmpty);

        Intent notifyIntent = new Intent(context, OnlineActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);

        String sAppName = context.getResources().getString(R.string.app_name);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, sAppName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        if (notificationManager != null)
            notificationManager.notify(0, builder.build());

    }


    private void apiResponseHandler(ApiResponse apiResponse) {
        ServiceType serviceType = apiResponse.getServiceType();

        switch (apiResponse.status) {
            case LOADING:
                break;
            case SUCCESS:
                if (ServiceType.UPDATE_VEHICLE_LIVE_LOCATION == serviceType) {

                    if (apiResponse.isValidResponse()) {
                        UpdateVehicleResponse updateVehicleResponse = new Gson().fromJson(apiResponse.data, UpdateVehicleResponse.class);
                        if (Utils.getIntegerToPrefs(getApplicationContext(), "acceptDrive") == 0 && Utils.getStringToPrefs(getApplicationContext(), "showAccept").equalsIgnoreCase("false")) {
                            if (updateVehicleResponse.getAvaliableTripDataList().size() > 0) {
                                if (Utils.getStringToPrefs(getApplicationContext(),"cancelbookingId")!=null && Utils.getStringToPrefs(getApplicationContext(),"cancelbookingId").equalsIgnoreCase(updateVehicleResponse.getAvaliableTripDataList().get(0).getBooking_id()) ) {
                                    Log.e("SameBookingId",updateVehicleResponse.getAvaliableTripDataList().get(0).getBooking_id());
                                }else{
                                    Intent i = new Intent(getApplicationContext(), PickUpLocationAcceptActivity.class);

                                    i.putExtra("picklocation", utility.getCompleteAddressString(this, Double.parseDouble(updateVehicleResponse.getAvaliableTripDataList().get(0).getPickup_lat()), Double.parseDouble(updateVehicleResponse.getAvaliableTripDataList().get(0).getPickup_lng())));
                                    i.putExtra("customerNum", updateVehicleResponse.getAvaliableTripDataList().get(0).getCustomer_contact());
                                    i.putExtra("customerName", updateVehicleResponse.getAvaliableTripDataList().get(0).getCustomer_name());
                                    i.putExtra("bookingId", updateVehicleResponse.getAvaliableTripDataList().get(0).getBooking_id());
                                    i.putExtra("vehicleId", updateVehicleResponse.getAvaliableTripDataList().get(0).getVehicle_id());
                                    if (updateVehicleResponse.getAvaliableTripDataList().get(0).getDropLocationDataList().size() > 0) {
                                        List<DropLocationData> dropLocationDataList = updateVehicleResponse.getAvaliableTripDataList().get(0).getDropLocationDataList();
                                        Utils.saveStringToPrefs(this, "dropLat", dropLocationDataList.get(0).getDrop_lat());
                                        Utils.saveStringToPrefs(this, "dropLng", dropLocationDataList.get(0).getDrop_lng());
                                        i.putExtra("droplocation", utility.getCompleteAddressString(this, Double.parseDouble(dropLocationDataList.get(0).getDrop_lat()), Double.parseDouble(dropLocationDataList.get(0).getDrop_lng())));
                                    }
                                    startActivity(i);
                                }
                            }
                        }
                    }
                }
                break;
            case ERROR:
                break;
        }
    }
}