package com.codesense.driverapp.binder;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.view.View;

import java.util.HashMap;

public class DriverAppUI {

    private static final String TAG = "Builder";
    private final HashMap<String, View> VIEW_GROUP_HASHMAP = new HashMap<>();
    private static DriverAppUI driverAppUI;
    private FieldInitialize fieldInitialize;
    private ClickListenerMethod clickListenerMethod;

    private DriverAppUI(Activity activity) {
        fieldInitialize = new FieldInitialize.FieldInitializeBuilder(activity, VIEW_GROUP_HASHMAP).build();
        clickListenerMethod = new ClickListenerMethod.ClickListenerMethodBuilder(activity, VIEW_GROUP_HASHMAP).build();
        initAnnotationValue();
    }

    private DriverAppUI(Object o, View view) {
        fieldInitialize = new FieldInitialize.FieldInitializeBuilder(o, view, VIEW_GROUP_HASHMAP).build();
        clickListenerMethod = new ClickListenerMethod.ClickListenerMethodBuilder(o, view, VIEW_GROUP_HASHMAP).build();
        initAnnotationValue();
        //noinspection unchecked
    }

    @UiThread
    public static DriverAppUI activityBinder(@NonNull Activity activity) {
        driverAppUI = new DriverAppUI(activity);
        return driverAppUI;
    }

    @UiThread
    public static DriverAppUI binder(@NonNull Object target, @NonNull View view) {
        driverAppUI = new DriverAppUI(target, view);
        return driverAppUI;
    }

    private void initAnnotationValue() {
        fieldInitialize.initAnnotation();
        clickListenerMethod.initAnnotation();
    }

    public void clearBinder() {
        driverAppUI = null;
        VIEW_GROUP_HASHMAP.clear();
    }
}
