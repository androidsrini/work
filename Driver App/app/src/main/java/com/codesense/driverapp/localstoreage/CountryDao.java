package com.codesense.driverapp.localstoreage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.codesense.driverapp.data.CountriesItem;

import java.util.List;

import io.reactivex.Single;

/**
 * App country object Dao operations
 */
@Dao
public interface CountryDao {
    /**
     * This method will be used for to retrieve all country from app data base.
     * @return List<Country></Country>
     */
    @Query("SELECT * FROM CountriesItem")
    List<CountriesItem> getCountryListByNormalThread();

    @Query("SELECT * FROM CountriesItem")
    Single<List<CountriesItem>> getCountryList();

    /**
     * This method will return all country based on country ids param.
     * @param countryIds country ids.
     * @return List<Country></Country>
     */
    @Query("SELECT * FROM CountriesItem WHERE country_id IN (:countryIds)")
    Single<List<CountriesItem>> loadAllCountryByIds(int[] countryIds);

    /**
     * This method will retrun single country based on country name param
     * @param countryName param
     * @return Countury
     */
    @Query("SELECT * FROM CountriesItem WHERE country_name =:countryName LIMIT 1")
    Single<CountriesItem> findByCountryName(String countryName);

    /**
     * This method to insert country object to data base
     * @param countries param
     */
    @Insert
    void insertAllCountry(List<CountriesItem> countries);

    /**
     * This method to delete specify country from data base.
     * @param country param
     */
    @Delete
    void delete(CountriesItem country);

    /**
     * This method to delete all data from data base.
     */
    @Query("DELETE FROM countriesitem")
    void deleteAllRowFromDataBase();

}
