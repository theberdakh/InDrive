package com.aralhub.araltaxi.request

import android.util.Log
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
import com.aralhub.ui.model.LocationItemClickOwner
import com.aralhub.ui.model.args.LocationType
import com.aralhub.ui.model.args.SelectedLocation
import com.aralhub.ui.model.args.SelectedLocations
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session.SearchListener
import com.yandex.mapkit.search.SuggestOptions
import com.yandex.mapkit.search.SuggestResponse
import com.yandex.mapkit.search.SuggestSession
import com.yandex.mapkit.search.SuggestType
import com.yandex.runtime.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val searchManager =
        SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    private val searchOptions = SearchOptions().apply {
        searchTypes = SearchType.GEO.value
        resultPageSize = 1
    }

    private val suggestSession: SuggestSession = searchManager.createSuggestSession()
    private val suggestOptions = SuggestOptions().setSuggestTypes(
                SuggestType.BIZ.value
    )

    private companion object {
        const val MIN_LAT = 41.0
        const val MAX_LAT = 44.0
        const val MIN_LON = 56.0
        const val MAX_LON = 61.0
    }

    private val KarakalpakstanBoundingBox = BoundingBox(
        Point(MIN_LAT, MIN_LON),
        Point(MAX_LAT, MAX_LON)
    )

    private val _suggestionsUiState =
        MutableStateFlow<SuggestionsUiState>(SuggestionsUiState.Loading)
    val suggestionsUiState = _suggestionsUiState.asStateFlow()

    fun suggestLocation(query: String, locationItemClickOwner: LocationItemClickOwner) =
        viewModelScope.launch {
            suggestSession.suggest(
                query,
                KarakalpakstanBoundingBox,
                suggestOptions,
                object : SuggestSession.SuggestListener {
                    override fun onResponse(suggestResponse: SuggestResponse) {
                        _suggestionsUiState.value =
                            SuggestionsUiState.Success(suggestResponse.items.filter { suggestItem ->
                                suggestItem.center?.let {
                                    it.latitude < MAX_LAT  || it.longitude < MAX_LON
                                }?: false
                            }.map {
                                LocationItem(
                                    id = 1,
                                    title = it?.title?.text ?: "",
                                    subtitle = it?.subtitle?.text ?: "",
                                    longitude = it?.center?.longitude ?: 0.0,
                                    latitude = it?.center?.latitude ?: 0.0,
                                    clickOwner = locationItemClickOwner
                                )
                            })
                    }

                    override fun onError(error: Error) {
                        _suggestionsUiState.value =
                            SuggestionsUiState.Error(error.isValid.toString())
                    }
                })
        }

    private var _locationEnabled = MutableStateFlow(false)
    val locationEnabled = _locationEnabled.asStateFlow()
    fun updateLocationEnabled(value: Boolean) {
        _locationEnabled.value = value
    }

    private val _fromLocationUiState = MutableSharedFlow<FromLocationUiState>()
    val fromLocationUiState = _fromLocationUiState.asSharedFlow()
    private var lastLocation = Point(0.0, 0.0)

    fun getFromLocation(latitude: Double, longitude: Double ) = viewModelScope.launch {
        if (lastLocation.latitude == latitude && lastLocation.longitude == longitude) {
            return@launch
        } else {
            lastLocation = Point(latitude, longitude)
            searchName(lastLocation.latitude, lastLocation.longitude)
        }
    }

    private fun searchName(latitude: Double, longitude: Double) {
        searchManager.submit(Point(latitude, longitude), 17, searchOptions, object : SearchListener {
            override fun onSearchResponse(response: Response) {
                val geoObjects = response.collection.children.mapNotNull { it.obj }
                val names = geoObjects.filter { it.name != null }.map { it.name }
                if (names.isNotEmpty()) {
                    Log.i("Log", "Names: $names")
                    viewModelScope.launch {
                        _fromLocationUiState.emit(
                            FromLocationUiState.Success(
                                SelectedLocation(locationType = LocationType.FROM,
                                    name = names[0] ?: "Anıqlap bolmadı",
                                    latitude = latitude,
                                    longitude = longitude)
                            )
                        )
                    }
                } else {
                    viewModelScope.launch {
                        _fromLocationUiState.emit(FromLocationUiState.Error("Unknown Location"))
                    }
                }
            }

            override fun onSearchError(error: Error) {
                viewModelScope.launch {
                    _fromLocationUiState.emit(FromLocationUiState.Error("Error to find location name"))
                }
            }
        })
    }

    private val _selectedLocations = MutableStateFlow<SelectedLocations?>(null)
    val selectedLocations: StateFlow<SelectedLocations?> = _selectedLocations.asStateFlow()

    private val _fromLocation = MutableStateFlow<SelectedLocation?>(null)
    private val _toLocation = MutableStateFlow<SelectedLocation?>(null)

    fun updateLocation(location: SelectedLocation) = viewModelScope.launch {
        when (location.locationType) {
            LocationType.FROM -> {
                _fromLocation.value = location
            }

            LocationType.TO -> _toLocation.value = location
        }

        val from = _fromLocation.value
        val to = _toLocation.value
        if (from != null && to != null) {
            _selectedLocations.value = SelectedLocations(from, to)
        }
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

    fun getSearchRide() = viewModelScope.launch {
        _searchRideUiState.emit(SearchRideUiState.Loading)
        _searchRideUiState.emit(clientGetSearchRideUseCase().let {
            when (it) {
                is Result.Error -> SearchRideUiState.Error(it.message)
                is Result.Success -> SearchRideUiState.Success(it.data)
            }
        })
    }


    fun getActiveRide() = viewModelScope.launch {
        _activeRideUiState.emit(ActiveRideUiState.Loading)
        _activeRideUiState.emit(clientGetActiveRideUseCase().let {
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

sealed interface FromLocationUiState {
    data class Success(val location: SelectedLocation) : FromLocationUiState
    data class Error(val message: String) : FromLocationUiState
    data object Loading : FromLocationUiState
}
