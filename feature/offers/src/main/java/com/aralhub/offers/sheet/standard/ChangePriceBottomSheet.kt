package com.aralhub.offers.sheet.standard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.aralhub.araltaxi.client.offers.R
import com.aralhub.araltaxi.client.offers.databinding.BottomSheetChangePriceBinding
import com.aralhub.ui.utils.MoneyFormatter
import com.aralhub.ui.utils.viewBinding

class ChangePriceBottomSheet: Fragment(R.layout.bottom_sheet_change_price) {
    private val binding by viewBinding(BottomSheetChangePriceBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MoneyFormatter(binding.etPrice)
    }
}