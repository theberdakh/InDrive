package com.aralhub.araltaxi.request

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
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
import com.aralhub.araltaxi.request.navigation.FeatureRequestNavigation
import com.aralhub.araltaxi.request.navigation.models.LocationType
import com.aralhub.araltaxi.request.navigation.models.SelectedLocation
import com.aralhub.araltaxi.request.utils.BottomSheetBehaviorDrawerListener
import com.aralhub.araltaxi.request.utils.CurrentLocationListener
import com.aralhub.araltaxi.request.utils.MapKitInitializer
import com.aralhub.indrive.core.data.model.client.ClientProfile
import com.aralhub.ui.adapter.location.LocationItemAdapter
import com.aralhub.ui.model.LocationItemClickOwner
import com.aralhub.ui.sheets.LogoutModalBottomSheet
import com.aralhub.ui.utils.GlideEx.displayAvatar
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
internal class RequestFragment : Fragment(R.layout.fragment_request) {
    private val binding by viewBinding(FragmentRequestBinding::bind)
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    @Inject
    lateinit var navigation: FeatureRequestNavigation
    @Inject
    lateinit var errorHandler: ErrorHandler
    private val adapter = LocationItemAdapter()
    private val viewModel by viewModels<RequestViewModel>()
    private var locationManager: LocationManager? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MapKitInitializer.init("f1c206ee-1f73-468c-8ba8-ec3ef7a7f69a", requireContext())
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
        initObservers()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
        locationManager = null
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager?.let { observeLocationUpdates(it) }
        initViews()
        initListeners()
        viewModel.getProfile()
    }

    @SuppressLint("MissingPermission")
    private fun observeLocationUpdates(locationManager: LocationManager) {
        val lastKnownLocation =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: Location(
                LocationManager.GPS_PROVIDER
            ).apply {
                latitude = 42.4651
                longitude = 59.6136
            }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            CurrentLocationListener(requireContext(), binding.mapView.mapWindow.map,
                initialLocation = lastKnownLocation,
                onProviderEnabledListener = {
                    viewModel.updateLocationEnabled(true)
                },
                onProviderDisabledListener = {
                    viewModel.updateLocationEnabled(false)
                })
        )
    }

    private fun initObservers() {

        observeState(viewModel.selectedLocations) { selectedLocations ->
            if(selectedLocations != null){
                navigation.goToCreateOrderFromRequestFragment(selectedLocations)
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

        observeState(viewModel.locationEnabled) { isEnabled ->
            if (isEnabled) {
                // Location GPS is enabled
            } else {
                // Location GPS is disabled
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

    private fun displayProfile(profile: ClientProfile) {
        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_name).text =
            profile.fullName
        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_phone).text =
            profile.phone
        val imageView =
            binding.navigationView.getHeaderView(0).findViewById<ImageView>(R.id.iv_avatar)
        displayAvatar(profile.profilePhoto, imageView)
    }

    private fun initViews() {
        binding.rvLocations.adapter = adapter
        setUpBottomSheet()
    }

    private fun initListeners() {

        parentFragmentManager.setFragmentResultListener(
            "location_key",
            viewLifecycleOwner
        ) { requestKey, bundle ->
            val latitude = bundle.getDouble("latitude")
            val longitude = bundle.getDouble("longitude")
            val locationName = bundle.getString("locationName") ?: "null name"
            val locationOwner = bundle.getInt("owner")
            when (locationOwner) {
                0 -> {
                    binding.etFromLocation.setText(locationName)
                    viewModel.updateLocation(
                        SelectedLocation(
                            name = locationName,
                            longitude = longitude,
                            latitude = latitude,
                            locationType = LocationType.FROM
                        )
                    )
                }

                1 -> {
                    binding.etToLocation.setText(locationName)
                    viewModel.updateLocation(
                        SelectedLocation(
                            name = locationName,
                            longitude = longitude,
                            latitude = latitude,
                            locationType = LocationType.TO
                        )
                    )
                }
            }
            errorHandler.showToast("Selected location: $locationOwner $locationName: $latitude, $longitude")
        }


        adapter.setOnItemClickListener {
            when (it.clickOwner) {
                LocationItemClickOwner.FROM -> {
                    binding.etFromLocation.text = it.title
                    viewModel.updateLocation(
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

        binding.etFromLocation.setOnTextChangedListener {
            if (it.isEmpty()) {
                adapter.submitList(null)
            }
            viewModel.suggestLocation(it, LocationItemClickOwner.FROM)
        }

        binding.etToLocation.setOnTextChangedListener {
            if (it.isEmpty()) {
                adapter.submitList(null)
            }
            viewModel.suggestLocation(it, LocationItemClickOwner.TO)
        }

        binding.etFromLocation.setOnActivatedListener { isActivated ->
            binding.etFromLocation.setEndTextVisible(isActivated)
        }

        binding.etToLocation.setOnActivatedListener { isActivated ->
            binding.etToLocation.setEndTextVisible(isActivated)
        }

        binding.etFromLocation.setEndTextClickListener {
            navigation.goToSelectFromLocationFromRequestFragment()
        }
        binding.etToLocation.setEndTextClickListener {
           // navigation.goToCreateOrderFromRequestFragment()
            navigation.goToSelectToLocationFromRequestFragment()
        }

        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
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
        if (bottomSheetBehavior != null) {
            binding.drawerLayout.addDrawerListener(
                BottomSheetBehaviorDrawerListener(bottomSheetBehavior!!)
            )
        }
    }

    private fun setUpBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.layoutBottomSheet.isVisible = true
    }

}