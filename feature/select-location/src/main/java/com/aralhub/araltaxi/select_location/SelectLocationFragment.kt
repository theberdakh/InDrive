package com.aralhub.araltaxi.select_location

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aralhub.araltaxi.select_location.databinding.FragmentSelectLocationBinding
import com.aralhub.ui.utils.FloatLandAnimation
import com.aralhub.ui.utils.viewBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapWindow
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session.SearchListener
import com.yandex.runtime.Error

class SelectLocationFragment : Fragment(R.layout.fragment_select_location) {
    private val binding by viewBinding(FragmentSelectLocationBinding::bind)
    private lateinit var mapWindow: MapWindow
    private lateinit var map: Map
    private val searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    private val searchOptions = SearchOptions().apply {
        searchTypes = SearchType.GEO.value
        resultPageSize = 1
    }
    private lateinit var floatLandAnimation: FloatLandAnimation

    private val movementHandler = Handler(Looper.getMainLooper())
    private val MOVEMENT_DELAY = 500L
    private var isMapMoving = false

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        floatLandAnimation.stopAnimations()
    }

    private object CameraCallback: Map.CameraCallback {
        override fun onMoveFinished(finished: Boolean) {
        }
    }

    private val cameraListener = CameraListener { map, cameraPosition, cameraUpdateReason, finished ->

        if (!finished && !isMapMoving){
            isMapMoving = true
            floatLandAnimation.startFloating()
        } else if (finished){
            isMapMoving = false
            floatLandAnimation.land()
        }

            // Handle map movement start
            if (!isMapMoving) {
                isMapMoving = true
            }

            // Debounce to determine when map movement has stopped
            movementHandler.removeCallbacksAndMessages(null)
            movementHandler.postDelayed({
                if (finished) {
                    isMapMoving = false
                    // Update placemark and trigger search when map stops moving
                    val centerPoint = cameraPosition.target
                    updatePlacemarkAndSearchLocation(centerPoint)
                }
            }, MOVEMENT_DELAY)
        }

    private fun updatePlacemarkAndSearchLocation(point: Point) {
        // Update placemark to map center

        // Clear previous text
        binding.itemSelectLocation.tvTitle.text = "${point.latitude}, ${point.longitude}"
        binding.itemSelectLocation.tvSubtitle.text = ""

        // Submit search for location name
        searchManager.submit(point, 22, searchOptions, object : SearchListener {
            override fun onSearchResponse(response: Response) {
                val geoObjects = response.collection.children.mapNotNull { it.obj }
                val names = geoObjects.filter { it.name != null }.map { it.name }

                if (names.isNotEmpty()) {
                    binding.itemSelectLocation.tvTitle.text = names[0]
                    binding.itemSelectLocation.tvSubtitle.text = names.toString()
                } else {
                    binding.itemSelectLocation.tvTitle.text = "Unknown Location"
                }
            }

            override fun onSearchError(error: Error) {
                binding.itemSelectLocation.tvSubtitle.text = "Search Error: ${error.isValid}"
            }
        })
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MapKitFactory.initialize(requireContext())

        mapWindow = binding.mapView.mapWindow
        map = mapWindow.map

        floatLandAnimation  = FloatLandAnimation(binding.iconCenter)

        map.addCameraListener(cameraListener)

        map.move(
            CameraPosition(
                /* target */ Point(42.473875, 59.615476),
                /* zoom */ ZOOM,
                /* azimuth */ 0f,
                /* tilt */ 0f,
            ),
            Animation(Animation.Type.LINEAR, 1f),
            CameraCallback
        )


        binding.btnMenu.setOnClickListener {

        }

    }

    private fun createPlacemark(point: Point) {
        Toast.makeText(requireContext(), "Placemark created $point", Toast.LENGTH_SHORT).show()
      /*  placemarkMapObject = map.mapObjects.addPlacemark().apply {
            geometry = point
            setIcon(
                ImageProvider.fromResource(requireContext(), com.aralhub.ui.R.drawable.ic_vector),
                IconStyle().apply { anchor = PointF(0.5f, 1.0f) })
            isDraggable = true
        }*/
    }

    companion object {
        private const val ZOOM = 16f
    }
}