package com.aralhub.network.models.distance

import com.aralhub.network.models.location.NetworkLocationPoint
import com.google.gson.annotations.SerializedName

data class NetworkDistanceSegment(
    val distance: Number,
    val duration: Number,
    @SerializedName("start_point")
    val startPoint: NetworkLocationPoint,
    @SerializedName("end_point")
    val endPoint: NetworkLocationPoint
)