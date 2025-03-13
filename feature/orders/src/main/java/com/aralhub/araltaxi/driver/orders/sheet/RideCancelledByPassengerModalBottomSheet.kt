package com.aralhub.araltaxi.driver.orders.sheet

import android.os.Bundle
import android.view.View
import com.aralhub.araltaxi.driver.orders.R
import com.aralhub.araltaxi.driver.orders.databinding.ModalBottomSheetRideCancelledByPassengerBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RideCancelledByPassengerModalBottomSheet :
    BottomSheetDialogFragment(R.layout.modal_bottom_sheet_ride_cancelled_by_passenger) {

    private val binding by viewBinding(ModalBottomSheetRideCancelledByPassengerBinding::bind)

    private var action: () -> Unit = {}
    fun setOnUnderstandClickListener(action: () -> Unit) {
        this.action = action
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        binding.btnUnderstand.setOnClickListener {
            action()
            dismissAllowingStateLoss()
        }
    }
}