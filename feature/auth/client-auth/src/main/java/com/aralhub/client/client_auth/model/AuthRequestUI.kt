package com.aralhub.client.client_auth.model

import com.aralhub.indrive.core.data.model.client.AuthRequest

data class AuthRequestUI(
    val phoneNumber: String,
)

fun AuthRequestUI.toDomain() = AuthRequest(this.phoneNumber)
