package com.aralhub.offers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientGetOffersUseCase
import com.aralhub.indrive.core.data.model.offer.Offer
import com.aralhub.indrive.core.data.util.ClientWebSocketEvent
import com.aralhub.ui.model.OfferItem
import com.aralhub.ui.model.OfferItemDriver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OffersViewModel @Inject constructor(
    private val getOffersUseCase: ClientGetOffersUseCase
) : ViewModel() {
    private var _offersUiState = MutableStateFlow<OffersUiState>(OffersUiState.Loading)
    val offersUiState = _offersUiState.asSharedFlow()

    private var offers = mutableListOf<Offer>()

    fun getOffers() = viewModelScope.launch {
        _offersUiState.emit(OffersUiState.Loading)
        getOffersUseCase().collect {
            when (it) {
                is ClientWebSocketEvent.DriverOffer -> {
                    offers.add(it.offer)
                    _offersUiState.emit(OffersUiState.Success(offers.map { offer -> offer.asOfferItem() }))
                }

                is ClientWebSocketEvent.Unknown -> {
                    _offersUiState.emit(OffersUiState.Error(it.error))
                }
            }
        }
    }

}

private fun Offer.asOfferItem(): OfferItem {
    return OfferItem(
        id = offerId,
        driver = OfferItemDriver(
            id = driver.driverId,
            name = driver.fullName,
            carName = driver.vehicleType.kk,
            rating = driver.rating.toFloat(),
            avatar = ""
        ),
        offeredPrice = amount.toString(),
        timeToArrive = "0"
    )
}

sealed interface OffersUiState {
    data object Loading : OffersUiState
    data class Success(val offers: List<OfferItem>) : OffersUiState
    data class Error(val message: String) : OffersUiState
}