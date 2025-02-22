package com.aralhub.network.utils

import com.aralhub.network.api.UserNetworkApi
import javax.inject.Inject

class ClientTokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val api:  UserNetworkApi
) {
}