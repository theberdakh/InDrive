package com.aralhub.araltaxi.ride

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientCancelRideWithReasonUseCase
import com.aralhub.araltaxi.core.domain.client.ClientCancelRideWithoutReasonUseCase
import com.aralhub.araltaxi.core.domain.client.ClientGetActiveRideUseCase
import com.aralhub.indrive.core.data.model.ride.ActiveRide
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RideViewModel @Inject constructor(
    private val getActiveRideUseCase: ClientGetActiveRideUseCase,
    private val cancelRideWithoutReasonUseCase: ClientCancelRideWithoutReasonUseCase,
    private val cancelRideWithReasonUseCase: ClientCancelRideWithReasonUseCase
) : ViewModel() {
    val rideState = getRideState().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = RideBottomSheetUiState.Loading
    )

    private var _rideState = MutableStateFlow<RideBottomSheetUiState>(RideBottomSheetUiState.Loading)
    val rideState2 = _rideState.asStateFlow()

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

    private val _cancelRideState = MutableStateFlow<CancelRideUiState>(CancelRideUiState.Loading)
    val cancelRideState = _cancelRideState.asStateFlow()
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

    init {

        /*  getRideState().onEach {
              _rideState.emit(it)
          }.launchIn(viewModelScope)*/
    }
}


fun getRideState() = flow {
    emit(RideBottomSheetUiState.Success(RideState.WAITING_FOR_DRIVER, cardRideData))
    delay(3000)
    emit(RideBottomSheetUiState.Success(RideState.DRIVER_IS_WAITING, cardRideData))
    delay(3000)
    emit(RideBottomSheetUiState.Success(RideState.IN_RIDE, cardRideData))
    delay(3000)
    emit(RideBottomSheetUiState.Success(RideState.FINISHED, cardRideData))
}

sealed interface RideBottomSheetUiState {
    data object Loading : RideBottomSheetUiState
    data class Success(val rideState: RideState, val rideData: Ride) : RideBottomSheetUiState
    data object Error : RideBottomSheetUiState
}

data class Ride(
    val driver: Driver,
    val car: Car,
    val route: Route,
    val price: String,
    val paymentMethod: PaymentMethod,
    val waitForDriverTime: String,
    val driverWaitTime: String
)

enum class PaymentMethod {
    CASH,
    CARD
}

data class Route(
    val start: String,
    val end: String,
    val time: String
)

data class Driver(
    val name: String,
    val phone: String,
    val rating: Float,
    val avatar: String,
    val cardNumber: String
)

data class Car(
    val model: String,
    val number: String
)

enum class RideState {
    WAITING_FOR_DRIVER,
    DRIVER_IS_WAITING,
    DRIVER_CANCELED,
    IN_RIDE,
    FINISHED
}

val cardRideData = Ride(
    driver = Driver(
        name = "John Doe",
        phone = "+1234567890",
        rating = 4.5f,
        avatar = "https://www.example.com/avatar.jpg",
        cardNumber = "1234 5678 9012 3456"
    ),
    car = Car(
        model = "Toyota Camry",
        number = "A123BC"
    ),
    route = Route(
        start = "Moscow, Russia",
        end = "Saint Petersburg, Russia",
        time = "1 hour 30 minutes"
    ),
    price = "1000 RUB",
    paymentMethod = PaymentMethod.CARD,
    driverWaitTime = "2 minutes",
    waitForDriverTime = "5 minutes"
)

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