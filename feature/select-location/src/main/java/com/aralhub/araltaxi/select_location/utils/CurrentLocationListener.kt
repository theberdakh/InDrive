package com.aralhub.araltaxi.select_location.utils

import android.location.Location
import android.location.LocationListener
import com.yandex.mapkit.geometry.Point

class CurrentLocationListener(
    private val onUpdateMapPosition: (listener: Point) -> Unit,
    private val onProviderDisabledListener: (initialPoint: Point) -> Unit,
    private val onProviderEnabledListener: (point: Point) -> Unit
) :
    LocationListener {

    private val initialLocation = Point(42.4651,59.6136)
    private val initialPoint = Point(initialLocation.latitude, initialLocation.longitude)

    init {
        onUpdateMapPosition(initialPoint)
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
        onProviderDisabledListener(initialPoint)
    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)
        onProviderEnabledListener(initialPoint)
        onUpdateMapPosition(initialPoint)
    }

    private fun updateMapPosition(point: Point) {
        onUpdateMapPosition(point)
    }

}