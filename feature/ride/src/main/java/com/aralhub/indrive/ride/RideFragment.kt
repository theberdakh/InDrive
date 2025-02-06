package com.aralhub.indrive.ride

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.aralhub.indrive.ride.navigation.FeatureWaitingNavigation
import com.aralhub.indrive.waiting.R
import com.aralhub.indrive.waiting.databinding.FragmentRideBinding
import com.aralhub.ui.utils.viewBinding
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RideFragment: Fragment(R.layout.fragment_ride) {
    private val binding by viewBinding(FragmentRideBinding::bind)
    @Inject
    lateinit var waitingNavigation: FeatureWaitingNavigation
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMapView()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true){
            waitingNavigation.goBackToHomeFragment()
        }

    }

    private fun initBottomNavController() {
       /* val navHostFragment = childFragmentManager.findFragmentById(R.id.offers_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        navController.let {
            sheetNavigator.bind(navController)
        }*/
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