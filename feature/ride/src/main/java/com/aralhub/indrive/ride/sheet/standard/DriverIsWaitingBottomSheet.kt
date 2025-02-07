package com.aralhub.indrive.ride.sheet.standard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aralhub.indrive.ride.Ride
import com.aralhub.indrive.ride.RideBottomSheetUiState
import com.aralhub.indrive.ride.RideViewModel
import com.aralhub.indrive.ride.sheet.modal.CancelTripFragment
import com.aralhub.indrive.ride.sheet.modal.WaitingTimeFragment
import com.aralhub.indrive.ride.utils.FragmentEx.loadAvatar
import com.aralhub.indrive.ride.utils.FragmentEx.sendPhoneNumberToDial
import com.aralhub.indrive.waiting.R
import com.aralhub.indrive.waiting.databinding.BottomSheetDriverIsWaitingBinding
import com.aralhub.ui.utils.StringUtils
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DriverIsWaitingBottomSheet : Fragment(R.layout.bottom_sheet_driver_is_waiting) {
    private val rideViewModel: RideViewModel by viewModels()
    private val binding by viewBinding(BottomSheetDriverIsWaitingBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun showWaitingTimeBottomSheet() {
        WaitingTimeFragment().show(childFragmentManager, WaitingTimeFragment.TAG)
    }

    private fun initObservers() {
        lifecycleScope.launch {
            rideViewModel.rideState.collect { state ->
                when (state) {
                    RideBottomSheetUiState.Error -> {}
                    RideBottomSheetUiState.Loading -> {}
                    is RideBottomSheetUiState.Success -> initRideData(state.rideData)
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
            CancelTripFragment().show(childFragmentManager, CancelTripFragment.TAG)
        }
        binding.layoutTime.setOnClickListener {
            showWaitingTimeBottomSheet()
        }
    }
}