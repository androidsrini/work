package com.codesense.driverapp.ui.helper;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

public class CrashlyticsHelper {

    private static final String TAG = "DriverApp";
    private static final String START_CLASS = "START U0";
    private static final String VIEW_CLICK = "[view click]";

    /**
     * This method will print info logs.
     * @param msg
     */
    public static void i(String msg) {
        Crashlytics.log(Log.INFO, TAG, msg);
    }

    /**
     * This method to print debug log
     * @param msg
     */
    public static void d(String msg) {
        Crashlytics.log(Log.DEBUG, TAG, msg);
    }

    /**
     * This method to print error log.
     * @param msg
     */
    public static void e(String msg) {
        Crashlytics.log(Log.ERROR, TAG, msg);
    }

    /**
     * This method to print waring log
     * @param msg
     */
    public static void w(String msg) {
        Crashlytics.log(Log.WARN, TAG, msg);
    }

    /**
     * This method to print exception log
     * @param e
     */
    public static void exception(Exception e) {
        Crashlytics.logException(e);
    }

    /**
     * This method will print start activity logs.
     * @param className
     */
    public static void startLog(String className) {
        i(START_CLASS + className);
    }

    /**
     * This method to print click logs.
     * @param clickViewName
     */
    public static void clickLog(String clickViewName) {
        i(VIEW_CLICK + clickViewName);
    }
}
