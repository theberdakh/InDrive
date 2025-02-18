package com.aralhub.araltaxi.request

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.aralhub.araltaxi.client.request.R
import com.aralhub.araltaxi.client.request.databinding.FragmentRequestBinding
import com.aralhub.araltaxi.request.navigation.FeatureRequestNavigation
import com.aralhub.araltaxi.request.navigation.sheet.SheetNavigator
import com.aralhub.araltaxi.request.utils.BottomSheetBehaviorDrawerListener
import com.aralhub.araltaxi.request.utils.MapKitInitializer
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class RequestFragment : Fragment(R.layout.fragment_request) {
    private val binding by viewBinding(FragmentRequestBinding::bind)
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    @Inject
    lateinit var sheetNavigator: SheetNavigator
    @Inject
    lateinit var navigation: FeatureRequestNavigation
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.forEach { permission ->
                Log.i(
                    "RequestTaxiFragment",
                    "Permission: ${permission.key} is granted: ${permission.value}"
                )
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MapKitInitializer.init("eb911707-c4c6-4608-9917-f22012813a34", requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchPermissions()
        initViews()
        initListeners()
    }

    private fun launchPermissions() {
        locationPermissionLauncher.launch(requiredPermissions)
    }

    private fun initViews() {
        setUpMapView()
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
            when(it.itemId){
                R.id.action_support -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navigation.goToSupportFromRequestFragment()
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

    private fun setUpMapView() {
        binding.mapView.onStart()
        binding.mapView.mapWindow.map.move(
            CameraPosition(
                Point(42.4619, 59.6166),
                /* zoom = */ 17.0f,
                /* azimuth = */ 150.0f,
                /* tilt = */ 30.0f
            )
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