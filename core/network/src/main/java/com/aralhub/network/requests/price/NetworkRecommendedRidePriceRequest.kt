package com.aralhub.network.requests.price

import com.aralhub.network.models.location.NetworkLocationPoint

data class NetworkRecommendedRidePriceRequest(
    val points: List<NetworkLocationPoint>
)


