package com.aralhub.araltaxi.request.sheet.standard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.aralhub.araltaxi.client.request.R
import com.aralhub.araltaxi.client.request.databinding.BottomSheetSelectLocationBinding
import com.aralhub.ui.utils.viewBinding

class SelectLocationBottomSheet: Fragment(R.layout.bottom_sheet_select_location) {
    private val binding by viewBinding(BottomSheetSelectLocationBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.itemSelectLocation.ivIcon.setImageResource(com.aralhub.ui.R.drawable.ic_ic_round_pin_drop)
        binding.itemSelectLocation.tvTitle.text = "Aral Hub"
        binding.itemSelectLocation.tvSubtitle.text = "Aral Hub, 123 Main St"
    }
}