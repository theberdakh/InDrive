package com.aralhub.araltaxi.ride.sheet.modal

import android.os.Bundle
import android.view.View
import com.aralhub.araltaxi.client.ride.R
import com.aralhub.araltaxi.client.ride.databinding.FragmentTripCancelledByDriverBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripCanceledByDriverFragment(private val onClearClick: () -> Unit): BottomSheetDialogFragment(R.layout.fragment_trip_cancelled_by_driver) {
    private val binding by viewBinding(FragmentTripCancelledByDriverBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.btnClear.setOnClickListener {
            onClearClick()
            dismissAllowingStateLoss()
        }
    }

    companion object {
        const val TAG = "TripCanceledByDriverFragment"
    }
}