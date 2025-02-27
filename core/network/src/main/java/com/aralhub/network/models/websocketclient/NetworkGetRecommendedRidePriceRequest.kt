package com.aralhub.network.models.websocketclient

import com.google.gson.annotations.SerializedName

data class NetworkGetRecommendedRidePriceRequest(
    val points: List<NetworkGetRecommendedRidePricePoint>
)

data class NetworkGetRecommendedRidePricePoint(
    val coordinates: NetworkGetRecommendedRidePricePointCoordinates,
    val name: String
)

data class NetworkGetRecommendedRidePricePointCoordinates (
    val longitude: Number,
    val latitude: Number
)

data class NetworkGetRecommendedRidePriceResponse (
    @SerializedName("min_amount")
    val minAmount: Number,
    @SerializedName("max_amount")
    val maxAmount: Number,
    @SerializedName("recommended_amount")
    val recommendedAmount: Number
)
