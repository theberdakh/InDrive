package com.aralhub.indrive.ride

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.aralhub.indrive.ride.navigation.sheet.FeatureRideBottomSheetNavigation
import com.aralhub.indrive.ride.navigation.sheet.SheetNavigator
import com.aralhub.indrive.ride.sheet.modal.TripCanceledByDriverFragment
import com.aralhub.indrive.waiting.R
import com.aralhub.indrive.waiting.databinding.FragmentRideBinding
import com.aralhub.ui.utils.viewBinding
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class RideFragment : Fragment(R.layout.fragment_ride) {
    private val rideViewModel: RideViewModel by viewModels()
    private val binding by viewBinding(FragmentRideBinding::bind)
    @Inject
    lateinit var sheetNavigator: SheetNavigator
    @Inject
    lateinit var bottomSheetNavigation: FeatureRideBottomSheetNavigation
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpMapView()
        initBottomNavController()
        initObservers()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            rideViewModel.rideState.collect {
                when (it) {
                    RideBottomSheetUiState.Error -> {}
                    RideBottomSheetUiState.Loading -> {}
                    is RideBottomSheetUiState.Success -> handleNavigation(it.rideState)
                }
            }
        }
    }

    private fun handleNavigation(rideState: RideState) {
        when(rideState){
            RideState.WAITING_FOR_DRIVER -> {}
            RideState.DRIVER_IS_WAITING -> bottomSheetNavigation.goToDriverIsWaiting()
            RideState.DRIVER_CANCELED -> { TripCanceledByDriverFragment().show(childFragmentManager, TripCanceledByDriverFragment.TAG) }
            RideState.IN_RIDE -> bottomSheetNavigation.goToRide()
            RideState.FINISHED -> bottomSheetNavigation.goToRideFinished()
        }
    }

    private fun initBottomNavController() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.ride_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        navController.let { sheetNavigator.bind(navController) }
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
        sheetNavigator.unbind()
        super.onDestroyView()
    }

}