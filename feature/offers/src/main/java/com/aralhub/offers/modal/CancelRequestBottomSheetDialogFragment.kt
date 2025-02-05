package com.aralhub.offers.modal

import android.os.Bundle
import android.view.View
import com.aralhub.offers.R
import com.aralhub.offers.databinding.FragmentCancelRequestBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CancelRequestBottomSheetDialogFragment: BottomSheetDialogFragment(R.layout.fragment_cancel_request) {
    private val binding by viewBinding(FragmentCancelRequestBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            dismiss()
        }
    }

}