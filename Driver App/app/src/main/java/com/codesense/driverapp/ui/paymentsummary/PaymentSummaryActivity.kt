package com.codesense.driverapp.ui.paymentsummary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.codesense.driverapp.R
import com.codesense.driverapp.ui.drawer.DrawerActivity
import com.codesense.driverapp.ui.review.SubmitReviewActivity

class PaymentSummaryActivity : DrawerActivity() {

    lateinit var collectPaymentButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_payment_summary)
        val view = LayoutInflater.from(this@PaymentSummaryActivity).inflate(R.layout.activity_payment_summary, null);
        val paymentTextView = view.findViewById<TextView>(R.id.paymentTextView);
        collectPaymentButton = view.findViewById(R.id.collectPaymentButton)
        frameLayout.addView(view)
        collectPaymentButton.setOnClickListener({
          SubmitReviewActivity.start(this@PaymentSummaryActivity)
        })
        paymentTextView.text = getString(R.string.total_fare_placeholder, 100)
    }

    companion object {
        @JvmStatic
        fun start(context: Context) = context.startActivity(Intent(context, PaymentSummaryActivity::class.java))
    }
}