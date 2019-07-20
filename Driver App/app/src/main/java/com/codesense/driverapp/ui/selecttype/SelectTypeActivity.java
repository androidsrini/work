package com.codesense.driverapp.ui.selecttype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.ui.legalagreement.LegalAgreementActivity;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

public class SelectTypeActivity extends BaseActivity {

    @Initialize(R.id.tvTitle)
    TextView tvTitle;
    @Initialize(R.id.toolbarClose)
    ImageView toolbarClose;
    @Initialize(R.id.tvSelectTypeDesc)
    TextView tvSelectTypeDesc;
    @Initialize(R.id.rlDrive)
    RelativeLayout rlDrive;
    @Initialize(R.id.rlNonDrive)
    RelativeLayout rlNonDrive;
    @Initialize(R.id.tvDriverText)
    TextView tvDriverText;
    @Initialize(R.id.tvDriverdesc)
    TextView tvDriverdesc;
    @Initialize(R.id.tvNonDriverText)
    TextView tvNonDriverText;
    @Initialize(R.id.tvNonDriverdesc)
    TextView tvNonDriverdesc;


    @Override
    protected int layoutRes() {
        return R.layout.activity_select_type;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);
        setDynamicValue();
        tvTitle.setText(getResources().getText(R.string.select_type_title));

    }

    private void setDynamicValue() {
        int topBottomSpace = (int) (screenHeight * 0.0089);

        int imgIconWidth = (int) (screenWidth * 0.075);
        int imgIconHeight = (int) (screenWidth * 0.075);


        RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) toolbarClose.getLayoutParams();
        imgLayParams.width = imgIconWidth;
        imgLayParams.height = imgIconHeight;
        toolbarClose.setLayoutParams(imgLayParams);

        ConstraintLayout.LayoutParams tvPhoneNumberLayoutParams = (ConstraintLayout.LayoutParams) tvSelectTypeDesc.getLayoutParams();
        tvPhoneNumberLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace * 2, topBottomSpace * 2, 0);
        tvSelectTypeDesc.setLayoutParams(tvPhoneNumberLayoutParams);

        ConstraintLayout.LayoutParams rlDriveLayoutParams = (ConstraintLayout.LayoutParams) rlDrive.getLayoutParams();
        rlDriveLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 9, topBottomSpace * 2, 0);
        rlDrive.setLayoutParams(rlDriveLayoutParams);

        ConstraintLayout.LayoutParams rlNonDriveLayoutParams = (ConstraintLayout.LayoutParams) rlNonDrive.getLayoutParams();
        rlNonDriveLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 6, topBottomSpace * 2, 0);
        rlNonDrive.setLayoutParams(rlNonDriveLayoutParams);

        tvDriverText.setPadding(topBottomSpace * 4, topBottomSpace, topBottomSpace * 2, 0);
        tvNonDriverText.setPadding(topBottomSpace * 4, topBottomSpace, topBottomSpace * 2, 0);
        tvDriverdesc.setPadding(topBottomSpace * 4, topBottomSpace, topBottomSpace * 2, topBottomSpace * 4);
        tvNonDriverdesc.setPadding(topBottomSpace * 4, topBottomSpace, topBottomSpace * 2, topBottomSpace * 2);
    }

    @Onclick(R.id.toolbarClose)
    public void toolbarClose(View v) {
        finish();
    }

    @Onclick(R.id.rlDrive)
    public void rlDrive(View v) {
        Intent intent = new Intent(this, LegalAgreementActivity.class);
        startActivity(intent);
    }

    @Onclick(R.id.rlNonDrive)
    public void rlNonDrive(View v) {
        Intent intent = new Intent(this, LegalAgreementActivity.class);
        startActivity(intent);
    }
}
