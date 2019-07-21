package com.codesense.driverapp.ui.launchscreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.ui.register.RegisterActivity;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

import javax.inject.Inject;

/**
 * Launch screen of the application and get public-key
 */
public class LaunchScreenActivity extends BaseActivity {

    private static final String TAG = "Driver";

    @Initialize(R.id.tvWelcomeScreen)
    TextView tvWelcomeScreen;
    @Initialize(R.id.tvdriver)
    TextView tvdriver;
    @Initialize(R.id.tvRide)
    TextView tvRide;
    @Initialize(R.id.ll_btn)
    LinearLayout ll_btn;
    @Initialize(R.id.btnSignIn)
    Button btnSignIn;
    @Initialize(R.id.btnRegister)
    Button btnRegister;
    @Initialize(R.id.view)
    View view;
    @Initialize(R.id.tvAppName)
    TextView tvAppName;
    @Inject
    protected LaunchScreenViewModel launchScreenViewModel;
    @Inject
    protected Utility utility;

    @Override
    protected int layoutRes() {
        return R.layout.activity_launch_screen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);
        launchScreenViewModel.getApiResponseMutableLiveData().observe(this, this::profileApiResponse);
        launchScreenViewModel.loadArticleDetails();
        setDynamicValue();
    }

    private void profileApiResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                Log.d(TAG, "Success data: " + apiResponse.data);
                break;
            case ERROR:
                utility.dismissDialog();
                Log.d(TAG, "Error data: " + apiResponse.data);
                break;
        }
    }

    private void setDynamicValue() {
        int topBottomSpace = (int) (screenHeight * 0.0089);

        LinearLayout.LayoutParams tvWelcomeScreenLayoutParams = (LinearLayout.LayoutParams) tvWelcomeScreen.getLayoutParams();
        tvWelcomeScreenLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace, 0, 0);
        tvWelcomeScreen.setLayoutParams(tvWelcomeScreenLayoutParams);

        LinearLayout.LayoutParams tvAppNameLayoutParams = (LinearLayout.LayoutParams) tvAppName.getLayoutParams();
        tvAppNameLayoutParams.setMargins(0, topBottomSpace, topBottomSpace * 3, 0);
        tvAppName.setLayoutParams(tvAppNameLayoutParams);

        LinearLayout.LayoutParams tvdriverLayoutParams = (LinearLayout.LayoutParams) tvdriver.getLayoutParams();
        tvdriverLayoutParams.setMargins(topBottomSpace * 3, 0, topBottomSpace * 3, 0);
        tvdriver.setLayoutParams(tvdriverLayoutParams);

        LinearLayout.LayoutParams ll_btnLayoutParams = (LinearLayout.LayoutParams) ll_btn.getLayoutParams();
        ll_btnLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace * 5, topBottomSpace * 3, 0);
        ll_btn.setLayoutParams(ll_btnLayoutParams);

        LinearLayout.LayoutParams btn_signInLayoutParams = (LinearLayout.LayoutParams) btnSignIn.getLayoutParams();
        btn_signInLayoutParams.setMargins(0, 0, topBottomSpace, 0);
        btnSignIn.setLayoutParams(btn_signInLayoutParams);

        LinearLayout.LayoutParams btn_registerLayoutParams = (LinearLayout.LayoutParams) btnRegister.getLayoutParams();
        btn_registerLayoutParams.setMargins(topBottomSpace, 0, 0, 0);
        btnRegister.setLayoutParams(btn_registerLayoutParams);

        LinearLayout.LayoutParams viewLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        viewLayoutParams.setMargins(0, topBottomSpace * 4, 0, 0);
        view.setLayoutParams(viewLayoutParams);

        LinearLayout.LayoutParams tvRideLayoutParams = (LinearLayout.LayoutParams) tvRide.getLayoutParams();
        tvRideLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 2, 0, 0);
        tvRide.setLayoutParams(tvRideLayoutParams);
    }

    @Onclick(R.id.btnRegister)
    public void btnRegister(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
