package com.example.transportproject

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.random.Random

class Manage_Shipment : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val LOCATION_PERMISSION_REQUEST = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(applicationContext, getSharedPreferences("osm", MODE_PRIVATE))
        setContentView(R.layout.activity_manage_shipment)

        mapView = findViewById(R.id.mapView)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(15.0)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermissionAndFetch()
    }

    private fun checkLocationPermissionAndFetch() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
        } else {
            fetchUserLocation()
        }
    }

    private fun fetchUserLocation() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val userGeoPoint = GeoPoint(it.latitude, it.longitude)
                    showUserLocation(userGeoPoint)
                    showNearbyProviders(userGeoPoint)
                } ?: run {
                    showToast("Unable to get current location. Ensure GPS is enabled.")
                }
            }
        } catch (e: SecurityException) {
            showToast("Permission error: ${e.message}")
        }
    }

    private fun showUserLocation(userGeoPoint: GeoPoint) {
        val locationOverlay = MyLocationNewOverlay(mapView)
        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()
        mapView.overlayManager.add(locationOverlay)

        mapView.controller.setCenter(userGeoPoint)
    }

    private fun showNearbyProviders(userGeoPoint: GeoPoint) {
        val providers = List(5) {
            GeoPoint(
                userGeoPoint.latitude + Random.nextDouble(-0.05, 0.05),
                userGeoPoint.longitude + Random.nextDouble(-0.05, 0.05)
            )
        }

        for (provider in providers) {
            val marker = Marker(mapView)
            marker.position = provider
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = "Logistics Provider"
            mapView.overlayManager.add(marker)
        }
        mapView.invalidate()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchUserLocation()
            } else {
                showToast("Location permission denied. Unable to fetch logistics providers.")
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
