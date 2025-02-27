package com.aralhub.araltaxi.driver.orders.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.driver.DriverLogoutUseCase
import com.aralhub.araltaxi.core.domain.driver.DriverProfileUseCase
import com.aralhub.indrive.core.data.model.driver.DriverProfile
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class OrdersViewModel @Inject constructor(private val driverProfileUseCase: DriverProfileUseCase,
    private val driverLogoutUseCase: DriverLogoutUseCase): ViewModel() {

    private var _profileUiState = MutableSharedFlow<ProfileUiState>()
    val profileUiState = _profileUiState.asSharedFlow()
    fun getDriverProfile()  = viewModelScope.launch {
        _profileUiState.emit(ProfileUiState.Loading)
        driverProfileUseCase().let { result ->
            when(result){
                is Result.Success -> {
                    _profileUiState.emit(ProfileUiState.Success(result.data))
                }
                is Result.Error -> {
                    _profileUiState.emit(ProfileUiState.Error(result.message))
                }
            }
        }
    }

    private val _logoutUiState = MutableSharedFlow<LogoutUiState>()
    val logoutUiState = _logoutUiState.asSharedFlow()
    fun logout() = viewModelScope.launch {
        _logoutUiState.emit(LogoutUiState.Loading)
        driverLogoutUseCase().let { result ->
            when(result){
                is Result.Success -> {
                    _logoutUiState.emit(LogoutUiState.Success)
                }
                is Result.Error -> {
                    _logoutUiState.emit(LogoutUiState.Error(result.message))
                }
            }
        }
    }
}

sealed interface LogoutUiState {
    data object Loading : LogoutUiState
    data object Success : LogoutUiState
    data class Error(val message: String) : LogoutUiState
}

sealed interface ProfileUiState {
    data object Loading: ProfileUiState
    data class Success(val driverProfile: DriverProfile): ProfileUiState
    data class Error(val message: String): ProfileUiState
}