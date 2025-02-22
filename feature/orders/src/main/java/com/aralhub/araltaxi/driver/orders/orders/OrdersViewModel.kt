package com.aralhub.araltaxi.driver.orders.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.driver.DriverProfileUseCase
import com.aralhub.indrive.core.data.model.driver.DriverProfile
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class OrdersViewModel @Inject constructor(private val driverProfileUseCase: DriverProfileUseCase): ViewModel() {

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
}

sealed interface ProfileUiState {
    data object Loading: ProfileUiState
    data class Success(val driverProfile: DriverProfile): ProfileUiState
    data class Error(val message: String): ProfileUiState
}