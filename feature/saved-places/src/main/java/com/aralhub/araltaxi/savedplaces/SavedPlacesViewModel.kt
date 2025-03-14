package com.aralhub.araltaxi.savedplaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.address.CreateAddressUseCase
import com.aralhub.araltaxi.core.domain.address.GetAllSavedAddressesUseCase
import com.aralhub.indrive.core.data.model.address.Address
import com.aralhub.indrive.core.data.model.address.CreateAddressRequest
import com.aralhub.indrive.core.data.result.fold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedPlacesViewModel @Inject constructor(
    private val createAddressUseCase: CreateAddressUseCase,
    private val getAllSavedAddressesUseCase: GetAllSavedAddressesUseCase
) : ViewModel() {

    private val _createAddressUiState = MutableStateFlow<CreateAddressUiState>(CreateAddressUiState.Loading)
    val createAddressUiState = _createAddressUiState.asStateFlow()

    private val _savedPlacesUiState = MutableStateFlow<SavedPlacesUiState>(SavedPlacesUiState.Loading)
    val savedPlacesUiState = _savedPlacesUiState.asStateFlow()

    fun createAddress(address: CreateAddressRequest) {
        viewModelScope.launch {
            _createAddressUiState.value = createAddressUseCase(address).fold(
                onSuccess = CreateAddressUiState::Success,
                onError = CreateAddressUiState::Error
            )
        }
    }

    fun getAllSavedAddresses(userId: Int) {
        viewModelScope.launch {
            _savedPlacesUiState.value = getAllSavedAddressesUseCase(userId).fold(
                onSuccess = SavedPlacesUiState::Success,
                onError = SavedPlacesUiState::Error
            )
        }
    }
}

sealed interface CreateAddressUiState {
    data object Loading : CreateAddressUiState
    data class Success(val address: Address) : CreateAddressUiState
    data class Error(val message: String) : CreateAddressUiState
}

sealed interface SavedPlacesUiState {
    data object Loading : SavedPlacesUiState
    data class Success(val addresses: List<Address>) : SavedPlacesUiState
    data class Error(val message: String) : SavedPlacesUiState
}