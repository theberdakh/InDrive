package com.aralhub.araltaxi.ride.sheet.modal

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.aralhub.araltaxi.client.ride.R
import com.aralhub.araltaxi.client.ride.databinding.FragmentWaitingTimeBinding
import com.aralhub.araltaxi.ride.GetStandardPriceUiState
import com.aralhub.araltaxi.ride.RideViewModel
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class WaitingTimeFragment: BottomSheetDialogFragment(R.layout.fragment_waiting_time) {
    private val binding by viewBinding(FragmentWaitingTimeBinding::bind)
    private val rideViewModel: RideViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListeners()
        initObservers()
        rideViewModel.getStandardPrice()
    }

    private fun initObservers() {
        observeState(rideViewModel.getStandardPriceUiState){ getStandardPriceUiState ->
            when(getStandardPriceUiState){
                is GetStandardPriceUiState.Error -> {
                    binding.tvTime.text = "Biypul kútiw waqtın alıwda qátelik júz berdi."
                }
                GetStandardPriceUiState.Loading -> {}
                is GetStandardPriceUiState.Success -> {
                    binding.tvTime.text = "Biypul kútiw waqtı ${getStandardPriceUiState.standardPrice.freeWaitMinutes} minut bolıp, bunnan keyingi hárbir minut ushın tólem ${getStandardPriceUiState.standardPrice.waitPricePerMinute} somdı quraydı."
                }
            }
        }
    }

    private fun setUpListeners() {
        binding.btnClear.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    companion object {
        const val TAG = "WaitingTimeFragment"
    }
}