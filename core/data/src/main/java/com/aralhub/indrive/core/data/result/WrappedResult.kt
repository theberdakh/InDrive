package com.aralhub.indrive.core.data.result

sealed interface WrappedResult<out T> {
    data class Success<T>(val data: T) : WrappedResult<T>
    data class Error(val message: String) : WrappedResult<Nothing>
    data object Loading : WrappedResult<Nothing>
}