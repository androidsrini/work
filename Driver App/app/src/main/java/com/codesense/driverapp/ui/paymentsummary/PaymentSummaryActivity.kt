package com.codesense.driverapp.ui.paymentsummary

import android.os.Bundle
import android.view.LayoutInflater
import com.codesense.driverapp.R
import com.codesense.driverapp.ui.drawer.DrawerActivity

class PaymentSummaryActivity : DrawerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_payment_summary)
        val view = LayoutInflater.from(this@PaymentSummaryActivity).inflate(R.layout.activity_payment_summary, null);
        frameLayout.addView(view)
    }
}
