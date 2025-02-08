package com.aralhub.indrive.request

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.aralhub.indrive.request.databinding.FragmentRequestBinding
import com.aralhub.indrive.request.navigation.sheet.SheetNavigator
import com.aralhub.indrive.request.utils.MapKitInitializer
import com.aralhub.network.utils.NetworkMonitor
import com.aralhub.ui.components.crouton.Crouton
import com.aralhub.ui.utils.CroutonInDriveStyle
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class RequestFragment: Fragment(R.layout.fragment_request) {
    @Inject
    lateinit var networkMonitor: NetworkMonitor
    private val binding by viewBinding(FragmentRequestBinding::bind)
    private var bottomSheetBehavior: BottomSheetBehavior<View>? =null
    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    @Inject
    lateinit var sheetNavigator: SheetNavigator
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
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
        locationPermissionLauncher.launch(requiredPermissions)
        setUpMapView()
        setUpBottomSheet()
        setUpDrawerLayout()
        monitorNetworkConnection()
        initBottomNavController()
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

    private fun setUpDrawerLayout() {
        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                bottomSheetBehavior?.apply {
                    isHideable = true
                    state = BottomSheetBehavior.STATE_HIDDEN
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                bottomSheetBehavior?.apply {
                    isHideable = true
                    state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
            override fun onDrawerClosed(drawerView: View) {
                bottomSheetBehavior?.apply {
                    isHideable = false
                    state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })
    }

    private fun initBottomNavController() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.bottom_sheet_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        navController.let {
           sheetNavigator.bind(navController)
        }
    }


    private fun monitorNetworkConnection() {
        var isDisconnected = false
        lifecycleScope.launch {
            networkMonitor.isOnline.collect {
                if (!it) {
                    isDisconnected = true
                    Crouton.makeText(
                        requireActivity(),
                        com.aralhub.ui.R.string.error_network_connection,
                        CroutonInDriveStyle.errorStyle
                    ).show()
                } else if (isDisconnected) {
                    Crouton.makeText(
                        requireActivity(),
                        com.aralhub.ui.R.string.success_network_connection,
                        CroutonInDriveStyle.successStyle
                    ).show()
                }
            }
        }
    }

    override fun onDestroy() {
        sheetNavigator.unbind()
        super.onDestroy()
    }
}