package com.aralhub.araltaxi.driver.orders.sheet

import android.os.Bundle
import android.view.View
import com.aralhub.araltaxi.driver.orders.R
import com.aralhub.araltaxi.driver.orders.databinding.ModalBottomSheetTripCanceledBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TripCanceledModalBottomSheet :
    BottomSheetDialogFragment(R.layout.modal_bottom_sheet_trip_canceled) {
    private val binding by viewBinding(ModalBottomSheetTripCanceledBinding::bind)

    private var onCloseListener: () -> Unit = {}
    fun setOnCloseListener(onBack: () -> Unit) {
        this.onCloseListener = onBack
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener {
            onCloseListener.invoke()
        }
    }
    companion object {
        const val TAG = "TripCanceledModalBottomSheet"
    }
}