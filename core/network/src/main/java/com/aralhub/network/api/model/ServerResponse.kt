package com.aralhub.network.api.model

data class ServerResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T
)
