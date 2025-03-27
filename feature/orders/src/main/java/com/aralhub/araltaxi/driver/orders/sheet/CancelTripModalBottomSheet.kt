package com.aralhub.araltaxi.driver.orders.sheet

import android.os.Build
import android.os.Bundle
import android.view.View
import com.aralhub.araltaxi.driver.orders.R
import com.aralhub.araltaxi.driver.orders.databinding.ModalBottomSheetCancelTripBinding
import com.aralhub.ui.model.OrderItem
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber

class CancelTripModalBottomSheet :
    BottomSheetDialogFragment(R.layout.modal_bottom_sheet_cancel_trip) {

    private val binding by viewBinding(ModalBottomSheetCancelTripBinding::bind)

    private var order: OrderItem? = null

    private var action: (order: OrderItem?) -> Unit = {}
    fun setOnRideCancelClickListener(action: (order: OrderItem?) -> Unit) {
        this.action = action
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        order = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("OrderDetail", OrderItem::class.java)
        } else {
            arguments?.getParcelable("OrderDetail")
        }

        setupListeners()

    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            dismissAllowingStateLoss()
        }
        binding.btnCancel.setOnClickListener {
            Timber.tag(TAG).e("setupListeners: $order")
            action.invoke(order)
        }
    }

    companion object {
        const val TAG = "CancelTripModalBottomSheet"
    }
}