package com.codesense.driverapp.ui.legalagreement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.data.OwnerTypesItem;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.Constant;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;
import com.codesense.driverapp.ui.signin.LoginActivity;
import com.codesense.driverapp.ui.uploaddocument.UploadDocumentActivity;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LegalAgreementActivity extends BaseActivity {


    private static final String OWNER_TYPE_ID_ARG = "OwnerTypeArg";
    @Initialize(R.id.tvTitle)
    TextView tvTitle;
    /*@Initialize(R.id.tvLegalText)
    TextView tvLegalText;*/
    @Initialize(R.id.toolbarClose)
    ImageView toolbarClose;
    @Initialize(R.id.view)
    View view;
    /*@Initialize(R.id.tvTitle)
    View view1;*/
    @Initialize(R.id.checkbox)
    CheckBox checkbox;
    @Initialize(R.id.btnAcceptContinue)
    Button btnAcceptContinue;
    @Initialize(R.id.legalArgumentWebView)
    WebView legalArgumentWebView;
    @Initialize(R.id.webViewProgressBar)
    ProgressBar webViewProgressBar;
    /**
     * To create RequestHandler object
     */
    @Inject
    RequestHandler requestHandler;
    /**
     * To create Utility object.
     */
    @Inject
    Utility utility;
    /**
     * To create AppSharedPreference object
     */
    @Inject
    protected AppSharedPreference appSharedPreference;
    /**
     * To create CompositeDisposable object.
     */
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private OwnerTypesItem ownerType;

    /**
     * This method to start LegalAgreementActivity class.
     *
     * @param context
     * @param ownerTypesItem
     */
    public static void start(Context context, OwnerTypesItem ownerTypesItem) {
        Intent intent = new Intent(context, LegalAgreementActivity.class);
        intent.putExtra(OWNER_TYPE_ID_ARG, ownerTypesItem);
        context.startActivity(intent);
    }

    public static void start(Context context, String ownerId) {
        Intent intent = new Intent(context, LegalAgreementActivity.class);
        intent.putExtra("owner_id", ownerId);
//        intent.putExtra("from", from);
        context.startActivity(intent);
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_legal_agreement;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);
        tvTitle.setText(getResources().getString(R.string.legal_title));
        legalArgumentWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String urlNewString) {
                webView.loadUrl(urlNewString);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                webViewProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webViewProgressBar.setVisibility(View.GONE);
            }
        });
        setDynamicValue();
        btnAcceptContinue.setEnabled(false);
        btnAcceptContinue.setClickable(false);
        fetchLegalAgreementRequest();

        toolbarClose.setVisibility(View.VISIBLE);
        toolbarClose.setBackgroundResource(R.drawable.ic_close);
    }

    private OwnerTypesItem getOwnerType() {
        if (null != getIntent() && null == ownerType) {
            ownerType = getIntent().getParcelableExtra(OWNER_TYPE_ID_ARG);
        }
        return ownerType;
    }

    private String getOwnerTypeId() {
        String ownerId = null;
        if (null == ownerType) {
            ownerType = getOwnerType();
            ownerId = ownerType.getOwnerTypeId();
        } else {
            if (null != getIntent()) {
                ownerId = getIntent().getStringExtra("owner_id");
            }
        }
        return ownerId;
    }

    private void setDynamicValue() {
        int topBottomSpace = (int) (screenHeight * 0.0089);
        int imgIconWidth = (int) (screenWidth * 0.075);
        int imgIconHeight = (int) (screenWidth * 0.075);

        RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) toolbarClose.getLayoutParams();
        imgLayParams.width = imgIconWidth;
        imgLayParams.height = imgIconHeight;
        toolbarClose.setLayoutParams(imgLayParams);

        /*ConstraintLayout.LayoutParams tvLegalTextLayoutParams = (ConstraintLayout.LayoutParams) tvLegalText.getLayoutParams();
        tvLegalTextLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 3, 0, 0);
        tvLegalText.setLayoutParams(tvLegalTextLayoutParams);
        tvLegalText.setPadding(0, 0, topBottomSpace * 2, 0);*/

        LinearLayout.LayoutParams checkboxLayoutParams = (LinearLayout.LayoutParams) checkbox.getLayoutParams();
        checkboxLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 3, 0, 0);
        checkbox.setLayoutParams(checkboxLayoutParams);
        checkbox.setPadding(topBottomSpace * 2, 0, topBottomSpace * 2, 0);

        /*LinearLayout.LayoutParams viewLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        viewLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 3, topBottomSpace * 2, 0);
        view.setLayoutParams(viewLayoutParams);
*/
        RelativeLayout.LayoutParams btnAcceptContinueLayoutParams = (RelativeLayout.LayoutParams) btnAcceptContinue.getLayoutParams();
        btnAcceptContinueLayoutParams.setMargins(topBottomSpace * 2, 0, topBottomSpace * 2, topBottomSpace * 2);
        btnAcceptContinue.setLayoutParams(btnAcceptContinueLayoutParams);
    }

    /**
     * This method to update RegistationOwnerType to server.
     */
    private void updateRegistationOwnerTypeRequest() {
        compositeDisposable.add(requestHandler.updateRegistationOwnerTypeRequest(ApiUtility.getInstance().getApiKeyMetaData(), getOwnerTypeId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> handleAgreementResponse(ApiResponse.loading(ServiceType.REGISTATION_OWNER_TYPE)))
                .subscribe(result -> handleAgreementResponse(ApiResponse.success(ServiceType.REGISTATION_OWNER_TYPE, result)),
                        error -> handleAgreementResponse(ApiResponse.error(ServiceType.REGISTATION_OWNER_TYPE, error))));
    }

    /**
     * This method to update legal AgreementAccept to server.
     */
    private void updateAgreementAcceptRequest() {
        compositeDisposable.add(requestHandler.updateAgreementAcceptRequest(ApiUtility.getInstance().getApiKeyMetaData(), "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> handleAgreementResponse(ApiResponse.loading(ServiceType.ACCEPT_LEGAL_AGREEMENT)))
                .subscribe(result -> handleAgreementResponse(ApiResponse.success(ServiceType.ACCEPT_LEGAL_AGREEMENT, result)),
                        error -> handleAgreementResponse(ApiResponse.error(ServiceType.ACCEPT_LEGAL_AGREEMENT, error))));
    }

    /**
     * This method to fetch LegalAgreement url
     */
    private void fetchLegalAgreementRequest() {
        compositeDisposable.add(requestHandler.getOwnerAgreementRequest(ApiUtility.getInstance().getApiKeyMetaData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> handleAgreementResponse(ApiResponse.loading(ServiceType.GET_LEGAL_AGREEMENT)))
                .subscribe(result -> handleAgreementResponse(ApiResponse.success(ServiceType.GET_LEGAL_AGREEMENT, result)),
                        error -> handleAgreementResponse(ApiResponse.error(ServiceType.GET_LEGAL_AGREEMENT, error))));
    }

    private void handleAgreementResponse(ApiResponse apiResponse) {
        ServiceType serviceType = apiResponse.getServiceType();
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (apiResponse.isValidResponse()) {
                    if (ServiceType.GET_LEGAL_AGREEMENT == serviceType) {
                        //Update webui.
                        if (null != apiResponse.getResponseJsonObject()) {
                            String legalAgreementUrl = apiResponse.getResponseJsonObject().optString(Constant.OWNER_AGREEMENT_RESPONSE);
                            if (!TextUtils.isEmpty(legalAgreementUrl)) {
                                legalArgumentWebView.loadUrl(legalAgreementUrl);
                            }
                        }
                    } else if (ServiceType.REGISTATION_OWNER_TYPE == serviceType) {
                        updateAgreementAcceptRequest();
                    } else if (ServiceType.ACCEPT_LEGAL_AGREEMENT == serviceType) {
                        //To show dashboard screen.
                        UploadDocumentActivity.start(this);
                        //To clear all Activity class from backstack
                        ActivityCompat.finishAffinity(this);

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

    @Onclick(R.id.toolbarClose)
    public void toolbarClose(View v) {
        LoginActivity.start(this);
        finish();
    }

    @Onclick(R.id.checkbox)
    public void checkbox(View v) {
        if (checkbox.isChecked()) {
            btnAcceptContinue.setBackgroundColor(getResources().getColor(R.color.primary_color));
            btnAcceptContinue.setTextColor(getResources().getColor(R.color.secondary_color));
            btnAcceptContinue.setEnabled(true);
            btnAcceptContinue.setClickable(true);
            toolbarClose.setVisibility(View.GONE);
        } else {
            btnAcceptContinue.setBackgroundColor(getResources().getColor(R.color.low_contrast));
            btnAcceptContinue.setTextColor(getResources().getColor(R.color.background_color));
            toolbarClose.setVisibility(View.VISIBLE);
            btnAcceptContinue.setEnabled(false);
            btnAcceptContinue.setClickable(false);
        }
    }

    @Onclick(R.id.btnAcceptContinue)
    public void btnAcceptContinue(View v) {
        if (checkbox.isChecked()) {
            updateAgreementAcceptRequest();
        }
    }

    /*private enum ServiceType {
        REGISTATION_OWNER_TYPE, ACCEPT_LEGAL_AGREEMENT, GET_LEGAL_AGREEMENT
    }*/
}
