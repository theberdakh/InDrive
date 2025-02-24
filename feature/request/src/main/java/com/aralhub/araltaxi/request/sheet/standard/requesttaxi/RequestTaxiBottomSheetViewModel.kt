package com.aralhub.araltaxi.request.sheet.standard.requesttaxi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientGetRecommendedPriceUseCase
import com.aralhub.indrive.core.data.model.client.GeoPoint
import com.aralhub.indrive.core.data.model.client.RecommendedPrice
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestTaxiBottomSheetViewModel @Inject constructor(private val clientNetworkClientGetRecommendedPriceUseCase: ClientGetRecommendedPriceUseCase): ViewModel() {

    private var _requestTaxiBottomSheetUiState = MutableSharedFlow<RequestTaxiBottomSheetUiState>()
    val requestTaxiBottomSheetUiState = _requestTaxiBottomSheetUiState.asSharedFlow()

    fun getRecommendedPrice(points: List<GeoPoint>) = viewModelScope.launch {
        _requestTaxiBottomSheetUiState.emit(RequestTaxiBottomSheetUiState.Loading)
        clientNetworkClientGetRecommendedPriceUseCase.invoke(points).let {
            when(it){
                is Result.Error -> {
                    _requestTaxiBottomSheetUiState.emit(RequestTaxiBottomSheetUiState.Error(it.message))
                }
                is Result.Success -> {
                    _requestTaxiBottomSheetUiState.emit(RequestTaxiBottomSheetUiState.Success(it.data))
                }      }
        }
    }
}

sealed interface RequestTaxiBottomSheetUiState {
    data class Success(val recommendedPrice: RecommendedPrice): RequestTaxiBottomSheetUiState
    data class Error(val message: String): RequestTaxiBottomSheetUiState
    data object Loading: RequestTaxiBottomSheetUiState
}