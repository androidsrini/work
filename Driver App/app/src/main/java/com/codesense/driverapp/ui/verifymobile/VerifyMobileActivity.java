package com.codesense.driverapp.ui.verifymobile;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;
import com.codesense.driverapp.ui.editmobilenumber.EditMobileNumberActivity;
import com.codesense.driverapp.ui.selecttype.SelectTypeActivity;
import com.codesense.driverapp.ui.signin.LoginActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VerifyMobileActivity extends BaseActivity {

    private static final String TAG = "Driver";
    private static final String USER_ID_ARG = "UserID";
    private static final String PHONE_NUMBER_ARG = "PhoneNumber";
    private static final String IS_NEED_TO_CALL_SEND_OTP_ARG = "IsNeedToCallSendOtp";
    public static final boolean NEED_TO_CALL_SEND_OTP = true;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Inject
    protected RequestHandler requestHandler;
    @Inject
    protected Utility utility;
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
    @Initialize(R.id.errorResponseStripTextView)
    TextView errorResponseStripTextView;
    @Initialize(R.id.nextFloatingActionButton)
    FloatingActionButton nextFloatingActionButton;
    private String userID, phoneNumber;

    @Inject
    protected AppSharedPreference appSharedPreference;

    private long timeCountInMilliSeconds = 60000;
    private int timeCountProgressSeconds = 1;
    private TimerStatus timerStatus = TimerStatus.STOPPED;
    private CountDownTimer countDownTimer;
    private boolean isValiedAllFields;
    private ClipboardManager clipboardManager;
    private boolean isNeedToCallSendOtp;
    private boolean ispasteValue;

    public static void start(Context context, String userID, String phoneNumber, boolean isNeedToCallSendOtp) {
        Intent starter = new Intent(context, VerifyMobileActivity.class);
        starter.putExtra(USER_ID_ARG, userID);
        starter.putExtra(PHONE_NUMBER_ARG, phoneNumber);
        starter.putExtra(IS_NEED_TO_CALL_SEND_OTP_ARG, isNeedToCallSendOtp);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    public static void start(Context context, String userID, String phoneNumber, String from, boolean isNeedToCallSendOtp) {
        Intent starter = new Intent(context, VerifyMobileActivity.class);
        starter.putExtra(USER_ID_ARG, userID);
        starter.putExtra(PHONE_NUMBER_ARG, phoneNumber);
        starter.putExtra("from", from);
        starter.putExtra(IS_NEED_TO_CALL_SEND_OTP_ARG, isNeedToCallSendOtp);
        starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(starter);
    }

    private boolean isNeedToCallSendOtp() {
        if (null != getIntent()) {
            isNeedToCallSendOtp = getIntent().getBooleanExtra(IS_NEED_TO_CALL_SEND_OTP_ARG, false);
        }
        return isNeedToCallSendOtp;
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_verify_mobile;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);
        Object clipboardService = this.getSystemService(CLIPBOARD_SERVICE);
        clipboardManager = (ClipboardManager) clipboardService;
        setDynamicValue();
        functionality();
        updateUI();
        setEditTextObserver();
        /*if (!TextUtils.isEmpty(getUserId()) && isNeedToCallSendOtp()) {
            sendOTPRequest(getUserId(), getPhoneNumber());
        } else {*/
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("from") != null && !TextUtils.isEmpty(intent.getStringExtra("from"))) {
            if (intent.getStringExtra("from").equalsIgnoreCase("Edit")) {
                sendOTPRequest(getUserId(), getPhoneNumber());
            } else {
                startStop();
            }
        } else {
            startStop();
        }
        /* }*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        ispasteValue = false;
    }

    /**
     * This method will update Action bar title and append phone number.
     */
    @UiThread
    private void updateUI() {
        tvTitle.setText(getResources().getText(R.string.verify_title));
        tvPhoneNumber.setText(getString(R.string.verify_mobile_text_place_holder, getPhoneNumber()));
    }

    @UiThread
    private void updateErrorMessageStripUI(@NonNull String msg) {
        if (View.INVISIBLE == errorResponseStripTextView.getVisibility())
            errorResponseStripTextView.setVisibility(View.VISIBLE);
        errorResponseStripTextView.setText(msg);
    }

    @UiThread
    private void functionality() {
       /* optNumber1.setOnLongClickListener(this::handleClipBoardPaste);
        optNumber2.setOnLongClickListener(this::handleClipBoardPaste);
        optNumber3.setOnLongClickListener(this::handleClipBoardPaste);
        optNumber4.setOnLongClickListener(this::handleClipBoardPaste);*/
        toolbarClose.setImageResource(R.drawable.ic_close);
        optNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!ispasteValue && editable.length() > 0) {
                    ispasteValue = true;
                    handleClipBoardPaste();
                }else{
                    ispasteValue = false;
                }
            }
        });

        optNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() <= 0) {
                   optNumber1.requestFocus();
                }
            }
        });
        optNumber3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() <= 0) {
                    optNumber2.requestFocus();
                }
            }
        });
        optNumber4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() <= 0) {
                    optNumber3.requestFocus();
                }
            }
        });
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

/*
        if (android.os.Build.VERSION.SDK_INT < 11) {
            optNumber1.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

                @Override
                public void onCreateContextMenu(ContextMenu menu, View v,
                                                ContextMenu.ContextMenuInfo menuInfo) {
                    // TODO Auto-generated method stub
                    menu.clear();
                }
            });
        } else {
            optNumber1.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    // TODO Auto-generated method stub
                    return false;
                }

                public void onDestroyActionMode(ActionMode mode) {
                    // TODO Auto-generated method stub

                }

                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    // TODO Auto-generated method stub
                    return false;
                }

                public boolean onActionItemClicked(ActionMode mode,
                                                   MenuItem item) {
                    // TODO Auto-generated method stub
                    handleClipBoardPaste();
                    return false;
                }
            });
        }*/
    }

    private boolean handleClipBoardPaste() {
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData!=null) {
            int itemCount = clipData.getItemCount();
            if (itemCount > 0) {
                ClipData.Item item = clipData.getItemAt(0);
                if (item.getText() != null) {
                    String text = item.getText().toString();
                    if (text.length() == 4) {
                        optNumber1.setText(text.substring(0, 1));
                        optNumber2.setText(text.substring(1, 2));
                        optNumber3.setText(text.substring(2, 3));
                        optNumber4.setText(text.substring(3, 4));

                        optNumber4.requestFocus();
                        optNumber4.setSelection(optNumber4.getText().toString().trim().length());
                        view1.setBackgroundResource(R.drawable.view_for_edittext_primary);
                        view2.setBackgroundResource(R.drawable.view_for_edittext_primary);
                        view3.setBackgroundResource(R.drawable.view_for_edittext_primary);
                        view.setBackgroundResource(R.drawable.view_for_edittext_primary);
                        clearClipboard();
                    }

                }
                //destTextView.setText(text);

            }
        }else {
            view.setBackgroundResource(R.drawable.view_for_edittext_primary);
            view1.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
            view2.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
            view3.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
            optNumber2.requestFocus();
        }
        return true;
    }



    private void clearClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        // you can set an empty string or set to null, same result
        //android.content.ClipData clip = android.content.ClipData.newPlainText("", "");
        ClipData clip = ClipData.newPlainText(null, null);
        clipboard.setPrimaryClip(clip);

    }

    @UiThread
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

    /**
     * This method to observe verfication code edit text and call verfiy api.
     */
    @UiThread
    private void setEditTextObserver() {
        // Show button Active code when enough fields active code
        Observable<Boolean> mObsPhoneVerify1 = RxTextView.textChanges(optNumber1)
                .observeOn(AndroidSchedulers.mainThread())
                .map(charSequence -> {
                            boolean isField = null != charSequence && !charSequence.toString().equals("");
                            boolean nextViewIsEmpty = TextUtils.isEmpty(optNumber2.getText());
                            if (isField) {
                                optNumber1.setSelection(charSequence.length());
                                if (nextViewIsEmpty) {
                                    optNumber2.requestFocus();
                                }
                            }
                            return isField;
                        }
                );
        Observable<Boolean> mObsPhoneVerify2 = RxTextView.textChanges(optNumber2)
                .observeOn(AndroidSchedulers.mainThread())
                .map(charSequence -> {
                    boolean isField = null != charSequence && !charSequence.toString().equals("");
                    boolean nextViewIsEmpty = TextUtils.isEmpty(optNumber3.getText());
                    boolean previousViewIsEmpty = TextUtils.isEmpty(optNumber1.getText());
                    if (isField) {
                        optNumber2.setSelection(charSequence.length());
                        if (nextViewIsEmpty) {
                            optNumber3.requestFocus();
                        }
                    } else {
                        if (!previousViewIsEmpty) {
                            optNumber1.requestFocus();
                        }
                    }
                    return isField;
                });
        Observable<Boolean> mObsPhoneVerify3 = RxTextView.textChanges(optNumber3)
                .observeOn(AndroidSchedulers.mainThread())
                .map(charSequence -> {
                    boolean isField = null != charSequence && !charSequence.toString().equals("");
                    boolean nextViewIsEmpty = TextUtils.isEmpty(optNumber4.getText());
                    boolean previousViewIsEmpty = TextUtils.isEmpty(optNumber2.getText());
                    if (isField) {
                        optNumber3.setSelection(charSequence.length());
                        if (nextViewIsEmpty) {
                            optNumber4.requestFocus();
                        }
                    } else {
                        if (!previousViewIsEmpty) {
                            optNumber2.requestFocus();
                        }
                    }
                    return isField;
                });
        Observable<Boolean> mObsPhoneVerify4 = RxTextView.textChanges(optNumber4)
                .observeOn(AndroidSchedulers.mainThread())
                .map(charSequence -> {
                    //hideKeyboard();
                    boolean isFieldNotEmpty = charSequence != null && !charSequence.toString().equals("");
                    boolean previousViewIsEmpty = TextUtils.isEmpty(optNumber3.getText());
                    if (!isFieldNotEmpty) {
                        if (!previousViewIsEmpty) {
                            optNumber3.requestFocus();
                        }
                    } else {
                        optNumber4.setSelection(charSequence.length());
                    }
                    return isFieldNotEmpty;
                });

        Disposable disposable = Observable
                .combineLatest(mObsPhoneVerify1, mObsPhoneVerify2, mObsPhoneVerify3, mObsPhoneVerify4,
                        (PhoneVerify1, PhoneVerify2, PhoneVerify3, PhoneVerify4)
                                -> PhoneVerify1 && PhoneVerify2 && PhoneVerify3 && PhoneVerify4)
                .subscribe(result -> {
                    isValiedAllFields = result;
                    Log.d(TAG, " The verify observable: " + result);
                });
        compositeDisposable.add(disposable);
    }

    /**
     * This method will return user enter otp value
     *
     * @return string.
     */
    private String getVerifyOtp() {
        return optNumber1.getText().toString() + optNumber2.getText().toString() + optNumber3.getText().toString()
                + optNumber4.getText().toString();
    }

    private String getUserId() {
        if (null != getIntent() && TextUtils.isEmpty(userID)) {
            userID = getIntent().getStringExtra(USER_ID_ARG);
        }
        return userID;
    }

    private String getPhoneNumber() {
        if (null != getIntent() && TextUtils.isEmpty(phoneNumber)) {
            phoneNumber = getIntent().getStringExtra(PHONE_NUMBER_ARG);
            appSharedPreference.saveMobileNumber(phoneNumber);
            return phoneNumber;
        } else {
            phoneNumber = appSharedPreference.getPhoneNum();
        }
        return phoneNumber;
    }

    /**
     * This method is used for to send OTP request server.
     *
     * @param userID
     * @param phoneNumber
     */
    private void sendOTPRequest(String userID, String phoneNumber) {
        compositeDisposable.add(requestHandler.sentOTPRequest(ApiUtility.getInstance().getApiKeyMetaData(), userID, phoneNumber)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(d -> apiResponseHandle(ApiResponse.loading(ServiceType.SEND_OTP)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            apiResponseHandle(ApiResponse.success(ServiceType.SEND_OTP, result));
                        },
                        error -> {
                            apiResponseHandle(ApiResponse.error(ServiceType.SEND_OTP, error));
                        }));
    }

    /**
     * This method is used for to verify opt value with server
     *
     * @param userID
     * @param phoneNumber
     */
    private void verifyOTPRequest(String userID, String phoneNumber) {
        compositeDisposable.add(requestHandler.verifyOTPRequest(ApiUtility.getInstance().getApiKeyMetaData(), userID, phoneNumber)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(d -> apiResponseHandle(ApiResponse.loading(ServiceType.VERIFY_OTP)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> apiResponseHandle(ApiResponse.success(ServiceType.VERIFY_OTP, result)),
                        error -> apiResponseHandle(ApiResponse.error(ServiceType.VERIFY_OTP, error))));
    }

    /**
     * This method will handle verify mobile screen api responses.
     *
     * @param apiResponse
     */
    private void apiResponseHandle(ApiResponse apiResponse) {
        ServiceType serviceType = apiResponse.getServiceType();
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (ServiceType.SEND_OTP == serviceType) {
                    //Start countdown timer
                    /*if (apiResponse.getResponseStatus() != ApiResponse.OTP_VALIDATION) {
                        startStop();
                    } else {
                        //Disabled resend button because of 'OTP_VALIDATION'
                        btnResend.setEnabled(false);
                    }*/
                    startStop();

                } else if (ServiceType.VERIFY_OTP == serviceType) {
                    if (apiResponse.isValidResponse()) {
                        SelectTypeActivity.start(this);
                        //To finish this class.
                        finish();
                    } else {
                        updateErrorMessageStripUI(apiResponse.getResponseMessage());
                    }
                }
                break;
            case ERROR:
                utility.dismissDialog();
                if (ServiceType.SEND_OTP == serviceType) {
                    //Start countdown timer
                    startStop();
                }
                break;
        }
    }

    @Onclick(R.id.toolbarClose)
    public void toolbarClose(View v) {
        LoginActivity.start(this);
        finishAffinity();
    }

    @Onclick(R.id.nextFloatingActionButton)
    public void imgNext(View v) {
        if (isValiedAllFields)
            verifyOTPRequest(getUserId(), getVerifyOtp());
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
        return String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
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

    private boolean isCountDownTimerRunning() {
        return timerStatus == TimerStatus.STARTED;
    }

    @Onclick(R.id.btnResend)
    protected void resendOtpRequest(View v) {
        if (!TextUtils.isEmpty(getUserId()) && !isCountDownTimerRunning()) {
            sendOTPRequest(getUserId(), getPhoneNumber());
        }
    }

    @Onclick(R.id.btnEditNumber)
    protected void btnEditNumber(View v) {
        //Show number edit dialog.
        Intent intent = new Intent(this, EditMobileNumberActivity.class);
        intent.putExtra(USER_ID_ARG, getUserId());
        intent.putExtra(PHONE_NUMBER_ARG, phoneNumber);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    /*private enum ServiceType {
        SENT_OTP, VERIFY_OTP
    }*/
}
