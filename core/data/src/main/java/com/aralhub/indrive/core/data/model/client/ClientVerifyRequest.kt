package com.aralhub.indrive.core.data.model.client

import com.aralhub.network.models.user.NetworkUserVerifyRequest

data class ClientVerifyRequest(
    val phoneNumber: String,
    val code: String
)

fun ClientVerifyRequest.toDTO() = NetworkUserVerifyRequest(this.phoneNumber, this.code)
