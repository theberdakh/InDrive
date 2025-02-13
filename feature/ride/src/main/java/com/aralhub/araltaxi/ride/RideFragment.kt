package com.aralhub.araltaxi.ride

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.aralhub.araltaxi.client.ride.R
import com.aralhub.araltaxi.client.ride.databinding.FragmentRideBinding
import com.aralhub.araltaxi.ride.navigation.sheet.FeatureRideBottomSheetNavigation
import com.aralhub.araltaxi.ride.navigation.sheet.SheetNavigator
import com.aralhub.ui.utils.viewBinding
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class RideFragment : Fragment(R.layout.fragment_ride) {
    private val binding by viewBinding(FragmentRideBinding::bind)
    @Inject
    lateinit var navigator: SheetNavigator
    @Inject
    lateinit var bottomSheetNavigation: FeatureRideBottomSheetNavigation
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMapView()
        initBottomNavController()
    }

    private fun initBottomNavController() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.ride_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        navController.let { navigator.bind(navController) }
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

    override fun onDestroyView() {
        navigator.unbind()
        super.onDestroyView()
    }

}