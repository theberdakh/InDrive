package com.aralhub.network.utils

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.ServerResponse
import retrofit2.Response

object NetworkEx {
    fun <T> Response<ServerResponse<T>>.safeRequestServerResponse(): NetworkResult<T> {
        return try {
            if (isSuccessful) {
                body()?.let { serverResponse ->
                    if (serverResponse.success) {
                        NetworkResult.Success(data = serverResponse.data)
                    } else {
                        NetworkResult.Error(message = serverResponse.message.toString())
                    }
                } ?: NetworkResult.Error(message = "Response body is null")
            } else {
                NetworkResult.Error(message = "Response is not successful: ${code()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(message = e.message ?: "Unknown error", exception = e)
        }
    }

    fun <T> Response<T>.safeRequest(): NetworkResult<T> {
        return try {
            if (isSuccessful) {
                body()?.let {
                    NetworkResult.Success(data = it)
                } ?: NetworkResult.Error(message = "Response body is null")
            } else {
                val errorBody = errorBody()?.string()
                NetworkResult.Error(message = errorBody ?: message())
            }
        } catch (e: Exception) {
            NetworkResult.Error(message = e.localizedMessage ?: "Unknown error", exception = e)
        }
    }
}