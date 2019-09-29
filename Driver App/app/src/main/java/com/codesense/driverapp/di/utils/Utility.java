package com.codesense.driverapp.di.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.UiThread;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Toast;

import com.codesense.driverapp.R;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Utility {

    /*private static final Utility utility = new Utility();*/
    private ProgressDialog progressDialog;

    private Context appContext;

    public Utility(Context context) {
        this.appContext = context;
    }

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

    /**
     * This method to disply toast message based in UI context.
     * @param context
     * @param msg
     */
    @UiThread
    public void showToastMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * This method to show the toast message based on the app context
     * @param msg string
     */
    public void showToastMsg(String msg) {
        Toast.makeText(appContext, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * This method to check the given email address are valid or not.
     * @param email string
     * @return boolean
     */
    public boolean isValidEmailAddress(String email) {
        if (null != email) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }

    /**
     * This method to parse the sting value to int.
     * @param s
     * @return int
     */
    public int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * This method to show
     * @param context
     * @param msg
     * @param onClickListener
     */
    public void showOkDialog(Context context, String msg, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(context.getString(R.string.ok_label), null != onClickListener ?
                onClickListener : (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss());
        alertDialog.create().show();
    }

    /**
     * This method to display Conformation dialog it will show two button, negative button default it will close dialog, positive button will handle
     * @param context
     * @param msg
     * @param onClickListener
     */
    public void showConformationDialog(Context context, String msg, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(context.getString(R.string.yes_label), null != onClickListener ?
                onClickListener : (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss());
        alertDialog.setNegativeButton(context.getString(R.string.no_label), (dialog, which) -> dialog.dismiss());
        alertDialog.create().show();
    }

    public String decrypt(String key, String encrypted) throws Exception {
        byte[] rawKey = getRawKey(key.getBytes());
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

    private byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    private byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public byte[] toByte(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        return result;
    }


    public String decrypt(String result)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {
        byte[] decryptedBytes;
        final int CRYPTO_BITS = 2048;
        final String CRYPTO_METHOD = "RSA";
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(CRYPTO_METHOD);
        kpg.initialize(CRYPTO_BITS);
        KeyPair kp = kpg.genKeyPair();
        PrivateKey privateKey = kp.getPrivate();
        Cipher  cipher1 = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher1.init(Cipher.DECRYPT_MODE, privateKey);
        decryptedBytes = cipher1.doFinal(Base64.decode(result, Base64.CRLF));
        String decrypted = new String(decryptedBytes);
        return decrypted;
    }

    /**
     * This method to Expand views.
     * @param view
     */
    public void expand(final View view) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) view.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = view.getMeasuredHeight();
        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        view.getLayoutParams().height = 1;
        view.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                view.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // Expansion speed of 1dp/ms
        a.setDuration((int) (targetHeight / view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(a);
        //return view;
    }

    /**
     * This method to find mobile internet connection status.
     * If connection enabled return true else false.
     * @param context
     * @return
     */
    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isAvailable());
    }
}
