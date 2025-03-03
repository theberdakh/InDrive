package com.aralhub.araltaxi.request.utils

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider
import java.io.Flushable

class CurrentLocationListener(private val context: Context, private val map: Map, private val onProviderDisabledListener: () -> Unit, private val onProviderEnabledListener: () -> Unit) :
    LocationListener {
    private var _placeMarkObject: PlacemarkMapObject? = null
    private val placeMarkObject = _placeMarkObject.takeIf { it != null }
    override fun onLocationChanged(location: Location) {
        setPlaceMarkToPosition(location)
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
        onProviderDisabledListener()
    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)
        onProviderEnabledListener()
    }

    private fun setPlaceMarkToPosition(location: Location) {
        val point = Point(location.latitude, location.longitude)
        val cameraPosition = CameraPosition(point, 17.0f, 150.0f, 30.0f)
        map.move(cameraPosition)
        val imageProvider = ImageProvider.fromResource(context, com.aralhub.ui.R.drawable.ic_vector)
        map.move(cameraPosition)
        if (placeMarkObject == null) {
            _placeMarkObject = map.mapObjects.addPlacemark().apply {
                geometry = point
                setIcon(imageProvider)
            }
        } else {
            placeMarkObject.geometry = point
        }
    }
}