package com.aralhub.indrive.core.data.model.client

import com.aralhub.network.models.user.NetworkAuthResponseData

data class AuthResponse(
    val success: Boolean,
    val message: String,
)

fun NetworkAuthResponseData.asDomain() = AuthResponse(this.success, this.message)
