package com.aralhub.indrive.core.data.model.client

import com.aralhub.network.models.user.NetworkUserAuthRequest

data class ClientAddPhoneRequest(
    val phoneNumber: String
)

fun ClientAddPhoneRequest.toNetwork() = NetworkUserAuthRequest(this.phoneNumber)