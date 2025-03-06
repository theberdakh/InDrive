package com.aralhub.araltaxi.create_order.utils

import android.content.Context
import android.location.Location
import android.location.LocationListener
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider

class CurrentLocationListener(
    private val context: Context,
    private val map: Map,
    initialLocation: Location,
    private val onProviderDisabledListener: () -> Unit,
    private val onProviderEnabledListener: () -> Unit
) :
    LocationListener {
    private var placeMarkObject: PlacemarkMapObject

    private val imageProvider =
        ImageProvider.fromResource(context, com.aralhub.ui.R.drawable.ic_vector)

    init {
        val initialPoint = Point(initialLocation.latitude, initialLocation.longitude)
        placeMarkObject = map.mapObjects.addPlacemark().apply {
            geometry = initialPoint
            setIcon(imageProvider)
        }
        updateMapPosition(initialPoint)
    }

    override fun onLocationChanged(location: Location) {
        val point = Point(location.latitude, location.longitude)
        placeMarkObject.geometry = point
        updateMapPosition(point)
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
        map.move(cameraPosition)
    }

}