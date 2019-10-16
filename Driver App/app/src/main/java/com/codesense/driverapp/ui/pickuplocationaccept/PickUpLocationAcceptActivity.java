package com.codesense.driverapp.ui.pickuplocationaccept;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.ui.accept.AcceptActivity;
import com.codesense.driverapp.ui.helper.Utils;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PickUpLocationAcceptActivity extends BaseActivity {

    @Initialize(R.id.progressBarCircle)
    ProgressBar progressBarCircle;
    @Initialize(R.id.textViewTime)
    TextView textViewTime;

    private long timeCountInMilliSeconds = 60000;
    private int timeCountProgressSeconds = 1;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;


    private CountDownTimer countDownTimer;

    @Override
    protected int layoutRes() {
        return R.layout.activity_pickup_location_accept;
    }

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        ProductBindView.bind(this);
        startStop();
    }


    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {

            // call to initialize the progress bar values
            setProgressBarValues();

            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();


        } else {
            stopCountDownTimer();

        }

    }


    /**
     * method to start count down timer
     */
    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                timeCountProgressSeconds++;
                progressBarCircle.setProgress(timeCountProgressSeconds);
                progressBarCircle.setMax(120);

            }

            @Override
            public void onFinish() {
                timeCountProgressSeconds++;
                progressBarCircle.setProgress(timeCountProgressSeconds);
                Utils.saveIntegerToPrefs(PickUpLocationAcceptActivity.this, "acceptDrive", 0);
                finish();
                timerStatus = TimerStatus.STOPPED;
            }

        }.start();
        countDownTimer.start();
    }

    private void setProgressBarValues() {

        progressBarCircle.setProgress(timeCountProgressSeconds);
        progressBarCircle.setMax(timeCountProgressSeconds);
    }

    /**
     * method to stop count down timer
     */
    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }


    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format(Locale.getDefault(),"%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
        return hms;
    }

    @Onclick(R.id.btnAccept)
    public void btnAccept(View v) {
        //Utils.saveIntegerToPrefs(this, "acceptDrive", 1);
        //TODO show accept screen
        AcceptActivity.start(this);
        finish();
    }

    @Onclick(R.id.btnReject)
    public void btnReject(View v) {
        Utils.saveIntegerToPrefs(this, "acceptDrive", 0);
        finish();
    }
}
