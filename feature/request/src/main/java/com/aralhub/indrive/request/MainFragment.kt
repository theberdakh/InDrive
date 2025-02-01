package com.aralhub.indrive.request

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.aralhub.indrive.request.databinding.FragmentMainBinding
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
class MainFragment : Fragment(R.layout.fragment_main) {
    @Inject
    lateinit var networkMonitor: NetworkMonitor
    private val binding by viewBinding(FragmentMainBinding::bind)
    private var bottomSheetBehavior = BottomSheetBehavior<View>()
    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
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

   /*     bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottom)
        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            // Ensure the bottom sheet sits above the keyboard
            isDraggable = true
            isFitToContents = true
            isShouldRemoveExpandedCorners = true
        }*/

        lifecycleScope.launch {
            monitorNetworkConnection()
        }
        //setUpDrawerLayout()
        setLightStatusBar()
        locationPermissionLauncher.launch(requiredPermissions)
        setUpMapView()

    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutBottom) { view, windowInsets ->
            val imeInsets = windowInsets.getInsets(WindowInsetsCompat.Type.ime())
            val navigationInsets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())

            // Update bottom padding to account for IME and navigation bar
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = if (windowInsets.isVisible(WindowInsetsCompat.Type.ime())) {
                    imeInsets.bottom
                } else {
                    navigationInsets.bottom
                }
            }

            windowInsets
        }
    }

    private fun setUpDrawerLayout() {

        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START) }
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                bottomSheetBehavior.isHideable = true
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            override fun onDrawerOpened(drawerView: View) {
                bottomSheetBehavior.isHideable = true
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            override fun onDrawerClosed(drawerView: View) {
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })
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

    private suspend fun monitorNetworkConnection() {
        var isDisconnected = false
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

    private fun setLightStatusBar() {
        requireActivity().enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(
            lightScrim = Color.WHITE,
            darkScrim = resources.getColor(com.aralhub.ui.R.color.color_interactive_control),
            detectDarkMode = { resources ->
                false
            }
        ))
    }
}