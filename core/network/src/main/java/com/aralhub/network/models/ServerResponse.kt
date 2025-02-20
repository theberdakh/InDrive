package com.aralhub.network.models

data class ServerResponse<T>(
    val success: Boolean,
    val message: Any,
    val data: T
)
