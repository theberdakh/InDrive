package com.aralhub.offers.sheet.standard.changeprice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientChangeSearchPriceUseCase
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePriceViewModel @Inject constructor(private val changeSearchPriceUseCase: ClientChangeSearchPriceUseCase): ViewModel(){

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

}

sealed interface ChangePriceUiState {
    data object Loading : ChangePriceUiState
    data class Success(val data: Any) : ChangePriceUiState
    data class Error(val message: String) : ChangePriceUiState
}