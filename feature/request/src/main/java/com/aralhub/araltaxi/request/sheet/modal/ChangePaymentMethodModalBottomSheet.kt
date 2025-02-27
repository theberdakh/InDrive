package com.aralhub.araltaxi.request.sheet.modal

import android.os.Bundle
import android.view.View
import com.aralhub.araltaxi.client.request.R
import com.aralhub.araltaxi.client.request.databinding.ModalBottomSheetChangePaymentMethodBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChangePaymentMethodModalBottomSheet: BottomSheetDialogFragment(R.layout.modal_bottom_sheet_change_payment_method) {
    private var _binding: ModalBottomSheetChangePaymentMethodBinding? = null
    private val binding get() = _binding!!

    private var onOnlineClickListener: (() -> Unit) = {}
    fun setOnOnlineClickListener(listener: () -> Unit) {
        onOnlineClickListener = listener
    }

    private var onCashClickListener: (() -> Unit) = {}
    fun setOnCashClickListener(listener: () -> Unit) {
        onCashClickListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ModalBottomSheetChangePaymentMethodBinding.bind(view)

        binding.layoutOnline.setOnClickListener {
            onOnlineClickListener.invoke()
        }

        binding.layoutCash.setOnClickListener {
            onCashClickListener.invoke()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ChangePaymentMethodModalBottomSheet"
    }
}