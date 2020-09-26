package com.example.mappicker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.lay_ok.*


class MainActivity : AppCompatActivity() {

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val locationRequestCode = 1000
    lateinit var oldPosition : LatLng
    val defaultLatLng = LatLng(-7.983908, 112.621391)

    var newLat = -7.983908
    var newLong = 112.621391
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        btn_ok.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                setResult(
                    RESULT_OK,
                    Intent().putExtra("latitude", newLat).putExtra("longitude", newLong)
                )
                finish()
            }

        })
        initmap()
    }

    fun initmap(){
        (map_view as SupportMapFragment).getMapAsync { map ->
            /*map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 18f))

            oldPosition = map.cameraPosition.target
*/
            // check permission

            // check permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    locationRequestCode
                )
            } else {
                // already permission granted
                // get location here
                mFusedLocationClient!!.getLastLocation().addOnSuccessListener(this) { location ->
                    if (location != null) {
                        /*wayLatitude = location.getLatitude()
                        wayLongitude = location.getLongitude()
                        txtLocation.setText(String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude))*/
                        oldPosition = LatLng(location.getLatitude(), location.getLongitude())
                        newLat = location.getLatitude()
                        newLong = location.getLongitude()
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(oldPosition, 18f))
                    }
                }
            }

            btn_loc_now.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(oldPosition, 18f))
                }

            })

            map.setOnCameraMoveStartedListener {
                // drag started

                // start animation
                icon_marker.animate().translationY(-50f).start()
                icon_marker_shadow.animate().withStartAction {
                    icon_marker_shadow.setPadding(10, 10, 10, 10)
                }.start()
            }

            map.setOnCameraIdleListener {
                val newPosition = map.cameraPosition.target
                if (oldPosition!=null) {
                    if (newPosition != oldPosition) {
                        // drag ended

                        // start animation
                        icon_marker.animate().translationY(0f).start()
                        icon_marker_shadow.animate().withStartAction {
                            icon_marker_shadow.setPadding(0, 0, 0, 0)
                        }.start()
                        val lat = newPosition.latitude;
                        val lon = newPosition.longitude;
                        Log.e("latlng", "" + lat + ";" + lon);
                        newLat = lat
                        newLong = lon
                    }
                }
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<kotlin.String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            locationRequestCode -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    initmap()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
