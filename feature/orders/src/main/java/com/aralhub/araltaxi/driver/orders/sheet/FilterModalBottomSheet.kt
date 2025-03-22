package com.aralhub.araltaxi.driver.orders.sheet

import android.os.Bundle
import android.view.View
import com.aralhub.araltaxi.core.common.sharedpreference.DriverSharedPreference
import com.aralhub.araltaxi.driver.orders.R
import com.aralhub.araltaxi.driver.orders.databinding.ModalBottomSheetFilterBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FilterModalBottomSheet : BottomSheetDialogFragment(R.layout.modal_bottom_sheet_filter) {
    private val binding by viewBinding(ModalBottomSheetFilterBinding::bind)

    @Inject
    lateinit var driverSharedPreference: DriverSharedPreference

    private var distance: Int = 3000

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.slider.setOnValueChangeListener { value ->
            binding.tvRadius.text = "$value km"
            driverSharedPreference.distance = (value * 1000).toInt()
        }
        binding.btnApply.setOnClickListener { dismissAllowingStateLoss() }
    }

    companion object {
        const val TAG = "FilterModalBottomSheet"
    }
}