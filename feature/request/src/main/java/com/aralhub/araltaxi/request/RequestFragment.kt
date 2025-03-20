package com.aralhub.araltaxi.request

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aralhub.araltaxi.client.request.R
import com.aralhub.araltaxi.client.request.databinding.FragmentRequestBinding
import com.aralhub.araltaxi.core.common.error.ErrorHandler
import com.aralhub.araltaxi.core.common.permission.PermissionHelper
import com.aralhub.araltaxi.request.navigation.FeatureRequestNavigation
import com.aralhub.araltaxi.request.utils.BottomSheetBehaviorDrawerListener
import com.aralhub.araltaxi.request.utils.MapKitInitializer
import com.aralhub.indrive.core.data.model.client.ClientProfile
import com.aralhub.ui.adapter.location.LocationItemAdapter
import com.aralhub.ui.model.LocationItemClickOwner
import com.aralhub.ui.model.args.LocationType
import com.aralhub.ui.model.args.SelectedLocation
import com.aralhub.ui.sheets.LoadingModalBottomSheet
import com.aralhub.ui.sheets.LogoutModalBottomSheet
import com.aralhub.ui.utils.GlideEx.displayAvatar
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class RequestFragment : Fragment(R.layout.fragment_request) {

    private companion object {
        private const val LOCATION_REQUEST_MIN_TIME = 0L //Milliseconds
        private const val LOCATION_REQUEST_MIN_DISTANCE = 5f //Meter

        //To fetch args when go back from SelectLocationFragment
        private const val SELECT_LOCATION_OWNER_FROM = 0
        private const val SELECT_LOCATION_OWNER_TO = 1
        private const val SELECT_LOCATION_REQUEST_KEY = "location_key"
        private const val SELECT_LOCATION_KEY_LATITUDE = "latitude"
        private const val SELECT_LOCATION_KEY_LONGITUDE = "longitude"
        private const val SELECT_LOCATION_KEY_LOCATION_NAME = "locationName"
        private const val SELECT_LOCATION_KEY_LOCATION_OWNER = "owner"
        private const val NULL_STRING = "null" //locationName can be null

        //To fetch args when go back from CreateOrderFragment
        private const val CREATE_ORDER_REQUEST_KEY = "cancel"

        //CurrentLocation values
        private const val CURRENT_LOCATION_NOT_INITIALISED_VALUE = 0.0
        private var currentLongitude = CURRENT_LOCATION_NOT_INITIALISED_VALUE
        private var currentLatitude = CURRENT_LOCATION_NOT_INITIALISED_VALUE
    }

    private val binding by viewBinding(FragmentRequestBinding::bind)
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    @Inject
    lateinit var navigation: FeatureRequestNavigation

    @Inject
    lateinit var errorHandler: ErrorHandler
    private val adapter = LocationItemAdapter()
    private val viewModel by viewModels<RequestViewModel>()
    private val requestViewModel2 by viewModels<RequestViewModel2>()
    private var locationManager: LocationManager? = null

    // Variables to track latest states
    private var latestSearchRideState: SearchRideUiState? = null
    private var latestActiveRideState: ActiveRideUiState? = null

    private var placeMarkObject: PlacemarkMapObject? = null
    private var isNavigatedToCreateOrderFragment = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MapKitInitializer.init("f1c206ee-1f73-468c-8ba8-ec3ef7a7f69a", requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        observeStates()
        initViews()
        initListeners()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        locationManager?.let { observeLocationUpdates(it) }
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
        locationManager = null
    }

    @SuppressLint("MissingPermission")
    private fun observeLocationUpdates(locationManager: LocationManager) {
        if (PermissionHelper.arePermissionsGranted(
                requireContext(),
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                LOCATION_REQUEST_MIN_TIME,
                LOCATION_REQUEST_MIN_DISTANCE
            ) { location ->
                requestViewModel2.setCurrentLocation(location.latitude, location.longitude)
            }
        }

    }

    private fun observeStates() {
        observeState(requestViewModel2.currentLocationFlow) {
            updateMap(it.longitude, it.latitude)
            currentLatitude = it.latitude
            currentLongitude = it.longitude
            binding.etFromLocation.text = it.name
        }
        observeState(requestViewModel2.fromLocationFlow) {
            it?.let { fromLocation ->
                binding.etFromLocation.text = fromLocation.name
            }
        }
        observeState(requestViewModel2.toLocationFlow) {
            it?.let { toLocation ->
                Log.i("Location", "${toLocation.name}")
                binding.etToLocation.text = toLocation.name
            } ?: run {
                binding.etToLocation.text = ""
            }
        }
        observeState(requestViewModel2.navigateToCreateOrderFlow) { selectedLocations ->
            if (!isNavigatedToCreateOrderFragment && selectedLocations != null) {
                isNavigatedToCreateOrderFragment = true
                navigation.goToCreateOrderFromRequestFragment(selectedLocations)
                requestViewModel2.clearToLocation()
            }
        }
        observeState(viewModel.suggestionsUiState) { suggestionsUiState ->
            when (suggestionsUiState) {
                is SuggestionsUiState.Error -> errorHandler.showToast(suggestionsUiState.message)
                SuggestionsUiState.Loading -> {}
                is SuggestionsUiState.Success -> {
                    adapter.submitList(null)
                    adapter.submitList(suggestionsUiState.suggestions)
                }
            }
        }
        observeState(viewModel.searchRideUiState) { searchRideUiState ->
            latestSearchRideState = searchRideUiState
            updateLoadingDialog()
            when (searchRideUiState) {
                is SearchRideUiState.Error -> {}
                SearchRideUiState.Loading -> {}
                is SearchRideUiState.Success -> {
//                    navigation.goToGetOffersFromRequestFragment()
                }
            }
        }
        observeState(viewModel.activeRideUiState) { activeRideUiState ->
            latestActiveRideState = activeRideUiState
            updateLoadingDialog()
            when (activeRideUiState) {
                is ActiveRideUiState.Error -> {}
                ActiveRideUiState.Loading -> {}
                is ActiveRideUiState.Success -> {
                    LoadingModalBottomSheet.hide(childFragmentManager)
                    navigation.goToRideFragmentFromRequestFragment()
                }
            }
        }
        observeState(viewModel.profileUiState) { profileUiState ->
            when (profileUiState) {
                is ProfileUiState.Error -> errorHandler.showToast(profileUiState.message)
                ProfileUiState.Loading -> {}
                is ProfileUiState.Success -> displayProfile(profileUiState.profile)
            }
        }
        observeState(viewModel.logOutUiState) { logOutUiState ->
            when (logOutUiState) {
                is LogOutUiState.Error -> errorHandler.showToast(logOutUiState.message)
                LogOutUiState.Loading -> {}
                LogOutUiState.Success -> navigation.goToLogoFromRequestFragment()
            }
        }
    }

    private fun initViews() {
        binding.rvLocations.adapter = adapter
        setUpBottomSheet()
    }

    private fun initListeners() {
        initFragmentResultListener()
        initAdapterListener()
        binding.etFromLocation.setOnTextChangedListener {
            if (it.isNotEmpty() && !it.isNullOrBlank()) {
                viewModel.suggestLocation(it, LocationItemClickOwner.FROM)
            } else {
                adapter.submitList(null)
            }
        }

        binding.etToLocation.setOnTextChangedListener {
            if (it.isNotEmpty() && !it.isNullOrBlank()) {
                viewModel.suggestLocation(it, LocationItemClickOwner.TO)
            } else {
                adapter.submitList(null)
            }
        }

        val textFields = listOf(binding.etFromLocation, binding.etToLocation)
        for (textField in textFields) {
            textField.setOnActivatedListener {
                textField.setEndTextVisible(it)
            }
        }

        binding.etFromLocation.setEndTextClickListener {
            navigation.goToSelectFromLocationFromRequestFragment()
        }

        binding.etToLocation.setEndTextClickListener {
            navigation.goToSelectToLocationFromRequestFragment()
        }

        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.btnCurrentLocation.setOnClickListener {
            if (currentLatitude != CURRENT_LOCATION_NOT_INITIALISED_VALUE && currentLongitude != CURRENT_LOCATION_NOT_INITIALISED_VALUE) {
                updateMap(currentLongitude, currentLatitude)
            }
        }

        initNavigationViewListener()
        initDrawerListener()

    }

    private fun initFragmentResultListener() {
        parentFragmentManager.clearFragmentResultListener(SELECT_LOCATION_REQUEST_KEY)
        parentFragmentManager.setFragmentResultListener(
            SELECT_LOCATION_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val latitude = bundle.getDouble(SELECT_LOCATION_KEY_LATITUDE)
            val longitude = bundle.getDouble(SELECT_LOCATION_KEY_LONGITUDE)
            val locationName = bundle.getString(SELECT_LOCATION_KEY_LOCATION_NAME) ?: NULL_STRING
            val locationOwner = bundle.getInt(SELECT_LOCATION_KEY_LOCATION_OWNER)
            when (locationOwner) {
                SELECT_LOCATION_OWNER_FROM -> {
                    requestViewModel2.setFromLocation(
                        SelectedLocation(
                            name = locationName,
                            longitude = longitude,
                            latitude = latitude,
                            locationType = LocationType.FROM
                        )
                    )
                }

                SELECT_LOCATION_OWNER_TO -> {
                    requestViewModel2.setToLocation(
                        SelectedLocation(
                            name = locationName,
                            longitude = longitude,
                            latitude = latitude,
                            locationType = LocationType.TO
                        )
                    )
                }
            }
        }

        parentFragmentManager.setFragmentResultListener(
            CREATE_ORDER_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, _ ->
            isNavigatedToCreateOrderFragment = false
        }
    }

    private fun initAdapterListener() {
        adapter.setOnItemClickListener {
            when (it.clickOwner) {
                LocationItemClickOwner.FROM -> {
                    requestViewModel2.setFromLocation(
                        SelectedLocation(
                            name = it.title,
                            longitude = it.longitude,
                            latitude = it.latitude,
                            locationType = LocationType.FROM
                        )
                    )
                    adapter.submitList(null)
                }

                LocationItemClickOwner.TO -> {
                    binding.etToLocation.text = it.title
                    viewModel.updateLocation(
                        SelectedLocation(
                            name = it.title,
                            longitude = it.longitude,
                            latitude = it.latitude,
                            locationType = LocationType.TO
                        )
                    )
                    adapter.submitList(null)
                }
            }
        }
    }

    private fun initDrawerListener() {
        if (bottomSheetBehavior != null) {
            binding.drawerLayout.addDrawerListener(
                BottomSheetBehaviorDrawerListener(bottomSheetBehavior!!)
            )
        }
    }

    private fun initNavigationViewListener() {
        binding.navigationView.getHeaderView(0).setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            navigation.goToProfileFromRequestFragment()
        }
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_support -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navigation.goToSupportFromRequestFragment()
                    true
                }

                R.id.action_log_out -> {
                    val logOutModalBottomSheet = LogoutModalBottomSheet()
                    logOutModalBottomSheet.show(childFragmentManager, LogoutModalBottomSheet.TAG)
                    logOutModalBottomSheet.setOnLogoutListener {
                        logOutModalBottomSheet.dismissAllowingStateLoss()
                        viewModel.logOut()
                    }
                    true
                }

                R.id.action_my_addresses -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navigation.goToSavedPlacesFromRequestFragment()
                    true
                }

                R.id.action_trip_history -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navigation.goToHistoryFromRequestFragment()
                    true
                }

                else -> false
            }
        }
    }

    private fun setUpBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.layoutBottomSheet.isVisible = true
    }

    private fun updateLoadingDialog() {
        val searchState = latestSearchRideState
        val activeState = latestActiveRideState

        // Show dialog if we haven't received final states yet
        if (searchState == null || activeState == null ||
            (searchState is SearchRideUiState.Loading || activeState is ActiveRideUiState.Loading)
        ) {
            showLoadingDialog()
            return
        }

        // Hide dialog when:
        // 1. One of them is Success
        // 2. Both are Error
        when {
            searchState is SearchRideUiState.Success || activeState is ActiveRideUiState.Success -> {
                hideLoadingDialog()
            }

            searchState is SearchRideUiState.Error && activeState is ActiveRideUiState.Error -> {
                hideLoadingDialog()
            }
        }
    }

    private fun updateMap(longitude: Double, latitude: Double) {
        val imageProvider = ImageProvider.fromResource(context, com.aralhub.ui.R.drawable.ic_vector)
        placeMarkObject?.let {
            it.geometry = Point(latitude, longitude)
        } ?: run {
            placeMarkObject = binding.mapView.mapWindow.map.mapObjects.addPlacemark().apply {
                geometry = Point(latitude, longitude)
                setIcon(imageProvider)
            }
        }
        binding.mapView.mapWindow.map.move(
            CameraPosition(
                Point(latitude, longitude),
                17.0f,
                150.0f,
                30.0f
            )
        )
    }

    private fun displayProfile(profile: ClientProfile) {
        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_name).text =
            profile.fullName
        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_phone).text =
            profile.phone
        val imageView =
            binding.navigationView.getHeaderView(0).findViewById<ImageView>(R.id.iv_avatar)
        displayAvatar(profile.profilePhoto, imageView)
    }

    private fun showLoadingDialog() {
        Log.i("Dialog", "Show")
        LoadingModalBottomSheet.show(childFragmentManager)
    }

    private fun hideLoadingDialog() {
        Log.i("Dialog", "Hide")
        LoadingModalBottomSheet.hide(childFragmentManager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LoadingModalBottomSheet.hide(childFragmentManager)
    }

}