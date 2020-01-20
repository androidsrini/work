package com.codesense.driverapp.di.module;


import com.codesense.driverapp.ui.accept.AcceptActivity;
import com.codesense.driverapp.ui.adddriver.AddDriverActivity;
import com.codesense.driverapp.ui.addvehicle.AddVehicleActivity;
import com.codesense.driverapp.ui.documentstatus.DocumentStatusActivity;
import com.codesense.driverapp.ui.driver.DriverListActivity;
import com.codesense.driverapp.ui.editmobilenumber.EditMobileNumberActivity;
import com.codesense.driverapp.ui.imagepicker.ImagePickerActivity;
import com.codesense.driverapp.ui.invitefriends.InviteFriendsActivity;
import com.codesense.driverapp.ui.launchscreen.LaunchScreenActivity;
import com.codesense.driverapp.ui.legalagreement.LegalAgreementActivity;
import com.codesense.driverapp.ui.online.OnlineActivity;
import com.codesense.driverapp.ui.paymentType.PaymentActivity;
import com.codesense.driverapp.ui.paymentsummary.PaymentSummaryActivity;
import com.codesense.driverapp.ui.pickuplocationaccept.PickUpLocationAcceptActivity;
import com.codesense.driverapp.ui.referalprogram.ReferalProgramActivity;
import com.codesense.driverapp.ui.register.RegisterActivity;
import com.codesense.driverapp.ui.review.SubmitReviewActivity;
import com.codesense.driverapp.ui.selecttype.SelectTypeActivity;
import com.codesense.driverapp.ui.setting.SettingActivity;
import com.codesense.driverapp.ui.signin.LoginActivity;
import com.codesense.driverapp.ui.splash.SplashActivity;
import com.codesense.driverapp.ui.uploadDriver.UploadDocumentDriverActivity;
import com.codesense.driverapp.ui.uploaddocument.UploadDocumentActivity;
import com.codesense.driverapp.ui.uploaddocument.UploadDocumentSecondActivity;
import com.codesense.driverapp.ui.vehicle.VehicleListActivity;
import com.codesense.driverapp.ui.verifymobile.VerifyMobileActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract LaunchScreenActivity bindLaunchScreenActivity();

    @ContributesAndroidInjector
    abstract RegisterActivity bindRegisterActivity();

    @ContributesAndroidInjector
    abstract VerifyMobileActivity bindVerifyMobileActivity();

    @ContributesAndroidInjector
    abstract SelectTypeActivity bindSelectTypeActivity();

    @ContributesAndroidInjector
    abstract LegalAgreementActivity bindLegalAgreementActivity();

    @ContributesAndroidInjector
    abstract UploadDocumentActivity bindUploadDocumentActivity();

    @ContributesAndroidInjector
    abstract UploadDocumentSecondActivity bindUploadDocumentSecondActivity();

    @ContributesAndroidInjector
    abstract DocumentStatusActivity bindDocumentStatusActivity();

    @ContributesAndroidInjector
    abstract VehicleListActivity bindVehicleListActivity();

    @ContributesAndroidInjector
    abstract AddVehicleActivity bindAddVehicleActivity();

    @ContributesAndroidInjector
    abstract OnlineActivity bindOnlineActivity();

    @ContributesAndroidInjector
    abstract PickUpLocationAcceptActivity bindPickUpLocationAcceptActivity();

    @ContributesAndroidInjector
    abstract PaymentActivity bindPaymentActivity();

    @ContributesAndroidInjector
    abstract SettingActivity bindSettingActivity();

    @ContributesAndroidInjector
    abstract InviteFriendsActivity bindInviteFriendsActivity();

    @ContributesAndroidInjector
    abstract ReferalProgramActivity bindReferalProgramActivity();

    @ContributesAndroidInjector
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector
    abstract SplashActivity bindSplashActivity();

    @ContributesAndroidInjector
    abstract EditMobileNumberActivity bindEditMobileNumberActivity();

    @ContributesAndroidInjector
    abstract ImagePickerActivity bindImagePickerActivity();

    @ContributesAndroidInjector
    abstract AcceptActivity bindAcceptActivity();

    @ContributesAndroidInjector
    abstract PaymentSummaryActivity bindPaymentSummaryActivity();

    @ContributesAndroidInjector
    abstract SubmitReviewActivity bindSubmitReviewActivity();

    @ContributesAndroidInjector
    abstract AddDriverActivity bindAddDriverActivity();

    @ContributesAndroidInjector
    abstract DriverListActivity bindDriverListActivity();

    @ContributesAndroidInjector
    abstract UploadDocumentDriverActivity bindUploadDocumentDriverActivity();

    /*@ContributesAndroidInjector
    abstract AddDriverActivity.AddDriverFragment bindAddDriverFragment();*/
}
