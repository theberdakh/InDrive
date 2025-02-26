package com.aralhub.araltaxi.savedplaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.address.CreateAddressUseCase
import com.aralhub.araltaxi.core.domain.address.GetAllSavedAddressesUseCase
import com.aralhub.indrive.core.data.model.address.Address
import com.aralhub.indrive.core.data.model.address.CreateAddressRequest
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedPlacesViewModel @Inject constructor(private val createAddressUseCase: CreateAddressUseCase,
    private val getAllSavedAddressesUseCase: GetAllSavedAddressesUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateAddressUiState>(CreateAddressUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun createAddress(address: CreateAddressRequest) = viewModelScope.launch {
        _uiState.value = createAddressUseCase.invoke(address).let {
            when (it) {
                is Result.Error -> CreateAddressUiState.Error(it.message)
                is Result.Success -> CreateAddressUiState.Success(it.data)
            }
        }
    }

    private val _savedPlacesUiState = MutableStateFlow<SavedPlacesUiState>(SavedPlacesUiState.Loading)
    val savedPlacesUiState = _savedPlacesUiState.asStateFlow()
    fun getAllSavedAddresses(userId: Int) = viewModelScope.launch {
        _savedPlacesUiState.value = getAllSavedAddressesUseCase.invoke(userId).let {
            when (it) {
                is Result.Error -> SavedPlacesUiState.Error(it.message)
                is Result.Success -> SavedPlacesUiState.Success(it.data)
            }
        }
    }

}

sealed interface CreateAddressUiState {
    data object Loading : CreateAddressUiState
    data class Success(val data: Address) : CreateAddressUiState
    data class Error(val message: String) : CreateAddressUiState
}

sealed interface SavedPlacesUiState {
    data object Loading : SavedPlacesUiState
    data class Success(val data: List<Address>) : SavedPlacesUiState
    data class Error(val message: String) : SavedPlacesUiState
}