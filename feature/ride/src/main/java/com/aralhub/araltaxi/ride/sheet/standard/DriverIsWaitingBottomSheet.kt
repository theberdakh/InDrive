package com.aralhub.araltaxi.ride.sheet.standard

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aralhub.araltaxi.client.ride.R
import com.aralhub.araltaxi.client.ride.databinding.BottomSheetDriverIsWaitingBinding
import com.aralhub.araltaxi.ride.Ride
import com.aralhub.araltaxi.ride.RideBottomSheetUiState
import com.aralhub.araltaxi.ride.RideState
import com.aralhub.araltaxi.ride.RideStateUiState
import com.aralhub.araltaxi.ride.RideViewModel
import com.aralhub.araltaxi.ride.navigation.sheet.FeatureRideBottomSheetNavigation
import com.aralhub.araltaxi.ride.sheet.modal.CancelTripFragment
import com.aralhub.araltaxi.ride.sheet.modal.WaitingTimeFragment
import com.aralhub.araltaxi.ride.sheet.modal.cause.ReasonCancelFragment
import com.aralhub.araltaxi.ride.utils.FragmentEx.loadAvatar
import com.aralhub.araltaxi.ride.utils.FragmentEx.sendPhoneNumberToDial
import com.aralhub.indrive.core.data.model.ride.RideStatus
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.StringUtils
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
        observeState(rideViewModel.rideStateUiState) { rideStateUiState ->
            when(rideStateUiState){
                is RideStateUiState.Error -> {}
                RideStateUiState.Loading -> {}
                is RideStateUiState.Success -> {
                    when(rideStateUiState.rideState){
                        is RideStatus.CanceledByDriver -> {}
                        is RideStatus.DriverOnTheWay -> {}
                        is RideStatus.DriverWaitingClient -> {
                            navigation.goToRide()
                        }
                        is RideStatus.RideCompleted -> {}
                        is RideStatus.RideStarted -> {
                            navigation.goToRide()
                        }
                        is RideStatus.RideStartedAfterWaiting -> {
                            navigation.goToRide()
                        }
                        is RideStatus.Unknown -> {}
                    }
                }
            }
        }
    }

    private fun initRideData(rideData: Ride) {
        binding.tvTitle.text = getString(com.aralhub.ui.R.string.label_driver_is_waiting)
        loadAvatar(url = rideData.driver.avatar, binding.ivDriver)
        binding.tvDriverRating.text = getString(com.aralhub.ui.R.string.label_driver_rating, rideData.driver.rating)
        binding.tvDriverName.text = rideData.driver.name
        binding.tvCarInfo.text = StringUtils.getBoldSpanString(
            fullText = "${rideData.car.model}, ${rideData.car.number}",
            boldText = rideData.car.number
        )
        binding.btnCall.setOnClickListener {
            sendPhoneNumberToDial(rideData.driver.phone)
        }
        binding.btnCancel.setOnClickListener {
            cancelTripFragment.show(childFragmentManager, CancelTripFragment.TAG)
        }
        binding.layoutTime.setOnClickListener {
            showWaitingTimeBottomSheet()
        }
        cancelTripFragment.setOnCancelClickListener {
            Log.i("DriverIsWaitingBottomSheet", "showReasonCancel")
        }
    }
}