package com.aralhub.araltaxi.driver.orders.sheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.aralhub.araltaxi.driver.orders.R
import com.aralhub.araltaxi.driver.orders.databinding.ModalBottomSheetLogoutBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LogoutModalBottomSheet : BottomSheetDialogFragment(R.layout.modal_bottom_sheet_logout) {
    private val binding by viewBinding(ModalBottomSheetLogoutBinding::bind)

    private var _onLogoutListener: (() -> Unit) = {}
    fun setOnLogoutListener(listener: () -> Unit) {
        _onLogoutListener = listener
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            _onLogoutListener.invoke()
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