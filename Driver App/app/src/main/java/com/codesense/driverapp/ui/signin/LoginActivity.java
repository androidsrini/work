package com.codesense.driverapp.ui.signin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.data.OwnerTypeResponse;
import com.codesense.driverapp.data.OwnerTypesItem;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.Constant;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.ui.selecttype.SelectTypeActivity;
import com.codesense.driverapp.ui.uploaddocument.UploadDocumentActivity;
import com.codesense.driverapp.ui.verifymobile.VerifyMobileActivity;
import com.google.gson.Gson;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
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
    @Initialize(R.id.toolbarClose)
    ImageView toolbarClose;
    /**
     * To add the asyncrones subscribe process RXJava
     */
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static void start(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);
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
                .doOnSubscribe(d->loginResponse(ApiResponse.loading(), ServiceType.SIGNIN_OWNER))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result->loginResponse(ApiResponse.success(result), ServiceType.SIGNIN_OWNER),
                        error->{loginResponse(ApiResponse.error(error), ServiceType.SIGNIN_OWNER);}));
    }

    private void fetchOwnerTypeRequest() {
        compositeDisposable.add(requestHandler.fetchOwnerTypeRequest(ApiUtility.getInstance().getApiKeyMetaData())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(d->loginResponse(ApiResponse.loading(), ServiceType.OWNER_TYPES))
        .subscribe(result->loginResponse(ApiResponse.success(result), ServiceType.OWNER_TYPES),
                error->loginResponse(ApiResponse.error(error), ServiceType.OWNER_TYPES)));
    }

    /**
     * This method to handle api response
     * @param apiResponse
     */
    private void loginResponse(ApiResponse apiResponse, ServiceType serviceType) {
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (null != apiResponse.getResponseJsonObject()) {
                    if (ServiceType.SIGNIN_OWNER == serviceType) {
                        if (!TextUtils.isEmpty(apiResponse.getResponseJsonObject().optString(Constant.USER_ID_RESPONSE))) {
                            appSharedPreference.saveUserID(apiResponse.getResponseJsonObject().optString(Constant.USER_ID_RESPONSE));
                            appSharedPreference.saveAccessToken(apiResponse.getResponseJsonObject().optString(Constant.ACCESS_TOKEN_RESPONSE));
                            appSharedPreference.saveOwnerTypeId(apiResponse.getResponseJsonObject().optInt(Constant.OWNER_TYPE_ID_RESPONSE, -1));
                            appSharedPreference.saveOtpVerify(apiResponse.getResponseJsonObject().optInt(Constant.OTP_VERIFY_RESPONSE, -1));
                            appSharedPreference.saveMobileNumber(apiResponse.getResponseJsonObject().optString(Constant.MOBILE_NUMBER_RESPONSE));
                            appSharedPreference.saveUserType(apiResponse.getResponseJsonObject().optString(Constant.USER_TYPE_RESPONSE));
                        }
                        fetchOwnerTypeRequest();
                    } else if (ServiceType.OWNER_TYPES == serviceType) {
                        if (-1 == appSharedPreference.getOtpVerify() || 0 == appSharedPreference.getOtpVerify()) {
                            VerifyMobileActivity.start(this, appSharedPreference.getUserID(), appSharedPreference.getMobileNumberKey());
                        } else if (-1 == appSharedPreference.getOwnerTypeId() || 0 == appSharedPreference.getOwnerTypeId()) {
                            SelectTypeActivity.start(this);
                            //TO kill this activity class from backstack
                            finish();
                        } else {
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
                        }
                    }
                }
                break;
            case ERROR:
                utility.dismissDialog();
                break;
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

    @Onclick(R.id.btnLogin)
    protected void siginInClick(View v) {
        if (isValidAllFields()) {
            signInRequest();
        }
    }

    private enum ServiceType {
        SIGNIN_OWNER, OWNER_TYPES
    }
}
