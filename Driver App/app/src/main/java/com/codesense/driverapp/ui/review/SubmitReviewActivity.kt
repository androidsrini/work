package com.codesense.driverapp.ui.review

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.codesense.driverapp.R
import com.codesense.driverapp.ui.drawer.DrawerActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_submit_review.*

class SubmitReviewActivity : DrawerActivity(), OnMapReadyCallback {

    val position = LatLng(-33.920455, 18.466941)

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
            //supportFragmentManager.fragments.clear()
            /*val intent = Intent(this@CheckOut, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)*/
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_review)
        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.rating_and_review_label)
        //actionBar.setHomeAsUpIndicator(android.R.drawable.)
        actionBar.setDisplayHomeAsUpEnabled(true);
        with(submitMapView) {
            submitMapView.onCreate(savedInstanceState)
            submitMapView.getMapAsync{
                onCreate(null)
                MapsInitializer.initialize(applicationContext)
                setMapLocation(it)
            }
        }
        submitButton.setOnClickListener{ showSuccessTrip() }
        //showSuccessTrip()
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
