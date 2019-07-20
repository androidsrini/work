package com.codesense.driverapp.binder;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.Display;
import android.view.View;

public class BinderUtil {

    private static final String ID_TYPE = "id";
    public static final float DEFAULT_WIDTH = 8.1f;
    public static final float DEFAULT_HEIGHT = 13.4f;
    private static BinderUtil binderUtil = null;
    private static final Object LOCK = new Object();
    private int width, height;

    private BinderUtil(Activity activity) {
        calculateScreenSize(activity);
    }

    private BinderUtil(Context context) {
        calculateScreenSize(context);
    }

    private BinderUtil() {

    }

    private void calculateScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = getDisplaySize(display);
        this.width = size.x;
        this.height = size.y;
    }

    private void calculateScreenSize(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = getDisplaySize(display);
        this.width = size.x;
        this.height = size.y;
    }

    private Point getDisplaySize(final Display display) {
        final Point point = new Point();
        display.getSize(point);
        return point;
    }

    public static BinderUtil GetInstance() {
        if (binderUtil == null) {
            binderUtil = new BinderUtil();
        }
        return binderUtil;
    }

    public static BinderUtil GetInstance(Activity activity) {
        if (binderUtil == null) {
            synchronized (LOCK) {
                binderUtil = new BinderUtil(activity);
            }
        } else {
            binderUtil.calculateScreenSize(activity);
        }
        return binderUtil;
    }

    public static BinderUtil GetInstance(Context context) {
        if (binderUtil == null) {
            synchronized (LOCK) {
                binderUtil = new BinderUtil(context);
            }
        } else {
            binderUtil.calculateScreenSize(context);
        }
        return binderUtil;
    }

    protected View findViewById(Activity activity, int viewId) {
        return activity.findViewById(viewId);
    }

    protected View findViewById(View view, int viewId) {
        return view.findViewById(viewId);
    }

    protected int findResourceIdByName(Activity activity, String resourceName) {
        try {
            return activity.getResources().
                    getIdentifier(resourceName, ID_TYPE, activity.getPackageName());
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return View.NO_ID;
        }
    }

    protected int findResourceIdByName(View view, String resourceName) {
        try {
            return view.getResources().
                    getIdentifier(resourceName, ID_TYPE, view.getContext().getPackageName());
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return View.NO_ID;
        }
    }

    protected View findViewByName(Activity activity, String viewName) {
        int resource = findResourceIdByName(activity, viewName);
        if (resource != View.NO_ID) {
            return findViewById(activity, resource);
        }
        return null;
    }

    protected View findViewByName(View view, String viewName) {
        int resource = findResourceIdByName(view, viewName);
        if (resource != View.NO_ID) {
            return findViewById(view, resource);
        }
        return null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

