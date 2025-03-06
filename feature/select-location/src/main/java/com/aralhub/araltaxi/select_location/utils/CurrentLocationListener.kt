package com.aralhub.araltaxi.select_location.utils

import android.location.Location
import android.location.LocationListener
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map

class CurrentLocationListener(
    private val map: Map,
    initialLocation: Location,
    private val onProviderDisabledListener: () -> Unit,
    private val onProviderEnabledListener: () -> Unit
) :
    LocationListener {
    init {
        val initialPoint = Point(initialLocation.latitude, initialLocation.longitude)
        updateMapPosition(initialPoint)
    }

    private var isUpdated = false
    override fun onLocationChanged(location: Location) {
        if (!isUpdated) {
            val point = Point(location.latitude, location.longitude)
            updateMapPosition(point)
            isUpdated = true
        }
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
        onProviderDisabledListener()
    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)
        onProviderEnabledListener()
    }

    private fun updateMapPosition(point: Point) {
        val cameraPosition = CameraPosition(point, 17.0f, 150.0f, 30.0f)
        if (map.isValid){
            map.move(cameraPosition)
        }

    }

}