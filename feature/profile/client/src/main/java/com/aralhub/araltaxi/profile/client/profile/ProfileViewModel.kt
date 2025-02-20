package com.aralhub.araltaxi.profile.client.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientProfileUseCase
import com.aralhub.indrive.core.data.model.client.ClientProfile
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val useCase: ClientProfileUseCase): ViewModel() {

    private val _profileUiState = MutableSharedFlow<ProfileUiState>()
    val profileUiState = _profileUiState.asSharedFlow()

    fun getProfile() = viewModelScope.launch {
        _profileUiState.emit(ProfileUiState.Loading)
        _profileUiState.emit(useCase().let {
            when (it) {
                is Result.Error -> ProfileUiState.Error(it.message)
                is Result.Success -> ProfileUiState.Success(it.data)
            }
        })
    }
}

sealed interface ProfileUiState {
    data class Success(val profile: ClientProfile) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
    data object Loading : ProfileUiState
}