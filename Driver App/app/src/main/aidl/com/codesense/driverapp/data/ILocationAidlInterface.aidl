// ILocationAidlInterface.aidl
package com.codesense.driverapp.data;
import com.codesense.driverapp.data.CountriesItem;
import com.codesense.driverapp.data.CitiesItem;

// Declare any non-default types here with import statements

interface ILocationAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<CountriesItem> getCountryList();
    List<CitiesItem> getCitiesList();
    int addNumbers(int num1, int num2);
}
