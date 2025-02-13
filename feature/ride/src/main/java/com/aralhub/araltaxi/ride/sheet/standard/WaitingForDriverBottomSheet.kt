package com.aralhub.araltaxi.ride.sheet.standard

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aralhub.araltaxi.ride.Ride
import com.aralhub.araltaxi.ride.RideBottomSheetUiState
import com.aralhub.araltaxi.ride.RideState
import com.aralhub.araltaxi.ride.RideViewModel
import com.aralhub.araltaxi.ride.navigation.sheet.FeatureRideBottomSheetNavigation
import com.aralhub.araltaxi.ride.sheet.modal.CancelTripFragment
import com.aralhub.araltaxi.ride.utils.FragmentEx.loadAvatar
import com.aralhub.araltaxi.ride.utils.FragmentEx.sendPhoneNumberToDial
import com.aralhub.indrive.waiting.R
import com.aralhub.indrive.waiting.databinding.BottomSheetWaitingForDriverBinding
import com.aralhub.ui.utils.StringUtils
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WaitingForDriverBottomSheet : Fragment(R.layout.bottom_sheet_waiting_for_driver) {
    private val binding by viewBinding(BottomSheetWaitingForDriverBinding::bind)
    private val rideViewModel: RideViewModel by activityViewModels()

    @Inject
    lateinit var navigation: FeatureRideBottomSheetNavigation
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        Log.i("WaitingForDriver", "initObservers: called")
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                rideViewModel.rideState2.collect { state ->

                    when (state) {
                        RideBottomSheetUiState.Error -> {}
                        RideBottomSheetUiState.Loading -> {}
                        is RideBottomSheetUiState.Success -> {
                            Log.i("WaitingForDriver", "initObservers: ${state.rideState}")
                            when (state.rideState) {
                                RideState.WAITING_FOR_DRIVER -> { initRideData(state.rideData)}
                                RideState.DRIVER_IS_WAITING -> {
                                    navigation.goToDriverIsWaiting()
                                }

                                RideState.DRIVER_CANCELED -> {}
                                RideState.IN_RIDE -> {}
                                RideState.FINISHED -> {}
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initRideData(rideData: Ride) {
        binding.tvTitle.text = "Aydawshı ~${rideData.waitForDriverTime} minut ishinde jetip keledi"
        loadAvatar(url = rideData.driver.avatar, binding.ivDriver)
        binding.tvDriverRating.text =
            getString(com.aralhub.ui.R.string.label_driver_rating, rideData.driver.rating)
        binding.tvDriverName.text = rideData.driver.name
        binding.tvCarInfo.text = StringUtils.getBoldSpanString(
            fullText = "${rideData.car.model}, ${rideData.car.number}",
            boldText = rideData.car.number
        )
        binding.tvFromLocation.text = rideData.route.start
        binding.tvToLocation.text = rideData.route.end

        binding.btnCall.setOnClickListener {
            sendPhoneNumberToDial(rideData.driver.phone)
        }
        binding.btnCancel.setOnClickListener {
            CancelTripFragment().show(childFragmentManager, CancelTripFragment.TAG)
        }
    }

}