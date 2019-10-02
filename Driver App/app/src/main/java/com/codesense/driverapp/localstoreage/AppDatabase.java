package com.codesense.driverapp.localstoreage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.codesense.driverapp.data.CitiesItem;
import com.codesense.driverapp.data.CountriesItem;

@Database(entities = {CountriesItem.class, CitiesItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CountryDao countryDao();
    public abstract CityDao cityDao();
}
