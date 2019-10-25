package com.codesense.driverapp.ui.review

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.codesense.driverapp.R
import com.codesense.driverapp.ui.history.RideHistoryActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_submit_review.*

class SubmitReviewActivity : AppCompatActivity(), OnMapReadyCallback {

    val position = LatLng(-33.920455, 18.466941)
    lateinit var handler: Handler

    companion object {
        @JvmStatic
        fun start(context: Context) = context.startActivity(Intent(context, SubmitReviewActivity::class.java))
    }

    override fun onMapReady(p0: GoogleMap?) {

    }

    private fun setMapLocation(map : GoogleMap) {
        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
            addMarker(MarkerOptions().position(position))
            mapType = GoogleMap.MAP_TYPE_NORMAL
            setOnMapClickListener {
                Toast.makeText(this@SubmitReviewActivity, "Clicked on map", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSuccessTrip() {
        val dialogBuilder = AlertDialog.Builder(this)

        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.layout_dialog_pay_suc, null)
        dialogBuilder.setView(dialogView)

        val anim = AnimationUtils.loadAnimation(applicationContext,
                R.anim.scale_up)
        anim.interpolator = OvershootInterpolator()

        val imageView = dialogView.findViewById<ImageView>(R.id.tripSucIconImageView)
        val button_close = dialogView.findViewById<Button>(R.id.button_close)
        imageView.startAnimation(anim)

        val alertDialog = dialogBuilder.create()
        alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        alertDialog.show()

        button_close.setOnClickListener {
            alertDialog.dismiss()
           RideHistoryActivity.start(this@SubmitReviewActivity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_review)
        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        //toolbar.navigationIcon = ContextCompat.getDrawable(this@SubmitReviewActivity, R.drawable.left_arrow)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.rating_and_review_label)
        actionBar.setDisplayHomeAsUpEnabled(true);
        handler = Handler()
        with(submitMapView) {
            submitMapView.onCreate(savedInstanceState)
            submitMapView.getMapAsync{
                setMapLocation(it)
            }
        }
        submitButton.setOnClickListener{ showSuccessTrip() }
        handler.postDelayed(Runnable {
            showSuccessTrip()
        }, 2000)
        //
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        submitMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        submitMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        submitMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        submitMapView.onLowMemory()
    }
}
