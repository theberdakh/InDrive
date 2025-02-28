package com.aralhub.araltaxi.driver.orders.orders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.driver.CloseDriverWebSocketConnectionUseCase
import com.aralhub.araltaxi.core.domain.driver.DriverLogoutUseCase
import com.aralhub.araltaxi.core.domain.driver.DriverProfileUseCase
import com.aralhub.araltaxi.core.domain.driver.GetActiveOrdersUseCase
import com.aralhub.araltaxi.core.domain.driver.SendDriverLocationUseCase
import com.aralhub.araltaxi.driver.orders.model.OrderItem
import com.aralhub.araltaxi.driver.orders.model.SendDriverLocationUI
import com.aralhub.araltaxi.driver.orders.model.asDomain
import com.aralhub.araltaxi.driver.orders.model.asUI
import com.aralhub.indrive.core.data.model.driver.DriverProfile
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.indrive.core.data.util.WebSocketEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val driverProfileUseCase: DriverProfileUseCase,
    private val driverLogoutUseCase: DriverLogoutUseCase,
    getActiveOrdersUseCase: GetActiveOrdersUseCase,
    private val sendDriverLocationUseCase: SendDriverLocationUseCase,
    private val closeDriverWebSocketConnectionUseCase: CloseDriverWebSocketConnectionUseCase
) : ViewModel() {

    private var _profileUiState = MutableSharedFlow<ProfileUiState>()
    val profileUiState = _profileUiState.asSharedFlow()
    fun getDriverProfile() = viewModelScope.launch {
        _profileUiState.emit(ProfileUiState.Loading)
        driverProfileUseCase().let { result ->
            when (result) {
                is Result.Success -> {
                    _profileUiState.emit(ProfileUiState.Success(result.data))
                }

                is Result.Error -> {
                    _profileUiState.emit(ProfileUiState.Error(result.message))
                }
            }
        }
    }

    private val _logoutUiState = MutableSharedFlow<LogoutUiState>()
    val logoutUiState = _logoutUiState.asSharedFlow()
    fun logout() = viewModelScope.launch {
        _logoutUiState.emit(LogoutUiState.Loading)
        driverLogoutUseCase().let { result ->
            when (result) {
                is Result.Success -> {
                    _logoutUiState.emit(LogoutUiState.Success)
                }

                is Result.Error -> {
                    _logoutUiState.emit(LogoutUiState.Error(result.message))
                }
            }
        }
    }

    val ordersState = getActiveOrdersUseCase
        .invoke()
        .map {
            when (it) {
                is WebSocketEvent.ActiveOffer -> {
                    GetActiveOrdersUiState.GetNewOrder(it.order.asUI())
                }

                is WebSocketEvent.RideCancel -> {
                    GetActiveOrdersUiState.OrderCanceled(it.rideId)
                }

                is WebSocketEvent.Unknown -> {
                    GetActiveOrdersUiState.Error(it.error)
                }
            }
        }
        .catch { t ->
            GetActiveOrdersUiState.Error(t.localizedMessage ?: "WebSocket Error")
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            GetActiveOrdersUiState.Loading
        )

    fun sendLocation(data: SendDriverLocationUI) {
        viewModelScope.launch {
            sendDriverLocationUseCase(data.asDomain())
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            Log.d("OrderViewModel", "cleared")
            closeDriverWebSocketConnectionUseCase.close()
        }
    }

}

sealed interface LogoutUiState {
    data object Loading : LogoutUiState
    data object Success : LogoutUiState
    data class Error(val message: String) : LogoutUiState
}

sealed interface GetActiveOrdersUiState {
    data object Loading : GetActiveOrdersUiState
    data class GetNewOrder(val data: OrderItem) : GetActiveOrdersUiState
    data class OrderCanceled(val rideId: String) : GetActiveOrdersUiState
    data class Error(val message: String) : GetActiveOrdersUiState
}

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Success(val driverProfile: DriverProfile) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}