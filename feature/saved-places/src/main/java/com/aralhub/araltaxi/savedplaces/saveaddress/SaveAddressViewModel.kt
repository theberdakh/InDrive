package com.aralhub.araltaxi.savedplaces.saveaddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.address.CreateAddressUseCase
import com.aralhub.indrive.core.data.model.address.Address
import com.aralhub.indrive.core.data.model.address.CreateAddressRequest
import com.aralhub.indrive.core.data.result.fold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveAddressViewModel @Inject constructor(private val createAddressUseCase: CreateAddressUseCase,): ViewModel() {

    private val _createAddressUiState =
        MutableStateFlow<CreateAddressUiState>(CreateAddressUiState.Loading)
    val createAddressUiState = _createAddressUiState.asStateFlow()

    fun createAddress(address: CreateAddressRequest) {
        viewModelScope.launch {
            _createAddressUiState.value = createAddressUseCase(address).fold(
                onSuccess = CreateAddressUiState::Success,
                onError = CreateAddressUiState::Error
            )
        }
    }
}

sealed interface CreateAddressUiState {
    data object Loading : CreateAddressUiState
    data class Success(val address: Address) : CreateAddressUiState
    data class Error(val message: String) : CreateAddressUiState
}