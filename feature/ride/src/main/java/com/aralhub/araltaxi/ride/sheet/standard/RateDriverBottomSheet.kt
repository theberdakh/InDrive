package com.aralhub.araltaxi.ride.sheet.standard

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aralhub.araltaxi.ride.RideBottomSheetUiState
import com.aralhub.araltaxi.ride.RideViewModel
import com.aralhub.araltaxi.ride.utils.FragmentEx.loadAvatar
import com.aralhub.indrive.waiting.R
import com.aralhub.indrive.waiting.databinding.BottomSheetRateDriverBinding
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RateDriverBottomSheet : Fragment(R.layout.bottom_sheet_rate_driver) {
    private val viewModel: RideViewModel by activityViewModels()
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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.rideState2.collect { state ->
                    when (state) {
                        is RideBottomSheetUiState.Success -> {
                            Log.i("RateDriverBottomSheet", "initObservers: ${state.rideState}")
                        }

                        RideBottomSheetUiState.Error -> {}
                        RideBottomSheetUiState.Loading -> {}
                    }
                }
            }
        }
    }
}