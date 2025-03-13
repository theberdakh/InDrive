package com.aralhub.ui.sheets

import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.aralhub.ui.R

class LoadingModalBottomSheet: DialogFragment(R.layout.modal_bottom_sheet_loading) {
    override fun onStart() {
        super.onStart()
        dialog?.setCancelable(false)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    companion object {
        private const val TAG = "LoadingModalBottomSheet"
        private var instance: LoadingModalBottomSheet? = null
        fun show(fragmentManager: FragmentManager) {
            // Prevent multiple instances
            if (instance == null || instance?.dialog?.isShowing != true) {
                instance = LoadingModalBottomSheet()
                instance?.show(fragmentManager, TAG)
            }
        }


        fun hide(fragmentManager: FragmentManager) {
            instance?.let { dialog ->
                if (dialog.dialog?.isShowing == true) {
                    dialog.dismiss()
                }
            }
            // Clean up reference
            instance = null

            // Alternative approach using FragmentManager
            fragmentManager.executePendingTransactions()
            (fragmentManager.findFragmentByTag(TAG) as? LoadingModalBottomSheet)?.dismiss()
        }

        // Utility method to check if dialog is showing
        fun isShowing(): Boolean {
            return instance?.dialog?.isShowing == true
        }
    }
}