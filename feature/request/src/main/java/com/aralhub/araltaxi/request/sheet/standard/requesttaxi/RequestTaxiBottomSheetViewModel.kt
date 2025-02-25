package com.aralhub.araltaxi.request.sheet.standard.requesttaxi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.cancel.GetAllCancelCausesUseCase
import com.aralhub.araltaxi.core.domain.client.ClientGetActiveRideUseCase
import com.aralhub.araltaxi.core.domain.client.ClientGetRecommendedPriceUseCase
import com.aralhub.indrive.core.data.model.cancel.CancelCause
import com.aralhub.indrive.core.data.model.ride.ActiveRide
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestTaxiBottomSheetViewModel @Inject constructor(
    private val clientGetActiveRideUseCase: ClientGetActiveRideUseCase,
    private val clientCancelCausesUseCase: GetAllCancelCausesUseCase
) : ViewModel() {

    private val _activeRideUiState = MutableSharedFlow<ActiveRideUiState>()
    val activeRideUiState = _activeRideUiState.asSharedFlow()
    fun getActiveRide(userId: Int) = viewModelScope.launch {
        _activeRideUiState.emit(ActiveRideUiState.Loading)
        _activeRideUiState.emit(clientGetActiveRideUseCase(userId).let {
            when (it) {
                is Result.Error -> ActiveRideUiState.Error(it.message)
                is Result.Success -> ActiveRideUiState.Success(it.data)
            }
        })
    }

    private val _cancelRideUiState = MutableSharedFlow<CancelRideUiState>()
    val cancelRideUiState = _cancelRideUiState.asSharedFlow()
    fun getCancelCauses() = viewModelScope.launch {
        _cancelRideUiState.emit(CancelRideUiState.Loading)
        _cancelRideUiState.emit(clientCancelCausesUseCase().let {
            when (it) {
                is Result.Error -> CancelRideUiState.Error(it.message)
                is Result.Success -> CancelRideUiState.Success(it.data)
            }
        })
    }

}

sealed interface CancelRideUiState {
    data class Success(val cancelCauses: List<CancelCause>) : CancelRideUiState
    data class Error(val message: String) : CancelRideUiState
    data object Loading : CancelRideUiState
}

sealed interface ActiveRideUiState {
    data class Success(val activeRide: ActiveRide) : ActiveRideUiState
    data class Error(val message: String) : ActiveRideUiState
    data object Loading : ActiveRideUiState
}
