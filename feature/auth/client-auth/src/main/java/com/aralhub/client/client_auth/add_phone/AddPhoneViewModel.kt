package com.aralhub.client.client_auth.add_phone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientAuthUseCase
import com.aralhub.indrive.core.data.model.client.AuthRequest
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPhoneViewModel @Inject constructor(
    private val useCase: ClientAuthUseCase,
) : ViewModel() {

    private val _addPhoneUiState = MutableSharedFlow<AddPhoneUiState>()
    val addPhoneUiState = _addPhoneUiState.asSharedFlow()

    fun auth(phone: String) = viewModelScope.launch {
        _addPhoneUiState.emit(AddPhoneUiState.Loading)
        _addPhoneUiState.emit(useCase(AuthRequest(phoneNumber = phone)).let {
            when (it) {
                is Result.Error -> AddPhoneUiState.Error(message = it.message)
                is Result.Success -> AddPhoneUiState.Success
            }
        })
    }
}

sealed interface AddPhoneUiState {
    data object Success : AddPhoneUiState
    data class Error(val message: String) : AddPhoneUiState
    data object Loading : AddPhoneUiState
}