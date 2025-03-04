package com.aralhub.araltaxi.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.client.ClientGetActiveRideUseCase
import com.aralhub.araltaxi.core.domain.client.ClientGetSearchRideUseCase
import com.aralhub.araltaxi.core.domain.client.ClientLogOutUseCase
import com.aralhub.araltaxi.core.domain.client.ClientProfileUseCase
import com.aralhub.indrive.core.data.model.client.ClientProfile
import com.aralhub.indrive.core.data.model.ride.ActiveRide
import com.aralhub.indrive.core.data.model.ride.SearchRide
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.ui.model.LocationItem
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SuggestItem
import com.yandex.mapkit.search.SuggestOptions
import com.yandex.mapkit.search.SuggestResponse
import com.yandex.mapkit.search.SuggestSession
import com.yandex.mapkit.search.SuggestType
import com.yandex.runtime.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestViewModel @Inject constructor(
    private val clientProfileUseCase: ClientProfileUseCase,
    private val clientLogOutUseCase: ClientLogOutUseCase,
    private val clientGetActiveRideUseCase: ClientGetActiveRideUseCase,
    private val clientGetSearchRideUseCase: ClientGetSearchRideUseCase
) : ViewModel() {

    private val searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    private val suggestSession: SuggestSession = searchManager.createSuggestSession()
    private val suggestOptions = SuggestOptions().setSuggestTypes(
        SuggestType.GEO.value
                or SuggestType.BIZ.value
                or SuggestType.TRANSIT.value
    )
    private val KarakalpakstanBoundingBox = BoundingBox(
        Point(42.7, 58.4),
        Point( 45.8, 62.2)
    )

    private val _suggestionsUiState = MutableStateFlow<SuggestionsUiState>(SuggestionsUiState.Loading)
    val suggestionsUiState = _suggestionsUiState.asStateFlow()

    fun suggestLocation(query: String) = viewModelScope.launch {
        suggestSession.suggest(query, KarakalpakstanBoundingBox, suggestOptions, object: SuggestSession.SuggestListener {
            override fun onResponse(suggestResponse: SuggestResponse) {

                _suggestionsUiState.value = SuggestionsUiState.Success(suggestResponse.items.map {
                    LocationItem(
                        id = 1,
                        title = it?.title?.text?: "",
                        subtitle = it?.subtitle?.text?: ""
                    ) {}
                })
            }

            override fun onError(error: Error) {
                _suggestionsUiState.value = SuggestionsUiState.Error(error.isValid.toString())
            }
        })
    }

    private var _locationEnabled = MutableStateFlow<Boolean>(false)
    val locationEnabled = _locationEnabled.asStateFlow()
    fun updateLocationEnabled(value: Boolean) {
        _locationEnabled.value = value
    }

    private val _profileUiState = MutableSharedFlow<ProfileUiState>()
    val profileUiState = _profileUiState.asSharedFlow()

    private val _activeRideUiState = MutableSharedFlow<ActiveRideUiState>()
    val activeRideUiState = _activeRideUiState.asSharedFlow()

    private val _searchRideUiState = MutableSharedFlow<SearchRideUiState>()
    val searchRideUiState = _searchRideUiState.asSharedFlow()

    private val _logOutUiState = MutableSharedFlow<LogOutUiState>()
    val logOutUiState = _logOutUiState.asSharedFlow()


    fun getProfile() = viewModelScope.launch {
        _profileUiState.emit(ProfileUiState.Loading)
        _profileUiState.emit(clientProfileUseCase().let {
            when (it) {
                is Result.Error -> ProfileUiState.Error(it.message)
                is Result.Success -> ProfileUiState.Success(it.data.copy(profilePhoto = "https://araltaxi.aralhub.uz/${it.data.profilePhoto}"))
            }
        })
    }

    fun logOut() = viewModelScope.launch {
        _logOutUiState.emit(LogOutUiState.Loading)
        _logOutUiState.emit(clientLogOutUseCase().let {
            when (it) {
                is Result.Error -> LogOutUiState.Error(it.message)
                is Result.Success -> LogOutUiState.Success
            }
        })
    }


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

sealed interface ProfileUiState {
    data class Success(val profile: ClientProfile) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
    data object Loading : ProfileUiState
}

sealed interface LogOutUiState {
    data object Success : LogOutUiState
    data class Error(val message: String) : LogOutUiState
    data object Loading : LogOutUiState
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

sealed interface SuggestionsUiState {
    data class Success(val suggestions: List<LocationItem>) : SuggestionsUiState
    data class Error(val message: String) : SuggestionsUiState
    data object Loading : SuggestionsUiState
}
