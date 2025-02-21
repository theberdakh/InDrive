package com.aralhub.araltaxi.request.sheet.modal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.aralhub.araltaxi.client.request.R
import com.aralhub.araltaxi.client.request.databinding.ModalBottomSheetLogoutBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LogoutModalBottomSheet : BottomSheetDialogFragment(R.layout.modal_bottom_sheet_logout) {
    private val binding by viewBinding(ModalBottomSheetLogoutBinding::bind)

    private var onLogoutListener: (() -> Unit) = {}
    fun setOnLogoutListener(listener: () -> Unit) {
        onLogoutListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            onLogoutListener()
        }

        binding.btnBack.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    companion object {
        const val TAG = "LogoutModalBottomSheet"
        fun show(fragmentManager: FragmentManager) {
            LogoutModalBottomSheet().show(fragmentManager, TAG)
        }
    }
}