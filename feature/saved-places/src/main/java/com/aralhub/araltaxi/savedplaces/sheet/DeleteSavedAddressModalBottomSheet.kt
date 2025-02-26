package com.aralhub.araltaxi.savedplaces.sheet

import android.os.Bundle
import android.view.View
import com.aralhub.araltaxi.saved_places.R
import com.aralhub.araltaxi.saved_places.databinding.ModalBottomSheetDeleteSavedAddressBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeleteSavedAddressModalBottomSheet: BottomSheetDialogFragment(R.layout.modal_bottom_sheet_delete_saved_address) {
    private var _binding : ModalBottomSheetDeleteSavedAddressBinding? = null
    private val binding get() = _binding!!

    private var onDeleteClickListener: (() -> Unit) = {}
    fun setOnDeleteClickListener(listener: () -> Unit) {
        onDeleteClickListener = listener
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ModalBottomSheetDeleteSavedAddressBinding.bind(view)

        binding.btnDelete.setOnClickListener {
            onDeleteClickListener()
        }

        binding.btnBack.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "DeleteSavedAddressModalBottomSheet"
    }
}