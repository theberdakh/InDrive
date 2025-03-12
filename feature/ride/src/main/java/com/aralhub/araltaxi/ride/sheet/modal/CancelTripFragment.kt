package com.aralhub.araltaxi.ride.sheet.modal

import android.os.Bundle
import android.util.Log
import android.view.View
import com.aralhub.araltaxi.client.ride.R
import com.aralhub.araltaxi.client.ride.databinding.FragmentCancelTripBinding
import com.aralhub.araltaxi.ride.sheet.modal.cause.ReasonCancelFragment
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CancelTripFragment: BottomSheetDialogFragment(R.layout.fragment_cancel_trip) {
    private val binding by viewBinding(FragmentCancelTripBinding::bind)
    private val reasonCancelFragment = ReasonCancelFragment()
    private var onCancelClickListener: (() -> Unit) = {}
    fun setOnCancelClickListener(listener: () -> Unit) {
        onCancelClickListener = listener
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            dismissAllowingStateLoss()
        }
        binding.btnCancel.setOnClickListener {
            reasonCancelFragment.show(parentFragmentManager, ReasonCancelFragment.TAG)
            dismissAllowingStateLoss()
            Log.i(TAG, "onViewCreated: Cancel button clicked")
            onCancelClickListener()
        }

    }




    companion object {
        const val TAG = "CancelTripFragment"
    }
}