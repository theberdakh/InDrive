package com.aralhub.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.driver.DriverProfileUseCase
import com.aralhub.indrive.core.data.model.driver.DriverProfile
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(private val driverProfileUseCase: DriverProfileUseCase): ViewModel() {
    private val _profileUiState = MutableSharedFlow<ProfileUiState>()
    val profileUiState = _profileUiState.asSharedFlow()

    fun getProfile() = viewModelScope.launch {
        _profileUiState.emit(ProfileUiState.Loading)
        _profileUiState.emit(driverProfileUseCase().let {
            when (it) {
                is Result.Error -> ProfileUiState.Error(it.message)
                is Result.Success -> ProfileUiState.Success(it.data)
            }
        })
    }
}

sealed interface ProfileUiState {
    data class Success(val profile: DriverProfile) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
    data object Loading : ProfileUiState
}