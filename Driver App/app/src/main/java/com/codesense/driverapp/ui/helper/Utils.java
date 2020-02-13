package com.codesense.driverapp.ui.helper;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;

import com.codesense.driverapp.base.BaseApplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    /**
     * This method is used to convert uri to byte array
     * it will use application context
     * @param uri
     * @return
     */
    public static byte[] convertUri(Uri uri) {
        if (null == uri)
            return null;
        InputStream inputStream = null;
        try {
            inputStream = BaseApplication.getBaseApplication().getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int read = 0;
            while ((read = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, read);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
