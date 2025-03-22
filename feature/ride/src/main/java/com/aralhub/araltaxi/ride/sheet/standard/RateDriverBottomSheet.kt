package com.aralhub.araltaxi.ride.sheet.standard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aralhub.araltaxi.client.ride.R
import com.aralhub.araltaxi.client.ride.databinding.BottomSheetRateDriverBinding
import com.aralhub.araltaxi.ride.ActiveRideUiState
import com.aralhub.araltaxi.ride.RideViewModel
import com.aralhub.araltaxi.ride.utils.FragmentEx.loadAvatar
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RateDriverBottomSheet : Fragment(R.layout.bottom_sheet_rate_driver) {
    private val rideViewModel: RideViewModel by activityViewModels()
    private val binding: BottomSheetRateDriverBinding by viewBinding(BottomSheetRateDriverBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        loadAvatar(url = "https://randomuser.me/api/portraits/women/3.jpg", binding.ivDriver)
        binding.btnSend.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initObservers() {
        observeState(rideViewModel.activeRideState){ activeRideUiState ->
            when(activeRideUiState){
                is ActiveRideUiState.Error -> {}
                ActiveRideUiState.Loading -> {}
                is ActiveRideUiState.Success -> {

                }
            }
        }
    }
}