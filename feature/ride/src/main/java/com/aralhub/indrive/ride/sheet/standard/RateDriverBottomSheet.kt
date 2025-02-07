package com.aralhub.indrive.ride.sheet.standard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aralhub.indrive.ride.RideViewModel
import com.aralhub.indrive.ride.utils.FragmentEx.loadAvatar
import com.aralhub.indrive.waiting.R
import com.aralhub.indrive.waiting.databinding.BottomSheetRateDriverBinding
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RateDriverBottomSheet: Fragment(R.layout.bottom_sheet_rate_driver) {
    private val viewModel: RideViewModel by viewModels()
    private val binding: BottomSheetRateDriverBinding by viewBinding(BottomSheetRateDriverBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAvatar(url = "https://randomuser.me/api/portraits/women/3.jpg", binding.ivDriver)
        binding.btnSend.setOnClickListener {
           requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}