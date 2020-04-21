package com.codesense.driverapp.ui.referalprogram;

import android.annotation.SuppressLint;
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
import com.codesense.driverapp.ui.invitefriends.InviteFriendsActivity;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

@SuppressLint("Registered")
public class ReferalProgramActivity extends BaseActivity {

    @Initialize(R.id.rlReferalMain)
    RelativeLayout rlReferalMain;
    @Initialize(R.id.rlInivite)
    RelativeLayout rlInivite;
    @Initialize(R.id.imgInviteFirends)
    ImageView imgInviteFirends;
    @Initialize(R.id.tvInviteFriendsText)
    TextView tvInviteFriendsText;
    @Initialize(R.id.singleChoiceRadioPre)
    RadioButton singleChoiceRadioPre;
    @Initialize(R.id.rlReferalEarning)
    RelativeLayout rlReferalEarning;
    @Initialize(R.id.imgReferalEarning)
    ImageView imgReferalEarning;
    @Initialize(R.id.tvReferalEarning)
    TextView tvReferalEarning;
    @Initialize(R.id.singleChoiceRadioPost)
    RadioButton singleChoiceRadioPost;
    @Initialize(R.id.toolbarClose)
    ImageView toolbarClose;
    @Initialize(R.id.tvTitle)
    TextView tvTitle;
    @Initialize(R.id.btnSubmit)
    Button btnSubmit;

    @Override
    protected int layoutRes() {
        return R.layout.activity_referal_program;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);
        setDynamicValue();
        tvTitle.setText(getResources().getString(R.string.referal_earn_title));

    }

    private void setDynamicValue() {
        int imgIconWidth = (int) (screenWidth * 0.075);
        int imgIconHeight = (int) (screenWidth * 0.075);

        int topBottomSpace = (int) (screenHeight * 0.0089);

        int imgIconReferalWidth = (int) (screenWidth * 0.185);
        int imgIconReferalHeight = (int) (screenWidth * 0.185);
        

        RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) toolbarClose.getLayoutParams();
        imgLayParams.width = imgIconWidth;
        imgLayParams.height = imgIconHeight;
        toolbarClose.setLayoutParams(imgLayParams);

        ConstraintLayout.LayoutParams rlPaymentTypeMainLayoutParams = (ConstraintLayout.LayoutParams) rlReferalMain.getLayoutParams();
        rlPaymentTypeMainLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 9, topBottomSpace * 2, 0);
        rlReferalMain.setLayoutParams(rlPaymentTypeMainLayoutParams);


        RelativeLayout.LayoutParams singleChoiceRadioPreLayoutParams = (RelativeLayout.LayoutParams) singleChoiceRadioPre.getLayoutParams();
        singleChoiceRadioPreLayoutParams.setMargins(0, 0, topBottomSpace * 3, 0);
        singleChoiceRadioPre.setLayoutParams(singleChoiceRadioPreLayoutParams);

        RelativeLayout.LayoutParams singleChoiceRadioPostLayoutParams = (RelativeLayout.LayoutParams) singleChoiceRadioPost.getLayoutParams();
        singleChoiceRadioPostLayoutParams.setMargins(0, 0, topBottomSpace * 3, 0);
        singleChoiceRadioPost.setLayoutParams(singleChoiceRadioPostLayoutParams);

        RelativeLayout.LayoutParams imgInviteFirendsLayParams = (RelativeLayout.LayoutParams) imgInviteFirends.getLayoutParams();
        imgInviteFirendsLayParams.width = imgIconReferalWidth;
        imgInviteFirendsLayParams.height = imgIconReferalHeight;
        imgInviteFirends.setLayoutParams(imgInviteFirendsLayParams);

        RelativeLayout.LayoutParams imgReferalEarningLayParams = (RelativeLayout.LayoutParams) imgReferalEarning.getLayoutParams();
        imgReferalEarningLayParams.width = imgIconReferalWidth;
        imgReferalEarningLayParams.height = imgIconReferalHeight;
        imgReferalEarning.setLayoutParams(imgReferalEarningLayParams);

        tvInviteFriendsText.setPadding(0, topBottomSpace * 2, 0, topBottomSpace * 4);
        tvReferalEarning.setPadding(0, topBottomSpace * 2, 0, topBottomSpace * 4);
        imgInviteFirends.setPadding(topBottomSpace * 3, 0, 0, 0);
        imgReferalEarning.setPadding(topBottomSpace * 3, 0, 0, 0);


        ConstraintLayout.LayoutParams btnSubmitLayoutParams = (ConstraintLayout.LayoutParams) btnSubmit.getLayoutParams();
        btnSubmitLayoutParams.setMargins(topBottomSpace * 7, 0, topBottomSpace * 7, topBottomSpace * 10);
        btnSubmit.setLayoutParams(btnSubmitLayoutParams);


        RelativeLayout.LayoutParams rlPaymentPostTypeParams = (RelativeLayout.LayoutParams) rlReferalEarning.getLayoutParams();
        rlPaymentPostTypeParams.setMargins(0, topBottomSpace * 6, 0, 0);
        rlReferalEarning.setLayoutParams(rlPaymentPostTypeParams);

    }

    @Onclick(R.id.btnSubmit)
    public  void btnSubmit(View v){
        Intent intent = new Intent(this, InviteFriendsActivity.class);
        startActivity(intent);
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
}
