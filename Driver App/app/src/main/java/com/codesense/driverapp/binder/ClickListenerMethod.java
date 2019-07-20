package com.codesense.driverapp.binder;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ClickListenerMethod {

    private Activity activity;
    private HashMap<String, View> stringViewHashMap;
    private Object object;
    private View view;

    private ClickListenerMethod(ClickListenerMethodBuilder clickListenerMethodBuilder) {
        this.activity = clickListenerMethodBuilder.activity;
        this.stringViewHashMap = clickListenerMethodBuilder.stringViewHashMap;
        this.object = clickListenerMethodBuilder.object;
        this.view = clickListenerMethodBuilder.view;
    }

    protected void initAnnotation() {
        if (activity != null) {
            updateOnClickMethodListener();
        } else {
            updateObjectOnClickMethodListener();
        }
    }

    private void updateClickListenerAction(View view, Method method) {
        view.setOnClickListener(v -> {
            try {
                method.setAccessible(true);
                Class[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 0) {
                    if (activity != null) {
                        method.invoke(activity);
                    } else {
                        method.invoke(object);
                    }
                } else if (parameterTypes.length == 1) {
                    if (activity != null) {
                        method.invoke(activity, v);
                    } else {
                        method.invoke(object, v);
                    }
                } else {
                    throw new IllegalAccessException("Wrong number of arguments; expected 0 or 1");
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateOnClickMethodListener() {
        Method[] methods = activity.getClass().getDeclaredMethods();
        /*for (Method method : methods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                String[] viewName = onClick.value();
                for (String name: viewName) {
                    View view = stringViewHashMap.get(name);
                    if (view == null) {
                        view = BinderUtil.GetInstance().findViewByName(activity, name);
                        stringViewHashMap.put(name, view);
                    }
                    updateClickListenerAction(view, method);
                }
            }
        }*/
    }

    private void updateObjectOnClickMethodListener() {
        Method[] methods = object.getClass().getDeclaredMethods();
        /*for (Method method : methods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                String[] viewName = onClick.value();
                for (String name: viewName) {
                    View view = stringViewHashMap.get(name);
                    if (view == null) {
                        view = BinderUtil.GetInstance().findViewByName(this.view, name);
                        stringViewHashMap.put(name, view);
                    }
                    updateClickListenerAction(view, method);
                }
            }
        }*/
    }

    protected static class ClickListenerMethodBuilder {
        private Activity activity;
        private HashMap<String, View> stringViewHashMap;
        private Object object;
        private View view;

        ClickListenerMethodBuilder(Activity activity, HashMap<String, View> stringViewHashMap) {
            this.activity = activity;
            this.stringViewHashMap = stringViewHashMap;
        }

        ClickListenerMethodBuilder(Object object, View view, HashMap<String, View> stringViewHashMap) {
            this.object = object;
            this.view = view;
            this.stringViewHashMap = stringViewHashMap;
        }

        protected ClickListenerMethod build() {
            return new ClickListenerMethod(this);
        }
    }
}
