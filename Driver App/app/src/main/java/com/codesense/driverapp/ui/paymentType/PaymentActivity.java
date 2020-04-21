package com.codesense.driverapp.ui.paymentType;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.ui.setting.SettingActivity;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

public class PaymentActivity extends BaseActivity {

    @Initialize(R.id.rlPaymentTypeMain)
    RelativeLayout rlPaymentTypeMain;
    @Initialize(R.id.tvPaymentPrePaidText)
    TextView tvPaymentPrePaidText;
    @Initialize(R.id.tvPredesc)
    TextView tvPredesc;
    @Initialize(R.id.singleChoiceRadioPre)
    RadioButton singleChoiceRadioPre;
    @Initialize(R.id.rlPaymentPostType)
    RelativeLayout rlPaymentPostType;
    @Initialize(R.id.tvPaymentPostPaidText)
    TextView tvPaymentPostPaidText;
    @Initialize(R.id.tvPostdesc)
    TextView tvPostdesc;
    @Initialize(R.id.singleChoiceRadioPost)
    RadioButton singleChoiceRadioPost;
    @Initialize(R.id.tvTitle)
    TextView tvTitle;
    @Initialize(R.id.toolbarClose)
    ImageView toolbarClose;
    @Initialize(R.id.btnSubmit)
    Button btnSubmit;

    @Override
    protected int layoutRes() {
        return R.layout.activity_payment_type;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);
        tvTitle.setText(getResources().getText(R.string.payment_type_title));
        setDynamicValue();
        functionality();

    }

    private void functionality() {

    }

    private void setDynamicValue() {
        int imgIconWidth = (int) (screenWidth * 0.075);
        int imgIconHeight = (int) (screenWidth * 0.075);

        int topBottomSpace = (int) (screenHeight * 0.0089);


        RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) toolbarClose.getLayoutParams();
        imgLayParams.width = imgIconWidth;
        imgLayParams.height = imgIconHeight;
        toolbarClose.setLayoutParams(imgLayParams);

        ConstraintLayout.LayoutParams rlPaymentTypeMainLayoutParams = (ConstraintLayout.LayoutParams) rlPaymentTypeMain.getLayoutParams();
        rlPaymentTypeMainLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 9, topBottomSpace * 2, 0);
        rlPaymentTypeMain.setLayoutParams(rlPaymentTypeMainLayoutParams);

        ConstraintLayout.LayoutParams btnSubmitLayoutParams = (ConstraintLayout.LayoutParams) btnSubmit.getLayoutParams();
        btnSubmitLayoutParams.setMargins(topBottomSpace * 7, 0, topBottomSpace * 7, topBottomSpace * 10);
        btnSubmit.setLayoutParams(btnSubmitLayoutParams);

        RelativeLayout.LayoutParams singleChoiceRadioPreLayoutParams = (RelativeLayout.LayoutParams) singleChoiceRadioPre.getLayoutParams();
        singleChoiceRadioPreLayoutParams.setMargins(0, 0, topBottomSpace * 3, 0);
        singleChoiceRadioPre.setLayoutParams(singleChoiceRadioPreLayoutParams);

        RelativeLayout.LayoutParams singleChoiceRadioPostLayoutParams = (RelativeLayout.LayoutParams) singleChoiceRadioPost.getLayoutParams();
        singleChoiceRadioPostLayoutParams.setMargins(0, 0, topBottomSpace * 3, 0);
        singleChoiceRadioPost.setLayoutParams(singleChoiceRadioPostLayoutParams);


        RelativeLayout.LayoutParams tvPaymentPrePaidTextLayoutParams = (RelativeLayout.LayoutParams) tvPredesc.getLayoutParams();
        tvPaymentPrePaidTextLayoutParams.setMargins(0, 0, 0, topBottomSpace * 2);
        tvPredesc.setLayoutParams(tvPaymentPrePaidTextLayoutParams);

        RelativeLayout.LayoutParams tvPaymentPostPaidTextLayoutParams = (RelativeLayout.LayoutParams) tvPostdesc.getLayoutParams();
        tvPaymentPostPaidTextLayoutParams.setMargins(0, 0, 0, topBottomSpace * 2);
        tvPostdesc.setLayoutParams(tvPaymentPostPaidTextLayoutParams);

        RelativeLayout.LayoutParams rlPaymentPostTypeParams = (RelativeLayout.LayoutParams) rlPaymentPostType.getLayoutParams();
        rlPaymentPostTypeParams.setMargins(0, topBottomSpace * 6, 0, 0);
        rlPaymentPostType.setLayoutParams(rlPaymentPostTypeParams);

        tvPaymentPrePaidText.setPadding(topBottomSpace * 4, topBottomSpace, topBottomSpace * 2, 0);
        tvPredesc.setPadding(topBottomSpace * 4, topBottomSpace, topBottomSpace * 2, 0);
        singleChoiceRadioPre.setPadding(0, topBottomSpace, topBottomSpace * 2, 0);
        tvPaymentPostPaidText.setPadding(topBottomSpace * 4, topBottomSpace, topBottomSpace * 2, 0);
        tvPostdesc.setPadding(topBottomSpace * 4, topBottomSpace, topBottomSpace * 2, 0);
        singleChoiceRadioPost.setPadding(0, topBottomSpace, topBottomSpace * 2, 0);

    }

    @Onclick(R.id.singleChoiceRadioPost)
    public void singleChoiceRadioPost(View view) {
        if (singleChoiceRadioPre.isChecked()) {
            singleChoiceRadioPre.setChecked(false);
        }
    }

    @Onclick(R.id.singleChoiceRadioPre)
    public void singleChoiceRadioPre(View v) {
        if (singleChoiceRadioPost.isChecked()) {
            singleChoiceRadioPost.setChecked(false);
        }
    }

    @Onclick(R.id.btnSubmit)
    public void btnSubmit(View v) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
}
