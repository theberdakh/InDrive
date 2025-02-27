package com.aralhub.araltaxi.history.client

import androidx.lifecycle.ViewModel
import com.aralhub.araltaxi.core.domain.address.CreateAddressUseCase
import com.aralhub.indrive.core.data.model.address.Address
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val createAddressUseCase: CreateAddressUseCase) :
    ViewModel() {
    private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val uiState = _uiState.asStateFlow()


}

sealed interface HistoryUiState {
    data object Loading : HistoryUiState
    data class Success(val data: Address) : HistoryUiState
    data class Error(val message: String) : HistoryUiState
}