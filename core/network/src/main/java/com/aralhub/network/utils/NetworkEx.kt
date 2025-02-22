package com.aralhub.network.utils

import com.aralhub.network.models.CustomError
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.ValidationError
import com.google.gson.Gson
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
                val gson = Gson()
                val errorBody = errorBody()
                return when (code()) {
                    422 -> {
                        return if (errorBody == null) {
                            NetworkResult.Error(message = "Error body is null")
                        } else {
                            val error = gson.fromJson(errorBody.string(), ValidationError::class.java)
                            NetworkResult.Error(message = error.detail[0].msg)
                        }
                    }

                    in 500..502 -> {
                        NetworkResult.Error("Server Error")
                    }

                    in 504..599 -> {
                        NetworkResult.Error(message = "Server Error")
                    }

                    503 -> {
                        NetworkResult.Error(message = "No Internet")
                    }

                    else -> {
                        return if (errorBody == null) {
                            NetworkResult.Error(message = "Error body is null")
                        } else {
                            val error = gson.fromJson(errorBody.string(), CustomError::class.java)
                            NetworkResult.Error(message = error.detail.kk)
                        }
                    }
                }
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
                val gson = Gson()
                val errorBody = errorBody()
                return when (code()) {
                    422 -> {
                        return if (errorBody == null) {
                            NetworkResult.Error(message = "Error body is null")
                        } else {
                            val error =
                                gson.fromJson(errorBody.string(), ValidationError::class.java)
                            NetworkResult.Error(message = error.detail[0].msg)
                        }
                    }

                    in 500..502 -> {
                        NetworkResult.Error("Server Error")
                    }

                    in 504..599 -> {
                        NetworkResult.Error(message = "Server Error")
                    }

                    503 -> {
                        NetworkResult.Error(message = "No Internet")
                    }

                    else -> {
                        return if (errorBody == null) {
                            NetworkResult.Error(message = "Error body is null")
                        } else {
                            val error = gson.fromJson(errorBody.string(), CustomError::class.java)
                            NetworkResult.Error(message = error.detail.kk)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            NetworkResult.Error(message = e.localizedMessage ?: "Unknown error", exception = e)
        }
    }
}