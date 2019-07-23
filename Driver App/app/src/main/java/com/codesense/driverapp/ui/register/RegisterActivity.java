package com.codesense.driverapp.ui.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.data.CitiesItem;
import com.codesense.driverapp.data.CountriesItem;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.request.RegisterNewUser;
import com.codesense.driverapp.ui.verifymobile.VerifyMobileActivity;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity {


    private CountriesItem countriesItem;
    private CitiesItem citiesItem;
    @Initialize(R.id.tvRegisterDes)
    TextView tvRegisterDes;
    @Initialize(R.id.ll_name)
    LinearLayout ll_name;
    @Initialize(R.id.etFirstName)
    EditText etFirstName;
    @Initialize(R.id.etLastName)
    EditText etLastName;
    @Initialize(R.id.etEmail)
    EditText etEmail;
    @Initialize(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @Initialize(R.id.etPassword)
    EditText etPassword;
    @Initialize(R.id.etCity)
    EditText etCity;
    @Initialize(R.id.etInviteCode)
    EditText etInviteCode;
    @Initialize(R.id.tvPrivacyPolicy)
    TextView tvPrivacyPolicy;
    @Initialize(R.id.tvPrivacyPolicy2)
    TextView tvPrivacyPolicy2;
    @Initialize(R.id.fbNext)
    FloatingActionButton fbNext;
    @Initialize(R.id.tvTitle)
    TextView tvTitle;
    @Initialize(R.id.toolbarClose)
    ImageView toolbarClose;
    @Initialize(R.id.etCountry)
    EditText etCountry;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Inject
    protected RequestHandler requestHandler;
    @Inject
    protected Utility utility;


    @Override
    protected int layoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);
        functionality();
        setDynamicValue();
    }


    private void functionality() {
        tvTitle.setText(getResources().getText(R.string.register_button));
        toolbarClose.setBackgroundResource(R.drawable.icon_close);
        singleTextView(tvPrivacyPolicy, getResources().getString(R.string.register_policy_text_first_prefix1), getResources().getString(R.string.register_policy_text_prefix2), getResources().getString(R.string.register_policy_text_first_sufix1), getResources().getString(R.string.register_policy_text_first_sufix2), getResources().getColor(R.color.primary_color), getResources().getString(R.string.register_policy_app_name));
        setCountryAndCitiesOnTouchListener();
    }

    private void startCountryCitiesSelectionScreen(CountryCitiesSelectionActivity.ActivityType activityType) {
        Intent starter = new Intent(this, CountryCitiesSelectionActivity.class);
        starter.putExtra(CountryCitiesSelectionActivity.ACTIVITY_TYPE, activityType);
        startActivityForResult(starter, CountryCitiesSelectionActivity.REQUEST_CODE);
    }

    /**
     * This method will set touch listener for city and country edit text.
     */
    private void setCountryAndCitiesOnTouchListener() {
        etCity.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                //Show cities list screen.
                startCountryCitiesSelectionScreen(CountryCitiesSelectionActivity.ActivityType.CITIES);
            }
            return true;
        });
        etCountry.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                //Show country list screen.
                startCountryCitiesSelectionScreen(CountryCitiesSelectionActivity.ActivityType.COUNTRY);
            }
            return true;
        });
    }


    private void setDynamicValue() {

        int imgIconWidth = (int) (screenWidth * 0.075);
        int imgIconHeight = (int) (screenWidth * 0.075);

        RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) toolbarClose.getLayoutParams();
        imgLayParams.width = imgIconWidth;
        imgLayParams.height = imgIconHeight;
        toolbarClose.setLayoutParams(imgLayParams);

        int topBottomSpace = (int) (screenHeight * 0.0089);

        ConstraintLayout.LayoutParams tvRegisterDesLayoutParams = (ConstraintLayout.LayoutParams) tvRegisterDes.getLayoutParams();
        tvRegisterDesLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace * 2, topBottomSpace * 3, 0);
        tvRegisterDes.setLayoutParams(tvRegisterDesLayoutParams);

        ConstraintLayout.LayoutParams ll_nameLayoutParams = (ConstraintLayout.LayoutParams) ll_name.getLayoutParams();
        ll_nameLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace * 3, topBottomSpace * 3, 0);
        ll_name.setLayoutParams(ll_nameLayoutParams);

        LinearLayout.LayoutParams etFirstNameLayoutParams = (LinearLayout.LayoutParams) etFirstName.getLayoutParams();
        etFirstNameLayoutParams.setMargins(0, topBottomSpace, 0, 0);
        etFirstName.setLayoutParams(etFirstNameLayoutParams);

        LinearLayout.LayoutParams etLastNameLayoutParams = (LinearLayout.LayoutParams) etLastName.getLayoutParams();
        etLastNameLayoutParams.setMargins(0, topBottomSpace, 0, 0);
        etLastName.setLayoutParams(etLastNameLayoutParams);

        ConstraintLayout.LayoutParams etEmailLayoutParams = (ConstraintLayout.LayoutParams) etEmail.getLayoutParams();
        etEmailLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace, topBottomSpace * 3, 0);
        etEmail.setLayoutParams(etEmailLayoutParams);

        ConstraintLayout.LayoutParams etPhoneNumberLayoutParams = (ConstraintLayout.LayoutParams) etPhoneNumber.getLayoutParams();
        etPhoneNumberLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace, topBottomSpace * 3, 0);
        etPhoneNumber.setLayoutParams(etPhoneNumberLayoutParams);

        ConstraintLayout.LayoutParams etPasswordLayoutParams = (ConstraintLayout.LayoutParams) etPassword.getLayoutParams();
        etPasswordLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace, topBottomSpace * 3, 0);
        etPassword.setLayoutParams(etPasswordLayoutParams);

        LinearLayout.LayoutParams etCityLayoutParams = (LinearLayout.LayoutParams) etCity.getLayoutParams();
        etCityLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace, topBottomSpace * 3, 0);
        etCity.setLayoutParams(etCityLayoutParams);

        ConstraintLayout.LayoutParams etInviteCodeLayoutParams = (ConstraintLayout.LayoutParams) etInviteCode.getLayoutParams();
        etInviteCodeLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace, topBottomSpace * 3, 0);
        etInviteCode.setLayoutParams(etInviteCodeLayoutParams);

        ConstraintLayout.LayoutParams tvPrivacyPolicyLayoutParams = (ConstraintLayout.LayoutParams) tvPrivacyPolicy.getLayoutParams();
        tvPrivacyPolicyLayoutParams.setMargins(topBottomSpace * 4, topBottomSpace, topBottomSpace * 3, 0);
        tvPrivacyPolicy.setLayoutParams(tvPrivacyPolicyLayoutParams);

        tvPrivacyPolicy2.setPadding(topBottomSpace * 4, topBottomSpace, topBottomSpace * 2, topBottomSpace * 2);

    }

    private void singleTextView(TextView textView, final String first, String second, final String third, final String fourth, final int color, final String appName) {

        final SpannableStringBuilder spanText = new SpannableStringBuilder();
        spanText.append(first);
        spanText.append(appName);
        spanText.append(" ");
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setUnderlineText(false);
                textPaint.setFakeBoldText(true);
            }
        }, spanText.length() - appName.length(), spanText.length(), 0);
        spanText.append(second);

        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // On Click Action
            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(color);    // you can use custom color
                textPaint.setUnderlineText(false);
                textPaint.setFakeBoldText(true);
                // this remove the underline
            }
        }, spanText.length() - second.length(), spanText.length(), 0);

        spanText.append(" ");
        spanText.append(third);
        spanText.append(" ");
        spanText.append(fourth);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // On Click Action
            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(color);    // you can use custom color
                textPaint.setUnderlineText(false);
                textPaint.setFakeBoldText(true);
                // this remove the underline
            }
        }, spanText.length() - fourth.length(), spanText.length(), 0);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spanText, TextView.BufferType.SPANNABLE);
    }

    public String getEtFirstName() {
        return etFirstName.getText().toString().trim();
    }

    public String getEtLastName() {
        return etLastName.getText().toString().trim();
    }

    public String getEtEmail() {
        return etEmail.getText().toString().trim();
    }

    public String getEtPhoneNumber() {
        return etPhoneNumber.getText().toString().trim();
    }

    public String getEtPassword() {
        return etPassword.getText().toString().trim();
    }

    public String getEtCity() {
        return etCity.getText().toString().trim();
    }

    public String getEtInviteCode() {
        return etInviteCode.getText().toString().trim();
    }

    public String getEtCountry() {
        return etCountry.getText().toString().trim();
    }

    private RegisterNewUser createRegisterNewUserObject() {
        RegisterNewUser registerNewUser = new RegisterNewUser();
        registerNewUser.setFirstName(getEtFirstName());
        registerNewUser.setLastName(getEtLastName());
        registerNewUser.setMobileNumber(getEtPhoneNumber());
        registerNewUser.setPassword(getEtPassword());
        if (null != countriesItem) {
            registerNewUser.setCountryId(countriesItem.countryId);
        }
        if (null != citiesItem) {
            registerNewUser.setCityId(citiesItem.getCityId());
        }
        registerNewUser.setInviteCode(getEtInviteCode());
        registerNewUser.setEmailId(getEtEmail());
        return registerNewUser;
    }

    private void registerNewUserRequest() {
        compositeDisposable.add(requestHandler.registerNewOwnerRequest(ApiUtility.getInstance().getApiKeyMetaData(), createRegisterNewUserObject())
                .doOnSubscribe(d->registerApiResponse(ApiResponse.loading()))
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result->registerApiResponse(ApiResponse.success(result)),
                        error->registerApiResponse(ApiResponse.error(error))));
    }

    private void registerApiResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (apiResponse.isValidResponse()) {
                    startActivity(new Intent(this, VerifyMobileActivity.class));
                }
                break;
            case ERROR:
                utility.dismissDialog();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CountryCitiesSelectionActivity.REQUEST_CODE == requestCode && Activity.RESULT_OK == resultCode && null != data) {
            CountryCitiesSelectionActivity.ActivityType activityType = (CountryCitiesSelectionActivity.ActivityType) data.getSerializableExtra(CountryCitiesSelectionActivity.ACTIVITY_TYPE);
            if (null != activityType) {
                if (CountryCitiesSelectionActivity.ActivityType.CITIES == activityType) {
                    //Update city edit text UI.
                    citiesItem = data.getParcelableExtra(CountryCitiesSelectionActivity.RESULT_DATA);
                    if (null != citiesItem) {
                        etCity.setText(citiesItem.getCityName());
                    }
                } else if (CountryCitiesSelectionActivity.ActivityType.COUNTRY == activityType) {
                    //Update Country Edit text UI.
                    countriesItem = data.getParcelableExtra(CountryCitiesSelectionActivity.RESULT_DATA);
                    if (null != countriesItem) {
                        etCountry.setText(countriesItem.getCountryName());
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * To clear listener for edit text.
         */
        etCity.setOnTouchListener(null);
        etCountry.setOnTouchListener(null);
    }

    @Onclick(R.id.fbNext)
    public void fbNext(View v) {
        //Update new uer to server.
        registerNewUserRequest();
        /*Intent intent = new Intent(this, VerifyMobileActivity.class);
        startActivity(intent);*/
    }

    @Onclick(R.id.toolbarClose)
    public void toolbarClose(View v) {
        finish();
    }
}
