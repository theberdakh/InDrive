package com.aralhub.indrive.ride.sheet.standard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aralhub.indrive.ride.navigation.sheet.FeatureRideBottomSheetNavigation
import com.aralhub.indrive.waiting.R
import com.aralhub.indrive.waiting.databinding.BottomSheetRideFinishedBinding
import com.aralhub.ui.utils.ViewEx.enable
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RideFinishedBottomSheet : Fragment(R.layout.bottom_sheet_ride_finished) {
    private val binding by viewBinding(BottomSheetRideFinishedBinding::bind)

    @Inject
    lateinit var navigation: FeatureRideBottomSheetNavigation
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutPlasticCard.setOnClickListener {
            requireContext().copyToClipboard(binding.tvCardNumber.text)
        }
        binding.btnClear.setOnClickListener {
            navigation.goToRateDriverFromRideFinished()
        }
    }

    private fun Context.copyToClipboard(text: CharSequence) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getString(com.aralhub.ui.R.string.label_card_number), text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(
            requireContext(),
            getString(com.aralhub.ui.R.string.message_card_number_is_copied),
            Toast.LENGTH_SHORT
        ).show()
        binding.btnClear.enable()
    }
}