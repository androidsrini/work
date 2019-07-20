package com.codesense.driverapp.binder;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.codesense.driverapp.exception.BinderResourceNotFoundException;
import com.codesense.driverapp.exception.ExceptionMessageConstant;

import java.lang.reflect.Field;
import java.util.HashMap;

public class FieldInitialize {

    private Activity activity;
    private Object object;
    private View view;
    private HashMap<String, View> stringViewHashMap;

    private FieldInitialize(FieldInitializeBuilder fieldInitializeBuilder) {
        this.activity = fieldInitializeBuilder.activity;
        this.stringViewHashMap = fieldInitializeBuilder.stringViewHashMap;
        this.object = fieldInitializeBuilder.object;
        this.view = fieldInitializeBuilder.view;
    }

    protected void initAnnotation() {
        if (activity != null) {
            updateInitializeView();
        } else {
            updateViewInitializeField();
        }
    }

    private void initializedFieldValue(Field field, String name) {
        int resourceName;
        if (activity != null) {
            resourceName = BinderUtil.GetInstance().findResourceIdByName(activity, name);
        } else {
            resourceName = BinderUtil.GetInstance().findResourceIdByName(view, name);
        }
        //Log.d(TAG, "View buinder value: " + initialize.value() + " ,resource id: " + resourceName);
        if (resourceName != View.NO_ID) {
            try {
                field.setAccessible(true); // should work on private fields
                View view;
                if (activity != null) {
                    view = BinderUtil.GetInstance().findViewById(activity, resourceName);
                } else {
                    view = BinderUtil.GetInstance().findViewById(this.view, resourceName);
                }
                if (view != null) {
                    stringViewHashMap.put(name, view);
                    field.set(activity != null ? activity : object, view);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            throw new BinderResourceNotFoundException(ExceptionMessageConstant.NO_RESOURCE);
        }
    }

    private View updateFieldValue(Field field, String name) {
        initializedFieldValue(field, name);
        return stringViewHashMap.get(name);
    }

    private void updateLayoutParam(Activity activity, View view, ViewGroup.LayoutParams layoutParams,
                                   float resizeWidth, float resizedHeight) {
        //DecimalFormat decimalFormat = new DecimalFormat("#.####");
        float vWidth = resizeWidth / BinderUtil.DEFAULT_WIDTH;
        float vHeight = resizedHeight / BinderUtil.DEFAULT_HEIGHT;
        int width = (int) (BinderUtil.GetInstance(activity).getWidth() * vWidth);
        int height = (int) (BinderUtil.GetInstance(activity).getHeight() * vHeight);
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    private void updateLayoutParam(View view, ViewGroup.LayoutParams layoutParams,
                                   float resizeWidth, float resizedHeight) {
        //DecimalFormat decimalFormat = new DecimalFormat("#.####");
        float vWidth = resizeWidth / BinderUtil.DEFAULT_WIDTH;
        float vHeight = resizedHeight / BinderUtil.DEFAULT_HEIGHT;
        int width = (int) (BinderUtil.GetInstance(this.view.getContext()).getWidth() * vWidth);
        int height = (int) (BinderUtil.GetInstance(this.view.getContext()).getHeight() * vHeight);
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

//    private void updateResizeField(Field field) {
//        Resize resize = field.getAnnotation(Resize.class);
//        if (resize != null) {
//            field.setAccessible(true); // should work on private fields
//            View view = stringViewHashMap.get(resize.viewName());
//            if (view == null) {
//                view = updateFieldValue(field, resize.viewName());
//            }
//            if (view != null) {
//                //Log.d(TAG, " The reflation view available: " + (view != null));
//                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//                if (activity != null) {
//                    updateLayoutParam(activity, view, layoutParams, resize.width(), resize.height());
//                } else {
//                    updateLayoutParam(view, layoutParams, resize.width(), resize.height());
//                }
//                view.setLayoutParams(layoutParams);
//            }
//        }
//    }

    private void updateInitializeField(Field field) {
        /*Initialize initialize = field.getAnnotation(Initialize.class);
        if (initialize != null) {
            initializedFieldValue(field, initialize.value());
        }*/
    }

    private void updateInitializeView() {
        Field[] fields = activity.getClass().getDeclaredFields();
        for (Field field : fields) {
            updateInitializeField(field);
//            updateResizeField(field);
        }
    }

    private void updateViewInitializeField() {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            updateInitializeField(field);
//            updateResizeField(field);
        }
    }

    protected static class FieldInitializeBuilder {
        private Activity activity;
        private HashMap<String, View> stringViewHashMap;
        private Object object;
        private View view;

        FieldInitializeBuilder(Activity activity, HashMap<String, View> stringViewHashMap) {
            this.activity = activity;
            this.stringViewHashMap = stringViewHashMap;
        }

        FieldInitializeBuilder(Object object, View view, HashMap<String, View> stringViewHashMap) {
            this.object = object;
            this.view = view;
            this.stringViewHashMap = stringViewHashMap;
        }

        protected FieldInitialize build() {
            return new FieldInitialize(this);
        }
    }
}
