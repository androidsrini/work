package com.codesense.driverapp.ui.helper;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

public class CrashlyticsHelper {

    private static final String TAG = "DriverApp";
    private static final String START_CLASS = "START U0";
    private static final String VIEW_CLICK = "[view click]";
    private static StringBuffer stringBuffer;

    private static void createStringBufferObject() {
        if (null == stringBuffer) {
            stringBuffer = new StringBuffer();
        }
    }

    private static void clearAllFromStringBuffer() {
        if (null != stringBuffer) {
            if (0 < stringBuffer.length()) {
                stringBuffer.delete(0, stringBuffer.length());
            }
        } else {
            createStringBufferObject();
        }
    }

    private static String appendString(Object... objects) {
        for (Object o: objects) {
            stringBuffer.append(o);
        }
        return stringBuffer.toString();
    }

    /**
     * This method will print info logs.
     * @param msg
     */
    public static void i(Object... msg) {
        clearAllFromStringBuffer();
        Crashlytics.log(Log.INFO, TAG, appendString(msg));
        Log.i(TAG, appendString(msg));
    }

    /**
     * This method to print debug log
     * @param msg
     */
    public static void d(Object... msg) {
        clearAllFromStringBuffer();
        Crashlytics.log(Log.DEBUG, TAG, appendString(msg));
        Log.d(TAG, appendString(msg));
    }

    /**
     * This method to print error log.
     * @param msg
     */
    public static void e(String msg) {
        clearAllFromStringBuffer();
        Crashlytics.log(Log.ERROR, TAG, appendString(msg));
        Log.e(TAG, appendString(msg));
    }

    /**
     * This method to print waring log
     * @param msg
     */
    public static void w(String msg) {
        clearAllFromStringBuffer();
        Crashlytics.log(Log.WARN, TAG, appendString(msg));
        Log.w(TAG, appendString(msg));
    }

    /**
     * This method to print exception log
     * @param e
     */
    public static void exception(Exception e) {
        Crashlytics.logException(e);
        clearAllFromStringBuffer();
        Log.i(TAG, appendString(Log.getStackTraceString(e)));
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
