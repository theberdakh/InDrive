package com.aralhub.client.clientauth.addsms

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AddSMSViewModel : ViewModel() {
    private val _addSMSUiState = MutableSharedFlow<AddSMSUiState>()
    val addSMSUiState = _addSMSUiState.asSharedFlow()

}

sealed interface AddSMSUiState {
    data class Success(val phone: String) : AddSMSUiState
    data class Error(val message: String) : AddSMSUiState
    data object Loading : AddSMSUiState
}