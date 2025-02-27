package com.aralhub.araltaxi.profile.driver.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.driver.DriverProfileUseCase
import com.aralhub.araltaxi.core.domain.driver.DriverProfileWithVehicleUseCase
import com.aralhub.araltaxi.core.domain.driver.DriverUploadProfileImageUseCase
import com.aralhub.araltaxi.core.domain.driver.GetDriverCardUseCase
import com.aralhub.indrive.core.data.model.driver.DriverProfile
import com.aralhub.indrive.core.data.model.driver.DriverProfileWithVehicle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.aralhub.indrive.core.data.result.Result
import kotlinx.coroutines.flow.asSharedFlow
import java.io.File

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val driverProfileUseCase: DriverProfileUseCase,
    private val driverCardUseCase: GetDriverCardUseCase,
    private val driverProfileWithVehicleUseCase: DriverProfileWithVehicleUseCase,
    private val driverUploadProfileImageUseCase: DriverUploadProfileImageUseCase
) : ViewModel() {

    private var _profileUiState = MutableSharedFlow<ProfileUiState>()
    val profileUiState = _profileUiState.asSharedFlow()
    fun getDriverProfile() = viewModelScope.launch {
        _profileUiState.emit(ProfileUiState.Loading)
        driverProfileUseCase().let { result ->
            when (result) {
                is Result.Success -> {
                    _profileUiState.emit(ProfileUiState.Success(result.data))
                }
                is Result.Error -> {
                    _profileUiState.emit(ProfileUiState.Error(result.message))
                }
            }
        }
    }

    private var _cardUiState = MutableSharedFlow<CardUiState>()
    val cardUiState = _cardUiState.asSharedFlow()
    fun getDriverCard() = viewModelScope.launch {
        _cardUiState.emit(CardUiState.Loading)
        driverCardUseCase().let { result ->
            when (result) {
                is Result.Success -> {
                    _cardUiState.emit(CardUiState.Success(result.data.cardNumber, result.data.nameOnCard))
                }
                is Result.Error -> {
                    _cardUiState.emit(CardUiState.Error(result.message))
                }
            }
        }
    }

    private var _profileWithVehicleUiState = MutableSharedFlow<ProfileWithVehicleUiState>()
    val profileWithVehicleUiState = _profileWithVehicleUiState.asSharedFlow()
    fun getDriverProfileWithVehicle() = viewModelScope.launch {
        _profileWithVehicleUiState.emit(ProfileWithVehicleUiState.Loading)
        driverProfileWithVehicleUseCase().let { result ->
            when (result) {
                is Result.Success -> {
                    _profileWithVehicleUiState.emit(ProfileWithVehicleUiState.Success(result.data))
                }
                is Result.Error -> {
                    _profileWithVehicleUiState.emit(ProfileWithVehicleUiState.Error(result.message))
                }
            }
        }
    }

    private val _uploadImageUiState = MutableSharedFlow<UploadImageUiState>()
    val uploadImageUiState = _uploadImageUiState.asSharedFlow()

    fun uploadImage(file: File) = viewModelScope.launch {
        _uploadImageUiState.emit(UploadImageUiState.Loading)
        _uploadImageUiState.emit(driverUploadProfileImageUseCase(file).let {
            when (it) {
                is Result.Error -> UploadImageUiState.Error(it.message)
                is Result.Success -> UploadImageUiState.Success
            }
        })
    }
}

sealed interface ProfileWithVehicleUiState {
    data object Loading : ProfileWithVehicleUiState
    data class Success(val driverProfileWithVehicle: DriverProfileWithVehicle) :
        ProfileWithVehicleUiState

    data class Error(val message: String) : ProfileWithVehicleUiState
}

sealed interface CardUiState {
    data object Loading : CardUiState
    data class Success(val card: String, val cardHolder: String) : CardUiState
    data class Error(val message: String) : CardUiState
}

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Success(val driverProfile: DriverProfile) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}

sealed interface UploadImageUiState {
    data object Success : UploadImageUiState
    data class Error(val message: String) : UploadImageUiState
    data object Loading : UploadImageUiState
}