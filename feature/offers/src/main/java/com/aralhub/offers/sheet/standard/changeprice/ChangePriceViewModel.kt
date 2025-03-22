package com.aralhub.offers.sheet.standard.changeprice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientChangeSearchPriceUseCase
import com.aralhub.araltaxi.core.domain.client.ClientGetSearchRideUseCase
import com.aralhub.indrive.core.data.model.ride.SearchRide
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePriceViewModel @Inject constructor(private val changeSearchPriceUseCase: ClientChangeSearchPriceUseCase,
    private val searchRideUseCase: ClientGetSearchRideUseCase): ViewModel(){

    private var _changePriceUiState = MutableSharedFlow<ChangePriceUiState>()
    val changePriceUiState = _changePriceUiState.asSharedFlow()
    fun changePrice(rideId: String, amount: Number) = viewModelScope.launch{
        _changePriceUiState.emit(changeSearchPriceUseCase(rideId, amount).let {
          when(it){
              is Result.Error -> ChangePriceUiState.Error(it.message)
              is Result.Success -> ChangePriceUiState.Success(it.data)
          }
        })
    }

    private var _searchRideUiState = MutableSharedFlow<SearchRideUiState>()
    val searchRideUiState = _searchRideUiState.asSharedFlow()
    fun getSearchRide() = viewModelScope.launch{
        searchRideUseCase().let {
           _searchRideUiState.emit(when(it){
                is Result.Error -> SearchRideUiState.Error(it.message)
                is Result.Success -> SearchRideUiState.Success(it.data)
            })
        }
    }

}

sealed interface ChangePriceUiState {
    data object Loading : ChangePriceUiState
    data class Success(val data: Any) : ChangePriceUiState
    data class Error(val message: String) : ChangePriceUiState
}

sealed interface SearchRideUiState {
    data object Loading : SearchRideUiState
    data class Success(val data: SearchRide) : SearchRideUiState
    data class Error(val message: String) : SearchRideUiState
}