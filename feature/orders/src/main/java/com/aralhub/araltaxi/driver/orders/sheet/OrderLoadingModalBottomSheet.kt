package com.aralhub.araltaxi.driver.orders.sheet

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.aralhub.indrive.driver.orders.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OrderLoadingModalBottomSheet: BottomSheetDialogFragment(R.layout.modal_bottom_sheet_order_loading) {
    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val offerAmount = arguments?.getString("OfferAmount")
        view.findViewById<TextView>(R.id.tv_offer_amount).text = offerAmount
    }

    companion object {
        const val TAG = "OrderLoadingModalBottomSheet"
    }
}