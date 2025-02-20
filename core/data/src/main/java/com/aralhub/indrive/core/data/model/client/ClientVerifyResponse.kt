package com.aralhub.indrive.core.data.model.client

import com.aralhub.network.models.user.NetworkUserVerifyResponse

data class ClientVerifyResponse(
    val token: String,
    val refreshToken: String,
)

fun NetworkUserVerifyResponse.asDomain() = ClientVerifyResponse(this.token, this.refreshToken)
