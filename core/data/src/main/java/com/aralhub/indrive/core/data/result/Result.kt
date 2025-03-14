package com.aralhub.indrive.core.data.result

import com.aralhub.network.models.NetworkResult

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val message: String) : Result<Nothing>
}

inline fun <T, R> NetworkResult<T>.asResult(
    crossinline onSuccess: (T) -> Result<R>,
): Result<R> =
    when (this) {
        is NetworkResult.Error -> Result.Error(this.message)
        is NetworkResult.Success -> onSuccess(this.data)
    }
