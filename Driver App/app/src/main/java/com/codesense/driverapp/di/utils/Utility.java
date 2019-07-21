package com.codesense.driverapp.di.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.UiThread;
import com.codesense.driverapp.R;

public class Utility {

    /*private static final Utility utility = new Utility();*/
    private ProgressDialog progressDialog;

    /*public static  Utility GetInstance() {
        return utility;
    }*/

    public void showProgressDialog(@UiThread Context context) {
        progressDialog = ProgressDialog.show(context, null, context.getString(R.string.loading), false);
    }

    @UiThread
    public void dismissDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


}
