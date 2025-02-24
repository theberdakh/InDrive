package com.aralhub.araltaxi.request.sheet.modal.addlocation

import android.support.v4.os.IResultReceiver._Parcel
import androidx.lifecycle.ViewModel
import com.aralhub.araltaxi.request.model.SearchResponseItem
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.search.Session.SearchListener
import com.yandex.runtime.Error
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

class AddLocationViewModel: ViewModel() {
    private val searchManager =SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    private var searchSession: Session? = null
    private val query = MutableStateFlow("")
    private var region = MutableStateFlow<VisibleRegion?>(null)

    fun setQueryText(value: String) {
        query.value = value
    }

    private var _searchItems = MutableSharedFlow<SearchResponseItem>()
    private val searchItems = _searchItems.asSharedFlow()

    fun startSearch(searchText: String? = null){
        val text = searchText ?: query.value
        if (query.value.isEmpty()) return
        val region = region.value?.let {
            VisibleRegionUtils.toPolygon(it)
        } ?: return
        submitSearch(text)
    }

    fun submitSearch(uri: String) {
        searchSession?.cancel()
        searchSession = searchManager.searchByURI(uri, SearchOptions(), searchSessionListener)
    }

    sealed interface AddLocationUiState {
        data class Success(val items: SearchResponseItem): AddLocationUiState
        data object Error: AddLocationUiState
        data object Loading: AddLocationUiState
    }



    private val searchSessionListener = object : SearchListener {
        override fun onSearchResponse(response: Response) {
            val items = response.collection.children.mapNotNull {
                val point =it.obj?.geometry?.firstOrNull()?.point ?:return@mapNotNull null
                SearchResponseItem(point, it.obj)
            }


        }

        override fun onSearchError(error: Error) {
        }

    }



}