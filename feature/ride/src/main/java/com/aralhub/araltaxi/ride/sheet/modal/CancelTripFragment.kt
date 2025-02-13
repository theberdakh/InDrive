package com.aralhub.araltaxi.ride.sheet.modal

import android.os.Bundle
import android.view.View
import com.aralhub.indrive.waiting.R
import com.aralhub.indrive.waiting.databinding.FragmentCancelTripBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CancelTripFragment: BottomSheetDialogFragment(R.layout.fragment_cancel_trip) {
    private val binding by viewBinding(FragmentCancelTripBinding::bind)
    companion object {
        const val TAG = "CancelTripFragment"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            dismissAllowingStateLoss()
        }
        binding.btnCancel.setOnClickListener {
            showReasonCancelFragment()
        }
    }

    private fun showReasonCancelFragment() {
        dismissAllowingStateLoss()
        ReasonCancelFragment().show(parentFragmentManager, ReasonCancelFragment.TAG)
    }
}