package com.aralhub.network.models

data class ServerResponse<out T>(
    val success: Boolean,
    val message: String,
    val data: T
)

class ServerResponseEmpty(
    val success: Boolean,
    val message: String
)
