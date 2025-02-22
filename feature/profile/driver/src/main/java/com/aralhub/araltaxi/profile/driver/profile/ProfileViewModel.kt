package com.aralhub.araltaxi.profile.driver.profile

import android.provider.ContactsContract.Profile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.driver.DriverProfileUseCase
import com.aralhub.indrive.core.data.model.driver.DriverProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val driverProfileUseCase: DriverProfileUseCase): ViewModel() {

    private var _profileUiState = MutableSharedFlow<ProfileUiState>()
    val profileUiState = _profileUiState
    fun getDriverProfile()  = viewModelScope.launch {
        _profileUiState.emit(ProfileUiState.Loading)
        driverProfileUseCase().let { result ->
            when(result){
                is com.aralhub.indrive.core.data.result.Result.Success -> {
                    _profileUiState.emit(ProfileUiState.Success(result.data))
                }
                is com.aralhub.indrive.core.data.result.Result.Error -> {
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