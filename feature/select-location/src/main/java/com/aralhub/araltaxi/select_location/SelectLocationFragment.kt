package com.aralhub.araltaxi.select_location

import android.graphics.PointF
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aralhub.araltaxi.select_location.databinding.FragmentSelectLocationBinding
import com.aralhub.ui.utils.viewBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraBounds
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapWindow
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session.SearchListener
import com.yandex.mapkit.search.Snippet
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider

class SelectLocationFragment : Fragment(R.layout.fragment_select_location) {
    private val binding by viewBinding(FragmentSelectLocationBinding::bind)
    private lateinit var mapWindow: MapWindow
    private lateinit var map: Map
    private lateinit var placemarkMapObject: PlacemarkMapObject
    val searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    private val searchOptions = SearchOptions().apply {
        searchTypes = SearchType.GEO.value
        resultPageSize = 1
    }


    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MapKitFactory.initialize(requireContext())

        mapWindow = binding.mapView.mapWindow
        map = mapWindow.map

        map.move(
            CameraPosition(
                /* target */ Point(42.473875, 59.615476),
                /* zoom */ ZOOM,
                /* azimuth */ 0f,
                /* tilt */ 0f,
            ),
            Animation(Animation.Type.LINEAR, 1f),
            null
        )

        createPlacemark(Point(55.751280, 37.629720))

        binding.btnMenu.setOnClickListener {
            val screenCenter = ScreenPoint(
                mapWindow.width() / 2f,
                mapWindow.height() / 2f
            )
            val point = mapWindow.screenToWorld(screenCenter) ?: return@setOnClickListener
            placemarkMapObject.geometry = point
            binding.itemSelectLocation.tvTitle.text = "${point.latitude}, ${point.longitude}"
            searchManager.submit(point, 22, searchOptions, object: SearchListener{
                override fun onSearchResponse(response: Response) {
                    val geoObjects = response.collection.children.mapNotNull { it.obj }
                    val names = geoObjects.filter { it.name != null }.map { it.name }
                    binding.itemSelectLocation.tvTitle.text = names[0]
                    binding.itemSelectLocation.tvSubtitle.text = names.toString()
                }

                override fun onSearchError(error: Error) {
                    binding.itemSelectLocation.tvSubtitle.text = error.isValid.toString()
                }

            })
        }

    }

    private fun createPlacemark(point: Point) {
        Toast.makeText(requireContext(), "Placemark created $point", Toast.LENGTH_SHORT).show()
        placemarkMapObject = map.mapObjects.addPlacemark().apply {
            geometry = point
            setIcon(
                ImageProvider.fromResource(requireContext(), com.aralhub.ui.R.drawable.ic_vector),
                IconStyle().apply { anchor = PointF(0.5f, 1.0f) })
            isDraggable = true
        }
    }

    companion object {
        private const val ZOOM = 16f
    }
}