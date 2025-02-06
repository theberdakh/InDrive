package com.aralhub.indrive.waiting.modal

import android.os.Bundle
import android.view.View
import com.aralhub.indrive.waiting.R
import com.aralhub.indrive.waiting.databinding.FragmentCancelWaitingBinding
import com.aralhub.indrive.waiting.navigation.FeatureWaitingNavigation
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CancelWaitingFragment: BottomSheetDialogFragment(R.layout.fragment_cancel_waiting) {
    private val binding by viewBinding(FragmentCancelWaitingBinding::bind)
    companion object {
        const val TAG = "CancelWaitingFragment"
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