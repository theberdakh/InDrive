package com.aralhub.ui.sheets

import android.os.Bundle
import android.view.View
import com.aralhub.ui.R
import com.aralhub.ui.adapter.location.LocationItemAdapter
import com.aralhub.ui.databinding.ModalBottomSheetChooseLocationBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseLocationModalBottomSheet: BottomSheetDialogFragment(R.layout.modal_bottom_sheet_choose_location) {
    private val binding by viewBinding(ModalBottomSheetChooseLocationBinding::bind)
    private val locationItemAdapter by lazy { LocationItemAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvAddresses.adapter = locationItemAdapter
    }

    companion object{
        const val TAG = "ChooseLocationModalBottomSheet"
    }
}