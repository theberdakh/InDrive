package com.aralhub.network.utils

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import java.net.UnknownHostException
import javax.inject.Inject

class NetworkErrorInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            return chain.proceed(request)
        } catch (e: UnknownHostException) {
            val errorMessage = "No internet connection available"
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(503) // Service Unavailable or custom code
                .message(errorMessage)
                .body(ResponseBody.create(null, errorMessage))
                .build()
        } catch (e: Exception) {
            val errorMessage = "An unexpected error occurred: ${e.message}"
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(500) // Generic error code
                .message(errorMessage)
                .body(ResponseBody.create(null, errorMessage))
                .build()
        }
    }
}