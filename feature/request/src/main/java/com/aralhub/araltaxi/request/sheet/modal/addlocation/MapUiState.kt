package com.aralhub.araltaxi.request.sheet.modal.addlocation

import com.aralhub.araltaxi.request.model.LocationItem
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point

data class MapUiState(
    val query: String = "",
    val searchState: SearchState = SearchState.Off,
    val suggestState: SuggestState = SuggestState.Off,
)

sealed interface SearchState {
    data object Off : SearchState
    data object Loading : SearchState
    data object Error : SearchState
    data class Success(
        val items: List<LocationItem>,
        val zoomToItems: Boolean,
        val itemsBoundingBox: BoundingBox,
    ) : SearchState
}

data class SearchResponseItem(
    val point: Point,
    val geoObject: GeoObject?,
)

sealed interface SuggestState {
    data object Off : SuggestState
    data object Loading : SuggestState
    data object Error : SuggestState
    data class Success(val items: List<LocationItem>) : SuggestState
}

fun SearchState.toTextStatus(): String {
    return when (this) {
        SearchState.Error -> "Error"
        SearchState.Loading -> "Loading"
        SearchState.Off -> "Off"
        is SearchState.Success -> "Success"
    }
}

fun SuggestState.toTextStatus(): String {
    return when (this) {
        SuggestState.Error -> "Error"
        SuggestState.Loading -> "Loading"
        SuggestState.Off -> "Off"
        is SuggestState.Success -> "Success"
    }
}
