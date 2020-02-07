package com.codesense.driverapp.ui.signin;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.data.ChangePasswordResponse;
import com.codesense.driverapp.data.ForgotPasswordResponse;
import com.codesense.driverapp.data.SigninOwnerResponse;
import com.codesense.driverapp.data.VerifyForgotPasswordResponse;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.Constant;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;
import com.codesense.driverapp.ui.addvehicle.AddVehicleActivity;
import com.codesense.driverapp.ui.documentstatus.DocumentStatusActivity;
import com.codesense.driverapp.ui.legalagreement.LegalAgreementActivity;
import com.codesense.driverapp.ui.online.OnlineActivity;
import com.codesense.driverapp.ui.selecttype.SelectTypeActivity;
import com.codesense.driverapp.ui.uploaddocument.UploadDocumentActivity;
import com.codesense.driverapp.ui.vehicle.VehicleListActivity;
import com.codesense.driverapp.ui.verifymobile.VerifyMobileActivity;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    /**
     * To create utility class object.
     */
    @Inject
    protected Utility utility;
    /**
     * To create RequestHandler object
     */
    @Inject
    protected RequestHandler requestHandler;
    /**
     * To create AppSharedPreference object.
     */
    @Inject
    protected AppSharedPreference appSharedPreference;
    @Initialize(R.id.imgLaunchImage)
    ImageView imgLaunchImage;
    @Initialize(R.id.etEmail)
    EditText etEmail;
    @Initialize(R.id.etPassword)
    EditText etPassword;
    @Initialize(R.id.btnLogin)
    Button btnLogin;
    @Initialize(R.id.tvTitle)
    TextView tvTitle;
    @Initialize(R.id.toolbar)
    Toolbar toolbar;
    @Initialize(R.id.toolbarClose)
    ImageView toolbarClose;
    @Initialize(R.id.tvForgot)
    TextView tvForgot;

    BottomSheetDialog bottomSheetDialog;
    View v;

    ImageView imgClose;
    Button btnSubmit;
    EditText et_mobileNumber;
    TextView tvPhoneNumber;
    EditText optNumber1, optNumber2, optNumber3, optNumber4, etForgotPassword, etConfirmForgotPassword;
    View view, view1, view2, view3;
    FloatingActionButton nextFloatingActionButton;
    ConstraintLayout rlOTP;
    ConstraintLayout rlPassword;
    RelativeLayout rlContact;
    Button btnSubmitPass;

    String verifyCode;

    private boolean ispasteValue;

    private ClipboardManager clipboardManager;
    private boolean isValiedAllFields;


    /**
     * To add the asyncrones subscribe process RXJava
     */
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static void start(Context context) {
        Intent intent = new Intent(context,
                LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ispasteValue = false;
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);
//        setSupportActionBar(toolbar);
        functionality();
        setDynamicValue();
    }

    private void setDynamicValue() {

        int imgIconWidth = (int) (screenWidth * 0.075);
        int imgIconHeight = (int) (screenWidth * 0.075);

        RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) toolbarClose.getLayoutParams();
        imgLayParams.width = imgIconWidth;
        imgLayParams.height = imgIconHeight;
        toolbarClose.setLayoutParams(imgLayParams);

        int topBottomSpace = (int) (screenHeight * 0.0089);

        ConstraintLayout.LayoutParams etEmailLayoutParams = (ConstraintLayout.LayoutParams) etEmail.getLayoutParams();
        etEmailLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace * 2, topBottomSpace * 3, 0);
        etEmail.setLayoutParams(etEmailLayoutParams);

        FrameLayout.LayoutParams etPasswordLayoutParams = (FrameLayout.LayoutParams) etPassword.getLayoutParams();
        etPasswordLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace * 2, topBottomSpace * 3, 0);
        etPassword.setLayoutParams(etPasswordLayoutParams);

        ConstraintLayout.LayoutParams btnLoginLayoutParams = (ConstraintLayout.LayoutParams) btnLogin.getLayoutParams();
        btnLoginLayoutParams.setMargins(topBottomSpace * 4, topBottomSpace * 10, topBottomSpace * 4, 0);
        btnLogin.setLayoutParams(btnLoginLayoutParams);
    }

    private void functionality() {
        tvTitle.setText(getResources().getText(R.string.login_title));
        /*if (null != getSupportActionBar()) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        }*/
//        toolbarClose.setBackground(null);
        toolbarClose.setBackgroundResource(R.drawable.icon_close);
    }

    /**
     * This method will return user entered email address
     *
     * @return String
     */
    private String getEtEmail() {
        return etEmail.getText().toString().trim();
    }

    /**
     * This method will returned user entered password.
     *
     * @return String
     */
    private String getEtPassword() {
        return etPassword.getText().toString().trim();
    }

    private String getEtConformPassword() {
        return etConfirmForgotPassword.getText().toString().trim();
    }

    private String getEtForgotPassword() {
        return etForgotPassword.getText().toString().trim();
    }

    /**
     * This method to validate all fields and show the error toast msg.
     *
     * @return boolean
     */
    private boolean isValidAllFields() {
        boolean isValid = true;
        if (TextUtils.isEmpty(getEtEmail())) {
            utility.showToastMsg(getString(R.string.email_address_empty));
            isValid = false;
        } else if (TextUtils.isEmpty(getEtPassword())) {
            utility.showToastMsg(getString(R.string.password_empty));
            isValid = false;
        } else if (!utility.isValidEmailAddress(getEtEmail())) {
            utility.showToastMsg(getString(R.string.email_not_valid));
            isValid = false;
        }
        return isValid;
    }

    /**
     * This method to validate user information is correct or not.
     */
    private void signInRequest() {
        compositeDisposable.add(requestHandler.signInRequest(ApiUtility.getInstance().getApiKeyMetaData(), getEtEmail(), getEtPassword())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(d -> loginResponse(ApiResponse.loading(ServiceType.SIGNIN_OWNER)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> loginResponse(ApiResponse.success(ServiceType.SIGNIN_OWNER, result)),
                        error -> {
                            loginResponse(ApiResponse.error(ServiceType.SIGNIN_OWNER, error));
                        }));
    }

    private void fetchOwnerTypeRequest() {
        compositeDisposable.add(requestHandler.fetchOwnerTypeRequest(ApiUtility.getInstance().getApiKeyMetaData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> loginResponse(ApiResponse.loading(ServiceType.OWNER_TYPES)))
                .subscribe(result -> loginResponse(ApiResponse.success(ServiceType.OWNER_TYPES, result)),
                        error -> loginResponse(ApiResponse.error(ServiceType.OWNER_TYPES, error))));
    }

    /**
     * This method to handle api response
     *
     * @param apiResponse
     */
    private void loginResponse(ApiResponse apiResponse) {
        ServiceType serviceType = apiResponse.getServiceType();
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (null != apiResponse.getResponseJsonObject() && apiResponse.getResponseStatus() != 0) {
                    if (ServiceType.SIGNIN_OWNER == serviceType) {
                        updatePreferenceValues(apiResponse);
                        fetchOwnerTypeRequest();
                    } else if (ServiceType.OWNER_TYPES == serviceType && apiResponse.getResponseStatus() != 0) {
                        if (ApiResponse.OTP_VALIDATION == apiResponse.getResponseStatus() || -1 == appSharedPreference.getOtpVerify() || 0 == appSharedPreference.getOtpVerify()) {
                            VerifyMobileActivity.start(this, appSharedPreference.getUserID(), appSharedPreference.getMobileNumberKey(),
                                    ApiResponse.OTP_VALIDATION != apiResponse.getResponseStatus());
                        } else if (-1 == appSharedPreference.getOwnerTypeId() || 0 == appSharedPreference.getOwnerTypeId()) {
                            SelectTypeActivity.start(this);
                            //TO kill this activity class from backstack
                            finish();
                        } else if (-1 == appSharedPreference.getAgreementAccept() || 0 == appSharedPreference.getAgreementAccept()) {
                            LegalAgreementActivity.start(this, "" + appSharedPreference.getOwnerTypeId());
                            //TO kill this activity class from backstack
                            finish();
                        } /*else if (1 != appSharedPreference.getIsActivate() || 0 == appSharedPreference.getOtpVerify()){
                            //To show dashboard screen.
                            OwnerTypeResponse ownerTypeResponse = new Gson().fromJson(apiResponse.data, OwnerTypeResponse.class);
                            if (null != ownerTypeResponse) {
                                for (OwnerTypesItem ownerTypesItem: ownerTypeResponse.getOwnerTypes()) {
                                    if (ownerTypesItem.getOwnerTypeId().equals(String.valueOf(appSharedPreference.getOwnerTypeId()))) {
                                        appSharedPreference.saveOwnerType(ownerTypesItem.getOwnerType());
                                        break;
                                    }
                                }
                            }
                            UploadDocumentActivity.start(this);
                            finish();
                        }*/ else if (1 != appSharedPreference.getIsActivate()) {
                            //To show online screen.
                            //OnlineActivity.start(this);
                            if (Constant.OWNER_ID.equals(String.valueOf(appSharedPreference.getOwnerTypeId()))) {
                                DocumentStatusActivity.start(this, Constant.NON_DRIVER_DOCUMENT_STATUS);
                                finish();
                            } else if (1 != appSharedPreference.getVehicleActivate()) {
                                AddVehicleActivity.start(this);
                                finish();
                            }
                        } else if (appSharedPreference.getIsActivate() == 1) {
                            if (Constant.OWNER_ID.equals(String.valueOf(appSharedPreference.getOwnerTypeId()))) {
                                OnlineActivity.start(this);
                                finish();
                            } else {
                                VehicleListActivity.start(this);
                                finish();
                            }

                        } else {
                            UploadDocumentActivity.start(this);
                        }
                    } else if (serviceType == ServiceType.FORGOT_PASSWORD) {
                        if ((null != apiResponse.getResponseJsonObject() && apiResponse.isValidResponse())
                                || ApiResponse.FORGOT_OTP_VALIDATION == apiResponse.getResponseStatus()) {
                            updateForgotValues(apiResponse);
                        }
                    } else if (serviceType == ServiceType.FORGOT_VERIFY_OTP) {
                        updateVerifyPassword(apiResponse);
                    } else if (serviceType == ServiceType.CHANGE_PASSWORD) {
                        if (apiResponse.isValidResponse()) {
                            appSharedPreference.clear();
                            appSharedPreference.saveUserID("");
                            ChangePasswordResponse changePasswordResponse = new Gson().fromJson(apiResponse.data, ChangePasswordResponse.class);
                            utility.showToastMsg(changePasswordResponse.getMessage());
                            bottomSheetDialog.dismiss();
                        }
                    }
                } else {
                    utility.showToastMsg(apiResponse.getResponseMessage());
                }
                break;
            case ERROR:
                utility.dismissDialog();
                break;
        }
    }

    private void updatePreferenceValues(ApiResponse apiResponse) {
        if (null != apiResponse && null != apiResponse.getResponseJsonObject()) {
            SigninOwnerResponse signinOwnerResponse = new Gson().fromJson(apiResponse.data, SigninOwnerResponse.class);
            appSharedPreference.saveUserID(signinOwnerResponse.getUserId());
            appSharedPreference.saveAccessToken(signinOwnerResponse.getAccessToken());
            appSharedPreference.saveOwnerTypeId(signinOwnerResponse.getOwnerTypeId());
            appSharedPreference.saveOtpVerify(signinOwnerResponse.getOtpVerify());
            appSharedPreference.saveVehicleActivate(signinOwnerResponse.getIs_vehicle_activate());
            appSharedPreference.saveMobileNumber(signinOwnerResponse.getMobileNumber());
            appSharedPreference.saveUserType(signinOwnerResponse.getUserType());
            appSharedPreference.setCountryDialCode(signinOwnerResponse.getCountryDialCode());
            appSharedPreference.setEmailId(signinOwnerResponse.getEmailId());
            appSharedPreference.setProfilePicture(signinOwnerResponse.getProfilePicture());
            appSharedPreference.saveIsActivate(signinOwnerResponse.getIs_activated());
            appSharedPreference.saveIsLive(signinOwnerResponse.getLiveStatus());
            appSharedPreference.saveIsAgreement(signinOwnerResponse.getAgreementAccept());
            appSharedPreference.setDisplayName(signinOwnerResponse.getDisplayName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Onclick(R.id.toolbarClose)
    public void toolbarClose(View v) {
        finish();
    }

    @Onclick(R.id.tvForgot)
    public void tvForgot(View v) {
        if (bottomSheetDialog == null)
            showBottomSheetDialogForgot();
    }

    @Onclick(R.id.btnLogin)
    protected void siginInClick(View v) {
        if (isValidAllFields()) {
            signInRequest();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint({"ResourceAsColor", "InflateParams"})
    public void showBottomSheetDialogForgot() {
        v = getLayoutInflater().inflate(R.layout.activity_forgot_password, null);

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(v);
        Object clipboardService = this.getSystemService(CLIPBOARD_SERVICE);
        clipboardManager = (ClipboardManager) clipboardService;


        imgClose = v.findViewById(R.id.img_close);
        btnSubmit = v.findViewById(R.id.btnSubmit);
        et_mobileNumber = v.findViewById(R.id.et_mobileNumber);
        tvPhoneNumber = v.findViewById(R.id.tvPhoneNumber);
        optNumber1 = v.findViewById(R.id.optNumber1);
        optNumber2 = v.findViewById(R.id.optNumber2);
        optNumber3 = v.findViewById(R.id.optNumber3);
        optNumber4 = v.findViewById(R.id.optNumber4);
        view = v.findViewById(R.id.view);
        view1 = v.findViewById(R.id.view1);
        view2 = v.findViewById(R.id.view2);
        view3 = v.findViewById(R.id.view3);
        nextFloatingActionButton = v.findViewById(R.id.nextFloatingActionButton);
        rlOTP = v.findViewById(R.id.rlOTP);
        rlContact = v.findViewById(R.id.rlContact);
        rlPassword = v.findViewById(R.id.rlPassword);
        etForgotPassword = v.findViewById(R.id.etForgotPassword);
        etConfirmForgotPassword = v.findViewById(R.id.etConfirmForgotPassword);
        btnSubmitPass = v.findViewById(R.id.btnSubmitPass);

        rlContact.setVisibility(View.VISIBLE);
        rlOTP.setVisibility(View.GONE);
        rlPassword.setVisibility(View.GONE);

        int imgCloseWidth = (int) (screenWidth * 0.085);
        int imgCloseHeight = (int) (screenHeight * 0.085);

        int imgIconWidth = (int) (screenWidth * 0.075);
        int imgEditheight = (int) (screenWidth * 0.105);
        int imgIconHeight = (int) (screenWidth * 0.075);

        RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) imgClose.getLayoutParams();
        imgLayParams.width = imgIconWidth;
        imgLayParams.height = imgIconHeight;
        imgClose.setLayoutParams(imgLayParams);

        imgClose.setBackgroundResource(R.drawable.ic_close);

        setDynamicValueBottom();
        functionalityBottom();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_mobileNumber.getHint().toString().contains("Mobile")) {
                    if (!TextUtils.isEmpty(et_mobileNumber.getText().toString().trim())) {
                        sendContactNoRequest(et_mobileNumber.getText().toString().trim());
                    } else {
                        utility.showToastMsg("Please enter your mobile number");
                    }
                }
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appSharedPreference.clear();
                bottomSheetDialog.dismiss();
            }
        });
        btnSubmitPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidAllFieldsForgot()) {
                    changePasswordRequest(verifyCode, etForgotPassword.getText().toString().trim());
                }
            }
        });

        nextFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValiedAllFields)
                    verifyOTPRequest(getVerifyOtp());
            }
        });
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();


    }

    public void sendContactNoRequest(String contactNo) {
        compositeDisposable.add(requestHandler.sendForgotPasswordRequest(ApiUtility.getInstance().getApiKeyMetaData(), contactNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> loginResponse(ApiResponse.loading(ServiceType.FORGOT_PASSWORD)))
                .subscribe(result -> loginResponse(ApiResponse.success(ServiceType.FORGOT_PASSWORD, result)),
                        error -> loginResponse(ApiResponse.error(ServiceType.FORGOT_PASSWORD, error))));
    }

    private void updateForgotValues(ApiResponse apiResponse) {
        if (null != apiResponse && null != apiResponse.getResponseJsonObject()) {
            ForgotPasswordResponse forgotPasswordResponse = new Gson().fromJson(apiResponse.data, ForgotPasswordResponse.class);
            appSharedPreference.saveUserID(forgotPasswordResponse.getUser_id());
            appSharedPreference.saveAccessToken(forgotPasswordResponse.getAccess_token());
            appSharedPreference.saveOtpVerify(forgotPasswordResponse.getOtp_verify());
            appSharedPreference.saveMobileNumber(forgotPasswordResponse.getMobile_number());
            appSharedPreference.saveUserType(forgotPasswordResponse.getUser_type());
            appSharedPreference.setCountryDialCode(forgotPasswordResponse.getCountry_dial_code());

            rlOTP.setVisibility(View.VISIBLE);
            rlContact.setVisibility(View.GONE);
            rlPassword.setVisibility(View.GONE);
            tvPhoneNumber.setText(getString(R.string.verify_mobile_text_place_holder, forgotPasswordResponse.getMobile_number()));

            setEditTextObserver();

        }
    }

    private void updateVerifyPassword(ApiResponse apiResponse) {
        if (null != apiResponse && null != apiResponse.getResponseJsonObject()) {
            VerifyForgotPasswordResponse forgotPasswordResponse = new Gson().fromJson(apiResponse.data, VerifyForgotPasswordResponse.class);

            verifyCode = forgotPasswordResponse.getVerification_code();
            rlContact.setVisibility(View.GONE);
            rlOTP.setVisibility(View.GONE);
            rlPassword.setVisibility(View.VISIBLE);


        }
    }

    private void functionalityBottom() {
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
                } else {
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
        if (clipData != null) {
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
        } else {
            view.setBackgroundResource(R.drawable.view_for_edittext_primary);
            view1.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
            view2.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
            view3.setBackgroundResource(R.drawable.view_for_edittext_lowconstract);
            optNumber2.requestFocus();
        }
        return true;
    }


    private void clearClipboard() {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        // you can set an empty string or set to null, same result
        //android.content.ClipData clip = android.content.ClipData.newPlainText("", "");
        android.content.ClipData clip = android.content.ClipData.newPlainText(null, null);
        clipboard.setPrimaryClip(clip);

    }

    @UiThread
    private void setDynamicValueBottom() {
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
                });
        compositeDisposable.add(disposable);
    }


    private String getVerifyOtp() {
        return optNumber1.getText().toString() + optNumber2.getText().toString() + optNumber3.getText().toString()
                + optNumber4.getText().toString();
    }

    private void verifyOTPRequest(String phoneNumber) {
        compositeDisposable.add(requestHandler.verifyOTPForgotRequest(ApiUtility.getInstance().getApiKeyMetaData(), phoneNumber)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(d -> loginResponse(ApiResponse.loading(ServiceType.FORGOT_VERIFY_OTP)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> loginResponse(ApiResponse.success(ServiceType.FORGOT_VERIFY_OTP, result)),
                        error -> loginResponse(ApiResponse.error(ServiceType.FORGOT_VERIFY_OTP, error))));
    }

    private void changePasswordRequest(String verifyCode, String password) {
        compositeDisposable.add(requestHandler.changePasswordRequest(ApiUtility.getInstance().getApiKeyMetaData(), verifyCode, password)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(d -> loginResponse(ApiResponse.loading(ServiceType.CHANGE_PASSWORD)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> loginResponse(ApiResponse.success(ServiceType.CHANGE_PASSWORD, result)),
                        error -> loginResponse(ApiResponse.error(ServiceType.CHANGE_PASSWORD, error))));
    }


    private boolean isValidAllFieldsForgot() {
        boolean isValid = true;
        if (TextUtils.isEmpty(getEtForgotPassword())) {
            utility.showToastMsg(getString(R.string.phone_number_empty));
            isValid = false;
        } else if (TextUtils.isEmpty(getEtForgotPassword())) {
            utility.showToastMsg(getString(R.string.password_empty));
            isValid = false;
        } else if (TextUtils.isEmpty(getEtConformPassword())) {
            utility.showToastMsg(getString(R.string.confirm_password_empty));
            isValid = false;
        } else if (!getEtForgotPassword().equals(getEtConformPassword())) {
            utility.showToastMsg(getString(R.string.password_not_matched));
            isValid = false;
        }
        return isValid;
    }
}
