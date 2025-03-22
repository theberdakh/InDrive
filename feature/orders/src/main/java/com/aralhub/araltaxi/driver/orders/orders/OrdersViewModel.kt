package com.aralhub.araltaxi.driver.orders.orders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.common.utils.rejectOfferState
import com.aralhub.araltaxi.core.domain.driver.CancelRideUseCase
import com.aralhub.araltaxi.core.domain.driver.CloseDriverWebSocketConnectionUseCase
import com.aralhub.araltaxi.core.domain.driver.DriverLogoutUseCase
import com.aralhub.araltaxi.core.domain.driver.DriverProfileUseCase
import com.aralhub.araltaxi.core.domain.driver.GetActiveOrdersUseCase
import com.aralhub.araltaxi.core.domain.driver.GetCancelCausesUseCase
import com.aralhub.araltaxi.core.domain.driver.GetExistingOrdersUseCase
import com.aralhub.araltaxi.core.domain.driver.UpdateRideStatusUseCase
import com.aralhub.araltaxi.driver.orders.model.SendDriverLocationUI
import com.aralhub.araltaxi.driver.orders.model.asDomain
import com.aralhub.araltaxi.driver.orders.model.asUI
import com.aralhub.indrive.core.data.model.driver.DriverInfo
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.indrive.core.data.util.WebSocketEvent
import com.aralhub.ui.model.CancelItem
import com.aralhub.ui.model.OrderItem
import com.aralhub.ui.model.RideCompletedUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val closeDriverWebSocketConnectionUseCase: CloseDriverWebSocketConnectionUseCase,
    private val updateRideStatusUseCase: UpdateRideStatusUseCase,
    private val cancelRideUseCase: CancelRideUseCase,
    private val getCancelCausesUseCase: GetCancelCausesUseCase
) : ViewModel() {

    private val _ordersListState = MutableStateFlow<List<OrderItem>>(emptyList())
    val ordersListState: StateFlow<List<OrderItem>> = _ordersListState.asStateFlow()

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
                    _ordersListState.value = listOfOrders
                }

                is Result.Error -> {
                    existingOrdersState.value = (GetActiveOrdersUiState.Error(result.message))
                }
            }
        }
    }

    private val webSocketOrdersState = getActiveOrdersUseCase
        .invoke()
        .map {
            when (it) {
                is WebSocketEvent.ActiveOffer -> {
                    addOrder(it.order.asUI())
                    GetActiveOrdersUiState.GetNewOrder(it.order.asUI())
                }

                is WebSocketEvent.OfferAccepted -> {
                    GetActiveOrdersUiState.OfferAccepted(it.data.asUI())
                }

                is WebSocketEvent.RideCancel -> {
                    removeOrder(it.rideId)
                    GetActiveOrdersUiState.OrderCanceled(it.rideId)
                }

                is WebSocketEvent.OfferReject -> {
                    rejectOfferState.emit(Unit)
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

    val ordersState = merge(
        existingOrdersState,
        webSocketOrdersState
    ).map { result ->
        when (result) {
            is GetActiveOrdersUiState.GetExistOrder -> {
                GetActiveOrdersUiState.GetExistOrder(result.data)
            }

            is GetActiveOrdersUiState.GetNewOrder -> {
                GetActiveOrdersUiState.GetNewOrder(result.data)
            }

            is GetActiveOrdersUiState.OfferAccepted -> {
                Log.e("WebSocketLog", "viewModel Offer Accepted!!!!!!!")
                GetActiveOrdersUiState.OfferAccepted(result.data)
            }

            is GetActiveOrdersUiState.OrderCanceled -> {
                GetActiveOrdersUiState.OrderCanceled(result.rideId)
            }

            is GetActiveOrdersUiState.OfferRejected -> {
                GetActiveOrdersUiState.OfferRejected(result.rideUUID)
            }

            else -> {
                GetActiveOrdersUiState.Loading
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        GetActiveOrdersUiState.Loading
    )

    private fun disconnect() {
        CoroutineScope(Dispatchers.IO).launch {
            closeDriverWebSocketConnectionUseCase.close()
        }
    }

    private var _rideCanceledResult = MutableSharedFlow<RideCancelUiState>()
    val rideCanceledResult = _rideCanceledResult.asSharedFlow()
    fun cancelRide(rideId: Int, cancelCauseId: Int) {
        viewModelScope.launch {
            _rideCanceledResult.emit(RideCancelUiState.Loading)
            cancelRideUseCase(rideId, cancelCauseId).let { result ->
                when (result) {
                    is Result.Error -> {
                        _rideCanceledResult.emit(RideCancelUiState.Error(result.message))
                    }

                    is Result.Success -> {
                        _rideCanceledResult.emit(RideCancelUiState.Success)
                    }
                }
            }
        }
    }

    private var _updateRideStatusResult = MutableSharedFlow<RideUpdateUiState>()
    val updateRideStatusResult = _updateRideStatusResult.asSharedFlow()
    fun updateRideStatus(rideId: Int, status: String) {
        viewModelScope.launch {
            updateRideStatusUseCase(rideId, status).let { result ->
                when (result) {
                    is Result.Error -> {
                        _updateRideStatusResult.emit(RideUpdateUiState.Error(result.message))
                    }

                    is Result.Success -> {
                        _updateRideStatusResult.emit(RideUpdateUiState.Success(result.data?.asUI()))
                    }
                }
            }
        }
    }

    private fun addOrder(order: OrderItem) {
        _ordersListState.value = _ordersListState.value.toMutableList().apply { add(order) }
    }

    private fun removeOrder(rideId: String) {
        _ordersListState.value = _ordersListState.value.filterNot { it.uuid == rideId }
    }

    private var _getCancelCausesResult = MutableSharedFlow<CancelRideUiState>()
    val getCancelCausesResult = _getCancelCausesResult.asSharedFlow()
    fun getCancelCauses() {
        viewModelScope.launch {
            _getCancelCausesResult.emit(CancelRideUiState.Loading)
            getCancelCausesUseCase().let { result ->
                when (result) {
                    is Result.Error -> {
                        _getCancelCausesResult.emit(CancelRideUiState.Error(result.message))
                    }

                    is Result.Success -> {
                        _getCancelCausesResult.emit(CancelRideUiState.Success(result.data.map {
                            CancelItem(
                                id = it.id,
                                title = it.name
                            )
                        }))
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
    data class OfferAccepted(val data: OrderItem) : GetActiveOrdersUiState
    data class Error(val message: String) : GetActiveOrdersUiState
}

sealed interface GetStartedRideStatusUiState {
    data object RideCanceled : GetStartedRideStatusUiState
    data class Error(val message: String) : GetStartedRideStatusUiState
}

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Success(val driverProfile: DriverInfo) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}

sealed interface RideCancelUiState {
    data object Loading : RideCancelUiState
    data object Success : RideCancelUiState
    data class Error(val message: String) : RideCancelUiState
}

sealed interface RideUpdateUiState {
    data class Success(val data: RideCompletedUI?) : RideUpdateUiState
    data class Error(val message: String) : RideUpdateUiState
}

sealed interface CancelRideUiState {
    data object Loading : CancelRideUiState
    data class Success(val data: List<CancelItem>) : CancelRideUiState
    data class Error(val message: String) : CancelRideUiState
}