package com.aralhub.araltaxi.ride.sheet.modal.cause

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.cancel.GetAllCancelCausesUseCase
import com.aralhub.indrive.core.data.model.cancel.CancelCause
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.ui.model.CancelItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReasonCancelViewModel @Inject constructor(
    private val getAllCancelCausesUseCase: GetAllCancelCausesUseCase
): ViewModel() {

    private val _reasonCancelUiState = MutableSharedFlow<ReasonCancelUiState>()
    val reasonCancelUiState = _reasonCancelUiState.asSharedFlow()
    fun getCancelCauses() = viewModelScope.launch {
        _reasonCancelUiState.emit(ReasonCancelUiState.Loading)
        getAllCancelCausesUseCase().let { result ->
            when(result){
                is Result.Error -> _reasonCancelUiState.emit(ReasonCancelUiState.Error(result.message))
                is Result.Success -> _reasonCancelUiState.emit(ReasonCancelUiState.Success(result.data.filter {
                    it.isActive && it.type == "by_user"
                }.map {
                    it.toCancelItem()
                }))
            }
        }
    }
}

sealed interface ReasonCancelUiState {
    data object Loading: ReasonCancelUiState
    data class Success(val causes: List<CancelItem>): ReasonCancelUiState
    data class Error(val message: String): ReasonCancelUiState
}

fun CancelCause.toCancelItem() = CancelItem(id, name)