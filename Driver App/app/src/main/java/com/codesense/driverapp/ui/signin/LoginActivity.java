package com.codesense.driverapp.ui.signin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

public class LoginActivity  extends BaseActivity {

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
        etEmailLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace*2, topBottomSpace * 3, 0);
        etEmail.setLayoutParams(etEmailLayoutParams);

        ConstraintLayout.LayoutParams etPasswordLayoutParams = (ConstraintLayout.LayoutParams) etPassword.getLayoutParams();
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

    @Onclick(R.id.toolbarClose)
    public void toolbarClose(View v) {
        finish();
    }
}
