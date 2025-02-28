package com.aralhub.network.models

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
    val data: T
)

