package com.codesense.driverapp.ui.receipt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.codesense.driverapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_receipt.*

class ReceiptActivity : AppCompatActivity(), OnMapReadyCallback {

    val position = LatLng(-33.920455, 18.466941)
    lateinit var handler: Handler

    companion object {
        @JvmStatic
        fun start(context: Context) = context.startActivity(Intent(context, ReceiptActivity::class.java))
    }

    override fun onMapReady(p0: GoogleMap?) {

    }

    private fun setMapLocation(map : GoogleMap) {
        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
            addMarker(MarkerOptions().position(position))
            mapType = GoogleMap.MAP_TYPE_NORMAL
            setOnMapClickListener {
                Toast.makeText(this@ReceiptActivity, "Clicked on map", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)
        setSupportActionBar(toolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = getString(R.string.receipt_label)
        with(receiptMapView) {
            receiptMapView.onCreate(savedInstanceState)
            receiptMapView.getMapAsync{
                //onCreate(null)
                //MapsInitializer.initialize(applicationContext)
                setMapLocation(it)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        receiptMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        receiptMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        receiptMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        receiptMapView.onLowMemory()
    }
}
