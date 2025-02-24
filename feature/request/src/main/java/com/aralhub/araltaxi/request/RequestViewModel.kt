package com.aralhub.araltaxi.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientLogOutUseCase
import com.aralhub.araltaxi.core.domain.client.ClientProfileUseCase
import com.aralhub.indrive.core.data.model.client.ClientProfile
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestViewModel @Inject constructor(
    private val clientProfileUseCase: ClientProfileUseCase,
    private val clientLogOutUseCase: ClientLogOutUseCase
) :
    ViewModel() {

    private var _locationEnabled = MutableStateFlow<Boolean>(false)
    val locationEnabled = _locationEnabled.asStateFlow()
    fun updateLocationEnabled(value: Boolean) {
        _locationEnabled.value = value
    }


    private val _profileUiState = MutableSharedFlow<ProfileUiState>()
    val profileUiState = _profileUiState.asSharedFlow()

    fun getProfile() = viewModelScope.launch {
        _profileUiState.emit(ProfileUiState.Loading)
        _profileUiState.emit(clientProfileUseCase().let {
            when (it) {
                is Result.Error -> ProfileUiState.Error(it.message)
                is Result.Success -> ProfileUiState.Success(it.data)
            }
        })
    }

    private val _logOutUiState = MutableSharedFlow<LogOutUiState>()
    val logOutUiState = _logOutUiState.asSharedFlow()

    fun logOut() = viewModelScope.launch {
        _logOutUiState.emit(LogOutUiState.Loading)
        _logOutUiState.emit(clientLogOutUseCase().let {
            when (it) {
                is Result.Error -> LogOutUiState.Error(it.message)
                is Result.Success -> LogOutUiState.Success
            }
        })
    }

}

sealed interface ProfileUiState {
    data class Success(val profile: ClientProfile) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
    data object Loading : ProfileUiState
}

sealed interface LogOutUiState {
    data object Success : LogOutUiState
    data class Error(val message: String) : LogOutUiState
    data object Loading : LogOutUiState
}