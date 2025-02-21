package com.aralhub.araltaxi.profile.client.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientDeleteProfileUseCase
import com.aralhub.araltaxi.core.domain.client.ClientProfileUseCase
import com.aralhub.araltaxi.core.domain.client.ClientUploadProfileImageUseCase
import com.aralhub.indrive.core.data.model.client.ClientProfile
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val clientProfileUseCase: ClientProfileUseCase,
    private val clientUploadProfileImageUseCase: ClientUploadProfileImageUseCase,
    private val clientDeleteProfileUseCase: ClientDeleteProfileUseCase): ViewModel() {

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

    private val _uploadImageUiState = MutableSharedFlow<UploadImageUiState>()
    val uploadImageUiState = _uploadImageUiState.asSharedFlow()

    fun uploadImage(file: File) = viewModelScope.launch {
        _uploadImageUiState.emit(UploadImageUiState.Loading)
        _uploadImageUiState.emit(clientUploadProfileImageUseCase(file).let {
            when(it){
                is Result.Error -> UploadImageUiState.Error(it.message)
                is Result.Success -> UploadImageUiState.Success
            }
        })
    }

    private val _deleteProfileUiState = MutableSharedFlow<DeleteProfileUiState>()
    val deleteProfileUiState = _deleteProfileUiState.asSharedFlow()

    fun deleteProfile() = viewModelScope.launch {
        _deleteProfileUiState.emit(DeleteProfileUiState.Loading)
        _deleteProfileUiState.emit(clientDeleteProfileUseCase().let {
            when(it){
                is Result.Error -> DeleteProfileUiState.Error(it.message)
                is Result.Success -> DeleteProfileUiState.Success
            }
        })
    }
}

sealed interface DeleteProfileUiState {
    data object Success : DeleteProfileUiState
    data class Error(val message: String) : DeleteProfileUiState
    data object Loading : DeleteProfileUiState
}

sealed interface ProfileUiState {
    data class Success(val profile: ClientProfile) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
    data object Loading : ProfileUiState
}

sealed interface UploadImageUiState {
    data object Success : UploadImageUiState
    data class Error(val message: String) : UploadImageUiState
    data object Loading : UploadImageUiState
}