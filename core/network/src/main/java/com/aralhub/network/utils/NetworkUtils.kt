package com.aralhub.network.utils

import com.aralhub.network.models.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

object NetworkUtils {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall.invoke()
                if (response.isSuccessful) {
                    NetworkResult.Success(response.body()!!)
                } else {
                    val except = when (response.code()) {
                        in 400..499 -> {
                            fetchErrorResponseMessage(response.errorBody())
                        }
                        in 500..599 -> {
                            "Server Error!"
                        }
                        else -> {
                            "Http unknown exception"
                        }
                    }
                    NetworkResult.Error(
                        except ?: "error"
                    )
                }
            } catch (error: Throwable) {

//                val exception = when (error) {
//
//                    is HttpException -> {
//                        when (error.code()) {
//                            in 400..499 -> {
//                                ClientException(
//                                    message = "$CLIENT_ERROR: ${error.code()}",
//                                    cause = error,
//                                )
//                            }
//
//                            in 500..599 -> ServerException(
//                                message = "${AppConstants.SERVER_ERROR}: ${error.code()}",
//                                cause = error
//                            )
//
//                            else -> UnknownException(
//                                message = "${AppConstants.HTTP_UNKNOWN_ERROR}: ${error.code()}",
//                                cause = error
//                            )
//                        }
//                    }
//
//                    is NoNetworkException -> AppException(
//                        message = AppConstants.CONNECTIVITY_ERROR
//                    )
//
//                    is TimeoutException -> ServerException(
//                        message = AppConstants.TIME_OUT_ERROR
//                    )
//
//                    is ConnectException -> ServerException(
//                        message = AppConstants.TIME_OUT_ERROR
//                    )
//
//                    is SocketTimeoutException -> ServerException(
//                        message = AppConstants.TIME_OUT_ERROR
//                    )
//
//                    is SSLHandshakeException -> ServerException(
//                        message = AppConstants.HANDSHAKE_ERROR
//                    )
//
//                    is UnknownHostException -> NetworkException(
//                        message = AppConstants.UNKNOWN_HOST,
//                        cause = error
//                    )
//
//                    is IOException -> NetworkException(
//                        message = AppConstants.NETWORK_ERROR,
//                        cause = error
//                    )
//
//                    else -> AppException(
//                        message = AppConstants.UNKNOWN_ERROR,
//                        cause = error
//                    )
//
//                }

                NetworkResult.Error("Error")
            }
        }
    }

    private fun fetchErrorResponseMessage(errorBody: ResponseBody?): String? {
        return try {
//            val error = errorBody?.string()?.parse<ErrorResponseData>()
//            error?.message
            "Error occurred"
        } catch (e: Exception) {
            "Неизвестная ошибка"
        }
    }

}