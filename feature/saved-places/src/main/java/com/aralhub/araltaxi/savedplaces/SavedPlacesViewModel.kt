package com.aralhub.araltaxi.savedplaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.address.CreateAddressUseCase
import com.aralhub.indrive.core.data.model.address.Address
import com.aralhub.indrive.core.data.model.address.CreateAddressRequest
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedPlacesViewModel @Inject constructor(private val createAddressUseCase: CreateAddressUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun createAddress(address: CreateAddressRequest) = viewModelScope.launch {
        _uiState.value = createAddressUseCase.invoke(address).let {
            when (it) {
                is Result.Error -> HistoryUiState.Error(it.message)
                is Result.Success -> HistoryUiState.Success(it.data)
            }
        }
    }

}

sealed interface HistoryUiState {
    data object Loading : HistoryUiState
    data class Success(val data: Address) : HistoryUiState
    data class Error(val message: String) : HistoryUiState
}