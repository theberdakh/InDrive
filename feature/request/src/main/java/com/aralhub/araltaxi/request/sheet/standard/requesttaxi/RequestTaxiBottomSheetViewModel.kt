package com.aralhub.araltaxi.request.sheet.standard.requesttaxi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientGetActiveRideUseCase
import com.aralhub.araltaxi.core.domain.client.ClientGetSearchRideUseCase
import com.aralhub.indrive.core.data.model.ride.ActiveRide
import com.aralhub.indrive.core.data.model.ride.SearchRide
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestTaxiBottomSheetViewModel @Inject constructor(
    private val clientGetActiveRideUseCase: ClientGetActiveRideUseCase,
    private val clientGetSearchRideUseCase: ClientGetSearchRideUseCase
) : ViewModel() {

    private val _activeRideUiState = MutableSharedFlow<ActiveRideUiState>()
    val activeRideUiState = _activeRideUiState.asSharedFlow()

    private val _searchRideUiState = MutableSharedFlow<SearchRideUiState>()
    val searchRideUiState = _searchRideUiState.asSharedFlow()

    fun getSearchRide(userId: Int) = viewModelScope.launch {
        _searchRideUiState.emit(SearchRideUiState.Loading)
        _searchRideUiState.emit(clientGetSearchRideUseCase(userId).let {
            when (it) {
                is Result.Error -> SearchRideUiState.Error(it.message)
                is Result.Success -> SearchRideUiState.Success(it.data)
            }
        })
    }


    fun getActiveRide(userId: Int) = viewModelScope.launch {
        _activeRideUiState.emit(ActiveRideUiState.Loading)
        _activeRideUiState.emit(clientGetActiveRideUseCase(userId).let {
            when (it) {
                is Result.Error -> ActiveRideUiState.Error(it.message)
                is Result.Success -> ActiveRideUiState.Success(it.data)
            }
        })
    }


}

sealed interface SearchRideUiState {
    data class Success(val searchRide: SearchRide) : SearchRideUiState
    data class Error(val message: String) : SearchRideUiState
    data object Loading : SearchRideUiState
}



sealed interface ActiveRideUiState {
    data class Success(val activeRide: ActiveRide) : ActiveRideUiState
    data class Error(val message: String) : ActiveRideUiState
    data object Loading : ActiveRideUiState
}
