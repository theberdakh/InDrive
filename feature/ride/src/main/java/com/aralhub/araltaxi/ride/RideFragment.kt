package com.aralhub.araltaxi.ride

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.client.ride.R
import com.aralhub.araltaxi.client.ride.databinding.FragmentRideBinding
import com.aralhub.araltaxi.ride.navigation.sheet.FeatureRideBottomSheetNavigation
import com.aralhub.araltaxi.ride.navigation.sheet.SheetNavigator
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.viewBinding
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class RideFragment : Fragment(R.layout.fragment_ride) {
    private val binding by viewBinding(FragmentRideBinding::bind)
    @Inject lateinit var navigator: SheetNavigator
    @Inject lateinit var bottomSheetNavigation: FeatureRideBottomSheetNavigation
    private val rideViewModel by activityViewModels<RideViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        setUpMapView()
        initBottomNavController()
        initObservers()
    }

    private fun initListeners() {
    }

    private fun initObservers() {
        observeState(rideViewModel.cancelRideState){ cancelRideUiState ->
            Log.i("RideFragment", "initObservers: $cancelRideUiState")
            when(cancelRideUiState){
                is CancelRideUiState.Error -> {}
                CancelRideUiState.Loading -> {}
                CancelRideUiState.Success -> {
                   findNavController().navigateUp()
                }
            }
        }
    }

    private fun initBottomNavController() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.ride_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        navController.let { navigator.bind(navController) }
    }

    private fun setUpMapView() {
        binding.mapView.onStart()
        binding.mapView.mapWindow.map.move(
            CameraPosition(
                Point(42.4619, 59.6166),
                17.0f,
               150.0f,
                30.0f
            )
        )
    }

    override fun onDestroyView() {
        navigator.unbind()
        super.onDestroyView()
    }

}