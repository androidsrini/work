package com.codesense.driverapp.ui.verifymobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.ui.selecttype.SelectTypeActivity;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

import java.util.concurrent.TimeUnit;

public class VerifyMobileActivity extends BaseActivity {

    @Initialize(R.id.tvPhoneNumber)
    TextView tvPhoneNumber;
    @Initialize(R.id.optNumber1)
    EditText optNumber1;
    @Initialize(R.id.optNumber2)
    EditText optNumber2;
    @Initialize(R.id.optNumber3)
    EditText optNumber3;
    @Initialize(R.id.optNumber4)
    EditText optNumber4;
    @Initialize(R.id.btnEditNumber)
    Button btnEditNumber;
    @Initialize(R.id.btnResend)
    Button btnResend;
    @Initialize(R.id.imgNext)
    ImageButton imgNext;
    @Initialize(R.id.tvTitle)
    TextView tvTitle;
    @Initialize(R.id.view)
    View view;
    @Initialize(R.id.view1)
    View view1;
    @Initialize(R.id.view2)
    View view2;
    @Initialize(R.id.view3)
    View view3;
    @Initialize(R.id.toolbarClose)
    ImageView toolbarClose;

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
        return R.layout.activity_verify_mobile;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);
        tvTitle.setText(getResources().getText(R.string.verify_title));
        setDynamicValue();
        functionality();
    }

    private void functionality() {
        optNumber1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                view.setBackgroundResource(R.drawable.view_for_edittext_primary);
                view1.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
                view2.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
                view3.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
                return false;
            }
        });

        optNumber2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                view.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
                view1.setBackgroundResource(R.drawable.view_for_edittext_primary);
                view2.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
                view3.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
                return false;
            }
        });

        optNumber1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                view.setBackgroundResource(R.drawable.view_for_edittext_primary);
                view1.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
                view2.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
                view3.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
            }
        });

        optNumber2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                view.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
                view1.setBackgroundResource(R.drawable.view_for_edittext_primary);
                view2.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
                view3.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
            }
        });

        optNumber3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                view.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
                view1.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
                view2.setBackgroundResource(R.drawable.view_for_edittext_primary);
                view3.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
            }
        });
        optNumber4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                view.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
                view1.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
                view2.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
                view3.setBackgroundResource(R.drawable.view_for_edittext_primary);
            }
        });
        startStop();

    }

    private void setDynamicValue() {
        int topBottomSpace = (int) (screenHeight * 0.0089);

        int imgIconWidth = (int) (screenWidth * 0.075);
        int imgEditheight = (int) (screenWidth * 0.105);
        int imgIconHeight = (int) (screenWidth * 0.075);


        RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) toolbarClose.getLayoutParams();
        imgLayParams.width = imgIconWidth;
        imgLayParams.height = imgIconHeight;
        toolbarClose.setLayoutParams(imgLayParams);

        ConstraintLayout.LayoutParams tvPhoneNumberLayoutParams = (ConstraintLayout.LayoutParams) tvPhoneNumber.getLayoutParams();
        tvPhoneNumberLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace * 2, topBottomSpace * 2, 0);
        tvPhoneNumber.setLayoutParams(tvPhoneNumberLayoutParams);

        tvPhoneNumber.setPadding(0, 0, topBottomSpace * 3, 0);
        ConstraintLayout.LayoutParams optNumber1LayoutParams = (ConstraintLayout.LayoutParams) optNumber1.getLayoutParams();
        optNumber1LayoutParams.setMargins(topBottomSpace * 3, topBottomSpace * 7, 0, 0);
        optNumber1.setLayoutParams(optNumber1LayoutParams);


        ConstraintLayout.LayoutParams optNumber2LayoutParams = (ConstraintLayout.LayoutParams) optNumber2.getLayoutParams();
        optNumber2LayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 7, topBottomSpace * 2, 0);
        optNumber2.setLayoutParams(optNumber2LayoutParams);

        ConstraintLayout.LayoutParams optNumber3LayoutParams = (ConstraintLayout.LayoutParams) optNumber3.getLayoutParams();
        optNumber3LayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 7, topBottomSpace * 2, 0);
        optNumber3.setLayoutParams(optNumber3LayoutParams);

        ConstraintLayout.LayoutParams optNumber4LayoutParams = (ConstraintLayout.LayoutParams) optNumber4.getLayoutParams();
        optNumber4LayoutParams.setMargins(topBottomSpace * 3, topBottomSpace * 7, topBottomSpace * 2, 0);
        optNumber4.setLayoutParams(optNumber4LayoutParams);


        ConstraintLayout.LayoutParams viewLayoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        viewLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace, 0, 0);
        view.setLayoutParams(viewLayoutParams);

        ConstraintLayout.LayoutParams view1LayoutParams = (ConstraintLayout.LayoutParams) view1.getLayoutParams();
        view1LayoutParams.setMargins(topBottomSpace * 2, topBottomSpace, topBottomSpace * 2, 0);
        view1.setLayoutParams(view1LayoutParams);

        ConstraintLayout.LayoutParams view2LayoutParams = (ConstraintLayout.LayoutParams) view2.getLayoutParams();
        view2LayoutParams.setMargins(topBottomSpace * 2, topBottomSpace, topBottomSpace * 2, 0);
        view2.setLayoutParams(view2LayoutParams);

        ConstraintLayout.LayoutParams view3LayoutParams = (ConstraintLayout.LayoutParams) view3.getLayoutParams();
        view3LayoutParams.setMargins(topBottomSpace * 2, topBottomSpace, 0, 0);
        view3.setLayoutParams(view3LayoutParams);

        LinearLayout.LayoutParams btnEditNumberLayoutParams = (LinearLayout.LayoutParams) btnEditNumber.getLayoutParams();
        btnEditNumberLayoutParams.height = imgEditheight;
        btnEditNumberLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace * 4, topBottomSpace, 0);
        btnEditNumber.setLayoutParams(btnEditNumberLayoutParams);

        LinearLayout.LayoutParams btnResendLayoutParams = (LinearLayout.LayoutParams) btnResend.getLayoutParams();
        btnResendLayoutParams.height = imgEditheight;
        btnResendLayoutParams.setMargins(topBottomSpace, topBottomSpace * 4, topBottomSpace, 0);
        btnResend.setLayoutParams(btnResendLayoutParams);

        LinearLayout.LayoutParams imgNextLayoutParams = (LinearLayout.LayoutParams) imgNext.getLayoutParams();
        imgNextLayoutParams.setMargins(0, topBottomSpace * 2, topBottomSpace, 0);
        imgNext.setLayoutParams(imgNextLayoutParams);
    }

    @Onclick(R.id.toolbarClose)
    public void toolbarClose(View v) {
        finish();
    }

    @Onclick(R.id.imgNext)
    public void imgNext(View v) {
        Intent intent = new Intent(this, SelectTypeActivity.class);
        startActivity(intent);
    }

    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnResend.setText(hmsTimeFormatter(millisUntilFinished));


            }

            @Override
            public void onFinish() {
                btnResend.setText(getResources().getString(R.string.resend_text));
                timerStatus = TimerStatus.STOPPED;
            }

        }.start();
        countDownTimer.start();
    }

    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;
    }

    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {

            // call to initialize the progress bar values

            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();


        } else {
            stopCountDownTimer();

        }

    }

    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }


}
