package com.aralhub.network.utils

import com.aralhub.network.local.LocalStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val localStorage: LocalStorage
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        localStorage.access.let { token ->
            request.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(request.build())
    }
}



