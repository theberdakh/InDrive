package com.aralhub.araltaxi.driver.orders.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.driver.GetStartedRideStatusUseCase
import com.aralhub.indrive.core.data.util.StartedRideWebSocketEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StartedRideStatusViewModel @Inject constructor(
    getStartedRideStatusUseCase: GetStartedRideStatusUseCase,
) : ViewModel() {

    val startedRideStatus = getStartedRideStatusUseCase
        .invoke()
        .map {
            when (it) {
                is StartedRideWebSocketEvent.RideCancelledByPassenger -> {
                    GetStartedRideStatusUiState.RideCanceled
                }

                is StartedRideWebSocketEvent.UnknownAction -> {
                    GetStartedRideStatusUiState.Error(it.error)
                }
            }
        }
        .catch { t ->
            GetActiveOrdersUiState.Error(t.localizedMessage ?: "WebSocket Error")
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            GetActiveOrdersUiState.Loading
        )

}