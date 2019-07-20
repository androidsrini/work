package com.codesense.driverapp.ui.invitefriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

public class InviteFriendsActivity extends BaseActivity {

    @Initialize(R.id.imgNotification)
    ImageView imgNotification;
    @Initialize(R.id.tvInviteText)
    TextView tvInviteText;
    @Initialize(R.id.tvInviteValueCode)
    TextView tvInviteValueCode;
    @Initialize(R.id.tvInvitedesc)
    TextView tvInvitedesc;
    @Initialize(R.id.btnInviteCode)
    Button btnInviteCode;
    @Initialize(R.id.tvTitle)
    TextView tvTitle;
    @Initialize(R.id.toolbarClose)
    ImageView toolbarClose;

    @Override
    protected int layoutRes() {
        return R.layout.activity_invite_friends;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);
        tvTitle.setText(getResources().getText(R.string.invite_friends_title));
        setDynamicValue();
    }

    private void setDynamicValue() {
        int imgIconWidth = (int) (screenWidth * 0.075);
        int imgIconHeight = (int) (screenWidth * 0.075);

        int topBottomSpace = (int) (screenHeight * 0.0089);


        RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) toolbarClose.getLayoutParams();
        imgLayParams.width = imgIconWidth;
        imgLayParams.height = imgIconHeight;
        toolbarClose.setLayoutParams(imgLayParams);

        ConstraintLayout.LayoutParams imgNotificationLayParams = (ConstraintLayout.LayoutParams) imgNotification.getLayoutParams();
        imgNotificationLayParams.width = imgIconWidth * 5;
        imgNotificationLayParams.height = imgIconHeight * 5;
        imgNotification.setLayoutParams(imgNotificationLayParams);


        ConstraintLayout.LayoutParams btnSubmitLayoutParams = (ConstraintLayout.LayoutParams) btnInviteCode.getLayoutParams();
        btnSubmitLayoutParams.setMargins(topBottomSpace * 2, 0, topBottomSpace * 2, topBottomSpace * 4);
        btnInviteCode.setLayoutParams(btnSubmitLayoutParams);

        ConstraintLayout.LayoutParams tvInviteTextLayoutParams = (ConstraintLayout.LayoutParams) tvInviteText.getLayoutParams();
        tvInviteTextLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 4, topBottomSpace * 2, 0);
        tvInviteText.setLayoutParams(tvInviteTextLayoutParams);

        ConstraintLayout.LayoutParams tvInviteValueCodeTextLayoutParams = (ConstraintLayout.LayoutParams) tvInviteValueCode.getLayoutParams();
        tvInviteValueCodeTextLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 2, topBottomSpace * 2, 0);
        tvInviteValueCode.setLayoutParams(tvInviteValueCodeTextLayoutParams);

        ConstraintLayout.LayoutParams tvInvitedescLayoutParams = (ConstraintLayout.LayoutParams) tvInvitedesc.getLayoutParams();
        tvInvitedescLayoutParams.setMargins(topBottomSpace * 4, topBottomSpace * 2, topBottomSpace * 2, 0);
        tvInvitedesc.setLayoutParams(tvInvitedescLayoutParams);

    }

    @Onclick(R.id.btnInviteCode)
    public void btnInviteCode(View v) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }
}
