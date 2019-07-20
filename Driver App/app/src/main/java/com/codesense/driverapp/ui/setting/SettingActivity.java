package com.codesense.driverapp.ui.setting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.ui.drawer.DrawerActivity;
import com.product.annotationbuilder.ProductBindView;

public class SettingActivity extends DrawerActivity {


    RelativeLayout rlSetting;
    RelativeLayout rlSettingAddBank;
    ImageView imgAddBank;
    TextView tvSettingAddBankText;
    RelativeLayout rlSettingPayment;
    ImageView imgPayment;
    TextView tvSettingPaymentText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_setting, null, false);
        frameLayout.addView(contentView);
        ProductBindView.bind(this);
        titleTextView.setText(getResources().getString(R.string.setting_title));
        initially();
        setDynamicValue();
    }

    private void setDynamicValue() {

        int imgIconWidth = (int) (screenWidth * 0.185);
        int imgIconHeight = (int) (screenWidth * 0.185);

        int topBottomSpace = (int) (screenHeight * 0.0089);


        RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) imgAddBank.getLayoutParams();
        imgLayParams.width = imgIconWidth;
        imgLayParams.height = imgIconHeight;
        imgAddBank.setLayoutParams(imgLayParams);

        RelativeLayout.LayoutParams imgPayLayParams = (RelativeLayout.LayoutParams) imgPayment.getLayoutParams();
        imgPayLayParams.width = imgIconWidth;
        imgPayLayParams.height = imgIconHeight;
        imgPayment.setLayoutParams(imgPayLayParams);


        ConstraintLayout.LayoutParams rlSettingLayoutParams = (ConstraintLayout.LayoutParams) rlSetting.getLayoutParams();
        rlSettingLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 17, topBottomSpace * 2, 0);
        rlSetting.setLayoutParams(rlSettingLayoutParams);

        RelativeLayout.LayoutParams rlSettingPaymentLayoutParams = (RelativeLayout.LayoutParams) rlSettingPayment.getLayoutParams();
        rlSettingPaymentLayoutParams.setMargins(0, topBottomSpace * 4, 0, 0);
        rlSettingPayment.setLayoutParams(rlSettingPaymentLayoutParams);

        tvSettingAddBankText.setPadding(0, topBottomSpace * 2, 0, topBottomSpace * 4);
        tvSettingPaymentText.setPadding(0, topBottomSpace * 2, 0, topBottomSpace * 4);
        imgAddBank.setPadding(topBottomSpace * 3, 0, 0, 0);
        imgPayment.setPadding(topBottomSpace * 3, 0, 0, 0);

    }

    private void initially() {
        rlSetting = findViewById(R.id.rlSetting);
        rlSettingAddBank = findViewById(R.id.rlSettingAddBank);
        imgAddBank = findViewById(R.id.imgAddBank);
        tvSettingAddBankText = findViewById(R.id.tvSettingAddBankText);
        rlSettingPayment = findViewById(R.id.rlSettingPayment);
        imgPayment = findViewById(R.id.imgPayment);
        tvSettingPaymentText = findViewById(R.id.tvSettingPaymentText);
    }
}
