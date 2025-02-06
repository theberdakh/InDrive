package com.aralhub.indrive.ride

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aralhub.indrive.waiting.R
import com.aralhub.indrive.waiting.databinding.BottomSheetRideFinishedBinding
import com.aralhub.ui.utils.viewBinding


class RideFinishedBottomSheet: Fragment(R.layout.bottom_sheet_ride_finished) {
    private val binding by viewBinding(BottomSheetRideFinishedBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutPlasticCard.setOnClickListener {
            requireContext().copyToClipboard(binding.tvCardNumber.text)
        }
        binding.btnClear.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun Context.copyToClipboard(text: CharSequence){
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Plastic card",text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Karta maǵlıwmatları nusqalandı!", Toast.LENGTH_SHORT).show()
        binding.btnClear.isEnabled = true
    }
}