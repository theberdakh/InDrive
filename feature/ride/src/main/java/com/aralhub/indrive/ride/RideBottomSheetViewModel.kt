package com.aralhub.indrive.ride

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RideBottomSheetViewModel @Inject constructor(): ViewModel() {
    val rideState = getRideState().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = RideBottomSheetUiState.Loading
    )
}

fun getRideState() = flow {
    emit(RideBottomSheetUiState.Loading)
    kotlinx.coroutines.delay(10000)
    emit(RideBottomSheetUiState.Success(RideState.WAITING_FOR_DRIVER))
    kotlinx.coroutines.delay(10000)
    emit(RideBottomSheetUiState.Success(RideState.DRIVER_IS_WAITING))
    kotlinx.coroutines.delay(10000)
    emit(RideBottomSheetUiState.Success(RideState.IN_RIDE))
    kotlinx.coroutines.delay(10000)
    emit(RideBottomSheetUiState.Success(RideState.FINISHED))
}

sealed interface RideBottomSheetUiState {
    data object Loading: RideBottomSheetUiState
    data class Success(
        val rideState: RideState
    ): RideBottomSheetUiState
    data object Error: RideBottomSheetUiState
}

enum class RideState {
    WAITING_FOR_DRIVER,
    DRIVER_IS_WAITING,
    DRIVER_CANCELED,
    IN_RIDE,
    FINISHED
}