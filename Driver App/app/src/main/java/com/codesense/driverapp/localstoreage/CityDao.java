package com.codesense.driverapp.localstoreage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.codesense.driverapp.data.CitiesItem;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface CityDao {

    /**
     * This method will be used for to retrieve all city from app data base.
     * @return List<City>
     */
    @Query("SELECT * FROM CitiesItem")
    Single<List<CitiesItem>> getCityList();

    @Query("SELECT * FROM CitiesItem")
    List<CitiesItem> getCityListByNormalThread();

    /**
     * This method will return all city based on city ids param.
     * @param cityIds country ids.
     * @return List<City></City>
     */
    @Query("SELECT * FROM CitiesItem WHERE city_id IN (:cityIds)")
    Single<List<CitiesItem>> loadAllCityByIds(int[] cityIds);

    /**
     * This method will retrun single City based on City name param
     * @param cityName param
     * @return City
     */
    @Query("SELECT * FROM CitiesItem WHERE city_name =:cityName LIMIT 1")
    Single<CitiesItem> findByCityName(String cityName);

    /**
     * This method to insert City object to data base
     * @param cities param
     */
    @Insert
    void insertAllCity(List<CitiesItem> cities);

    /**
     * This method to delete specify city from data base.
     * @param city param
     */
    @Delete
    void delete(CitiesItem city);

    /**
     * This method to delete all cities from data base.
     */
    @Query("DELETE FROM citiesitem")
    void deleteAllRowFromDataBase();
}
