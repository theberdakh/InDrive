package com.aralhub.offers.sheet.modal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.cancel.GetAllCancelCausesUseCase
import com.aralhub.offers.model.CancelItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.aralhub.indrive.core.data.result.Result


@HiltViewModel
class ReasonCancelModalBottomSheetViewModel @Inject constructor(private val clientCancelCausesUseCase: GetAllCancelCausesUseCase): ViewModel() {

    private val _cancelRideUiState = MutableSharedFlow<CancelRideUiState>()
    val cancelRideUiState = _cancelRideUiState.asSharedFlow()

    fun getCancelCauses() = viewModelScope.launch {
        _cancelRideUiState.emit(CancelRideUiState.Loading)
        _cancelRideUiState.emit(clientCancelCausesUseCase().let {
            when (it) {
                is Result.Error -> CancelRideUiState.Error(it.message)
                is Result.Success -> CancelRideUiState.Success(it.data.filter { cancelCause -> cancelCause.type == "by_user" }.map { cancelCause ->
                    CancelItem(cancelCause.id, cancelCause.name)
                })
            }
        })
    }
}

sealed interface CancelRideUiState {
    data class Success(val cancelCauses: List<CancelItem>) : CancelRideUiState
    data class Error(val message: String) : CancelRideUiState
    data object Loading : CancelRideUiState
}