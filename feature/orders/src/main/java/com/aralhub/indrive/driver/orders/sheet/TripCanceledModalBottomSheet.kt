package com.aralhub.indrive.driver.orders.sheet

import com.aralhub.indrive.driver.orders.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TripCanceledModalBottomSheet :
    BottomSheetDialogFragment(R.layout.modal_bottom_sheet_trip_canceled) {
    companion object {
        const val TAG = "TripCanceledModalBottomSheet"
    }
}