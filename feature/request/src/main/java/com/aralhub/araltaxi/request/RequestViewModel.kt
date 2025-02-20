package com.aralhub.araltaxi.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientProfileUseCase
import com.aralhub.indrive.core.data.model.client.ClientProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.aralhub.indrive.core.data.result.Result

@HiltViewModel
class RequestViewModel @Inject constructor(private val clientProfileUseCase: ClientProfileUseCase) :
    ViewModel() {
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
}

sealed interface ProfileUiState {
    data class Success(val profile: ClientProfile) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
    data object Loading : ProfileUiState
}