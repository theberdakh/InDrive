package com.aralhub.araltaxi.savedplaces.editsavedplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.address.GetAddressByIdUseCase
import com.aralhub.indrive.core.data.model.address.Address
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import com.aralhub.indrive.core.data.result.Result
import javax.inject.Inject

@HiltViewModel
class EditSavedPlaceViewModel @Inject constructor(private val getAddressByIdUseCase: GetAddressByIdUseCase): ViewModel() {

    private val _getAddressByIdUiState = MutableSharedFlow<GetAddressByIdUiState>()
    val getAddressByIdUiState = _getAddressByIdUiState.asSharedFlow()

    fun getAddressById(addressId: Int) = viewModelScope.launch {
        _getAddressByIdUiState.emit(GetAddressByIdUiState.Loading)
        _getAddressByIdUiState.emit(getAddressByIdUseCase(addressId).let {
            when(it) {
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

