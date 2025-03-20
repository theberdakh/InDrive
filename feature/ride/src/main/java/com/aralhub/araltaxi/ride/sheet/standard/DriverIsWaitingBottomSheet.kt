package com.aralhub.araltaxi.ride.sheet.standard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aralhub.araltaxi.client.ride.R
import com.aralhub.araltaxi.client.ride.databinding.BottomSheetDriverIsWaitingBinding
import com.aralhub.araltaxi.ride.RideViewModel
import com.aralhub.araltaxi.ride.navigation.sheet.FeatureRideBottomSheetNavigation
import com.aralhub.araltaxi.ride.sheet.modal.CancelTripFragment
import com.aralhub.araltaxi.ride.sheet.modal.WaitingTimeFragment
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DriverIsWaitingBottomSheet : Fragment(R.layout.bottom_sheet_driver_is_waiting) {
    private val rideViewModel: RideViewModel by activityViewModels()
    @Inject lateinit var navigation: FeatureRideBottomSheetNavigation
    private val binding by viewBinding(BottomSheetDriverIsWaitingBinding::bind)
    private val cancelTripFragment = CancelTripFragment()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun showWaitingTimeBottomSheet() {
        WaitingTimeFragment().show(childFragmentManager, WaitingTimeFragment.TAG)
    }

    private fun initObservers() {

        /*
        observeState(rideViewModel.rideStateUiState){ rideStateUiState ->
            when(rideStateUiState){
                is RideStateUiState.Error -> {
                    Log.i("RideFragment", "initObservers: Loading")
                }
                RideStateUiState.Loading -> {
                    Log.i("RideFragment", "initObservers: Loading")
                }
                is RideStateUiState.Success -> {
                    when(rideStateUiState.rideState){
                        is RideStatus.DriverOnTheWay -> {}
                        is RideStatus.DriverWaitingClient -> {}
                        is RideStatus.PaidWaiting ->{}
                        is RideStatus.PaidWaitingStarted ->{}
                        is RideStatus.RideCompleted -> {}
                        is RideStatus.RideStarted -> {
                            Log.i("Navigation", "goToRide")
                            navigation.goToRide()
                        }
                        is RideStatus.Unknown -> {}
                    }
                }
            }
        }*/
    }

}