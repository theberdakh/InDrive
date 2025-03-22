package com.aralhub.ui.sheets

import android.os.Bundle
import android.view.View
import com.aralhub.ui.R
import com.aralhub.ui.databinding.ModalBottomSheetCommentToDriverBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentToDriverModalBottomSheet: BottomSheetDialogFragment(R.layout.modal_bottom_sheet_comment_to_driver) {
    private var _binding: ModalBottomSheetCommentToDriverBinding? = null
    private val binding get() = _binding!!

    private var onSaveCommentToDriver: ((String) -> Unit) = {}
    fun setOnSaveCommentToDriver(onSaveCommentToDriver: (String) -> Unit) {
        this.onSaveCommentToDriver = onSaveCommentToDriver
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ModalBottomSheetCommentToDriverBinding.bind(view)

        binding.btnSave.setOnClickListener {
            onSaveCommentToDriver.invoke(binding.etComment.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "CommentToDriverModalBottomSheet"
    }
}
