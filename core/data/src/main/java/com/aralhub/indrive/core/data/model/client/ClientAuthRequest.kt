package com.aralhub.indrive.core.data.model.client

import com.aralhub.network.models.user.NetworkUserAuthRequest

data class AuthRequest(
    val phoneNumber: String,
)

fun AuthRequest.toNetwork() = NetworkUserAuthRequest(this.phoneNumber)