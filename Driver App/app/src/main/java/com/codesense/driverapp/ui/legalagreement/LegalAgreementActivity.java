package com.codesense.driverapp.ui.legalagreement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.ui.uploaddocument.UploadDocumentActivity;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

public class LegalAgreementActivity extends BaseActivity {


    @Initialize(R.id.tvTitle)
    TextView tvTitle;
    @Initialize(R.id.tvLegalText)
    TextView tvLegalText;
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


    @Override
    protected int layoutRes() {
        return R.layout.activity_legal_agreement;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);
        tvTitle.setText(getResources().getString(R.string.legal_title));
        setDynamicValue();
    }


    private void setDynamicValue() {
        int topBottomSpace = (int) (screenHeight * 0.0089);

        int imgIconWidth = (int) (screenWidth * 0.075);
        int imgIconHeight = (int) (screenWidth * 0.075);


        RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) toolbarClose.getLayoutParams();
        imgLayParams.width = imgIconWidth;
        imgLayParams.height = imgIconHeight;
        toolbarClose.setLayoutParams(imgLayParams);

        ConstraintLayout.LayoutParams tvLegalTextLayoutParams = (ConstraintLayout.LayoutParams) tvLegalText.getLayoutParams();
        tvLegalTextLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 3, 0, 0);
        tvLegalText.setLayoutParams(tvLegalTextLayoutParams);
        tvLegalText.setPadding(0, 0, topBottomSpace * 2, 0);

        ConstraintLayout.LayoutParams checkboxLayoutParams = (ConstraintLayout.LayoutParams) checkbox.getLayoutParams();
        checkboxLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 3, 0, 0);
        checkbox.setLayoutParams(checkboxLayoutParams);
        checkbox.setPadding(topBottomSpace * 2, 0, topBottomSpace * 2, 0);

        ConstraintLayout.LayoutParams viewLayoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        viewLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 3, topBottomSpace * 2, 0);
        view.setLayoutParams(viewLayoutParams);

        RelativeLayout.LayoutParams btnAcceptContinueLayoutParams = (RelativeLayout.LayoutParams) btnAcceptContinue.getLayoutParams();
        btnAcceptContinueLayoutParams.setMargins(topBottomSpace * 2, 0, topBottomSpace * 2, topBottomSpace * 2);
        btnAcceptContinue.setLayoutParams(btnAcceptContinueLayoutParams);

    }

    @Onclick(R.id.toolbarClose)
    public void toolbarClose(View v) {
        finish();
    }

    @Onclick(R.id.checkbox)
    public void checkbox(View v) {
        if (checkbox.isChecked()) {
            btnAcceptContinue.setBackgroundColor(getResources().getColor(R.color.primary_color));
            btnAcceptContinue.setTextColor(getResources().getColor(R.color.secondary_color));
        } else {
            btnAcceptContinue.setBackgroundColor(getResources().getColor(R.color.low_contrast));
            btnAcceptContinue.setTextColor(getResources().getColor(R.color.background_color));
        }
    }

    @Onclick(R.id.btnAcceptContinue)
    public void btnAcceptContinue(View v) {
        if (checkbox.isChecked()) {
            Intent intent = new Intent(this, UploadDocumentActivity.class);
            startActivity(intent);
            ActivityCompat.finishAffinity(this);
        }
    }
}
