package com.aralhub.araltaxi.core.common.ui

sealed interface UiState<T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}