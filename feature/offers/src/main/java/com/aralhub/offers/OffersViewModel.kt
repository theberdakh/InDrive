package com.aralhub.offers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientAcceptOfferUseCase
import com.aralhub.araltaxi.core.domain.client.ClientDeclineOfferUseCase
import com.aralhub.araltaxi.core.domain.client.ClientGetOffersUseCase
import com.aralhub.indrive.core.data.model.offer.Offer
import com.aralhub.indrive.core.data.util.ClientWebSocketEvent
import com.aralhub.ui.model.OfferItem
import com.aralhub.ui.model.OfferItemDriver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.aralhub.indrive.core.data.result.Result

@HiltViewModel
class OffersViewModel @Inject constructor(
    private val getOffersUseCase: ClientGetOffersUseCase,
    private val acceptOfferUseCase: ClientAcceptOfferUseCase,
    private val declineOfferUseCase: ClientDeclineOfferUseCase
) : ViewModel() {
    private var _offersUiState = MutableStateFlow<OffersUiState>(OffersUiState.Loading)
    val offersUiState = _offersUiState.asSharedFlow()

    private var offers = mutableListOf<Offer>()
    private val declinedOfferIds = mutableListOf<String>()
    fun getOffers() = viewModelScope.launch {
        _offersUiState.emit(OffersUiState.Loading)
        getOffersUseCase().collect {
            when (it) {
                is ClientWebSocketEvent.DriverOffer -> {
                    val newOffer = it.offer

                    // If it's a declined offer, don't add it
                    if (declinedOfferIds.contains(newOffer.offerId)) {
                        return@collect
                    } else if (!offers.any { offer -> offer.offerId == newOffer.offerId }) {
                        // Only add if it's not already in the list
                        offers.add(newOffer)
                    }
                    // Clean up any offers that might be in the declined list
                    offers.removeIf { offer -> declinedOfferIds.contains(offer.offerId) }
                    // Emit current state
                    _offersUiState.emit(OffersUiState.Success(offers.map { offer -> offer.asOfferItem() }))
                }

                is ClientWebSocketEvent.Unknown -> {
                    _offersUiState.emit(OffersUiState.Error(it.error))
                }
            }
        }
    }

    private var _acceptOfferUiState = MutableSharedFlow<AcceptOfferUiState>()
    val acceptOfferUiState = _acceptOfferUiState.asSharedFlow()
    fun acceptOffer(offerId: String) = viewModelScope.launch {
        acceptOfferUseCase.invoke(offerId).let {
            when (it) {
                is Result.Success -> {
                    _acceptOfferUiState.emit(AcceptOfferUiState.Success)
                }
                is Result.Error -> {
                    _acceptOfferUiState.emit(AcceptOfferUiState.Error(it.message))
                }
            }
        }
    }

    private var _declineOfferUiState = MutableSharedFlow<DeclineOfferUiState>()
    val declineOfferUiState = _declineOfferUiState.asSharedFlow()
    fun declineOffer(offerId: String, position: Int) = viewModelScope.launch {
        declineOfferUseCase.invoke(offerId).let {
            when (it) {
                is Result.Success -> {
                    declinedOfferIds.add(offerId)
                    offers.removeIf { offer -> offer.offerId == offerId }
                    _offersUiState.emit(OffersUiState.Success(offers.map { offer -> offer.asOfferItem() }))
                    _declineOfferUiState.emit(DeclineOfferUiState.Success(position))
                }
                is Result.Error -> {
                    _declineOfferUiState.emit(DeclineOfferUiState.Error(it.message))
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
            rating = driver.rating,
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

sealed interface AcceptOfferUiState {
    data object Loading : AcceptOfferUiState
    data object Success : AcceptOfferUiState
    data class Error(val message: String) : AcceptOfferUiState
}

sealed interface DeclineOfferUiState {
    data object Loading : DeclineOfferUiState
    data class Success(val position: Int) : DeclineOfferUiState
    data class Error(val message: String) : DeclineOfferUiState
}