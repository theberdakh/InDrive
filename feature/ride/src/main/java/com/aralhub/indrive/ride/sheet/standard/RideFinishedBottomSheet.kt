package com.aralhub.indrive.ride.sheet.standard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aralhub.indrive.ride.Ride
import com.aralhub.indrive.ride.RideBottomSheetUiState
import com.aralhub.indrive.ride.RideState
import com.aralhub.indrive.ride.RideViewModel
import com.aralhub.indrive.ride.navigation.sheet.FeatureRideBottomSheetNavigation
import com.aralhub.indrive.waiting.R
import com.aralhub.indrive.waiting.databinding.BottomSheetRideFinishedBinding
import com.aralhub.ui.utils.ViewEx.enable
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RideFinishedBottomSheet : Fragment(R.layout.bottom_sheet_ride_finished) {
    private val binding by viewBinding(BottomSheetRideFinishedBinding::bind)
    private val viewModel: RideViewModel by activityViewModels()
    @Inject
    lateinit var navigation: FeatureRideBottomSheetNavigation
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        binding.layoutPlasticCard.setOnClickListener {
            requireContext().copyToClipboard(binding.tvCardNumber.text)
        }
        binding.btnClear.setOnClickListener {
            navigation.goToRateDriverFromRideFinished()
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.rideState2.collect { state ->
                    when(state){
                        RideBottomSheetUiState.Error -> {}
                        RideBottomSheetUiState.Loading -> {}
                        is RideBottomSheetUiState.Success -> {
                            Log.i("RideFinishedBottomSheet", "initObservers: ${state.rideState}")
                            when(state.rideState){
                                RideState.FINISHED -> {
                                    Log.i("RideFinishedBottomSheet", "initObservers: ${state.rideState}")
                                    initRideData(state.rideData)
                                }
                                RideState.WAITING_FOR_DRIVER -> {}
                                RideState.DRIVER_IS_WAITING -> {}
                                RideState.DRIVER_CANCELED -> {}
                                RideState.IN_RIDE -> {}
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initRideData(rideData: Ride) {
        binding.tvTotalMoney.text = rideData.price
        binding.tvCardNumber.text = rideData.driver.cardNumber
    }

    private fun Context.copyToClipboard(text: CharSequence) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getString(com.aralhub.ui.R.string.label_card_number), text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(
            requireContext(),
            getString(com.aralhub.ui.R.string.message_card_number_is_copied),
            Toast.LENGTH_SHORT
        ).show()
        binding.btnClear.enable()
    }
}