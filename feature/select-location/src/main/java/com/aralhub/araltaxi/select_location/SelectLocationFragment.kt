package com.aralhub.araltaxi.select_location

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.core.common.error.ErrorHandler
import com.aralhub.araltaxi.select_location.databinding.FragmentSelectLocationBinding
import com.aralhub.araltaxi.select_location.utils.CurrentLocationListener
import com.aralhub.ui.utils.FloatLandAnimation
import com.aralhub.ui.utils.viewBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapWindow
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session.SearchListener
import com.yandex.runtime.Error
import javax.inject.Inject

class SelectLocationFragment : Fragment(R.layout.fragment_select_location) {
    private val binding by viewBinding(FragmentSelectLocationBinding::bind)
    private lateinit var mapWindow: MapWindow
    private lateinit var map: Map
    @Inject lateinit var errorHandler: ErrorHandler
    private val searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    private var locationManager: LocationManager? = null
    private val searchOptions = SearchOptions().apply {
        searchTypes = SearchType.GEO.value
        resultPageSize = 1
    }
    private lateinit var floatLandAnimation: FloatLandAnimation
    private val movementHandler = Handler(Looper.getMainLooper())
    private val MOVEMENT_DELAY = 500L
    private var isMapMoving = false
    private var selectedLatitude = 0.0
    private var selectedLongitude = 0.0
    private var locationOwner = LOCATION_OWNER_UNSPECIFIED

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
        locationManager = requireActivity().getSystemService(LocationManager::class.java)
        locationManager?.let { observeLocationUpdates(it) }
    }

    override fun onStop() {
        super.onStop()
        floatLandAnimation.stopAnimations()
        locationManager = null
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

        if (view != null){
            binding.itemSelectLocation.tvTitle.text = "${point.latitude}, ${point.longitude}"
            binding.itemSelectLocation.tvSubtitle.text = ""
        }

        searchManager.submit(point, 17, searchOptions, object : SearchListener {
            override fun onSearchResponse(response: Response) {
                val geoObjects = response.collection.children.mapNotNull { it.obj }
                val names = geoObjects.filter { it.name != null }.map { it.name }
                selectedLongitude = geoObjects.firstOrNull()?.geometry?.get(0)?.point?.longitude ?: 0.0
                selectedLatitude = geoObjects.firstOrNull()?.geometry?.get(0)?.point?.latitude ?: 0.0
                if (view != null){
                    if (names.isNotEmpty()) {
                        binding.itemSelectLocation.tvTitle.text = names[0]
                        binding.itemSelectLocation.tvSubtitle.text = names.toString()
                    } else {
                        binding.itemSelectLocation.tvTitle.text = "Unknown Location"
                    }
                }

            }

            override fun onSearchError(error: Error) {
                errorHandler.showToast("Error to find location name")
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MapKitFactory.initialize(requireContext())
        mapWindow = binding.mapView.mapWindow
        map = mapWindow.map
        binding.itemSelectLocation.ivIcon.setImageResource(com.aralhub.ui.R.drawable.ic_ic_round_pin_drop)
        floatLandAnimation  = FloatLandAnimation(binding.iconCenter)
        map.addCameraListener(cameraListener)
        initArgs()

        binding.btnSelectLocation.setOnClickListener {
            val result = Bundle().apply {
                putDouble("latitude", selectedLatitude)
                putDouble("longitude", selectedLongitude)
                putInt("owner", locationOwner)
                putString("locationName", binding.itemSelectLocation.tvTitle.text.toString())
            }
            // Set the result and navigate back
            parentFragmentManager.setFragmentResult("location_key", result)
            findNavController().navigateUp()
        }
    }

    private fun initArgs() {
        val owner = when (requireArguments().getInt("owner")) {
            LOCATION_OWNER_FROM -> LocationOwner.FROM
            LOCATION_OWNER_TO -> LocationOwner.TO
            else -> LocationOwner.UNSPECIFIED
        }
        binding.itemSelectLocation.tvTitle.text = when (owner) {
            LocationOwner.FROM -> "Alıp ketiw ornı"
            LocationOwner.TO -> "Mánzil"
            LocationOwner.UNSPECIFIED -> "Mánzil"
        }
        locationOwner = when (owner) {
            LocationOwner.FROM -> LOCATION_OWNER_FROM
            LocationOwner.TO -> LOCATION_OWNER_TO
            LocationOwner.UNSPECIFIED -> LOCATION_OWNER_UNSPECIFIED
        }
    }

    @SuppressLint("MissingPermission")
    private fun observeLocationUpdates(locationManager: LocationManager) {
        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: Location(LocationManager.GPS_PROVIDER).apply {
            latitude = 42.4651
            longitude = 59.6136
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, CurrentLocationListener(map,
            initialLocation = lastKnownLocation,
            onProviderEnabledListener = {},
            onProviderDisabledListener = {})
        )
    }

    companion object {
        private const val ZOOM = 17f
        private const val LOCATION_OWNER_FROM = 0
        private const val LOCATION_OWNER_TO = 1
        private const val LOCATION_OWNER_UNSPECIFIED = -1
        enum class LocationOwner {
            FROM, TO, UNSPECIFIED
        }
        fun args(owner: LocationOwner) = Bundle().apply {
            putInt("owner", when(owner) {
                LocationOwner.FROM -> LOCATION_OWNER_FROM
                LocationOwner.TO -> LOCATION_OWNER_TO
                LocationOwner.UNSPECIFIED -> LOCATION_OWNER_UNSPECIFIED
            })
        }
    }
}