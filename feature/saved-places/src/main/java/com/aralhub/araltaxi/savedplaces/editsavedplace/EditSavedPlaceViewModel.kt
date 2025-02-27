package com.aralhub.araltaxi.savedplaces.editsavedplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.address.DeleteAddressUseCase
import com.aralhub.araltaxi.core.domain.address.GetAddressByIdUseCase
import com.aralhub.araltaxi.core.domain.address.UpdateAddressUseCase
import com.aralhub.indrive.core.data.model.address.Address
import com.aralhub.indrive.core.data.model.address.CreateAddressRequest
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditSavedPlaceViewModel @Inject constructor(
    private val getAddressByIdUseCase: GetAddressByIdUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase
) : ViewModel() {

    private val _getAddressByIdUiState = MutableSharedFlow<GetAddressByIdUiState>()
    val getAddressByIdUiState = _getAddressByIdUiState.asSharedFlow()

    private val _updateAddressUiState = MutableSharedFlow<UpdateAddressUiState>()
    val updateAddressUiState = _updateAddressUiState.asSharedFlow()

    private val _deleteAddressUiState = MutableSharedFlow<DeleteAddressUiState>()
    val deleteAddressUiState = _deleteAddressUiState.asSharedFlow()

    fun deleteAddress(addressId: Int) = viewModelScope.launch {
        _deleteAddressUiState.emit(DeleteAddressUiState.Loading)
        _deleteAddressUiState.emit(deleteAddressUseCase(addressId).let {
            when (it) {
                is Result.Success -> DeleteAddressUiState.Success
                is Result.Error -> DeleteAddressUiState.Error(it.message)
            }
        })
    }

    fun updateAddress(addressId: Int, address: CreateAddressRequest) = viewModelScope.launch {
        _updateAddressUiState.emit(UpdateAddressUiState.Loading)
        _updateAddressUiState.emit(updateAddressUseCase(addressId, address).let {
            when (it) {
                is Result.Success -> UpdateAddressUiState.Success(it.data)
                is Result.Error -> UpdateAddressUiState.Error(it.message)
            }
        })
    }

    fun getAddressById(addressId: Int) = viewModelScope.launch {
        _getAddressByIdUiState.emit(GetAddressByIdUiState.Loading)
        _getAddressByIdUiState.emit(getAddressByIdUseCase(addressId).let {
            when (it) {
                is Result.Success -> GetAddressByIdUiState.Success(it.data)
                is Result.Error -> GetAddressByIdUiState.Error(it.message)
            }
        })
    }
}

sealed interface GetAddressByIdUiState {
    data object Loading : GetAddressByIdUiState
    data class Success(val data: Address) : GetAddressByIdUiState
    data class Error(val message: String) : GetAddressByIdUiState
}

sealed interface UpdateAddressUiState {
    data object Loading : UpdateAddressUiState
    data class Success(val data: Address) : UpdateAddressUiState
    data class Error(val message: String) : UpdateAddressUiState
}

sealed interface DeleteAddressUiState {
    data object Loading : DeleteAddressUiState
    data object Success : DeleteAddressUiState
    data class Error(val message: String) : DeleteAddressUiState
}
