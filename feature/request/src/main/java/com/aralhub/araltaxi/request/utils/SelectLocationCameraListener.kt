package com.aralhub.araltaxi.request.utils

import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.yandex.runtime.image.ImageProvider

class SelectLocationCameraListener(private val imageProvider: ImageProvider) : CameraListener {

    private var onLocationChangedListener: ((Point) -> Unit) ={}
    fun setOnLocationChangedListener(listener: (Point) -> Unit) {
        onLocationChangedListener = listener
    }

    override fun onCameraPositionChanged(
        map: Map,
        cameraPosition: CameraPosition,
        cameraUpdateReason: CameraUpdateReason,
        p3: Boolean
    ) {
        onLocationChangedListener(cameraPosition.target)

        map.mapObjects.clear()
        map.mapObjects.addPlacemark().apply {
            geometry = cameraPosition.target
            setIcon(imageProvider)
        }
    }
}