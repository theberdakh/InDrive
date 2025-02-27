package com.aralhub.araltaxi.request

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.aralhub.araltaxi.client.request.R
import com.aralhub.araltaxi.client.request.databinding.FragmentRequestBinding
import com.aralhub.araltaxi.request.navigation.FeatureRequestNavigation
import com.aralhub.araltaxi.request.navigation.sheet.SheetNavigator
import com.aralhub.araltaxi.request.sheet.modal.LogoutModalBottomSheet
import com.aralhub.araltaxi.request.utils.BottomSheetBehaviorDrawerListener
import com.aralhub.araltaxi.request.utils.MapKitInitializer
import com.aralhub.araltaxi.request.utils.SelectLocationCameraListener
import com.aralhub.indrive.core.data.model.client.ClientProfile
import com.aralhub.ui.utils.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.Map.CameraCallback
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
internal class RequestFragment : Fragment(R.layout.fragment_request) {
    private val binding by viewBinding(FragmentRequestBinding::bind)
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private var selectLocationCameraListener: SelectLocationCameraListener? = null
    @Inject
    lateinit var sheetNavigator: SheetNavigator
    @Inject
    lateinit var navigation: FeatureRequestNavigation
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.forEach { permission ->
                Log.i("RequestTaxiFragment", "Permission: ${permission.key} is granted: ${permission.value}")
            }
        }
    private val viewModel by viewModels<RequestViewModel>()
    private var placeMarkObject: PlacemarkMapObject? = null
    private var locationManager: LocationManager? = null
    private var gpsEnabled: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MapKitInitializer.init("f1c206ee-1f73-468c-8ba8-ec3ef7a7f69a", requireContext())
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
        locationManager = null
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToLocationUpdates()
        initViews()
        initListeners()
        initObservers()
    }


    @SuppressLint("MissingPermission")
    private fun listenToLocationUpdates() {
        launchPermissions()
        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            val isProviderEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
            gpsEnabled = isProviderEnabled == true
            viewModel.updateLocationEnabled(gpsEnabled)
        } catch (_: Exception) {
            gpsEnabled = false
            viewModel.updateLocationEnabled(gpsEnabled)
            Toast.makeText(requireContext(), "Location is off", Toast.LENGTH_SHORT).show()
        }
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f) {
            Toast.makeText(
                requireContext(),
                "Requesting Location Updates",
                Toast.LENGTH_SHORT
            ).show()

            val map = binding.mapView.mapWindow.map
            val point = Point(it.latitude, it.longitude)
            val cameraPosition = CameraPosition(point, 17.0f, 150.0f, 30.0f)
            map.move(cameraPosition)
            val imageProvider = ImageProvider.fromResource(requireContext(), com.aralhub.ui.R.drawable.ic_vector)
            setPlaceMarkToPosition(
                cameraPosition,
                binding.mapView.mapWindow.map,
                point,
                imageProvider
            ) { placeMarkPoint ->
                Toast.makeText(
                    requireContext(),
                    "Tapped. lat: ${placeMarkPoint.latitude}, ${placeMarkPoint.longitude}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setPlaceMarkToPosition(
        position: CameraPosition,
        map: Map,
        point: Point,
        imageProvider: ImageProvider,
        onPlaceMarkTap: (point: Point) -> Unit
    ) {
        map.move(position)
        if (placeMarkObject == null) {
            placeMarkObject = map.mapObjects.addPlacemark().apply {
                geometry = point
                setIcon(imageProvider)
            }
            placeMarkObject!!.addTapListener { _, placeMarkPoint ->
                onPlaceMarkTap(placeMarkPoint)
                true
            }
        } else {
            placeMarkObject!!.geometry = point
        }
    }

    private fun initObservers() {
        viewModel.locationEnabled.onEach { isEnabled ->
            if (isEnabled) {
                Toast.makeText(requireContext(), "Location is enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Location is disabled", Toast.LENGTH_SHORT).show()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getProfile()
        viewModel.profileUiState.onEach {
            when (it) {
                is ProfileUiState.Error -> Log.i(
                    "RequestFragment",
                    "profileUiState: error ${it.message}"
                )

                ProfileUiState.Loading -> Log.i("RequestFragment", "profileUiState: loading")
                is ProfileUiState.Success -> displayProfile(it.profile)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.logOutUiState.onEach {
            when (it) {
                is LogOutUiState.Error -> Log.i("RequestFragment", "logOutUiState: error ${it.message}")
                LogOutUiState.Loading -> Log.i("RequestFragment", "logOutUiState: loading")
                LogOutUiState.Success -> navigation.goToLogoFromRequestFragment()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun displayProfile(profile: ClientProfile) {
        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_name).text =
            profile.fullName
        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_phone).text =
            profile.phone
        Glide.with(this)
            .load("https://araltaxi.aralhub.uz/${profile.profilePhoto}")
            .apply(RequestOptions.circleCropTransform())
            .signature(ObjectKey(System.currentTimeMillis()))
            .into(binding.navigationView.getHeaderView(0).findViewById(R.id.iv_avatar))
    }

    private fun launchPermissions() {
        locationPermissionLauncher.launch(requiredPermissions)
    }

    private fun initViews() {
        setUpBottomSheet()
        initBottomNavController()
    }

    private fun initListeners() {
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
                BottomSheetBehaviorDrawerListener(
                    bottomSheetBehavior!!
                )
            )
        }
    }

    private fun setUpSelectLocation() {
        selectLocationCameraListener?.let {
            binding.mapView.mapWindow.map.addCameraListener(selectLocationCameraListener!!)
            it.setOnLocationChangedListener {
                Log.i("RequestFragment", "onLocationChanged: $it")
            }
        }

    }

    private fun setUpMapView(latitude: Double, longitude: Double) {
        val point = Point(latitude, longitude)
        val imageProvider =
            ImageProvider.fromResource(requireContext(), com.aralhub.ui.R.drawable.ic_vector)
        binding.mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = point
            setIcon(imageProvider)
        }
        val cameraCallback = CameraCallback {}
        binding.mapView.mapWindow.map.move(
            CameraPosition(
                point,/* zoom = */
                17.0f,/* azimuth = */
                150.0f,/* tilt = */
                30.0f
            ), Animation(Animation.Type.LINEAR, 1f), cameraCallback
        )
    }

    private fun setUpBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.layoutBottomSheet.isVisible = true
    }

    private fun initBottomNavController() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.bottom_sheet_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        navController.let { sheetNavigator.bind(navController) }
    }

    override fun onDestroy() {
        sheetNavigator.unbind()
        super.onDestroy()
    }
}