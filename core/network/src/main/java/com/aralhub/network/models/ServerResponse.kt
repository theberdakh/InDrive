package com.aralhub.network.models

import com.google.gson.annotations.SerializedName

data class ServerResponse<out T>(
    val success: Boolean,
    val message: ServerResponseMessage,
    val data: T
)

data class ServerResponseMessage(
    val ru: String,
    val en: String,
    val kk: String
)
class ServerResponseEmpty(
    val success: Boolean,
    val message: ServerResponseMessage
)

data class WebSocketServerResponse<out T>(
    val type: String,
    val data: T,
    @SerializedName("distance")
    val distanceToClient: DistanceToClient
)

data class DistanceToClient(
    val distance: Double,
    val duration: Double
)

data class ClientWebSocketServerResponse<T>(
    val type: String,
    val data: T
)

