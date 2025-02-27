package com.aralhub.network.models.distance

import com.google.gson.annotations.SerializedName

data class NetworkDistance(
    val segments: List<NetworkDistanceSegment>,
    @SerializedName("total_distance")
    val totalDistance: Number,
    @SerializedName("total_duration")
    val totalDuration: Number
)







