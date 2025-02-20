package com.aralhub.client.clientauth.addname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientProfileUseCase
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNameViewModel @Inject constructor(private val useCase: ClientProfileUseCase) :
    ViewModel() {

    private val _addNameUiState = MutableSharedFlow<AddNameUiState>()
    val addNameUiState = _addNameUiState.asSharedFlow()

    fun addName(name: String) = viewModelScope.launch {
        _addNameUiState.emit(AddNameUiState.Loading)
        _addNameUiState.emit(useCase(fullName = name).let {
            when (it) {
                is Result.Error -> AddNameUiState.Error(it.message)
                is Result.Success -> AddNameUiState.Success
            } }
        )
    }

}

sealed interface AddNameUiState {
    data object Success : AddNameUiState
    data class Error(val message: String) : AddNameUiState
    data object Loading : AddNameUiState
}