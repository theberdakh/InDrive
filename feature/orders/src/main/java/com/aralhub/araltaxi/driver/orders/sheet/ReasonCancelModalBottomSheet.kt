package com.aralhub.araltaxi.driver.orders.sheet

import android.os.Bundle
import android.view.View
import com.aralhub.indrive.driver.orders.R
import com.aralhub.indrive.driver.orders.databinding.ModalBottomSheetReasonCancelBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReasonCancelModalBottomSheet :
    BottomSheetDialogFragment(R.layout.modal_bottom_sheet_reason_cancel) {
    private val binding by viewBinding(ModalBottomSheetReasonCancelBinding::bind)
    private var sendListener: () -> Unit = {}
    fun setSendReasonListener(listener: () -> Unit) {
        sendListener = listener
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        binding.btnSend.setOnClickListener {
            sendListener.invoke()
        }
    }

    companion object {
        const val TAG = "ReasonCancelModalBottomSheet"
    }
}