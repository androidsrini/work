package com.codesense.driverapp.ui.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.data.CitiesItem;
import com.codesense.driverapp.data.CountriesItem;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.localstoreage.DatabaseClient;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.Constant;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;
import com.codesense.driverapp.request.RegisterNewUser;
import com.codesense.driverapp.ui.verifymobile.VerifyMobileActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity {


    private static final String TAG = "DriverApp";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Inject
    protected RequestHandler requestHandler;
    @Inject
    protected Utility utility;
    @Inject
    protected AppSharedPreference appSharedPreference;
    @Initialize(R.id.tvRegisterDes)
    TextView tvRegisterDes;
    @Initialize(R.id.ll_name)
    LinearLayout ll_name;
    @Initialize(R.id.scrollView)
    ScrollView scrollView;
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
    AutoCompleteTextView etCity;
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
    /*@Initialize(R.id.toolbarClose)
    ImageView toolbarClose;*/
    @Initialize(R.id.etCountry)
    AutoCompleteTextView etCountry;
    @Initialize(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @Initialize(R.id.toolbar)
    Toolbar toolbar;
    private CountriesItem countriesItem;
    private CitiesItem citiesItem;

    /**
     * This method will start the RegisterActivity class
     * @param context
     */
    public static void start(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);
        setSupportActionBar(toolbar);
        functionality();
        setDynamicValue();
    }


    private void functionality() {
        tvTitle.setText(getResources().getText(R.string.register_button));
        if (null != getSupportActionBar()) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //toolbarClose.setBackgroundResource(R.drawable.icon_close);
        singleTextView(tvPrivacyPolicy, getResources().getString(R.string.register_policy_text_first_prefix1), getResources().getString(R.string.register_policy_text_prefix2), getResources().getString(R.string.register_policy_text_first_sufix1), getResources().getString(R.string.register_policy_text_first_sufix2), getResources().getColor(R.color.primary_color), getResources().getString(R.string.register_policy_app_name));
        fetchAndUpdateCountryListFromDataBase();
        fetchAndUpdateCitiesListFromDataBase();
        addTextWatcherForCountyAndCityUI();
    }

    private void addTextWatcherForCountyAndCityUI() {
        compositeDisposable.add(RxTextView.textChanges(etCountry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext->{
                    if (null != onNext && 0 < onNext.length()) {
                        findCountryFromCountryName(onNext.toString());
                    } else {
                        countriesItem = null;
                    }
                }));
        compositeDisposable.add(RxTextView.textChanges(etCity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(textViewAfterTextChangeEvent -> {
                    if (null != textViewAfterTextChangeEvent
                            && textViewAfterTextChangeEvent.length() > 0) {
                        findCityFromCityName(textViewAfterTextChangeEvent.toString());
                    } else {
                        citiesItem = null;
                    }
                }));
    }

    /**
     * This method to fetch country list from data base and update in country auto complete UI.
     */
    private void fetchAndUpdateCountryListFromDataBase() {
        compositeDisposable.add(DatabaseClient.getInstance(this).getAppDatabase().countryDao().getCountryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.d(TAG, " Country List data size is: " + result.size());
                    ArrayAdapter arrayAdapter = new ArrayAdapter<CountriesItem>(this, android.R.layout.simple_list_item_1, result);
                    etCountry.setAdapter(arrayAdapter);
                }, error -> {
                    Log.d(TAG, " Country List data getting error: " + Log.getStackTraceString(error));
                }));
    }

    /**
     * This method to fetch cities list from data base and update in cities auto complete UI.
     */
    private void fetchAndUpdateCitiesListFromDataBase() {
        compositeDisposable.add(DatabaseClient.getInstance(this).getAppDatabase().cityDao().getCityList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.d(TAG, " cities List data size is: " + result.size());
                    ArrayAdapter arrayAdapter = new ArrayAdapter<CitiesItem>(this, android.R.layout.simple_list_item_1, result);
                    etCity.setAdapter(arrayAdapter);
                }, error -> {
                    Log.d(TAG, " cities List data getting error: " + Log.getStackTraceString(error));
                }));
    }

    private void setDynamicValue() {

        int imgIconWidth = (int) (screenWidth * 0.075);
        int imgIconHeight = (int) (screenWidth * 0.075);

        /*RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) toolbarClose.getLayoutParams();
        imgLayParams.width = imgIconWidth;
        imgLayParams.height = imgIconHeight;
        toolbarClose.setLayoutParams(imgLayParams);*/

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

        FrameLayout.LayoutParams etPasswordLayoutParams = (FrameLayout.LayoutParams) etPassword.getLayoutParams();
        etPasswordLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace, topBottomSpace * 3, 0);
        etPassword.setLayoutParams(etPasswordLayoutParams);

        FrameLayout.LayoutParams etConfirmPasswordLayoutParams = (FrameLayout.LayoutParams) etConfirmPassword.getLayoutParams();
        etConfirmPasswordLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace, topBottomSpace * 3, 0);
        etConfirmPassword.setLayoutParams(etPasswordLayoutParams);

        LinearLayout.LayoutParams etCityLayoutParams = (LinearLayout.LayoutParams) etCity.getLayoutParams();
        etCityLayoutParams.setMargins(0, topBottomSpace, topBottomSpace * 3, 0);
        etCity.setLayoutParams(etCityLayoutParams);

        LinearLayout.LayoutParams etCountryLayoutParams = (LinearLayout.LayoutParams) etCountry.getLayoutParams();
        etCountryLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace, 0, 0);
        etCountry.setLayoutParams(etCountryLayoutParams);

        ConstraintLayout.LayoutParams etInviteCodeLayoutParams = (ConstraintLayout.LayoutParams) etInviteCode.getLayoutParams();
        etInviteCodeLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace, topBottomSpace * 3, 0);
        etInviteCode.setLayoutParams(etInviteCodeLayoutParams);

        ConstraintLayout.LayoutParams tvPrivacyPolicyLayoutParams = (ConstraintLayout.LayoutParams) tvPrivacyPolicy.getLayoutParams();
        tvPrivacyPolicyLayoutParams.setMargins(topBottomSpace * 4, topBottomSpace, topBottomSpace * 3, 0);
        tvPrivacyPolicy.setLayoutParams(tvPrivacyPolicyLayoutParams);

        tvPrivacyPolicy2.setPadding(topBottomSpace * 4, topBottomSpace, topBottomSpace * 2, topBottomSpace * 2);

    }

    private void singleTextView(TextView textView, final String first, String second, final String third,
                                final String fourth, final int color, final String appName) {
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

    /**
     * This method will return user enterd First name
     *
     * @return String
     */
    private String getEtFirstName() {
        return etFirstName.getText().toString().trim();
    }

    /**
     * This method will return user Entered last name.
     *
     * @return String
     */
    private String getEtLastName() {
        return etLastName.getText().toString().trim();
    }

    /**
     * This Method will return user entered Email address.
     *
     * @return String
     */
    private String getEtEmail() {
        return etEmail.getText().toString().trim();
    }

    /**
     * This method will return User Entered phone number
     *
     * @return String
     */
    private String getEtPhoneNumber() {
        return etPhoneNumber.getText().toString().trim();
    }

    /**
     * This method will return User entered password
     *
     * @return String
     */
    private String getEtPassword() {
        return etPassword.getText().toString().trim();
    }

    /**
     * This method will return User Entered Invite code.
     *
     * @return String
     */
    private String getEtInviteCode() {
        return etInviteCode.getText().toString().trim();
    }

    /**
     * This method will return user entered city
     *
     * @return String
     */
    public String getEtCity() {
        return etCity.getText().toString().trim();
    }

    /**
     * This method will return user entered country
     *
     * @return String
     */
    public String getEtCountry() {
        return etCountry.getText().toString().trim();
    }

    /**
     * This method will returned user entered ConfirmPassword
     * @return String
     */
    public String getEtConfirmPassword() {
        return etConfirmPassword.getText().toString();
    }

    /**
     * This method will create Register New User Object.
     *
     * @return RegisterNewUser
     */
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

    /**
     * This method call register new owner request API.
     */
    private void registerNewUserRequest() {
        compositeDisposable.add(requestHandler.registerNewOwnerRequest(ApiUtility.getInstance().getApiKeyMetaData(), createRegisterNewUserObject())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(d -> registerApiResponse(ApiResponse.loading(ServiceType.REGISTER_NEW_OWNER)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> registerApiResponse(ApiResponse.success(ServiceType.REGISTER_NEW_OWNER, result)),
                        error -> registerApiResponse(ApiResponse.error(ServiceType.REGISTER_NEW_OWNER, error))));
    }

    /**
     * This method will handle register new Driver api response.
     *
     * @param apiResponse
     */
    private void registerApiResponse(ApiResponse apiResponse) {
        ServiceType serviceType = apiResponse.getServiceType();
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (ServiceType.REGISTER_NEW_OWNER == serviceType) {
                    if ((null != apiResponse.getResponseJsonObject() && apiResponse.isValidResponse())
                            || ApiResponse.OTP_VALIDATION == apiResponse.getResponseStatus()) {
                        appSharedPreference.saveUserType(apiResponse.getResponseJsonObject().optString(Constant.USER_TYPE_RESPONSE));
                        appSharedPreference.saveUserID(apiResponse.getResponseJsonObject().optString(Constant.USER_ID_RESPONSE));
                        appSharedPreference.savePhoneNum(apiResponse.getResponseJsonObject().optString(Constant.MOBILE_NUMBER_PARAM));
                        appSharedPreference.setDisplayName(apiResponse.getResponseJsonObject().optString(Constant.DISPLAY_NAME_PARAM));
                        appSharedPreference.saveAccessToken(apiResponse.getResponseJsonObject().optString(Constant.ACCESS_TOKEN_PARAM));
                        appSharedPreference.setProfilePicture(apiResponse.getResponseJsonObject().optString(Constant.PROFILE_PARAM));
                        VerifyMobileActivity.start(this, apiResponse.getResponseJsonObject().optString(Constant.USER_ID_RESPONSE),
                                getEtPhoneNumber(), VerifyMobileActivity.NEED_TO_CALL_SEND_OTP);
                    } else if (null != apiResponse.getResponseJsonObject()) {
                        utility.showToastMsg(apiResponse.getResponseMessage());
                    }
                }
                break;
            case ERROR:
                utility.showToastMsg(apiResponse.getResponseMessage());
                utility.dismissDialog();
                break;
        }
    }

    /**
     * This method to find any mandatory field are empty
     *
     * @return boolean
     */
    private boolean doesAnyMandatoyFieldIsEmpty() {
        return TextUtils.isEmpty(getEtFirstName()) || TextUtils.isEmpty(getEtLastName())
                || TextUtils.isEmpty(getEtPhoneNumber()) || TextUtils.isEmpty(getEtPassword())
                || TextUtils.isEmpty(getEtPassword()) || TextUtils.isEmpty(getEtEmail());
    }

    /**
     * This method is used for get country object from data base based on user enter value.
     *
     * @param s Editable argument
     */
    private void findCountryFromCountryName(String s) {
        compositeDisposable.add(DatabaseClient.getInstance(this).getAppDatabase().countryDao().findByCountryName(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    countriesItem = result;
                    //showCountryErrorMsg();
                }, error -> {
                    countriesItem = null;
                    //showCountryErrorMsg();
                }));
    }

    /**
     * This method is used for get city object from data base based on user enter value.
     *
     * @param s Editable argument
     */
    private void findCityFromCityName(String s) {
        compositeDisposable.add(DatabaseClient.getInstance(this).getAppDatabase().cityDao().findByCityName(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    citiesItem = result;
                    //showCityErrorMsg();
                }, error -> {
                    citiesItem = null;
                    //showCityErrorMsg();
                }));
    }

    /**
     * This method to display Invalid Country selection error msg
     */
    private void showCountryErrorMsg() {
        if (null == countriesItem) {
            utility.showToastMsg(this, "Please Enter Valid Country");
        }
    }

    /**
     * This method to display Invalid City selection error msg
     */
    private void showCityErrorMsg() {
        if (null == citiesItem) {
            utility.showToastMsg(this, "Please Enter Valid city");
        }
    }

    /**
     * This method will be validate and show error msg
     * @return boolean
     */
    private boolean isValidAllFields() {
        boolean isValid = true;
        if (TextUtils.isEmpty(getEtFirstName())) {
            utility.showToastMsg(getString(R.string.first_name_empty));
            isValid = false;
        } else if (TextUtils.isEmpty(getEtLastName())) {
            utility.showToastMsg(getString(R.string.last_name_empty));
            isValid = false;
        } else if (TextUtils.isEmpty(getEtEmail())) {
            utility.showToastMsg(getString(R.string.email_address_empty));
            isValid = false;
        } else if (TextUtils.isEmpty(getEtPhoneNumber())) {
            utility.showToastMsg(getString(R.string.phone_number_empty));
            isValid = false;
        } else if (TextUtils.isEmpty(getEtPassword())) {
            utility.showToastMsg(getString(R.string.password_empty));
            isValid = false;
        } else if (TextUtils.isEmpty(getEtConfirmPassword())) {
            utility.showToastMsg(getString(R.string.confirm_password_empty));
            isValid = false;
        } else if (!utility.isValidEmailAddress(getEtEmail())) {
            utility.showToastMsg(getString(R.string.email_not_valid));
            isValid = false;
        } else if (!getEtPassword().equals(getEtConfirmPassword())) {
            utility.showToastMsg(getString(R.string.password_not_matched));
            isValid = false;
        } else if (getEtPhoneNumber().length() != getResources().getInteger(R.integer.max_length_phone_number)) {
            utility.showToastMsg(getString(R.string.mobile_number_not_valid));
            isValid = false;
        }
        return isValid;
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
        etCountry.addTextChangedListener(null);
        etCity.addTextChangedListener(null);
    }

    @Onclick(R.id.fbNext)
    public void fbNext(View v) {
        //Update new uer to server.
        if (isValidAllFields()) {
            registerNewUserRequest();
        }
    }

    /*@Onclick(R.id.toolbarClose)
    public void toolbarClose(View v) {
        finish();
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
