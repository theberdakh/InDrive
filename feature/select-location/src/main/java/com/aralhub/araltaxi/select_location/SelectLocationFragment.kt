package com.aralhub.araltaxi.select_location

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.core.common.error.ErrorHandler
import com.aralhub.araltaxi.select_location.databinding.FragmentSelectLocationBinding
import com.aralhub.araltaxi.select_location.utils.CurrentLocationListener
import com.aralhub.ui.utils.FloatLandAnimation
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.ViewEx.enable
import com.aralhub.ui.utils.viewBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.ScreenRect
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapWindow
import com.yandex.mapkit.map.SizeChangedListener
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session.SearchListener
import com.yandex.runtime.Error
import javax.inject.Inject

class SelectLocationFragment : Fragment(R.layout.fragment_select_location) {
    private lateinit var mapWindow: MapWindow
    private lateinit var map: Map
    private lateinit var floatLandAnimation: FloatLandAnimation
    @Inject lateinit var errorHandler: ErrorHandler

    private val viewModel by viewModels<SelectLocationViewModel>()
    private val binding by viewBinding(FragmentSelectLocationBinding::bind)
    private val movementHandler = Handler(Looper.getMainLooper())
    private val movementDelay = 500L

    private val cameraListener = CameraListener { map, cameraPosition, cameraUpdateReason, finished ->
            if (!finished && !isMapMoving) {
                isMapMoving = true
                floatLandAnimation.startFloating()
            } else if (finished) {
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
            }, movementDelay)
        }
    private val sizeChangedListener = SizeChangedListener { _, _, _ ->
        // Recalculate FocusRect and FocusPoint on every map's size change
        updateFocusInfo()
    }
    private val currentLocationListener = CurrentLocationListener(
        onUpdateMapPosition = ::updateMapPosition,
        onProviderDisabledListener = {displayGpsStatus(false)},
        onProviderEnabledListener = { displayGpsStatus(true)}
    )

    private var locationManager: LocationManager? = null
    private var isMapMoving = false
    private var selectedLatitude = 0.0
    private var selectedLongitude = 0.0
    private var selectedTitle = ""
    private var selectedSubtitle = ""
    private var locationOwner = LOCATION_OWNER_UNSPECIFIED

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationManager = requireActivity().getSystemService(LocationManager::class.java)
        initMap()
        updateFocusInfo()
        initObservers()
        initArgs()
        initViews()
        initListeners()
    }

    private fun initViews() {
        binding.itemSelectLocation.ivIcon.setImageResource(com.aralhub.ui.R.drawable.ic_ic_round_pin_drop)
    }

    private fun initMap() {
        mapWindow = binding.mapView.mapWindow
        map = mapWindow.map
        viewModel.setVisibleRegion(map.visibleRegion)
        mapWindow.addSizeChangedListener(sizeChangedListener)
        map.addCameraListener(cameraListener)
    }

    private fun initListeners() {
        binding.btnSelectLocation.setOnClickListener {
            val result = Bundle().apply {
                putDouble("latitude", selectedLatitude)
                putDouble("longitude", selectedLongitude)
                putString("locationName", selectedTitle)
                putInt("owner", locationOwner)
            }

            parentFragmentManager.setFragmentResult("location_key", result)
            if (mapWindow.isValid) {
                map.removeCameraListener(cameraListener)
                findNavController().navigateUp()
            }
        }
    }

    private fun initArgs() {
        val owner = when (requireArguments().getInt("owner")) {
            LOCATION_OWNER_FROM -> LocationOwner.FROM
            LOCATION_OWNER_TO -> LocationOwner.TO
            else -> LocationOwner.UNSPECIFIED
        }
        binding.tvTitle.text = when (owner) {
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

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
        floatLandAnimation = FloatLandAnimation(binding.iconCenter)
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, currentLocationListener)
    }

    override fun onPause() {
        super.onPause()
        locationManager?.removeUpdates(currentLocationListener)
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        floatLandAnimation.stopAnimations()
        locationManager = null
        super.onStop()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        locationManager = null
    }

    private fun initObservers(){
        observeState(viewModel.uiState){
           when(it.searchState){
               SearchState.Error -> errorHandler.showToast("Error to find location name")
               SearchState.Loading -> {binding.itemSelectLocation.tvTitle.text = "Updating..."}
               SearchState.Off -> binding.itemSelectLocation.tvTitle.text = "Searching..."
               is SearchState.Success -> {
                   viewModel.selectLocation(
                       it.searchState.items.firstOrNull()?.geoObject?.name ?: "Unknown Location",
                       it.searchState.items.firstOrNull()?.geoObject?.descriptionText ?: "",
                       it.searchState.items.firstOrNull()?.point ?: Point(0.0, 0.0)
                   )
               }
           }
        }

        observeState(viewModel.locationSelectedUiState){ locationSelectedUiState ->
            when(locationSelectedUiState){
                LocationSelectedUiState.Error -> {}
                LocationSelectedUiState.Loading -> {}
                is LocationSelectedUiState.Success -> {
                    selectedLongitude =  locationSelectedUiState.point.longitude
                    selectedLatitude = locationSelectedUiState.point.latitude
                    selectedTitle = locationSelectedUiState.title
                    selectedSubtitle = locationSelectedUiState.subtitle

                    binding.itemSelectLocation.tvTitle.text = locationSelectedUiState.title
                    binding.itemSelectLocation.tvSubtitle.text = locationSelectedUiState.subtitle
                    binding.btnSelectLocation.enable()
                }
            }
        }
    }

    private fun updatePlacemarkAndSearchLocation(point: Point) {
        viewModel.submitLocation(point, 17)
       /* searchManager.submit(point, 17, searchOptions, object : SearchListener {
            override fun onSearchResponse(response: Response) {
                val geoObjects = response.collection.children.mapNotNull { it.obj }
                val names = geoObjects.filter { it.name != null }.map { it.name }
                selectedLongitude = geoObjects.firstOrNull()?.geometry?.get(0)?.point?.longitude ?: 0.0
                selectedLatitude = geoObjects.firstOrNull()?.geometry?.get(0)?.point?.latitude ?: 0.0
                if (view != null) {
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
        })*/
    }

    private fun displayGpsStatus(enabled: Boolean){
        if (!enabled) {
            errorHandler.showToast("GPS is disabled")
        }
    }

    private fun updateMapPosition(point: Point) {
        val cameraPosition = CameraPosition(point, 17.0f, 150.0f, 30.0f)
        if (this::map.isInitialized) {
            map.move(cameraPosition)
        }
    }


    private fun updateFocusInfo() {
        val defaultPadding = resources.getDimension(R.dimen.default_focus_rect_padding)
        mapWindow.focusRect = ScreenRect(
            ScreenPoint(defaultPadding, defaultPadding),
            ScreenPoint(
                mapWindow.width() - defaultPadding,
                mapWindow.height() - defaultPadding,
            )
        )
        mapWindow.focusPoint = ScreenPoint(
            mapWindow.width() / 2f,
            mapWindow.height() / 2f,
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
            putInt(
                "owner", when (owner) {
                    LocationOwner.FROM -> LOCATION_OWNER_FROM
                    LocationOwner.TO -> LOCATION_OWNER_TO
                    LocationOwner.UNSPECIFIED -> LOCATION_OWNER_UNSPECIFIED
                }
            )
        }
    }
}