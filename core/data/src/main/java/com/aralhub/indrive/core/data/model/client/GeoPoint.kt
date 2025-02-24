package com.aralhub.indrive.core.data.model.client

import com.aralhub.network.models.websocketclient.NetworkGetRecommendedRidePricePoint
import com.aralhub.network.models.websocketclient.NetworkGetRecommendedRidePricePointCoordinates

data class GeoPoint(
    val longitude: Number,
    val latitude: Number,
    val name: String = ""
)

fun GeoPoint.toNetwork(): NetworkGetRecommendedRidePricePoint {
    return NetworkGetRecommendedRidePricePoint(
        coordinates = NetworkGetRecommendedRidePricePointCoordinates(
            longitude = longitude.toDouble(),
            latitude = latitude.toDouble()
        ),
        name = ""
    )
}
