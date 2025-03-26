package com.aralhub.araltaxi.select_location

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.select_location.utils.getPointsAt10MeterRadius
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.LinearRing
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polygon
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.search.Address
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.search.Session.SearchListener
import com.yandex.mapkit.search.ToponymObjectMetadata
import com.yandex.runtime.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SelectLocationViewModel @Inject constructor() : ViewModel() {
    private val searchManager =
        SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    private val point = MutableStateFlow(defaultPoint)
    private var searchSession: Session? = null
    private val searchState = MutableStateFlow<SearchState>(SearchState.Off)
    private val region = MutableStateFlow<VisibleRegion?>(null)

    @OptIn(FlowPreview::class)
    private val throttledRegion = region.debounce(1.seconds)
    private val locationIsValid = MutableStateFlow(false)

    val uiState: StateFlow<SelectLocationUiState> =
        combine(point, searchState) { point, searchState ->
            SelectLocationUiState(
                point = point,
                searchState = searchState
            )
        }.stateIn(viewModelScope, SharingStarted.Lazily, SelectLocationUiState(defaultPoint))

    fun submitLocation(point: Point, zoom: Int, visibleRegion: VisibleRegion) {

        this.point.value = point
        val locations = getPointsAt10MeterRadius(point.latitude, point.longitude)
        val polygonPoints = arrayListOf(
            Point(locations[0].lat, locations[0].lon),
            Point(locations[1].lat, locations[1].lon),
            Point(locations[2].lat, locations[2].lon),
            Point(locations[3].lat, locations[3].lon),
        )
        Log.i("Locations", "$locations")
        val polygons = Geometry.fromPolygon(
            Polygon(LinearRing(polygonPoints), ArrayList())
        )
        val polyLine = Polyline(polygonPoints)

        val regions  = VisibleRegion( Point(locations[0].lat, locations[0].lon), Point(locations[1].lat, locations[1].lon), Point(locations[2].lat, locations[2].lon), Point(locations[3].lat, locations[3].lon))
       // searchSession?.setSearchArea(polygons)
        //searchManager.submit("", Polyline(polygonPoints), Geometry.fromPoint(point), searchOptions, searchListener)
      //  searchManager.submit("жилой дом", Geometry.fromPoint(point), searchOptions, searchListener)
      //  searchManager.submit("", polyLine, Geometry.fromPolyline(polyLine), searchOptions, searchListener)
            // searchManager.submit("жилой дом", VisibleRegionUtils.toPolygon(regions), searchOptions, searchListener)
       searchManager.submit(point, zoom, searchOptions, searchListener)
    }

    private val _locationSelectedUiState = MutableSharedFlow<LocationSelectedUiState>()
    val locationSelectedUiState = _locationSelectedUiState.asSharedFlow()
    fun selectLocation(title: String, subtitle: String, point: Point) = viewModelScope.launch {
        _locationSelectedUiState.emit(LocationSelectedUiState.Success(title, subtitle, point))
    }


    fun setVisibleRegion(region: VisibleRegion) {
        VisibleRegion()
        this.region.value = region
    }

    fun subscribeForSearch(): Flow<*> {
        return throttledRegion.filter { it != null }
            .filter { searchState.value is SearchState.Success }
            .mapNotNull { it }
            .onEach { visibleRegion ->
                searchSession?.let {
                    it.setSearchArea(VisibleRegionUtils.toPolygon(visibleRegion))
                    it.resubmit(searchListener)
                    searchState.value = SearchState.Loading
                }
            }
    }

    private val searchListener = object : SearchListener {
        override fun onSearchResponse(response: Response) {
            val items = response.collection.children.mapNotNull {
                val addresses = it.obj?.metadataContainer?.getItem(ToponymObjectMetadata::class.java)?.address?.components!!.filter { comp -> comp.kinds.contains(Address.Component.Kind.HOUSE) }
                for(address in addresses){
                    Log.i("Address", "${address.name}")
                }
                val point = it.obj?.geometry?.firstOrNull()?.point ?: return@mapNotNull null
                SearchResponseItem(point, it.obj)
            }
            val boundingBox = response.metadata.boundingBox ?: return
            searchState.value = SearchState.Success(
                items = items,
                zoomToItems = true,
                itemsBoundingBox = boundingBox
            )
        }

        override fun onSearchError(error: Error) {
            Log.i("Search Response", "Error")
            searchState.value = SearchState.Error
        }
    }

    companion object {
        private val searchOptions = SearchOptions().apply {
            searchTypes = SearchType.GEO.value
            resultPageSize = 1
        }
        private val defaultPoint = Point(42.4651, 59.6136)

        private const val MIN_LAT = 41.0
        private const val MAX_LAT = 44.0
        private const val MIN_LON = 56.0
        private const val MAX_LON = 61.0

    }
}

sealed interface LocationSelectedUiState {
    data class Success(val title: String, val subtitle: String, val point: Point) :
        LocationSelectedUiState

    data object Error : LocationSelectedUiState
    data object Loading : LocationSelectedUiState
}