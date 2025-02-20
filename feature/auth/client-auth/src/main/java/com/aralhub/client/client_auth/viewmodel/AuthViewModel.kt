package com.aralhub.client.client_auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientAuthUseCase
import com.aralhub.client.client_auth.model.AuthRequestUI
import com.aralhub.client.client_auth.model.toDomain
import com.aralhub.indrive.core.data.model.client.ClientVerifyRequest
import com.aralhub.indrive.core.data.result.WrappedResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val useCase: ClientAuthUseCase,
) : ViewModel() {

    private val _authState = MutableSharedFlow<LoginUiState>()
    val authState = _authState.asSharedFlow()

    fun auth(authRequestUI: AuthRequestUI) {
        viewModelScope.launch {
            _authState.emit(useCase.invoke(authRequestUI.toDomain()).let {
                when (it) {
                    is WrappedResult.Error -> LoginUiState.Error(it.message)
                    is WrappedResult.Loading -> LoginUiState.Loading
                    is WrappedResult.Success -> LoginUiState.Success
                }
            })
        }
    }
    fun verify(request: ClientVerifyRequest) {
        viewModelScope.launch {
            _authState.emit(useCase.userVerify(request).let {
                when (it) {
                    is WrappedResult.Error -> LoginUiState.Error(it.message)
                    is WrappedResult.Loading -> LoginUiState.Loading
                    is WrappedResult.Success -> LoginUiState.Success
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