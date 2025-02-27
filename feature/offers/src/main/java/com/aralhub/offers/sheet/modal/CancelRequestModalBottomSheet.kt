package com.aralhub.offers.sheet.modal

import android.os.Bundle
import android.view.View
import com.aralhub.araltaxi.client.offers.R
import com.aralhub.araltaxi.client.offers.databinding.ModalBottomSheetCancelRequestBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CancelRequestModalBottomSheet: BottomSheetDialogFragment(R.layout.modal_bottom_sheet_cancel_request) {
    private val binding by viewBinding(ModalBottomSheetCancelRequestBinding::bind)
    private val reasonCancelModalBottomSheet by lazy { ReasonCancelModalBottomSheet() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCancel.setOnClickListener {
            reasonCancelModalBottomSheet.show(childFragmentManager, "reason_cancel")
        }
        binding.btnBack.setOnClickListener {
            dismiss()
        }
    }

}