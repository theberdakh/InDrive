package com.aralhub.client.client_auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientAuthUseCase
import com.aralhub.client.client_auth.model.AuthRequestUI
import com.aralhub.client.client_auth.model.toDomain
import com.aralhub.indrive.core.data.result.Result
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
            _authState.emit(LoginUiState.Loading)
            _authState.emit(useCase(authRequestUI.toDomain()).let {
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