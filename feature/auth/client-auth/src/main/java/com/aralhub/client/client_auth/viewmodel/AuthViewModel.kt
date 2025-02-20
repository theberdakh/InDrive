package com.aralhub.client.client_auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientAuthUseCase
import com.aralhub.araltaxi.core.domain.client.ClientProfileUseCase
import com.aralhub.indrive.core.data.model.client.ClientAddPhoneRequest
import com.aralhub.indrive.core.data.model.client.ClientVerifyRequest
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val useCase: ClientAuthUseCase,
    private val addUseCase: ClientProfileUseCase,
) : ViewModel() {

    private val _authState = MutableSharedFlow<LoginUiState>()
    val authState = _authState.asSharedFlow()

    fun auth(authRequestUI: ClientAddPhoneRequest) {
        viewModelScope.launch {
            _authState.emit(useCase.invoke(authRequestUI).let {
                when (it) {
                    is Result.Error -> LoginUiState.Error(it.message)
                    is Result.Success -> LoginUiState.Success
                }
            })
        }
    }
    fun verify(request: ClientVerifyRequest) {
        viewModelScope.launch {
            _authState.emit(useCase.userVerify(request).let {
                when (it) {
                    is Result.Error -> LoginUiState.Error(it.message)
                    is Result.Success -> LoginUiState.Success
                }
            })
        }
    }
    fun addName(fullName: String) {
        viewModelScope.launch {
            _authState.emit(addUseCase(fullName).let {
                when (it) {
                    is Result.Error -> LoginUiState.Error(it.message)
                    is Result.Success -> LoginUiState.Success
                }
            })
        }
    }

    sealed interface LoginUiState {
        data object Success : LoginUiState
        data class Error(val message: String) : LoginUiState
        data object Loading : LoginUiState
    }
}