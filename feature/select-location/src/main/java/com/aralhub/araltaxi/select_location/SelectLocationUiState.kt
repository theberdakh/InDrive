package com.aralhub.araltaxi.select_location

import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point

data class SelectLocationUiState(
    val point: Point,
    val searchState: SearchState = SearchState.Off
)

sealed interface SearchState {
    data object Off : SearchState
    data object Loading : SearchState
    data object Error : SearchState
    data class Success(
        val items: List<SearchResponseItem>,
        val zoomToItems: Boolean,
        val itemsBoundingBox: BoundingBox,
    ) : SearchState
}

data class SearchResponseItem(
    val point: Point,
    val geoObject: GeoObject?,
)
