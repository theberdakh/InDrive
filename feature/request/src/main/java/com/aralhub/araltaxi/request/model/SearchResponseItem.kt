package com.aralhub.araltaxi.request.model

import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.geometry.Point

data class SearchResponseItem(
    val point: Point,
    val geoObject: GeoObject?
)
