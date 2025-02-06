package com.aralhub.indrive.ride.modal

import android.os.Bundle
import android.view.View
import com.aralhub.indrive.ride.navigation.FeatureWaitingNavigation
import com.aralhub.indrive.waiting.R
import com.aralhub.indrive.waiting.databinding.FragmentCancelTripBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CancelTripFragment: BottomSheetDialogFragment(R.layout.fragment_cancel_trip) {
    private val binding by viewBinding(FragmentCancelTripBinding::bind)
    companion object {
        const val TAG = "CancelTripFragment"
    }
    @Inject
    lateinit var waitingNavigation: FeatureWaitingNavigation
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