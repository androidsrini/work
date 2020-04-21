package com.codesense.driverapp.ui.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.codesense.driverapp.data.CitiesItem;
import com.codesense.driverapp.data.CountriesItem;
import com.codesense.driverapp.localstoreage.DatabaseClient;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import com.codesense.driverapp.R;

public class CountryCitiesSelectionActivity extends AppCompatActivity {

    private static final String TAG = "DriverApp";
    /**
     * This field to get intent value
     */
    public static final String ACTIVITY_TYPE = "ActivityTypeArg";
    /**
     * This field key for result data.
     */
    public static final String RESULT_DATA = "ResultData";
    /**
     * This is used for to pass the data based on this code.
     * Activity result(data with result code).
     */
    public static final int REQUEST_CODE = 0x0001;
    @Initialize(R.id.dataListView)
    ListView dataListView;
    @Initialize(R.id.toolbar)
    Toolbar toolbar;
    @Initialize(R.id.toolbarClose)
    ImageView toolbarClose;
    /**
     * This object to use rxjava for api server call.
     */
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    /**
     * To keep activity type in this object
     */
    private ActivityType activityType;
    private ArrayAdapter arrayAdapter;


    private ActivityType getActivityType(Intent intent) {
        if (null != intent && null == activityType) {
            activityType = (ActivityType) intent.getSerializableExtra(ACTIVITY_TYPE);
        }
        /**
         * By default this screen will display country list
         */
        if (null == activityType) {
            activityType = ActivityType.COUNTRY;
        }
        return activityType;
    }

    private void fetchCountryListFromDataBase() {
        compositeDisposable.add(DatabaseClient.getInstance(this).getAppDatabase().countryDao().getCountryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.d(TAG, " Country List data size is: " + result.size());
                    arrayAdapter = new ArrayAdapter<CountriesItem>(this, android.R.layout.simple_list_item_1, result);
                    dataListView.setAdapter(arrayAdapter);
                }, error -> {
                    Log.d(TAG, " Country List data getting error: " + Log.getStackTraceString(error));
                }));
    }

    private void fetchCitiesListFromDataBase() {
        compositeDisposable.add(DatabaseClient.getInstance(this).getAppDatabase().cityDao().getCityList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.d(TAG, " cities List data size is: " + result.size());
                    arrayAdapter = new ArrayAdapter<CitiesItem>(this, android.R.layout.simple_list_item_1, result);
                    dataListView.setAdapter(arrayAdapter);
                }, error -> {
                    Log.d(TAG, " cities List data getting error: " + Log.getStackTraceString(error));
                }));
    }

    /**
     * This method will update the actionbar ui
     * @param activityType
     */
    private void updateActionBarTextAndUI(ActivityType activityType) {
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (ActivityType.CITIES == activityType) {
                getSupportActionBar().setTitle(getString(R.string.city_text));
            } else {
                getSupportActionBar().setTitle(getString(R.string.country_text));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_cities_selection);
        ProductBindView.bind(this);
        setSupportActionBar(toolbar);
        updateActionBarTextAndUI(getActivityType(getIntent()));
        if (ActivityType.CITIES == getActivityType(getIntent())) {
            //Fetch Cities list from data base and update UI.
            fetchCitiesListFromDataBase();
        } else {
            fetchCountryListFromDataBase();
        }
        toolbarClose.setVisibility(View.GONE);
        dataListView.setOnItemClickListener((parent, view, position, id) -> {
            if (ActivityType.CITIES == getActivityType(getIntent())) {
                CitiesItem citiesItem = (CitiesItem) arrayAdapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(RESULT_DATA, citiesItem);
                intent.putExtra(ACTIVITY_TYPE, activityType);
                setResult(RESULT_OK, intent);
                finish();
            } else if (ActivityType.COUNTRY == getActivityType(getIntent())) {
                CountriesItem countriesItem = (CountriesItem) arrayAdapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(RESULT_DATA, countriesItem);
                intent.putExtra(ACTIVITY_TYPE, activityType);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public enum ActivityType {
        COUNTRY, CITIES
    }
}
