package com.aralhub.offers.sheet.standard.changeorcancel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientCancelSearchRideUseCase
import com.aralhub.araltaxi.core.domain.client.ClientGetSearchRideUseCase
import com.aralhub.araltaxi.core.domain.client.ClientUpdateAutoTakeUseCase
import com.aralhub.indrive.core.data.model.ride.SearchRide
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeOrCancelRequestViewModel @Inject constructor(
    private val cancelSearchRideUseCase: ClientCancelSearchRideUseCase,
    private val clientGetSearchRideUseCase: ClientGetSearchRideUseCase,
    private val updateAutoTakeUseCase: ClientUpdateAutoTakeUseCase
) : ViewModel() {

    private val _updateAutoTakeUiState = MutableSharedFlow<UpdateAutoTakeUiState>()
    val updateAutoTakeUiState = _updateAutoTakeUiState.asSharedFlow()

    fun updateAutoTake(rideId: String, autoTake: Boolean) = viewModelScope.launch{
        updateAutoTakeUseCase(rideId, autoTake).let {
            _updateAutoTakeUiState.emit(when(it){
                is Result.Error -> UpdateAutoTakeUiState.Error(it.message)
                is Result.Success -> UpdateAutoTakeUiState.Success
            })
        }
    }


    private val _searchRideUiState = MutableSharedFlow<SearchRideUiState>()
    val searchRideUiState = _searchRideUiState.asSharedFlow()

    fun getSearchRide() = viewModelScope.launch{
        clientGetSearchRideUseCase().let { searchRideResult ->
            _searchRideUiState.emit(when(searchRideResult){
                is Result.Error ->  SearchRideUiState.Error(searchRideResult.message)
                is Result.Success -> SearchRideUiState.Success(searchRideResult.data)
            })
        }
    }

    private val _cancelSearchRideUiState = MutableStateFlow<CancelSearchRideUiState>(CancelSearchRideUiState.Loading)
    val cancelSearchRideUiState = _cancelSearchRideUiState.asStateFlow()

    fun cancelSearchRide() = viewModelScope.launch{
        clientGetSearchRideUseCase().let { searchRideResult ->
            _cancelSearchRideUiState.value =  when(searchRideResult){
                is Result.Error ->  CancelSearchRideUiState.Error(searchRideResult.message)
                is Result.Success -> cancelSearchRideUseCase(searchRideResult.data.uuid).let {
                    when(it){
                        is Result.Error -> CancelSearchRideUiState.Error(it.message)
                        is Result.Success -> CancelSearchRideUiState.Success
                    }
                }
            }
        }
    }

}

sealed interface UpdateAutoTakeUiState {
    data object Success : UpdateAutoTakeUiState
    data class Error(val message: String) : UpdateAutoTakeUiState
    data object Loading : UpdateAutoTakeUiState
}

sealed interface SearchRideUiState {
    data class Success(val searchRide: SearchRide) : SearchRideUiState
    data class Error(val message: String) : SearchRideUiState
    data object Loading : SearchRideUiState
}

sealed interface CancelSearchRideUiState {
    data object Success : CancelSearchRideUiState
    data class Error(val message: String) : CancelSearchRideUiState
    data object Loading : CancelSearchRideUiState
}