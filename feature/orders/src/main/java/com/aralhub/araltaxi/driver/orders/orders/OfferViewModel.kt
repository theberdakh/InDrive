package com.aralhub.araltaxi.driver.orders.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.driver.offer.CreateOfferUseCase
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferViewModel @Inject constructor(
    private val createOfferUseCase: CreateOfferUseCase
) : ViewModel() {

    private var _createOfferUiState = MutableSharedFlow<CreateOfferUiState>()
    val createOfferUiState = _createOfferUiState.asSharedFlow()
    fun createOffer(rideUUID: String, amount: Int) {
        viewModelScope.launch {
            _createOfferUiState.emit(CreateOfferUiState.Loading)
            createOfferUseCase(rideUUID, amount).let { result ->
                when (result) {
                    is Result.Error -> _createOfferUiState.emit(CreateOfferUiState.Error(result.message))
                    is Result.Success -> _createOfferUiState.emit(CreateOfferUiState.Success(result.data))
                }
            }
        }
    }
}

sealed interface CreateOfferUiState {
    data object Loading : CreateOfferUiState
    data class Success(val rideId: String?) : CreateOfferUiState
    data class Error(val message: String) : CreateOfferUiState
}