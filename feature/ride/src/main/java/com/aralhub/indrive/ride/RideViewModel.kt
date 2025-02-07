package com.aralhub.indrive.ride

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RideViewModel @Inject constructor() : ViewModel() {
    val rideState = getRideState().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = RideBottomSheetUiState.Loading
    )
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

fun getRideState() = flow {
    emit(RideBottomSheetUiState.Loading)
    delay(10000)
    emit(RideBottomSheetUiState.Success(RideState.WAITING_FOR_DRIVER, cardRideData))
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