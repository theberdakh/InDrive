package com.aralhub.araltaxi.ride.sheet.modal

import android.os.Bundle
import android.view.View
import com.aralhub.araltaxi.client.ride.R
import com.aralhub.araltaxi.client.ride.databinding.FragmentWaitingTimeBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class WaitingTimeFragment: BottomSheetDialogFragment(R.layout.fragment_waiting_time) {
    private val binding by viewBinding(FragmentWaitingTimeBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListeners()
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