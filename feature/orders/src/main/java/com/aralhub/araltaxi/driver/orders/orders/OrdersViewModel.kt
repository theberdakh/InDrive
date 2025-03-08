package com.aralhub.araltaxi.driver.orders.orders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.driver.CloseDriverWebSocketConnectionUseCase
import com.aralhub.araltaxi.core.domain.driver.DriverLogoutUseCase
import com.aralhub.araltaxi.core.domain.driver.DriverProfileUseCase
import com.aralhub.araltaxi.core.domain.driver.GetActiveOrdersUseCase
import com.aralhub.araltaxi.core.domain.driver.GetExistingOrdersUseCase
import com.aralhub.araltaxi.core.domain.driver.SendDriverLocationUseCase
import com.aralhub.araltaxi.driver.orders.model.SendDriverLocationUI
import com.aralhub.araltaxi.driver.orders.model.asDomain
import com.aralhub.araltaxi.driver.orders.model.asUI
import com.aralhub.indrive.core.data.model.driver.DriverProfile
import com.aralhub.indrive.core.data.repository.driver.DriverRepository
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.indrive.core.data.util.WebSocketEvent
import com.aralhub.ui.model.OrderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val driverProfileUseCase: DriverProfileUseCase,
    private val driverLogoutUseCase: DriverLogoutUseCase,
    getActiveOrdersUseCase: GetActiveOrdersUseCase,
    private val getExistingOrdersUseCase: GetExistingOrdersUseCase,
    private val sendDriverLocationUseCase: SendDriverLocationUseCase,
    private val closeDriverWebSocketConnectionUseCase: CloseDriverWebSocketConnectionUseCase,
    private val repository: DriverRepository
) : ViewModel() {

    init {
        getActiveRide()
    }

    private var _rejectOfferState = MutableSharedFlow<String>()
    val rejectOfferState = _rejectOfferState.asSharedFlow()

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

    private val existingOrdersState =
        MutableStateFlow<GetActiveOrdersUiState>(GetActiveOrdersUiState.Loading)

    fun getExistingOrders(
        sendDriverLocationUI: SendDriverLocationUI
    ) = viewModelScope.launch {
        getExistingOrdersUseCase(sendDriverLocationUI.asDomain()).let { result ->
            when (result) {
                is Result.Success -> {
                    val listOfOrders = result.data.map { it.asUI() }
                    existingOrdersState.value =
                        (GetActiveOrdersUiState.GetExistOrder(listOfOrders))
                }

                is Result.Error -> {
                    existingOrdersState.value = (GetActiveOrdersUiState.Error(result.message))
                }
            }
        }
    }

    private val ordersState = getActiveOrdersUseCase
        .invoke()
        .map {
            when (it) {
                is WebSocketEvent.ActiveOffer -> {
                    GetActiveOrdersUiState.GetNewOrder(it.order.asUI())
                }

                is WebSocketEvent.OfferAccepted -> {
                    GetActiveOrdersUiState.OfferAccepted(it.rideId)
                }

                is WebSocketEvent.RideCancel -> {
                    GetActiveOrdersUiState.OrderCanceled(it.rideId)
                }

                is WebSocketEvent.OfferReject -> {
                    GetActiveOrdersUiState.OfferRejected(it.rideUUID)
                }

                is WebSocketEvent.Unknown -> {
                    GetActiveOrdersUiState.Error(it.error)
                }
            }
        }
        .catch { t ->
            GetActiveOrdersUiState.Error(t.localizedMessage ?: "WebSocket Error")
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            GetActiveOrdersUiState.Loading
        )

    val combinedOrdersState = merge(
        existingOrdersState,
        ordersState
    ).map { result ->
        when (result) {
            is GetActiveOrdersUiState.GetExistOrder -> {
                GetActiveOrdersUiState.GetExistOrder(result.data)
            }

            is GetActiveOrdersUiState.GetNewOrder -> {
                GetActiveOrdersUiState.GetNewOrder(result.data)
            }

            is GetActiveOrdersUiState.OfferAccepted -> {
                GetActiveOrdersUiState.OfferAccepted(result.rideId)
            }

            is GetActiveOrdersUiState.OrderCanceled -> {
                GetActiveOrdersUiState.OrderCanceled(result.rideId)
            }

            is GetActiveOrdersUiState.OfferRejected -> {
                _rejectOfferState.emit(result.rideUUID)
                GetActiveOrdersUiState.OfferRejected(result.rideUUID)
            }

            else -> {
                GetActiveOrdersUiState.Loading
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        GetActiveOrdersUiState.Loading
    )

    fun sendLocation(data: SendDriverLocationUI) {
        viewModelScope.launch {
            sendDriverLocationUseCase(data.asDomain())
        }
    }

    private fun disconnect() {
        CoroutineScope(Dispatchers.IO).launch {
            closeDriverWebSocketConnectionUseCase.close()
        }
    }

    private var _activeOrdersUiState = MutableSharedFlow<Int?>()
    val activeOrdersUiState = _activeOrdersUiState.asSharedFlow()
    fun getActiveRide() {
        viewModelScope.launch {
            repository.getActiveRide().let { result ->
                when (result) {
                    is Result.Error -> {}
                    is Result.Success -> _activeOrdersUiState.emit((result.data))
                }
            }
        }
    }

    fun cancelRide(rideId: Int, cancelCauseId: Int) {
        viewModelScope.launch {
            repository.cancelRide(rideId, cancelCauseId).let { result ->
                when (result) {
                    is Result.Error -> {}
                    is Result.Success -> {
                        _activeOrdersUiState.emit(0)
                    }
                }
            }
        }
    }

    fun updateRideStatus(rideId: Int, status: String) {
        viewModelScope.launch {
            repository.updateRideStatus(rideId, status).let { result ->
                when (result) {
                    is Result.Error -> {
                        Log.d("OrdersViewModel", "error")
                    }

                    is Result.Success -> {
                        Log.d("OrdersViewModel", "success")
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
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
    data class GetExistOrder(val data: List<OrderItem>) : GetActiveOrdersUiState
    data class OrderCanceled(val rideId: String) : GetActiveOrdersUiState
    data class OfferRejected(val rideUUID: String) : GetActiveOrdersUiState
    data class OfferAccepted(val rideId: Int) : GetActiveOrdersUiState
    data class Error(val message: String) : GetActiveOrdersUiState
}

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Success(val driverProfile: DriverProfile) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}