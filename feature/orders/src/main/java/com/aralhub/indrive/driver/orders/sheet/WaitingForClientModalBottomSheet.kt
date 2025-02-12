package com.aralhub.indrive.driver.orders.sheet

import com.aralhub.indrive.driver.orders.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class WaitingForClientModalBottomSheet: BottomSheetDialogFragment(R.layout.modal_bottom_sheet_waiting_for_client) {
    companion object {
        const val TAG = "WaitingForClientModalBottomSheet"
    }
}