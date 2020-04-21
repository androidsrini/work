package com.codesense.driverapp.ui.paymentsummary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.data.TripSummaryPassData;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.net.RequestHandler;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

public class PaymentSummaryNewActivity extends BaseActivity {


    @Inject
    RequestHandler requestHandler;
    /**
     * To create Utility object.
     */
    @Inject
    Utility utility;
    @Initialize(R.id.paymentTextView)
    TextView paymentTextView;
    @Initialize(R.id.tvTripStatus)
    TextView tvTripStatus;
    @Initialize(R.id.tvDate)
    TextView tvDate;
    @Initialize(R.id.pickupLocationAddressTextView)
    TextView pickupLocationAddressTextView;
    @Initialize(R.id.dropLocationAddressTextView)
    TextView dropLocationAddressTextView;
    @Initialize(R.id.distanceTravelledValueTextView)
    TextView distanceTravelledValueTextView;
    @Initialize(R.id.timeTakenValueTextView)
    TextView timeTakenValueTextView;
    @Initialize(R.id.baseTareValueTextView)
    TextView baseTareValueTextView;
    @Initialize(R.id.transWaitingValueTextView)
    TextView transWaitingValueTextView;
    @Initialize(R.id.taxValueTextView)
    TextView taxValueTextView;
    @Initialize(R.id.subTotalValueTextView)
    TextView subTotalValueTextView;
    @Initialize(R.id.discountValueTextView)
    TextView discountValueTextView;
    @Initialize(R.id.totalValueTextView)
    TextView totalValueTextView;
    @Initialize(R.id.paymentTypeValueTextView)
    TextView paymentTypeValueTextView;
    @Initialize(R.id.collectPaymentButton)
    Button collectPaymentButton;

    TripSummaryPassData tripSummaryPassData;

    @Override
    protected int layoutRes() {
        return R.layout.activity_payment_summary;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);


        Intent intent = getIntent();
        tripSummaryPassData = (TripSummaryPassData) intent.getSerializableExtra("tripSummary");

        if (tripSummaryPassData!=null) {
            paymentTextView.setText(getResources().getString(R.string.ruppes_symbol)+convertDecimal(tripSummaryPassData.getActual_fare()));
            tvTripStatus.setText(tripSummaryPassData.getBooking_no());
            tvDate.setText(convertDateFormat(tripSummaryPassData.getCompleted_on()));
            pickupLocationAddressTextView.setText(utility.getCompleteAddressString(this,Double.parseDouble(tripSummaryPassData.getPickup_lat()),Double.parseDouble(tripSummaryPassData.getPickup_lng())));
            dropLocationAddressTextView.setText(utility.getCompleteAddressString(this,Double.parseDouble(tripSummaryPassData.getEnd_lat()),Double.parseDouble(tripSummaryPassData.getEnd_lng())));
            distanceTravelledValueTextView.setText(tripSummaryPassData.getTotal_travelled_km()+"KM");
            timeTakenValueTextView.setText(tripSummaryPassData.getTime_taken());
            transWaitingValueTextView.setText(getResources().getString(R.string.ruppes_symbol)+convertDecimal("0.00"));
            taxValueTextView.setText(getResources().getString(R.string.ruppes_symbol)+convertDecimal("0.00"));
            discountValueTextView.setText(getResources().getString(R.string.ruppes_symbol)+convertDecimal("0.00"));
            subTotalValueTextView.setText(getResources().getString(R.string.ruppes_symbol)+convertDecimal(tripSummaryPassData.getActual_fare()));
            Double totalValue = Double.parseDouble(tripSummaryPassData.getActual_fare())+((double)tripSummaryPassData.getWaiting_fare());
            totalValueTextView.setText(getResources().getString(R.string.ruppes_symbol)+convertDecimal(String.valueOf(totalValue)));
            paymentTypeValueTextView.setText(tripSummaryPassData.getPayment_mode());
        }
    }


    public String convertDecimal(String value){
        double f = Double.parseDouble(value);

        @SuppressLint("DefaultLocale") String stringValue = String.format("%.2f", new BigDecimal(f));
        return stringValue;
    }
    public String convertDateFormat(String date){
        @SuppressLint("SimpleDateFormat") DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressLint("SimpleDateFormat") DateFormat targetFormat = new SimpleDateFormat("E ,dd MMM yyyy hh:mm a");
        Date dat= null;
        try {
            dat = originalFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(dat);
        return  formattedDate;
    }


    @Onclick(R.id.collectPaymentButton)
    public void  collectPaymentButton(View v){
        finish();
    }
}
