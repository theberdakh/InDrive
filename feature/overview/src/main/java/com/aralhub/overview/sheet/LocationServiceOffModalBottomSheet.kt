package com.aralhub.overview.sheet

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.aralhub.araltaxi.overview.R
import com.aralhub.araltaxi.overview.databinding.ModalBottomSheetLocationServiceOffBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LocationServiceOffModalBottomSheet :
    BottomSheetDialogFragment(R.layout.modal_bottom_sheet_location_service_off) {

    private val binding by viewBinding(ModalBottomSheetLocationServiceOffBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        binding.btnEnable.setOnClickListener {
            val intent = Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS
            )
            startActivity(
                intent
            )
        }
    }

    companion object {
        const val TAG = "LocationServiceOffModalBottomSheet"
    }
}