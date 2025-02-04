package com.aralhub.indrive.request

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.aralhub.indrive.request.databinding.FragmentMapBinding
import com.aralhub.ui.utils.viewBinding
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition

class MapFragment: Fragment(R.layout.fragment_map) {
    private val binding by viewBinding(FragmentMapBinding::bind)
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
        locationPermissionLauncher.launch(requiredPermissions)
        setUpMapView()
        val bottomSheetHostFragment = BottomSheetHostFragment()
      //  bottomSheetHostFragment.show(childFragmentManager, "BottomSheetInside")
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
}