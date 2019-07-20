package com.codesense.driverapp.ui.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class Utils {


    public static final String UPLOAD_DOCUMENT = "upload_document_item";
    public static final String UPLOAD_DOCUMENT2 = "upload_document2_item";
    public static final String DOCUMENT_STATUS = "document_status_item";


    public static List<TypedArray> getMultiTypedArray(Context context, String key) {
        List<TypedArray> array = new ArrayList<>();
        try {
            //Class<R.array> res = R.array.class;
            //Field field;
            int counter = 0;
            int fieldID = -1;
            do {
                //field = res.getField(key + "_" + counter);
                //details = res.getIdentifier(strItem, "array", getPackageName());
                //array.add(context.getResources().obtainTypedArray(field.getInt(null)));
                fieldID = context.getResources().getIdentifier(key + "_" + counter, "array", context.getPackageName());
                array.add(context.getResources().obtainTypedArray(fieldID));
                counter++;
            } while (fieldID != -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    public static void saveIntegerToPrefs(Context context, String key, int value) {

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void saveLongToPrefs(Context context, String key, long value) {

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getLongToPrefs(Context context, String key) {

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return prefs.getLong(key, 0);
    }

    public static int getIntegerToPrefs(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPrefs.getInt(key, 0);

    }

    public static void saveStringToPrefs(Context context, String key,
                                         String value) {

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);

        editor.commit();
    }

    public static String getStringToPrefs(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPrefs.getString(key, null);

    }
}
