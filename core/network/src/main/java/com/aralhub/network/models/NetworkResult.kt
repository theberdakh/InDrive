package com.aralhub.network.models

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String, val exception: Throwable? = null) :
        NetworkResult<Nothing>()
}

sealed interface NetworkWrappedResult<out T> {
    data class Success<T>(val data: T) : NetworkWrappedResult<T>
    data class Error(val message: String) : NetworkWrappedResult<Nothing>
    data object Loading : NetworkWrappedResult<Nothing>
}