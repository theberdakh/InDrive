package com.aralhub.araltaxi.request.sheet.standard.sendorder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientCreateRideUseCase
import com.aralhub.araltaxi.core.domain.payment.GetActivePaymentMethodUseCase
import com.aralhub.indrive.core.data.model.client.ClientRide
import com.aralhub.indrive.core.data.model.client.ClientRideRequest
import com.aralhub.indrive.core.data.model.client.ClientRideRequestLocations
import com.aralhub.indrive.core.data.model.client.ClientRideRequestLocationsItems
import com.aralhub.indrive.core.data.model.client.ClientRideRequestLocationsItemsCoordinates
import com.aralhub.indrive.core.data.model.client.ClientRideRequestRecommendedAmount
import com.aralhub.indrive.core.data.model.payment.PaymentMethod
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendOrderBottomSheetViewModel @Inject constructor(private val useCase: ClientCreateRideUseCase,
    private val getActivePaymentMethodUseCase: GetActivePaymentMethodUseCase) :
    ViewModel() {

    private var _sendOrderBottomSheetUiState = MutableSharedFlow<SendOrderBottomSheetUiState>()
    val sendOrderBottomSheetUiState = _sendOrderBottomSheetUiState.asSharedFlow()

    fun createRide() = viewModelScope.launch {
        _sendOrderBottomSheetUiState.emit(SendOrderBottomSheetUiState.Loading)
        useCase.invoke(
            ClientRideRequest(
                passengerId = 39,
                baseAmount = 3000,
                recommendedAmount = ClientRideRequestRecommendedAmount(
                    minAmount = 2000,
                    maxAmount = 4000,
                    recommendedAmount = 3000
                ),

                /*
        // 42.482461, 59.613237
        // 42.466078, 59.611981*/
                locations = ClientRideRequestLocations(
                    points = listOf(
                        ClientRideRequestLocationsItems(
                            coordinates = ClientRideRequestLocationsItemsCoordinates(
                                longitude = 42.482461,
                                latitude = 59.613237
                            ),
                            name = "Nukus"
                        ), ClientRideRequestLocationsItems(
                            coordinates = ClientRideRequestLocationsItemsCoordinates(
                                longitude = 42.466078,
                                latitude = 59.611981
                            ),
                            name = "Nukus"
                        )
                    )
                ),
                comment = "",
                autoTake = false,
                paymentId = 2,
                optionIds = listOf(1),
                cancelCauseId = null
            )
        ).let {
            when (it) {
                is Result.Error -> {
                    _sendOrderBottomSheetUiState.emit(SendOrderBottomSheetUiState.Error(it.message))
                    Log.i("SendOrderBottomSheetViewModel", "Error: ${it.message}")
                }

                is Result.Success -> {
                    _sendOrderBottomSheetUiState.emit(SendOrderBottomSheetUiState.Success(it.data))
                    Log.i("SendOrderBottomSheetViewModel", "Success: ${it.data}")
                }
            }
        }
    }

    private var _activePaymentMethodUiState = MutableSharedFlow<ActivePaymentMethodUiState>()
    val activePaymentMethodUiState = _activePaymentMethodUiState.asSharedFlow()
    fun getActivePaymentMethod() = viewModelScope.launch {
        _activePaymentMethodUiState.emit(ActivePaymentMethodUiState.Loading)
        getActivePaymentMethodUseCase.invoke().let {
            when (it) {
                is Result.Error -> {
                    _activePaymentMethodUiState.emit(ActivePaymentMethodUiState.Error(it.message))
                    Log.i("SendOrderBottomSheetViewModel", "Error: ${it.message}")
                }

                is Result.Success -> {
                    _activePaymentMethodUiState.emit(ActivePaymentMethodUiState.Success(it.data))
                    Log.i("SendOrderBottomSheetViewModel", "Success: ${it.data}")
                }
            }
        }
    }
}


sealed interface SendOrderBottomSheetUiState {
    data class Success(val clientRide: ClientRide) : SendOrderBottomSheetUiState
    data class Error(val message: String) : SendOrderBottomSheetUiState
    data object Loading : SendOrderBottomSheetUiState
}

sealed interface ActivePaymentMethodUiState {
    data class Success(val paymentMethod: PaymentMethod) : ActivePaymentMethodUiState
    data class Error(val message: String) : ActivePaymentMethodUiState
    data object Loading : ActivePaymentMethodUiState
}