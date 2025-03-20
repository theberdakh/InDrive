package com.aralhub.araltaxi.ride

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientCancelRideWithReasonUseCase
import com.aralhub.araltaxi.core.domain.client.ClientCancelRideWithoutReasonUseCase
import com.aralhub.araltaxi.core.domain.client.ClientGetActiveRideUseCase
import com.aralhub.araltaxi.core.domain.client.GetClientRideStatusUseCase
import com.aralhub.indrive.core.data.model.ride.ActiveRide
import com.aralhub.indrive.core.data.model.ride.RideStatus
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RideViewModel @Inject constructor(
    private val getActiveRideUseCase: ClientGetActiveRideUseCase,
    private val cancelRideWithoutReasonUseCase: ClientCancelRideWithoutReasonUseCase,
    private val cancelRideWithReasonUseCase: ClientCancelRideWithReasonUseCase,
    private val getClientRideStatusUseCase: GetClientRideStatusUseCase
) : ViewModel() {

    private var _activeRideState = MutableStateFlow<ActiveRideUiState>(ActiveRideUiState.Loading)
    val activeRideState = _activeRideState.asStateFlow()
    fun getActiveRide() = viewModelScope.launch {
        getActiveRideUseCase().let {
            when (it) {
                is Result.Error -> {
                    _activeRideState.emit(ActiveRideUiState.Error(it.message))
                }

                is Result.Success -> {
                    _activeRideState.emit(ActiveRideUiState.Success(it.data))
                }
            }
        }
    }

    private val _cancelRideState = MutableSharedFlow<CancelRideUiState>()
    val cancelRideState = _cancelRideState.asSharedFlow()
    fun cancelRide(rideId: Int) = viewModelScope.launch {
        cancelRideWithoutReasonUseCase(rideId).let {
            when (it) {
                is Result.Error -> {
                    _cancelRideState.emit(CancelRideUiState.Error(it.message))
                }

                is Result.Success -> {
                    _cancelRideState.emit(CancelRideUiState.Success)
                }
            }
        }
    }

    fun cancelRideWithReason(rideId: Int, reasonId: Int) = viewModelScope.launch {
        cancelRideWithReasonUseCase(rideId, reasonId).let {
            when (it) {
                is Result.Error -> {
                    _cancelRideState.emit(CancelRideUiState.Error(it.message))
                }

                is Result.Success -> {
                    _cancelRideState.emit(CancelRideUiState.Success)
                }
            }
        }
    }

    private var _rideStateUiState = MutableSharedFlow<RideStateUiState>()
    val rideStateUiState = _rideStateUiState.asSharedFlow()

    private var _waitingForDriverRideState = MutableSharedFlow<RideStateUiState>()
    val waitingForDriverRideState = _waitingForDriverRideState.asSharedFlow()

    fun getClientRideState() = viewModelScope.launch {
        getClientRideStatusUseCase().collect {
            Log.i("RideState ViewModel", it.toString())
            _waitingForDriverRideState.emit(RideStateUiState.Success(it))
            _rideStateUiState.emit(RideStateUiState.Success(it))
        }
    }
}


sealed interface RideStateUiState {
    data object Loading : RideStateUiState
    data class Success(val rideState: RideStatus) : RideStateUiState
    data class Error(val message: String) : RideStateUiState
}

sealed interface ActiveRideUiState {
    data object Loading : ActiveRideUiState
    data class Success(val activeRide: ActiveRide) : ActiveRideUiState
    data class Error(val message: String) : ActiveRideUiState
}

sealed interface CancelRideUiState {
    data object Loading : CancelRideUiState
    data object Success : CancelRideUiState
    data class Error(val message: String) : CancelRideUiState
}