package com.aralhub.indrive.waiting.modal

import android.os.Bundle
import android.view.View
import com.aralhub.indrive.waiting.R
import com.aralhub.indrive.waiting.databinding.FragmentReasonCancelBinding
import com.aralhub.indrive.waiting.navigation.FeatureWaitingNavigation
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReasonCancelFragment: BottomSheetDialogFragment(R.layout.fragment_reason_cancel){
    @Inject
    lateinit var waitingNavigation: FeatureWaitingNavigation
    private val binding by viewBinding(FragmentReasonCancelBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSend.setOnClickListener {
            dismissAllowingStateLoss()
            waitingNavigation.goBackToHomeFragment()
        }
    }

    companion object {
        const val TAG = "ReasonCancelFragment"
    }
}