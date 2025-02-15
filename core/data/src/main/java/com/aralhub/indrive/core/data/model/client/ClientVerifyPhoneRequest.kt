package com.aralhub.indrive.core.data.model.client

import com.aralhub.network.models.user.NetworkUserVerifyRequest

data class ClientVerifyPhoneRequest(
    val phone: String,
    val code: String
)

fun ClientVerifyPhoneRequest.toNetwork() = NetworkUserVerifyRequest(
    phoneNumber = this.phone,
    code = this.code
)