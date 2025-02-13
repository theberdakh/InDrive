package com.aralhub.araltaxi.ride.sheet.modal

import android.os.Bundle
import android.view.View
import com.aralhub.araltaxi.client.ride.R
import com.aralhub.araltaxi.client.ride.databinding.FragmentReasonCancelBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReasonCancelFragment: BottomSheetDialogFragment(R.layout.fragment_reason_cancel){
    private val binding by viewBinding(FragmentReasonCancelBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSend.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    companion object {
        const val TAG = "ReasonCancelFragment"
    }
}