package com.codesense.driverapp.ui.accept

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import com.codesense.driverapp.R
import com.codesense.driverapp.ui.drawer.DrawerActivity
import com.codesense.driverapp.ui.paymentsummary.PaymentSummaryActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_accept.*



class AcceptActivity : DrawerActivity(), OnMapReadyCallback, View.OnTouchListener, View.OnDragListener {

    val position = LatLng(-33.920455, 18.466941)
    lateinit var handler: Handler;

    override fun onMapReady(p0: GoogleMap?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setMapLocation(map : GoogleMap) {
        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
            addMarker(MarkerOptions().position(position))
            mapType = GoogleMap.MAP_TYPE_NORMAL
            setOnMapClickListener {
                Toast.makeText(this@AcceptActivity, "Clicked on map", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEndTripUI() {
        handler.postDelayed(Runnable {
            endTripConstrainRelativeLayout.visibility = View.VISIBLE
            startTripConstrainLinearLayout.visibility = View.GONE
        }, 2000)
    }

    override fun onDrag(v: View, event: DragEvent): Boolean {
        // TODO Auto-generated method stub
        if (event.action == DragEvent.ACTION_DROP) {
            //we want to make sure it is dropped only to left and right parent view
            val view = event.localState as View
            if (v.id == R.id.leftSideLinearLayout
                    || v.id == R.id.rightSideLinearLayout) {
                val source = view.parent as ViewGroup
                source.removeView(view)

                val target = v as LinearLayout
                target.addView(view)
            }
            //make view visible as we set visibility to invisible while starting drag
            view.visibility = View.VISIBLE
        }
        return true
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        // TODO Auto-generated method stub
        if (event.action == MotionEvent.ACTION_DOWN) {
            val shadowBuilder = View.DragShadowBuilder(view)
            view.startDrag(null, shadowBuilder, view, 0)
            view.visibility = View.VISIBLE
            return true
        }
        return false
    }

    companion object {
        val TAG = AcceptActivity::class.java.name
        @JvmStatic
        fun start(context: Context) = context.startActivity(Intent(context, AcceptActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LayoutInflater.from(this).inflate(R.layout.activity_accept, null)
        frameLayout.addView(view)
        titleTextView.text = getResources().getString(R.string.online_text)
        handler = Handler()
        with(acceptMapView) {
            acceptMapView.onCreate(savedInstanceState)
            acceptMapView.getMapAsync{
                setMapLocation(it)
            }
        }
        endTripImageView.setOnTouchListener(this)
        leftSideLinearLayout.setOnDragListener(this)
        rightSideLinearLayout.setOnDragListener(this)
        endTripConstrainRelativeLayout.setOnClickListener({
            PaymentSummaryActivity.start(this@AcceptActivity)
        })
        showEndTripUI()
        //setContentView(R.layout.activity_accept)
    }

    override fun onResume() {
        super.onResume()
        //Log.d(TAG, "On Resume acceptMapView: " + acceptMapView)
        acceptMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        acceptMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        acceptMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        acceptMapView.onLowMemory()
    }
}
