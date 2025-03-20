package com.aralhub.araltaxi.savedplaces

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.address.CreateAddressUseCase
import com.aralhub.araltaxi.core.domain.address.GetAllSavedAddressesUseCase
import com.aralhub.indrive.core.data.model.address.Address
import com.aralhub.indrive.core.data.model.address.CreateAddressRequest
import com.aralhub.indrive.core.data.result.fold
import com.aralhub.ui.model.AddressCategory
import com.aralhub.ui.model.AddressItem
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

    private val _savedPlacesUiState =
        MutableStateFlow<SavedPlacesUiState>(SavedPlacesUiState.Loading)
    val savedPlacesUiState = _savedPlacesUiState.asStateFlow()


    fun getAllSavedAddresses() {
        viewModelScope.launch {
            _savedPlacesUiState.value = getAllSavedAddressesUseCase().fold(
                onSuccess = {
                    Log.i("Locations", "$it")
                    SavedPlacesUiState.Success(it.map { address -> address.toAddressItem() })
                },
                onError = SavedPlacesUiState::Error
            )
        }
    }
}

fun Address.toAddressItem() = AddressItem(
    id = this.id,
    name = this.name,
    address = this.address,
    category = AddressCategory.OTHER,
    latitude = this.latitude,
    longitude = this.longitude
)

sealed interface SavedPlacesUiState {
    data object Loading : SavedPlacesUiState
    data class Success(val addresses: List<AddressItem>) : SavedPlacesUiState
    data class Error(val message: String) : SavedPlacesUiState
}