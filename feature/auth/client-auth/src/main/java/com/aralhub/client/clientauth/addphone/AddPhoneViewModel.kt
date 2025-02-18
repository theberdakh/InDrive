package com.aralhub.client.clientauth.addphone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientAddPhoneUseCase
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPhoneViewModel @Inject constructor(private val useCase: ClientAddPhoneUseCase) : ViewModel() {
    private val _addPhoneUiState = MutableSharedFlow<AddPhoneUiState>()
    val addPhoneUiState = _addPhoneUiState.asSharedFlow()

    fun auth(phone: String) = viewModelScope.launch {
        _addPhoneUiState.emit(AddPhoneUiState.Loading)
        _addPhoneUiState.emit(useCase(phone = phone).let {
            when (it) {
                is Result.Error -> AddPhoneUiState.Error(message = it.message)
                is Result.Success -> AddPhoneUiState.Success
            }
        })
    }
}

sealed interface AddPhoneUiState {
    data object Success : AddPhoneUiState
    data class Error(val message: String) : AddPhoneUiState
    data object Loading : AddPhoneUiState
}