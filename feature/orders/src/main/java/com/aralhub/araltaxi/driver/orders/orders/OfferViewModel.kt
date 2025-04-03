package com.aralhub.araltaxi.driver.orders.orders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.driver.offer.CreateOfferUseCase
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferViewModel @Inject constructor(
    private val createOfferUseCase: CreateOfferUseCase
) : ViewModel() {

    private var _createOfferUiState = MutableStateFlow<CreateOfferUiState>(CreateOfferUiState.Loading)
    val createOfferUiState = _createOfferUiState.asStateFlow()
    fun createOffer(rideUUID: String, amount: Int) {
        Log.w("OfferViewModel", "createOffer called")
        viewModelScope.launch {
            createOfferUseCase(rideUUID, amount).let { result ->
                when (result) {
                    is Result.Error -> _createOfferUiState.value = (CreateOfferUiState.Error(result.message))
                    is Result.Success -> _createOfferUiState.value = (CreateOfferUiState.Success(result.data))
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