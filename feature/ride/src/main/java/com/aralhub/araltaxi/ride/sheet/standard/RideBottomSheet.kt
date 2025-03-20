package com.aralhub.araltaxi.ride.sheet.standard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aralhub.araltaxi.client.ride.R
import com.aralhub.araltaxi.client.ride.databinding.BottomSheetRideBinding
import com.aralhub.araltaxi.core.common.error.ErrorHandler
import com.aralhub.araltaxi.ride.RideViewModel
import com.aralhub.araltaxi.ride.navigation.sheet.FeatureRideBottomSheetNavigation
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RideBottomSheet : Fragment(R.layout.bottom_sheet_ride) {
    @Inject
    lateinit var navigation: FeatureRideBottomSheetNavigation
    private val rideViewModel: RideViewModel by activityViewModels()
    @Inject lateinit var errorHandler: ErrorHandler
    private val binding by viewBinding(BottomSheetRideBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        /*observeState(rideViewModel.rideStateUiState){ rideStateUiState ->
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
                        is RideStatus.RideCompleted -> {
                            navigation.goToRideFinished()
                        }
                        is RideStatus.RideStarted -> {}
                        is RideStatus.Unknown -> {}
                    }
                }
            }
        }*/
    }

}